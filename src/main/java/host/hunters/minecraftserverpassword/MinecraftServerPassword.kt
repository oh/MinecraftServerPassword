// Written by github.com/oh & github.com/insyri
//
// MinecraftServerPassword allows users to set a password for their server to disallow unknown users from moving,
// breaking blocks, or chatting. Once a user has entered the password once, they will be added to a whitelist and will
// not have to enter the password again upon joining the server.
//

package host.hunters.minecraftserverpassword

import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin

class MinecraftServerPassword : JavaPlugin() {
    override fun onEnable() {
        // Plugin startup logic
        logger.info("MinecraftServerPassword has loaded.")
        val pm = Bukkit.getServer().pluginManager
        pm.registerEvents(JoinEvent(), this)
    }

    override fun onDisable() {
        // Plugin shutdown logic
    }
}