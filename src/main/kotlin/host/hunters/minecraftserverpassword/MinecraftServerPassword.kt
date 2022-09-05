// Written by github.com/oh & github.com/insyri
//
// MinecraftServerPassword allows users to set a password for their server to disallow unknown users from moving,
// breaking blocks, or chatting. Once a user has entered the password once, they will be added to a whitelist and will
// not have to enter the password again upon joining the server.
//

package host.hunters.minecraftserverpassword

import org.bukkit.event.Listener
import org.bukkit.permissions.PermissionAttachment
import org.bukkit.plugin.PluginManager
import org.bukkit.plugin.java.JavaPlugin
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class MinecraftServerPassword : JavaPlugin(), Listener {
    private lateinit var pm : PluginManager
    var attachments : HashMap<UUID, PermissionAttachment> = HashMap()

    override fun onEnable() {
        // TODO: Refactor this..?
        if (config.getString("password") == null)        config.addDefault("password", "yourServerPassword")
        if (config.getString("allowedAttempts") == null) config.addDefault("allowedAttempts", 3)
        if (config.getList("whitelistedUsers") == null)  config.addDefault("whitelistedUsers", ArrayList<String>())
        if (config.getList("blacklistedUsers") == null)  config.addDefault("blacklistedUsers", ArrayList<String>())
        if (config.get("allPlayerAttemptsLeft") == null)   config.addDefault("allPlayerAttemptsLeft", HashMap<UUID, Int>())

        config.options().copyDefaults(true)
        saveConfig()

        logger.info("MinecraftServerPassword has loaded.")
        this.pm = server.pluginManager
        pm.registerEvents(JoinEvent(this), this)
        pm.registerEvents(OnMessage(this), this)

        getCommand("setpassword")?.setExecutor(SetPassword(this))
        getCommand("whitelistadd")?.setExecutor(WhitelistAdd(this))
    }

    override fun onDisable() {
        // Plugin shutdown logic
    }

}