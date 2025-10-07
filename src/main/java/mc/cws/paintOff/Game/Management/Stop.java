package mc.cws.paintOff.Game.Management;

import mc.cws.paintOff.Configuration;
import mc.cws.paintOff.Game.Extras.ClearInventory;
import mc.cws.paintOff.Game.Extras.Painter;
import mc.cws.paintOff.Game.Points.Points;
import mc.cws.paintOff.PaintOffMain;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

public class Stop {
    private static PaintOffMain plugin; // Your plugin class
    public static void setPlugin(PaintOffMain plugin) {
        Stop.plugin = plugin;
    }
    public static String[] arenaName = new String[Configuration.maxQueues];

    public static boolean[] finished = new boolean[Configuration.maxQueues];
    static int tick = 20;
    public static double lost = 5;
    public static double won = 25;

    public static String getColorNameA(int queueNumber) {
        String color = Verteiler.colorA[queueNumber];
        String name = switch (color) {
            case "§4" -> "RED";
            case "§6" -> "ORANGE";
            case "§e" -> "YELLOW";
            case "§a" -> "LIME";
            case "§2" -> "GREEN";
            case "§b" -> "LIGHT_BLUE";
            case "§1" -> "BLUE";
            case "§5" -> "PURPLE";
            case "§d" -> "MAGENTA";
            case "§8" -> "GRAY";
            case "§0" -> "BLACK";
            case "§f" -> "WHITE";
            case "§c" -> "BROWN";
            case "§3" -> "CYAN";
            default -> "";
        };
        return name;
    }
    public static String getColorNameSmallA(int queueNumber) {
        String color = Verteiler.colorA[queueNumber];
        String name = switch (color) {
            case "§4" -> "DARK_RED";
            case "§6" -> "GOLD";
            case "§e" -> "YELLOW";
            case "§a" -> "GREEN";
            case "§2" -> "DARK_GREEN";
            case "§b" -> "AQUA";
            case "§1" -> "BLUE";
            case "§5" -> "DARK_PURPLE";
            case "§d" -> "LIGHT_PURPLE";
            case "§8" -> "GRAY";
            case "§0" -> "BLACK";
            case "§f" -> "WHITE";
            case "§c" -> "RED";
            case "§3" -> "DARK_AQUA";
            default -> "";
        };
        return name;
    }
    public static String getColorNameB(int queueNumber) {
        String color = Verteiler.colorB[queueNumber];
        String name = switch (color) {
            case "§4" -> "RED";
            case "§6" -> "ORANGE";
            case "§e" -> "YELLOW";
            case "§a" -> "LIME";
            case "§2" -> "GREEN";
            case "§b" -> "LIGHT_BLUE";
            case "§1" -> "BLUE";
            case "§5" -> "PURPLE";
            case "§d" -> "MAGENTA";
            case "§8" -> "GRAY";
            case "§0" -> "BLACK";
            case "§f" -> "WHITE";
            case "§c" -> "BROWN";
            case "§3" -> "CYAN";
            default -> "";
        };
        return name;
    }
    public static String getColorNameSmallB(int queueNumber) {
        String color = Verteiler.colorB[queueNumber];
        String name = switch (color) {
            case "§4" -> "DARK_RED";
            case "§6" -> "GOLD";
            case "§e" -> "YELLOW";
            case "§a" -> "GREEN";
            case "§2" -> "DARK_GREEN";
            case "§b" -> "AQUA";
            case "§1" -> "BLUE";
            case "§5" -> "DARK_PURPLE";
            case "§d" -> "LIGHT_PURPLE";
            case "§8" -> "GRAY";
            case "§0" -> "BLACK";
            case "§f" -> "WHITE";
            case "§c" -> "RED";
            case "§3" -> "DARK_AQUA";
            default -> "";
        };
        return name;
    }

    public static String getColorNameBack(String color) {
        String name = switch (color) {
            case "RED" -> "§4";
            case "ORANGE" -> "§6";
            case "YELLOW" -> "§e";
            case "LIME" -> "§a";
            case "GREEN" -> "§2";
            case "LIGHT_BLUE" -> "§b";
            case "BLUE" -> "§1";
            case "PURPLE" -> "§5";
            case "MAGENTA" -> "§d";
            case "GRAY" -> "§8";
            case "BLACK" -> "§0";
            case "WHITE" -> "§f";
            case "BROWN" -> "§c";
            case "CYAN" -> "§3";
            default -> "";
        };
        return name;
    }

