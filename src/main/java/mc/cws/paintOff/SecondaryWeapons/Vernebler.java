package mc.cws.paintOff.SecondaryWeapons;

import mc.cws.paintOff.Game.Extras.Painter;
import mc.cws.paintOff.Game.Resources.UltPoints;
import mc.cws.paintOff.Game.Resources.FulePoints;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

public class Vernebler {
    private static PaintOffMain plugin;
    public static void setPlugin(PaintOffMain plugin) {Vernebler.plugin = plugin;}
    public static int verbrauch = 180;
    public static int width = 4;
    public static int duration = 12; // 12*5 = 60 ticks = 3 seconds
    public static int effektDuration = 8;
    public static int effektDichte = 20;
    static int requiredPoints;

    public static void getItem(Player player) {
        ItemStack carrot = new ItemStack(Material.FIREWORK_STAR, 1);
        ItemMeta meta = carrot.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(ChatColor.DARK_PURPLE + "Vernebler");
            List<String> lore = new ArrayList<>();
            lore.add(ChatColor.GRAY + "Wirft eine Nebelkugel.");
            lore.add("");
            lore.add(ChatColor.DARK_PURPLE + "Verbrauch: " + ChatColor.LIGHT_PURPLE + ChatColor.BOLD + verbrauch + " Farbe/Schuss");
            meta.setLore(lore);
            carrot.setItemMeta(meta);
        }
        player.getInventory().setItem(6, carrot);
    }

    public static void shoot(Player player, String color, boolean grusel) {
        int n = Start.getQueueNumber(player);
        if (grusel) {
            if (UltPoints.hasEnoughUltPoints(player,UltPoints.getUltPoints(player)/2)) {
                // Remove ult points
                requiredPoints = (UltPoints.getUltPoints(player)/2);
                UltPoints.removeUltPoints(player,requiredPoints);
                player.playSound(player.getLocation(), Sound.ENTITY_WITHER_AMBIENT, 2.5f, 3.0f);
                player.playSound(player.getLocation(), Sound.ENTITY_WITHER_SHOOT, 2.5f, 1.0f);
                Snowball snowball = player.launchProjectile(Snowball.class);
                snowball.setGravity(false);
                snowball.setVelocity(player.getLocation().getDirection().multiply(1.0));
                snowball.setMetadata("spawnedBy", new FixedMetadataValue(plugin, "Grusel"));
                return;
            }
        } else if (FulePoints.isAbleToPerform(player, verbrauch)) {
            // Play custom sound
            player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_HAT, 0.1f, 2.0f);
            // Shoot snowball
            Snowball snowball = player.launchProjectile(Snowball.class);
            snowball.setGravity(false);
            snowball.setVelocity(player.getLocation().getDirection().multiply(1.0));
            snowball.setMetadata("spawnedBy", new FixedMetadataValue(plugin, "Vernebler"));


            Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                if (snowball.isDead() || !snowball.isValid()) {
                    return;
                }
                Location loc = snowball.getLocation();
                snowball.getWorld().playSound(snowball.getLocation(), Sound.ENTITY_CREEPER_DEATH, 0.5f, 1.0f);
                snowball.getWorld().spawnParticle(Particle.SONIC_BOOM, loc, 1, 0, 0, 0, 0.1);
                phaseTwo(player, snowball.getLocation(), n);
                snowball.remove();
            }, (20));
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
        snowball.getWorld().spawnParticle(Particle.SONIC_BOOM, loc, 1, 0, 0, 0, 0.1);
        Painter.paintBlockWithUltpoint(hitBlock,player,n,color);
        Vernebler.phaseTwo(player, hitBlock.getLocation(), n);
        // Play sound effect
        hitBlock.getWorld().playSound(hitBlock.getLocation(), Sound.ENTITY_CREEPER_DEATH, 0.5f, 1.0f);

    }
    public static void phaseTwo(Player player, Location hitLocation, int n) {
        AtomicBoolean stop = new AtomicBoolean(false);
        Bukkit.getScheduler().runTaskTimer(plugin, () -> {
            if (stop.get()) {
                return;
            }
            if (Verteiler.teamB.get(n).contains(player)) {
                for (Player targetPlayer : Verteiler.teamA.get(n)) {
                    Location playerLoc = targetPlayer.getLocation();
                    if (playerLoc.distance(hitLocation) <= width) {
                        targetPlayer.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, effektDuration*5, 0));
                        targetPlayer.addPotionEffect(new PotionEffect(PotionEffectType.SLOWNESS, effektDuration*5, 0));
                    }
                }
            } else if (Verteiler.teamA.get(n).contains(player)) {
                for (Player targetPlayer : Verteiler.teamB.get(n)) {
                    Location playerLoc = targetPlayer.getLocation();
                    if (playerLoc.distance(hitLocation) <= width) {
                        targetPlayer.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, effektDuration*5, 0));
                        targetPlayer.addPotionEffect(new PotionEffect(PotionEffectType.SLOWNESS, effektDuration*5, 0));
                    }
                }
            }
            // Add visual effects
            Objects.requireNonNull(hitLocation.getWorld()).spawnParticle(Particle.WHITE_ASH, hitLocation, effektDichte, width-2, width-2, width-2, 0);
            Objects.requireNonNull(hitLocation.getWorld()).spawnParticle(Particle.ASH, hitLocation, effektDichte, width-2, width-2, width-2, 0);
            hitLocation.getWorld().spawnParticle(Particle.SMOKE, hitLocation, effektDichte*2, width-2, width-2, width-2, 0);
            hitLocation.getWorld().spawnParticle(Particle.CAMPFIRE_COSY_SMOKE, hitLocation, 2, width-2, width-2, width-2, 0);
        }, 0, 2);

        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            stop.set(true);
        }, duration* 5L);
    }

    public static void phaseGrusel(Player player, Location hitLocation, int n) {
        Objects.requireNonNull(hitLocation.getWorld()).playSound(hitLocation, Sound.ENTITY_ZOMBIE_AMBIENT, 1.0f, 0.5f);
        Objects.requireNonNull(hitLocation.getWorld()).playSound(hitLocation, Sound.ENTITY_GHAST_HURT, 1.0f, 0.5f);
        Objects.requireNonNull(hitLocation.getWorld()).spawnParticle(Particle.SONIC_BOOM, hitLocation, 1, 0, 0, 0, 0.1);
        AtomicBoolean stop = new AtomicBoolean(false);
        Bukkit.getScheduler().runTaskTimer(plugin, () -> {
            if (stop.get()) {
                return;
            }
            if (Verteiler.teamB.get(n).contains(player)) {
                for (Player targetPlayer : Verteiler.teamA.get(n)) {
                    Location playerLoc = targetPlayer.getLocation();
                    if (playerLoc.distance(hitLocation) <= width*2) {
                        targetPlayer.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, effektDuration*2*5, 0));
                        targetPlayer.addPotionEffect(new PotionEffect(PotionEffectType.DARKNESS, effektDuration*2*5, 0));
                    }
                }
            } else if (Verteiler.teamA.get(n).contains(player)) {
                for (Player targetPlayer : Verteiler.teamB.get(n)) {
                    Location playerLoc = targetPlayer.getLocation();
                    if (playerLoc.distance(hitLocation) <= width*2) {
                        targetPlayer.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, effektDuration*2*5, 0));
                        targetPlayer.addPotionEffect(new PotionEffect(PotionEffectType.DARKNESS, effektDuration*2*5, 0));
                    }
                }
            }
            // Add visual effects
            Objects.requireNonNull(hitLocation.getWorld()).spawnParticle(Particle.WHITE_ASH, hitLocation, effektDichte*4, width*2-3, width*2-3, width*2-3, 0);
            Objects.requireNonNull(hitLocation.getWorld()).spawnParticle(Particle.ASH, hitLocation, effektDichte*4, width*2-3, width*2-3, width*2-3, 0);
            hitLocation.getWorld().spawnParticle(Particle.SMOKE, hitLocation, effektDichte*8, width*2-3, width*2-3, width*2-3, 0);
        }, 0, 2);

        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            stop.set(true);
        }, duration*2* 5L);
    }
}
