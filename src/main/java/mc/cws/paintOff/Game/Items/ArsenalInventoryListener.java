package mc.cws.paintOff.Game.Items;

import mc.cws.paintOff.Configuration;
import mc.cws.paintOff.Game.Items.Primarys.None.Abwandlung.FiftySnipEx;
import mc.cws.paintOff.Game.Items.Primarys.None.Abwandlung.Pikolat;
import mc.cws.paintOff.Game.Items.Primarys.Haepec.HaeSchnipsLehr;
import mc.cws.paintOff.Game.Items.Primarys.Prime.PrimeMillilat;
import mc.cws.paintOff.Game.Items.Primarys.Prime.PrimeSchweddler;
import mc.cws.paintOff.Game.Items.Primarys.Pyrex.PyrTwentySniplEx;
import mc.cws.paintOff.Game.Items.Primarys.None.*;
import mc.cws.paintOff.Game.Items.Primarys.Pyrex.PyrBlubber;
import mc.cws.paintOff.Game.Items.Primarys.Tulipa.TulQuinter;
import mc.cws.paintOff.Game.Items.Primarys.Tulipa.TulTriAtler;
import mc.cws.paintOff.Game.Items.Secondarys.*;
import mc.cws.paintOff.Game.Items.Ultimates.*;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ArsenalInventoryListener implements Listener {
    private static final String ARSENAL_TITLE = ChatColor.DARK_GRAY + "- Del'Finn Arsenal -";
    public static final Inventory ARSENAL = Bukkit.createInventory(null, 54, ARSENAL_TITLE);
    private static final String ARSENAL_TITLE2 = ChatColor.DARK_GRAY + "- Del'Finn Arsenal - ";
    public static final Inventory ARSENAL2 = Bukkit.createInventory(null, 54, ARSENAL_TITLE2);

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getView().getTitle().equals(ARSENAL_TITLE)) {
            event.setCancelled(true);

            if (event.getWhoClicked() instanceof Player player) {

                // Prüfe, welches Item angeklickt wurde
                int clickedSlot = event.getRawSlot();
                if (clickedSlot == SchnipsLehr.slot) { // SchnipsLehr
                    setKitNumber(player, 0);
                    player.closeInventory();
                    player.playSound(player.getLocation(), Sound.ENTITY_DOLPHIN_SPLASH, 0.25f, 3.0f);
                    player.playSound(player.getLocation(), Sound.ENTITY_DOLPHIN_AMBIENT, 0.5f, 1.0f);
                    player.sendMessage(ChatColor.GOLD + Configuration.name + " | " + ChatColor.GRAY + "Dein Arsenal wurde auf" + ChatColor.YELLOW + " Schnips-Leer " + ChatColor.GRAY + " aktualisiert!");
                } else if (clickedSlot == TriAtler.slot) { // Placeholder
                    setKitNumber(player, 1);
                    player.closeInventory();
                    player.playSound(player.getLocation(), Sound.ENTITY_DOLPHIN_SPLASH, 0.25f, 3.0f);
                    player.playSound(player.getLocation(), Sound.ENTITY_DOLPHIN_AMBIENT, 0.5f, 1.0f);
                    player.sendMessage(ChatColor.GOLD + Configuration.name + " | " + ChatColor.GRAY + "Dein Arsenal wurde auf" + ChatColor.YELLOW + " Tri-Atler " + ChatColor.GRAY + "aktualisiert!");
                } else if (clickedSlot == Quinter.slot) { // Placeholder
                    setKitNumber(player, 2);
                    player.closeInventory();
                    player.playSound(player.getLocation(), Sound.ENTITY_DOLPHIN_SPLASH, 0.25f, 3.0f);
                    player.playSound(player.getLocation(), Sound.ENTITY_DOLPHIN_AMBIENT, 0.5f, 1.0f);
                    player.sendMessage(ChatColor.GOLD + Configuration.name + " | " + ChatColor.GRAY + "Dein Arsenal wurde auf" + ChatColor.YELLOW + " Quinter " + ChatColor.GRAY + "aktualisiert!");
                } else if (clickedSlot == TulTriAtler.slot) { // Placeholder
                    setKitNumber(player, 3);
                    player.closeInventory();
                    player.playSound(player.getLocation(), Sound.ENTITY_DOLPHIN_SPLASH, 0.25f, 3.0f);
                    player.playSound(player.getLocation(), Sound.ENTITY_DOLPHIN_AMBIENT, 0.5f, 1.0f);
                    player.sendMessage(ChatColor.GOLD + Configuration.name + " | " + ChatColor.GRAY + "Dein Arsenal wurde auf" + ChatColor.YELLOW + " Tulipa Tri-Atler " + ChatColor.GRAY + "aktualisiert!");
                } else if (clickedSlot == Schweddler.slot) { // Placeholder
                    setKitNumber(player, 4);
                    player.closeInventory();
                    player.playSound(player.getLocation(), Sound.ENTITY_DOLPHIN_SPLASH, 0.25f, 3.0f);
                    player.playSound(player.getLocation(), Sound.ENTITY_DOLPHIN_AMBIENT, 0.5f, 1.0f);
                    player.sendMessage(ChatColor.GOLD + Configuration.name + " | " + ChatColor.GRAY + "Dein Arsenal wurde auf" + ChatColor.YELLOW + " Schweddler " + ChatColor.GRAY + "aktualisiert!");
                } else if (clickedSlot == SniplEx.slot) { // Placeholder
                    setKitNumber(player, 5);
                    player.closeInventory();
                    player.playSound(player.getLocation(), Sound.ENTITY_DOLPHIN_SPLASH, 0.25f, 3.0f);
                    player.playSound(player.getLocation(), Sound.ENTITY_DOLPHIN_AMBIENT, 0.5f, 1.0f);
                    player.sendMessage(ChatColor.GOLD + Configuration.name + " | " + ChatColor.GRAY + "Dein Arsenal wurde auf" + ChatColor.YELLOW + " 25er-SniplEx " + ChatColor.GRAY + "aktualisiert!");
                } else if (clickedSlot == Blubber.slot) { // Placeholder
                    setKitNumber(player, 6);
                    player.closeInventory();
                    player.playSound(player.getLocation(), Sound.ENTITY_DOLPHIN_SPLASH, 0.25f, 3.0f);
                    player.playSound(player.getLocation(), Sound.ENTITY_DOLPHIN_AMBIENT, 0.5f, 1.0f);
                    player.sendMessage(ChatColor.GOLD + Configuration.name + " | " + ChatColor.GRAY + "Dein Arsenal wurde auf" + ChatColor.YELLOW + " Blubber " + ChatColor.GRAY + "aktualisiert!");
                } else if (clickedSlot == FiftySnipEx.slot) { // Placeholder
                    setKitNumber(player, 7);
                    player.closeInventory();
                    player.playSound(player.getLocation(), Sound.ENTITY_DOLPHIN_SPLASH, 0.25f, 3.0f);
                    player.playSound(player.getLocation(), Sound.ENTITY_DOLPHIN_AMBIENT, 0.5f, 1.0f);
                    player.sendMessage(ChatColor.GOLD + Configuration.name + " | " + ChatColor.GRAY + "Dein Arsenal wurde auf" + ChatColor.YELLOW + " 50er-SniplEx " + ChatColor.GRAY + "aktualisiert!");
                } else if (clickedSlot == PrimeSchweddler.slot) { // Placeholder
                    setKitNumber(player, 8);
                    player.closeInventory();
                    player.playSound(player.getLocation(), Sound.ENTITY_DOLPHIN_SPLASH, 0.25f, 3.0f);
                    player.playSound(player.getLocation(), Sound.ENTITY_DOLPHIN_AMBIENT, 0.5f, 1.0f);
                    player.sendMessage(ChatColor.GOLD + Configuration.name + " | " + ChatColor.GRAY + "Dein Arsenal wurde auf" + ChatColor.YELLOW + " Prime Schweddler " + ChatColor.GRAY + "aktualisiert!");
                } else if (clickedSlot == Mikrolat.slot) { // Placeholder
                    setKitNumber(player, 9);
                    player.closeInventory();
                    player.playSound(player.getLocation(), Sound.ENTITY_DOLPHIN_SPLASH, 0.25f, 3.0f);
                    player.playSound(player.getLocation(), Sound.ENTITY_DOLPHIN_AMBIENT, 0.5f, 1.0f);
                    player.sendMessage(ChatColor.GOLD + Configuration.name + " | " + ChatColor.GRAY + "Dein Arsenal wurde auf" + ChatColor.YELLOW + " Mikrolat " + ChatColor.GRAY + "aktualisiert!");
                } else if (clickedSlot == TulQuinter.slot) { // Placeholder
                    setKitNumber(player, 10);
                    player.closeInventory();
                    player.playSound(player.getLocation(), Sound.ENTITY_DOLPHIN_SPLASH, 0.25f, 3.0f);
                    player.playSound(player.getLocation(), Sound.ENTITY_DOLPHIN_AMBIENT, 0.5f, 1.0f);
                    player.sendMessage(ChatColor.GOLD + Configuration.name + " | " + ChatColor.GRAY + "Dein Arsenal wurde auf" + ChatColor.YELLOW + " Tulipa Quinter " + ChatColor.GRAY + "aktualisiert!");
                } else if (clickedSlot == Pikolat.slot) { // Placeholder
                    setKitNumber(player, 11);
                    player.closeInventory();
                    player.playSound(player.getLocation(), Sound.ENTITY_DOLPHIN_SPLASH, 0.25f, 3.0f);
                    player.playSound(player.getLocation(), Sound.ENTITY_DOLPHIN_AMBIENT, 0.5f, 1.0f);
                    player.sendMessage(ChatColor.GOLD + Configuration.name + " | " + ChatColor.GRAY + "Dein Arsenal wurde auf" + ChatColor.YELLOW + " Pikolat " + ChatColor.GRAY + "aktualisiert!");
                }
                else if (clickedSlot == PyrBlubber.slot && Configuration.testbuild) { // Placeholder
                    setKitNumber(player, 12);
                    player.closeInventory();
                    player.playSound(player.getLocation(), Sound.ENTITY_DOLPHIN_SPLASH, 0.25f, 3.0f);
                    player.playSound(player.getLocation(), Sound.ENTITY_DOLPHIN_AMBIENT, 0.5f, 1.0f);
                    player.sendMessage(ChatColor.GOLD + Configuration.name + " | " + ChatColor.GRAY + "Dein Arsenal wurde auf" + ChatColor.YELLOW + " Pyrex Blubber " + ChatColor.GRAY + "aktualisiert!");
                }
                else if (clickedSlot == HaeSchnipsLehr.slot && Configuration.testbuild) { // Placeholder
                    setKitNumber(player, 13);
                    player.closeInventory();
                    player.playSound(player.getLocation(), Sound.ENTITY_DOLPHIN_SPLASH, 0.25f, 3.0f);
                    player.playSound(player.getLocation(), Sound.ENTITY_DOLPHIN_AMBIENT, 0.5f, 1.0f);
                    player.sendMessage(ChatColor.GOLD + Configuration.name + " | " + ChatColor.GRAY + "Dein Arsenal wurde auf" + ChatColor.YELLOW + " Haepec Schnips-Leer " + ChatColor.GRAY + "aktualisiert!");
                }
                else if (clickedSlot == PyrTwentySniplEx.slot && Configuration.testbuild) { // Placeholder
                    setKitNumber(player, 14);
                    player.closeInventory();
                    player.playSound(player.getLocation(), Sound.ENTITY_DOLPHIN_SPLASH, 0.25f, 3.0f);
                    player.playSound(player.getLocation(), Sound.ENTITY_DOLPHIN_AMBIENT, 0.5f, 1.0f);
                    player.sendMessage(ChatColor.GOLD + Configuration.name + " | " + ChatColor.GRAY + "Dein Arsenal wurde auf" + ChatColor.YELLOW + " Pyrex 25er-SniplEx " + ChatColor.GRAY + "aktualisiert!");
                }
                else if (clickedSlot == PrimeMillilat.slot && Configuration.testbuild) {
                    setKitNumber(player, 15);
                    player.closeInventory();
                    player.playSound(player.getLocation(), Sound.ENTITY_DOLPHIN_SPLASH, 0.25f, 3.0f);
                    player.playSound(player.getLocation(), Sound.ENTITY_DOLPHIN_AMBIENT, 0.5f, 1.0f);
                    player.sendMessage(ChatColor.GOLD + Configuration.name + " | " + ChatColor.GRAY + "Dein Arsenal wurde auf" + ChatColor.YELLOW + " Prime Mikrolat " + ChatColor.GRAY + "aktualisiert!");
                }

                else if (clickedSlot == 49) {
                    Random random = new Random();
                    if (Configuration.testbuild) {
                        Configuration.unreleasedWeapons = 0;
                    }
                    int randomKitNumber = random.nextInt(Configuration.maxWaffen-Configuration.unreleasedWeapons) + 1; // Generates a number between 1 and 11
                    setKitNumber(player, randomKitNumber);
                    player.closeInventory();
                    player.playSound(player.getLocation(), Sound.ENTITY_DOLPHIN_SPLASH, 0.25f, 3.0f);
                    player.playSound(player.getLocation(), Sound.ENTITY_DOLPHIN_AMBIENT, 0.5f, 1.0f);
                    player.sendMessage(ChatColor.GOLD + Configuration.name + " | " + ChatColor.GRAY + "Dein Arsenal wurde auf ein zufälliges Kit gesetzt!");
                } else if (clickedSlot == 53 && Configuration.testbuild) {
                    setKitNumber(player, Configuration.maxWaffen + 1);
                    player.closeInventory();
                    player.playSound(player.getLocation(), Sound.ENTITY_DOLPHIN_SPLASH, 0.25f, 3.0f);
                    player.playSound(player.getLocation(), Sound.ENTITY_DOLPHIN_AMBIENT, 0.5f, 1.0f);
                    player.sendMessage(ChatColor.GOLD + Configuration.name + " | " + ChatColor.GRAY + "Dein Arsenal wurde auf Testwaffe gesetzt!");
                } else if (clickedSlot == 50 && Configuration.testbuild) {
                    player.openInventory(ARSENAL2);
                }
            }
        }
        if (event.getView().getTitle().equals(ARSENAL_TITLE2)) {
            event.setCancelled(true);

            if (event.getWhoClicked() instanceof Player player) {

                // Prüfe, welches Item angeklickt wurde
                int clickedSlot = event.getRawSlot();
                if (clickedSlot == Musket.slot) { // SchnipsLehr
                    setKitNumber(player, 16);
                    player.closeInventory();
                    player.playSound(player.getLocation(), Sound.ENTITY_DOLPHIN_SPLASH, 0.25f, 3.0f);
                    player.playSound(player.getLocation(), Sound.ENTITY_DOLPHIN_AMBIENT, 0.5f, 1.0f);
                    player.sendMessage(ChatColor.GOLD + Configuration.name + " | " + ChatColor.GRAY + "Dein Arsenal wurde auf" + ChatColor.YELLOW + " Musket " + ChatColor.GRAY + " aktualisiert!");
                } else if (clickedSlot == 49) {
                    Random random = new Random();
                    if (Configuration.testbuild) {
                        Configuration.unreleasedWeapons = 0;
                    }
                    int randomKitNumber = random.nextInt(Configuration.maxWaffen - Configuration.unreleasedWeapons) + 1; // Generates a number between 1 and 11
                    setKitNumber(player, randomKitNumber);
                    player.closeInventory();
                    player.playSound(player.getLocation(), Sound.ENTITY_DOLPHIN_SPLASH, 0.25f, 3.0f);
                    player.playSound(player.getLocation(), Sound.ENTITY_DOLPHIN_AMBIENT, 0.5f, 1.0f);
                    player.sendMessage(ChatColor.GOLD + Configuration.name + " | " + ChatColor.GRAY + "Dein Arsenal wurde auf ein zufälliges Kit gesetzt!");
                } else if (clickedSlot == 48) {
                    player.openInventory(ARSENAL);
                }
            }
        }
    }

    @EventHandler
    public void onInventoryOpen(InventoryOpenEvent event) {
        // Prüfe, ob es sich um das Arsenal-Inventar handelt
        if (event.getView().getTitle().equals(ARSENAL_TITLE)) {
            // Setze glas
            for (int i = 0; i < ARSENAL.getSize(); i++) {
                ItemStack current = ARSENAL.getItem(i);
                if (current == null || current.getType() != Material.GRAY_STAINED_GLASS_PANE) {
                    ARSENAL.setItem(i, createGlass());
                }
            }
            // Setze die Items zurück
            ARSENAL.setItem(SchnipsLehr.slot, SchnipsLehr.arsenalDisplay());
            ARSENAL.setItem(TriAtler.slot, TriAtler.arsenalDisplay());
            ARSENAL.setItem(Quinter.slot, Quinter.arsenalDisplay());
            ARSENAL.setItem(TulTriAtler.slot, TulTriAtler.arsenalDisplay());
            ARSENAL.setItem(Schweddler.slot, Schweddler.arsenalDisplay());
            ARSENAL.setItem(SniplEx.slot, SniplEx.arsenalDisplay());
            ARSENAL.setItem(Blubber.slot, Blubber.arsenalDisplay());
            ARSENAL.setItem(FiftySnipEx.slot, FiftySnipEx.arsenalDisplay());
            ARSENAL.setItem(PrimeSchweddler.slot, PrimeSchweddler.arsenalDisplay());
            ARSENAL.setItem(Mikrolat.slot, Mikrolat.arsenalDisplay());
            ARSENAL.setItem(TulQuinter.slot, TulQuinter.arsenalDisplay());
            ARSENAL.setItem(Pikolat.slot, Pikolat.arsenalDisplay());
             if (Configuration.testbuild) {
                 ARSENAL.setItem(PyrBlubber.slot, PyrBlubber.arsenalDisplay());
                 ARSENAL.setItem(HaeSchnipsLehr.slot, HaeSchnipsLehr.arsenalDisplay());
                 ARSENAL.setItem(PyrTwentySniplEx.slot, PyrTwentySniplEx.arsenalDisplay());
                 ARSENAL.setItem(PrimeMillilat.slot, PrimeMillilat.arsenalDisplay());
             }
            ARSENAL.setItem(49, createRandom());
             if (Configuration.testbuild) {
                 ARSENAL.setItem(50, createNextSite(false));
             }
            if (Configuration.testbuild) {ARSENAL.setItem(53, createTesting());}
        }
        if (event.getView().getTitle().equals(ARSENAL_TITLE2)) {
            // Setze glas
            for (int i = 0; i < ARSENAL2.getSize(); i++) {
                ItemStack current = ARSENAL2.getItem(i);
                if (current == null || current.getType() != Material.GRAY_STAINED_GLASS_PANE) {
                    ARSENAL2.setItem(i, createGlass());
                }
            }
            // Setze die Items zurück
            if (Configuration.testbuild) {
                ARSENAL2.setItem(Musket.slot,Musket.arsenalDisplay());
            }
            ARSENAL2.setItem(49, createRandom());
            ARSENAL2.setItem(48, createNextSite(true));
        }
    }
    private ItemStack createGlass() {
        ItemStack glass = new ItemStack(Material.GRAY_STAINED_GLASS_PANE, 1);
        ItemMeta meta = glass.getItemMeta();
        assert meta != null;
        meta.setDisplayName("§a");
        glass.setItemMeta(meta);
        return glass;
    }


    // ---------------------------------------------------------------------------------------------------------

    private void setKitNumber(Player player, int kitNumber) {
        // 1. Arsenal-Ordner erstellen
        File dataFolder = new File(Configuration.mainCommand + "-lobby/Arsenal");
        if (!dataFolder.exists() && !dataFolder.mkdirs()) {
            player.sendMessage(ChatColor.RED + Configuration.name + " | " +
                    ChatColor.GRAY + "Konnte Arsenal-Ordner nicht erstellen: " + dataFolder.getAbsolutePath());
            return;
        }

        // 2. Spielerspezifische Datei erstellen
        File playerFile = new File(dataFolder, player.getName() + ".dat");
        File tempFile = new File(dataFolder, player.getName() + ".tmp");

        try {
            // 3. In temporäre Datei schreiben
            try (BufferedWriter writer = new BufferedWriter(
                    new FileWriter(tempFile, false))) {
                writer.write(String.valueOf(kitNumber));
            }

            // 4. Originaldatei ersetzen
            if (playerFile.exists() && !playerFile.delete()) {
                throw new IOException("Konnte Spielerdatei nicht löschen: " + playerFile.getAbsolutePath());
            }

            if (!tempFile.renameTo(playerFile)) {
                throw new IOException("Konnte temporäre Datei nicht umbenennen: " +
                        tempFile.getAbsolutePath() + " -> " + playerFile.getAbsolutePath());
            }

            System.out.println("[DEBUG] Kit für " + player.getName() + " erfolgreich gespeichert");

        } catch (IOException e) {
            System.err.println("[ERROR] Fehler beim Speichern des Kits: " + e.getMessage());
            e.printStackTrace();
            player.sendMessage(ChatColor.RED + Configuration.name + " | " +
                    ChatColor.GRAY + "Fehler beim Speichern des Kits: " + e.getMessage());
        } finally {
            // 5. Aufräumen
            if (tempFile.exists()) {
                tempFile.delete();
            }
        }
    }

    public static int getKitNumber(Player player) {
        // 1. Arsenal-Ordner überprüfen
        File dataFolder = new File(Configuration.mainCommand + "-lobby/Arsenal");
        if (!dataFolder.exists() || !dataFolder.isDirectory()) {
            return 0; // Standardwert, wenn der Ordner nicht existiert
        }

        // 2. Spielerspezifische Datei suchen
        File playerFile = new File(dataFolder, player.getName() + ".dat");
        if (!playerFile.exists() || playerFile.length() == 0) {
            return 0; // Standardwert, wenn die Datei nicht existiert oder leer ist
        }

        try {
            // 3. Kit-Nummer aus der Datei lesen
            String content = new String(Files.readAllBytes(playerFile.toPath()), StandardCharsets.UTF_8).trim();
            return Integer.parseInt(content);
        } catch (Exception e) {
            System.err.println("[ERROR] Fehler beim Lesen der Kit-Nummer für " + player.getName() + ": " + e.getMessage());
            return 0; // Standardwert bei Fehlern
        }
    }

    // ---------------------------------------------------------------------------------------------------------

    private ItemStack createNextSite(boolean back) {
        ItemStack paintball = new ItemStack(Material.ARROW, 1);
        ItemMeta meta = paintball.getItemMeta();
        if (back) {
            meta.setDisplayName(ChatColor.YELLOW + "Letzte Seite");
        } else {
            meta.setDisplayName(ChatColor.YELLOW + "Nächste Seite");
        }
        paintball.setItemMeta(meta);
        return paintball;
    }

    private ItemStack createRandom() {
        ItemStack paintball = new ItemStack(Material.PUFFERFISH_BUCKET, 1);
        ItemMeta meta = paintball.getItemMeta();
        meta.setDisplayName(ChatColor.YELLOW + "Zufallswaffe");
        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.GRAY + "Lass dich Überraschen!");
        lore.add("");
        lore.add(ChatColor.YELLOW + "Sekundärwaffe: " + ChatColor.LIGHT_PURPLE + "???");
        lore.add(ChatColor.YELLOW + "Spezialwaffe: " + ChatColor.LIGHT_PURPLE + "???");
        meta.setLore(lore);
        paintball.setItemMeta(meta);
        return paintball;
    }
    private static ItemStack createTesting() {
        ItemStack paintball = new ItemStack(Material.STICK, 1);
        ItemMeta meta = paintball.getItemMeta();
        meta.setDisplayName(ChatColor.YELLOW + "Testwaffe");
        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.GRAY + "Dies ist eine Testwaffe!");
        lore.add("");
        meta.setLore(lore);
        paintball.setItemMeta(meta);
        return paintball;
    }

    public static void getWeapons(Player player, Inventory inventory, int slot) {
        int n = getKitNumber(player);
        if (n == 0) {
            inventory.setItem(slot, SchnipsLehr.arsenalDisplay());
        } else if (n == 1) {
            inventory.setItem(slot, TriAtler.arsenalDisplay());
        } else if (n == 2) {
            inventory.setItem(slot, Quinter.arsenalDisplay());
        } else if (n == 3) {
            inventory.setItem(slot, TulTriAtler.arsenalDisplay());
        } else if (n == 4) {
            inventory.setItem(slot, Schweddler.arsenalDisplay());
        } else if (n == 5) {
            inventory.setItem(slot, SniplEx.arsenalDisplay());
        } else if (n == 6) {
            inventory.setItem(slot, Blubber.arsenalDisplay());
        } else if (n == 7) {
            inventory.setItem(slot, FiftySnipEx.arsenalDisplay());
        } else if (n == 8) {
            inventory.setItem(slot, PrimeSchweddler.arsenalDisplay());
        } else if (n == 9) {
            inventory.setItem(slot, Pikolat.arsenalDisplay());
        } else if (n == 10) {
            inventory.setItem(slot, TulQuinter.arsenalDisplay());
        } else if (n == 11) {
            inventory.setItem(slot, Pikolat.arsenalDisplay());
        } else if (n == 12) {
            inventory.setItem(slot, PyrBlubber.arsenalDisplay());
        } else if (n == 13) {
            inventory.setItem(slot, HaeSchnipsLehr.arsenalDisplay());
        } else if (n == 14) {
            inventory.setItem(slot, PyrTwentySniplEx.arsenalDisplay());
        } else if (n == 15) {
            inventory.setItem(slot, PrimeMillilat.arsenalDisplay());
        } else if (n == 16) {
            inventory.setItem(slot, Musket.arsenalDisplay());
        } else if (n == Configuration.maxWaffen+1) {
            inventory.setItem(slot, createTesting());
        }
        else {
            inventory.setItem(slot, SchnipsLehr.arsenalDisplay());
        }
    }

    public static void getArsenal(Player player) {
        int n = getKitNumber(player);
        TeleportPad.getItem(player);
        if (n == 0) {
            SchnipsLehr.getItem(player);
            Marker.getItem(player);
            Tornedo.getItem(player);
        } else if (n == 1) {
            TriAtler.getItem(player);
            Marker.getItem(player);
            GenetischerBoost.getItem(player);
        } else if (n == 2) {
            Quinter.getItem(player);
            Vernebler.getItem(player);
            Wellenschlag.getItem(player);
        } else if (n == 3) {
            TulTriAtler.getItem(player);
            Aufdecker.getItem(player);
            Wellenschlag.getItem(player);
        } else if (n == 4) {
            Schweddler.getItem(player);
            Klotzbombe.getItem(player);
            Sonnenwindler.getItem(player);
        } else if (n == 5) {
            SniplEx.getItem(player);
            Kreuzer.getItem(player);
            Klotzhagel.getItem(player);
        } else if (n == 6) {
            Blubber.getItem(player);
            Vernebler.getItem(player);
            PlatzRegen.getItem(player);
        } else if (n == 7) {
            FiftySnipEx.getItem(player);
            Aufdecker.getItem(player);
            Eruptor.getItem(player);
        } else if (n == 8) {
            PrimeSchweddler.getItem(player);
            Reihenzieher.getItem(player);
            Tornedo.getItem(player);
        } else if (n == 9) {
            Mikrolat.getItem(player);
            Aufdecker.getItem(player);
            Eruptor.getItem(player);
        } else if (n == 10) {
            TulQuinter.getItem(player);
            Klotzbombe.getItem(player);
            PlatzRegen.getItem(player);
        } else if (n == 11) {
            Pikolat.getItem(player);
            Kreuzer.getItem(player);
            Tornedo.getItem(player);
        } else if (!Configuration.testbuild) { // Stopper
            SchnipsLehr.getItem(player);
            Marker.getItem(player);
            Tornedo.getItem(player);
        }
        else if (n == 12) {
            PyrBlubber.getItem(player);
            Vernebler.getItem(player);
            Gammablitzer.getItem(player);
        } else if (n == 13) {
            HaeSchnipsLehr.getItem(player);
            Amalgamalge.getItem(player);
            GenetischerBoost.getItem(player);
        } else if (n == 14) {
            PyrTwentySniplEx.getItem(player);
            Reihenzieher.getItem(player);
            GlitzerMeteor.getItem(player);
        } else if (n == 15) {
            PrimeMillilat.getItem(player);
            Dreifarber.getItem(player);
            Sonnenwindler.getItem(player);
        } else if (n == 16) {
            Musket.getItem(player);
            Vernebler.getItem(player);
            Gammablitzer.getItem(player);
        } else if (n == Configuration.maxWaffen+1) {
            Testing.getItem(player);
            Dreifarber.getItem(player);
            Lauffeuer.getItem(player);
        } else {
            SchnipsLehr.getItem(player);
            Marker.getItem(player);
            Tornedo.getItem(player);
            player.sendMessage(ChatColor.GOLD + "PaintOff | " + ChatColor.GRAY + "Dein Arsenal ist Standard! /poarsenal um dein Arsenal zu aktualisieren!");
        }
    }
}