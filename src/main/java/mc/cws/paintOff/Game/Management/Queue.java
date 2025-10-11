package mc.cws.paintOff.Game.Management;

import mc.cws.paintOff.Configuration;
import mc.cws.paintOff.Game.Arena.Arena;
import mc.cws.paintOff.Game.Arena.ArenaAuswahl;
import mc.cws.paintOff.Game.Extras.ClearInventory;
import mc.cws.paintOff.Game.Resources.ExtraItems;
import mc.cws.paintOff.Game.Management.Scoreboards.Scoreboards;
import mc.cws.paintOff.PaintOffMain;
import mc.cws.paintOff.Po.Modifications;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.*;

public class Queue {
    private static PaintOffMain plugin;

    public static void setPlugin(PaintOffMain plugin) {
        Queue.plugin = plugin;
    }

    public static Map<Integer, List<Player>> queuedPlayers = new HashMap<>();
    public static Map<Integer, List<String>> playerNames = new HashMap<>();
    public static int maxSize = Configuration.normalMaxSize;
    public static int minSize;
    public static int[] countAllow;
    public static boolean[] queueBlock;
    public static boolean[] lokStartBlock;
    public static int[] secondTimer;
    public static int[] playerCount;

    static {
        // Initialize arrays
        if (Modifications.searchMod("SixPlayer")) {
            maxSize = 6;
        } else if (Modifications.searchMod("EightPlayer")) {
            maxSize = 8;
        } else if (Modifications.searchMod("TenPlayer")) {
            maxSize = 10;
        } else if (Modifications.searchMod("TwelvePlayer")) {
            maxSize = 12;
        }
        minSize = (maxSize-2)+Configuration.minSizeAddition;
        playerCount = new int[Configuration.maxQueues];
        queueBlock = new boolean[Configuration.maxQueues];
        lokStartBlock = new boolean[Configuration.maxQueues];
        secondTimer = new int[Configuration.maxQueues];
        countAllow = new int[Configuration.maxQueues];
        Arrays.fill(queueBlock, false);
        Arrays.fill(lokStartBlock, false);
        Arrays.fill(secondTimer, 0);
        Arrays.fill(countAllow, 0);

        // Initialize queuedPlayers and playerNames maps
        for (int i = 0; i < Configuration.maxQueues; i++) {
            queuedPlayers.put(i, new ArrayList<>());
            playerNames.put(i, new ArrayList<>());
        }
    }

    public static void initializeQueues() {
        // Initialize queuedPlayers and playerNames maps
        for (int i = 0; i < Configuration.maxQueues; i++) {
            queuedPlayers.put(i, new ArrayList<>());
            playerNames.put(i, new ArrayList<>());
        }
    }

    public static void setParameters() {
        initializeQueues();
        Arrays.fill(queueBlock, false);
        Arrays.fill(Start.gameRunning, false);
        Arrays.fill(lokStartBlock, false);
        Arrays.fill(secondTimer, 0);
        Arrays.fill(countAllow, 0);
    }

    public static void queueGame(Player player) {
        queueGameReal(player, true);
    }

    public static void queueGameReal(Player player, boolean sendMessages) {
        if (Modifications.searchMod("SixPlayer")) {
            maxSize = 6;
        } else if (Modifications.searchMod("EightPlayer")) {
            maxSize = 8;
        } else if (Modifications.searchMod("TenPlayer")) {
            maxSize = 10;
        } else if (Modifications.searchMod("TwelvePlayer")) {
            maxSize = 12;
        }
        // Finde eine Queue
        int queueNumber = findAvailableQueue();
        if (queueNumber == -1) {
            player.sendMessage(ChatColor.RED + Configuration.name + " | " + ChatColor.GRAY + "All queues are currently blocked!");
            return;
        }

        // Find available position
        int position = findAvailablePosition(queueNumber);
        if (position == -1) {
            player.sendMessage(ChatColor.RED + Configuration.name + " | " + ChatColor.GRAY + "The queue is full!");
            return;
        }

        // Add player to queue
        List<Player> queueList = queuedPlayers.computeIfAbsent(queueNumber, k -> new ArrayList<>());
        // Only send join messages if player is not already in the queue
        if (!queueList.contains(player)) {
            queueList.add(player);
            playerNames.get(queueNumber).add(position, player.getName());
            playerCount[queueNumber] = queueList.size();

            // Send join messages only if requested
            if (sendMessages) {
                player.sendMessage(ChatColor.GOLD + Configuration.name + " | " + ChatColor.GRAY + "Warteschlange beigetreten " +
                        ChatColor.GOLD + "(" + (playerCount[queueNumber]) + "/" + (maxSize) + ")");
                player.sendMessage(ChatColor.DARK_GRAY + "§o" + "jetzt in Queue: " + queueNumber);
            }
        }
        // Warteschlange aktualisieren
        updatePlayerCountDisplay(queueNumber);
        if (position == 0) {
            ArenaAuswahl.getTwoRandomArenas(queueNumber);
        }
        Arena.portLobby(player);
        ClearInventory.invClearPlayer(player);
        ExtraItems.getItemArsenal(player);
        ExtraItems.getItemLeaving(player);
        ExtraItems.getItemAuswahl(player);
        ExtraItems.getShop(player);
        Scoreboards.updateScoreboardQueue(player);

        // Check if queue has reached minimum size
        if (playerCount[queueNumber] >= minSize) {
            countAllow[queueNumber] = 1;
            broadcastMessage(queueNumber, ChatColor.GOLD + Configuration.name + " | " +
                    ChatColor.GRAY + "Spiel beginnt in " + ChatColor.GREEN + Configuration.queueWaiting + ChatColor.GRAY + " Sekunden!");
            for (Player p : queueList) {
                p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_BELL, 0.5f, 1.0f);
            }
        }

