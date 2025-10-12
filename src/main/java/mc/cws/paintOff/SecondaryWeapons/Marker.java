package mc.cws.paintOff.SecondaryWeapons;

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

public class Marker {
    private static PaintOffMain plugin;
    public static void setPlugin(PaintOffMain plugin) {Marker.plugin = plugin;}
    public static int verbrauch = 120;
    public static int dauer = 4;

    public static void getItem(Player player) {
        ItemStack carrot = new ItemStack(Material.LEVER, 1);
        ItemMeta meta = carrot.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(ChatColor.DARK_PURPLE + "Marker");
            List<String> lore = new ArrayList<>();
            lore.add(ChatColor.GRAY + "Schießt einen Marker.");
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
            snowball.setGravity(false);
            snowball.setVelocity(player.getLocation().getDirection().multiply(3.0));
            snowball.setMetadata("spawnedBy", new FixedMetadataValue(plugin, "Marker"));

            // Create a task to show particles while flying
            Bukkit.getScheduler().runTaskTimer(plugin, () -> {
                if (snowball.isDead() || !snowball.isValid()) {
                    return;
                }
                Location loc = snowball.getLocation();
                snowball.getWorld().spawnParticle(Particle.SCRAPE, loc, 10, 0, 0, 0, 0.005);
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
            }, (16));
            // Handle fuel consumption
            FulePoints.removeFulePoints(player, verbrauch);
        }
    }

    public static void phaseOne(Block hitBlock, Player player, String color) {
        int n = Start.getQueueNumber(player);
        Painter.paintBlockWithUltpoint(hitBlock,player,n,color);
        hitBlock.getWorld().spawnParticle(Particle.SONIC_BOOM, hitBlock.getLocation().add(0.5, 1, 0.5), 2, 0.125, 0.125, 0.125, 0.1);
        hitBlock.getWorld().spawnParticle(Particle.SOUL, hitBlock.getLocation().add(0.5, 1, 0.5), 10, 0.5, 0.5, 0.5, 0.1);
    }
}
