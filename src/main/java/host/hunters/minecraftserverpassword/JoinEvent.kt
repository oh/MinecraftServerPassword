package host.hunters.minecraftserverpassword

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextColor
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent

class JoinEvent : Listener {
    @EventHandler
    fun onPlayerJoin(event: PlayerJoinEvent) {
        val p = event.player
        val text = Component.text()
                .content("")
                .color(TextColor.color(0xCBCD3D))
                .append(p.name())
                .append(Component.text(" has joined!"))
                .build()
        event.joinMessage(text)
    }
}