package mc.cws.paintOff.Po;

import mc.cws.paintOff.Configuration;
import mc.cws.paintOff.Game.Management.Verteiler;
import mc.cws.paintOff.PaintOffMain;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Modifications implements Listener {
    private static final Logger LOGGER = Logger.getLogger(Modifications.class.getName());

    private static PaintOffMain plugin;

    public static void setPlugin(PaintOffMain plugin) {
        Modifications.plugin = plugin;}

    private static final String MODS_FOLDER = "mods";
    private static final String MODIFIERS_FOLDER = "Modifiers";
    private static final String BLOCKS_FOLDER = "Blocks";
    private static final String TIME_FOLDER = "Time";
    private static final String PALLETS_FOLDER = "Pallets";
    private static final String FESTIVAL_FOLDER = "Festivals";
    private static final String GAME_FOLDER = "GameMaster";

    private static File modsDir;
    private static File modifiersDir;
    private static File blocksDir;
    private static File timeDir;
    private static File palletsDir;
    private static File festivalsDir;
    private static File gameMasterDir;

    private static final String MENU_TITLE = ChatColor.DARK_PURPLE + "- Menu -";
    public static final Inventory Menu = Bukkit.createInventory(null, 9, MENU_TITLE);
    private static final String EVENT_TITLE = ChatColor.DARK_PURPLE + "- Events -";
    public static final Inventory Event = Bukkit.createInventory(null, 45, EVENT_TITLE);
    private static final String PALETTE_TITLE = ChatColor.DARK_PURPLE + "- Paletten -";
    public static final Inventory Palette = Bukkit.createInventory(null, 27, PALETTE_TITLE);
    private static final String BLOCK_TITLE = ChatColor.DARK_PURPLE + "- Blockarten -";
    public static final Inventory Block = Bukkit.createInventory(null, 27, BLOCK_TITLE);
    private static final String FESTIVAL_TITLE = ChatColor.DARK_PURPLE + "- Festivals -";
    public static final Inventory Festival = Bukkit.createInventory(null, 27, FESTIVAL_TITLE);

    private static final Map<String, Boolean> modCache = new ConcurrentHashMap<>();
    private static final Map<Integer, String> lineToModMap = new ConcurrentHashMap<>();
    private static final Map<String, Integer> modToLineMap = new ConcurrentHashMap<>();
    private static File kitsFile;

    public static void onEnable(JavaPlugin plugin) {
        // Erstelle die Verzeichnisstruktur
        kitsFile = new File(plugin.getDataFolder(), "mods.yml");
        modsDir = new File(plugin.getDataFolder(), MODS_FOLDER);
        modifiersDir = new File(modsDir, MODIFIERS_FOLDER);
        blocksDir = new File(modsDir, BLOCKS_FOLDER);
        timeDir = new File(modsDir, TIME_FOLDER);
        palletsDir = new File(modsDir, PALLETS_FOLDER);
        festivalsDir = new File(modsDir, FESTIVAL_FOLDER);
        gameMasterDir = new File(modsDir, GAME_FOLDER);

        // Erstelle die Verzeichnisse, falls sie nicht existieren
        for (File dir : new File[]{modsDir, modifiersDir, blocksDir, timeDir, palletsDir, festivalsDir, gameMasterDir}) {
            if (!dir.exists()) {
                if (!dir.mkdirs()) {
                    LOGGER.severe("Konnte Verzeichnis nicht erstellen: " + dir.getAbsolutePath());
                }
            }
        }
        // Erstelle Standard-Mods
        createDefaultMods();

        // Lade die Mods
        loadAllMods();
        loadModCache();
    }

    private static void createDefaultMods() {
        // Pallets
        createDefaultMod(palletsDir, "Default", true);
        createDefaultMod(palletsDir, "BlackWhite", false);
        createDefaultMod(palletsDir, "Halloween", false);
        createDefaultMod(palletsDir, "Weihnacht", false);
        createDefaultMod(palletsDir, "Fruhling", false);
        createDefaultMod(palletsDir, "Strand", false);
        createDefaultMod(palletsDir, "Ultra", false);
        createDefaultMod(palletsDir, "Pastell", false);

        // Time
        createDefaultMod(timeDir, "Zufallszeit", false);
        createDefaultMod(timeDir, "Abendzeit", false);
        createDefaultMod(timeDir, "Morgenzeit", false);
        createDefaultMod(timeDir, "NachtZeit", false);
        createDefaultMod(timeDir, "RealTime", true);

        // Blocks
        createDefaultMod(blocksDir, "GlazedTerracotta", false);
        createDefaultMod(blocksDir, "Concrete", false);
        createDefaultMod(blocksDir, "Terracotta", false);
        createDefaultMod(blocksDir, "Wolle", true);

        // Modifiers
        createDefaultMod(modifiersDir, "FließendeZeit", false);
        createDefaultMod(modifiersDir, "HalbierteUltpower", false);
        createDefaultMod(modifiersDir, "DoppeltePunkte", false);

        createDefaultMod(festivalsDir, "GoldeneStunde", false);
        createDefaultMod(festivalsDir, "HallowedEve", false);

        createDefaultMod(gameMasterDir, "SixPlayer", false);
        createDefaultMod(gameMasterDir, "EightPlayer", false);
        createDefaultMod(gameMasterDir, "TenPlayer", false);
        createDefaultMod(gameMasterDir, "TwelvePlayer", false);
    }

    private static void createDefaultMod(File dir, String modName, boolean defaultValue) {
        File modFile = new File(dir, modName + ".txt");
        if (!modFile.exists()) {
            try {
                Files.writeString(modFile.toPath(), String.valueOf(defaultValue));
                LOGGER.info("Erstelle Standard-Mod: " + modFile.getName());
            } catch (IOException e) {
                LOGGER.log(Level.SEVERE, "Konnte Standard-Mod nicht erstellen: " + modFile.getName(), e);
            }
        }
    }

    private static void loadAllMods() {
        modCache.clear();
        lineToModMap.clear();
        modToLineMap.clear();

        // Lade Modifier
        loadModsFromDirectory(modifiersDir, 0);
        // Lade Blocks
        loadModsFromDirectory(blocksDir, 10); // Beispiel-Offset
        // Lade Time
        loadModsFromDirectory(timeDir, 20); // Beispiel-Offset
        // Lade Pallets
        loadModsFromDirectory(palletsDir, 30); // Beispiel-Offset

        loadModsFromDirectory(festivalsDir, 40);
    }

    private static File determineModDirectory(String modName) {
        // Hier kannst du die Logik anpassen, um zu bestimmen, in welchen Ordner die Mod gehört
        if (modName.equals("Default") || modName.equals("BlackWhite") || modName.equals("Halloween") ||
                modName.equals("Weihnacht") || modName.equals("Fruhling") || modName.equals("Strand")) {
            return palletsDir;
        } else if (modName.equals("Zufallszeit") || modName.equals("Abendzeit") ||
                modName.equals("Morgenzeit") || modName.equals("NachtZeit") || modName.equals("RealTime")) {
            return timeDir;
        } else if (modName.equals("GlazedTerracotta") || modName.equals("Concrete") ||
                modName.equals("Terracotta") || modName.equals("Wolle")) {
            return blocksDir;
        } else if (modName.equals("GoldeneStunde") || modName.equals("HallowedEve")) {
            return festivalsDir;
        } else if (modName.equals("SixPlayer") || modName.equals("EightPlayer") || modName.equals("TenPlayer") || modName.equals("TwelvePlayer")) {
            return gameMasterDir;
        } else {
            return modifiersDir;
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getView().getTitle().equals(MENU_TITLE)) {
            event.setCancelled(true);

            if (event.getWhoClicked() instanceof Player player) {
                int clickedSlot = event.getRawSlot();
                if (clickedSlot == 1) {
                    player.closeInventory();
                    player.openInventory(Event);
                } else if (clickedSlot == 3) {
                    player.closeInventory();
                    player.openInventory(Palette);
                } else if (clickedSlot == 5) {
                    player.closeInventory();
                    player.openInventory(Block);
                } else if (clickedSlot == 7) {
                    player.closeInventory();
                    player.openInventory(Festival);
                }
            }
        }
        if (event.getView().getTitle().equals(EVENT_TITLE)) {
            event.setCancelled(true);

            if (event.getWhoClicked() instanceof Player player) {
                int clickedSlot = event.getRawSlot();
                if (clickedSlot == 1) {
                    boolean currentState = searchMod("DoppeltePunkte");
                    setMod("DoppeltePunkte", !currentState);
                    player.sendMessage(ChatColor.GOLD + Configuration.name + " | " + ChatColor.GRAY + "Event: " +
                            ChatColor.YELLOW + "Doppelte Punkte " + ChatColor.GRAY +
                            (!currentState ? "aktiviert" : "deaktiviert") + "!");

                    // Update the item in the inventory
                    Event.setItem(1, createDubblePoints());
                } else if (clickedSlot == 2) {
                    boolean currentState = searchMod("HalbierteUltpower");
                    setMod("HalbierteUltpower", !currentState);
                    player.sendMessage(ChatColor.GOLD + Configuration.name + " | " + ChatColor.GRAY + "Event: " +
                            ChatColor.YELLOW + "Halbierte Ultpower " + ChatColor.GRAY +
                            (!currentState ? "aktiviert" : "deaktiviert") + "!");

                    // Update the item in the inventory
                    Event.setItem(2, createHalfedUltcharge());
                } else if (clickedSlot == 10) {
                    boolean currentState = searchMod("FließendeZeit");
                    setMod("FließendeZeit", !currentState);
                    player.sendMessage(ChatColor.GOLD + Configuration.name + " | " + ChatColor.GRAY + "Event: " +
                            ChatColor.YELLOW + "Fließende Zeit " + ChatColor.GRAY +
                            (!currentState ? "aktiviert" : "deaktiviert") + "!");

                    // Update the item in the inventory
                    Event.setItem(10, createFlowingTime());
                } else if (clickedSlot == 11) {
                    boolean currentState = searchMod("RealTime");
                    setMod("RealTime", !currentState);
                    setMod("FließendeZeit", false);
                    setMod("NachtZeit", false);
                    setMod("Abendzeit", false);
                    setMod("Zufallszeit", false);
                    setMod("Morgenzeit", false);
                    player.sendMessage(ChatColor.GOLD + Configuration.name + " | " + ChatColor.GRAY + "Event: " +
                            ChatColor.YELLOW + "Echte Zeit " + ChatColor.GRAY +
                            (!currentState ? "aktiviert" : "deaktiviert") + "!");
                    // Update the item in the inventory
                    Event.setItem(11, createRealTime());
                    Event.setItem(4, createNightTime());
                    Event.setItem(13, createDawnTime());
                    Event.setItem(22, createDuskTime());
                    Event.setItem(31, createRandomTime());
                    Event.setItem(10, createFlowingTime());
                } else if (clickedSlot == 4) {
                    boolean currentState = searchMod("NachtZeit");
                    setMod("NachtZeit", !currentState);
                    setMod("Abendzeit", false);
                    setMod("Zufallszeit", false);
                    setMod("Morgenzeit", false);
                    player.sendMessage(ChatColor.GOLD + Configuration.name + " | " + ChatColor.GRAY + "Event: " +
                            ChatColor.YELLOW + "Nachtzeit " + ChatColor.GRAY +
                            (!currentState ? "aktiviert" : "deaktiviert") + "!");

                    // Update the item in the inventory
                    Event.setItem(4, createNightTime());
                    Event.setItem(13, createDawnTime());
                    Event.setItem(22, createDuskTime());
                    Event.setItem(31, createRandomTime());
                } else if (clickedSlot == 13) {
                    boolean currentState = searchMod("Morgenzeit");
                    setMod("Morgenzeit", !currentState);
                    setMod("Abendzeit", false);
                    setMod("Zufallszeit", false);
                    setMod("NachtZeit", false);
                    player.sendMessage(ChatColor.GOLD + Configuration.name + " | " + ChatColor.GRAY + "Event: " +
                            ChatColor.YELLOW + "Morgenzeit " + ChatColor.GRAY +
                            (!currentState ? "aktiviert" : "deaktiviert") + "!");

                    // Update the item in the inventory
                    Event.setItem(4, createNightTime());
                    Event.setItem(13, createDawnTime());
                    Event.setItem(22, createDuskTime());
                    Event.setItem(31, createRandomTime());
                } else if (clickedSlot == 22) {
                    boolean currentState = searchMod("Abendzeit");
                    setMod("Abendzeit", !currentState);
                    setMod("Morgenzeit", false);
                    setMod("Zufallszeit", false);
                    setMod("NachtZeit", false);
                    player.sendMessage(ChatColor.GOLD + Configuration.name + " | " + ChatColor.GRAY + "Event: " +
                            ChatColor.YELLOW + "Abendzeit " + ChatColor.GRAY +
                            (!currentState ? "aktiviert" : "deaktiviert") + "!");

                    // Update the item in the inventory
                    Event.setItem(4, createNightTime());
                    Event.setItem(13, createDawnTime());
                    Event.setItem(22, createDuskTime());
                    Event.setItem(31, createRandomTime());
                } else if (clickedSlot == 31) {
                    boolean currentState = searchMod("Zufallszeit");
                    setMod("Zufallszeit", !currentState);
                    setMod("Morgenzeit", false);
                    setMod("Abendzeit", false);
                    setMod("NachtZeit", false);
                    player.sendMessage(ChatColor.GOLD + Configuration.name + " | " + ChatColor.GRAY + "Event: " +
                            ChatColor.YELLOW + "Zufallszeit " + ChatColor.GRAY +
                            (!currentState ? "aktiviert" : "deaktiviert") + "!");

                    // Update the item in the inventory
                    Event.setItem(4, createNightTime());
                    Event.setItem(13, createDawnTime());
                    Event.setItem(22, createDuskTime());
                    Event.setItem(31, createRandomTime());
                } else if (clickedSlot == 6) {
                    boolean currentState = searchMod("SixPlayer");
                    setMod("SixPlayer", !currentState);
                    setMod("EightPlayer", false);
                    setMod("TenPlayer", false);
                    setMod("TwelvePlayer", false);
                    player.sendMessage(ChatColor.GOLD + Configuration.name + " | " + ChatColor.GRAY + "Game: " +
                            ChatColor.YELLOW + "6-Spieler " + ChatColor.GRAY +
                            (!currentState ? "aktiviert" : "deaktiviert") + "!");

                    Event.setItem(6, createSixPlayers());
                    Event.setItem(15, createEightPlayers());
                    Event.setItem(24, createTenPlayers());
                    Event.setItem(33, createTwelvePlayers());
                } else if (clickedSlot == 15) {
                    boolean currentState = searchMod("EightPlayer");
                    setMod("SixPlayer", false);
                    setMod("EightPlayer", !currentState);
                    setMod("TenPlayer", false);
                    setMod("TwelvePlayer", false);
                    player.sendMessage(ChatColor.GOLD + Configuration.name + " | " + ChatColor.GRAY + "Game: " +
                            ChatColor.YELLOW + "8-Spieler " + ChatColor.GRAY +
                            (!currentState ? "aktiviert" : "deaktiviert") + "!");

                    Event.setItem(6, createSixPlayers());
                    Event.setItem(15, createEightPlayers());
                    Event.setItem(24, createTenPlayers());
                    Event.setItem(33, createTwelvePlayers());
                } else if (clickedSlot == 24) {
                    boolean currentState = searchMod("TenPlayer");
                    setMod("SixPlayer", false);
                    setMod("EightPlayer", false);
                    setMod("TenPlayer", !currentState);
                    setMod("TwelvePlayer", false);
                    player.sendMessage(ChatColor.GOLD + Configuration.name + " | " + ChatColor.GRAY + "Game: " +
                            ChatColor.YELLOW + "10-Spieler " + ChatColor.GRAY +
                            (!currentState ? "aktiviert" : "deaktiviert") + "!");

                    Event.setItem(6, createSixPlayers());
                    Event.setItem(15, createEightPlayers());
                    Event.setItem(24, createTenPlayers());
                    Event.setItem(33, createTwelvePlayers());
                } else if (clickedSlot == 33) {
                    boolean currentState = searchMod("TwelvePlayer");
                    setMod("SixPlayer", false);
                    setMod("EightPlayer", false);
                    setMod("TenPlayer", false);
                    setMod("TwelvePlayer", !currentState);
                    player.sendMessage(ChatColor.GOLD + Configuration.name + " | " + ChatColor.GRAY + "Game: " +
                            ChatColor.YELLOW + "12-Spieler " + ChatColor.GRAY +
                            (!currentState ? "aktiviert" : "deaktiviert") + "!");

                    Event.setItem(6, createSixPlayers());
                    Event.setItem(15, createEightPlayers());
                    Event.setItem(24, createTenPlayers());
                    Event.setItem(33, createTwelvePlayers());
                }
                else if (clickedSlot == 40) {
                    player.closeInventory();
                    player.openInventory(Menu);
                }
            }
        }
        if (event.getView().getTitle().equals(PALETTE_TITLE)) {
            event.setCancelled(true);

            if (event.getWhoClicked() instanceof Player player) {
                int clickedSlot = event.getRawSlot();
                if (clickedSlot == 1) {
                    setMod("Default", true);
                    setMod("BlackWhite", false);
                    setMod("Halloween", false);
                    setMod("Weihnacht", false);
                    setMod("Strand", false);
                    setMod("Fruhling", false);
                    setMod("Ultra", false);
                    setMod("Pastell", false);
                    player.sendMessage(ChatColor.GOLD + Configuration.name + " | " + ChatColor.GRAY + "Palette: " +
                            ChatColor.YELLOW + "Normale Palette " + ChatColor.GRAY + "ausgewählt" + "!");

                    Palette.setItem(1, createDefault());
                    Palette.setItem(3, createBlackWhite());
                    Palette.setItem(5, createHalloween());
                    Palette.setItem(7, createCristmas());
                    Palette.setItem(10, createSpring());
                    Palette.setItem(12, createSommer());
                    Palette.setItem(14, createUltra());
                    Palette.setItem(16, createPastell());
                } else if (clickedSlot == 3) {
                    boolean currentState = searchMod("BlackWhite");
                    setMod("BlackWhite", !currentState);
                    setMod("Fruhling", false);
                    setMod("Helloween", false);
                    setMod("Weihnacht", false);
                    setMod("Strand", false);
                    setMod("Ultra", false);
                    setMod("Pastell", false);
                    setMod("Default", currentState);
                    player.sendMessage(ChatColor.GOLD + Configuration.name + " | " + ChatColor.GRAY + "Palette: " +
                            ChatColor.YELLOW + "Schwarz-Weiß Palette " + ChatColor.GRAY +
                            (!currentState ? "aktiviert" : "deaktiviert") + "!");

                    // Update the item in the inventory
                    Palette.setItem(1, createDefault());
                    Palette.setItem(3, createBlackWhite());
                    Palette.setItem(5, createHalloween());
                    Palette.setItem(7, createCristmas());
                    Palette.setItem(10, createSpring());
                    Palette.setItem(12, createSommer());
                    Palette.setItem(14, createUltra());
                    Palette.setItem(16, createPastell());
                } else if (clickedSlot == 5) {
                    boolean currentState = searchMod("Halloween");
                    setMod("Halloween", !currentState);
                    setMod("Fruhling", false);
                    setMod("BlackWhite", false);
                    setMod("Weihnacht", false);
                    setMod("Strand", false);
                    setMod("Ultra", false);
                    setMod("Pastell", false);
                    setMod("Default", currentState);
                    player.sendMessage(ChatColor.GOLD + Configuration.name + " | " + ChatColor.GRAY + "Palette: " +
                            ChatColor.YELLOW + "Halloween Palette " + ChatColor.GRAY +
                            (!currentState ? "aktiviert" : "deaktiviert") + "!");

                    // Update the item in the inventory
                    Palette.setItem(1, createDefault());
                    Palette.setItem(3, createBlackWhite());
                    Palette.setItem(5, createHalloween());
                    Palette.setItem(7, createCristmas());
                    Palette.setItem(10, createSpring());
                    Palette.setItem(12, createSommer());
                    Palette.setItem(14, createUltra());
                    Palette.setItem(16, createPastell());
                } else if (clickedSlot == 7) {
                    boolean currentState = searchMod("Weihnacht");
                    setMod("Weihnacht", !currentState);
                    setMod("Strand", false);
                    setMod("Fruhling", false);
                    setMod("Halloween", false);
                    setMod("BlackWhite", false);
                    setMod("Ultra", false);
                    setMod("Pastell", false);
                    setMod("Default", currentState);
                    player.sendMessage(ChatColor.GOLD + Configuration.name + " | " + ChatColor.GRAY + "Palette: " +
                            ChatColor.YELLOW + "Weihnachts Palette " + ChatColor.GRAY +
                            (!currentState ? "aktiviert" : "deaktiviert") + "!");

                    // Update the item in the inventory
                    Palette.setItem(1, createDefault());
                    Palette.setItem(3, createBlackWhite());
                    Palette.setItem(5, createHalloween());
                    Palette.setItem(7, createCristmas());
                    Palette.setItem(10, createSpring());
                    Palette.setItem(12, createSommer());
                    Palette.setItem(14, createUltra());
                    Palette.setItem(16, createPastell());
                } else if (clickedSlot == 10) {
                    boolean currentState = searchMod("Fruhling");
                    setMod("Fruhling", !currentState);
                    setMod("Strand", false);
                    setMod("Weihnacht", false);
                    setMod("Halloween", false);
                    setMod("BlackWhite", false);
                    setMod("Ultra", false);
                    setMod("Pastell", false);
                    setMod("Default", currentState);
                    player.sendMessage(ChatColor.GOLD + Configuration.name + " | " + ChatColor.GRAY + "Palette: " +
                            ChatColor.YELLOW + "Blüten Palette " + ChatColor.GRAY +
                            (!currentState ? "aktiviert" : "deaktiviert") + "!");

                    // Update the item in the inventory
                    Palette.setItem(1, createDefault());
                    Palette.setItem(3, createBlackWhite());
                    Palette.setItem(5, createHalloween());
                    Palette.setItem(7, createCristmas());
                    Palette.setItem(10, createSpring());
                    Palette.setItem(12, createSommer());
                    Palette.setItem(14, createUltra());
                    Palette.setItem(16, createPastell());
                } else if (clickedSlot == 12) {
                    boolean currentState = searchMod("Strand");
                    setMod("Strand", !currentState);
                    setMod("Fruhling", false);
                    setMod("Weihnacht", false);
                    setMod("Halloween", false);
                    setMod("BlackWhite", false);
                    setMod("Ultra", false);
                    setMod("Pastell", false);
                    setMod("Default", currentState);
                    player.sendMessage(ChatColor.GOLD + Configuration.name + " | " + ChatColor.GRAY + "Palette: " +
                            ChatColor.YELLOW + "Strand Palette " + ChatColor.GRAY +
                            (!currentState ? "aktiviert" : "deaktiviert") + "!");

                    // Update the item in the inventory
                    Palette.setItem(1, createDefault());
                    Palette.setItem(3, createBlackWhite());
                    Palette.setItem(5, createHalloween());
                    Palette.setItem(7, createCristmas());
                    Palette.setItem(10, createSpring());
                    Palette.setItem(12, createSommer());
                    Palette.setItem(14, createUltra());
                    Palette.setItem(16, createPastell());
                } else if (clickedSlot == 14) {
                    boolean currentState = searchMod("Ultra");
                    setMod("Ultra", !currentState);
                    setMod("Strand", false);
                    setMod("Fruhling", false);
                    setMod("Weihnacht", false);
                    setMod("Halloween", false);
                    setMod("BlackWhite", false);
                    setMod("Default", currentState);
                    player.sendMessage(ChatColor.GOLD + Configuration.name + " | " + ChatColor.GRAY + "Palette: " +
                            ChatColor.YELLOW + "Ultra Palette " + ChatColor.GRAY +
                            (!currentState ? "aktiviert" : "deaktiviert") + "!");

                    // Update the item in the inventory
                    Palette.setItem(1, createDefault());
                    Palette.setItem(3, createBlackWhite());
                    Palette.setItem(5, createHalloween());
                    Palette.setItem(7, createCristmas());
                    Palette.setItem(10, createSpring());
                    Palette.setItem(12, createSommer());
                    Palette.setItem(14, createUltra());
                    Palette.setItem(16, createPastell());
                } else if (clickedSlot == 16) {
                    boolean currentState = searchMod("Pastell");
                    setMod("Pastell", !currentState);
                    setMod("Ultra", false);
                    setMod("Strand", false);
                    setMod("Fruhling", false);
                    setMod("Weihnacht", false);
                    setMod("Halloween", false);
                    setMod("BlackWhite", false);
                    setMod("Default", currentState);
                    player.sendMessage(ChatColor.GOLD + Configuration.name + " | " + ChatColor.GRAY + "Palette: " +
                            ChatColor.YELLOW + "Pastell Palette " + ChatColor.GRAY +
                            (!currentState ? "aktiviert" : "deaktiviert") + "!");

                    // Update the item in the inventory
                    Palette.setItem(1, createDefault());
                    Palette.setItem(3, createBlackWhite());
                    Palette.setItem(5, createHalloween());
                    Palette.setItem(7, createCristmas());
                    Palette.setItem(10, createSpring());
                    Palette.setItem(12, createSommer());
                    Palette.setItem(14, createUltra());
                    Palette.setItem(16, createPastell());
                } else if (clickedSlot == 22) {
                    player.closeInventory();
                    player.openInventory(Menu);
                }
            }
        }
        if (event.getView().getTitle().equals(BLOCK_TITLE)) {
            event.setCancelled(true);

            if (event.getWhoClicked() instanceof Player player) {
                int clickedSlot = event.getRawSlot();
                if (clickedSlot == 1) {
                    setMod("Wolle", true);
                    setMod("Terracotta", false);
                    setMod("Concrete", false);
                    setMod("GlazedTerracotta", false);
                    player.sendMessage(ChatColor.GOLD + Configuration.name + " | " + ChatColor.GRAY + "Block: " +
                            ChatColor.YELLOW + "Wolle " + ChatColor.GRAY + "ausgewählt" + "!");

                    // Update the item in the inventory
                    Block.setItem(1, createWolle());
                    Block.setItem(3, createTerracotta());
                    Block.setItem(5, createConcrete());
                    Block.setItem(7, createGlazedTerracotta());
                } else if (clickedSlot == 3) {
                    boolean currentState = searchMod("Terracotta");
                    setMod("Wolle", currentState);
                    setMod("Terracotta", !currentState);
                    setMod("Concrete", false);
                    setMod("GlazedTerracotta", false);
                    player.sendMessage(ChatColor.GOLD + Configuration.name + " | " + ChatColor.GRAY + "Block: " +
                            ChatColor.YELLOW + "Terracotta " + ChatColor.GRAY +
                            (!currentState ? "aktiviert" : "deaktiviert") + "!");

                    // Update the item in the inventory
                    Block.setItem(1, createWolle());
                    Block.setItem(3, createTerracotta());
                    Block.setItem(5, createConcrete());
                    Block.setItem(7, createGlazedTerracotta());
                } else if (clickedSlot == 5) {
                    boolean currentState = searchMod("Concrete");
                    setMod("Wolle", currentState);
                    setMod("Terracotta", false);
                    setMod("Concrete", !currentState);
                    setMod("GlazedTerracotta", false);
                    player.sendMessage(ChatColor.GOLD + Configuration.name + " | " + ChatColor.GRAY + "Block: " +
                            ChatColor.YELLOW + "Concrete " + ChatColor.GRAY +
                            (!currentState ? "aktiviert" : "deaktiviert") + "!");

                    // Update the item in the inventory
                    Block.setItem(1, createWolle());
                    Block.setItem(3, createTerracotta());
                    Block.setItem(5, createConcrete());
                    Block.setItem(7, createGlazedTerracotta());
                } else if (clickedSlot == 7) {
                    boolean currentState = searchMod("GlazedTerracotta");
                    setMod("Wolle", currentState);
                    setMod("Terracotta", false);
                    setMod("Concrete", false);
                    setMod("GlazedTerracotta", !currentState);
                    player.sendMessage(ChatColor.GOLD + Configuration.name + " | " + ChatColor.GRAY + "Block: " +
                            ChatColor.YELLOW + "GlazedTerracotta " + ChatColor.GRAY +
                            (!currentState ? "aktiviert" : "deaktiviert") + "!");

                    // Update the item in the inventory
                    Block.setItem(1, createWolle());
                    Block.setItem(3, createTerracotta());
                    Block.setItem(5, createConcrete());
                    Block.setItem(7, createGlazedTerracotta());
                } else if (clickedSlot == 22) {
                    player.closeInventory();
                    player.openInventory(Menu);
                }
            }
        }
        if (event.getView().getTitle().equals(FESTIVAL_TITLE)) {
            event.setCancelled(true);

            if (event.getWhoClicked() instanceof Player player) {
                int clickedSlot = event.getRawSlot();
                if (clickedSlot == 10) {
                    boolean currentState = searchMod("GoldeneStunde");
                    setMod("GoldeneStunde", !currentState);
                    setMod("HallowedEve", false);
                    player.sendMessage(ChatColor.GOLD + Configuration.name + " | " + ChatColor.GRAY + "Event: " +
                            ChatColor.YELLOW + "Goldene Stunde " + ChatColor.GRAY +
                            (!currentState ? "aktiviert" : "deaktiviert") + "!");

                    // Update the item in the inventory
                    Festival.setItem(10, createGoldeneStunde());
                    Festival.setItem(12, createHallowedEve());
                } else if (clickedSlot == 12) {
                    boolean currentState = searchMod("HallowedEve");
                    setMod("HallowedEve", !currentState);
                    setMod("GoldeneStunde", false);
                    player.sendMessage(ChatColor.GOLD + Configuration.name + " | " + ChatColor.GRAY + "Event: " +
                            ChatColor.YELLOW + "Halloween Nacht " + ChatColor.GRAY +
                            (!currentState ? "aktiviert" : "deaktiviert") + "!");

                    // Update the item in the inventory
                    Festival.setItem(10, createGoldeneStunde());
                    Festival.setItem(12, createHallowedEve());
                } else if (clickedSlot == 22) {
                    player.closeInventory();
                    player.openInventory(Menu);
                }
            }
        }
    }

    @EventHandler
    public void onInventoryOpen(InventoryOpenEvent event) {
        if (event.getView().getTitle().equals(MENU_TITLE)) {
            // Ensure items are up to date when inventory is opened
            for (int i = 0; i < Menu.getSize(); i++) {
                ItemStack current = Menu.getItem(i);
                if (current == null || current.getType() != Material.GRAY_STAINED_GLASS_PANE) {
                    Menu.setItem(i, createGlass());
                }
            }
            Menu.setItem(1, createEventInventory());
            Menu.setItem(3, createColorInventory());
            Menu.setItem(5, createBlockInventory());
            Menu.setItem(7, createFestivalInventory());
        } else if (event.getView().getTitle().equals(EVENT_TITLE)) {
            // Ensure items are up to date when inventory is opened
            for (int i = 0; i < Event.getSize(); i++) {
                ItemStack current = Event.getItem(i);
                if (current == null || current.getType() != Material.GRAY_STAINED_GLASS_PANE) {
                    Event.setItem(i, createGlass());
                }
            }
            Event.setItem(1, createDubblePoints());
            Event.setItem(2, createHalfedUltcharge());

            Event.setItem(6, createSixPlayers());
            Event.setItem(15, createEightPlayers());
            Event.setItem(24, createTenPlayers());
            Event.setItem(33, createTwelvePlayers());

            Event.setItem(10, createFlowingTime());
            Event.setItem(4, createNightTime());
            Event.setItem(13, createDawnTime());
            Event.setItem(22, createDuskTime());
            Event.setItem(11, createRealTime());
            Event.setItem(31, createRandomTime());

            Event.setItem(40, createBack());
        } else if (event.getView().getTitle().equals(PALETTE_TITLE)) {
            // Ensure items are up to date when inventory is opened
            for (int i = 0; i < Palette.getSize(); i++) {
                ItemStack current = Palette.getItem(i);
                if (current == null || current.getType() != Material.GRAY_STAINED_GLASS_PANE) {
                    Palette.setItem(i, createGlass());
                }
            }
            Palette.setItem(1, createDefault());
            Palette.setItem(3, createBlackWhite());
            Palette.setItem(5, createHalloween());
            Palette.setItem(7, createCristmas());
            Palette.setItem(10, createSpring());
            Palette.setItem(12, createSommer());
            Palette.setItem(14, createUltra());
            Palette.setItem(16, createPastell());
            Palette.setItem(22, createBack());
        } else if (event.getView().getTitle().equals(BLOCK_TITLE)) {
            // Ensure items are up to date when inventory is opened
            for (int i = 0; i < Block.getSize(); i++) {
                ItemStack current = Block.getItem(i);
                if (current == null || current.getType() != Material.GRAY_STAINED_GLASS_PANE) {
                    Block.setItem(i, createGlass());
                }
            }
            Block.setItem(1, createWolle());
            Block.setItem(3, createTerracotta());
            Block.setItem(5, createConcrete());
            Block.setItem(7, createGlazedTerracotta());
            Block.setItem(22, createBack());
        } else if (event.getView().getTitle().equals(FESTIVAL_TITLE)) {
            // Ensure items are up to date when inventory is opened
            for (int i = 0; i < Festival.getSize(); i++) {
                ItemStack current = Festival.getItem(i);
                if (current == null || current.getType() != Material.GRAY_STAINED_GLASS_PANE) {
                    Festival.setItem(i, createGlass());
                }
            }
            Festival.setItem(10, createGoldeneStunde());
            Festival.setItem(12, createHallowedEve());
            Festival.setItem(22, createBack());
        }
    }

    // Spielgröße
    private static ItemStack createSixPlayers() {
        ItemStack playerHead = new ItemStack(Material.PLAYER_HEAD, 6);
        ItemMeta meta = playerHead.getItemMeta();

        if (meta != null) {
            meta.setDisplayName(ChatColor.GOLD + "6-Spieler Modus");
            List<String> lore = new ArrayList<>();
            lore.add(ChatColor.GRAY + "Momentan: " +
                    (searchMod("SixPlayer") ? ChatColor.GREEN + "Aktiviert" : ChatColor.RED + "Deaktiviert"));
            meta.setLore(lore);
            playerHead.setItemMeta(meta);
        }
        return playerHead;
    }
    private static ItemStack createEightPlayers() {
        ItemStack playerHead = new ItemStack(Material.PLAYER_HEAD, 8);
        ItemMeta meta = playerHead.getItemMeta();

        if (meta != null) {
            meta.setDisplayName(ChatColor.GOLD + "8-Spieler Modus");
            List<String> lore = new ArrayList<>();
            lore.add(ChatColor.GRAY + "Momentan: " +
                    (searchMod("EightPlayer") ? ChatColor.GREEN + "Aktiviert" : ChatColor.RED + "Deaktiviert"));
            meta.setLore(lore);
            playerHead.setItemMeta(meta);
        }
        return playerHead;
    }
    private static ItemStack createTenPlayers() {
        ItemStack playerHead = new ItemStack(Material.PLAYER_HEAD, 10);
        ItemMeta meta = playerHead.getItemMeta();

        if (meta != null) {
            meta.setDisplayName(ChatColor.GOLD + "10-Spieler Modus");
            List<String> lore = new ArrayList<>();
            lore.add(ChatColor.GRAY + "Momentan: " +
                    (searchMod("TenPlayer") ? ChatColor.GREEN + "Aktiviert" : ChatColor.RED + "Deaktiviert"));
            meta.setLore(lore);
            playerHead.setItemMeta(meta);
        }
        return playerHead;
    }
    private static ItemStack createTwelvePlayers() {
        ItemStack playerHead = new ItemStack(Material.PLAYER_HEAD, 12);
        ItemMeta meta = playerHead.getItemMeta();

        if (meta != null) {
            meta.setDisplayName(ChatColor.GOLD + "12-Spieler Modus");
            List<String> lore = new ArrayList<>();
            lore.add(ChatColor.GRAY + "Momentan: " +
                    (searchMod("TwelvePlayer") ? ChatColor.GREEN + "Aktiviert" : ChatColor.RED + "Deaktiviert"));
            meta.setLore(lore);
            playerHead.setItemMeta(meta);
        }
        return playerHead;
    }

    //Else
    private static ItemStack createDubblePoints() { // lineNumber = 18
        ItemStack paintball = new ItemStack(Material.GOLD_INGOT, 1);
        ItemMeta meta = paintball.getItemMeta();
        assert meta != null;
        meta.setDisplayName(ChatColor.GOLD + "Doppelte Punkte");
        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.GRAY + "Momentan: " +
                (searchMod("DoppeltePunkte") ? ChatColor.GREEN + "Aktiviert" : ChatColor.RED + "Deaktiviert"));
        meta.setLore(lore);
        paintball.setItemMeta(meta);
        return paintball;
    }

    private static ItemStack createHalfedUltcharge() { // lineNumber = 17
        ItemStack paintball = new ItemStack(Material.WIND_CHARGE, 1);
        ItemMeta meta = paintball.getItemMeta();
        assert meta != null;
        meta.setDisplayName(ChatColor.GOLD + "Halbierte Ultpower");
        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.GRAY + "Momentan: " +
                (searchMod("HalbierteUltpower") ? ChatColor.GREEN + "Aktiviert" : ChatColor.RED + "Deaktiviert"));
        meta.setLore(lore);
        paintball.setItemMeta(meta);
        return paintball;
    }

    private static ItemStack createFlowingTime() { // lineNumber = 16
        ItemStack paintball = new ItemStack(Material.CLOCK, 1);
        ItemMeta meta = paintball.getItemMeta();
        assert meta != null;
        meta.setDisplayName(ChatColor.GOLD + "Fließende Zeit");
        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.GRAY + "Momentan: " +
                (searchMod("FließendeZeit") ? ChatColor.GREEN + "Aktiviert" : ChatColor.RED + "Deaktiviert"));
        meta.setLore(lore);
        paintball.setItemMeta(meta);
        return paintball;
    }

    // Blöcke // lineNumber = 12-15
    private static ItemStack createWolle() { // lineNumber = 15
        ItemStack paintball = new ItemStack(Material.valueOf(getColorNameRaw(Verteiler.randomCollor()) + "_WOOL"), 1);
        ItemMeta meta = paintball.getItemMeta();
        assert meta != null;
        meta.setDisplayName(ChatColor.GOLD + "Wolle");
        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.GRAY + "Momentan: " +
                (searchMod("Wolle") ? ChatColor.GREEN + "Aktiviert" : ChatColor.RED + "Deaktiviert"));
        meta.setLore(lore);
        paintball.setItemMeta(meta);
        return paintball;
    }

    private static ItemStack createTerracotta() { // lineNumber = 14
        ItemStack paintball = new ItemStack(Material.valueOf(getColorNameRaw(Verteiler.randomCollor()) + "_TERRACOTTA"), 1);
        ItemMeta meta = paintball.getItemMeta();
        assert meta != null;
        meta.setDisplayName(ChatColor.GOLD + "Terracotta");
        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.GRAY + "Momentan: " +
                (searchMod("Terracotta") ? ChatColor.GREEN + "Aktiviert" : ChatColor.RED + "Deaktiviert"));
        meta.setLore(lore);
        paintball.setItemMeta(meta);
        return paintball;
    }

    private static ItemStack createConcrete() { // lineNumber = 13
        ItemStack paintball = new ItemStack(Material.valueOf(getColorNameRaw(Verteiler.randomCollor()) + "_CONCRETE"), 1);
        ItemMeta meta = paintball.getItemMeta();
        assert meta != null;
        meta.setDisplayName(ChatColor.GOLD + "Beton");
        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.GRAY + "Momentan: " +
                (searchMod("Concrete") ? ChatColor.GREEN + "Aktiviert" : ChatColor.RED + "Deaktiviert"));
        meta.setLore(lore);
        paintball.setItemMeta(meta);
        return paintball;
    }

    private static ItemStack createGlazedTerracotta() { // lineNumber = 12
        ItemStack paintball = new ItemStack(Material.valueOf(getColorNameRaw(Verteiler.randomCollor()) + "_GLAZED_TERRACOTTA"), 1);
        ItemMeta meta = paintball.getItemMeta();
        assert meta != null;
        meta.setDisplayName(ChatColor.GOLD + "Glasierter Terracotta");
        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.GRAY + "Momentan: " +
                (searchMod("GlazedTerracotta") ? ChatColor.GREEN + "Aktiviert" : ChatColor.RED + "Deaktiviert"));
        meta.setLore(lore);
        paintball.setItemMeta(meta);
        return paintball;
    }

    //Zeit
    private static ItemStack createRealTime() { // lineNumber = 11
        ItemStack paintball = new ItemStack(Material.GOLDEN_APPLE, 1);
        ItemMeta meta = paintball.getItemMeta();
        assert meta != null;
        meta.setDisplayName(ChatColor.GOLD + "Echte Zeit");
        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.GRAY + "Momentan: " +
                (searchMod("RealTime") ? ChatColor.GREEN + "Aktiviert" : ChatColor.RED + "Deaktiviert"));
        meta.setLore(lore);
        paintball.setItemMeta(meta);
        return paintball;
    }

    private static ItemStack createNightTime() { // lineNumber = 11
        ItemStack paintball = new ItemStack(Material.WITHER_ROSE, 1);
        ItemMeta meta = paintball.getItemMeta();
        assert meta != null;
        meta.setDisplayName(ChatColor.GOLD + "Nachtzeit");
        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.GRAY + "Momentan: " +
                (searchMod("NachtZeit") ? ChatColor.GREEN + "Aktiviert" : ChatColor.RED + "Deaktiviert"));
        meta.setLore(lore);
        paintball.setItemMeta(meta);
        return paintball;
    }

    private static ItemStack createDawnTime() {// lineNumber = 10
        ItemStack paintball = new ItemStack(Material.DANDELION, 1);
        ItemMeta meta = paintball.getItemMeta();
        assert meta != null;
        meta.setDisplayName(ChatColor.GOLD + "Morgenzeit");
        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.GRAY + "Momentan: " +
                (searchMod("Morgenzeit") ? ChatColor.GREEN + "Aktiviert" : ChatColor.RED + "Deaktiviert"));
        meta.setLore(lore);
        paintball.setItemMeta(meta);
        return paintball;
    }

    private static ItemStack createDuskTime() { // lineNumber = 9
        ItemStack paintball = new ItemStack(Material.POPPY, 1);
        ItemMeta meta = paintball.getItemMeta();
        assert meta != null;
        meta.setDisplayName(ChatColor.GOLD + "Abendzeit");
        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.GRAY + "Momentan: " +
                (searchMod("Abendzeit") ? ChatColor.GREEN + "Aktiviert" : ChatColor.RED + "Deaktiviert"));
        meta.setLore(lore);
        paintball.setItemMeta(meta);
        return paintball;
    }

    private static ItemStack createRandomTime() { // lineNumber = 8
        ItemStack paintball = new ItemStack(Material.PINK_PETALS, 1);
        ItemMeta meta = paintball.getItemMeta();
        assert meta != null;
        meta.setDisplayName(ChatColor.GOLD + "Zufallszeit");
        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.GRAY + "Momentan: " +
                (searchMod("Zufallszeit") ? ChatColor.GREEN + "Aktiviert" : ChatColor.RED + "Deaktiviert"));
        meta.setLore(lore);
        paintball.setItemMeta(meta);
        return paintball;
    }

    // Paletten 0-7
    private static ItemStack createDefault() { // lineNumber = 0
        ItemStack paintball = new ItemStack(Material.SNOWBALL, 1);
        ItemMeta meta = paintball.getItemMeta();
        assert meta != null;
        meta.setDisplayName(ChatColor.GOLD + "Normale Palette");
        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.GRAY + "Momentan: " +
                (searchMod("Default") ? ChatColor.GREEN + "Aktiviert" : ChatColor.RED + "Deaktiviert"));
        meta.setLore(lore);
        paintball.setItemMeta(meta);
        return paintball;
    }

    private static ItemStack createBlackWhite() { // lineNumber = 1
        ItemStack paintball = new ItemStack(Material.PALE_OAK_DOOR, 1);
        ItemMeta meta = paintball.getItemMeta();
        assert meta != null;
        meta.setDisplayName(ChatColor.GOLD + "Schwarz-Weiß Palette");
        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.GRAY + "Momentan: " +
                (searchMod("BlackWhite") ? ChatColor.GREEN + "Aktiviert" : ChatColor.RED + "Deaktiviert"));
        meta.setLore(lore);
        paintball.setItemMeta(meta);
        return paintball;
    }

    private static ItemStack createHalloween() { // lineNumber = 2
        ItemStack paintball = new ItemStack(Material.JACK_O_LANTERN, 1);
        ItemMeta meta = paintball.getItemMeta();
        assert meta != null;
        meta.setDisplayName(ChatColor.GOLD + "Halloween Palette");
        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.GRAY + "Momentan: " +
                (searchMod("Halloween") ? ChatColor.GREEN + "Aktiviert" : ChatColor.RED + "Deaktiviert"));
        meta.setLore(lore);
        paintball.setItemMeta(meta);
        return paintball;
    }

    private static ItemStack createCristmas() { // lineNumber = 3
        ItemStack paintball = new ItemStack(Material.NETHER_STAR, 1);
        ItemMeta meta = paintball.getItemMeta();
        assert meta != null;
        meta.setDisplayName(ChatColor.GOLD + "Weihnachts Palette");
        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.GRAY + "Momentan: " +
                (searchMod("Weihnacht") ? ChatColor.GREEN + "Aktiviert" : ChatColor.RED + "Deaktiviert"));
        meta.setLore(lore);
        paintball.setItemMeta(meta);
        return paintball;
    }

    private static ItemStack createSpring() { // lineNumber = 4
        ItemStack paintball = new ItemStack(Material.PINK_TULIP, 1);
        ItemMeta meta = paintball.getItemMeta();
        assert meta != null;
        meta.setDisplayName(ChatColor.GOLD + "Blüten Palette");
        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.GRAY + "Momentan: " +
                (searchMod("Fruhling") ? ChatColor.GREEN + "Aktiviert" : ChatColor.RED + "Deaktiviert"));
        meta.setLore(lore);
        paintball.setItemMeta(meta);
        return paintball;
    }

    private static ItemStack createSommer() {
        ItemStack paintball = new ItemStack(Material.AXOLOTL_BUCKET, 1);
        ItemMeta meta = paintball.getItemMeta();
        assert meta != null;
        meta.setDisplayName(ChatColor.GOLD + "Strand Palette");
        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.GRAY + "Momentan: " +
                (searchMod("Strand") ? ChatColor.GREEN + "Aktiviert" : ChatColor.RED + "Deaktiviert"));
        meta.setLore(lore);
        paintball.setItemMeta(meta);
        return paintball;
    }
    private static ItemStack createUltra() {
        ItemStack paintball = new ItemStack(Material.BLACK_GLAZED_TERRACOTTA, 1);
        ItemMeta meta = paintball.getItemMeta();
        assert meta != null;
        meta.setDisplayName(ChatColor.GOLD + "Ultra Palette");
        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.GRAY + "Momentan: " +
                (searchMod("Ultra") ? ChatColor.GREEN + "Aktiviert" : ChatColor.RED + "Deaktiviert"));
        meta.setLore(lore);
        paintball.setItemMeta(meta);
        return paintball;
    }
    private static ItemStack createPastell() {
        ItemStack paintball = new ItemStack(Material.PINK_TERRACOTTA, 1);
        ItemMeta meta = paintball.getItemMeta();
        assert meta != null;
        meta.setDisplayName(ChatColor.GOLD + "Pastell Palette");
        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.GRAY + "Momentan: " +
                (searchMod("Pastell") ? ChatColor.GREEN + "Aktiviert" : ChatColor.RED + "Deaktiviert"));
        meta.setLore(lore);
        paintball.setItemMeta(meta);
        return paintball;
    }

    // Festivals
    private static ItemStack createGoldeneStunde() {
        ItemStack paintball = new ItemStack(Material.GOLD_BLOCK, 1);
        ItemMeta meta = paintball.getItemMeta();
        assert meta != null;
        meta.setDisplayName(ChatColor.GOLD + "Goldene Stunde");
        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.GRAY + "Momentan: " +
                (searchMod("GoldeneStunde") ? ChatColor.GREEN + "Aktiviert" : ChatColor.RED + "Deaktiviert"));
        meta.setLore(lore);
        paintball.setItemMeta(meta);
        return paintball;
    }

    private static ItemStack createHallowedEve() {
        ItemStack paintball = new ItemStack(Material.JACK_O_LANTERN, 1);
        ItemMeta meta = paintball.getItemMeta();
        assert meta != null;
        meta.setDisplayName(ChatColor.GOLD + "Halloween Nacht");
        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.GRAY + "Momentan: " +
                (searchMod("HallowedEve") ? ChatColor.GREEN + "Aktiviert" : ChatColor.RED + "Deaktiviert"));
        meta.setLore(lore);
        paintball.setItemMeta(meta);
        return paintball;
    }

    // Create the menu inventory
    private static ItemStack createEventInventory() {
        ItemStack paintball = new ItemStack(Material.CAKE, 1);
        ItemMeta meta = paintball.getItemMeta();
        assert meta != null;
        meta.setDisplayName(ChatColor.GOLD + "Events");
        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.GRAY + "Wähle ein Event!");
        meta.setLore(lore);
        paintball.setItemMeta(meta);
        return paintball;
    }

    private static ItemStack createColorInventory() {
        ItemStack paintball = new ItemStack(Material.ENDER_EYE, 1);
        ItemMeta meta = paintball.getItemMeta();
        assert meta != null;
        meta.setDisplayName(ChatColor.GOLD + "Paletten");
        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.GRAY + "Wähle ein Farbpalette!");
        meta.setLore(lore);
        paintball.setItemMeta(meta);
        return paintball;
    }

    private static ItemStack createBlockInventory() {
        ItemStack paintball = new ItemStack(Material.STONE, 1);
        ItemMeta meta = paintball.getItemMeta();
        assert meta != null;
        meta.setDisplayName(ChatColor.GOLD + "Block Auswahl");
        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.GRAY + "Wähle ein Blockart!");
        meta.setLore(lore);
        paintball.setItemMeta(meta);
        return paintball;
    }

    private static ItemStack createFestivalInventory() {
        ItemStack paintball = new ItemStack(Material.FIREWORK_ROCKET, 1);
        ItemMeta meta = paintball.getItemMeta();
        assert meta != null;
        meta.setDisplayName(ChatColor.GOLD + "Festival Auswahl");
        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.GRAY + "Wähle ein Festival!");
        meta.setLore(lore);
        paintball.setItemMeta(meta);
        return paintball;
    }

    private static ItemStack createBack() {
        ItemStack paintball = new ItemStack(Material.BARRIER, 1);
        ItemMeta meta = paintball.getItemMeta();
        assert meta != null;
        meta.setDisplayName(ChatColor.RED + "Zurück");
        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.GRAY + "Kehre zur vorherigen Seite zurück!");
        meta.setLore(lore);
        paintball.setItemMeta(meta);
        return paintball;
    }

    private static ItemStack createGlass() {
        ItemStack glass = new ItemStack(Material.GRAY_STAINED_GLASS_PANE, 1);
        ItemMeta meta = glass.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(" ");
            glass.setItemMeta(meta);
        }
        return glass;
    }

    private static void loadModCache() {
        if (kitsFile == null || !kitsFile.exists()) {
            return;
        }

        try {
            List<String> lines = Files.readAllLines(kitsFile.toPath(), StandardCharsets.UTF_8);
            for (String line : lines) {
                String[] data = line.split(",", 2);
                if (data.length >= 2) {
                    try {
                        modCache.put(data[0], Boolean.parseBoolean(data[1].trim()));
                    } catch (Exception e) {
                        // Handle invalid entries
                        modCache.put(data[0], false);
                    }
                }
            }
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Failed to load mod cache: " + e.getMessage(), e);
        }
    }

    public static String getColorNameRaw(String color) {
        return switch (color) {
            case "Rot" -> "RED";
            case "Orange" -> "ORANGE";
            case "Gelb" -> "YELLOW";
            case "Hellgrün" -> "LIME";
            case "Grün" -> "GREEN";
            case "Hellblau" -> "LIGHT_BLUE";
            case "Blau" -> "BLUE";
            case "Lila" -> "PURPLE";
            case "Magenta" -> "MAGENTA";
            case "Grau" -> "GRAY";
            case "Schwarz" -> "BLACK";
            case "Weiß" -> "WHITE";
            case "Braun" -> "BROWN";
            case "Cyan" -> "CYAN";
            default -> "";
        };
    }

    private static void loadModsFromDirectory(File dir, int startIndex) {
        File[] modFiles = dir.listFiles((d, name) -> name.endsWith(".txt"));
        if (modFiles != null) {
            for (File modFile : modFiles) {
                try {
                    String modName = modFile.getName().replace(".txt", "");
                    boolean isActive = Boolean.parseBoolean(Files.readAllLines(modFile.toPath()).getFirst());
                    modCache.put(modName, isActive);

                    int lineNumber = startIndex + modToLineMap.size();
                    lineToModMap.put(lineNumber, modName);
                    modToLineMap.put(modName, lineNumber);
                } catch (IOException e) {
                    LOGGER.log(Level.SEVERE, "Fehler beim Laden der Mod: " + modFile.getName(), e);
                }
            }
        }
    }

    public static boolean searchMod(String modification) {
        // Versuche es zuerst aus dem Cache zu laden
        if (modCache.containsKey(modification)) {
            return modCache.get(modification);
        }

        // Wenn nicht im Cache, lade die Datei
        return searchModSync(modification);
    }

    private static boolean searchModSync(String modification) {
        if (modification == null || modification.trim().isEmpty()) {
            LOGGER.warning("Ungültiger Mod-Name: " + modification);
            return false;
        }

        File modDir = determineModDirectory(modification);
        File modFile = new File(modDir, modification + ".txt");

        if (!modFile.exists()) {
            LOGGER.info("Mod-Datei existiert nicht: " + modFile.getAbsolutePath());
            return false;
        }

        try {
            // Lese die gesamte Datei
            String content = Files.readString(modFile.toPath()).trim();

            // Prüfe auf leere Datei
            if (content.isEmpty()) {
                LOGGER.warning("Leere Mod-Datei: " + modFile.getName());
                return false;
            }

            // Teile in Zeilen auf und nimm die erste nicht-leere Zeile
            String[] lines = content.split("\\R", 2);
            String firstLine = lines[0].trim();

            // Prüfe auf gültigen Boolean-Wert
            if (firstLine.equalsIgnoreCase("true")) {
                modCache.put(modification, true);
                return true;
            } else if (firstLine.equalsIgnoreCase("false")) {
                modCache.put(modification, false);
                return false;
            } else {
                LOGGER.warning("Ungültiger Wert in Mod-Datei " + modFile.getName() + ": " + firstLine);
                return false;
            }

        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Fehler beim Lesen der Mod-Datei: " + modFile.getName(), e);
            return false;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Unerwarteter Fehler beim Lesen der Mod: " + modification, e);
            return false;
        }
    }

    public static void setMod(String modification, boolean activated) {
        // Aktualisiere den Cache
        modCache.put(modification, activated);

        // Bestimme das richtige Verzeichnis basierend auf dem Mod-Namen
        File modDir = determineModDirectory(modification);
        File modFile = new File(modDir, modification + ".txt");

        // Speichere die Änderung asynchron
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            try {
                // Erstelle das Verzeichnis, falls es nicht existiert
                if (!modDir.exists()) {
                    modDir.mkdirs();
                }

                // Erstelle die Datei mit dem neuen Wert in der ersten Zeile
                String content = String.valueOf(activated);
                Files.writeString(modFile.toPath(),
                        content);

            } catch (IOException e) {
                LOGGER.log(Level.SEVERE, "Fehler beim Speichern der Mod: " + modification, e);
            }
        });
    }
}
