package mc.cws.paintOff.Game.Management;

import mc.cws.paintOff.Configuration;
import mc.cws.paintOff.Game.Arena.Arena;
import mc.cws.paintOff.Game.Arena.ArenaAuswahl;
import mc.cws.paintOff.Game.Extras.ClearInventory;
import mc.cws.paintOff.Game.Extras.Colorer;
import mc.cws.paintOff.Game.Extras.Painter;
import mc.cws.paintOff.Game.Items.ArsenalInventoryListener;
import mc.cws.paintOff.Game.Management.Scoreboards.Scoreboards;
import mc.cws.paintOff.Game.Resources.FulePoints;
import mc.cws.paintOff.Game.Resources.ExtraItems;
import mc.cws.paintOff.Game.Resources.UltPoints;
import mc.cws.paintOff.Game.Management.InGame.Game;
import mc.cws.paintOff.Game.Shop.Lists;
import mc.cws.paintOff.Game.Shop.ShopInventory;
import mc.cws.paintOff.PaintOffMain;
import mc.cws.paintOff.Po.Modifications;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.*;
import org.bukkit.entity.Player;

import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import mc.cws.paintOff.Po.ArenaManager;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.List;

public class Start {
    private static PaintOffMain plugin;
    public static void setPlugin(PaintOffMain plugin) {Start.plugin = plugin;}
    public static final Map<Integer, List<Player>> kitNumberBefore = new HashMap<>();
    public static Map<Player, Integer> colored = new HashMap<>();
    public static Map<Player, Double> points = new HashMap<>();
    public static Map<Player, Integer> fule = new HashMap<>();
    public static Map<Player, Integer> ultpoints = new HashMap<>();
    public static Map<Player, Integer> kills = new HashMap<>();
    public static Map<Player, Integer> rampage = new HashMap<>();
    public static Map<Player, Boolean> sound = new HashMap<>();

    public static final List<Player> speed1 = new ArrayList<>();
    public static final List<Player> speed2 = new ArrayList<>();
    public static final List<Player> spawnSave1 = new ArrayList<>();
    public static final List<Player> spawnSave2 = new ArrayList<>();
    public static final List<Player> spawnSave3 = new ArrayList<>();

    public static int[] remainingMinutes = new int[Configuration.maxQueues];
    public static int[] remainingSeconds = new int[Configuration.maxQueues];
    public static int[] remainingTens = new int[Configuration.maxQueues];

    public static int[][][] teamColored; // n | Team (0 = Team 1, 1 = Team 2) | Farbe
    public static boolean[] gameRunning;
    public static int[] gameTask;
    public static int[] game;
    public static int[] timer;
    public static int[] TaskId;
    public static int[] time;
    public static int[] timeA;
    public static int[] timeB;
    public static int[] timeC;
    public static String[] blockName;
    public static boolean[] golding;
    public static boolean[] isHallowed;
    public static void initializeArrays() {
        // Initialize arrays if not already initialized
        gameRunning = new boolean[Configuration.maxQueues];
        gameTask = new int[Configuration.maxQueues];
        game = new int[Configuration.maxQueues];
        timer = new int[Configuration.maxQueues];
        TaskId = new int[Configuration.maxQueues];
        time = new int[Configuration.maxQueues];
        timeA = new int[Configuration.maxQueues];
        timeB = new int[Configuration.maxQueues];
        timeC = new int[Configuration.maxQueues];
        teamColored = new int[Configuration.maxQueues][2][1];
        blockName = new String[Configuration.maxQueues];
        golding = new boolean[Configuration.maxQueues];
        isHallowed = new boolean[Configuration.maxQueues];

        // Reset all values
        Arrays.fill(gameRunning, false);
        Arrays.fill(gameTask, 0);
        Arrays.fill(game, 0);
        Arrays.fill(timer, 0);
        Arrays.fill(TaskId, 0);
        Arrays.fill(time, 0);
        Arrays.fill(timeA, 0);
        Arrays.fill(timeB, 0);
        Arrays.fill(timeC, 0);
        Arrays.fill(remainingMinutes, Configuration.gameTime-1);
        Arrays.fill(remainingSeconds, 9);
        Arrays.fill(remainingTens, 5);
        Arrays.fill(blockName, "_WOOL");
        Arrays.fill(golding, false);
        Arrays.fill(isHallowed, false);

        for (int i = 0; i <= Configuration.maxWaffen + 1; i++) {
            kitNumberBefore.put(i, new ArrayList<>());
        }

        // Log initialization
        if (plugin != null) {
            plugin.getLogger().info("Start: Arrays initialized for " + Configuration.maxQueues + " queues");
        }
    }
    
