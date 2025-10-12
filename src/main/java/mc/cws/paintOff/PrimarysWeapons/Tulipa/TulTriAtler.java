package mc.cws.paintOff.PrimarysWeapons.Tulipa;

import mc.cws.paintOff.Game.Management.InGame.Game;
import mc.cws.paintOff.Game.Resources.FulePoints;
import mc.cws.paintOff.Game.Resources.UltPoints;
import mc.cws.paintOff.Game.Management.InGame.SnowballEffect;
import mc.cws.paintOff.Game.Management.Start;
import mc.cws.paintOff.PaintOffMain;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

public class TulTriAtler {
    private static PaintOffMain plugin;
    public static void setPlugin(PaintOffMain plugin) {TulTriAtler.plugin = plugin;}

    public static int requiredPoints = (UltPoints.generalPoints + 70);
    public static String reichweite = "18m";
    public static int verbrauch = 16;
    public static int kugeln = 3;
    public static int winkel = 10;
    public static int coolDown = 2;
    public static int damage = 4;
    public static double coolDownCounter = coolDown/4.0;
    public static int slot = 20;

    public static void getItem(Player player) {
        ItemStack carrot = new ItemStack(Material.BOW, 1);
        ItemMeta meta = carrot.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(ChatColor.DARK_PURPLE + "Tulipa Tri-Atler");
            List<String> lore = new ArrayList<>();
            lore.add(ChatColor.GRAY + "Braucht: " + ChatColor.YELLOW + requiredPoints + ChatColor.GRAY + " Ult-Punkte.");
            lore.add("");
            lore.add(ChatColor.DARK_PURPLE + "Reichweite: " + ChatColor.LIGHT_PURPLE + ChatColor.BOLD + reichweite);
            lore.add(ChatColor.DARK_PURPLE + "Verbrauch: " + ChatColor.LIGHT_PURPLE + ChatColor.BOLD + verbrauch + " Farbe/Schuss");
            lore.add(ChatColor.DARK_PURPLE + "Schaden: " + ChatColor.LIGHT_PURPLE + ChatColor.BOLD + damage + " / Schuss");
            lore.add(ChatColor.DARK_PURPLE + "Färberate: " + ChatColor.LIGHT_PURPLE + ChatColor.BOLD + kugeln + " Kugeln");
            lore.add(ChatColor.DARK_PURPLE + "Winkel: " + ChatColor.LIGHT_PURPLE + ChatColor.BOLD + winkel + "°");
            lore.add(ChatColor.DARK_PURPLE + "Cool-Down: " + ChatColor.LIGHT_PURPLE + ChatColor.BOLD + coolDownCounter + " Sekunden");
            meta.setLore(lore);
            carrot.setItemMeta(meta);
        }
        player.getInventory().setItem(5, carrot);
    }
    public static ItemStack arsenalDisplay() {
        ItemStack paintball = new ItemStack(Material.BOW, 1);
        ItemMeta meta = paintball.getItemMeta();
        meta.setDisplayName(ChatColor.YELLOW + "Tulipa Tri-Atler");
        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.GRAY + "Erhöhte Reichweite und Genauigkeit!");
        lore.add(ChatColor.GRAY + "Braucht: " + ChatColor.YELLOW + requiredPoints + ChatColor.GRAY + " Ult-Punkte.");
        lore.add("");
        lore.add(ChatColor.DARK_PURPLE + "Reichweite: " + ChatColor.LIGHT_PURPLE + ChatColor.BOLD + reichweite);
        lore.add(ChatColor.DARK_PURPLE + "Verbrauch: " + ChatColor.LIGHT_PURPLE + ChatColor.BOLD + verbrauch + " Farbe/Schuss");
        lore.add(ChatColor.DARK_PURPLE + "Schaden: " + ChatColor.LIGHT_PURPLE + ChatColor.BOLD + damage + " / Schuss");
        lore.add(ChatColor.DARK_PURPLE + "Färberate: " + ChatColor.LIGHT_PURPLE + ChatColor.BOLD + kugeln + " Kugeln");
        lore.add(ChatColor.DARK_PURPLE + "Winkel: " + ChatColor.LIGHT_PURPLE + ChatColor.BOLD + winkel + "°");
        lore.add(ChatColor.DARK_PURPLE + "Cool-Down: " + ChatColor.LIGHT_PURPLE + ChatColor.BOLD + coolDownCounter + " Sekunden");
        lore.add("");
        lore.add(ChatColor.YELLOW + "Sekundärwaffe: " + ChatColor.LIGHT_PURPLE + "Aufdecker");
        lore.add(ChatColor.YELLOW + "Spezialwaffe: " + ChatColor.LIGHT_PURPLE + "Wellenschlag");
        meta.setLore(lore);
        paintball.setItemMeta(meta);
        return paintball;
    }

    public static void shoot(Player player) {
        if (FulePoints.isAbleToPerform(player, verbrauch)) {
            // Play custom sound
            player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_HAT, 0.1f, 2.0f);

            // Erstelle den Haupt-Schneeball
            Snowball mainSnowball = player.launchProjectile(Snowball.class);
            mainSnowball.setVelocity(player.getLocation().getDirection().multiply(1.2)); // 9m
            Game.applyGravity(mainSnowball, 0.05);
            mainSnowball.setMetadata("spawnedBy", new FixedMetadataValue(plugin, "Common"));
            SnowballEffect.dub(mainSnowball, player, Start.getQueueNumber(player));

            // Erstelle den linken Schneeball
            Snowball leftSnowball = player.launchProjectile(Snowball.class);
            Vector direction = player.getLocation().getDirection();
            Vector leftDirection = direction.clone().rotateAroundY(Math.toRadians(5)); // 15 Grad nach links
            leftSnowball.setVelocity(leftDirection.multiply(1.1));
            Game.applyGravity(leftSnowball, 0.05);
            leftSnowball.setMetadata("spawnedBy", new FixedMetadataValue(plugin, "Lower"));
            SnowballEffect.sin(leftSnowball, player, Start.getQueueNumber(player));

            // Erstelle den rechten Schneeball
            Snowball rightSnowball = player.launchProjectile(Snowball.class);
            Vector rightDirection = direction.clone().rotateAroundY(Math.toRadians(-5)); // 15 Grad nach rechts
            rightSnowball.setVelocity(rightDirection.multiply(1.1));
            Game.applyGravity(rightSnowball, 0.05);
            rightSnowball.setMetadata("spawnedBy", new FixedMetadataValue(plugin, "Lower"));
            SnowballEffect.sin(rightSnowball, player, Start.getQueueNumber(player));

            // Handle fuel consumption
            FulePoints.removeFulePoints(player,verbrauch);
            Game.setCooldown(player, coolDown);
        }
    }
}
