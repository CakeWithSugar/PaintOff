package mc.cws.paintOff.Game.Items.Primarys.None;

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

public class Blubber {
    private static PaintOffMain plugin;
    public static void setPlugin(PaintOffMain plugin) {Blubber.plugin = plugin;}

    public static int requiredPoints = (UltPoints.generalPoints + 40);
    public static String reichweite = "17m";
    public static int verbrauch = 30;
    public static int kugeln = 2;
    public static int winkel = 12;
    public static int folge = 3;
    public static int coolDown = 3;
    public static int damage = 10;
    public static double coolDownCounter = coolDown/4.0;
    public static int slot = 16;

    public static void getItem(Player player) {
        ItemStack carrot = new ItemStack(Material.LEATHER_HORSE_ARMOR, 1);
        ItemMeta meta = carrot.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(ChatColor.DARK_PURPLE + "Blubber");
            List<String> lore = new ArrayList<>();
            lore.add(ChatColor.GRAY + "Braucht: " + ChatColor.YELLOW + requiredPoints + ChatColor.GRAY + " Ult-Punkte.");
            lore.add("");
            lore.add(ChatColor.DARK_PURPLE + "Reichweite: " + ChatColor.LIGHT_PURPLE + ChatColor.BOLD + reichweite);
            lore.add(ChatColor.DARK_PURPLE + "Verbrauch: " + ChatColor.LIGHT_PURPLE + ChatColor.BOLD + verbrauch + " Farbe/Schuss");
            lore.add(ChatColor.DARK_PURPLE + "max. Schaden: " + ChatColor.LIGHT_PURPLE + ChatColor.BOLD + damage + " / Schuss");
            lore.add(ChatColor.DARK_PURPLE + "Färberate: " + ChatColor.LIGHT_PURPLE + ChatColor.BOLD + kugeln +" Kugeln");
            lore.add(ChatColor.DARK_PURPLE + "Winkel: " + ChatColor.LIGHT_PURPLE + ChatColor.BOLD + winkel +"°");
            lore.add(ChatColor.DARK_PURPLE + "Folge: " + ChatColor.LIGHT_PURPLE + ChatColor.BOLD + folge);
            lore.add(ChatColor.DARK_PURPLE + "Cool-Down: " + ChatColor.LIGHT_PURPLE + ChatColor.BOLD + coolDownCounter + " Sekunden");
            meta.setLore(lore);
            carrot.setItemMeta(meta);
        }
        player.getInventory().setItem(5, carrot);
    }
    public static ItemStack arsenalDisplay() {
        ItemStack paintball = new ItemStack(Material.LEATHER_HORSE_ARMOR, 1);
        ItemMeta meta = paintball.getItemMeta();
        meta.setDisplayName(ChatColor.YELLOW + "Blubber");
        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.GRAY + "Eine schwere und träge Waffe, welche in Gebietseroberung glänzt!");
        lore.add(ChatColor.GRAY + "Braucht: " + ChatColor.YELLOW + requiredPoints + ChatColor.GRAY + " Ult-Punkte.");
        lore.add("");
        lore.add(ChatColor.DARK_PURPLE + "Reichweite: " + ChatColor.LIGHT_PURPLE + ChatColor.BOLD + reichweite);
        lore.add(ChatColor.DARK_PURPLE + "Verbrauch: " + ChatColor.LIGHT_PURPLE + ChatColor.BOLD + verbrauch + " Farbe/Schuss");
        lore.add(ChatColor.DARK_PURPLE + "max. Schaden: " + ChatColor.LIGHT_PURPLE + ChatColor.BOLD + damage + " / Schuss");
        lore.add(ChatColor.DARK_PURPLE + "Färberate: " + ChatColor.LIGHT_PURPLE + ChatColor.BOLD + kugeln +" Kugeln");
        lore.add(ChatColor.DARK_PURPLE + "Winkel: " + ChatColor.LIGHT_PURPLE + ChatColor.BOLD + winkel +"°");
        lore.add(ChatColor.DARK_PURPLE + "Folge: " + ChatColor.LIGHT_PURPLE + ChatColor.BOLD + folge);
        lore.add(ChatColor.DARK_PURPLE + "Cool-Down: " + ChatColor.LIGHT_PURPLE + ChatColor.BOLD + coolDownCounter + " Sekunden");
        lore.add("");
        lore.add(ChatColor.YELLOW + "Sekundärwaffe: " + ChatColor.LIGHT_PURPLE + "Vernebler");
        lore.add(ChatColor.YELLOW + "Spezialwaffe: " + ChatColor.LIGHT_PURPLE + "Tornedo");
        meta.setLore(lore);
        paintball.setItemMeta(meta);
        return paintball;
    }

    public static void shoot(Player player) {
        if (FulePoints.isAbleToPerform(player, verbrauch)) {
            // Play custom sound
            player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_HAT, 0.1f, 2.0f);

            // Erstelle den linken Schneeball
            Snowball leftSnowball = player.launchProjectile(Snowball.class);
            Vector direction = player.getLocation().getDirection();
            Vector leftDirection = direction.clone().rotateAroundY(Math.toRadians(6)); // 15 Grad nach links
            leftSnowball.setVelocity(leftDirection.multiply(0.45));
            leftSnowball.setMetadata("spawnedBy", new FixedMetadataValue(plugin, "Blubber"));
            SnowballEffect.sin(leftSnowball, player, Start.getQueueNumber(player));

            // Erstelle den rechten Schneeball
            Snowball rightSnowball = player.launchProjectile(Snowball.class);
            Vector rightDirection = direction.clone().rotateAroundY(Math.toRadians(-6)); // 15 Grad nach rechts
            rightSnowball.setVelocity(rightDirection.multiply(0.45));
            rightSnowball.setMetadata("spawnedBy", new FixedMetadataValue(plugin, "Blubber"));
            SnowballEffect.sin(rightSnowball, player, Start.getQueueNumber(player));

            // Handle fuel consumption
            FulePoints.removeFulePoints(player,verbrauch);
            Game.setCooldown(player, coolDown);
        }
    }
}