    static {
        gameRunning = null;
        gameTask = null;
        game = null;
        timer = null;
        TaskId = null;
        time = null;
        timeA = null;
        timeB = null;
        timeC = null;
    }
    public static String[] currentArena = new String[Configuration.maxQueues];
    static final int[] count = new int[Configuration.maxQueues];

    public static boolean isPlayerInQueue(int n, Player player) {
        if (Queue.queuedPlayers.containsKey(n)) {
            return Queue.queuedPlayers.get(n).contains(player);
        }
        return false;
    }

    public static List<Player> getPlayersInQueue(int n) {
        if (!Queue.queuedPlayers.containsKey(n)) {
            return new ArrayList<>();
        }
        return new ArrayList<>(Queue.queuedPlayers.get(n));
    }

    public static void startGame(int n) {
        // Validate queue number
        if (n < 0 || n >= Configuration.maxQueues) {plugin.getLogger().severe("Error: Invalid queue number: " + n);return;}

        // Validate queue state
        List<Player> queueList = Queue.queuedPlayers.get(n);
        if (queueList == null || queueList.isEmpty()) {plugin.getLogger().severe("Error: Queue " + n + " is empty!");return;}

        // Validate game state
        if (gameRunning[n]) {plugin.getLogger().severe("Error: Game is already running in queue " + n);return;}

        // Validate arena
        String arenaName = ArenaAuswahl.chosenArena(n);
        ArenaAuswahl.arena1[n] = null;
        ArenaAuswahl.arena2[n] = null;
        ArenaAuswahl.vote[n][0] = 0;
        ArenaAuswahl.vote[n][1] = 0;
        ArenaAuswahl.hasVoted.clear();
        if (arenaName == null || arenaName.isEmpty()) {
            plugin.getLogger().severe("Error: No available arenas found!");
            return;
        }

        // Log game start attempt
        plugin.getLogger().info("Attempting to start game in queue " + n + " with arena " + arenaName);

        // Initialize game state
        try {
            // Set game state
            gameRunning[n] = true; // game is running
            count[n] = Configuration.gameTime; // count is in minutes for game

            // Create lobby directory if it doesn't exist
        File lobbyDir = new File(Configuration.mainCommand + "-lobby");
        if (!lobbyDir.exists()) {
            if (!lobbyDir.mkdirs()) {
                Bukkit.getLogger().warning(Configuration.name + " | " + ChatColor.GRAY + "Konnte po-lobby Verzeichnis nicht erstellen!");
            }
        }

        // Create OccupiedWorlds directory inside po-lobby
        File occupiedWorldsDir = new File(lobbyDir, "OccupiedWorlds");
        if (!occupiedWorldsDir.exists()) {
            if (!occupiedWorldsDir.mkdirs()) {
                Bukkit.getLogger().warning(Configuration.name + " | " + ChatColor.GRAY + "Konnte OccupiedWorlds Verzeichnis nicht erstellen!");
            }
        }

        // Create Occupied-PoX.dat file in OccupiedWorlds directory
        File occupiedFile = new File(occupiedWorldsDir, "Occupied-" + Configuration.mainCommand + n + ".dat");
        if (!occupiedFile.exists()) {
            try {
                if (!occupiedFile.createNewFile()) {
                    throw new IOException("Failed to create Occupied-" + Configuration.mainCommand + n + ".dat file");
                }
                Bukkit.getLogger().info(Configuration.name + " | " + ChatColor.GRAY + "Occupied-Po" + n + ".dat wurde erstellt");
            } catch (IOException e) {
                Bukkit.getLogger().severe(Configuration.name + " | " + ChatColor.GRAY + "Fehler beim Erstellen der Occupied-Po" + n + ".dat: " + e.getMessage());
                throw new RuntimeException(e);
            }
        }
            // Initialize game tasks ---------------------------------------------------------------------------------------------
            Bukkit.getScheduler().cancelTask(Queue.secondTimer[n]);

            Stop.arenaName[n] = arenaName;
            game[n] = 1;
            timer[n] = 1;
            gameTask[n] = 1;
            TaskId[n] = 1;
            time[n] = 1;
            timeA[n] = 1;
            timeB[n] = 1;
            timeC[n] = 1;
            currentArena[n] = arenaName;
            blockName[n] = Painter.getBlockType();
            golding[n] = Modifications.searchMod("GoldeneStunde");
            isHallowed[n] = Modifications.searchMod("HallowedEve");
            int temporalLoop = -1;

            Verteiler.verteilen(n);
            ArenaManager.pasteArena(arenaName, n);

            for (Player player : queueList) {
                if (player == null || !player.isOnline()) {
                    continue;
                }
                // Add to game ------------------------------------------------------------------------------------------------
                Game.addPlayer(player, n);
                colored.put(player, 0);
                points.put(player, 0.0);
                fule.put(player, 80);
                ultpoints.put(player, 0);
                kills.put(player, 0);
                rampage.put(player, 0);
                sound.put(player, true);
                for (int i = 0; i < ShopInventory.DURATION_ROUNDS+1; i++) {
                    if (Lists.speed2.get(i).contains(player)) {
                        speed2.add(player);
                        break;
                    } else if (Lists.speed1.get(i).contains(player)) {
                        speed1.add(player);
                        break;
                    }
                    if (Lists.protection1.get(i).contains(player)) {
                        spawnSave1.add(player);
                        break;
                    } else if (Lists.protection2.get(i).contains(player)) {
                        spawnSave2.add(player);
                        break;
                    } else if (Lists.protection3.get(i).contains(player)) {
                        spawnSave3.add(player);
                        break;
                    }
                }
                player.addPotionEffect(new PotionEffect(PotionEffectType.SATURATION, Configuration.gameTime*60*20, 0, false, false));
                int kitNumber = ArsenalInventoryListener.getKitNumber(player);
                
                // Remove player from any existing kit list
                for (List<Player> playerList : kitNumberBefore.values()) {
                    playerList.remove(player);
                }
                
                // Add player to the new kit list if kit number is valid
                if (Configuration.testbuild) {
                    Configuration.unreleasedWeapons = 0;
                }
                if (kitNumber >= 0 && kitNumber <= Configuration.maxWaffen-Configuration.unreleasedWeapons) {
                    kitNumberBefore.computeIfAbsent(kitNumber, k -> new ArrayList<>()).add(player);
                }

                // Check if world exists
                World world = Bukkit.getWorld(Configuration.mainCommand + n);
                if (world == null) {
                    // World does not exist, stop the game immediately
                    for (Player p : queueList) {
                        if (p != null && p.isOnline()) {
                            p.sendMessage(ChatColor.RED + Configuration.name + " | " + ChatColor.GRAY + "Nicht genug Welten! Queue wird Aufgelöst!");
                        }
                    }
                    DeadStop.stopGame(n);
                    return;
                }
                Bukkit.getWorlds().forEach(world2 -> world.setGameRule(GameRule.NATURAL_REGENERATION, false));
                Bukkit.getWorlds().forEach(world2 -> world.setGameRule(GameRule.DO_MOB_SPAWNING, false));
                player.setGameMode(GameMode.ADVENTURE);
                ClearInventory.invClearPlayer(player);

                if (Modifications.searchMod("Weihnacht")) {
                    temporalLoop = 0;
                } else if (Modifications.searchMod("Halloween")) {
                    temporalLoop = 1;
                } else if (Modifications.searchMod("Fruhling")) {
                    temporalLoop = 2;
                } else if (Modifications.searchMod("Strand")) {
                    temporalLoop = 3;
                }

                // Set world time
                if (Modifications.searchMod("NachtZeit")) {
                    world.setTime(18000L);
                } else if (Modifications.searchMod("Morgenzeit")) {
                    world.setTime(23500L);
                } else if (Modifications.searchMod("Abendzeit")) {
                    world.setTime(12500L);
                } else if (Modifications.searchMod("RealTime")) {
                    world.setTime(getRealTime());
                } else if (Modifications.searchMod("Zufallszeit")) {
                    Random random = new Random();
                    int randomTime = random.nextInt(24000) + 6000; // Generate random time between 6000 and 48000
                    world.setTime(randomTime);
                } else {
                    world.setTime(6000L);
                }

                if (Modifications.searchMod("FließendeZeit")) {
                    Bukkit.getWorlds().forEach(world2 -> world2.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, true));
                } else {
                    Bukkit.getWorlds().forEach(world2 -> world2.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, false));
                }

                // Send messages and sounds ----------------------------------------------------------------------------------
                Arena.portToArena(player, n);
                ArsenalInventoryListener.getArsenal(player);
                ExtraItems.getKills(player);

                // Give initial FulePoints
                FulePoints.giveFulePoint(player,80);
                player.sendMessage(ChatColor.GOLD + Configuration.name + " | " + ChatColor.GRAY + "Map: " + ChatColor.GREEN + arenaName);
                player.playSound(player.getLocation(), Sound.BLOCK_AMETHYST_BLOCK_RESONATE, 1.0f, 1.0f);
            }

