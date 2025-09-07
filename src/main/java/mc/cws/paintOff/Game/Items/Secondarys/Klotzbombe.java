package mc.cws.paintOff.Game.Items.Secondarys;

import mc.cws.paintOff.Game.Extras.Painter;
import mc.cws.paintOff.Game.Resources.FulePoints;
import mc.cws.paintOff.Game.Management.Start;
import mc.cws.paintOff.PaintOffMain;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class Klotzbombe {
    private static PaintOffMain plugin;
    public static void setPlugin(PaintOffMain plugin) {Klotzbombe.plugin = plugin;}
    public static int verbrauch = 160;

    public static void getItem(Player player) {
        ItemStack carrot = new ItemStack(Material.COPPER_BULB, 1);
        ItemMeta meta = carrot.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(ChatColor.DARK_PURPLE + "Klotzbombe");
            List<String> lore = new ArrayList<>();
            lore.add(ChatColor.GRAY + "Wirft eine Klotzbombe.");
            lore.add("");
            lore.add(ChatColor.DARK_PURPLE + "Verbrauch: " + ChatColor.LIGHT_PURPLE + ChatColor.BOLD + verbrauch + " Farbe/Schuss");
            meta.setLore(lore);
            carrot.setItemMeta(meta);
        }
        player.getInventory().setItem(6, carrot);
    }

    public static void shoot(Player player) {
        if (FulePoints.isAbleToPerform(player, verbrauch)) {
            // Play custom sound
            player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_HAT, 0.1f, 2.0f);
            // Shoot snowball
            Snowball snowball = player.launchProjectile(Snowball.class);
            snowball.setVelocity(player.getLocation().getDirection().multiply(0.8));
            snowball.setMetadata("spawnedBy", new FixedMetadataValue(plugin, "Klotzbombe"));

            // Create a task to show particles while flying
            Bukkit.getScheduler().runTaskTimer(plugin, () -> {
                if (snowball.isDead() || !snowball.isValid()) {
                    return;
                }
                Location loc = snowball.getLocation();
                snowball.getWorld().spawnParticle(Particle.FLAME, loc, 1, 0.1, 0.1, 0.1, 0.05);
                snowball.getWorld().spawnParticle(Particle.CRIT, loc, 1, 0.1, 0.1, 0.1, 0.1);
            }, 0, 1);

            // Handle fuel consumption
            FulePoints.removeFulePoints(player, verbrauch);
        }
    }

    public static void phaseOne(Block hitBlock, Player player, String color,int multiplicator) {
        int n = Start.getQueueNumber(player);
        AtomicBoolean stop = new AtomicBoolean(false);
        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            if (stop.get()) {
                return;
            }
            Painter.explosionAlgorithm(hitBlock, player, n, 2, color, multiplicator*3);
            // Add visual effects
            hitBlock.getWorld().spawnParticle(Particle.FLASH, hitBlock.getLocation(), 3, 0, 1, 0, 0.1);

            // Play sound effect
            hitBlock.getWorld().playSound(hitBlock.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 0.5f, 1.5f);
            if (!stop.get()) {
                stop.set(true);
            }
            hitBlock.getWorld().spawnParticle(Particle.FLAME, hitBlock.getLocation().add(0, 0.5, 0), 10, 0, 1, 0, 0.2);
        }, 20);
        Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, () -> {
            if (stop.get()) {
                return;
            }
            hitBlock.getWorld().spawnParticle(Particle.FIREWORK, hitBlock.getLocation().add(0, 0.5, 0), 10, 0, 0, 0, 0.1);
        },0,2);
    }
}
