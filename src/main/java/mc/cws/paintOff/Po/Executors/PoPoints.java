package mc.cws.paintOff.Po.Executors;

import mc.cws.paintOff.Configuration;
import mc.cws.paintOff.Game.Points.Points;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PoPoints implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            if (command.getName().equalsIgnoreCase(Configuration.mainCommand+"points")) {
                Player player = (Player) sender;
                double playerPoints = Points.getPoints(player);
                player.sendMessage(ChatColor.GOLD + Configuration.name + " | " + ChatColor.GRAY + "Du hast "+ ChatColor.GREEN + playerPoints + ChatColor.GRAY + " Punkte!");
                return true;
            }
        }
        return false;
    }
}
