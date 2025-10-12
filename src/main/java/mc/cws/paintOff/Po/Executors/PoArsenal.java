package mc.cws.paintOff.Po.Executors;

import mc.cws.paintOff.Configuration;
import mc.cws.paintOff.Listener.ArsenalInventoryListener;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PoArsenal implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player player = (Player) sender;
        if (player != null) {
            if (command.getName().equalsIgnoreCase(Configuration.mainCommand+"arsenal")) {
                player.openInventory(ArsenalInventoryListener.ARSENAL);
            }
        }
        return true;
    }
}
