package mc.cws.paintOff.Game.Points;

import mc.cws.paintOff.Configuration;
import mc.cws.paintOff.Game.Extras.Colorer;
import mc.cws.paintOff.Game.Management.Scoreboards.Scoreboards;
import mc.cws.paintOff.Game.Shop.Lists;
import mc.cws.paintOff.Game.Shop.ShopInventory;
import mc.cws.paintOff.Game.Management.Queue;
import mc.cws.paintOff.Game.Management.Start;
import mc.cws.paintOff.Game.Management.Verteiler;
import mc.cws.paintOff.Po.Modifications;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.io.*;
import java.util.Objects;

public class Points {
    public static void addPoints(Player player, double points) {
        File lobbyFolder = new File(Configuration.mainCommand + "-lobby");
        File punkteFile = new File(lobbyFolder, "points.dat");

        // Erstelle Ordner wenn nicht vorhanden
        if (!lobbyFolder.exists()) {
            lobbyFolder.mkdir();
        }
        try {
            // Prüfe ob Datei existiert
            if (!punkteFile.exists()) {
                // Erstelle neue Datei mit den Punkten
                try (FileWriter writer = new FileWriter(punkteFile)) {
                    writer.write(player.getName() + ":" + points + "\n");
                }
            } else {
                // Lies bestehende Punkte
                StringBuilder content = new StringBuilder();
                try (BufferedReader reader = new BufferedReader(new FileReader(punkteFile))) {
                    String line;
                    boolean playerFound = false;
                    while ((line = reader.readLine()) != null) {
                        if (line.startsWith(player.getName() + ":")) {
                            playerFound = true;
                            // Erhöhe Punkte um den angegebenen Wert
                            String[] parts = line.split(":");
                            double currentPoints = Double.parseDouble(parts[1]);
                            currentPoints += points;
                            content.append(player.getName()).append(":").append(currentPoints).append("\n");
                        } else {
                            content.append(line).append("\n");
                        }
                    }
                    // Wenn Spieler nicht gefunden wurde, füge ihn hinzu
                    if (!playerFound) {
                        content.append(player.getName()).append(":").append(points).append("\n");
                    }

                    // Schreibe aktualisierten Inhalt zurück in die Datei
                    try (FileWriter writer = new FileWriter(punkteFile)) {
                        writer.write(content.toString());
                    }
                }
            }
        } catch (IOException e) {
            player.sendMessage(ChatColor.RED + Configuration.name + " | " + ChatColor.GRAY + "Fehler beim Verwalten der Punkte: " + e.getMessage());
        }
    }

    public static double getPoints(Player player) {
        File lobbyFolder = new File(Configuration.mainCommand + "-lobby");
        File punkteFile = new File(lobbyFolder, "points.dat");

        try {
            if (!punkteFile.exists()) {
                return 0; // Wenn die Datei nicht existiert, hat der Spieler 0 Punkte
            }

            try (BufferedReader reader = new BufferedReader(new FileReader(punkteFile))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    if (line.startsWith(player.getName() + ":")) {
                        String[] parts = line.split(":");
                        return Double.parseDouble(parts[1]);
                    }
                }
                // Wenn der Spieler nicht gefunden wurde, hat er 0 Punkte
                return 0;
            }
        } catch (IOException e) {
            player.sendMessage(ChatColor.RED + Configuration.name + " | " + ChatColor.GRAY + "Fehler beim Laden der Punkte: " + e.getMessage());
            return 0;
        } catch (NumberFormatException e) {
            player.sendMessage(ChatColor.RED + Configuration.name + " | " + ChatColor.GRAY + "Ungültiges Punkteformat in der Datei.");
            return 0;
        }
    }

    public static void givePoints(Player player, double amount) {
        int n = Start.getQueueNumber(player);
        double bonus = 0;
        for (Player p : Queue.queuedPlayers.get(n)) {
            if (p != null && p.isOnline() && p == player) {
                bonus = calcBonus(p);
            }
        }
        double finalBonus = bonus;
        double finalAmount = amount * finalBonus;

        if (Modifications.searchMod("DoppeltePunkte")) {
            finalAmount *= 2.0;
        }
        addPoints(player, finalAmount);
        Start.points.put(player, Start.points.get(player) + finalAmount);
        Scoreboards.updateScoreboardGame(player);
    }

    public static double calcBonus(Player player) {
        double bonus = 1.0;
        // Überprüfe jeden möglichen Booster-Level
        boolean has25 = false;
        boolean has50 = false;
        boolean has100 = false;

        // Durchsuche alle Runden
        for (int i = 0; i <= ShopInventory.DURATION_ROUNDS; i++) {
            if (Lists.hasBonus.get(i).contains(player)) {
                if (Lists.bonus25.get(i).contains(player)) {
                    has25 = true;
                } else if (Lists.bonus50.get(i).contains(player)) {
                    has50 = true;
                } else if (Lists.bonus100.get(i).contains(player)) {
                    has100 = true;
                }
                break;
            }
        }

        // Wende den höchsten gefundenen Bonus an
        if (has100) {
            bonus += 1.0;
        } else if (has50) {
            bonus += 0.5;
        } else if (has25) {
            bonus += 0.25;
        }
        return bonus;
    }
}
