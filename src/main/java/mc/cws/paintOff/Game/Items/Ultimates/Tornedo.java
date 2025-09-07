package mc.cws.paintOff.Game.Items.Ultimates;

import mc.cws.paintOff.Configuration;
import mc.cws.paintOff.Game.Extras.Painter;
import mc.cws.paintOff.Game.Resources.UltPoints;
import mc.cws.paintOff.Game.Management.Start;
import mc.cws.paintOff.Game.Management.Stop;
import mc.cws.paintOff.Game.Management.Verteiler;
import mc.cws.paintOff.PaintOffMain;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class Tornedo {
    private static PaintOffMain plugin;
    public static void setPlugin(PaintOffMain plugin) {
        Tornedo.plugin = plugin;}

    public static int requiredPoints = 0;
    public static int lifecycle = 5;
    public static int schaden = 16;
    public static int explosion = 6;
    public static double geschwindigkeitMult = 1.5;
    public static double startSpeed = 0.25;
    public static int[] rangeAddition = new int[Configuration.maxQueues];

    public static void getItem(Player player) {
        ItemStack carrot = new ItemStack(Material.POINTED_DRIPSTONE, 1);
        ItemMeta meta = carrot.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(ChatColor.DARK_PURPLE + "Tornedo");
            List<String> lore = new ArrayList<>();
            lore.add(ChatColor.GRAY + "Schießt einen Torpedo.");
            lore.add("");
            lore.add(ChatColor.GRAY + "Verweildauer: " + ChatColor.GOLD + lifecycle + ChatColor.DARK_GRAY + " Sekunden");
            lore.add(ChatColor.GRAY + "Schaden: " + ChatColor.GOLD + (schaden/2) + ChatColor.DARK_GRAY + " Herzen");
            lore.add(ChatColor.GRAY + "Explosionsradius: " + ChatColor.GOLD + (explosion+1) + ChatColor.DARK_GRAY + " Blöcke");
            lore.add(ChatColor.GRAY + "Geschwindigkeit Multiplikator: " + ChatColor.GOLD + geschwindigkeitMult + ChatColor.DARK_GRAY + " m/s");
            lore.add(ChatColor.GRAY + "Startgeschwindigkeit: " + ChatColor.GOLD + startSpeed + ChatColor.DARK_GRAY + " m/s");
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
            player.playSound(player.getLocation(), Sound.ENTITY_CREEPER_DEATH, 0.25f, 0.75f);
            player.playSound(player.getLocation(), Sound.ENTITY_FIREWORK_ROCKET_LAUNCH, 0.5f, 0.25f);
            launch(player);
        }
    }

    public static void launch(Player player) {
        // Play custom sound
        player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_HAT, 0.1f, 2.0f);
        int n = Start.getQueueNumber(player);

        // Launch arrow with reduced gravity and speed
        Arrow arrow = player.launchProjectile(Arrow.class);
        arrow.setGravity(false); // Disable gravity
        double speed = Tornedo.geschwindigkeitMult;
        arrow.setVelocity(player.getLocation().getDirection().multiply(Tornedo.startSpeed)); // Slow speed
        arrow.setGlowing(true);
        arrow.setMetadata("spawnedBy", new FixedMetadataValue(plugin, "Tornedo"));

        // Add particle trail
        String colorPara = Painter.getColorPara(player);
        Bukkit.getScheduler().runTaskTimer(plugin, () -> {
            if (arrow.isDead() || !arrow.isValid()) {
                return;
            }
            Location loc = arrow.getLocation();
            Verteiler.playColorParticleBubble(colorPara, arrow.getLocation(), 0.1, 1, 0.1, Particle.DUST);
            arrow.getWorld().spawnParticle(Particle.SOUL_FIRE_FLAME, loc, 1, 0.1, 0.1, 0.1, 0.001);
        }, 0, 1);
        Bukkit.getScheduler().runTaskTimer(plugin, () -> {
            if (arrow.isDead() || !arrow.isValid()) {
                return;
            }
            Vector velocity = arrow.getVelocity();

            // Multipliziere die Geschwindigkeit mit 0.99 (nur 1% Abbremsung)
            velocity.multiply(speed);

            // Setze die neue Geschwindigkeit
            arrow.setVelocity(velocity);

            Location loc = arrow.getLocation();
            rangeAddition[n] += 1;
            if (rangeAddition[n] > Tornedo.lifecycle) {
                arrow.getWorld().spawnParticle(Particle.FIREWORK, loc, 50, 0.1, 0.1, 0.1, 0.1);
                Block block = loc.getBlock();
                phaseOne(block,player,n);
                arrow.remove();
                rangeAddition[n] = 0;
                return;
            }
            arrow.getWorld().spawnParticle(Particle.SONIC_BOOM, loc, 1, 0.1, 0.1, 0.1, 0.1);
        }, 0, 20);
    }

    public static void phaseOne(Block hitBlock, Player player, int n) {
        String team = Verteiler.getTeam(player, n);
        if (team == null || !team.equals("A") && !team.equals("B")) {
            return;
        }
        String color = team.equals("A") ? Stop.getColorNameA(n) : Stop.getColorNameB(n);
        if (rangeAddition[n] > Tornedo.lifecycle) {
            rangeAddition[n] = Tornedo.lifecycle;
        }
        String colorPara = Painter.getColorPara(player);
        AtomicBoolean stop = new AtomicBoolean(false);
        Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, () -> {
            if (stop.get()) {
                return;
            }
            hitBlock.getWorld().playSound(hitBlock.getLocation(), Sound.ENTITY_BREEZE_CHARGE, 1.0f, 0.5f);
            hitBlock.getWorld().playSound(hitBlock.getLocation(), Sound.ENTITY_BREEZE_IDLE_GROUND, 1.0f, 1.5f);
            hitBlock.getWorld().spawnParticle(Particle.TOTEM_OF_UNDYING, hitBlock.getLocation().add(0,  0.75, 0), 10, 0.1, 0.1, 0.1, 0.5);
            Verteiler.playColorParticleBubble(colorPara, hitBlock.getLocation(), 6, 40, 6, Particle.DUST);
            hitBlock.getWorld().spawnParticle(Particle.FIREWORK, hitBlock.getLocation().add(0, 0, 0), 25, 3, 3, 3, 0.01);
        },0,4);
        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            int worth = Tornedo.explosion -1;
            // Add visual effects
            hitBlock.getWorld().spawnParticle(Particle.FLASH, hitBlock.getLocation(), 6, 0, 1, 0, 0.1);

            // Play sound effect
            hitBlock.getWorld().playSound(hitBlock.getLocation(), Sound.ENTITY_DRAGON_FIREBALL_EXPLODE, 2.0f, 0.01f);
            hitBlock.getWorld().playSound(hitBlock.getLocation(), Sound.ENTITY_LIGHTNING_BOLT_THUNDER, 1.0f, 0.75f);
            Painter.explosionAlgorithmWithoutUltpoint(hitBlock, player, n, worth, color, Tornedo.schaden);
            if (!stop.get()) {
                stop.set(true);
                return;
            }
        }, 20L * (Tornedo.lifecycle-rangeAddition[n]));
        rangeAddition[n] = 0;
    }
}
