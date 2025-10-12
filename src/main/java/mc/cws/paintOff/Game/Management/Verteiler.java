package mc.cws.paintOff.Game.Management;

import mc.cws.paintOff.Configuration;
import mc.cws.paintOff.Scoreboards.ColorTeam;
import mc.cws.paintOff.Po.Modifications;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.Color;

import java.util.*;

public class Verteiler {
    public static final Map<Integer, List<Player>> teamA = new HashMap<>();
    public static final Map<Integer, List<Player>> teamB = new HashMap<>();
    public static String[] colorA = new String[Configuration.maxQueues];
    public static String[] colorB = new String[Configuration.maxQueues];
    public static void verteilen(int n) {

        List<Player> queue = Start.getPlayersInQueue(n);

        // Clear existing team assignments for this queue
        teamA.put(n, new ArrayList<>());
        teamB.put(n, new ArrayList<>());

        // Mische die Spieler
        Collections.shuffle(queue);

        // Verteile die Spieler gleichmäßig zwischen Team A und Team B
        // Math.ceil rundet auf, damit bei ungerader Spielerzahl das größere Team Team A ist
        int half = (int) Math.ceil(queue.size() / 2.0);

        // Erste Hälfte in Team A
        String teamAColor = randomCollor();
        String teamBColor = randomCollor();
        while (teamBColor.equals(teamAColor)) {
            teamBColor = randomCollor();
        }
        colorA[n] = getColor(teamAColor);
        colorB[n] = getColor(teamBColor);

        // Erste Hälfte in Team A
        ColorTeam.updateTeamColor(n, true, Stop.getColorNameSmallA(n));
        for (int i = 0; i < half; i++) {
            Player player = queue.get(i);
            teamA.get(n).add(player);
            ColorTeam.addPlayerToTeam(player, n);
        }
        // Zweite Hälfte in Team B
        ColorTeam.updateTeamColor(n, false, Stop.getColorNameSmallB(n));
        for (int i = half; i < queue.size(); i++) {
            Player player = queue.get(i);
            teamB.get(n).add(player);
            ColorTeam.addPlayerToTeam(player, n);
        }

        if (!teamA.get(n).isEmpty()) {
            for (Player player : teamA.get(n)) {
                player.sendTitle(colorA[n] + "* " + ChatColor.BOLD + teamAColor + colorA[n] +" *", "", 10, 40, 40);
            }
        }
        if (!teamB.get(n).isEmpty()) {
            for (Player player : teamB.get(n)) {
                player.sendTitle(colorB[n] + "* " + ChatColor.BOLD + teamBColor + colorB[n] + " *", "", 10, 40, 40);
            }
        }
    }

    public static String randomCollor() {
        String[] colors;

        if (Modifications.searchMod("Default")) {
            colors = new String[]{
                    "Orange",
                    "Gelb",
                    "Hellgrün",
                    "Hellblau",
                    "Blau",
                    "Lila",
                    "Magenta",
                    "Cyan"
            };
        } else if (Modifications.searchMod("Halloween")) {
            colors = new String[]{
                    "Schwarz",
                    "Grau",
                    "Weiß",
                    "Orange",
                    "Braun",
                    "Rot",
                    "Grün",
                    "Gelb"
            };
        } else if (Modifications.searchMod("BlackWhite")) {
            colors = new String[]{
                    "Schwarz",
                    "Grau",
                    "Weiß",
            };
        } else if (Modifications.searchMod("Weihnacht")) {  // Statt "Cristmas"
            colors = new String[]{
                    "Weiß",
                    "Hellblau",
                    "Cyan",
                    "Rot",
                    "Grün",
            };
        } else if (Modifications.searchMod("Fruhling")) {
            colors = new String[]{
                    "Hellgrün",
                    "Grün",
                    "Magenta",
                    "Lila",
                    "Gelb",
            };
        } else if (Modifications.searchMod("Strand")) {
            colors = new String[]{
                    "Blau",
                    "Hellblau",
                    "Cyan",
                    "Gelb",
                    "Magenta",
            };
        } else if (Modifications.searchMod("Ultra")) {
            colors = new String[]{
                    "Blau",
                    "Red",
                    "Lila",
                    "Grün",
                    "Schwarz",
                    "Magenta"
            };
        } else if (Modifications.searchMod("Pastell")) {
            colors = new String[]{
                    "Orange",
                    "Gelb",
                    "Hellgrün",
                    "Cyan",
                    "Hellblau",
                    "Magenta"
            };
        }  else {
            // Default palette if no specific mod is active
            colors = new String[]{
                    "Orange",
                    "Gelb",
                    "Hellgrün",
                    "Hellblau",
                    "Blau",
                    "Lila",
                    "Magenta",
                    "Cyan"
            };
        }

        // Wähle eine zufällige Farbe
        Random random = new Random();
        return colors[random.nextInt(colors.length)];
    }

