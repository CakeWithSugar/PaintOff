package mc.cws.paintOff.Game.Items.Secondarys;

import mc.cws.paintOff.Game.Resources.FulePoints;
import mc.cws.paintOff.Game.Management.InGame.SnowballEffect;
import mc.cws.paintOff.Game.Management.Start;
import mc.cws.paintOff.PaintOffMain;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

public class Reihenzieher {
    private static PaintOffMain plugin;
    public static void setPlugin(PaintOffMain plugin) {Reihenzieher.plugin = plugin;}
    public static int verbrauch = 140;
    public static int dauer = 6; // In 1/4 Sek

    public static void getItem(Player player) {
        ItemStack carrot = new ItemStack(Material.LIGHTNING_ROD, 1);
        ItemMeta meta = carrot.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(ChatColor.DARK_PURPLE + "Reihenzieher");
            List<String> lore = new ArrayList<>();
            lore.add(ChatColor.GRAY + "Schießt einen Reihenzieher.");
            lore.add("");
            lore.add(ChatColor.DARK_PURPLE + "Verbrauch: " + ChatColor.LIGHT_PURPLE + ChatColor.BOLD + verbrauch + " Farbe/Schuss");
            meta.setLore(lore);
            carrot.setItemMeta(meta);
        }
        player.getInventory().setItem(6, carrot);
    }

    public static void shoot(Player player) {
        int n = Start.getQueueNumber(player);
        if (FulePoints.isAbleToPerform(player, verbrauch)) {
            // Play custom sound
            player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_HAT, 0.1f, 2.0f);
            // Shoot snowball
            Snowball snowball = player.launchProjectile(Snowball.class);
            snowball.setGravity(false);
            Vector downVector = new Vector(0, -1, 0); // Creates a vector pointing straight down
            snowball.setVelocity(downVector);
            snowball.setVelocity(player.getLocation().getDirection().multiply(0.75));
            snowball.setMetadata("spawnedBy", new FixedMetadataValue(plugin, "Reihenzieher"));
            SnowballEffect.sin(snowball, player, n);

            // Create a task to show particles while flying
            Bukkit.getScheduler().runTaskTimer(plugin, () -> {
                if (snowball.isDead() || !snowball.isValid()) {
                    return;
                }
                Location loc = snowball.getLocation();

                // Generate random position within 1 block radius of the original location
                double angle = Math.random() * 2 * Math.PI; // Random angle in radians
                double distance = Math.sqrt(Math.random()); // Square root for uniform distribution
                double offsetX = Math.cos(angle) * distance;
                double offsetZ = Math.sin(angle) * distance;
                Location spawnLoc = loc.clone().add(offsetX, 0, offsetZ);

                // Ensure the snowball spawns at the same Y level
                spawnLoc.setY(loc.getY());

                Snowball snowballChild = snowball.getWorld().spawn(spawnLoc, Snowball.class);
                snowballChild.setVelocity(snowball.getLocation().getDirection().multiply(0));
                snowballChild.setMetadata("spawnedBy", new FixedMetadataValue(plugin, "Lower"));
                snowballChild.setShooter(snowball.getShooter());
                SnowballEffect.dub(snowballChild, player, n);
                snowball.getWorld().spawnParticle(Particle.ENCHANTED_HIT, spawnLoc, 10, 0.2, 0.2, 0.2, 0.005);
                snowball.getWorld().spawnParticle(Particle.CRIT, loc, 1, 0.1, 0.1, 0.1, 0.1);
            }, 0, 1);
            Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                if (snowball.isDead() || !snowball.isValid()) {
                    return;
                }
                Location loc = snowball.getLocation();
                snowball.getWorld().playSound(snowball.getLocation(), Sound.ENTITY_CREEPER_DEATH, 0.5f, 1.0f);
                snowball.getWorld().spawnParticle(Particle.SONIC_BOOM, loc, 1, 0, 0, 0, 0.1);
                snowball.remove();
            }, (dauer* 5L));
            // Handle fuel consumption
            FulePoints.removeFulePoints(player, verbrauch);
        }
    }
}
