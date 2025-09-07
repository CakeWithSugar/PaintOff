package mc.cws.paintOff.Game.Items.Ultimates;

import mc.cws.paintOff.Game.Extras.Painter;
import mc.cws.paintOff.Game.Resources.UltPoints;
import mc.cws.paintOff.Game.Management.Start;
import mc.cws.paintOff.Game.Management.Verteiler;
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

public class Wellenschlag {
    private static PaintOffMain plugin;
    public static void setPlugin(PaintOffMain plugin) {
        Wellenschlag.plugin = plugin;}

    public static int requiredPoints = 0;
    public static int explosion = 2;
    public static double height = 0.6;
    public static int damage = 8;
    public static String effekte = "Temporäre Langsamkeit";

    public static void getItem(Player player) {
        ItemStack carrot = new ItemStack(Material.STRING, 1);
        ItemMeta meta = carrot.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(ChatColor.DARK_PURPLE + "Wellenschlag");
            List<String> lore = new ArrayList<>();
            lore.add(ChatColor.GRAY + "Sendet ein Wellenschlag.");
            lore.add("");
            lore.add(ChatColor.GRAY + "Explosionsradien: " + ChatColor.GOLD + (explosion+1) + ChatColor.DARK_GRAY + " Blöcke");
            lore.add(ChatColor.GRAY + "Höhenmultiplikator: " + ChatColor.GOLD + (height+1) + ChatColor.DARK_GRAY + " Blöcke");
            lore.add(ChatColor.GRAY + "Schaden: " + ChatColor.GOLD + (damage/2) + ChatColor.DARK_GRAY + " Herzen");
            lore.add(ChatColor.GRAY + "Effekte: " + ChatColor.GOLD + effekte);
            meta.setLore(lore);
            carrot.setItemMeta(meta);
        }
        player.getInventory().setItem(1, carrot);
    }

    public static void handleItemUsage(Player player) {
        if (UltPoints.hasEnoughUltPoints(player,UltPoints.getUltPoints(player))) {
            // Remove ult points
            requiredPoints = (UltPoints.getUltPoints(player));
            UltPoints.removeUltPoints(player, requiredPoints);
            player.playSound(player.getLocation(), Sound.ENTITY_WITHER_SHOOT, 0.5f, 2f);
            player.playSound(player.getLocation(), Sound.ENTITY_WITHER_SHOOT, 0.5f, 1.5f);
            player.playSound(player.getLocation(), Sound.ENTITY_WITHER_SHOOT, 0.5f, 1f);
            launch(player);
        }
    }

    public static void launch(Player player) {
        // Play custom sound
        player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_HAT, 0.1f, 2.0f);

        // Erstelle den Haupt-Schneeball
        Snowball mainSnowball = player.launchProjectile(Snowball.class);
        mainSnowball.setVelocity(player.getLocation().getDirection().multiply(0.8));
        mainSnowball.setMetadata("spawnedBy", new FixedMetadataValue(plugin, "Wellenschlag"));

        // Erstelle den linken Schneeball
        Snowball leftSnowball = player.launchProjectile(Snowball.class);
        Vector direction = player.getLocation().getDirection();
        Vector leftDirection = direction.clone().rotateAroundY(Math.toRadians(15)); // 15 Grad nach links
        leftSnowball.setVelocity(leftDirection.multiply(0.5));
        leftSnowball.setMetadata("spawnedBy", new FixedMetadataValue(plugin, "Wellenschlag"));

        // Erstelle den rechten Schneeball
        Snowball rightSnowball = player.launchProjectile(Snowball.class);
        Vector rightDirection = direction.clone().rotateAroundY(Math.toRadians(-15)); // 15 Grad nach rechts
        rightSnowball.setVelocity(rightDirection.multiply(0.5));
        rightSnowball.setMetadata("spawnedBy", new FixedMetadataValue(plugin, "Wellenschlag"));

        // Erstelle den Haupt-Schneeball
        Snowball mainSnowball2 = player.launchProjectile(Snowball.class);
        mainSnowball2.setVelocity(player.getLocation().getDirection().multiply(0.3));
        mainSnowball2.setMetadata("spawnedBy", new FixedMetadataValue(plugin, "Wellenschlag"));

        // Erstelle den linken Schneeball
        Snowball leftSnowball2 = player.launchProjectile(Snowball.class);
        Vector leftDirection2 = direction.clone().rotateAroundY(Math.toRadians(20)); // 15 Grad nach links
        leftSnowball2.setVelocity(leftDirection2.multiply(0.8));
        leftSnowball2.setMetadata("spawnedBy", new FixedMetadataValue(plugin, "Wellenschlag"));

        // Erstelle den rechten Schneeball
        Snowball rightSnowball2 = player.launchProjectile(Snowball.class);
        Vector rightDirection2 = direction.clone().rotateAroundY(Math.toRadians(-20)); // 15 Grad nach rechts
        rightSnowball2.setVelocity(rightDirection2.multiply(0.8));
        rightSnowball2.setMetadata("spawnedBy", new FixedMetadataValue(plugin, "Wellenschlag"));

        // Erstelle den linken Schneeball
        Snowball leftSnowball3 = player.launchProjectile(Snowball.class);
        Vector leftDirection3 = direction.clone().rotateAroundY(Math.toRadians(10)); // 15 Grad nach links
        leftSnowball3.setVelocity(leftDirection3.multiply(0.65));
        leftSnowball3.setMetadata("spawnedBy", new FixedMetadataValue(plugin, "Wellenschlag"));

        // Erstelle den rechten Schneeball
        Snowball rightSnowball3 = player.launchProjectile(Snowball.class);
        Vector rightDirection3 = direction.clone().rotateAroundY(Math.toRadians(-10)); // 15 Grad nach rechts
        rightSnowball3.setVelocity(rightDirection3.multiply(0.65));
        rightSnowball3.setMetadata("spawnedBy", new FixedMetadataValue(plugin, "Wellenschlag"));

        // Add particle trail
        String colorPara = Painter.getColorPara(player);
        Bukkit.getScheduler().runTaskTimer(plugin, () -> {
            if (mainSnowball.isDead() || !mainSnowball.isValid()) {
                return;
            }
            Location loc = mainSnowball.getLocation();
            Verteiler.playColorParticleBubble(colorPara, mainSnowball.getLocation(), 0.1, 1, 0.1, Particle.DUST);
            mainSnowball.getWorld().spawnParticle(Particle.SOUL_FIRE_FLAME, loc, 1, 0.01, 0.01, 0.01, 0.01);
        }, 0, 2);
        Bukkit.getScheduler().runTaskTimer(plugin, () -> {
            if (rightSnowball.isDead() || !rightSnowball.isValid()) {
                return;
            }
            Location loc = rightSnowball.getLocation();
            Verteiler.playColorParticleBubble(colorPara, rightSnowball.getLocation(), 0.1, 1, 0.1, Particle.DUST);
            mainSnowball.getWorld().spawnParticle(Particle.SOUL_FIRE_FLAME, loc, 1, 0.01, 0.01, 0.01, 0.01);
        }, 0, 2);
        Bukkit.getScheduler().runTaskTimer(plugin, () -> {
            if (leftSnowball.isDead() || !leftSnowball.isValid()) {
                return;
            }
            Location loc = leftSnowball.getLocation();
            Verteiler.playColorParticleBubble(colorPara, leftSnowball.getLocation(), 0.1, 1, 0.1, Particle.DUST);
            mainSnowball.getWorld().spawnParticle(Particle.SOUL_FIRE_FLAME, loc, 1, 0.01, 0.01, 0.01, 0.01);
        }, 0, 2);
        Bukkit.getScheduler().runTaskTimer(plugin, () -> {
            if (leftSnowball2.isDead() || !leftSnowball2.isValid()) {
                return;
            }
            Location loc = leftSnowball2.getLocation();
            Verteiler.playColorParticleBubble(colorPara, leftSnowball2.getLocation(), 0.1, 1, 0.1, Particle.DUST);
            mainSnowball.getWorld().spawnParticle(Particle.SOUL_FIRE_FLAME, loc, 1, 0.01, 0.01, 0.01, 0.01);
        }, 0, 2);
        Bukkit.getScheduler().runTaskTimer(plugin, () -> {
            if (rightSnowball2.isDead() || !rightSnowball2.isValid()) {
                return;
            }
            Location loc = rightSnowball2.getLocation();
            Verteiler.playColorParticleBubble(colorPara, rightSnowball2.getLocation(), 0.1, 1, 0.1, Particle.DUST);
            mainSnowball.getWorld().spawnParticle(Particle.SOUL_FIRE_FLAME, loc, 1, 0.01, 0.01, 0.01, 0.01);
        }, 0, 2);
        Bukkit.getScheduler().runTaskTimer(plugin, () -> {
            if (mainSnowball2.isDead() || !mainSnowball2.isValid()) {
                return;
            }
            Location loc = mainSnowball2.getLocation();
            Verteiler.playColorParticleBubble(colorPara, mainSnowball2.getLocation(), 0.1, 1, 0.1, Particle.DUST);
            mainSnowball.getWorld().spawnParticle(Particle.SOUL_FIRE_FLAME, loc, 1, 0.01, 0.01, 0.01, 0.01);
        }, 0, 2);
        Bukkit.getScheduler().runTaskTimer(plugin, () -> {
            if (rightSnowball3.isDead() || !rightSnowball3.isValid()) {
                return;
            }
            Location loc = rightSnowball3.getLocation();
            Verteiler.playColorParticleBubble(colorPara, rightSnowball3.getLocation(), 0.1, 1, 0.1, Particle.DUST);
            mainSnowball.getWorld().spawnParticle(Particle.SOUL_FIRE_FLAME, loc, 1, 0.01, 0.01, 0.01, 0.01);
        }, 0, 2);
        Bukkit.getScheduler().runTaskTimer(plugin, () -> {
            if (leftSnowball3.isDead() || !leftSnowball3.isValid()) {
                return;
            }
            Location loc = leftSnowball3.getLocation();
            leftSnowball3.getWorld().spawnParticle(Particle.SOUL_FIRE_FLAME, loc, 1, 0.1, 0.1, 0.1, 0.01);
        }, 0, 2);
    }

    public static void phaseOne(Snowball snowball, Block hitBlock) {
        Location loc = snowball.getLocation().add(0, 1, 0);
        Snowball snowballChild = snowball.getWorld().spawn(snowball.getLocation(), Snowball.class);
        Vector direction = snowball.getLocation().getDirection();
        direction.setY(Wellenschlag.height); // Set Y component to 1.0 for upward motion
        direction.setX(0); // Set X component to 0
        direction.setZ(0); // Set Z component to 0
        snowballChild.setVelocity(direction);
        snowballChild.setShooter(snowball.getShooter());
        snowballChild.setGlowing(true);
        snowballChild.setMetadata("spawnedBy", new FixedMetadataValue(plugin, "WellenschlagChild"));
        snowball.getWorld().spawnParticle(Particle.FIREWORK, loc, 10, 0, 0, 0, 0.25);
        snowball.getWorld().spawnParticle(Particle.SOUL_FIRE_FLAME, loc, 20, 1, 0.1, 1, 0.05);
        Bukkit.getScheduler().runTaskTimer(plugin, () -> {
            if (snowballChild.isDead() || !snowballChild.isValid()) {
                return;
            }
            Location loc2 = snowballChild.getLocation();
            snowballChild.getWorld().spawnParticle(Particle.CRIMSON_SPORE, loc2, 6, 0, 0, 0, 0.1);
            snowballChild.getWorld().spawnParticle(Particle.DUST_PLUME, loc2, 3, 0, 0, 0, 0.05);
        }, 0, 2);

        // Add visual effects
        hitBlock.getWorld().spawnParticle(Particle.FLASH, hitBlock.getLocation(), 1, 0, 1, 0, 0.1);// Play sound effect
        hitBlock.getWorld().spawnParticle(Particle.SONIC_BOOM, hitBlock.getLocation(), 1, 0, 1, 0, 0.1);// Play sound effect
        hitBlock.getWorld().playSound(hitBlock.getLocation(), Sound.BLOCK_SLIME_BLOCK_BREAK, 2.0f, 3.0f);
    }

    public static void phaseTwo(Block hitBlock, Player player, String color) {
        int n = Start.getQueueNumber(player);
        Painter.explosionAlgorithmWithoutUltpoint(hitBlock, player, n, explosion, color, damage);

        // Add visual effects
        hitBlock.getWorld().spawnParticle(Particle.FLASH, hitBlock.getLocation(), 2, 0, 1, 0, 0.1);
        hitBlock.getWorld().spawnParticle(Particle.SOUL_FIRE_FLAME, hitBlock.getLocation(), 25, 0, 1, 0, 0.25);
        // Play sound effect
        hitBlock.getWorld().playSound(hitBlock.getLocation(), Sound.ENTITY_DRAGON_FIREBALL_EXPLODE, 2.0f, 0.25f);
    }
}
