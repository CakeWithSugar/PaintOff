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
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

public class Sonnenwindler {
    private static PaintOffMain plugin;
    public static void setPlugin(PaintOffMain plugin) {
        Sonnenwindler.plugin = plugin;}

    public static int requiredPoints = 0;
    public static int reichweite = 7;
    public static double abnahme = 0.2;
    public static double hight = 0.75;

    public static void getItem(Player player) {
        ItemStack carrot = new ItemStack(Material.MAGMA_CREAM, 1);
        ItemMeta meta = carrot.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(ChatColor.DARK_PURPLE + "Sonnenwindler");
            List<String> lore = new ArrayList<>();
            lore.add(ChatColor.GRAY + "Schießt einen Schutzschirm.");
            lore.add("");
            lore.add(ChatColor.GRAY + "Originalreichweite: " + ChatColor.GOLD + reichweite + ChatColor.DARK_GRAY + " Blöcke");
            lore.add(ChatColor.GRAY + "Reichweiten Abnahmerate: " + ChatColor.GOLD + abnahme + ChatColor.DARK_GRAY + " m/s");
            lore.add(ChatColor.GRAY + "Originalhöhenmultiplikator: " + ChatColor.GOLD + hight + ChatColor.DARK_GRAY + " m/s");
            meta.setLore(lore);
            carrot.setItemMeta(meta);
        }
        player.getInventory().setItem(1, carrot);
    }

    public static void handleItemUsage(Player player) {
        if (UltPoints.hasEnoughUltPoints(player,UltPoints.getUltPoints(player))) {
            // Remove ult points
            requiredPoints = (UltPoints.getUltPoints(player));
            UltPoints.removeUltPoints(player, requiredPoints );
            player.playSound(player.getLocation(), Sound.ENTITY_BLAZE_DEATH, 0.25f, 0.75f);
            player.playSound(player.getLocation(), Sound.ENTITY_BLAZE_SHOOT, 2.0f, 0.25f);
            launch(player);
        }
    }

    public static void launch(Player player) {
        player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_HAT, 0.1f, 2.0f);
        if (plugin == null) {
            System.err.println("Plugin ist null in launchSonnenwindler!");
            return;
        }

        Snowball snowball = player.launchProjectile(Snowball.class);
        snowball.setVelocity(player.getLocation().getDirection().multiply(0.6));
        snowball.setMetadata("spawnedBy", new FixedMetadataValue(plugin, "Sonnenwindler"));

        // Add particle trail
        String colorPara = Painter.getColorPara(player);
        Bukkit.getScheduler().runTaskTimer(plugin, () -> {
            if (snowball.isDead() || !snowball.isValid()) {
                return;
            }
            Location loc = snowball.getLocation();
            Verteiler.playColorParticleBubble(colorPara, snowball.getLocation(), 0.1, 1, 0.1, Particle.DUST);
            snowball.getWorld().spawnParticle(Particle.END_ROD, loc, 3, 0.1, 0.1, 0.1, 0.05);
        }, 0, 1);
    }

    public static void phaseOne(Snowball snowball, Block hitBlock, Player player) {
        if (hitBlock == null) {
            return;
        }
        Snowball snowballChild = snowball.getWorld().spawn(snowball.getLocation(), Snowball.class);
        Vector direction = snowball.getVelocity();
        direction.setY(hight);
        direction.setX(0);
        direction.setZ(0);
        snowballChild.setVelocity(direction);
        snowballChild.setShooter(snowball.getShooter());
        snowballChild.setGlowing(true);
        snowballChild.setMetadata("spawnedBy", new FixedMetadataValue(plugin, "Higher"));
        hitBlock.getWorld().playSound(hitBlock.getLocation(), Sound.ENTITY_FIREWORK_ROCKET_LAUNCH, 2.0f, 2.0f);

        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            if (snowballChild.isDead() || !snowballChild.isValid()) {
                return;
            }
            phaseTwo(player, snowballChild, hitBlock);
            snowballChild.remove();
        }, 20); // 20 Ticks = 1 Sekunde
    }

    private static void phaseTwo(Player player, Snowball snowball, Block hitBlock) {
        if (hitBlock == null) {
            return;
        }
        AtomicReference<Double> distance = new AtomicReference<>((double) reichweite);
        Snowball snowballChild = snowball.getWorld().spawn(snowball.getLocation(), Snowball.class);
        Vector direction = snowball.getVelocity();
        direction.setY(-0.05); // Set Y component to 1.0 for upward motion
        direction.setX(0); // Set X component to 0
        direction.setZ(0); // Set Z component to 0
        snowballChild.setVelocity(direction);
        snowballChild.setShooter(snowball.getShooter());
        snowballChild.setGlowing(true);
        snowballChild.setGravity(false);
        snowballChild.setMetadata("spawnedBy", new FixedMetadataValue(plugin, "Higher"));
        String colorPara = Painter.getColorPara(player);
        Bukkit.getScheduler().runTaskTimer(plugin, () -> {
            if (snowballChild.isDead() || !snowballChild.isValid()) {
                return;
            }
            Location shieldCenter = snowballChild.getLocation();
            double currentRadius = distance.updateAndGet(v -> (v - abnahme));

            // Get team of the player who launched the shield
            int n = Start.getQueueNumber(player);
            String team = Verteiler.getTeam(player, n);

            // Get the list of players to affect (opposite team)
            List<Player> playersToAffect = team.equals("A") ? Verteiler.teamA.get(n) : Verteiler.teamB.get(n);

            // Visual effect and player check for the shield
            for (int i = 0; i < 20; i++) {
                double angle = 2 * Math.PI * i / 20;
                double x = Math.cos(angle) * currentRadius;
                double z = Math.sin(angle) * currentRadius;
                double y = hitBlock.getLocation().add(0, 1.1, 0).getY() - snowballChild.getLocation().getY();
                Location particleLoc = shieldCenter.clone().add(x, y, z);

                // Spawn the particle effect
                Objects.requireNonNull(shieldCenter.getWorld()).spawnParticle(Particle.FLAME,
                        snowballChild.getLocation(), 1, 0, 0, 0, 0.05);
                Objects.requireNonNull(shieldCenter.getWorld()).spawnParticle(Particle.END_ROD,
                        particleLoc, 1, 0, 0, 0, 0.01);
                Verteiler.playColorParticleBubble(colorPara, particleLoc.add(0, 0.25,0), 0.2, 4, 0.2, Particle.DUST);
                Verteiler.playColorParticleBubble(colorPara, snowballChild.getLocation(), 0.1, 2, 0.1, Particle.DUST);
            }
            for (Player target : playersToAffect) {
                if (target.getWorld().equals(shieldCenter.getWorld())) {
                    Location targetLoc = target.getLocation();

                    // Get the shield's center location
                    Location shieldLoc = shieldCenter.clone();

                    // Calculate 2D distance (ignoring Y)
                    double dx = targetLoc.getX() - shieldLoc.getX();
                    double dz = targetLoc.getZ() - shieldLoc.getZ();
                    double dy = hitBlock.getLocation().add(0, 1.1, 0).getY() - snowballChild.getLocation().getY();
                    double distanceSquared = dx * dx + dz * dz;

                    // Check if player is within the circle's radius and at the correct height
                    if (distanceSquared <= currentRadius * currentRadius) {
                        // Check if player is at the correct height (using the calculated dy)
                        if (Math.abs(targetLoc.getY() - (shieldLoc.getY() + dy)) <= 1.0) {
                            // Apply effect to the player
                            target.addPotionEffect(new PotionEffect(PotionEffectType.RESISTANCE, 20, 0, false, false));
                        }
                    }
                }
            }

            // Remove the shield when radius reaches 0
            if (currentRadius <= 1) {
                Objects.requireNonNull(shieldCenter.getWorld()).spawnParticle(
                        Particle.FIREWORK, snowballChild.getLocation(), 40, 0, 0, 0, 0.1);
                Objects.requireNonNull(shieldCenter.getWorld()).spawnParticle(
                        Particle.END_ROD, snowballChild.getLocation(), 120, 0, 0, 0, 0.5);
                hitBlock.getWorld().playSound(hitBlock.getLocation(), Sound.ENTITY_FIREWORK_ROCKET_BLAST_FAR, 2.0f, 0.5f);
                hitBlock.getWorld().playSound(hitBlock.getLocation(), Sound.ENTITY_FIREWORK_ROCKET_TWINKLE_FAR, 4.0f, 1.0f);
                snowballChild.remove();
            }
        }, 0, 10);

        Objects.requireNonNull(snowballChild.getWorld()).spawnParticle(
                Particle.END_ROD, snowballChild.getLocation(), 60, 0, 0, 0, 0.5);
        hitBlock.getWorld().playSound(hitBlock.getLocation(), Sound.ENTITY_BLAZE_BURN, 2.0f, 0.5f);
        hitBlock.getWorld().playSound(hitBlock.getLocation(), Sound.ENTITY_BLAZE_SHOOT, 1.0f, 2.0f);
    }
}
