package mc.cws.paintOff.Po.Executors;

import mc.cws.paintOff.Configuration;
import mc.cws.paintOff.Game.Management.Queue;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class PoQueue implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player player) {
            int n = -1;
            boolean isInQueue = false;
            for (int i = 0; i < Configuration.maxQueues; i++) {
                if (Queue.queuedPlayers.containsKey(i)) {
                    List<Player> queue = Queue.queuedPlayers.get(i);
                    if (queue != null && queue.contains(player)) {
                        isInQueue = true;
                        n = i;
                        break;
                    }
                }
            }
            
            if (isInQueue) {
                sender.sendMessage(ChatColor.RED + Configuration.name + " | " + ChatColor.GRAY + "Du bist bereits in einer Queue!");
                return true;
            }
            for (int i = 0; i < Configuration.maxQueues; i++) {
                if (!Queue.queueBlock[i]) {
                    n = i;
                    break;
                }
            }
            if (n == -1) {
                sender.sendMessage(ChatColor.RED + Configuration.name + " | " + ChatColor.GRAY + "Keine freie Queue verfügbar!");
                return true;
            }
            if (Queue.queueBlock[n]) {
                sender.sendMessage(ChatColor.RED + Configuration.name + " | " + ChatColor.GRAY + "Die Queue ist aktuell blockiert!");
                return true;
            }
            Queue.queueGame(player);
            return true;
        }
        return true;
    }
}
