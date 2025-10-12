package mc.cws.paintOff.PrimaryWeapons.Haepec;

import mc.cws.paintOff.Game.Management.InGame.Game;
import mc.cws.paintOff.Game.Resources.FulePoints;
import mc.cws.paintOff.PrimaryWeapons.Normal.SchnipsLehr;
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

import java.util.ArrayList;
import java.util.List;

public class HaeSchnipsLehr {
    private static PaintOffMain plugin;
    public static void setPlugin(PaintOffMain plugin) {
        HaeSchnipsLehr.plugin = plugin;}

    public static int requiredPoints = (UltPoints.generalPoints - 100);
    public static String reichweite = "13m";
    public static int verbrauch = 6;
    public static int kugeln = 1;
    public static int damage = 4;
    public static int slot = 19;

    public static void getItem(Player player) {
        ItemStack carrot = new ItemStack(Material.WOODEN_HOE, 1);
        ItemMeta meta = carrot.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(ChatColor.DARK_PURPLE + "Haepec Schnips-Leer");
            List<String> lore = new ArrayList<>();
            lore.add(ChatColor.GRAY + "Braucht: " + ChatColor.YELLOW + requiredPoints + ChatColor.GRAY + " Ult-Punkte.");
            lore.add("");
            lore.add(ChatColor.DARK_PURPLE + "Reichweite: " + ChatColor.LIGHT_PURPLE + ChatColor.BOLD + reichweite);
            lore.add(ChatColor.DARK_PURPLE + "Verbrauch: " + ChatColor.LIGHT_PURPLE + ChatColor.BOLD + verbrauch +" Farbe/Schuss");
            lore.add(ChatColor.DARK_PURPLE + "Schaden: " + ChatColor.LIGHT_PURPLE + ChatColor.BOLD + damage + " / Schuss");
            lore.add(ChatColor.DARK_PURPLE + "Färberate: " + ChatColor.LIGHT_PURPLE + ChatColor.BOLD + kugeln +" Kugel");
            meta.setLore(lore);
            carrot.setItemMeta(meta);
        }
        player.getInventory().setItem(5, carrot);
    }

    public static ItemStack arsenalDisplay() {
        ItemStack paintball = new ItemStack(Material.WOODEN_HOE, 1);
        ItemMeta meta = paintball.getItemMeta();
        meta.setDisplayName(ChatColor.YELLOW + "Haepec Schnips-Leer");
        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.GRAY + "Kürzer aber genauer!");
        lore.add(ChatColor.GRAY + "Braucht: " + ChatColor.YELLOW + requiredPoints + ChatColor.GRAY + " Ult-Punkte.");
        lore.add("");
        lore.add(ChatColor.DARK_PURPLE + "Reichweite: " + ChatColor.LIGHT_PURPLE + ChatColor.BOLD + reichweite);
        lore.add(ChatColor.DARK_PURPLE + "Verbrauch: " + ChatColor.LIGHT_PURPLE + ChatColor.BOLD + verbrauch +" Farbe/Schuss");
        lore.add(ChatColor.DARK_PURPLE + "Schaden: " + ChatColor.LIGHT_PURPLE + ChatColor.BOLD + damage + " / Schuss");
        lore.add(ChatColor.DARK_PURPLE + "Färberate: " + ChatColor.LIGHT_PURPLE + ChatColor.BOLD + kugeln +" Kugel");
        lore.add("");
        lore.add(ChatColor.YELLOW + "Sekundärwaffe: " + ChatColor.LIGHT_PURPLE + "Amegamalge");
        lore.add(ChatColor.YELLOW + "Spezialwaffe: " + ChatColor.LIGHT_PURPLE + "Generischer Boost");
        meta.setLore(lore);
        paintball.setItemMeta(meta);
        return paintball;
    }

    public static void shoot(Player player) {
        if (FulePoints.isAbleToPerform(player, SchnipsLehr.verbrauch)) {
            // Play custom sound
            player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_HAT, 0.1f, 2.0f);

            // Launch snowball
            Snowball snowball = player.launchProjectile(Snowball.class);
            snowball.setVelocity(player.getLocation().getDirection().multiply(1.0));
            Game.applyGravity(snowball, 0.05);
            snowball.setMetadata("spawnedBy", new FixedMetadataValue(plugin, "DoubleHigher"));

            // Start continuous effect
            SnowballEffect.sin(snowball, player, Start.getQueueNumber(player));

            // Handle fuel consumption
            FulePoints.removeFulePoints(player, SchnipsLehr.verbrauch);
        }
    }
}
