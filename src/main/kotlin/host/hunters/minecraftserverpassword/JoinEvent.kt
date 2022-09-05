package host.hunters.minecraftserverpassword

import net.kyori.adventure.text.Component
import org.bukkit.Bukkit
import org.bukkit.GameMode
import org.bukkit.command.CommandException
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerMoveEvent
import org.bukkit.permissions.Permission
import org.bukkit.permissions.PermissionAttachment
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

class JoinEvent(private val plugin: MinecraftServerPassword) : Listener {
    @EventHandler
    fun onPlayerJoin(event: PlayerJoinEvent) {
        val p = event.player

        @Suppress("UNCHECKED_CAST")
        val allPlayerAttemptsLeft = plugin.config.getMapList("allPlayerAttemptsLeft") as HashMap<String, Int>

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

        // Finally found user is not on whitelist or blacklist

        allPlayerAttemptsLeft[event.player.uniqueId.toString()] = plugin.config.get("allowedAttempts") as Int
        plugin.saveConfig()

        p.gameMode = GameMode.ADVENTURE
        p.addPotionEffect(PotionEffect(PotionEffectType.BLINDNESS, Int.MAX_VALUE, 1))
        p.walkSpeed = 0F

        p.sendMessage("You are not on the whitelist. Send a message with the server password to be added to the whitelist.")
    }

    @EventHandler
    fun onMovement(event: PlayerMoveEvent) {
        if (event.player.hasPermission("serverpassword.whitelisted")) return
        event.isCancelled = true
    }
}
