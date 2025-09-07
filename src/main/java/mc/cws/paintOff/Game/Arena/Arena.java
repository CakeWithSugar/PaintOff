package mc.cws.paintOff.Game.Arena;

import mc.cws.paintOff.Configuration;
import mc.cws.paintOff.Game.Management.Start;
import mc.cws.paintOff.Game.Management.Verteiler;
import mc.cws.paintOff.PaintOffMain;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Arena {
    private static PaintOffMain plugin;
    public static void setPlugin(PaintOffMain plugin) {
        Arena.plugin = plugin;
    }
    public static String randomArena() {
        // Verwende den po-arenas Ordner direkt im Plugin-Ordner
        File arenasFolder = new File(Configuration.mainCommand+"-arenas");
        if (!arenasFolder.exists() || !arenasFolder.isDirectory()) {
            plugin.getLogger().warning("Fehler beim Erstellen des "+Configuration.mainCommand+"-arenas Ordners!");
            return null;
        }

        File[] arenaFolders = arenasFolder.listFiles();
        if (arenaFolders == null) {
            plugin.getLogger().warning("Keine Arenen gefunden!");
            return null;
        }

        List<String> availableArenas = getStrings(arenaFolders);

        if (availableArenas.isEmpty()) {
            return null;
        }

        Random random = new Random();
        return availableArenas.get(random.nextInt(availableArenas.size()));
    }

    private static List<String> getStrings(File[] arenaFolders) {
        List<String> availableArenas = new ArrayList<>();
        for (File arenaFolder : arenaFolders) {
            if (arenaFolder.isDirectory()) {
                File arenaFile = new File(arenaFolder, "arena.dat");
                if (arenaFile.exists()) {
                    availableArenas.add(arenaFolder.getName());
                }
            }
        }

        for (File arenaFolder : arenaFolders) {
            if (arenaFolder.isDirectory()) {
                availableArenas.add(arenaFolder.getName());
            }
        }
        return availableArenas;
    }

    private static Location getLobbySpawn() {
        // Verwende den po-lobby Ordner direkt im Plugin-Ordner
        File lobbyFolder = new File(Configuration.mainCommand+"-lobby");
        if (!lobbyFolder.exists()) {
            lobbyFolder.mkdirs();
        }
        
        File lobbyFile = new File(lobbyFolder, "lobby.dat");
        if (!lobbyFile.exists()) {
            plugin.getLogger().warning("Lobby-Datei nicht gefunden: " + lobbyFile.getAbsolutePath());
            return null;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(lobbyFile))) {
            String firstLine = reader.readLine();
            if (firstLine == null) {
                System.out.println("ERROR: Keine Koordinaten in der Lobby-Datei");
                return null;
            }

            String[] coordinates = firstLine.split("\\."); // Use dot as separator
            if (coordinates.length != 3) {
                System.out.println("ERROR: Ungültiges Format in der Lobby-Datei: " + firstLine);
                return null;
            }

            try {
                // Parse as double to keep decimal places
                double x = Double.parseDouble(coordinates[0]);
                double y = Double.parseDouble(coordinates[1]);
                double z = Double.parseDouble(coordinates[2]);

                // Use the Po1 world
                World world = Bukkit.getWorld("world");
                if (world == null) {
                    System.out.println("ERROR: Welt world nicht gefunden");
                    return null;
                }

                return new Location(world, x, y, z);
            } catch (NumberFormatException e) {
                System.out.println("ERROR: Ungültige Koordinaten in der Lobby-Datei: " + e.getMessage());
                return null;
            }
        } catch (IOException e) {
            System.out.println("ERROR: Fehler beim Lesen der Lobby-Datei: " + e.getMessage());
            return null;
        }
    }

    public static void portLobby(Player player) {
        Location lobbySpawn = getLobbySpawn();
        if (lobbySpawn != null) {
            player.teleport(lobbySpawn);
        } else {
            player.sendMessage(ChatColor.RED + "Fehler beim Teleportieren zur Lobby!");
        }
    }

    public static void portLobbyGroup(List<Player> players) {
        if (players == null || players.isEmpty()) {
            return;
        }

        Location lobbySpawn = getLobbySpawn();
        if (lobbySpawn != null) {
            for (Player player : players) {
                if (player != null) {
                    player.teleport(lobbySpawn);
                }
            }
        } else {
            for (Player player : players) {
                if (player != null) {
                    player.sendMessage(ChatColor.RED + "Fehler beim Teleportieren zur Lobby!");
                }
            }
        }
    }

    public static void portToArena(Player player, int n) {
        String arena = Start.currentArena[n];
        File arenaFile = new File(Configuration.mainCommand+"-arenas/", arena + "/arena.dat");

        if (!arenaFile.exists()) {
            player.sendMessage(ChatColor.RED + "Arena-Datei nicht gefunden!");
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(arenaFile))) {
            String firstLine = reader.readLine();
            if (firstLine == null) {
                player.sendMessage(ChatColor.RED + "Ungültige Arena-Datei!");
                return;
            }

            String[] positions = firstLine.split(",");
            if (positions.length != 6) {
                player.sendMessage(ChatColor.RED + "Ungültiges Format in der Arena-Datei!");
                return;
            }

            // Parse coordinates
            int x1 = Integer.parseInt(positions[0]);
            int y1 = Integer.parseInt(positions[1]);
            int z1 = Integer.parseInt(positions[2]);
            int x2 = Integer.parseInt(positions[3]);
            int y2 = Integer.parseInt(positions[4]);
            int z2 = Integer.parseInt(positions[5]);

            World world = Bukkit.getWorld(Configuration.mainCommand + n);
            if (world == null) {
                player.sendMessage(ChatColor.RED + "Welt nicht gefunden!");
                return;
            }

            // Teleport based on team
            if (Verteiler.teamA.get(n).contains(player)) {
                // Team A uses first position
                Location spawnA = new Location(world, 0, Configuration.spawnHight, 0);
                player.teleport(spawnA);
            }
            if (Verteiler.teamB.get(n).contains(player)) {
            // Calculate delta between position 1 and position 2
            int deltaX = x2 - x1;
            int deltaY = y2 - y1;
            int deltaZ = z2 - z1;

            // Team B uses the delta position with spawnHöhe offset
            Location spawnB = new Location(world, deltaX, deltaY + Configuration.spawnHight, deltaZ);
            player.teleport(spawnB);
        }
        } catch (IOException | NumberFormatException e) {
            player.sendMessage(ChatColor.RED + "Fehler beim Laden der Arena!");
            e.printStackTrace();
        }
    }

    public static int countArenas() {
        File arenasFolder = new File(Configuration.mainCommand+"-arenas");
        if (!arenasFolder.exists() || !arenasFolder.isDirectory()) {
            plugin.getLogger().warning("Arenen-Ordner nicht gefunden!");
            return 0;
        }

        File[] arenaFolders = arenasFolder.listFiles();
        if (arenaFolders == null) {
            return 0;
        }

        int count = 0;
        for (File arenaFolder : arenaFolders) {
            if (arenaFolder.isDirectory()) {
                File arenaFile = new File(arenaFolder, "arena.dat");
                if (arenaFile.exists()) {
                    count++;
                }
            }
        }
        return count;
    }
}