    public static String getColor(String color) {
        switch (color) {
            case "Rot" -> color = "§4";
            case "Orange" -> color = "§6";
            case "Gelb" -> color = "§e";
            case "Hellgrün" -> color = "§a";
            case "Grün" -> color = "§2";
            case "Hellblau" -> color = "§b";
            case "Blau" -> color = "§1";
            case "Lila" -> color = "§5";
            case "Magenta" -> color = "§d";
            case "Schwarz" -> color = "§0";
            case "Grau" -> color = "§8";
            case "Weiß" -> color = "§f";
            case "Braun" -> color = "§c";
            case "Cyan" -> color = "§3";
        }
        return color;
    }

    public static void playColorParticle(String color, Location loc, double breite, int anzahl, double höhe, Particle partikelArt) {
        Color particleColor = switch (color) {
            case "§4" -> Color.RED;
            case "§6" -> Color.ORANGE;
            case "§e" -> Color.YELLOW;
            case "§a" -> Color.LIME;
            case "§2" -> Color.GREEN;
            case "§b" -> Color.AQUA;
            case "§1" -> Color.BLUE;
            case "§5" -> Color.PURPLE;
            case "§d" -> Color.FUCHSIA;
            case "§3" -> Color.TEAL;
            case "§0" -> Color.BLACK;
            case "§8" -> Color.GRAY;
            case "§f" -> Color.WHITE;
            case "§c" -> Color.MAROON;
            default -> Color.SILVER;
        };

        Particle.DustOptions dustOptions = new Particle.DustOptions(particleColor, 1.5f);

        for (int i = 0; i < anzahl; i++) {
            if (partikelArt == Particle.DUST) {
                Objects.requireNonNull(loc.getWorld()).spawnParticle(
                        partikelArt,
                        loc.getX() + (Math.random() * breite * 2 - breite),
                        loc.getY() + (höhe),
                        loc.getZ() + (Math.random() * breite * 2 - breite),
                        1, 0, 0, 0, 0, dustOptions
                );
            } else {
                Objects.requireNonNull(loc.getWorld()).spawnParticle(
                        partikelArt,
                        loc.getX() + (Math.random() * breite * 2 - breite),
                        loc.getY() + (höhe),
                        loc.getZ() + (Math.random() * breite * 2 - breite),
                        1, 0, 0, 0, 0
                );
            }
        }
    }
    public static void playColorParticleBubble(String color, Location loc, double breite, int anzahl, double höhe, Particle partikelArt) {
        Color particleColor = switch (color) {
            case "§4" -> Color.RED;
            case "§6" -> Color.ORANGE;
            case "§e" -> Color.YELLOW;
            case "§a" -> Color.LIME;
            case "§2" -> Color.GREEN;
            case "§b" -> Color.AQUA;
            case "§1" -> Color.BLUE;
            case "§5" -> Color.PURPLE;
            case "§d" -> Color.FUCHSIA;
            case "§3" -> Color.TEAL;
            case "§0" -> Color.BLACK;
            case "§8" -> Color.GRAY;
            case "§f" -> Color.WHITE;
            case "§c" -> Color.MAROON;
            default -> Color.SILVER;
        };

        Particle.DustOptions dustOptions = new Particle.DustOptions(particleColor, 1.5f);

        for (int i = 0; i < anzahl; i++) {
            if (partikelArt == Particle.DUST) {
                Objects.requireNonNull(loc.getWorld()).spawnParticle(
                        partikelArt,
                        loc.getX() + (Math.random() * breite * 2 - breite),
                        loc.getY() + (Math.random() * höhe * 2 - höhe),
                        loc.getZ() + (Math.random() * breite * 2 - breite),
                        1, 0, 0, 0, 0, dustOptions
                );
            } else {
                Objects.requireNonNull(loc.getWorld()).spawnParticle(
                        partikelArt,
                        loc.getX() + (Math.random() * breite * 2 - breite),
                        loc.getY() + (Math.random() * höhe * 2 - höhe),
                        loc.getZ() + (Math.random() * breite * 2 - breite),
                        1, 0, 0, 0, 0
                );
            }
        }
    }

    public static String getTeam(Player player, int n) {
        if (teamA.containsKey(n) && teamA.get(n).contains(player)) {
            return "A";
        } else if (teamB.containsKey(n) && teamB.get(n).contains(player)) {
            return "B";
        } else {
            return null;
        }
    }

    public static void reset(int n) {
        // Clear and remove the team lists from the maps
        if (teamA.containsKey(n)) {
            teamA.get(n).clear();
            teamA.remove(n);
        }
        if (teamB.containsKey(n)) {
            teamB.get(n).clear();
            teamB.remove(n);
        }

        // Reset the colors for this queue
        if (n >= 0 && n < colorA.length) {
            colorA[n] = null;
        }
        if (n >= 0 && n < colorB.length) {
            colorB[n] = null;
        }
    }
}