            // Schedule game tasks ----------------------------------------------------------------------------------------------
            scheduleGameTasks(n,temporalLoop);
            if (isHallowed[n]) {
                ExtraItems.getHalloween(n);
            }

        } catch (Exception e) {
            plugin.getLogger().severe("Error starting game: " + e.getMessage());
            handleGameStartFailure(n, queueList);
        }
    }

    private static void handleGameStartFailure(int n, List<Player> players) {
        for (Player player : players) {
            if (player != null && player.isOnline()) {
                player.sendMessage(ChatColor.RED + Configuration.name + " | " +
                        ChatColor.GRAY + "Spiel konnte nicht gestartet werden: " + "Failed to start game");
                player.sendMessage(ChatColor.GRAY + "Queue " + n + " wurde aufgelöst!");
            }
        }
        DeadStop.stopGame(n);
    }

    private static void scheduleGameTasks(int n, int temporalLoop) {
        // Minute counter
        if (TaskId[n] == 1) {
            TaskId[n] = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, () -> {
                if (count[n] > 0) {
                    if (count[n] != Configuration.gameTime) {
                        for (Player player : Queue.queuedPlayers.get(n)) {
                            if (player != null && player.isOnline()) {
                                player.sendTitle("",
                                        "§a  " + count[n] + ChatColor.GRAY + " Minute(n) noch!", 0, 60, 20);
                                player.playSound(player.getLocation(), Sound.BLOCK_AMETHYST_BLOCK_RESONATE, 1.0f, 1.0f);
                            }
                        }
                    }
                    count[n]--;
                }
            }, 0, 20 * 60L);
        }

        // In Start.java, wo der Task gestartet wird:
        if (gameTask[n] == 1) {
            gameTask[n] = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, () -> {
                List<Player> players = Queue.queuedPlayers.get(n);
                if (players != null && !players.isEmpty()) {
                    for (Player player : players) {
                        String color = Colorer.getTeamColor(player,false);
                        assert color != null;

                        PotionEffect waterBreathing = player.getPotionEffect(PotionEffectType.WATER_BREATHING);
                        PotionEffect strength = player.getPotionEffect(PotionEffectType.STRENGTH);
                        PotionEffect invisibility = player.getPotionEffect(PotionEffectType.INVISIBILITY);
                        PotionEffect unluck = player.getPotionEffect(PotionEffectType.UNLUCK);
                        PotionEffect ultpoints = player.getPotionEffect(PotionEffectType.FIRE_RESISTANCE);

                        if (rampage.get(player) == 5) {
                            player.getWorld().spawnParticle(Particle.WITCH, player.getLocation().add(0, 0.1, 0), 2, 0.25, 0, 0.25, 0.01);
                        }
                        if (rampage.get(player) == 4) {
                            player.getWorld().spawnParticle(Particle.WITCH, player.getLocation().add(0, 0.1, 0), 1, 0.25, 0, 0.25, 0.01);
                        }
                        if (rampage.get(player) == 3 && invisibility == null) {
                            Verteiler.playColorParticle(color, player.getLocation(), 0.6, 3, 0.25, Particle.DUST);
                        }
                        if (rampage.get(player) == 2 && invisibility == null) {
                            Verteiler.playColorParticle(color, player.getLocation(), 0.4, 2, 0.1, Particle.DUST);
                        }
                        if (rampage.get(player) == 1 && invisibility == null) {
                            Verteiler.playColorParticle(color, player.getLocation(), 0.2, 1, 0.1, Particle.DUST);
                        }
                        if (FulePoints.isAbleToCollect(player)) {
                            FulePoints.giveFulePoint(player,1);
                            for (int i = ShopInventory.DURATION_ROUNDS; i > 0; i--) {
                                if (FulePoints.isAbleToCollect(player) && Lists.recharge1.get(i).contains(player)) {
                                    FulePoints.giveFulePoint(player,1);
                                    break;
                                } else if (FulePoints.isAbleToCollect(player) && Lists.recharge2.get(i).contains(player)) {
                                    FulePoints.giveFulePoint(player,1);
                                    FulePoints.giveFulePoint(player,1);
                                    break;
                                }
                            }
                        }
                        if (unluck != null) {
                            String opoColor = Colorer.getTeamColor(player,true);
                            Verteiler.playColorParticleBubble(opoColor, player.getLocation().add(0,0.75,0), 0.2, 2, 0.2, Particle.DUST);
                        }
                        if (ultpoints != null) {
                            player.getWorld().spawnParticle(Particle.DUST_PLUME, player.getLocation().add(0, 0.5, 0), 1, 0.25, 0, 0.25, 0.01);
                            player.getWorld().spawnParticle(Particle.ASH, player.getLocation().add(0, 1.5, 0), 4, 0.25, 0.5, 0.25, 0.01);
                        }
                        if (FulePoints.isAbleToCollect(player) && waterBreathing != null) {
                            FulePoints.giveFulePoint(player,1);
                            FulePoints.giveFulePoint(player,1);
                            FulePoints.giveFulePoint(player,1);
                            FulePoints.giveFulePoint(player,1);
                            FulePoints.giveFulePoint(player,1);
                            FulePoints.giveFulePoint(player,1);
                            FulePoints.giveFulePoint(player,1);
                            FulePoints.giveFulePoint(player,1);
                        }
                        if (FulePoints.isAbleToCollect(player) && strength != null) {
                            FulePoints.giveFulePoint(player,1);
                            FulePoints.giveFulePoint(player,1);
                        }
                        if (!isHallowed[n]) {
                            player.getInventory().setItem(4, null);
                        }
                        player.getInventory().setItem(3, null);
                        player.getInventory().setItem(7, null);
                        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(ChatColor.GRAY + "Farbe: " + color + fule.get(player) + ChatColor.GRAY +
                                " | Ultpunkte: " + color + ChatColor.BOLD + Start.ultpoints.get(player) + ChatColor.GRAY + " / " + UltPoints.getUltPoints(player)));
                    }
                    Stop.checkDead(n);
                }
            }, 0, 2);
        }

        if (game[n] == 1) {
            game[n] = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, () -> {
                List<Player> players = mc.cws.paintOff.Game.Management.Queue.queuedPlayers.get(n);
                if (players != null && !players.isEmpty()) {
                    for (Player player : players) {
                        PotionEffect invis = player.getPotionEffect(PotionEffectType.INVISIBILITY);
                        PotionEffect strength = player.getPotionEffect(PotionEffectType.STRENGTH);
                        PotionEffect shield = player.getPotionEffect(PotionEffectType.RESISTANCE);
                        if (shield != null) {
                            player.getWorld().spawnParticle(Particle.END_ROD, player.getLocation().add(0,0.5,0), 2, 0.25, 0.5, 0.25, 0.05);
                        }
                        if (strength != null) {
                            player.getWorld().spawnParticle(Particle.TRIAL_OMEN, player.getLocation(), 1, 0.1, 0.1, 0.1, 0.01);
                            player.getWorld().spawnParticle(Particle.RAID_OMEN, player.getLocation(), 1, 0.1, 0.1, 0.1, 0.01);
                        }
                        if (Game.cantSneak.contains(player)) {
                            player.getWorld().spawnParticle(Particle.DRIPPING_OBSIDIAN_TEAR, player.getLocation().add(0,1,0), 1, 0.25, 0.5, 0.25, 0.01);
                        }
                        if (invis != null) {
                            player.getWorld().spawnParticle(Particle.DOLPHIN, player.getLocation(), 3, 0.25, 0, 0.25, 1);
                        }
                        if (UltPoints.hasEnoughUltPoints(player,UltPoints.getUltPoints(player))) {
                            player.getWorld().spawnParticle(Particle.TRIAL_SPAWNER_DETECTION_OMINOUS, player.getLocation(), 1, 0.25, 0.1, 0.25, 0.01);
                            player.getWorld().spawnParticle(Particle.TRIAL_SPAWNER_DETECTION, player.getLocation(), 1, 0.25, 0.1, 0.25, 0.01);
                        }

                        if (temporalLoop ==0) {
                            player.getWorld().spawnParticle(Particle.FIREWORK, player.getLocation().add(0,10,0), 2, 20, 10, 20, 0.01);
                            player.getWorld().spawnParticle(Particle.SNOWFLAKE, player.getLocation().add(0,10,0), 5, 20, 10, 20, 0.01);
                        } else if ((temporalLoop ==1)) {
                            player.getWorld().spawnParticle(Particle.PALE_OAK_LEAVES, player.getLocation().add(0,10,0), 1, 20, 10, 20, 0.01);
                            player.getWorld().spawnParticle(Particle.ASH, player.getLocation().add(0,10,0), 10, 20, 10, 20, 0.01);
                        } else if ((temporalLoop ==2)) {
                            player.getWorld().spawnParticle(Particle.CHERRY_LEAVES, player.getLocation().add(0,10,0), 4, 20, 10, 20, 0.01);
                        } else if ((temporalLoop ==3)) {
                            player.getWorld().spawnParticle(Particle.CRIMSON_SPORE, player.getLocation(), 20, 20, 10, 20, 0.01);
                        }
                    }
                    Stop.checkDead(n);
                } else {
                    DeadStop.stopGame(n);
                }
            }, 0, 2);
        }
        if (timer[n] == 1) {
            Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                timer[n] = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, () -> {
                    remainingSeconds[n]--;
                    if (remainingSeconds[n] == -1) {
                        remainingTens[n]--;
                        remainingSeconds[n] = 9;
                    }
                    if (remainingTens[n] == -1) {
                        remainingMinutes[n]--;
                        remainingTens[n] = 5;
                    }
                    for (Player player : Queue.queuedPlayers.get(n)) {
                        Scoreboards.updateScoreboardGame(player);
                    }
                    if (remainingMinutes[n] == -1) {
                        remainingMinutes[n] = 0;
                        remainingTens[n] = 0;
                        remainingSeconds[n] = 0;
                        Bukkit.getScheduler().cancelTask(timer[n]);
                        timer[n] = 0;
                    }
                }, 0, 20);
            }, 40);
        }

        // 30 second warning
        if (time[n] == 1) {
            time[n] = Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                List<Player> players = mc.cws.paintOff.Game.Management.Queue.queuedPlayers.get(n);
                if (players != null) {
                    for (Player player : players) {
                        if (player != null && player.isOnline()) {
                            player.sendMessage(ChatColor.YELLOW + Configuration.name + " | " +
                                    ChatColor.GRAY + "Spiel endet in " + ChatColor.GREEN + "30 " +
                                    ChatColor.GRAY + "Sekunden!");
                            player.playSound(player.getLocation(), Sound.BLOCK_AMETHYST_BLOCK_RESONATE, 0.5f, 0.5f);
                        }
                    }
                }
            }, (long) Configuration.tick * 60 * Configuration.gameTime - (Configuration.tick * 30L));
        }

        // 10 second warning
        if (timeA[n] == 1) {
            timeA[n] = Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                List<Player> players = mc.cws.paintOff.Game.Management.Queue.queuedPlayers.get(n);
                if (players != null) {
                    for (int i = 10; i > 0; i--) {
                        int finalI = i;
                        timeA[n] = Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                            List<Player> currentPlayers = mc.cws.paintOff.Game.Management.Queue.queuedPlayers.get(n);
                            if (currentPlayers != null) {
                                for (Player player : currentPlayers) {
                                    if (player != null && player.isOnline()) {
                                        player.sendTitle("", "§7  " + ChatColor.BOLD + finalI, 10, 20, 10);
                                        player.playSound(player.getLocation(), Sound.ENTITY_BREEZE_LAND, 0.5f, 0.5f);
                                    }
                                }
                            }
                        }, (long) (10 - i) * Configuration.tick); // Calculate delay based on current number
                    }
                }
            }, (long) Configuration.tick * 60 * Configuration.gameTime - (Configuration.tick * 10L));
        }

        // 10 second warning
        if (timeB[n] == 1) {
            timeB[n] = Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                List<Player> players = Queue.queuedPlayers.get(n);
                if (players != null) {
                    for (Player player : players) {
                        if (player != null && player.isOnline()) {
                            Stop.checkDead(n);
                            Stop.check(n);
                        }
                    }
                }
            }, (long) Configuration.tick * 60 * Configuration.gameTime);
        }

        if (timeB[n] == 1) {
            timeB[n] = Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                List<Player> players = Queue.queuedPlayers.get(n);
                if (players != null) {
                    for (Player player : players) {
                        if (player != null && player.isOnline()) {
                            Stop.checkDead(n);
                            Stop.check(n);
                        }
                    }
                }
            }, (long) Configuration.tick * 60 * Configuration.gameTime);

        } if (timeC[n] == 1) {  // Check if no task is scheduled
            timeC[n] = Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                List<Player> players = mc.cws.paintOff.Game.Management.Queue.queuedPlayers.get(n);
                if (players != null) {
                    for (Player player : players) {
                        if (player != null && player.isOnline()) {
                            Arena.portToArena(player, n);
                        }
                    }
                }
                timeC[n] = 0;  // Reset the task ID after execution
            }, (long) Configuration.tick);
        }
    }

    public static int getQueueNumber(Player player) {
        if (player == null) {
            return -1;
        }

        String playerName = player.getName();

        for (int i = 0; i < Configuration.maxQueues; i++) {
            if (mc.cws.paintOff.Game.Management.Queue.queuedPlayers.containsKey(i)) {
                for (Player p : Queue.queuedPlayers.get(i)) {
                    if (p != null) {
                        p.getName();
                        if (p.getName().equals(playerName)) {
                            return i;
                        }
                    }
                }
            }
        }
        return -1;
    }

    private static long getRealTime() {
        // Hole die aktuelle Systemzeit
        Calendar calendar = Calendar.getInstance();
        int hours = calendar.get(Calendar.HOUR_OF_DAY); // 0-23
        int minutes = calendar.get(Calendar.MINUTE);    // 0-59

        int totalMinutes = hours * 60 + minutes;
        long minecraftTicks = (long)(totalMinutes * (24000.0 / (24 * 60)));
        minecraftTicks = (minecraftTicks + 18000) % 24000;

        return minecraftTicks;
    }
}