        // Block queue if full
        if (playerCount[queueNumber] == maxSize) {
            queueBlock[queueNumber] = true;
        }

        // Start countdown timer if minimum size is reached
        if (countAllow[queueNumber] == 1) {
            scheduleCountdownTimer(queueNumber);
        }
    }

    private static int findAvailableQueue() {
        // First try to find an empty queue
        for (int i = 0; i < Configuration.maxQueues; i++) {
            if (!queueBlock[i] && !Start.gameRunning[i]) {
                List<Player> queueList = queuedPlayers.get(i);
                if (queueList != null && queueList.size() < maxSize && !queueList.isEmpty()) {
                    return i;
                }
            }
        }
        for (int i = 0; i < Configuration.maxQueues; i++) {
            if (!queueBlock[i] && !Start.gameRunning[i]) {
                List<Player> queueList = queuedPlayers.get(i);
                if (queueList != null && queueList.isEmpty()) {
                    return i;
                } else if (queueList != null && queueList.size() < maxSize) {
                    return i;
                }
            }
        }
        return -1;
    }

    private static int findAvailablePosition(int queueNumber) {
        try {
            if (queuedPlayers.containsKey(queueNumber)) {
                List<Player> queue = queuedPlayers.get(queueNumber);

                for (int i = 0; i < maxSize; i++) {
                    // Wenn Liste kürzer ist als maxSize, oder Slot ist leer (null)
                    if (i >= queue.size() || queue.get(i) == null) {
                        return i; // Erste freie Position
                    }
                }
            }
            return -1; // Kein freier Slot gefunden oder Queue existiert nicht
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    public static void updatePlayerCountDisplay(int queueNumber) {
        for (Player p : queuedPlayers.get(queueNumber)) {
            p.spigot().sendMessage(net.md_5.bungee.api.ChatMessageType.ACTION_BAR,
                    new TextComponent(ChatColor.GRAY + "Warteschlange: " + ChatColor.GOLD + "(" + playerCount[queueNumber] + "/" + maxSize + ")"));
        }
    }

    private static void broadcastMessage(int queueNumber, String message) {
        for (Player p : queuedPlayers.get(queueNumber)) {
            p.sendMessage(message);
        }
    }

    private static void scheduleCountdownTimer(int queueNumber) {
        // Timer zurücksetzen
        lokStartBlock[queueNumber] = true;

        int delay = (Configuration.queueWaiting - Configuration.finalCounter) * 20;
        secondTimer[queueNumber] = Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
            for (Player p : queuedPlayers.get(queueNumber)) {
                p.spigot().sendMessage(net.md_5.bungee.api.ChatMessageType.ACTION_BAR,
                        new TextComponent(ChatColor.GRAY + "Noch " + ChatColor.GOLD + "10" + ChatColor.GRAY + " Sekunden!"));
                p.getWorld().playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_BELL, 0.5f, 1.0f);
            }
            scheduleCountdownTimerTwo(queueNumber);
        }, delay);
    }

    public static void scheduleCountdownTimerTwo(int queueNumber) {
        // Timer zurücksetzen
        secondTimer[queueNumber] = 0;

        secondTimer[queueNumber] = Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
            for (Player p : queuedPlayers.get(queueNumber)) {
                p.spigot().sendMessage(net.md_5.bungee.api.ChatMessageType.ACTION_BAR,
                        new TextComponent(ChatColor.GOLD + "3" + ChatColor.GRAY + " Sekunden!"));
                p.getWorld().playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_BELL, 0.5f, 1.0f);
            }
            scheduleCountdownTimerThree(queueNumber);
        }, 20 * 7);
    }

    public static void scheduleCountdownTimerThree(int queueNumber) {
        // Timer zurücksetzen
        secondTimer[queueNumber] = 0;

        // 2 Sekunden Countdown
        secondTimer[queueNumber] = Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
            for (Player p : queuedPlayers.get(queueNumber)) {
                p.spigot().sendMessage(net.md_5.bungee.api.ChatMessageType.ACTION_BAR,
                        new TextComponent(ChatColor.GOLD + "2" + ChatColor.GRAY + " Sekunden!"));
                p.getWorld().playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_BELL, 0.5f, 1.0f);
            }
            // 1 Sekunden Countdown
            secondTimer[queueNumber] = Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                for (Player p : queuedPlayers.get(queueNumber)) {
                    p.spigot().sendMessage(net.md_5.bungee.api.ChatMessageType.ACTION_BAR,
                            new TextComponent(ChatColor.GOLD + "1" + ChatColor.GRAY + " Sekunde!"));
                    p.getWorld().playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_BELL, 0.5f, 1.0f);
                }
                // Start!
                secondTimer[queueNumber] = Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                    for (Player p : queuedPlayers.get(queueNumber)) {
                        p.spigot().sendMessage(net.md_5.bungee.api.ChatMessageType.ACTION_BAR,
                                new TextComponent(ChatColor.GOLD + "Start!"));
                        p.getWorld().playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 0.5f, 1.0f);
                        Scoreboards.removeScoreboard(p);
                    }
                    Start.startGame(queueNumber);
                    lokStartBlock[queueNumber] = false;
                }, 20); // 1 Sekunde
            }, 20); // 2 Sekunden
        }, 20); // 3 Sekunden
    }

    public static void checkQueue(int queueNumber) {
        try {
            if (!queuedPlayers.containsKey(queueNumber)) {
                return;
            }
            
            List<Player> queueList = queuedPlayers.get(queueNumber);
            if (queueList.isEmpty()) {
                // Reset queue state but keep it available
                queueBlock[queueNumber] = false;
                lokStartBlock[queueNumber] = false;
                countAllow[queueNumber] = 0;
                playerCount[queueNumber] = 0;

                // Clear player lists
                playerNames.get(queueNumber).clear();

                // Cancel any running timers
                if (secondTimer[queueNumber] != 0) {
                    Bukkit.getScheduler().cancelTask(secondTimer[queueNumber]);
                    secondTimer[queueNumber] = 0;
                }

                DeadStop.stopGame(queueNumber);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void stopQueue(int queueNumber) {
        try {
            // Delete Occupied-PoX.dat file
            File occupiedFile = new File(Configuration.mainCommand + "-lobby/OccupiedWorlds", "Occupied-" + Configuration.mainCommand + queueNumber + ".dat");
            if (occupiedFile.exists()) {
                boolean deleted = occupiedFile.delete();
                if (!deleted) {
                    plugin.getLogger().warning("Failed to delete file: Occupied");
                }
            } else {
                plugin.getLogger().warning("File does not exist: " + occupiedFile.getAbsolutePath());
            }
            
            // Cancel any running timers
            if (secondTimer[queueNumber] != 0) {
                Bukkit.getScheduler().cancelTask(secondTimer[queueNumber]);
                secondTimer[queueNumber] = 0;
            }
            
            // Reset all queue flags
            countAllow[queueNumber] = 0;
            queueBlock[queueNumber] = false;
            lokStartBlock[queueNumber] = false;
            
            // Clear player lists and ensure they exist
            List<String> names = playerNames.get(queueNumber);
            if (names != null) {
                names.clear();
            } else {
                playerNames.put(queueNumber, new ArrayList<>());
            }
            List<Player> players = queuedPlayers.get(queueNumber);
            if (players != null) {
                players.clear();
            } else {
                queuedPlayers.put(queueNumber, new ArrayList<>());
            }
            playerCount[queueNumber] = 0;
        } catch (Exception e) {
            e.printStackTrace();
            plugin.getLogger().severe("Error resetting queue " + queueNumber + ": " + e.getMessage());
        }
    }
}
