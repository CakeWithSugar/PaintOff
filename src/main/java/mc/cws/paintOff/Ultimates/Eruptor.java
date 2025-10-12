package mc.cws.paintOff.Ultimates;

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

public class Eruptor {
    private static PaintOffMain plugin;
    public static void setPlugin(PaintOffMain plugin) {
        Eruptor.plugin = plugin;}

    public static int requiredPoints = 0;
    public static String explosionsRadien = "2, 4, 6";
    public static int radius1 = 2;
    public static int radius2 = 4;
    public static int radius3 = 6;

    public static String heights = "1.25, 1.5, 1.75";
    public static double hight1 = 0.25;
    public static double hight2 = 0.5;
    public static double hight3 = 0.75;

    public static String damage = "Original damage + 1, 2, 3";
    public static int damage1 = 1;
    public static int damage2 = 2;
    public static int damage3 = 3;

    public static void getItem(Player player) {
        ItemStack carrot = new ItemStack(Material.AMETHYST_CLUSTER, 1);
        ItemMeta meta = carrot.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(ChatColor.DARK_PURPLE + "Eruptor");
            List<String> lore = new ArrayList<>();
            lore.add(ChatColor.GRAY + "Wirft einen Partykracher!");
            lore.add("");
            lore.add(ChatColor.GRAY + "Explosionsradien: " + ChatColor.GOLD + explosionsRadien + ChatColor.DARK_GRAY + " Blöcke");
            lore.add(ChatColor.GRAY + "Höhenmultiplikator: " + ChatColor.GOLD + heights + ChatColor.DARK_GRAY + " Blöcke");
            lore.add(ChatColor.GRAY + "Schaden: " + ChatColor.GOLD + damage + ChatColor.DARK_GRAY + " Herzen");
            meta.setLore(lore);
            carrot.setItemMeta(meta);
        }
        player.getInventory().setItem(1, carrot);
    }

    public static void handleItemUsage(Player player) {
        if (UltPoints.hasEnoughUltPoints(player, UltPoints.getUltPoints(player))) {
            // Remove ult points
            requiredPoints = (UltPoints.getUltPoints(player));
            UltPoints.removeUltPoints(player, requiredPoints );
            player.playSound(player.getLocation(), Sound.ENTITY_ALLAY_AMBIENT_WITHOUT_ITEM, 2.5f, 4.75f);
            player.playSound(player.getLocation(), Sound.ENTITY_ALLAY_AMBIENT_WITHOUT_ITEM, 2.5f, 2.25f);
            launch(player);
        }
    }

    public static void launch(Player player) {
        // Play custom sound
        player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_HAT, 0.1f, 2.0f);
        // Überprüfen, ob plugin nicht null ist
        if (plugin == null) {
            System.err.println("Plugin ist null in launchPlumpsBoje!");
            return;
        }

        Snowball snowball = player.launchProjectile(Snowball.class);
        snowball.setVelocity(player.getLocation().getDirection().multiply(0.6));
        snowball.setMetadata("spawnedBy", new FixedMetadataValue(plugin, "PlumpsBoje"));

        // Add particle trail
        String colorPara = Painter.getColorPara(player);
        Bukkit.getScheduler().runTaskTimer(plugin, () -> {
            if (snowball.isDead() || !snowball.isValid()) {
                return;
            }
            Location loc = snowball.getLocation();
            Verteiler.playColorParticleBubble(colorPara, snowball.getLocation(), 0.1, 1, 0.1, Particle.DUST);
            snowball.getWorld().spawnParticle(Particle.WITCH, loc, 1, 0.01, 0.01, 0.01, 0.1);
        }, 0, 1);
    }

    public static void phaseOne(Snowball snowball, Block hitBlock) {
        Location loc = snowball.getLocation().add(0, 1, 0);
        Snowball snowballChild = snowball.getWorld().spawn(snowball.getLocation(), Snowball.class);
        Vector direction = snowball.getLocation().getDirection();
        direction.setY(hight1); // Set Y component to 1.0 for upward motion
        direction.setX(0); // Set X component to 0
        direction.setZ(0); // Set Z component to 0
        snowballChild.setVelocity(direction);
        snowballChild.setShooter(snowball.getShooter());
        snowballChild.setGlowing(true);
        snowballChild.setMetadata("spawnedBy", new FixedMetadataValue(plugin, "PlumpsBojeTwo"));
        snowball.getWorld().spawnParticle(Particle.FIREWORK, loc, 10, 0, 0, 0, 0.25);
        snowball.getWorld().spawnParticle(Particle.SOUL_FIRE_FLAME, loc, 20, 1, 0.1, 1, 0.05);

        // Add visual effects
        hitBlock.getWorld().playSound(hitBlock.getLocation(), Sound.ENTITY_DRAGON_FIREBALL_EXPLODE, 0.25f, 3.0f);
        hitBlock.getWorld().playSound(hitBlock.getLocation(), Sound.BLOCK_SCULK_SENSOR_CLICKING, 1.0f, 3.0f);
    }

    public static void phaseTwo(Snowball snowball, Block hitBlock, String color, Player player) {
        int n = Start.getQueueNumber(player);
        Painter.explosionAlgorithmWithoutUltpoint(hitBlock, player, n, radius1, color, damage1);

        Location loc = snowball.getLocation().add(0, 0.2, 0);
        Snowball snowballChild = snowball.getWorld().spawn(snowball.getLocation(), Snowball.class);
        Vector direction = snowball.getLocation().getDirection();
        direction.setY(hight2); // Set Y component to 1.0 for upward motion
        direction.setX(0); // Set X component to 0
        direction.setZ(0); // Set Z component to 0
        snowballChild.setVelocity(direction);
        snowballChild.setGlowing(true);
        snowballChild.setShooter(snowball.getShooter());
        snowballChild.setMetadata("spawnedBy", new FixedMetadataValue(plugin, "PlumpsBojeThree"));
        snowball.getWorld().spawnParticle(Particle.FIREWORK, loc, 60, 0, 0, 0, 0.5);
        snowball.getWorld().spawnParticle(Particle.SOUL_FIRE_FLAME, loc, 120, 3, 0, 3, 0.05);

        // Add visual effects
        hitBlock.getWorld().playSound(hitBlock.getLocation(), Sound.ENTITY_DRAGON_FIREBALL_EXPLODE, 0.25f, 3.0f);
        hitBlock.getWorld().playSound(hitBlock.getLocation(), Sound.BLOCK_SCULK_SENSOR_CLICKING, 1.0f, 3.0f);
    }

    public static void phaseThree(Snowball snowball, Block hitBlock, String color, Player player) {
        int n = Start.getQueueNumber(player);
        Painter.explosionAlgorithmWithoutUltpoint(hitBlock, player, n, radius2, color, damage2);

        Location loc = snowball.getLocation().add(0, 0.2, 0);
        Snowball snowballChild = snowball.getWorld().spawn(snowball.getLocation(), Snowball.class);
        Vector direction = snowball.getLocation().getDirection();
        direction.setY(hight3); // Set Y component to 1.0 for upward motion
        direction.setX(0); // Set X component to 0
        direction.setZ(0); // Set Z component to 0
        snowballChild.setVelocity(direction);
        snowballChild.setShooter(snowball.getShooter());
        snowballChild.setGlowing(true);
        snowballChild.setMetadata("spawnedBy", new FixedMetadataValue(plugin, "PlumpsBojeFour"));
        snowball.getWorld().spawnParticle(Particle.FIREWORK, loc, 80, 0, 0, 0, 0.5);
        snowball.getWorld().spawnParticle(Particle.SOUL_FIRE_FLAME, loc, 80, 3, 0.1, 3, 0.05);
        snowball.getWorld().spawnParticle(Particle.TOTEM_OF_UNDYING, hitBlock.getLocation().add(0, 2, 0), 30, 0, 0, 0, 1);

        // Add visual effects
        hitBlock.getWorld().playSound(hitBlock.getLocation(), Sound.ENTITY_DRAGON_FIREBALL_EXPLODE, 0.25f, 3.0f);
        hitBlock.getWorld().playSound(hitBlock.getLocation(), Sound.BLOCK_SCULK_SENSOR_CLICKING, 1.0f, 3.0f);
    }

    public static void phaseFour(Snowball snowball, Block hitBlock,String color, Player player) {
        int n = Start.getQueueNumber(player);
        snowball.getWorld().spawnParticle(Particle.SOUL_FIRE_FLAME, hitBlock.getLocation().add(0, 0.2, 0), 200, 4, 0.1, 4, 0.5);
        Painter.explosionAlgorithmWithoutUltpoint(hitBlock, player, n, radius3, color, damage3);

        // Add visual effects
        snowball.getWorld().spawnParticle(Particle.FIREWORK, hitBlock.getLocation().add(0, 0.2, 0), 120, 0, 0, 0, 0.5);
        snowball.getWorld().spawnParticle(Particle.TOTEM_OF_UNDYING, hitBlock.getLocation().add(0, 2, 0), 80, 0, 0, 0, 1);

        // Play sound effect
        hitBlock.getWorld().playSound(hitBlock.getLocation(), Sound.ENTITY_DRAGON_FIREBALL_EXPLODE, 1.0f, 3.0f);
    }
}
