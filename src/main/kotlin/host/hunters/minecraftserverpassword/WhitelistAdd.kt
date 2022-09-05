package host.hunters.minecraftserverpassword

import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender

class WhitelistAdd(private val plugin : MinecraftServerPassword) : CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>?): Boolean {

        if (args?.size != 1) {
            sender.sendMessage("Invalid number of arguments provided.")
        }

        @Suppress("UNCHECKED_CAST")
        val whitelist : ArrayList<String> = plugin.config.getList("whitelistedUsers") as ArrayList<String>
        whitelist.add(Bukkit.getOfflinePlayer(args?.get(0).toString()).uniqueId.toString())
        plugin.config.set("whitelistedUsers", whitelist)
        plugin.saveConfig()

        return true
    }
}