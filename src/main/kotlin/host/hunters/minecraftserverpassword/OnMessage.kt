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

    @Suppress("UNCHECKED_CAST")
    private val whitelist : ArrayList<String> = plugin.config.getList("whitelistedUsers") as ArrayList<String>

    @Suppress("UNCHECKED_CAST")
    private val blacklist : ArrayList<String> = plugin.config.getList("blacklistedUsers") as ArrayList<String>

    @EventHandler
    fun onMessage(event : AsyncChatEvent) {
        @Suppress("UNCHECKED_CAST")
        val allPlayerAttemptsLeft = plugin.config.getMapList("allPlayerAttemptsLeft") as HashMap<String, Int>

        // Maybe error log?
        val playerAttemptsLeft = allPlayerAttemptsLeft[event.player.uniqueId.toString()] ?: return

        // Prevents messages from whitelisted users being sent to non-whitelisted users
        if (whitelist.contains(event.player.uniqueId.toString())) {
            for (p : Player in Bukkit.getOnlinePlayers()) {
                if (whitelist.contains(p.uniqueId.toString())) {
                    println("whitelisted")
                    continue
                }
                event.viewers().remove(p)
                println("${p.name} not whitelisted")
            }
        }

        // Prevents messages from non-whitelisted user from being sent to other users
        for (p : Player in Bukkit.getOnlinePlayers()) {
            event.viewers().remove(p)
        }

        if (event.message().asComponent() == Component.text(plugin.config.get("password").toString())) {

            whitelist.add(event.player.uniqueId.toString())
            plugin.config.set("whitelistedUsers", whitelist)
            plugin.saveConfig()

            if (whitelist.contains(event.player.uniqueId.toString())) {
                println("working")
                plugin.server.scheduler.runTask(plugin, Runnable {
                    run {
                        event.player.exp = 1000F
                        event.player.removePotionEffect(PotionEffectType.BLINDNESS)
                        event.player.gameMode = GameMode.SURVIVAL
                        event.player.walkSpeed = 0.2F
                    }
                })
            } else println("not working")

            event.player.sendMessage("You have been added to the whitelist.")
        }

        allPlayerAttemptsLeft[event.player.uniqueId.toString()] = playerAttemptsLeft - 1
        plugin.saveConfig()

        if (playerAttemptsLeft == 0) {
            blacklist.add(event.player.uniqueId.toString())
            plugin.config.set("blacklistedUsers", blacklist)
            plugin.saveConfig()
            plugin.server.scheduler.runTask(plugin, Runnable {
                run {
                    event.player.kick(Component.text("You have incorrectly attempted the password too many times and have been blacklisted."))
                }
            })
        }

        event.player.sendMessage("The password you entered is incorrect. You have $playerAttemptsLeft attempts left.")
    }
}