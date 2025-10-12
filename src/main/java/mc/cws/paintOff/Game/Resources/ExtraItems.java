package mc.cws.paintOff.Game.Resources;

import mc.cws.paintOff.Game.Extras.Colorer;
import mc.cws.paintOff.SecondaryWeapons.Vernebler;
import mc.cws.paintOff.Game.Management.Queue;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ExtraItems {
    public static void getItemArsenal(Player player) {
        ItemStack carrot = new ItemStack(Material.CHEST, 1);
        ItemMeta meta = carrot.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(ChatColor.YELLOW + "Arsenal");
            List<String> lore = new ArrayList<>();
            lore.add(ChatColor.GRAY + "Wähle deine Waffenkombination!");
            meta.setLore(lore);
            carrot.setItemMeta(meta);
        }
        player.getInventory().setItem(0, carrot);
    }


    public static void getHalloween(int n) {
        ItemStack carrot = new ItemStack(Material.JACK_O_LANTERN, 1);
        ItemMeta meta = carrot.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(ChatColor.DARK_PURPLE + "Gruselschuss");
            List<String> lore = new ArrayList<>();
            for (Player player : Queue.queuedPlayers.get(n)) {
                lore.add(ChatColor.GRAY + "Braucht: " + ChatColor.YELLOW + (UltPoints.getUltPoints(player)/2) + ChatColor.GRAY + " Ult-Punkte.");
            }
            lore.add(ChatColor.GRAY + "Größe: " + ChatColor.LIGHT_PURPLE + Vernebler.width + ChatColor.GRAY + " Blöcke");
            meta.setLore(lore);
            carrot.setItemMeta(meta);
        }
        for (Player player : Queue.queuedPlayers.get(n)) {
            player.getInventory().setItem(4, carrot);
        }
    }

    public static void getKills(Player player) {
        String color = Colorer.getTeamColor(player,false);
        // Check if inventory slot 9 exists and has an item
        ItemStack currentItem = player.getInventory().getItem(0);
        int amount = 0;
        if (currentItem != null && currentItem.getType() != Material.AIR) {
            if (currentItem.getType().equals(Material.REDSTONE_BLOCK)) {
                amount = currentItem.getAmount();
            } else {
                amount = currentItem.getAmount() + 1;
            }
        } else if (currentItem == null) {
            ItemStack itemKills = new ItemStack(Material.REDSTONE_BLOCK, 1);
            ItemMeta meta2 = itemKills.getItemMeta();
            Objects.requireNonNull(meta2).setDisplayName(color + "Keine Eliminierungen momentan!");

            List<String> lore = new ArrayList<>();
            lore.add(ChatColor.GRAY + "Nächstes rampage Level: " + color + "§o1" + ChatColor.GRAY + " bei " + color +" 4" + ChatColor.GRAY + " Kills!");
            meta2.setLore(lore);
            itemKills.setItemMeta(meta2);
            player.getInventory().setItem(0, itemKills);
            return;
        }

        ItemStack killsItem = getItemStack(amount, color);
        player.getInventory().setItem(0, killsItem);
    }

    private static ItemStack getItemStack(int amount, String color) {
        ItemStack killsItem = new ItemStack(Material.PUFFERFISH, amount);
        ItemMeta meta = killsItem.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(color + "Eliminierungen");
            List<String> lore = new ArrayList<>();
            lore.add(ChatColor.GRAY + "Erledigt: " + color + (amount -1));

            if ((amount -1) < 4) {
                lore.add(ChatColor.GRAY + "Nächstes rampage Level: " + color + "§o1" + ChatColor.GRAY + " bei " + color +" 4" + ChatColor.GRAY + " Eliminierungen!");
            } else if ((amount -1) < 8) {
                lore.add(ChatColor.GRAY + "Nächstes rampage Level: " + color + "§o2" + ChatColor.GRAY + " bei " + color +" 8" + ChatColor.GRAY + " Eliminierungen!");
            } else if ((amount -1) < 12) {
                lore.add(ChatColor.GRAY + "Nächstes rampage Level: " + color + "§o3" + ChatColor.GRAY + " bei " + color +" 12" + ChatColor.GRAY + " Eliminierungen!");
            } else if ((amount -1) < 16) {
                lore.add(ChatColor.GRAY + "Nächstes rampage Level: " + color + "§o4" + ChatColor.GRAY + " bei " + color +" 16" + ChatColor.GRAY + " Eliminierungen!");
            } else if ((amount -1) < 20) {
                lore.add(ChatColor.GRAY + "Nächstes rampage Level: " + color + "§o5" + ChatColor.GRAY + " bei " + color +" 20" + ChatColor.GRAY + " Eliminierungen!");
            } else {
                lore.add(color + "Keine weiteren Rampage Levels!");
            }

            meta.setLore(lore);
            killsItem.setItemMeta(meta);
        }
        return killsItem;
    }

    public static void getItemLeaving(Player player) {
        ItemStack carrot = new ItemStack(Material.BARRIER, 1);
        ItemMeta meta = carrot.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(ChatColor.RED + "Verlassen");
            carrot.setItemMeta(meta);
        }
        player.getInventory().setItem(8, carrot);
    }

    public static void getItemAuswahl(Player player) {
        ItemStack carrot = new ItemStack(Material.END_PORTAL_FRAME, 1);
        ItemMeta meta = carrot.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(ChatColor.LIGHT_PURPLE + "Arenen");
            List<String> lore = new ArrayList<>();
            lore.add(ChatColor.GRAY + "Vote auf eine Arena!");
            meta.setLore(lore);
            carrot.setItemMeta(meta);
        }
        player.getInventory().setItem(2, carrot);
    }
    public static void getShop(Player player) {
        ItemStack carrot = new ItemStack(Material.BARREL, 1);
        ItemMeta meta = carrot.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(ChatColor.LIGHT_PURPLE + "Shop");
            List<String> lore = new ArrayList<>();
            lore.add(ChatColor.GRAY + "Hohl dir dein Booster!");
            meta.setLore(lore);
            carrot.setItemMeta(meta);
        }
        player.getInventory().setItem(3, carrot);
    }
}
