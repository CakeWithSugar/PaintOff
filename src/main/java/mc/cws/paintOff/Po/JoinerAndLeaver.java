package mc.cws.paintOff.Po;

import mc.cws.paintOff.Configuration;
import mc.cws.paintOff.Game.Arena.Arena;
import mc.cws.paintOff.Game.Management.Queue;
import mc.cws.paintOff.Game.Management.Start;
import mc.cws.paintOff.Po.Executors.PoLeave;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class JoinerAndLeaver implements Listener {
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        int n = Start.getQueueNumber(player);
        if (n == -1) {
            Queue.queueGame(player);
        } else {
            Arena.portLobby(player);
            player.sendMessage(ChatColor.RED + Configuration.name + " | " + ChatColor.GRAY + "Fehler beim entfernen des Spielers!");
        }
    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        PoLeave.leaveGame(player);
    }
}
