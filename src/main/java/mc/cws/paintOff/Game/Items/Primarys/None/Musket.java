package mc.cws.paintOff.Game.Items.Primarys.None;

import mc.cws.paintOff.Game.Resources.FulePoints;
import mc.cws.paintOff.Game.Resources.UltPoints;
import mc.cws.paintOff.Game.Management.InGame.Game;
import mc.cws.paintOff.Game.Management.InGame.SnowballEffect;
import mc.cws.paintOff.Game.Management.Start;
import mc.cws.paintOff.PaintOffMain;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.ArrayList;
import java.util.List;

public class Musket {
    private static PaintOffMain plugin;
    public static void setPlugin(PaintOffMain plugin) {Musket.plugin = plugin;}

    public static int requiredPoints = (UltPoints.generalPoints + 20);
    public static String reichweite = "23m";
    public static int verbrauch = 10;
    public static int kugeln = 3;
    public static int damage = 5;

    public static int spawnDelay = 2; // ticks
    public static int coolDown = (spawnDelay/2)+1;
    public static double coolDownCounter = coolDown/4.0;
    public static int slot = 10;

    public static void getItem(Player player) {
        ItemStack carrot = new ItemStack(Material.IRON_PICKAXE, 1);
        ItemMeta meta = carrot.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(ChatColor.DARK_PURPLE + "Musket");
            List<String> lore = new ArrayList<>();
            lore.add(ChatColor.GRAY + "Braucht: " + ChatColor.YELLOW + requiredPoints + ChatColor.GRAY + " Ult-Punkte.");
            lore.add("");
            lore.add(ChatColor.DARK_PURPLE + "Reichweite: " + ChatColor.LIGHT_PURPLE + ChatColor.BOLD + reichweite);
            lore.add(ChatColor.DARK_PURPLE + "Verbrauch: " + ChatColor.LIGHT_PURPLE + ChatColor.BOLD + verbrauch +" Farbe/Schuss");
            lore.add(ChatColor.DARK_PURPLE + "Schaden: " + ChatColor.LIGHT_PURPLE + ChatColor.BOLD + damage + " / Schuss");
            lore.add(ChatColor.DARK_PURPLE + "Färberate: " + ChatColor.LIGHT_PURPLE + ChatColor.BOLD + kugeln +" Kugel");
            lore.add(ChatColor.DARK_PURPLE + "Spawn Ablinkzeit: " + ChatColor.LIGHT_PURPLE + ChatColor.BOLD + spawnDelay + " Ticks");
            lore.add(ChatColor.DARK_PURPLE + "Cool-Down: " + ChatColor.LIGHT_PURPLE + ChatColor.BOLD + coolDownCounter + " Sekunden");
            meta.setLore(lore);
            carrot.setItemMeta(meta);
        }
        player.getInventory().setItem(5, carrot);
    }
    public static ItemStack arsenalDisplay() {
        ItemStack paintball = new ItemStack(Material.IRON_PICKAXE, 1);
        ItemMeta meta = paintball.getItemMeta();
        meta.setDisplayName(ChatColor.YELLOW + "Musket");
        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.GRAY + "Schießt schnell drei projektile!");
        lore.add(ChatColor.GRAY + "Braucht: " + ChatColor.YELLOW + requiredPoints + ChatColor.GRAY + " Ult-Punkte.");
        lore.add("");
        lore.add(ChatColor.DARK_PURPLE + "Reichweite: " + ChatColor.LIGHT_PURPLE + ChatColor.BOLD + reichweite);
        lore.add(ChatColor.DARK_PURPLE + "Verbrauch: " + ChatColor.LIGHT_PURPLE + ChatColor.BOLD + verbrauch +" Farbe/Schuss");
        lore.add(ChatColor.DARK_PURPLE + "Schaden: " + ChatColor.LIGHT_PURPLE + ChatColor.BOLD + damage + " / Schuss");
        lore.add(ChatColor.DARK_PURPLE + "Färberate: " + ChatColor.LIGHT_PURPLE + ChatColor.BOLD + kugeln +" Kugel");
        lore.add(ChatColor.DARK_PURPLE + "Cool-Down: " + ChatColor.LIGHT_PURPLE + ChatColor.BOLD + coolDownCounter + " Sekunden");
        lore.add("");
        lore.add(ChatColor.YELLOW + "Sekundärwaffe: " + ChatColor.LIGHT_PURPLE + "Vernebler");
        lore.add(ChatColor.YELLOW + "Spezialwaffe: " + ChatColor.LIGHT_PURPLE + "Gammablitzer");
        meta.setLore(lore);
        paintball.setItemMeta(meta);
        return paintball;
    }

    public static void shoot(Player player) {
        if (FulePoints.isAbleToPerform(player, verbrauch)) {
            // Play custom sound
            player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_HAT, 0.1f, 2.0f);

            // Launch snowball
            Snowball snowball = player.launchProjectile(Snowball.class);
            snowball.setVelocity(player.getLocation().getDirection().multiply(1.25));
            Game.applyGravity(snowball, 0.05);
            snowball.setMetadata("spawnedBy", new FixedMetadataValue(plugin, "Common"));
            SnowballEffect.dub(snowball, player, Start.getQueueNumber(player));

            Bukkit.getScheduler().runTaskLater(plugin, () -> {
                Snowball snowball2 = player.launchProjectile(Snowball.class);
                snowball2.setVelocity(player.getLocation().getDirection().multiply(1.3));
                Game.applyGravity(snowball2, 0.05);
                snowball2.setMetadata("spawnedBy", new FixedMetadataValue(plugin, "Common"));
                SnowballEffect.sin(snowball2, player, Start.getQueueNumber(player));
            }, spawnDelay);
            Bukkit.getScheduler().runTaskLater(plugin, () -> {
                Snowball snowball2 = player.launchProjectile(Snowball.class);
                snowball2.setVelocity(player.getLocation().getDirection().multiply(1.35));
                Game.applyGravity(snowball2, 0.05);
                snowball2.setMetadata("spawnedBy", new FixedMetadataValue(plugin, "Lower"));
                SnowballEffect.sin(snowball2, player, Start.getQueueNumber(player));
            }, spawnDelay*2L);

            // Start continuous effect

            // Handle fuel consumption
            FulePoints.removeFulePoints(player, verbrauch);
            Game.setCooldown(player, coolDown);
        }
    }
}
