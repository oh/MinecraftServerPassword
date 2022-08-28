package host.hunters.minecraftserverpassword

import net.kyori.adventure.text.Component
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent

class JoinEvent(private val plugin: MinecraftServerPassword) : Listener {
    @EventHandler
    fun onPlayerJoin(event: PlayerJoinEvent) {
        val p = event.player

        @Suppress("UNCHECKED_CAST")
        val blacklist : ArrayList<String> = plugin.config.getList("blacklistedUsers") as ArrayList<String>
        for (user in blacklist) {
            if (user == p.uniqueId.toString()) p.kick(Component.text("You are blacklisted."))
        }

        @Suppress("UNCHECKED_CAST")
        val whitelist : ArrayList<String> = plugin.config.getList("whitelistedUsers") as ArrayList<String>
        for (user in whitelist) {
            if (user == p.uniqueId.toString()) return
        }

        p.sendMessage("You are not on the whitelist. Send a message with the server password to be added to the whitelist.")
    }
}