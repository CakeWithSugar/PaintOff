package mc.cws.paintOff.Po.Executors;

import mc.cws.paintOff.Configuration;
import mc.cws.paintOff.Game.Management.Queue;
import mc.cws.paintOff.Game.Management.Start;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class PoStart implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player player) {
            int n = Start.getQueueNumber(player);
            // Check if the player is in any queue
            if (n != -1) {
                if (Queue.queuedPlayers.containsKey(n)) {
                    if (!Queue.lokStartBlock[n]) {
                        List<Player> queue = Queue.queuedPlayers.get(n);
                        if (queue != null && !queue.isEmpty()) {
                            if (queue.contains(player) && !Start.gameRunning[n]) {
                                Start.startGame(n);
                            } else {
                                player.sendMessage(ChatColor.RED + Configuration.name + " | " + ChatColor.GRAY + "Spiel ist schon gestartet!");
                            }
                            return true;
                        } else {
                            player.sendMessage(ChatColor.RED + Configuration.name + " | " + ChatColor.GRAY + "Queue ist leer!");
                            return true;
                        }
                    } else {
                        player.sendMessage(ChatColor.RED + Configuration.name + " | " + ChatColor.GRAY + "Countdown wurde bereits gestartet!");
                        return true;
                    }
                } else if (Start.gameRunning[n]) {
                    player.sendMessage(ChatColor.RED + Configuration.name + " | " + ChatColor.GRAY + "Es läuft bereits ein Spiel!");
                    return true;
                }
            }

            player.sendMessage(ChatColor.RED + Configuration.name + " | " + ChatColor.GRAY + "Du bist nicht in einer Queue!");
            return true;
        }
        return false;
    }
}
