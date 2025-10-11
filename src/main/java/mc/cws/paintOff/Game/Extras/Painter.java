package mc.cws.paintOff.Game.Extras;

import mc.cws.paintOff.Game.Resources.UltPoints;
import mc.cws.paintOff.Game.Items.Ultimates.GlitzerMeteor;
import mc.cws.paintOff.Game.Management.Stop;
import mc.cws.paintOff.Game.Points.Points;
import mc.cws.paintOff.Game.Management.Start;
import mc.cws.paintOff.Game.Management.Verteiler;
import mc.cws.paintOff.Po.Modifications;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static mc.cws.paintOff.Game.Management.InGame.DamageDealer.dealDamage;

public class Painter {
    public static void paintBlockWithUltpoint(Block targetBlock, Player player, int n, String colorName) {
        String team = Verteiler.getTeam(player, n);
        int teamNumber = 1;
        if (team == null) {
            return;
        }
        if (team.equals("A")) {
            teamNumber = 0;
        }
        if (!Unpaintables.isUnpaintable(targetBlock.getType(),targetBlock.getLocation()) && targetBlock.getType() != Material.AIR && (targetBlock.getType() != Material.valueOf(colorName + getBlockType()))) {
            if (Start.golding[n] && targetBlock.getType() == Material.GOLD_BLOCK) {
                Points.givePoints(player, 1);
            }
            targetBlock.setType(Material.valueOf(colorName + Start.blockName[n]));

            Start.colored.put(player, Start.colored.get(player)+1);
            Start.teamColored[n][teamNumber][0]++;
            String colorPara = getColorPara(player);
            Verteiler.playColorParticle(colorPara, targetBlock.getLocation(), 0.5, 1, 1, Particle.DUST);
            Objects.requireNonNull(targetBlock.getLocation().getWorld()).playSound(targetBlock.getLocation(), Sound.BLOCK_HONEY_BLOCK_PLACE, 0.25f, 2.0f);
            UltPoints.giveUltPoint(player);
        } else if (Resetables.resetable(targetBlock.getType()) && !Unpaintables.isUnpaintable(targetBlock.getType(),targetBlock.getLocation()) && targetBlock.getType() != Material.AIR && (targetBlock.getType() != Material.valueOf(colorName + "_WOOL"))) {
            targetBlock.setType(Material.AIR);
        }
    }
    public static void paintBlockWithoutUltpoint(Block targetBlock, String color,Player player) {
        int n = Start.getQueueNumber(player);
        String team = Verteiler.getTeam(player, n);
        int teamNumber = 1;
        if (team == null) {
            return;
        }
        if (team.equals("A")) {
            teamNumber = 0;
        }
        if (!Unpaintables.isUnpaintable(targetBlock.getType(),targetBlock.getLocation()) && targetBlock.getType() != Material.AIR) {
            if (Start.golding[n]) {
                targetBlock.setType(Material.GOLD_BLOCK);
                Start.colored.put(player, Start.colored.get(player)+1);
                Start.teamColored[n][teamNumber][0]++;
                String colorPara = Stop.getColorNameBack(color);
                Verteiler.playColorParticle(colorPara, targetBlock.getLocation(), 0.5, 1, 0.9, Particle.DUST);
                Objects.requireNonNull(targetBlock.getLocation().getWorld()).playSound(targetBlock.getLocation(), Sound.BLOCK_AMETHYST_BLOCK_PLACE, 0.25f, 2.0f);
                return;
            }
            targetBlock.setType(Material.valueOf(color + Start.blockName[n]));
            Start.colored.put(player, Start.colored.get(player)+1);
            Start.teamColored[n][teamNumber][0]++;
            String colorPara = Stop.getColorNameBack(color);
            Verteiler.playColorParticle(colorPara, targetBlock.getLocation(), 0.5, 1, 0.9, Particle.DUST);
            targetBlock.getLocation().getWorld().playSound(targetBlock.getLocation(), Sound.BLOCK_HONEY_BLOCK_PLACE, 0.25f, 2.0f);
        } else if (Resetables.resetable(targetBlock.getType()) && !Unpaintables.isUnpaintable(targetBlock.getType(),targetBlock.getLocation()) && targetBlock.getType() != Material.AIR && (targetBlock.getType() != Material.valueOf(color + "_WOOL"))) {
            targetBlock.setType(Material.AIR);
        }
    }


