package mc.cws.paintOff.Game.Arena;

import mc.cws.paintOff.Configuration;
import mc.cws.paintOff.Game.Management.Queue;
import mc.cws.paintOff.Game.Management.Scoreboards.Scoreboards;
import mc.cws.paintOff.Game.Management.Start;
import mc.cws.paintOff.PaintOffMain;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class ArenaAuswahl implements Listener {
    private static PaintOffMain plugin;
    public static void setPlugin(PaintOffMain plugin) {
        ArenaAuswahl.plugin = plugin;}

    private static final String AUSWAHL_TITLE = ChatColor.DARK_GRAY + "- Arenen -";
    public static final Inventory Auswahl = Bukkit.createInventory(null, 9, AUSWAHL_TITLE);
    public static int[][] vote = new int[Configuration.maxQueues][2]; // [queue][arenaIndex] where 0=arena1, 1=arena2
    public static final List<Player> hasVoted = new ArrayList<>();
    public static String[] arena1 = new String[Configuration.maxQueues];
    public static String[] arena2 = new String[Configuration.maxQueues];

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        try {
            if (!(event.getWhoClicked() instanceof Player player)) {
                plugin.getLogger().warning("Inventory click by non-player");
                return;
            }

            int clickedSlot = event.getRawSlot();
            int n = Start.getQueueNumber(player);

            // Check if the clicked inventory is our voting inventory
            if (!event.getView().getTitle().equals(AUSWAHL_TITLE)) {
                return;
            }
            
            event.setCancelled(true);
            
            int arenaCount = Arena.countArenas();
            if (arenaCount <= 1) {
                player.sendMessage(ChatColor.RED + "Nicht genügend Arenen verfügbar!");
                return;
            }
            
            // Check which item was clicked
            if (clickedSlot == 3) { // First map
                handleVote(player, n, 0);
            } else if (clickedSlot == 5) { // Second map
                handleVote(player, n, 1);
            } else {
                return;
            }
            
            // Update the voting display for all viewers
            for (HumanEntity viewer : event.getViewers()) {
                if (viewer instanceof Player) {
                    Player p = (Player) viewer;
                    p.updateInventory();
                }
            }
            
        } catch (Exception e) {
            plugin.getLogger().severe("Error in onInventoryClick: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private void handleVote(Player player, int queueNumber, int arenaIndex) {
        try {
            if (hasVoted.contains(player)) {
                player.sendMessage(ChatColor.RED + "PaintOff | " + ChatColor.GRAY + "Du hast bereits abgestimmt!");
                player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1.0f, 1.0f);
                return;
            }
            
            // Record the vote
            vote[queueNumber][arenaIndex]++;
            hasVoted.add(player);
            player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BELL, 0.5f, 2.0f);

            // Update the display
            Auswahl.setItem(3, createMap1(queueNumber));
            Auswahl.setItem(5, createMap2(queueNumber));

            player.sendMessage(ChatColor.GOLD + "PaintOff | " + ChatColor.GREEN + "Deine Stimme wurde gezählt!");
            if (arenaIndex == 0) {
                for (Player p : Queue.queuedPlayers.get(queueNumber)) {
                    p.spigot().sendMessage(net.md_5.bungee.api.ChatMessageType.ACTION_BAR,
                            new TextComponent(ChatColor.GOLD + " " + vote[queueNumber][arenaIndex] + ChatColor.GRAY + " Stimmen für " + ChatColor.GREEN + arena1[queueNumber]));
                }
            } else {
                for (Player p : Queue.queuedPlayers.get(queueNumber)) {
                    p.spigot().sendMessage(net.md_5.bungee.api.ChatMessageType.ACTION_BAR,
                            new TextComponent(ChatColor.GOLD + " " + vote[queueNumber][arenaIndex] + ChatColor.GRAY + " Stimmen für " + ChatColor.GREEN + arena2[queueNumber]));
                }
            }
            for (Player p : Queue.queuedPlayers.get(queueNumber)) {
                Scoreboards.updateScoreboardQueue(p);
            }
        } catch (Exception e) {
            plugin.getLogger().severe("Error in handleVote: " + e.getMessage());
            e.printStackTrace();
            player.sendMessage(ChatColor.RED + "Ein Fehler ist aufgetreten!");
        }
    }

    @EventHandler
    public void onInventoryOpen(InventoryOpenEvent event) {
        // Prüfe, ob es sich um das Arsenal-Inventar handelt
        int n = Start.getQueueNumber((Player) event.getPlayer());
        if (event.getView().getTitle().equals(AUSWAHL_TITLE)) {
            // Setze glas
            for (int i = 0; i < Auswahl.getSize(); i++) {
                ItemStack current = Auswahl.getItem(i);
                if (current == null || current.getType() != Material.GRAY_STAINED_GLASS_PANE) {
                    Auswahl.setItem(i, createGlass());
                }
            }
            // Setze die Items zurück
            Auswahl.setItem(3, createMap1(n));
            Auswahl.setItem(5, createMap2(n));
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
    private ItemStack createMap1(int n) {
        ItemStack paintball = new ItemStack(Material.MAP, 1);
        ItemMeta meta = paintball.getItemMeta();
        meta.setDisplayName(ChatColor.YELLOW + arena1[n]);
        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.GRAY + "Für diese map voten?");
        lore.add("");
        lore.add(ChatColor.DARK_PURPLE + "Votes: " + ChatColor.GOLD + vote[n][0]);
        meta.setLore(lore);
        paintball.setItemMeta(meta);
        return paintball;
    }
    private ItemStack createMap2(int n) {
        ItemStack paintball = new ItemStack(Material.MAP, 1);
        ItemMeta meta = paintball.getItemMeta();
        meta.setDisplayName(ChatColor.YELLOW + arena2[n]);
        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.GRAY + "Für diese map voten?");
        lore.add("");
        lore.add(ChatColor.DARK_PURPLE + "Votes: " + ChatColor.GOLD + vote[n][1]); // Vote count for arena2
        meta.setLore(lore);
        paintball.setItemMeta(meta);
        return paintball;
    }

    public static void getTwoRandomArenas(int n) {
        int arenaCount = Arena.countArenas();
        if (arenaCount < 2) {
            plugin.getLogger().info("Nicht genug arenas! mindestens 2 Arenen müssen vorhanden sein!");
            return;
        }
        arena1[n] = Arena.randomArena();
        arena2[n] = Arena.randomArena();
        for (int i = 0; i < 1; i++) {
            if (arena1[n].equals(arena2[n])) {
                arena2[n] = Arena.randomArena();
            }
            if (arena1[n].equals(arena2[n])) {
                i--;
            }
        }
    }

    public static String chosenArena(int n) {
        if (vote[n][0] > vote[n][1]) {
            return arena1[n];
        } else if (vote[n][0] == vote[n][1]) {
            return Arena.randomArena();
        } else {
            return arena2[n];
        }
    }
}
