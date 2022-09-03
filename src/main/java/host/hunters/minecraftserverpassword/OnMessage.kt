package host.hunters.minecraftserverpassword

import io.papermc.paper.event.player.AsyncChatEvent
import net.kyori.adventure.text.Component
import org.bukkit.GameMode
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

        val p = event.player

        if (whitelist.contains(p.uniqueId.toString())) return

        if (event.message().asComponent() == Component.text(plugin.config.get("password") as String)) {

            whitelist.add(p.uniqueId.toString())
            plugin.config.set("whitelistedUsers", whitelist)
            plugin.saveConfig()

            if (p.hasPermission("serverpassword.whitelisted")) {
                println("working")
                plugin.server.scheduler.runTaskLater(plugin, Runnable {
                    run {
                        p.removePotionEffect(PotionEffectType.BLINDNESS)
                        p.gameMode = GameMode.SURVIVAL
                        p.walkSpeed = 0.2F
                    }
                }, 1L)
            } else {
                println("not working")
            }

            p.sendMessage("You have been added to the whitelist.")

            return
        }

        attempts--

        if (attempts >= 0) {
            blacklist.add(p.uniqueId.toString())
            plugin.config.set("blacklistedUsers", blacklist)
            plugin.saveConfig()
            plugin.server.scheduler.runTaskLater(plugin, Runnable {
                run {
                    p.kick(Component.text("You have incorrectly attempted the password too many times and have been blacklisted."))
                }
            }, 1L)

            return
        }

        p.sendMessage("The password you entered is incorrect. You have $attempts attempts left.")

        return
    }
}