    public static void explodeAlgorithmSphere(Block hitblock, int radius, Player player, String color) {
        List<Location> possibleLocations = new ArrayList<>();
        int n = Start.getQueueNumber(player);
        String team = Verteiler.getTeam(player, n);
        if (team == null || !team.equals("A") && !team.equals("B")) {
            return;
        }

        for (int x = -radius; x <= radius; x++) {
            for (int y = -1; y <= 1; y++) { // Nur 3 Schichten in Y-Richtung
                for (int z = -radius; z <= radius; z++) {
                    Location loc = hitblock.getLocation().add(x, y, z);
                    if (loc.getBlock().getType() != Material.AIR) { // Nur Luft-Blocks
                        possibleLocations.add(loc);
                    }
                }
            }
        }
        // Spawn Schneebälle an den zufälligen Positionen
        List<int[]> excludeCoords = Arrays.asList(
                new int[]{radius, radius},  // x=6, z=6
                new int[]{radius-1, radius-1},  // x=5, z=5
                new int[]{radius, radius-1},  // x=6, z=5
                new int[]{radius-1, radius},  // x=5, z=6
                new int[]{radius-3, radius},  // x=3, z=6
                new int[]{radius-2, radius},  // x=4, z=6
                new int[]{radius, radius-3},  // x=6, z=3
                new int[]{radius, radius-2},   // x=6, z=4

                new int[]{-radius, radius},  // x=6, z=6
                new int[]{-(radius-1), radius-1},  // x=5, z=5
                new int[]{-radius, radius-1},  // x=6, z=5
                new int[]{-(radius-1), radius},  // x=5, z=6
                new int[]{-(radius-3), radius},  // x=3, z=6
                new int[]{-(radius-2), radius},  // x=4, z=6
                new int[]{-radius, radius-3},  // x=6, z=3
                new int[]{-radius, radius-2},   // x=6, z=4

                new int[]{-radius, -radius},  // x=6, z=6
                new int[]{-(radius-1), -(radius-1)},  // x=5, z=5
                new int[]{-radius, -(radius-1)},  // x=6, z=5
                new int[]{-(radius-1), -radius},  // x=5, z=6
                new int[]{-(radius-3), -radius},  // x=3, z=6
                new int[]{-(radius-2), -radius},  // x=4, z=6
                new int[]{-radius, -(radius-3)},  // x=6, z=3
                new int[]{-radius, -(radius-2)},   // x=6, z=4

                new int[]{radius, -radius},  // x=6, z=6
                new int[]{(radius-1), -(radius-1)},  // x=5, z=5
                new int[]{radius, -(radius-1)},  // x=6, z=5
                new int[]{(radius-1), -radius},  // x=5, z=6
                new int[]{(radius-3), -radius},  // x=3, z=6
                new int[]{(radius-2), -radius},  // x=4, z=6
                new int[]{radius, -(radius-3)},  // x=6, z=3
                new int[]{radius, -(radius-2)}
        );

        for (Location randomLoc : possibleLocations) {
            // Exclude if the coordinate matches any of the specified locations
            if (shouldExcludeAnyLocation(randomLoc, hitblock.getLocation(), excludeCoords)) {
                continue; // Skip this location and try the next one
            }
            Painter.paintBlockWithoutUltpoint(randomLoc.getBlock(), color, player);
            extractTeamAndDamage(player, n, randomLoc, team, GlitzerMeteor.damage);

        }
    }

    private static void extractTeamAndDamage(Player player, int n, Location loc, String teamName, int damage) {
        Location blockLocation = loc.clone();
        if (teamName.equals("A")) {
            for (Player p : Verteiler.teamB.get(n)) {
                Location playerLoc = p.getLocation();
                Location blockBelowPlayer = playerLoc.clone().subtract(0, 1, 0).getBlock().getLocation();
                if (blockBelowPlayer.equals(blockLocation)) {
                    dealDamage(p, player, damage);
                }
            }
        } else {
            for (Player p : Verteiler.teamA.get(n)) {
                Location playerLoc = p.getLocation();
                Location blockBelowPlayer = playerLoc.clone().subtract(0, 1, 0).getBlock().getLocation();
                if (blockBelowPlayer.equals(blockLocation)) {
                    dealDamage(p, player, damage);
                }
            }
        }
    }

