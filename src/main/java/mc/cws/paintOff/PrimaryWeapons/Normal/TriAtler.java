package mc.cws.paintOff.PrimaryWeapons.Normal;

import mc.cws.paintOff.Configuration;
import mc.cws.paintOff.Game.Resources.FulePoints;
import mc.cws.paintOff.Game.Management.InGame.SnowballEffect;
import mc.cws.paintOff.Game.Management.Start;
import mc.cws.paintOff.Game.Management.InGame.Game;
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

public class TriAtler {
    private static PaintOffMain plugin;
    public static void setPlugin(PaintOffMain plugin) {TriAtler.plugin = plugin;}

    public static int requiredPoints = (Configuration.generalUltPoints + 30);
    public static String reichweite = "12m";
    public static int verbrauch = 12;
    public static int kugeln = 3;
    public static int winkel = 30;
    public static int damage = 3;
    public static int slot = 11;

    public static void getItem(Player player) {
        ItemStack carrot = new ItemStack(Material.BOW, 1);
        ItemMeta meta = carrot.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(ChatColor.DARK_PURPLE + "Tri-Atler");
            List<String> lore = new ArrayList<>();
            lore.add(ChatColor.GRAY + "Braucht: " + ChatColor.YELLOW + requiredPoints + ChatColor.GRAY + " Ult-Punkte.");
            lore.add("");
            lore.add(ChatColor.DARK_PURPLE + "Reichweite: " + ChatColor.LIGHT_PURPLE + ChatColor.BOLD + reichweite);
            lore.add(ChatColor.DARK_PURPLE + "Verbrauch: " + ChatColor.LIGHT_PURPLE + ChatColor.BOLD + verbrauch + " Farbe/Schuss");
            lore.add(ChatColor.DARK_PURPLE + "Schaden: " + ChatColor.LIGHT_PURPLE + ChatColor.BOLD + damage + " / Schuss");
            lore.add(ChatColor.DARK_PURPLE + "Färberate: " + ChatColor.LIGHT_PURPLE + ChatColor.BOLD + kugeln + " Kugeln");
            lore.add(ChatColor.DARK_PURPLE + "Winkel: " + ChatColor.LIGHT_PURPLE + ChatColor.BOLD + winkel + "°");
            meta.setLore(lore);
            carrot.setItemMeta(meta);
        }
        player.getInventory().setItem(5, carrot);
    }
    public static ItemStack arsenalDisplay() {
        ItemStack paintbrush = new ItemStack(Material.BOW, 1);
        ItemMeta meta = paintbrush.getItemMeta();
        meta.setDisplayName(ChatColor.YELLOW + "Tri-Atler");
        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.GRAY + "Eine gute Waffe für feines färben!");
        lore.add(ChatColor.GRAY + "Braucht: " + ChatColor.YELLOW + requiredPoints + ChatColor.GRAY + " Ult-Punkte.");
        lore.add("");
        lore.add(ChatColor.DARK_PURPLE + "Reichweite: " + ChatColor.LIGHT_PURPLE + ChatColor.BOLD + reichweite);
        lore.add(ChatColor.DARK_PURPLE + "Verbrauch: " + ChatColor.LIGHT_PURPLE + ChatColor.BOLD + verbrauch + " Farbe/Schuss");
        lore.add(ChatColor.DARK_PURPLE + "Schaden: " + ChatColor.LIGHT_PURPLE + ChatColor.BOLD + damage + " / Schuss");
        lore.add(ChatColor.DARK_PURPLE + "Färberate: " + ChatColor.LIGHT_PURPLE + ChatColor.BOLD + kugeln + " Kugeln");
        lore.add(ChatColor.DARK_PURPLE + "Winkel: " + ChatColor.LIGHT_PURPLE + ChatColor.BOLD + winkel + "°");
        lore.add("");
        lore.add(ChatColor.YELLOW + "Sekundärwaffe: " + ChatColor.LIGHT_PURPLE + "Marker");
        lore.add(ChatColor.YELLOW + "Spezialwaffe: " + ChatColor.LIGHT_PURPLE + "Genetischer Boost");
        meta.setLore(lore);
        paintbrush.setItemMeta(meta);
        return paintbrush;
    }

    public static void shoot(Player player) {
        if (FulePoints.isAbleToPerform(player, verbrauch)) {
            // Play custom sound
            player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_HAT, 0.1f, 2.0f);

            // Erstelle den Haupt-Schneeball
            Snowball mainSnowball = player.launchProjectile(Snowball.class);
            mainSnowball.setVelocity(player.getLocation().getDirection().multiply(0.9));
            Game.applyGravity(mainSnowball, 0.05);
            mainSnowball.setMetadata("spawnedBy", new FixedMetadataValue(plugin, "Lower"));
            SnowballEffect.sin(mainSnowball, player, Start.getQueueNumber(player));

            // Erstelle den linken Schneeball
            Snowball leftSnowball = player.launchProjectile(Snowball.class);
            Vector direction = player.getLocation().getDirection();
            Vector leftDirection = direction.clone().rotateAroundY(Math.toRadians(15)); // 15 Grad nach links
            leftSnowball.setVelocity(leftDirection.multiply(0.9));
            Game.applyGravity(leftSnowball, 0.05);
            leftSnowball.setMetadata("spawnedBy", new FixedMetadataValue(plugin, "Lower"));
            SnowballEffect.sin(leftSnowball, player, Start.getQueueNumber(player));

            // Erstelle den rechten Schneeball
            Snowball rightSnowball = player.launchProjectile(Snowball.class);
            Vector rightDirection = direction.clone().rotateAroundY(Math.toRadians(-15)); // 15 Grad nach rechts
            rightSnowball.setVelocity(rightDirection.multiply(0.9));
            Game.applyGravity(rightSnowball, 0.05);
            rightSnowball.setMetadata("spawnedBy", new FixedMetadataValue(plugin, "Lower"));
            SnowballEffect.sin(rightSnowball, player, Start.getQueueNumber(player));

            // Handle fuel consumption
            FulePoints.removeFulePoints(player,verbrauch);
        }
    }
}
