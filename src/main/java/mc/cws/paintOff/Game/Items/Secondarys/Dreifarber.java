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
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

public class Dreifarber {
    private static PaintOffMain plugin;
    public static void setPlugin(PaintOffMain plugin) {Dreifarber.plugin = plugin;}
    public static int verbrauch = 190;

    public static void getItem(Player player) {
        ItemStack carrot = new ItemStack(Material.HEART_OF_THE_SEA, 1);
        ItemMeta meta = carrot.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(ChatColor.DARK_PURPLE + "Dreifärber");
            List<String> lore = new ArrayList<>();
            lore.add(ChatColor.GRAY + "Wirft einen Dreifärber.");
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
            snowball.setVelocity(player.getLocation().add(0, -1, 0).getDirection().multiply(0.8));
            snowball.setMetadata("spawnedBy", new FixedMetadataValue(plugin, "Dreifärber"));

            // Create a task to show particles while flying
            Bukkit.getScheduler().runTaskTimer(plugin, () -> {
                if (snowball.isDead() || !snowball.isValid()) {
                    return;
                }
                Location loc = snowball.getLocation();
                snowball.getWorld().spawnParticle(Particle.CRIT, loc, 1, 0.1, 0.1, 0.1, 0.1);
            }, 0, 1);

            // Handle fuel consumption
            FulePoints.removeFulePoints(player, verbrauch);
        }
    }

    public static void phaseOne(Snowball snowball, Block hitBlock, Player player, String color) {
        int n = Start.getQueueNumber(player);
        Location loc = snowball.getLocation().add(0, 1, 0);
        Snowball snowballChildTwo = snowball.getWorld().spawn(snowball.getLocation(), Snowball.class);
        // Create a new vector with specific values for forward jumping
        Vector direction = new Vector(snowball.getVelocity().getX()* 0.5, -snowball.getVelocity().getY() * 0.75, snowball.getVelocity().getZ()* 0.5); // X, Y, Z components
        snowballChildTwo.setVelocity(direction);
        snowballChildTwo.setMetadata("spawnedBy", new FixedMetadataValue(plugin, "Zweifärber"));
        snowballChildTwo.setShooter(snowball.getShooter());
        Painter.explosionAlgorithm(hitBlock,player,n,1,color,3);
        Bukkit.getScheduler().runTaskTimer(plugin, () -> {
            if (snowballChildTwo.isDead() || !snowballChildTwo.isValid()) {
                return;
            }
            Location loc2 = snowballChildTwo.getLocation();
            snowballChildTwo.getWorld().spawnParticle(Particle.CRIT, loc2, 1, 0.1, 0.1, 0.1, 0.1);
            snowballChildTwo.getWorld().spawnParticle(Particle.END_ROD, loc2, 1, 0, 0, 0, 0.01);
        }, 0, 1);
        snowballChildTwo.getWorld().spawnParticle(Particle.SNOWFLAKE, loc, 1, 0, 0, 0, 0.005);
        hitBlock.getWorld().playSound(hitBlock.getLocation(), Sound.BLOCK_SLIME_BLOCK_HIT, 0.125f, 2.0f);
    }

    public static void phaseTwo(Snowball snowball, Block hitBlock, Player player, String color) {
        int n = Start.getQueueNumber(player);
        Location loc = snowball.getLocation().add(0, 1, 0);
        Snowball snowballChildTwo = snowball.getWorld().spawn(snowball.getLocation(), Snowball.class);
        // Create a new vector with specific values for forward jumping
        Vector direction = new Vector(snowball.getVelocity().getX(), -snowball.getVelocity().getY(), snowball.getVelocity().getZ()); // X, Y, Z components
        snowballChildTwo.setVelocity(direction);
        snowballChildTwo.setMetadata("spawnedBy", new FixedMetadataValue(plugin, "Einfärber"));
        snowballChildTwo.setShooter(snowball.getShooter());
        Painter.explosionAlgorithm(hitBlock,player,n,1,color,2);

        Bukkit.getScheduler().runTaskTimer(plugin, () -> {
            if (snowballChildTwo.isDead() || !snowballChildTwo.isValid()) {
                return;
            }
            Location loc2 = snowballChildTwo.getLocation();
            snowballChildTwo.getWorld().spawnParticle(Particle.CRIT, loc2, 1, 0.1, 0.1, 0.1, 0.1);
            snowballChildTwo.getWorld().spawnParticle(Particle.END_ROD, loc2, 1, 0, 0, 0, 0.01);
        }, 0, 1);
        snowballChildTwo.getWorld().spawnParticle(Particle.SNOWFLAKE, loc, 1, 0, 0, 0, 0.005);
        hitBlock.getWorld().playSound(hitBlock.getLocation(), Sound.BLOCK_SLIME_BLOCK_HIT, 0.125f, 2.0f);
    }

    public static void phaseThree(Snowball snowball, Block hitBlock, Player player, String color) {
        int n = Start.getQueueNumber(player);
        Location loc = snowball.getLocation().add(0, 1, 0);
        Painter.explosionAlgorithm(hitBlock,player,n,1,color,1);
        snowball.getWorld().spawnParticle(Particle.END_ROD, loc, 5, 0, 0, 0, 0.1);
        hitBlock.getWorld().playSound(hitBlock.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 0.125f, 2.0f);
    }
}