    public static boolean shouldExcludeAnyLocation(Location loc, Location refLoc, List<int[]> excludeCoords) {
        for (int[] coords : excludeCoords) {
            if (shouldExcludeLocation(loc, refLoc, coords[0], coords[1])) { // x und z
                return true;
            }
        }
        return false;
    }
    private static boolean shouldExcludeLocation(Location loc, Location refLoc, int x, int z) {
        return loc.getBlockX() == refLoc.getBlockX() + x &&
                loc.getBlockZ() == refLoc.getBlockZ() + z;
    }

    public static void explosionAlgorithm(Block hitBlock, Player player, int n, int size, String color, int damage) {
        for (int x = -size; x <= size; x++) {
            for (int y = -size; y <= size; y++) {
                for (int z = -size; z <= size; z++) {
                    Block targetBlock = hitBlock.getRelative(x, y, z);
                    if (Verteiler.teamA.get(n).contains(player)) {
                        for (Player players : Verteiler.teamB.get(n)) {
                            Location playerLoc = players.getLocation();
                            if (playerLoc.getBlockX() == targetBlock.getX() &&
                                    playerLoc.getBlockY() == targetBlock.getY() &&
                                    playerLoc.getBlockZ() == targetBlock.getZ()) {
                                dealDamage(players,player, damage);
                            }
                        }
                    } else if (Verteiler.teamB.get(n).contains(player)) {
                        for (Player players : Verteiler.teamA.get(n)) {
                            Location playerLoc = players.getLocation();
                            if (playerLoc.getBlockX() == targetBlock.getX() &&
                                    playerLoc.getBlockY() == targetBlock.getY() &&
                                    playerLoc.getBlockZ() == targetBlock.getZ()) {
                                dealDamage(players,player, damage);
                            }
                        }
                    }
                    paintBlockWithUltpoint(targetBlock,player,n,color);
                }
            }
        }
    }

    public static void explosionAlgorithmWithoutUltpoint(Block hitBlock, Player player, int n, int size, String color, int damage) {
        for (int x = -size; x <= size; x++) {
            for (int y = -size; y <= size; y++) {
                for (int z = -size; z <= size; z++) {
                    Block targetBlock = hitBlock.getRelative(x, y, z);
                    if (Verteiler.teamA.get(n).contains(player)) {
                        for (Player players : Verteiler.teamB.get(n)) {
                            Location playerLoc = players.getLocation();
                            if (playerLoc.getBlockX() == targetBlock.getX() &&
                                    playerLoc.getBlockY() == targetBlock.getY() &&
                                    playerLoc.getBlockZ() == targetBlock.getZ()) {
                                dealDamage(players,player, damage);
                            }
                        }
                    } else if (Verteiler.teamB.get(n).contains(player)) {
                        for (Player players : Verteiler.teamA.get(n)) {
                            Location playerLoc = players.getLocation();
                            if (playerLoc.getBlockX() == targetBlock.getX() &&
                                    playerLoc.getBlockY() == targetBlock.getY() &&
                                    playerLoc.getBlockZ() == targetBlock.getZ()) {
                                dealDamage(players,player, damage);
                            }
                        }
                    }
                    paintBlockWithoutUltpoint(targetBlock,color,player);
                }
            }
        }
    }

    public static String getBlockType() {
        if (Modifications.searchMod("Concrete")) return "_CONCRETE";
        else if (Modifications.searchMod("Wolle")) return "_WOOL";
        else if (Modifications.searchMod("Terracotta")) return "_TERRACOTTA";
        else if (Modifications.searchMod("GlazedTerracotta")) return "_GLAZED_TERRACOTTA";
        else return "_WOOL";
    }

    public static String getColorPara(Player player) {
        int n = Start.getQueueNumber(player);
        String team = Verteiler.getTeam(player, n);
        String teamColor = "";
        if (Objects.equals(team, "A")) {
            teamColor = Verteiler.colorA[n];
        } else if (Objects.equals(team, "B")) {
            teamColor = Verteiler.colorB[n];
        }
        assert teamColor != null;
        return Verteiler.getColor(teamColor);
    }
}
