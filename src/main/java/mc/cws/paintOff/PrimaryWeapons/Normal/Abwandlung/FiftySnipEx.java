package mc.cws.paintOff.PrimaryWeapons.Normal.Abwandlung;

import mc.cws.paintOff.Game.Management.InGame.Game;
import mc.cws.paintOff.Game.Resources.FulePoints;
import mc.cws.paintOff.Game.Resources.UltPoints;
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

public class FiftySnipEx {
    private static PaintOffMain plugin;
    public static void setPlugin(PaintOffMain plugin) {FiftySnipEx.plugin = plugin;}

    public static int requiredPoints = (UltPoints.generalPoints + 20);
    public static String reichweite = "33m";
    public static int verbrauch = 18;
    public static int kugeln = 2;
    public static int coolDown = 4;
    public static int damage = 8;
    public static double coolDownCounter = coolDown/4.0;
    public static int slot = 23;

    public static void getItem(Player player) {
        ItemStack carrot = new ItemStack(Material.DIAMOND_SHOVEL, 1);
        ItemMeta meta = carrot.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(ChatColor.DARK_PURPLE + "50er-SniplEx");
            List<String> lore = new ArrayList<>();
            lore.add(ChatColor.GRAY + "Braucht: " + ChatColor.YELLOW + requiredPoints + ChatColor.GRAY + " Ult-Punkte.");
            lore.add("");
            lore.add(ChatColor.DARK_PURPLE + "Reichweite: " + ChatColor.LIGHT_PURPLE + ChatColor.BOLD + reichweite);
            lore.add(ChatColor.DARK_PURPLE + "Verbrauch: " + ChatColor.LIGHT_PURPLE + ChatColor.BOLD + verbrauch + " Farbe/Schuss");
            lore.add(ChatColor.DARK_PURPLE + "max.Schaden: " + ChatColor.LIGHT_PURPLE + ChatColor.BOLD + damage + " / Schuss");
            lore.add(ChatColor.DARK_PURPLE + "Färberate: " + ChatColor.LIGHT_PURPLE + ChatColor.BOLD + kugeln + " Kugeln");
            lore.add(ChatColor.DARK_PURPLE + "Cool-Down: " + ChatColor.LIGHT_PURPLE + ChatColor.BOLD + coolDownCounter + " Sekunden");
            meta.setLore(lore);
            carrot.setItemMeta(meta);
        }
        player.getInventory().setItem(5, carrot);
    }
    public static ItemStack arsenalDisplay() {
        ItemStack paintball = new ItemStack(Material.DIAMOND_SHOVEL, 1);
        ItemMeta meta = paintball.getItemMeta();
        meta.setDisplayName(ChatColor.YELLOW + "50er-SniplEx");
        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.GRAY + "Erhöhte Entfernung, erhöhter Schaden!");
        lore.add(ChatColor.GRAY + "Braucht: " + ChatColor.YELLOW + requiredPoints + ChatColor.GRAY + " Ult-Punkte.");
        lore.add("");
        lore.add(ChatColor.DARK_PURPLE + "Reichweite: " + ChatColor.LIGHT_PURPLE + ChatColor.BOLD + reichweite);
        lore.add(ChatColor.DARK_PURPLE + "Verbrauch: " + ChatColor.LIGHT_PURPLE + ChatColor.BOLD + verbrauch + " Farbe/Schuss");
        lore.add(ChatColor.DARK_PURPLE + "max.Schaden: " + ChatColor.LIGHT_PURPLE + ChatColor.BOLD + damage + " / Schuss");
        lore.add(ChatColor.DARK_PURPLE + "Färberate: " + ChatColor.LIGHT_PURPLE + ChatColor.BOLD + kugeln + " Kugeln");
        lore.add(ChatColor.DARK_PURPLE + "Cool-Down: " + ChatColor.LIGHT_PURPLE + ChatColor.BOLD + coolDownCounter + " Sekunden");
        lore.add("");
        lore.add(ChatColor.YELLOW + "Sekundärwaffe: " + ChatColor.LIGHT_PURPLE + "Aufdecker");
        lore.add(ChatColor.YELLOW + "Spezialwaffe: " + ChatColor.LIGHT_PURPLE + "Eruptor");
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
            snowball.setGravity(false);
            snowball.setVelocity(player.getLocation().getDirection().multiply(2.5));
            snowball.setMetadata("spawnedBy", new FixedMetadataValue(plugin, "SniplTwo"));
            SnowballEffect.tri(snowball, player, Start.getQueueNumber(player));

            // Launch snowball
            Snowball snowball2 = player.launchProjectile(Snowball.class);
            snowball2.setGravity(true);
            snowball2.setVelocity(player.getLocation().getDirection().multiply(1.75));
            snowball2.setMetadata("spawnedBy", new FixedMetadataValue(plugin, "Lower"));
            SnowballEffect.tri(snowball2, player, Start.getQueueNumber(player));

            Bukkit.getScheduler().runTaskTimer(plugin, () -> {
                if (snowball.isDead() || !snowball.isValid()) {
                    return;
                }
                Location loc = snowball.getLocation();
                snowball.getWorld().spawnParticle(Particle.ASH, loc, 3, 0, 0, 0, 0.001);
            }, 0, 1);
            Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                if (snowball.isDead() || !snowball.isValid()) {
                    return;
                }
                snowball.remove();

            }, (15));
            // Handle fuel consumption
            FulePoints.removeFulePoints(player,verbrauch);
            Game.setCooldown(player, coolDown);
        }
    }
}
