package mc.cws.paintOff.Po.Executors;

import mc.cws.paintOff.Configuration;
import mc.cws.paintOff.Po.Modifications;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PoMenu implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        openMenu((Player) sender, command);
        return true;
    }

    public static void openMenu(Player player, Command command) {
        if (player != null) {
            if (command.getName().equalsIgnoreCase(Configuration.mainCommand+"menu")) {
                player.openInventory(Modifications.Menu);
            }
        }
    }
}
