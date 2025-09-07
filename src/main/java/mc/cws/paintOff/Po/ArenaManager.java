package mc.cws.paintOff.Po;

import mc.cws.paintOff.Configuration;
import mc.cws.paintOff.PaintOffMain;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.io.File;

public class ArenaManager {
    private static PaintOffMain plugin;
    public static void setPlugin(PaintOffMain plugin) {
        ArenaManager.plugin = plugin;
    }
    private static final Map<String, Location> pos1Map = new HashMap<>();
    private static final Map<String, Location> pos2Map = new HashMap<>();
    private static final Map<String, Location> spawnMap = new HashMap<>();

    public static void setPos1(CommandSender sender, Location location) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(ChatColor.RED + Configuration.name + " | " + ChatColor.GRAY + "Dieser Befehl kann nur von einem Spieler ausgeführt werden!");
            return;
        }

        String playerName = player.getName();
        pos1Map.put(playerName, location);
        pos2Map.remove(playerName); // Reset pos2 if pos1 is set again
        spawnMap.remove(playerName);

        sender.sendMessage(ChatColor.GOLD + Configuration.name + " | " + ChatColor.GRAY + "Position 1 gesetzt!");
        sender.sendMessage(ChatColor.GRAY + "Setze jetzt Position 2 mit /" + Configuration.mainCommand + " pos2");
    }

    public static void setPos2(CommandSender sender, Location location) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(ChatColor.RED + Configuration.name + " | " + ChatColor.GRAY + "Dieser Befehl kann nur von einem Spieler ausgeführt werden!");
            return;
        }

        String playerName = player.getName();

        if (!pos1Map.containsKey(playerName)) {
            sender.sendMessage(ChatColor.RED + Configuration.name + " | " + ChatColor.GRAY + "Bitte setze zuerst Position 1 mit /po pos1!");
            return;
        }

        pos2Map.put(playerName, location);

        sender.sendMessage(ChatColor.GOLD + Configuration.name + " | " + ChatColor.GRAY + "Position 2 gesetzt!");
        sender.sendMessage(ChatColor.GRAY + "Setze jetzt Spawn-B mit /" + Configuration.mainCommand + " spawn");
    }

    public static void setSpawn(CommandSender sender, Location location) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(ChatColor.RED + Configuration.name + " | " + ChatColor.GRAY + "Dieser Befehl kann nur von einem Spieler ausgeführt werden!");
            return;
        }

        String playerName = player.getName();

        if (!pos2Map.containsKey(playerName)) {
            sender.sendMessage(ChatColor.RED + Configuration.name + " | " + ChatColor.GRAY + "Bitte setze zuerst Position 2 mit /po pos2!");
            return;
        }

        spawnMap.put(playerName, location);

        sender.sendMessage(ChatColor.GOLD + Configuration.name + " | " + ChatColor.GRAY + "Spawn-B gesetzt!");
        sender.sendMessage(ChatColor.GRAY + "Verwende jetzt /" + Configuration.mainCommand + " create <Name> um die Arena zu erstellen");
    }

    public static void createArena(CommandSender sender, String arenaName, Player player) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + Configuration.name + " | " + ChatColor.GRAY + "Dieser Befehl kann nur von einem Spieler ausgeführt werden!");
            return;
        }

        String playerName = player.getName();

        Location pos1 = pos1Map.get(playerName);
        Location pos2 = pos2Map.get(playerName);
        Location spawn = spawnMap.get(playerName);

        if (pos1 == null || pos2 == null || spawn == null) {
            sender.sendMessage(ChatColor.RED + Configuration.name + " | " + ChatColor.GRAY + "Bitte setze zuerst pos1 und pos2!");
            return;
        }

        // Erstelle den Arena-Ordner direkt im Plugin-Ordner
        File arenaFolder = new File(Configuration.mainCommand+"-arenas", arenaName);
        if (!arenaFolder.exists()) {
            if (!arenaFolder.mkdirs()) {
                plugin.getLogger().severe("Fehler beim Erstellen des Arena-Ordners: " + arenaName);
                sender.sendMessage(ChatColor.RED + Configuration.name + " | " + ChatColor.GRAY + "Fehler beim Erstellen des Arena-Ordners!");
                return;
            }
        }
        // Speichere die Arena-Daten
        File blocksFile = new File(arenaFolder, "arena.dat");
        saveBlocks(blocksFile, pos1, pos2, player.getLocation(), playerName);

        // Reset der Positionen
        pos1Map.remove(playerName);
        pos2Map.remove(playerName);
        spawnMap.remove(playerName);

        sender.sendMessage(ChatColor.GOLD + Configuration.name + " | " + ChatColor.GRAY + "Arena " + ChatColor.GREEN + arenaName + ChatColor.GRAY + " erfolgreich erstellt!");
    }

    private static void saveBlocks(File blocksFile, Location pos1, Location pos2, Location playerPos, String playerName) {
        // Verwende die übergebene Datei
        if (!blocksFile.exists()) {
            try {
                blocksFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(blocksFile))) {
            // Speichere die Originalposition
            Location spawnPos = spawnMap.get(playerName);
            writer.write(String.format("%d,%d,%d,%d,%d,%d",
                    playerPos.getBlockX(), playerPos.getBlockY(), playerPos.getBlockZ(),
                    spawnPos.getBlockX(), spawnPos.getBlockY(), spawnPos.getBlockZ()));
            writer.newLine();

            // Speichere die Min/Max Koordinaten
            int minX = Math.min(pos1.getBlockX(), pos2.getBlockX());
            int minY = Math.min(pos1.getBlockY(), pos2.getBlockY());
            int minZ = Math.min(pos1.getBlockZ(), pos2.getBlockZ());
            int maxX = Math.max(pos1.getBlockX(), pos2.getBlockX());
            int maxY = Math.max(pos1.getBlockY(), pos2.getBlockY());
            int maxZ = Math.max(pos1.getBlockZ(), pos2.getBlockZ());

            writer.write(String.format("%d,%d,%d,%d,%d,%d", minX, minY, minZ, maxX, maxY, maxZ));
            writer.newLine();

            // Speichere die Blöcke
            for (int x = minX; x <= maxX; x++) {
                for (int y = minY; y <= maxY; y++) {
                    for (int z = minZ; z <= maxZ; z++) {
                        Location blockLoc = new Location(playerPos.getWorld(), x, y, z);
                        Block block = blockLoc.getBlock();
                        if (Configuration.jumpAir && block.getType() == Material.AIR) {
                            continue;
                        }
                        BlockData blockData = block.getBlockData();
                        String blockDataString = blockData.getAsString(true);

                        // Speichere die absoluten Koordinaten
                        writer.write(String.format("%d,%d,%d,%s", x, y, z, blockDataString));
                        writer.newLine();
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void pasteArena(String arenaName, int n) {
        File arenaFolder = new File(Configuration.mainCommand+"-arenas", arenaName);
        if (!arenaFolder.exists()) {
            plugin.getLogger().severe(Configuration.name + " | " + ChatColor.RED + "Arena folder not found: " + arenaFolder.getAbsolutePath());
            return;
        }

        File blocksFile = new File(arenaFolder, "arena.dat");
        if (!blocksFile.exists()) {
            System.out.println("ERROR: Block data file not found: " + blocksFile.getAbsolutePath());
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(blocksFile))) {
            // Read original position from first line
            String firstLine = reader.readLine();
            if (firstLine == null) {
                System.out.println("ERROR: No block data found!");
                return;
            }

            String[] originalPos = firstLine.split(",");
            if (originalPos.length != 6) {
                plugin.getLogger().severe(Configuration.name+ " | " + ChatColor.RED + "Invalid position format: " + firstLine);
                return;
            }

            int originalX = Integer.parseInt(originalPos[0]);
            int originalY = Integer.parseInt(originalPos[1]);
            int originalZ = Integer.parseInt(originalPos[2]);

            // Read area bounds from second line
            String boundsLine = reader.readLine();
            if (boundsLine == null) {
                System.out.println("ERROR: Missing bounds data!");
                return;
            }

            // Get Po1 world
            World world = Bukkit.getWorld(Configuration.mainCommand+n);
            if (world == null) {
                System.out.println("ERROR: World "+ Configuration.mainCommand + n + " not found!");
                System.out.println("Erstelle eine Welt mit dem Namen: "+ Configuration.mainCommand + n);
                return;
            }

            // First pass: collect all blocks
            List<String> blockLines = new ArrayList<>();
            String line;
            while ((line = reader.readLine()) != null) {
                blockLines.add(line);
            }

            // Sort blocks by Y coordinate in reverse order (top to bottom)
            blockLines.sort((line1, line2) -> {
                String[] parts1 = line1.split(",", 4);
                String[] parts2 = line2.split(",", 4);
                if (parts1.length < 4 || parts2.length < 4) return 0;
                
                // Sort by Y coordinate in reverse order (highest Y first)
                int y1 = Integer.parseInt(parts1[1]);
                int y2 = Integer.parseInt(parts2[1]);
                return Integer.compare(y2, y1); // Reverse order
            });

            // Second pass: process blocks from top to bottom
            int blocksProcessed = 0;
            for (String blockLine : blockLines) {
                String[] parts = blockLine.split(",", 4);
                if (parts.length < 4) {
                    System.out.println("WARNING: Line has less than 4 values: " + blockLine);
                    continue;
                }

                try {
                    int x = Integer.parseInt(parts[0]);
                    int y = Integer.parseInt(parts[1]);
                    int z = Integer.parseInt(parts[2]);
                    String blockDataStr = parts[3];
                    String blockData4Extra = parts[3];

                    // Calculate corrected coordinates
                    int correctedX = x - originalX;
                    int correctedY = y - originalY + Configuration.spawnHight;
                    int correctedZ = z - originalZ;

                    // Get block using Location
                    Location blockLoc = new Location(world, correctedX, correctedY, correctedZ);
                    Block block = blockLoc.getBlock();

                    // Remove minecraft: prefix if present
                    if (blockDataStr.startsWith("minecraft:")) {
                        blockDataStr = blockDataStr.substring(10);
                    }
                    // Remove everything after first '[' if present
                    int bracketIndex = blockDataStr.indexOf('[');
                    if (bracketIndex != -1) {
                        blockDataStr = blockDataStr.substring(0, bracketIndex);
                        if (Configuration.debugger) {System.out.println(blockDataStr);}
                    }

                    // Process block data first to get material
                    Material material;
                    BlockData blockData = null;
                    
                    try {
                        // Try to create block data directly from the string
                        blockData = Bukkit.createBlockData(blockData4Extra);
                        material = blockData.getMaterial();
                    } catch (IllegalArgumentException e) {
                        // Fallback to basic material parsing if block data parsing fails
                        String materialName = blockDataStr;
                        if (bracketIndex != -1) {
                            materialName = materialName.substring(0, bracketIndex);
                        }
                        
                        material = Material.matchMaterial(materialName.toUpperCase());
                        if (material == null) {
                            try {
                                int materialId = Integer.parseInt(materialName);
                                material = Material.values()[materialId];
                            } catch (NumberFormatException | ArrayIndexOutOfBoundsException ex) {
                                material = Material.STONE;
                            }
                        }
                    }

                    if (material != null) {
                        try {
                            if (blockData != null) {
                                block.setBlockData(blockData, false);
                                if (Configuration.debugger) {
                                    System.out.println("Set to: " + blockData);
                                }
                            } else {
                                block.setType(material, false);
                            }
                        } catch (Exception e) {
                            System.out.println("WARNING: Could not set block data: " + e.getMessage());
                            block.setType(material, false);
                        }
                    }

                    blocksProcessed++;
                } catch (NumberFormatException e) {
                    System.out.println("ERROR: Invalid coordinates in line: " + line);
                    continue;
                }
            }

            System.out.println("INFO: Successfully pasted " + blocksProcessed + " blocks for arena " + arenaName);
        } catch (Exception e) {
            System.err.println("Error pasting arena blocks: " + e.getMessage());
            e.printStackTrace();
        }
    }


// ----------------------------------------------------------------------------------------------------------------------------------------------------

    public static void deleteArenaBlocks(String arenaName, int n) {
        File arenaFolder = new File(Configuration.mainCommand+"-arenas", arenaName);
        if (!arenaFolder.exists()) {
            plugin.getLogger().severe(Configuration.name + " | " + ChatColor.RED + "Arena folder not found: " + arenaFolder.getAbsolutePath());
            return;
        }

        File blocksFile = new File(arenaFolder, "arena.dat"); // Changed to lowercase
        if (!blocksFile.exists()) {
            System.out.println("ERROR: Block data file not found: " + blocksFile.getAbsolutePath());
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(blocksFile))) {
            // Read original position from first line
            String firstLine = reader.readLine();
            if (firstLine == null) {
                System.out.println("ERROR: No block data found!");
                return;
            }

            String[] originalPos = firstLine.split(",");
            if (originalPos.length != 6) {
                plugin.getLogger().severe(Configuration.name + " | " + ChatColor.RED + "Invalid position format: " + firstLine);
                return;
            }

            int originalX = Integer.parseInt(originalPos[0]);
            int originalY = Integer.parseInt(originalPos[1]);
            int originalZ = Integer.parseInt(originalPos[2]);

            // Read area bounds from second line
            String boundsLine = reader.readLine();
            if (boundsLine == null) {
                System.out.println("ERROR: Missing bounds data!");
                return;
            }

            String[] bounds = boundsLine.split(",");
            if (bounds.length != 6) {
                System.out.println("ERROR: Invalid bounds format: " + boundsLine);
                return;
            }

            int minX = Integer.parseInt(bounds[0]);
            int minY = Integer.parseInt(bounds[1]);
            int minZ = Integer.parseInt(bounds[2]);
            int maxX = Integer.parseInt(bounds[3]);
            int maxY = Integer.parseInt(bounds[4]);
            int maxZ = Integer.parseInt(bounds[5]);

            // Get Po1 world
            World world = Bukkit.getWorld(Configuration.mainCommand+n);
            if (world == null) {
                System.out.println("ERROR: World "+ Configuration.mainCommand+ n + " not found!");
                return;
            }

            // Process each block
            int blocksProcessed = 0;
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",", 4); // -1 to keep trailing empty strings
                if (parts.length < 4) {
                    System.out.println("WARNING: Line has less than 4 values: " + line);
                    continue;
                }
                try {
                    int x = Integer.parseInt(parts[0]);
                    int y = Integer.parseInt(parts[1]);
                    int z = Integer.parseInt(parts[2]);
                    String blockDataStr = parts[3];

                    // Calculate corrected coordinates
                    int correctedX = x - originalX;
                    int correctedY = y - originalY + Configuration.spawnHight;
                    int correctedZ = z - originalZ;

                    // Get block using Location
                    Location blockLoc = new Location(world, correctedX, correctedY, correctedZ);
                    Block block = blockLoc.getBlock();

                    // Remove minecraft: prefix if present
                    if (blockDataStr.startsWith("minecraft:")) {
                        blockDataStr = blockDataStr.substring(10);
                    }
                    // Remove everything after first '[' if present
                    int bracketIndex = blockDataStr.indexOf('[');
                    if (bracketIndex != -1) {
                        blockDataStr = blockDataStr.substring(0, bracketIndex);
                    }

                    // Try to get Material from name
                    Material material = Material.getMaterial(blockDataStr.toUpperCase());
                    if (material == null) {
                        // Try to parse as ID
                        try {
                            int materialId = Integer.parseInt(blockDataStr);
                            material = Material.values()[materialId];
                        } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
                            // If both methods fail, use default material (STONE)
                            material = Material.AIR;
                        }
                    }

                    if (material != null) {
                        // Set material first
                        block.setType(Material.AIR);
                    }

                    blocksProcessed++;
                } catch (NumberFormatException e) {
                    System.out.println("ERROR: Invalid coordinates in line: " + line);
                    continue;
                }
            }

            System.out.println("INFO: Successfully pasted " + blocksProcessed + " blocks for arena " + arenaName);
        } catch (Exception e) {
            System.err.println("Error pasting arena blocks: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void deleteArena(CommandSender sender, String arenaName) {
        File arenaFolder = new File(Configuration.mainCommand+"-arenas/" + arenaName);
        if (arenaFolder.exists()) {
            try {
                deleteDirectory(arenaFolder);
                sender.sendMessage(ChatColor.GOLD + Configuration.name + " | " + ChatColor.GRAY + "Arena " + ChatColor.GREEN + arenaName + ChatColor.GRAY + " erfolgreich gelöscht!");
            } catch (IOException e) {
                sender.sendMessage(ChatColor.RED + Configuration.name + " | " + ChatColor.GRAY + "Fehler beim Löschen der Arena: " + e.getMessage());
            }
        } else {
            sender.sendMessage(ChatColor.RED + Configuration.name + " | " + ChatColor.GRAY + "Arena nicht gefunden!");
        }
    }

    private static void deleteDirectory(File directory) throws IOException {
        if (directory.isDirectory()) {
            File[] files = directory.listFiles();
            if (files != null) {
                for (File file : files) {
                    deleteDirectory(file);
                }
            }
        }
        if (!directory.delete()) {
            throw new IOException("Could not delete file: " + directory);
        }
    }
}
