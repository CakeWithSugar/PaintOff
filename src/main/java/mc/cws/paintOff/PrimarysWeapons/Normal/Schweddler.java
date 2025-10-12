package mc.cws.paintOff.PrimarysWeapons.Normal;

import mc.cws.paintOff.Game.Management.InGame.Game;
import mc.cws.paintOff.Game.Resources.FulePoints;
import mc.cws.paintOff.Game.Resources.UltPoints;
import mc.cws.paintOff.Game.Management.InGame.SnowballEffect;
import mc.cws.paintOff.Game.Management.Start;
import mc.cws.paintOff.PaintOffMain;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.ArrayList;
import java.util.List;

public class Schweddler {
    private static PaintOffMain plugin;
    public static void setPlugin(PaintOffMain plugin) {Schweddler.plugin = plugin;}

    public static int requiredPoints = (UltPoints.generalPoints + 60);
    public static String reichweite = "14m";
    public static int verbrauch = 14;
    public static int kugeln = 2;
    public static int damage = 4;
    public static int slot = 13;
    public static int coolDown = 2;
    public static double coolDownCounter = coolDown/4.0;

    public static void getItem(Player player) {
        ItemStack carrot = new ItemStack(Material.IRON_SWORD, 1);
        ItemMeta meta = carrot.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(ChatColor.DARK_PURPLE + "Schweddler");
            List<String> lore = new ArrayList<>();
            lore.add(ChatColor.GRAY + "Braucht: " + ChatColor.YELLOW + requiredPoints + ChatColor.GRAY + " Ult-Punkte.");
            lore.add("");
            lore.add(ChatColor.DARK_PURPLE + "Reichweite: " + ChatColor.LIGHT_PURPLE + ChatColor.BOLD + reichweite);
            lore.add(ChatColor.DARK_PURPLE + "Verbrauch: " + ChatColor.LIGHT_PURPLE + ChatColor.BOLD + verbrauch + " Farbe/Schuss");
            lore.add(ChatColor.DARK_PURPLE + "max. Schaden: " + ChatColor.LIGHT_PURPLE + ChatColor.BOLD + damage + " / Schuss");
            lore.add(ChatColor.DARK_PURPLE + "Färberate: " + ChatColor.LIGHT_PURPLE + ChatColor.BOLD + kugeln + " Kugeln");
            lore.add(ChatColor.DARK_PURPLE + "Cool-Down: " + ChatColor.LIGHT_PURPLE + ChatColor.BOLD + coolDownCounter + " Sekunden");
            meta.setLore(lore);
            carrot.setItemMeta(meta);
        }
        player.getInventory().setItem(5, carrot);
    }
    public static ItemStack arsenalDisplay() {
        ItemStack paintball = new ItemStack(Material.IRON_SWORD, 1);
        ItemMeta meta = paintball.getItemMeta();
        meta.setDisplayName(ChatColor.YELLOW + "Schweddler");
        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.GRAY + "Hiermit sind Gebiete im nuh gefärbt!");
        lore.add(ChatColor.GRAY + "Braucht: " + ChatColor.YELLOW + requiredPoints + ChatColor.GRAY + " Ult-Punkte.");
        lore.add("");
        lore.add(ChatColor.DARK_PURPLE + "Reichweite: " + ChatColor.LIGHT_PURPLE + ChatColor.BOLD + reichweite);
        lore.add(ChatColor.DARK_PURPLE + "Verbrauch: " + ChatColor.LIGHT_PURPLE + ChatColor.BOLD + verbrauch + " Farbe/Schuss");
        lore.add(ChatColor.DARK_PURPLE + "max. Schaden: " + ChatColor.LIGHT_PURPLE + ChatColor.BOLD + damage + " / Schuss");
        lore.add(ChatColor.DARK_PURPLE + "Färberate: " + ChatColor.LIGHT_PURPLE + ChatColor.BOLD + kugeln + " Kugeln");
        lore.add(ChatColor.DARK_PURPLE + "Cool-Down: " + ChatColor.LIGHT_PURPLE + ChatColor.BOLD + coolDownCounter + " Sekunden");
        lore.add("");
        lore.add(ChatColor.YELLOW + "Sekundärwaffe: " + ChatColor.LIGHT_PURPLE + "Klotzbombe");
        lore.add(ChatColor.YELLOW + "Spezialwaffe: " + ChatColor.LIGHT_PURPLE + "Gammablitzer");
        meta.setLore(lore);
        paintball.setItemMeta(meta);
        return paintball;
    }

    public static void shoot(Player player) {
        if (FulePoints.isAbleToPerform(player, verbrauch)) {
            // Play custom sound
            player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_HAT, 0.1f, 2.0f);

            // Erstelle den Haupt-Schneeball
            Snowball mainSnowball2 = player.launchProjectile(Snowball.class);
            mainSnowball2.setVelocity(player.getLocation().getDirection().multiply(1.2)); // 9m
            mainSnowball2.setMetadata("spawnedBy", new FixedMetadataValue(plugin, "Common")); // 2 oder 3
            SnowballEffect.sin(mainSnowball2, player, Start.getQueueNumber(player));
            mainSnowball2.setGravity(false);
            Bukkit.getScheduler().runTaskLater(plugin, mainSnowball2::remove, 12);


            // Erstelle den Haupt-Schneeball
            Snowball mainSnowball3 = player.launchProjectile(Snowball.class);
            mainSnowball3.setVelocity(player.getLocation().getDirection().multiply(1.2)); // 9m
            mainSnowball3.setGravity(false);
            mainSnowball3.setMetadata("spawnedBy", new FixedMetadataValue(plugin, "Common")); // 2 oder 3
            SnowballEffect.dub(mainSnowball3, player, Start.getQueueNumber(player));
            Bukkit.getScheduler().runTaskLater(plugin, mainSnowball3::remove, 6);

            // Handle fuel consumption
            FulePoints.removeFulePoints(player,verbrauch);
            Game.setCooldown(player, coolDown);
        }
    }
}
