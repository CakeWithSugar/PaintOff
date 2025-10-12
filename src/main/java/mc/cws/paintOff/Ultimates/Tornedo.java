package mc.cws.paintOff.Ultimates;

import mc.cws.paintOff.Game.Extras.Painter;
import mc.cws.paintOff.Game.Management.InGame.DamageDealer;
import mc.cws.paintOff.Game.Management.InGame.Game;
import mc.cws.paintOff.Game.Resources.UltPoints;
import mc.cws.paintOff.Game.Management.Start;
import mc.cws.paintOff.Game.Management.Stop;
import mc.cws.paintOff.Game.Management.Verteiler;
import mc.cws.paintOff.PaintOffMain;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.Vector;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

public class Tornedo {
    private static PaintOffMain plugin;
    public static void setPlugin(PaintOffMain plugin) {
        Tornedo.plugin = plugin;}

    public static int requiredPoints = 0;
    public static int lifecycle = 5;
    public static int schaden = 8;
    public static int explosion = 6;
    public static double geschwindigkeitMult = 1.5;
    public static double startSpeed = 0.25;
    public static double beamLength = 100.0;
    public static double helixRadius = 2.5;
    public static double helixFrequency = -2.0;
    public static int prepTime = 4;
    public static int time = (prepTime*2)+2;

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
        player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_HAT, 0.1f, 2.0f);
        int n = Start.getQueueNumber(player);
        String team = Verteiler.getTeam(player,n);

        Location startLoc = player.getLocation().clone();
        startLoc.add(0,1,0);
        Vector direction = startLoc.getDirection().normalize();

        AtomicBoolean stop = new AtomicBoolean(false);
        AtomicBoolean prep = new AtomicBoolean(false);
        String colorPara = Painter.getColorPara(player);
        String colorName = team.equals("A") ? Stop.getColorNameA(n) : Stop.getColorNameB(n);

        final int[] rotationCounter = {0};

        Bukkit.getScheduler().runTaskTimer(plugin, () -> {
            if (prep.get()) {
                return;
            }
            rotationCounter[0]++;
            for (double d = 0; d <= beamLength; d += 0.75) {
                Location beamLoc = startLoc.clone().add(direction.clone().multiply(d));
                double angle = d * helixFrequency + (rotationCounter[0] * 0.5);

                Vector axis1 = direction.clone().crossProduct(new Vector(1, 0, 0));
                if (axis1.lengthSquared() < 0.01) {
                    axis1 = direction.clone().crossProduct(new Vector(0, 1, 0));
                }
                if (axis1.lengthSquared() < 0.01) {
                    axis1 = direction.clone().crossProduct(new Vector(0, 0, 1));
                }
                axis1.normalize();

                Vector axis2 = direction.clone().crossProduct(axis1).normalize();

                double offsetX = helixRadius * (Math.cos(angle) * axis1.getX() + Math.sin(angle) * axis2.getX());
                double offsetY = helixRadius * (Math.cos(angle) * axis1.getY() + Math.sin(angle) * axis2.getY());
                double offsetZ = helixRadius * (Math.cos(angle) * axis1.getZ() + Math.sin(angle) * axis2.getZ());

                beamLoc.add(offsetX, offsetY, offsetZ);
                Verteiler.playColorParticleBubble(colorPara, beamLoc, 0.05, 1, 0.05, Particle.DUST);
            }
        }, 0, 2);
        Bukkit.getScheduler().runTaskTimer(plugin, () -> {
            if (stop.get()) {
                return;
            }
            if (prep.get()) {
                rotationCounter[0]++;
                for (double d = 0; d <= beamLength; d += 0.25) {
                    Location beamLoc = startLoc.clone().add(direction.clone().multiply(d));
                    if (team.equals("A")) {
                        for (Player player1 : Verteiler.teamB.get(n)) {
                            if (player1.getLocation().distance(beamLoc) <= helixRadius) {
                                DamageDealer.dealDamage(player1, player, schaden);
                            }
                        }
                    } else {
                        for (Player player1 : Verteiler.teamA.get(n)) {
                            if (player1.getLocation().distance(beamLoc) <= helixRadius) {
                                DamageDealer.dealDamage(player1, player, schaden);
                            }
                        }
                    }
                    double angle = d * helixFrequency + (rotationCounter[0] * 0.25);

                    Vector axis1 = direction.clone().crossProduct(new Vector(1, 0, 0));
                    if (axis1.lengthSquared() < 0.01) {
                        axis1 = direction.clone().crossProduct(new Vector(0, 1, 0));
                    }
                    if (axis1.lengthSquared() < 0.01) {
                        axis1 = direction.clone().crossProduct(new Vector(0, 0, 1));
                    }
                    axis1.normalize();

                    Vector axis2 = direction.clone().crossProduct(axis1).normalize();

                    double offsetX = helixRadius * (Math.cos(angle) * axis1.getX() + Math.sin(angle) * axis2.getX());
                    double offsetY = helixRadius * (Math.cos(angle) * axis1.getY() + Math.sin(angle) * axis2.getY());
                    double offsetZ = helixRadius * (Math.cos(angle) * axis1.getZ() + Math.sin(angle) * axis2.getZ());

                    beamLoc.add(offsetX, offsetY, offsetZ);
                    Verteiler.playColorParticleBubble(colorPara, beamLoc, 0.05, 1, 0.05, Particle.DUST);
                    AtomicReference<Block> block = new AtomicReference<>(beamLoc.getBlock());
                    if (!block.get().getType().isAir()) {
                        Painter.paintBlockWithoutUltpoint(block.get(), colorName, player);
                    }
                }
            }
        }, 0, 4);
        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            stop.set(true);
        }, time * 20);
        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            prep.set(true);
        }, prepTime * 20);
    }
}
