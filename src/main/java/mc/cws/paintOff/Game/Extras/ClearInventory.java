package mc.cws.paintOff.Game.Extras;

import org.bukkit.entity.Player;

import java.util.List;

public class ClearInventory {
    public static void invClear(List<Player> queue) {
        for (Player player : queue) {
            player.getInventory().clear(); // Clear the player's inventory
            player.getInventory().setHeldItemSlot(0); // Reset held item slot to 0
        }
    }
    public static void invClearPlayer(Player player) {
        player.getInventory().clear(); // Clear the player's inventory
        player.getInventory().setHeldItemSlot(0); // Reset held item slot to 0
    }
}