    public static int checkA(int queueNumber, String arenaName) {
        // Welt abrufen
        World world = Bukkit.getWorld(Configuration.mainCommand + + queueNumber);
        if (world == null) {
            return 0;
        }

        // Lese die Arena-Datei
        File arenaFolder = new File(Configuration.mainCommand + "-arenas/" + arenaName);
        File blocksFile = new File(arenaFolder, "arena.dat");

        int dirtCount = 0;

        try (BufferedReader reader = new BufferedReader(new FileReader(blocksFile))) {
            String firstLine = reader.readLine();
            if (firstLine == null) {
                Bukkit.getLogger().warning(Configuration.name + " | " + ChatColor.GRAY + "Ungültige Arena-Datei!");
                return dirtCount;
            }
            // Read reference position from first line
            String[] referencePos = firstLine.split(",");
            int refX = Integer.parseInt(referencePos[0]);
            int refY = Integer.parseInt(referencePos[1]);
            int refZ = Integer.parseInt(referencePos[2]);

            // Read and paste blocks
            String line;
            while ((line = reader.readLine()) != null) {
                String[] blockData = line.split(",");
                if (blockData.length < 4) {
                    Bukkit.getLogger().warning(Configuration.name + " | " + ChatColor.GRAY + "Ungültige Blockdaten: " + line);
                    continue;
                }

                try {
                    int x = Integer.parseInt(blockData[0]);
                    int y = Integer.parseInt(blockData[1]);
                    int z = Integer.parseInt(blockData[2]);

                    Location pasteLoc = new Location(Bukkit.getWorld(Configuration.mainCommand +  + queueNumber),
                            x - refX, y - refY + Configuration.spawnHight, z - refZ);
                    Block block = pasteLoc.getBlock();
                    Material blockType = block.getType();

                    if (blockType == Material.valueOf(getColorNameA(queueNumber) + Painter.getBlockType())) {
                        dirtCount++;
                    }
                } catch (NumberFormatException e) {
                    Bukkit.getLogger().warning(Configuration.name + " | " + ChatColor.GRAY + "Ungültige Koordinaten: " + line);
                } catch (IllegalArgumentException e) {
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return dirtCount;
    }

    public static int checkB(int queueNumber, String arenaName) {
        World world = Bukkit.getWorld(Configuration.mainCommand + queueNumber);
        if (world == null) {
            return 0;
        }
        File arenaFolder = new File(Configuration.mainCommand + "-arenas/" + arenaName);
        File blocksFile = new File(arenaFolder, "arena.dat");
        int dirtCount = 0;

        try (BufferedReader reader = new BufferedReader(new FileReader(blocksFile))) {
            String firstLine = reader.readLine();
            if (firstLine == null) {
                Bukkit.getLogger().warning(Configuration.name + " | " + ChatColor.GRAY + "Ungültige Arena-Datei!");
                return dirtCount;
            }
            // Read reference position from first line
            String[] referencePos = firstLine.split(",");
            int refX = Integer.parseInt(referencePos[0]);
            int refY = Integer.parseInt(referencePos[1]);
            int refZ = Integer.parseInt(referencePos[2]);

            // Read and paste blocks
            String line;
            while ((line = reader.readLine()) != null) {
                String[] blockData = line.split(",");
                if (blockData.length < 4) {
                    Bukkit.getLogger().warning(Configuration.name + " | " + ChatColor.GRAY + "Ungültige Blockdaten: " + line);
                    continue;
                }

                try {
                    int x = Integer.parseInt(blockData[0]);
                    int y = Integer.parseInt(blockData[1]);
                    int z = Integer.parseInt(blockData[2]);

                    // Calculate position relative to reference position (add 80 to Y)
                    Location pasteLoc = new Location(Bukkit.getWorld(Configuration.mainCommand + queueNumber),
                            x - refX, y - refY + Configuration.spawnHight, z - refZ);
                    Location pasteLocAbove = new Location(Bukkit.getWorld(Configuration.mainCommand + queueNumber),
                            x - refX, y - refY + Configuration.spawnHight +1, z - refZ);

                    // Set block type
                    Block block = pasteLoc.getBlock();
                    Block blockAbove = pasteLocAbove.getBlock();
                    Material blockType = block.getType();
                    Material blockTypeAbove = blockAbove.getType();
                    // Zähle den Block wenn er die richtige Farbe hat
                    if (blockType == Material.valueOf(getColorNameB(queueNumber)+Painter.getBlockType()) && blockTypeAbove == Material.AIR) {
                        dirtCount++;
                    }
                } catch (NumberFormatException e) {
                    Bukkit.getLogger().warning(Configuration.name + " | " + ChatColor.GRAY + "Ungültige Koordinaten: " + line);
                    continue;
                } catch (IllegalArgumentException e) {
                    continue;
                }
            }
        } catch (IOException e) {
            Bukkit.getLogger().severe(Configuration.name + " | " + ChatColor.GRAY + "Fehler beim Lesen der Arena-Datei: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            Bukkit.getLogger().severe(Configuration.name + " | " + ChatColor.GRAY + "Fehler beim Einfügen der Arena: " + e.getMessage());
            e.printStackTrace();
        }

        return dirtCount;
    }

    public static void check(int queueNumber) {
        int countA = checkA(queueNumber, arenaName[queueNumber]);
        int countB = checkB(queueNumber,arenaName[queueNumber]);
        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
            for (Player player : Queue.queuedPlayers.get(queueNumber)) {
                if (player != null && player.isOnline()) {
                    player.sendTitle("", Verteiler.colorA[queueNumber] + ChatColor.BOLD + ">" + ChatColor.GRAY + " > > < < " + Verteiler.colorB[queueNumber] + ChatColor.BOLD + "<", 0, 20, 20);
                    player.playSound(player.getLocation(), Sound.ENTITY_CHICKEN_EGG, 0.5f, 1.0f);
                }
            }
            Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                for (Player player : Queue.queuedPlayers.get(queueNumber)) {
                    if (player != null && player.isOnline()) {
                        player.sendTitle("", Verteiler.colorA[queueNumber] + ChatColor.BOLD + "> >" + ChatColor.GRAY + " > < " + Verteiler.colorB[queueNumber] + ChatColor.BOLD + "< <", 0, 20, 20);
                        player.playSound(player.getLocation(), Sound.ENTITY_CHICKEN_EGG, 0.5f, 1.5f);
                    }
                }
                Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                    for (Player player : Queue.queuedPlayers.get(queueNumber)) {
                        if (player != null && player.isOnline()) {
                            player.sendMessage(Verteiler.colorA[queueNumber] + ChatColor.BOLD + countA + ChatColor.GRAY + " | " + Verteiler.colorB[queueNumber] + ChatColor.BOLD + countB);
                            player.sendTitle("", Verteiler.colorA[queueNumber] + ChatColor.BOLD + "> > >" + ChatColor.GRAY + " " + Verteiler.colorB[queueNumber] + ChatColor.BOLD + "< < <", 0, 20, 20);
                            player.playSound(player.getLocation(), Sound.ENTITY_CHICKEN_EGG, 0.5f, 2f);
                        }
                    }
                    Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                        if (countA > countB) {
                            for (Player player : Verteiler.teamA.get(queueNumber)) {
                                player.sendTitle(ChatColor.GRAY + "-= " + Verteiler.colorA[queueNumber] + ChatColor.BOLD + "Gewonnen!" + ChatColor.GRAY + " =-", "", 10, 40, 40);
                                player.playSound(player.getLocation(), Sound.ENTITY_ALLAY_AMBIENT_WITH_ITEM, 1.0f, 0.5f);
                                player.playSound(player.getLocation(), Sound.ENTITY_ALLAY_AMBIENT_WITH_ITEM, 1.0f, 1.0f);
                                player.playSound(player.getLocation(), Sound.ENTITY_ALLAY_AMBIENT_WITHOUT_ITEM, 1.0f, 2f);
                                Points.givePoints(player, won);
                            }
                            for (Player player : Verteiler.teamB.get(queueNumber)) {
                                player.sendTitle(ChatColor.GRAY + "-= " + Verteiler.colorB[queueNumber] + ChatColor.BOLD + "Verloren!" + ChatColor.GRAY + " =-", "", 10, 40, 40);
                                player.playSound(player.getLocation(), Sound.ENTITY_LIGHTNING_BOLT_THUNDER, 0.1f, 2f);
                                player.playSound(player.getLocation(), Sound.ENTITY_ILLUSIONER_AMBIENT, 0.5f, 0.5f);
                                player.playSound(player.getLocation(), Sound.ENTITY_PILLAGER_AMBIENT, 0.5f, 0.5f);
                                Points.givePoints(player, lost);
                            }
                        } else if (countB > countA) {
                            for (Player player : Verteiler.teamB.get(queueNumber)) {
                                player.sendTitle(ChatColor.GRAY + "-= " + Verteiler.colorB[queueNumber] + ChatColor.BOLD + "Gewonnen!" + ChatColor.GRAY + " =-", "", 10, 40, 40);
                                player.playSound(player.getLocation(), Sound.ENTITY_ALLAY_AMBIENT_WITH_ITEM, 1.0f, 0.5f);
                                player.playSound(player.getLocation(), Sound.ENTITY_ALLAY_AMBIENT_WITH_ITEM, 1.0f, 1.0f);
                                player.playSound(player.getLocation(), Sound.ENTITY_ALLAY_AMBIENT_WITHOUT_ITEM, 1.0f, 2f);
                                Points.givePoints(player, won);
                            }
                            for (Player player : Verteiler.teamA.get(queueNumber)) {
                                player.sendTitle(ChatColor.GRAY + "-= " + Verteiler.colorA[queueNumber] + ChatColor.BOLD + "Verloren!" + ChatColor.GRAY + " =-", "", 10, 40, 40);
                                player.playSound(player.getLocation(), Sound.ENTITY_LIGHTNING_BOLT_THUNDER, 0.1f, 2f);
                                player.playSound(player.getLocation(), Sound.ENTITY_ILLUSIONER_AMBIENT, 0.5f, 0.5f);
                                player.playSound(player.getLocation(), Sound.ENTITY_PILLAGER_AMBIENT, 0.5f, 0.5f);
                                Points.givePoints(player, lost);
                            }
                        } else {
                            for (Player player : Verteiler.teamA.get(queueNumber)) {
                                player.sendTitle(ChatColor.DARK_GRAY + "-= " + ChatColor.GRAY + "Draw!" + ChatColor.DARK_GRAY + " =-", "", 10, 40, 40);
                            }
                            for (Player player : Verteiler.teamB.get(queueNumber)) {
                                player.sendTitle(ChatColor.DARK_GRAY + "-= " + ChatColor.GRAY + "Draw!" + ChatColor.DARK_GRAY + " =-", "", 10, 40, 40);
                            }
                        }
                    },(tick));
                },(tick/2));
            },(tick/2));
        },(tick/2));
        DeadGame(queueNumber);
    }

    public static void checkDead(int n) {
        if (Verteiler.teamA.get(n).isEmpty() && Verteiler.teamB.get(n).isEmpty()) {
            DeadStop.stopGame(n);
        }
    }

    public static void DeadGame(int n) {
        List<Player> queuePlayers = Start.getPlayersInQueue(n);
        ClearInventory.invClear(queuePlayers);
        finished[n] = true;
        if (plugin == null) {
            Bukkit.getLogger().severe("Plugin instance is null in DeadStop!");
            return; // Beenden Sie die Methode, um weitere Fehler zu vermeiden
        }
        if (Start.gameTask[n] != 0) {
            Bukkit.getScheduler().cancelTask(Start.gameTask[n]);
            Start.gameTask[n]= 0;
        }
        if (Start.game[n] != 0) {
            Bukkit.getScheduler().cancelTask(Start.game[n]);
            Start.game[n]= 0;
        }
        if (Start.time[n] != 0) {
            Bukkit.getScheduler().cancelTask(Start.time[n]);
            Start.time[n] = 0;
        }
        if (Start.timeA[n] != 0) {
            Bukkit.getScheduler().cancelTask(Start.timeA[n] );
            Start.timeA[n]  = 0;
        }
        if (Start.timeB[n]  != 0) {
            Bukkit.getScheduler().cancelTask(Start.timeB[n]);
            Start.timeB[n] = 0;
        }
        if (Start.TaskId[n] != 0) {
            Bukkit.getScheduler().cancelTask(Start.TaskId[n]);
            Start.TaskId[n] = 0;
        }
        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
            DeadStop.stopGame(n);
            finished[n] = false;
        }, (long) tick * 6);
    }
}
