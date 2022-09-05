package host.hunters.minecraftserverpassword

import io.papermc.paper.event.player.AsyncChatEvent
import net.kyori.adventure.text.Component
import org.bukkit.Bukkit
import org.bukkit.GameMode
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.potion.PotionEffectType
import java.util.ArrayList

class OnMessage(private val plugin : MinecraftServerPassword) : Listener {

    private var attempts : Int = plugin.config.get("allowedAttempts") as Int

    @Suppress("UNCHECKED_CAST")
    private val whitelist : ArrayList<String> = plugin.config.getList("whitelistedUsers") as ArrayList<String>

    @Suppress("UNCHECKED_CAST")
    private val blacklist : ArrayList<String> = plugin.config.getList("blacklistedUsers") as ArrayList<String>

    @EventHandler
    fun onMessage(event : AsyncChatEvent) {

        if (whitelist.contains(event.player.uniqueId.toString())) return

        for (p : Player in Bukkit.getOnlinePlayers()) {
            event.viewers().remove(p)
        }

        if (event.message().asComponent() == Component.text(plugin.config.get("password").toString())) {

            whitelist.add(event.player.uniqueId.toString())
            plugin.config.set("whitelistedUsers", whitelist)
            plugin.saveConfig()

            if (event.player.hasPermission("serverpassword.whitelisted")) {
                println("working")
                plugin.server.scheduler.runTaskLater(plugin, Runnable {
                    run {
                        event.player.removePotionEffect(PotionEffectType.BLINDNESS)
                        event.player.gameMode = GameMode.SURVIVAL
                        event.player.walkSpeed = 0.2F
                    }
                }, 1L)
            } else println("not working")

            event.player.sendMessage("You have been added to the whitelist.")
        }

        attempts--

        if (attempts >= 0) {
            blacklist.add(event.player.uniqueId.toString())
            plugin.config.set("blacklistedUsers", blacklist)
            plugin.saveConfig()
            plugin.server.scheduler.runTaskLater(plugin, Runnable {
                run {
                    event.player.kick(Component.text("You have incorrectly attempted the password too many times and have been blacklisted."))
                }
            }, 1L)
        }

        event.player.sendMessage("The password you entered is incorrect. You have $attempts attempts left.")
    }
}