package mc.cws.paintOff.Po.Executors;

import mc.cws.paintOff.Configuration;
import mc.cws.paintOff.Game.Management.DeadStop;
import mc.cws.paintOff.Game.Management.Start;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PoStop implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player player = (Player) sender;
        int n = Start.getQueueNumber(player);
        if (n != -1 && Start.gameRunning[n]) {
            player.sendMessage(ChatColor.DARK_GREEN + Configuration.name + " | " + ChatColor.GRAY + "Spiel wurde von einem Admin beendet!");
            DeadStop.stopGame(n);
        } else {
            player.sendMessage(ChatColor.RED + Configuration.name +  " | " + ChatColor.GRAY + "Du bist nicht in einem Spiel!");
        }
        return true;
    }
}
