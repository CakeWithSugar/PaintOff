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
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class PlatzRegen {
    private static PaintOffMain plugin;
    public static void setPlugin(PaintOffMain plugin) {
        PlatzRegen.plugin = plugin;}

    public static int requiredPoints = 0;
    public static int duration = 12;
    public static double speedNegator = 8;
    public static int regentropfen = 6;
    private static Vector cloudDirection = new Vector(0, 0, 0); // Alle Komponenten auf 0 setzen

    public static void getItem(Player player) {
        ItemStack carrot = new ItemStack(Material.NETHERITE_UPGRADE_SMITHING_TEMPLATE, 1);
        ItemMeta meta = carrot.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(ChatColor.DARK_PURPLE + "Platzregen");
            List<String> lore = new ArrayList<>();
            lore.add(ChatColor.GRAY + "Wirft eine Gewitterwolke.");
            lore.add("");
            lore.add(ChatColor.GRAY + "Verweildauer: " + ChatColor.GOLD + duration + ChatColor.DARK_GRAY + " Sekunden");
            lore.add(ChatColor.GRAY + "Geschwindigkeit: " + ChatColor.GOLD + "Wurfgeschwindigkeit / " + speedNegator + ChatColor.DARK_GRAY + " m/s");
            lore.add(ChatColor.GRAY + "Regentropfen: " + ChatColor.GOLD + (regentropfen/2) + ChatColor.DARK_GRAY + " pro Tick");
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
            player.playSound(player.getLocation(), Sound.ENTITY_BREEZE_IDLE_AIR, 0.5f, 0.75f);
            player.playSound(player.getLocation(), Sound.ENTITY_BREEZE_WIND_BURST, 0.25f, 0.25f);
            launch(player);
        }
    }

    public static void launch(Player player) {
        // Play custom sound
        player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_HAT, 0.1f, 2.0f);
        int n = Start.getQueueNumber(player);

        // Überprüfen, ob plugin nicht null ist
        if (plugin == null) {
            System.err.println("Plugin ist null in launchPlatzRegen!");
            return;
        }

        // Launch arrow with reduced gravity and speed
        Snowball snowball = player.launchProjectile(Snowball.class);
        snowball.setVelocity(player.getLocation().getDirection().multiply(0.8));
        cloudDirection = snowball.getVelocity();
        snowball.setMetadata("spawnedBy", new FixedMetadataValue(plugin, "PlatzRegen"));

        // Add particle trail
        String colorPara = Painter.getColorPara(player);
        Bukkit.getScheduler().runTaskTimer(plugin, () -> {
            if (snowball.isDead() || !snowball.isValid()) {
                return;
            }
            Location loc = snowball.getLocation();
            Verteiler.playColorParticleBubble(colorPara, snowball.getLocation(), 0.1, 1, 0.1, Particle.DUST);
            snowball.getWorld().spawnParticle(Particle.DRIPPING_DRIPSTONE_WATER, loc, 1, 0.1, 0.1, 0.1, 0.01);
        }, 0, 1);
    }

    public static void phaseOne(Snowball snowball, Block hitBlock,Player player) {
        Location loc = snowball.getLocation().add(0, 1, 0);
        Snowball snowballChild = snowball.getWorld().spawn(snowball.getLocation(), Snowball.class);
        Vector direction = snowball.getLocation().getDirection();
        direction.setY(0.8); // Set Y component to 1.0 for upward motion
        direction.setX(0); // Set X component to 0
        direction.setZ(0); // Set Z component to 0
        snowballChild.setVelocity(direction);
        snowballChild.setShooter(snowball.getShooter());
        snowball.getWorld().spawnParticle(Particle.SONIC_BOOM, loc, 1, 0, 0, 0, 0.005);
        hitBlock.getWorld().playSound(hitBlock.getLocation(), Sound.BLOCK_SLIME_BLOCK_HIT, 1.0f, 2.0f);

        Bukkit.getScheduler().runTaskTimer(plugin, () -> {
            if (snowballChild.isDead() || !snowballChild.isValid()) {
                return;
            }
            Location loc2 = snowballChild.getLocation();
            snowballChild.getWorld().spawnParticle(Particle.WARPED_SPORE, loc2, 10, 0, 0, 0, 0.1);
            snowballChild.getWorld().spawnParticle(Particle.CLOUD, loc2, 3, 0, 0, 0, 0.05);
        }, 0, 1);
        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            if (snowballChild.isDead() || !snowballChild.isValid()) {
                return;
            }
            PlatzRegen.phaseTwo(player, snowballChild.getLocation());
            snowballChild.remove();
        }, 20); // 20 Ticks = 1 Sekunde
    }

    public static void phaseTwo(Player player, Location hitLocation) {
        Snowball snowballChild = hitLocation.getWorld().spawn(hitLocation, Snowball.class);
        Vector direction = cloudDirection; // Alle Komponenten auf 0 setzen
        direction.setX(direction.getX() / PlatzRegen.speedNegator);
        direction.setY(0);
        direction.setZ(direction.getZ() / PlatzRegen.speedNegator);
        snowballChild.setVelocity(direction);
        snowballChild.setGravity(false);
        snowballChild.setVisibleByDefault(false);
        snowballChild.teleport(hitLocation);
        snowballChild.setShooter(player);
        hitLocation.getWorld().spawnParticle(Particle.SONIC_BOOM, hitLocation, 1, 0, 0, 0, 0.005);
        hitLocation.getWorld().playSound(hitLocation, Sound.ENTITY_LIGHTNING_BOLT_THUNDER, 5.0f, 2.0f);
        hitLocation.getWorld().playSound(hitLocation, Sound.ENTITY_LIGHTNING_BOLT_THUNDER, 2.75f, 1.0f);
        hitLocation.getWorld().playSound(hitLocation, Sound.ENTITY_LIGHTNING_BOLT_THUNDER, 0.5f, 0.5f);
        cloudDirection = new Vector(0, 0, 0);
        // Wähle 2 zufällige Positionen aus


        Vector originalDirection = direction.clone();
        double speed = originalDirection.length();

        Bukkit.getScheduler().runTaskTimer(plugin, () -> {
            if (snowballChild.isDead() || !snowballChild.isValid()) {
                return;
            }
            // Setze die Geschwindigkeit auf den ursprünglichen Wert
            Vector currentDirection = snowballChild.getVelocity();
            currentDirection.normalize();
            currentDirection.multiply(speed);
            snowballChild.setVelocity(currentDirection);
        }, 0, 1);

        // Spawn Partikelfolge
        String colorPara = Painter.getColorPara(player);
        Bukkit.getScheduler().runTaskTimer(plugin, () -> {
            if (snowballChild.isDead() || !snowballChild.isValid()) {
                return;
            }
            Location loc2 = snowballChild.getLocation();
            snowballChild.getWorld().spawnParticle(Particle.FALLING_DRIPSTONE_WATER, loc2, 5, 3, 0, 3, 0.5);
            snowballChild.getWorld().spawnParticle(Particle.WHITE_SMOKE, loc2, 75, 3, 0.5, 3, 0.05);
            snowballChild.getWorld().spawnParticle(Particle.CLOUD, loc2, 75, 3, 0.5, 3, 0.05);
            snowballChild.getWorld().spawnParticle(Particle.FIREWORK, loc2, 50, 3, 0.5, 3, 0.05);
            snowballChild.getWorld().spawnParticle(Particle.SNOWFLAKE, loc2, 25, 3, 0.5, 3, 0.05);
            snowballChild.getWorld().spawnParticle(Particle.PALE_OAK_LEAVES, loc2, 1, 3, 0.5, 3, 0.5);
            Verteiler.playColorParticleBubble(colorPara, snowballChild.getLocation(), 5, 40, 1.5, Particle.DUST);


            List<Location> possibleLocations = new ArrayList<>();
            int radius = 6;
            for (int x = -radius; x <= radius; x++) {
                for (int y = -1; y <= 1; y++) { // Nur 3 Schichten in Y-Richtung
                    for (int z = -radius; z <= radius; z++) {
                        Location loc = loc2.clone().add(x, y, z);
                        if (loc.getBlock().getType() == Material.AIR) { // Nur Luft-Blocks
                            possibleLocations.add(loc);
                        }
                    }
                }
            }

            Collections.shuffle(possibleLocations);
            int numLocations = Math.min(PlatzRegen.regentropfen, possibleLocations.size());
            // Spawn Schneebälle an den zufälligen Positionen
            List<int[]> excludeCoords = Arrays.asList(
                    new int[]{6, 6},  // x=6, z=6
                    new int[]{5, 5},  // x=5, z=5
                    new int[]{6, 5},  // x=6, z=5
                    new int[]{5, 6},  // x=5, z=6
                    new int[]{3, 6},  // x=3, z=6
                    new int[]{4, 6},  // x=4, z=6
                    new int[]{6, 3},  // x=6, z=3
                    new int[]{6, 4},   // x=6, z=4

                    new int[]{-6, 6},
                    new int[]{-5, 5},
                    new int[]{-6, 5},
                    new int[]{-5, 6},
                    new int[]{-3, 6},
                    new int[]{-4, 6},
                    new int[]{-6, 3},
                    new int[]{-6, 4},

                    new int[]{-6, -6},
                    new int[]{-5, -5},
                    new int[]{-6, -5},
                    new int[]{-5, -6},
                    new int[]{-3, -6},
                    new int[]{-4, -6},
                    new int[]{-6, -3},
                    new int[]{-6, -4},

                    new int[]{6, -6},
                    new int[]{5, -5},
                    new int[]{6, -5},
                    new int[]{5, -6},
                    new int[]{3, -6},
                    new int[]{4, -6},
                    new int[]{6, -3},
                    new int[]{6, -4}
            );

            for (int i = 0; i < numLocations; i++) {
                Location randomLoc = possibleLocations.get(i);
                // Exclude if the coordinate matches any of the specified locations
                if (Painter.shouldExcludeAnyLocation(randomLoc, loc2, excludeCoords)) {
                    continue; // Skip this location and try the next one
                }
                Snowball childSnowball = snowballChild.getWorld().spawn(randomLoc, Snowball.class);
                childSnowball.setVelocity(new Vector(0, 0, 0));
                childSnowball.setGravity(true);
                childSnowball.setMetadata("spawnedBy", new FixedMetadataValue(plugin, "RainChild"));
                childSnowball.setVisibleByDefault(true);
                childSnowball.getWorld().spawnParticle(Particle.SCRAPE, randomLoc, 3, 0.1, 0.1, 0.1, 0.05);
                childSnowball.setShooter(player);
            }
        }, 0, 2);


        Bukkit.getScheduler().runTaskTimer(plugin, () -> {
            if (snowballChild.isDead() || !snowballChild.isValid()) {
                return;
            }
            Location loc2 = snowballChild.getLocation();
            snowballChild.getWorld().playSound(loc2, Sound.ENTITY_LIGHTNING_BOLT_THUNDER, 1.0f, 2.0f);

        }, 0, 20L *(PlatzRegen.duration/4));
        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            if (snowballChild.isDead() || !snowballChild.isValid()) {
                return;
            }
            snowballChild.remove();
        }, 20L *PlatzRegen.duration); // 20 Ticks = 10 Sekunde
    }

    public static void phaseThree(Snowball snowball,Block hitBlock, Player player,String color) {
        Location loc = snowball.getLocation().add(0, 0.1, 0);
        Painter.paintBlockWithoutUltpoint(hitBlock,color,player);
        hitBlock.getWorld().spawnParticle(Particle.GLOW, loc, 1, 0, 0, 0, 0.1);
    }
}
