package mc.cws.paintOff.Po;

import mc.cws.paintOff.Configuration;
import mc.cws.paintOff.Game.Arena.Arena;
import mc.cws.paintOff.Game.Extras.Colorer;
import mc.cws.paintOff.Game.Items.ArsenalInventoryListener;
import mc.cws.paintOff.Game.Management.InGame.Game;
import mc.cws.paintOff.Game.Management.Queue;
import mc.cws.paintOff.Game.Management.Start;
import mc.cws.paintOff.Game.Management.Verteiler;
import mc.cws.paintOff.PaintOffMain;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.*;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.*;

public class TeleportInventoryListener implements Listener {
    private static PaintOffMain plugin;
    public static void setPlugin(PaintOffMain plugin) {TeleportInventoryListener.plugin = plugin;}
    public static final String ARSENAL_TITLE = ChatColor.GOLD + "" + ChatColor.BOLD + "- Teleport -";
    public static List<Player> currentPlayers = new ArrayList<>();
    public static List<Player> vorhanden = new ArrayList<>();
    public static Map<Integer, List<Player>> kitChange = new HashMap<>(); // Kit number + player
    public static List<Player> caster = new ArrayList<>();

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getView().getTitle().equals(ARSENAL_TITLE)) {
            event.setCancelled(true); // Prevent item pickup

            if (event.getWhoClicked() instanceof Player player) {
                int n = Start.getQueueNumber(player);
                int clickedSlot = event.getRawSlot();

                // Get team members
                List<Player> currentPlayers = new ArrayList<>();
                if (Verteiler.teamA.get(n).contains(player)) {
                    currentPlayers.addAll(Verteiler.teamA.get(n));
                    currentPlayers.remove(player);
                } else if (Verteiler.teamB.get(n).contains(player)) {
                    currentPlayers.addAll(Verteiler.teamB.get(n));
                    currentPlayers.remove(player);
                }

                // Handle teleportation
                if (clickedSlot == 7) { // Spawn
                    player.sendMessage(ChatColor.GOLD + Configuration.name + " | " + ChatColor.GRAY + "Teleportiere zum Spawn...");
                    player.playSound(player.getLocation(), Sound.BLOCK_PORTAL_AMBIENT, 0.5f, 1.0f);
                    player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 20 * 10, 0, false, false));
                    player.addPotionEffect(new PotionEffect(PotionEffectType.SLOWNESS, 20 * 10, 4, false, false));
                    player.closeInventory();
                    Game.cantUse.add(player);
                    Game.cantSneak.add(player);
                    Bukkit.getScheduler().runTaskLater(plugin, () -> {
                        Arena.portToArena(player, n);
                        Game.cantUse.remove(player);
                        Game.cantSneak.remove(player);
                        player.removePotionEffect(PotionEffectType.SLOWNESS);
                        player.removePotionEffect(PotionEffectType.BLINDNESS);
                        player.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 0.5f, 1.0f);
                        player.getWorld().spawnParticle(Particle.PORTAL, player.getLocation(), 1);
                        player.closeInventory();
                    }, 20L * Configuration.teleportTime);

                } else if (clickedSlot >= 1 && clickedSlot <= (Queue.maxSize/2)) { // Team members
                    int playerIndex = clickedSlot - 1;
                    if (playerIndex < currentPlayers.size()) {
                        Player target = currentPlayers.get(playerIndex);
                        player.sendMessage(ChatColor.GOLD + Configuration.name + " | " + ChatColor.GRAY + "Teleportiere zu " + ChatColor.YELLOW + target.getName() + "...");
                        player.playSound(player.getLocation(), Sound.BLOCK_PORTAL_AMBIENT, 0.5f, 1.0f);
                        player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 20 * 10, 0, false, false));
                        player.addPotionEffect(new PotionEffect(PotionEffectType.SLOWNESS, 20 * 10, 4, false, false));
                        player.closeInventory();
                        Game.cantUse.add(player);
                        Game.cantSneak.add(player);
                        target.playSound(target.getLocation(), Sound.BLOCK_NOTE_BLOCK_BELL, 0.2f, 1.0f);
                        String color = null;
                        if (Verteiler.teamA.get(n).contains(target)) {
                            color = Verteiler.colorA[n];
                        } else if (Verteiler.teamB.get(n).contains(target)) {
                            color = Verteiler.colorB[n];
                        }
                    target.spigot().sendMessage(net.md_5.bungee.api.ChatMessageType.ACTION_BAR, new TextComponent(color + player.getName() + ChatColor.GRAY + " ---> " + color + target.getName()));
                        Bukkit.getScheduler().runTaskLater(plugin, () -> {
                            player.teleport(target);
                            Game.cantUse.remove(player);
                            Game.cantSneak.remove(player);
                            player.removePotionEffect(PotionEffectType.SLOWNESS);
                            player.removePotionEffect(PotionEffectType.BLINDNESS);
                            player.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 0.5f, 1.0f);
                            player.getWorld().spawnParticle(Particle.PORTAL, player.getLocation(), 1);
                            player.closeInventory();
                        }, 20L * Configuration.teleportTime);
                    }
                } else if (clickedSlot == 8) {
                    int kitNumber = ArsenalInventoryListener.getKitNumber(player);
                    // Sicherstellen, dass die Liste für diese Kit-Nummer existiert
                    kitChange.computeIfAbsent(kitNumber, k -> new ArrayList<>()).add(player);

                    player.closeInventory();
                    // Kleine Verzögerung, um sicherzustellen, dass das vorherige Inventar geschlossen ist
                    player.openInventory(ArsenalInventoryListener.ARSENAL);
                    event.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    public void onInventoryOpen(InventoryOpenEvent event) {
        HumanEntity player = event.getPlayer();
        int n = Start.getQueueNumber((Player) player);

        // Clear existing lists
        currentPlayers.clear();
        vorhanden.clear();
        caster.clear();

        // Add team members
        if (Verteiler.teamA.containsKey(n)) {
            List<Player> teamA = Verteiler.teamA.get(n);
            if (teamA != null && teamA.contains(player)) {
                currentPlayers.addAll(teamA);
                currentPlayers.remove(player);
            }
        } else if (Verteiler.teamB.containsKey(n)) {
            List<Player> teamB = Verteiler.teamB.get(n);
            if (teamB != null && teamB.contains(player)) {
                currentPlayers.addAll(teamB);
                currentPlayers.remove(player);
            }
        }

        // Check if this is our inventory
        if (event.getView().getTitle().equals(ARSENAL_TITLE)) {
            // Get the inventory from the event
            Inventory inventory = event.getInventory();

            // Add spawn item
            ItemStack spawn = getSpawn((Player) player);
            inventory.setItem(7, spawn);

            // Add player heads
            int slot = 1;
            for (Player teamMember : currentPlayers) {
                if (slot > (Queue.maxSize/2)) break; // Only show up to 5 players
                ItemStack playerHead = getPlayer(teamMember);
                inventory.setItem(slot, playerHead);
                slot++;
            }

            System.out.println("TeleportInventoryListener: Finished setting up inventory items");
        }
    }

    private ItemStack getPlayer(Player player) {
        if (player == null) {
            System.out.println("TeleportInventoryListener: Null player in getPlayer");
            return new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
        }
        String color = Colorer.getTeamColor(player,false);
        ItemStack playerHead = new ItemStack(Material.PLAYER_HEAD, 1);
        ItemMeta meta = playerHead.getItemMeta();
        if (meta == null) {
            System.out.println("TeleportInventoryListener: Failed to get ItemMeta for player head");
            return new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
        }

        try {
            if (meta instanceof SkullMeta skullMeta) {
                skullMeta.setOwningPlayer(player);
                skullMeta.setDisplayName(color + player.getName());
                List<String> lore = new ArrayList<>();
                lore.add(ChatColor.GRAY + "Klicke, um zu " + color +player.getName() + ChatColor.GRAY + " zu teleportieren");
                skullMeta.setLore(lore);
                playerHead.setItemMeta(skullMeta);
                if (Configuration.debugger) {System.out.println("TeleportInventoryListener: Created player head for " + player.getName());}
                return playerHead;
            } else {
                System.out.println("TeleportInventoryListener: Failed to cast to SkullMeta");
                return new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
            }
        } catch (Exception e) {
            System.out.println("TeleportInventoryListener: Error creating player head: " + e.getMessage());
            e.printStackTrace();
            return new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
        }
    }

    private ItemStack getSpawn(Player player) {
        String color = Colorer.getTeamColor(player,false);
        ItemStack spawn = new ItemStack(Material.ENDER_CHEST);
        ItemMeta meta = spawn.getItemMeta();
        if (meta == null) {
            System.out.println("TeleportInventoryListener: Failed to get ItemMeta for spawn");
            return new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
        }

        try {
            meta.setDisplayName(color + "Spawn");
            List<String> lore = new ArrayList<>();
            lore.add(ChatColor.GRAY + "Klicke, um zum Spawn zu teleportieren");
            meta.setLore(lore);
            spawn.setItemMeta(meta);
            System.out.println("TeleportInventoryListener: Created spawn item");
            return spawn;
        } catch (Exception e) {
            System.out.println("TeleportInventoryListener: Error creating spawn item: " + e.getMessage());
            e.printStackTrace();
            return new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
        }
    }
}
