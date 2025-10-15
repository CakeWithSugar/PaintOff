package mc.cws.paintOff.Ultimates;

import mc.cws.paintOff.Game.Extras.Painter;
import mc.cws.paintOff.Game.Resources.UltPoints;
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

public class GlitzerMeteor {
    private static PaintOffMain plugin;
    public static void setPlugin(PaintOffMain plugin) {
        GlitzerMeteor.plugin = plugin;}

    public static int requiredPoints = 0;
    public static int spawnhoehe  = 30;
    public static int radius = 5;
    public static double speed = 0.1;
    public static int damage = 20;

    public static int kugeln = 2;

    public static void getItem(Player player) {
        ItemStack carrot = new ItemStack(Material.FIRE_CHARGE, 1);
        ItemMeta meta = carrot.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(ChatColor.DARK_PURPLE + "Glitzer Meteor");
            List<String> lore = new ArrayList<>();
            lore.add(ChatColor.GRAY + "Hohle den Himmel runter!");
            lore.add("");
            lore.add(ChatColor.GRAY + "Spawnhöhe: " + ChatColor.GOLD + spawnhoehe + ChatColor.DARK_GRAY + " Blöcke");
            lore.add(ChatColor.GRAY + "Explosionsradius: " + ChatColor.GOLD + (radius+1) + ChatColor.DARK_GRAY + " Blöcke");
            lore.add(ChatColor.GRAY + "Geschwindigkeit: " + ChatColor.GOLD + (-speed) + ChatColor.DARK_GRAY + " m/s");
            lore.add(ChatColor.DARK_PURPLE + "Färberate: " + ChatColor.LIGHT_PURPLE + ChatColor.BOLD + kugeln +" Kugeln");
            meta.setLore(lore);
            carrot.setItemMeta(meta);
        }
        player.getInventory().setItem(1, carrot);
    }

    public static void handleItemUsage(Player player) {
        if (UltPoints.hasEnoughUltPoints(player, UltPoints.getUltPoints(player))) {
            // Remove ult points
            requiredPoints = (UltPoints.getUltPoints(player));
            UltPoints.removeUltPoints(player,requiredPoints );
            player.playSound(player.getLocation(), Sound.ENTITY_WARDEN_DEATH, 1.5f, 0.75f);
            player.playSound(player.getLocation(), Sound.BLOCK_BELL_RESONATE, 1.0f, 1.0f);
            launch(player);
        }
    }

    public static void launch(Player player) {
        // Play custom sound
        player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_HAT, 0.1f, 2.0f);
        // Überprüfen, ob plugin nicht null ist
        if (plugin == null) {
            System.err.println("Plugin ist null in launchGlitzerMeteor!");
            return;
        }
        
        for (int i = 0; i <= kugeln-1; i++) {
            Bukkit.getScheduler().runTaskLater(plugin, () -> {
                Snowball snowball = player.launchProjectile(Snowball.class);
        snowball.setVelocity(player.getLocation().getDirection().multiply(0.8));
        snowball.setMetadata("spawnedBy", new FixedMetadataValue(plugin, "GlitzerMeteor"));
                String colorPara = Painter.getColorPara(player);
        Bukkit.getScheduler().runTaskTimer(plugin, () -> {
            if (snowball.isDead() || !snowball.isValid()) {
                return;
            }
            Location loc = snowball.getLocation();
            Verteiler.playColorParticleBubble(colorPara, snowball.getLocation(), 0.1, 1, 0.1, Particle.DUST);
            snowball.getWorld().spawnParticle(Particle.FALLING_LAVA, loc, 5, 0.2, 0.2, 0.2, 0.1);
        }, 0, 2);
            },i*15);
        }
    }
    public static void phaseOne(Snowball snowball, Block hitBlock, Player player) {
        Snowball snowballChild = snowball.getWorld().spawn(snowball.getLocation().add(0,spawnhoehe,0), Snowball.class);
        Vector direction = snowball.getLocation().getDirection();
        direction.setY(-speed); // Set Y component to 1.0 for upward motion
        direction.setX(0); // Set X component to 0
        direction.setZ(0); // Set Z component to 0
        snowballChild.setVelocity(direction);
        snowballChild.setShooter(snowball.getShooter());
        snowballChild.setMetadata("spawnedBy", new FixedMetadataValue(plugin, "GlitzerMeteorTwo"));

        String colorPara = Painter.getColorPara(player);
        Bukkit.getScheduler().runTaskTimer(plugin, () -> {
            if (snowballChild.isDead() || !snowballChild.isValid()) {
                return;
            }

            snowballChild.getWorld().playSound(snowballChild.getLocation(), Sound.ENTITY_FIREWORK_ROCKET_BLAST, 0.75f, 2.0f);
            Verteiler.playColorParticleBubble(colorPara, hitBlock.getLocation().add(0,1,0), radius, 20, 0.5, Particle.DUST);
            hitBlock.getWorld().spawnParticle(Particle.CRIMSON_SPORE, hitBlock.getLocation(), 15, 3, 1, 3, 0.05);
            snowballChild.getWorld().spawnParticle(Particle.WITCH, snowballChild.getLocation(), 15, 1, 1, 1, 0.05);
            snowballChild.getWorld().spawnParticle(Particle.SMOKE, snowballChild.getLocation(), 30, 0.1, 0.1, 0.1, 0.05);
            snowballChild.getWorld().spawnParticle(Particle.CAMPFIRE_COSY_SMOKE, snowballChild.getLocation(), 4, 0, 1, 0, 0.05);
        },0 ,2); // 20 Ticks = 1 Sekunde
    }

    public static void phaseTwo(Snowball snowball, Block hitBlock,Player player,String color) {
        Block newBlock = hitBlock.getLocation().getBlock();
        Painter.explodeAlgorithmSphere(newBlock,radius,player,color);
        snowball.getWorld().playSound(hitBlock.getLocation(), Sound.ENTITY_ELDER_GUARDIAN_DEATH, 2.0f, 0.25f);
        snowball.getWorld().playSound(hitBlock.getLocation(), Sound.ENTITY_WITHER_DEATH, 0.5f, 2.0f);
        snowball.getWorld().playSound(hitBlock.getLocation(), Sound.ENTITY_FIREWORK_ROCKET_LARGE_BLAST_FAR, 3.0f, 0.75f);
        snowball.getWorld().spawnParticle(Particle.EXPLOSION_EMITTER, snowball.getLocation(), 5, 1, 0, 1, 0.05);
        snowball.getWorld().spawnParticle(Particle.END_ROD, snowball.getLocation(), 60, 0, 0, 0, 0.5);
    }
}
