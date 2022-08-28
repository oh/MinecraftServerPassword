// Written by github.com/oh & github.com/insyri
//
// MinecraftServerPassword allows users to set a password for their server to disallow unknown users from moving,
// breaking blocks, or chatting. Once a user has entered the password once, they will be added to a whitelist and will
// not have to enter the password again upon joining the server.
//

package host.hunters.minecraftserverpassword

import org.bukkit.Bukkit
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.event.Listener
import org.bukkit.plugin.PluginManager
import org.bukkit.plugin.java.JavaPlugin

class MinecraftServerPassword : JavaPlugin(), Listener {
    private lateinit var pm : PluginManager

    override fun onEnable() {
        val config : FileConfiguration = config
        if (config.getString("password") == null) {
            config.addDefault("password", "yourServerPassword")
        }
        if (config.getString("allowedAttempts") == null) {
            config.addDefault("allowedAttempts", 3)
        }
        if (config.getList("whitelistedUsers") == null) {
            val list : ArrayList<String> = ArrayList()
            config.addDefault("whitelistedUsers", list)
        }
        if (config.getList("blacklistedUsers") == null) {
            val list : ArrayList<String> = ArrayList()
            config.addDefault("blacklistedUsers", list)
        }

        config.options().copyDefaults(true)
        saveConfig()

        logger.info("MinecraftServerPassword has loaded.")
        this.pm = Bukkit.getServer().pluginManager
        pm.registerEvents(JoinEvent(this), this)
        pm.registerEvents(OnMessage(this), this)

        getCommand("setpassword")?.setExecutor(SetPassword(this))
        getCommand("whitelistadd")?.setExecutor(WhitelistAdd(this))
    }

    override fun onDisable() {
        // Plugin shutdown logic
    }

}