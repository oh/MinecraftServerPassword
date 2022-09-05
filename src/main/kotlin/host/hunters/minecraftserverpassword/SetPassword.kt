package host.hunters.minecraftserverpassword

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextColor
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender

class SetPassword(private val plugin : MinecraftServerPassword) : CommandExecutor {

    @Override
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>?): Boolean {

        if (args?.size != 1) {
            sender.sendMessage("Invalid number of arguments provided.")

            return false
        }

        plugin.config.set("password", args[0])
        plugin.saveConfig()

        val text = Component.text()
                .color(TextColor.color(0xCBCD3D))
                .append(Component.text("The new server password has been set to " + args[0]))
                .build()
        sender.sendMessage(text)

        return true
    }
}