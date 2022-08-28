package host.hunters.minecraftserverpassword

import io.papermc.paper.event.player.AsyncChatEvent
import net.kyori.adventure.text.Component
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import java.util.ArrayList

class OnMessage(private val plugin : MinecraftServerPassword) : Listener {

    private var attempts : Int = plugin.config.get("allowedAttempts") as Int

    @Suppress("UNCHECKED_CAST")
    private val whitelist : ArrayList<String> = plugin.config.getList("whitelistedUsers") as ArrayList<String>

    @EventHandler(ignoreCancelled = true)
    fun onMessage(event : AsyncChatEvent): Boolean {

        val p = event.player

        if (whitelist.contains(p.uniqueId.toString())) {
            return true
        }

        if (event.message().asComponent() == Component.text(plugin.config.get("password") as String)) {

            @Suppress("UNCHECKED_CAST")
            val whitelist : ArrayList<String> = plugin.config.getList("whitelistedUsers") as ArrayList<String>
            whitelist.add(p.uniqueId.toString())
            plugin.config.set("whitelistedUsers", whitelist)
            plugin.saveConfig()

            p.sendMessage("You have been added to the whitelist.")

            return false
        }

        if (attempts == 0) {

            @Suppress("UNCHECKED_CAST")
            val blacklist : ArrayList<String> = plugin.config.getList("blacklistedUsers") as ArrayList<String>
            blacklist.add(p.uniqueId.toString())
            plugin.config.set("blacklistedUsers", blacklist)
            plugin.saveConfig()
            p.kick(Component.text("You have incorrectly attempted the password too many times and have been blacklisted."))

            return false
        }

        p.sendMessage("The password you entered is incorrect. You have $attempts attempts left.")
        attempts--

        return false
    }
}