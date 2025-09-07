package mc.cws.paintOff.Game.Items;

import mc.cws.paintOff.Configuration;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class TeleportPad {
    public static void getItem(Player player) {

        ItemStack carrot = new ItemStack(Material.ECHO_SHARD, 1);
        ItemMeta meta = carrot.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(ChatColor.DARK_PURPLE + "Menu");
            List<String> lore = new ArrayList<>();
            lore.add(ChatColor.GRAY + "Teleportiere dich nach " + ChatColor.LIGHT_PURPLE + Configuration.teleportTime + ChatColor.GRAY + " Sekunden!");
            meta.setLore(lore);
            carrot.setItemMeta(meta);
        }
        player.getInventory().setItem(8, carrot);
    }
}
