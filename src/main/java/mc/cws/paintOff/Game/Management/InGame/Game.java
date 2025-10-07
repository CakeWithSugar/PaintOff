package mc.cws.paintOff.Game.Management.InGame;

import mc.cws.paintOff.Configuration;
import mc.cws.paintOff.Game.Arena.Arena;
import mc.cws.paintOff.Game.Arena.ArenaAuswahl;
import mc.cws.paintOff.Game.Items.ArsenalInventoryListener;
import mc.cws.paintOff.Game.Items.Primarys.Prime.PrimeMillilat;
import mc.cws.paintOff.Game.Management.Scoreboards.Scoreboards;
import mc.cws.paintOff.Game.Resources.ExtraItems;
import mc.cws.paintOff.Game.Resources.UltPoints;
import mc.cws.paintOff.Game.Management.Queue;
import mc.cws.paintOff.Game.Management.Start;
import mc.cws.paintOff.Game.Management.Stop;
import mc.cws.paintOff.Game.Management.Verteiler;
import mc.cws.paintOff.Game.Shop.ShopInventory;
import mc.cws.paintOff.Game.Items.Primarys.None.Abwandlung.FiftySnipEx;
import mc.cws.paintOff.Game.Items.Primarys.None.Abwandlung.Pikolat;
import mc.cws.paintOff.Game.Items.Primarys.Haepec.HaeSchnipsLehr;
import mc.cws.paintOff.Game.Items.Primarys.Prime.PrimeSchweddler;
import mc.cws.paintOff.Game.Items.Primarys.None.*;
import mc.cws.paintOff.Game.Items.Primarys.Pyrex.PyrBlubber;
import mc.cws.paintOff.Game.Items.Primarys.Tulipa.TulQuinter;
import mc.cws.paintOff.Game.Items.Primarys.Tulipa.TulTriAtler;
import mc.cws.paintOff.Game.Items.Primarys.Pyrex.PyrTwentySniplEx;
import mc.cws.paintOff.Game.Items.Secondarys.*;
import mc.cws.paintOff.Game.Items.Secondarys.Marker;
import mc.cws.paintOff.Game.Items.Ultimates.*;
import mc.cws.paintOff.Game.Extras.Painter;
import mc.cws.paintOff.Game.Points.Points;
import mc.cws.paintOff.PaintOffMain;
import mc.cws.paintOff.Po.Executors.PoLeave;
import mc.cws.paintOff.Po.TeleportInventoryListener;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.*;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.util.Vector;
import java.util.*;

public class Game implements Listener {
    private static PaintOffMain plugin;
    public static void setPlugin(PaintOffMain plugin) {Game.plugin = plugin;}

    public static final Map<Integer, List<Player>> inGame = new HashMap<>();
    public static final List<Player> cantUse = new ArrayList<>();
    public static final List<Player> cantSneak = new ArrayList<>();
    public static final Map<Integer, Integer> sneakTaskIds = new HashMap<>();  // Map für Task-IDs pro Spiel

    public static void dealDamage(Player player,Player hurter, double damage) {
        PotionEffect shield = player.getPotionEffect(PotionEffectType.RESISTANCE);
        if (shield != null) {
            return;
        }
        double health = player.getHealth();
        if (health - damage <= 0) {
            playDead(player, hurter, Start.getQueueNumber(player));
        } else {
            player.getWorld().playSound(player.getLocation(), Sound.ENTITY_PLAYER_HURT_SWEET_BERRY_BUSH, 0.5f, 1.0f);
            player.setHealth(health - damage);
        }
    }

    public static void addPlayer(Player player, int n) {
        if (!inGame.containsKey(n)) {
            inGame.put(n, new ArrayList<>());
        }
        inGame.get(n).add(player);
    }

    public static void removeGroup(int n) {
        List<Player> queuePlayers = Start.getPlayersInQueue(n);
        if (inGame.containsKey(n)) {
            inGame.get(n).removeAll(queuePlayers);
        }
    }

    public static void playDead(Player player, Player killer, int n) {
        // Sicherheitsabfrage
        if (player == killer) {
            playDeadSingle(player,n);
        }
        // Add potion effects
        player.addPotionEffect(new PotionEffect(PotionEffectType.DARKNESS,(2 * 20) , 0, false, false));
        getCauseOfDeath(player, killer, n);
        Points.givePoints(killer, 10);
        player.getWorld().spawnParticle(Particle.END_ROD, player.getLocation(), 20, 0.1, 0.1, 0.1, 0.05);
        Objects.requireNonNull(killer.getLocation().getWorld()).playSound(killer.getLocation(), Sound.ENTITY_IRON_GOLEM_REPAIR, 0.5f, 2.0f);
        Arena.portToArena(player, n);
        cantUse.add(player);
        cantSneak.add(player);
        player.setHealth(20.0);
        player.getInventory().setItem(1, null);
        player.getInventory().setItem(5, null);
        player.getInventory().setItem(6, null);
        player.getInventory().setItem(7, null);
        ExtraItems.getKills(killer);
        Start.kills.put(killer, Start.kills.get(killer)+1);
        controlKills(killer);
        Scoreboards.updateScoreboardGame(killer);
        for (int i = 0; i < Configuration.maxWaffen+1; i++) {
            if (Start.kitNumberBefore.get(i).contains(player)) {
                Start.kitNumberBefore.get(i).remove(player);
                break;
            }
        }
        if (Start.spawnSave1.contains(player)) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.RESISTANCE,(10 * 20) , 0, false, false));
        } else if (Start.spawnSave2.contains(player)) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.RESISTANCE,(12 * 20) , 0, false, false));
        } else if (Start.spawnSave3.contains(player)) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.RESISTANCE,(14 * 20) , 0, false, false));
        }

        Start.kitNumberBefore.get(ArsenalInventoryListener.getKitNumber(player)).add(player);
        player.getWorld().playSound(player.getLocation(), Sound.ENTITY_GHAST_DEATH, 0.5f, 1.0f);

        // Schedule return to game
        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                onlinePlayer.showPlayer(plugin, player);
            }
            cantUse.remove(player);
            cantSneak.remove(player);
            int kitNumber = ArsenalInventoryListener.getKitNumber(player);
            int kitNumberThen = kitNumber;
            for (int i = 0; i < Configuration.maxWaffen+1; i++) {
                if (TeleportInventoryListener.kitChange.containsKey(i) && TeleportInventoryListener.kitChange.get(i).contains(player)) {
                    kitNumberThen = i;
                }
            }
            // Prüfen, ob das Kit gewechselt wurde
            if (kitNumber != kitNumberThen) {
                // Kit wurde geändert - hier die Logik für den Kit-Wechsel
                TeleportInventoryListener.kitChange.get(kitNumberThen).add(player);
                Start.ultpoints.put(player, 0);
            }
            ArsenalInventoryListener.getArsenal(player);
            player.getInventory().setItem(3, null);
            // Send message
            player.sendMessage(ChatColor.GRAY + "§o" +"Du bist wieder im Spiel!");
        }, Configuration.respawnTime * 20L);
    }
    public static void playDeadSingle(Player player, int n) {
        // Add potion effects
        player.addPotionEffect(new PotionEffect(PotionEffectType.DARKNESS,(2 * 20) , 0, false, false));
        getSelfDeath(player, n);
        player.getWorld().spawnParticle(Particle.END_ROD, player.getLocation(), 20, 0.1, 0.1, 0.1, 0.05);
        Arena.portToArena(player, n);
        cantUse.add(player);
        cantSneak.add(player);
        player.setHealth(20.0);
        player.getInventory().setItem(1, null);
        player.getInventory().setItem(5, null);
        player.getInventory().setItem(6, null);
        player.getInventory().setItem(7, null);
        for (int i = 0; i < Configuration.maxWaffen+1; i++) {
            if (Start.kitNumberBefore.get(i).contains(player)) {
                Start.kitNumberBefore.get(i).remove(player);
                break;
            }
        }
        Start.kitNumberBefore.get(ArsenalInventoryListener.getKitNumber(player)).add(player);
        player.getWorld().playSound(player.getLocation(), Sound.ENTITY_GHAST_DEATH, 0.5f, 1.0f);

        if (Start.spawnSave1.contains(player)) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.RESISTANCE,(10 * 20) , 0, false, false));
        } else if (Start.spawnSave2.contains(player)) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.RESISTANCE,(12 * 20) , 0, false, false));
        } else if (Start.spawnSave3.contains(player)) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.RESISTANCE,(14 * 20) , 0, false, false));
        }
        // Schedule return to game
        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                onlinePlayer.showPlayer(plugin, player);
            }
            cantUse.remove(player);
            cantSneak.remove(player);
            int kitNumber = ArsenalInventoryListener.getKitNumber(player);
            int kitNumberThen = kitNumber;
            for (int i = 0; i < Configuration.maxWaffen+1; i++) {
                if (TeleportInventoryListener.kitChange.containsKey(i) && TeleportInventoryListener.kitChange.get(i).contains(player)) {
                    kitNumberThen = i;
                }
            }
            // Prüfen, ob das Kit gewechselt wurde
            if (kitNumber != kitNumberThen) {
                // Kit wurde geändert - hier die Logik für den Kit-Wechsel
                TeleportInventoryListener.kitChange.get(kitNumberThen).add(player);
                Start.ultpoints.put(player, 0);
            }
            ArsenalInventoryListener.getArsenal(player);
        }, (long) Configuration.respawnTime * 20L); // 10 Sekunden
    }

    public static void getCauseOfDeath(Player victim, Player killer, int n) {
        String teamVictim = Verteiler.getTeam(victim, n);
        if (teamVictim == null || !teamVictim.equals("A") && !teamVictim.equals("B")) {
            return;
        }
        String teamKiller = Verteiler.getTeam(killer, n);
        if (teamKiller == null || !teamKiller.equals("A") && !teamKiller.equals("B")) {
            return;
        }
        String colorA = Verteiler.colorA[n];
        String colorB = Verteiler.colorB[n];
        if (teamVictim.equals("A") && (teamKiller.equals("B"))) {
            for (Player p : inGame.get(n)) {
                p.sendMessage(colorB + killer.getName() + ChatColor.GRAY + "  --> x  " + colorA + ChatColor.STRIKETHROUGH + victim.getName());
            }
        } else {
            for (Player p : inGame.get(n)) {
                p.sendMessage(colorA + killer.getName() + ChatColor.GRAY + "  --> x  " + colorB + ChatColor.STRIKETHROUGH + victim.getName());
            }
        }
    }
    public static void getSelfDeath(Player victim, int n) {
        String teamVictim = Verteiler.getTeam(victim, n);
        if (teamVictim == null || !teamVictim.equals("A") && !teamVictim.equals("B")) {
            return;
        }
        String colorA = Verteiler.colorA[n];
        String colorB = Verteiler.colorB[n];
        if (teamVictim.equals("A")) {
            for (Player p : inGame.get(n)) {
                p.sendMessage(ChatColor.GRAY + "--> x  " + colorA + ChatColor.STRIKETHROUGH + victim.getName());
            }
        } else {
            for (Player p : inGame.get(n)) {
                p.sendMessage(ChatColor.GRAY + "--> x  " + colorB + ChatColor.STRIKETHROUGH + victim.getName());
            }
        }
    }


    @EventHandler
    public void onArrowStuckInGround(ProjectileHitEvent event) {
        if (event.getEntity() instanceof Arrow arrow) {
            if (arrow.getShooter() instanceof Player shooter) {
                int n = Start.getQueueNumber(shooter);
                List<Player> players = inGame.get(n);
                if (players != null && players.contains(shooter)) {
                    arrow.remove(); // Remove the arrow when it hits the ground
                }
            }
        }
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        int n = Start.getQueueNumber(player);
        if (n == -1) return;

        // Get the player's team color
        String team = Verteiler.getTeam(player, n);
        if (team == null) return;

        // Get the block under the player
        Block blockUnder = player.getLocation().getBlock().getRelative(0, -1, 0);

        // Check if the block is painted
        Material blockType = blockUnder.getType();
        if (blockType == Material.valueOf(Stop.getColorNameA(n) + Painter.getBlockType()) ||
                blockType == Material.valueOf(Stop.getColorNameB(n) + Painter.getBlockType())) {

            // Get the color name from the material
            String blockColor = blockType.name().replace(Painter.getBlockType(), "");

            // Determine if it's enemy color
            boolean isEnemyColor = false;

            if (team.equals("A")) {
                isEnemyColor = blockColor.equals(Stop.getColorNameB(n));
            } else if (team.equals("B")) {
                isEnemyColor = blockColor.equals(Stop.getColorNameA(n));
            }

            // Apply slow effect if on enemy color
            if (isEnemyColor) {
                player.addPotionEffect(new PotionEffect(PotionEffectType.SLOWNESS, 1, 2, false, false));
            } else {
                // Remove slow effect if not on enemy color
                player.removePotionEffect(PotionEffectType.SLOWNESS);
            }
        }
        // Check if player is in water/lava
        Block block = player.getLocation().getBlock();
        if (block.getType() == Material.WATER ||
                block.getType() == Material.POWDER_SNOW ||
                block.getType() == Material.LAVA ||
                block.getType() == Material.CAMPFIRE ||
                block.getType() == Material.FIRE ||
                block.getType() == Material.SOUL_CAMPFIRE) {
            if (inGame.get(n) != null && inGame.get(n).contains(player)) {
                // Verstecke den Spieler von anderen Spielern
                for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                    if (!onlinePlayer.equals(player)) {
                        onlinePlayer.hidePlayer(plugin, player);
                    }
                }
                playDeadSingle(player, n);
            }
        }
    }

    @EventHandler
    public void onItemPickup(PlayerPickupItemEvent event) {
        // Get the player who is trying to pick up the item
        Player player = event.getPlayer();
        int n = Start.getQueueNumber(player);
        if ((inGame.containsKey(n) && inGame.get(n).contains(player)) ||
                (mc.cws.paintOff.Game.Management.Queue.queuedPlayers.containsKey(n) && mc.cws.paintOff.Game.Management.Queue.queuedPlayers.get(n).contains(player))) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerDamage(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player player)) return;

        int n = Start.getQueueNumber(player);
        if ((inGame.containsKey(n) && inGame.get(n).contains(player)) || 
            (mc.cws.paintOff.Game.Management.Queue.queuedPlayers.containsKey(n) && mc.cws.paintOff.Game.Management.Queue.queuedPlayers.get(n).contains(player))) {
            event.setCancelled(true); // Verhindere den Tod
        }
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        int n = Start.getQueueNumber(player);

        if ((inGame.containsKey(n) && inGame.get(n).contains(player)) || 
            (mc.cws.paintOff.Game.Management.Queue.queuedPlayers.containsKey(n) && mc.cws.paintOff.Game.Management.Queue.queuedPlayers.get(n).contains(player))) {
            event.setDeathMessage(null);
            event.setDroppedExp(0);
            event.getDrops().clear();
            // Verhindere den Todesscreen
            player.setHealth(20.0); // Setze Gesundheit auf maximum
            player.setHealth(2.0);  // Setze dann auf 1 Herz

            // Verhindere den Todesscreen durch einen Task
            Bukkit.getScheduler().runTask(plugin, () -> {
                player.spigot().respawn(); // Sofortige Wiederbelebung
            });
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        int n = Start.getQueueNumber(event.getPlayer());
        if (n == -1) return; // Player is not in any queue

        List<Player> players = inGame.get(n);
        List<Player> queuedPlayers = Queue.queuedPlayers.get(n);
        if ((players != null && players.contains(event.getPlayer())) ||
                (queuedPlayers != null && queuedPlayers.contains(event.getPlayer()))) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        int n = Start.getQueueNumber(event.getPlayer());
        if (n == -1) return; // Player is not in any queue

        List<Player> players = inGame.get(n);
        List<Player> queuedPlayers = Queue.queuedPlayers.get(n);
        if ((players != null && players.contains(event.getPlayer())) ||
                (queuedPlayers != null && queuedPlayers.contains(event.getPlayer()))) {
            event.setDropItems(false);
            event.setExpToDrop(0);
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerDropItem(PlayerDropItemEvent event) {
        int n = Start.getQueueNumber(event.getPlayer());
        List<Player> players = inGame.get(n);
        if (players != null && players.contains(event.getPlayer()) || Queue.queuedPlayers.get(n).contains(event.getPlayer())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
        int n = Start.getQueueNumber(event.getPlayer());
        List<Player> players = inGame.get(n);
        if (players != null && players.contains(event.getPlayer()) || Queue.queuedPlayers.get(n).contains(event.getPlayer())) {
            // Nur bestimmte Interaktionen blockieren
            if (event.getRightClicked() instanceof Player) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        int n = Start.getQueueNumber((Player) event.getWhoClicked());
        List<Player> players = inGame.get(n);
        List<Player> queuedPlayers = Queue.queuedPlayers.get(n);

        if ((players != null && players.contains(event.getWhoClicked())) ||
                (queuedPlayers != null && queuedPlayers.contains(event.getWhoClicked()))) {
            event.setCancelled(true);
        }
    }
    @EventHandler
    public void onPlayerSneak(PlayerToggleSneakEvent event) {
        Player player = event.getPlayer();
        int n = Start.getQueueNumber(player);
        if ((!(inGame.containsKey(n) && inGame.get(n).contains(player))) || cantSneak.contains(player)) {
            event.setCancelled(true);
            return;
        }

        if (!event.isSneaking()) {
            removeSneakEffects(player);
        }
        if (n == -1 || !Start.gameRunning[n]) {
            return;
        }

        // Get team and color
        String team = Verteiler.getTeam(player, n);
        if (team == null || (!team.equals("A") && !team.equals("B"))) {
            return;
        }

        String colorName = team.equals("A") ? Stop.getColorNameA(n) : Stop.getColorNameB(n);
        Material targetMaterial = Material.valueOf(colorName + Painter.getBlockType());

        // Check if player is sneaking
        if (event.isSneaking() && !cantSneak.contains(player)) {
            // Spawn initial particles
            Location loc = player.getLocation();
            player.getWorld().spawnParticle(Particle.SPLASH, loc, 1, 0.1, 0.1, 0.1, 0.1);

            // Play sound when sneaking starts if on team color
            Block currentBlock = player.getLocation().getBlock().getRelative(BlockFace.DOWN);
            if (currentBlock.getType() == targetMaterial && !player.hasPotionEffect(PotionEffectType.INVISIBILITY)) {
                player.playSound(player.getLocation(), Sound.ENTITY_AXOLOTL_SPLASH, 0.5f, 1.0f);
            }

            // Cancel existing task if it exists
            if (sneakTaskIds.containsKey(n)) {
                Bukkit.getScheduler().cancelTask(sneakTaskIds.get(n));
            }

            // Create new task that runs every tick
            int taskId = Bukkit.getScheduler().runTaskTimer(plugin, () -> {
                // Check if player is still sneaking
                if (!player.isSneaking()) {
                    removeSneakEffects(player);
                    return;
                }

                // Spawn particles at player's current location
                Location currentLoc = player.getLocation();
                player.spawnParticle(Particle.DOLPHIN, currentLoc, 2, 0.25, 0.1, 0.25, 0.1);

                // Check current block below player
                Block currentBlockCheck = currentLoc.getBlock().getRelative(BlockFace.DOWN);

                // Check if player is on their team's color
                if (currentBlockCheck.getType() == targetMaterial) {
                    // Add effects if not already present
                    player.getInventory().setHeldItemSlot(3);
                    addMissingSneakEffects(player);
                    
                    // Play sound when first stepping on team color
                    if (!player.hasPotionEffect(PotionEffectType.INVISIBILITY)) {
                        player.playSound(player.getLocation(), Sound.ENTITY_AXOLOTL_SPLASH, 0.5f, 1.0f);
                    }
                } else {
                    // Remove effects if player is not on team color
                    removeSneakEffects(player);
                }
            }, 0, 1).getTaskId();

            // Store the task ID
            sneakTaskIds.put(n, taskId);
        } else {
            // Remove effects when player stops sneaking
            removeSneakEffects(player);
            // Cancel task if it exists
            if (sneakTaskIds.containsKey(n)) {
                Bukkit.getScheduler().cancelTask(sneakTaskIds.get(n));
                sneakTaskIds.remove(n);
            }
        }
    }

    private void removeSneakEffects(Player player) {
        player.removePotionEffect(PotionEffectType.INVISIBILITY);
        player.removePotionEffect(PotionEffectType.WATER_BREATHING);
        player.removePotionEffect(PotionEffectType.SPEED);
        player.getInventory().setHeldItemSlot(5);
    }

    private void addMissingSneakEffects(Player player) {
        if (!player.hasPotionEffect(PotionEffectType.INVISIBILITY)) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 5*60*20, 0, false, false));
        }
        if (!player.hasPotionEffect(PotionEffectType.WATER_BREATHING)) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.WATER_BREATHING, 5*60*20, 0, false, false));
        }
        if (!player.hasPotionEffect(PotionEffectType.SPEED)) {
            if (Start.speed2.contains(player)) {
                player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 5 * 60 * 20, 21, false, false));
            } else if (Start.speed1.contains(player)) {
                player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 5 * 60 * 20, 19, false, false));
            } else {
                player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 5 * 60 * 20, 17, false, false));
            }
        }
        player.getInventory().setHeldItemSlot(3);
    }

    @EventHandler
    public void onPlayerUseItem(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        int n = Start.getQueueNumber(player);
        if (n == -1) return; // Player is not in any queue

        // Check if player is in a game or in queue
        List<Player> players = inGame.get(n);
        List<Player> queuedPlayers = Queue.queuedPlayers.get(n);
        boolean isInGame = players != null && players.contains(player);
        boolean isInQueue = queuedPlayers != null && queuedPlayers.contains(player);
        
        if (!isInGame && !isInQueue) return;

        ItemStack item = event.getItem();
        Action action = event.getAction();
        if (isInQueue && (action == Action.RIGHT_CLICK_BLOCK || action == Action.LEFT_CLICK_BLOCK || action == Action.PHYSICAL)) {
            event.setCancelled(true);
        }
        // Handle specific item interactions first
        if (item != null && item.hasItemMeta() && Objects.requireNonNull(item.getItemMeta()).hasDisplayName()) {
            String displayName = item.getItemMeta().getDisplayName();
            
            // Handle Arsenal item
            if (displayName.equals(ChatColor.YELLOW + "Arsenal") && 
                (action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK)) {
                event.setCancelled(true);
                player.openInventory(ArsenalInventoryListener.ARSENAL);
                return;
            }
            
            // Handle Shop item (only for queued players)
            if (isInQueue && item.getType() == Material.BARREL &&
                displayName.equals(ChatColor.LIGHT_PURPLE + "Shop")) {
                event.setCancelled(true);
                player.openInventory(ShopInventory.Main);
                return;
            }


            if (isInQueue && item.getType() == Material.END_PORTAL_FRAME &&
                    displayName.equals(ChatColor.LIGHT_PURPLE + "Arenen")) {
                int arenaCount = Arena.countArenas();
                if (arenaCount <= 1) {
                    player.sendMessage(ChatColor.RED + Configuration.name + " | " + ChatColor.GRAY + "Zu wenig Arenen!");
                    return;
                }
                event.setCancelled(true);
                player.openInventory(ArenaAuswahl.Auswahl);
                return;
            }

            if (isInQueue && item.getType() == Material.BARRIER &&
                    displayName.equals(ChatColor.RED + "Verlassen")) {
                event.setCancelled(true);
                PoLeave.leaveGame(player);
                return;
            }
        }
        if (item == null) {
            return;
        }
        String team = Verteiler.getTeam(player, n);
        if (team == null || !team.equals("A") && !team.equals("B")) {
            return;
        }
        String color = team.equals("A") ? Verteiler.colorA[n] : Verteiler.colorB[n];

        if (cantUse.contains(player)) {
            event.setCancelled(true);
            return;
        }
        if (players != null && players.contains(player)) {
            if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
                boolean hasInvisibility = player.hasPotionEffect(PotionEffectType.INVISIBILITY);
                if (hasInvisibility) {
                    event.setCancelled(true);
                    return;
                }
                if (item.getType() == Material.ECHO_SHARD && item.hasItemMeta() && Objects.requireNonNull(item.getItemMeta()).hasDisplayName() && item.getItemMeta().getDisplayName().equals(ChatColor.DARK_PURPLE + "Menu")) {
                    // Create a new inventory for this player
                    int size = 18;
                    Inventory inventory = Bukkit.createInventory(null, size, TeleportInventoryListener.ARSENAL_TITLE);

                    // Get team members
                    List<Player> currentPlayers = new ArrayList<>();
                    if (Verteiler.teamA.get(n).contains(player)) {
                        currentPlayers.addAll(Verteiler.teamA.get(n));
                        currentPlayers.remove(player);
                    } else if (Verteiler.teamB.get(n).contains(player)) {
                        currentPlayers.addAll(Verteiler.teamB.get(n));
                        currentPlayers.remove(player);
                    }
                    ItemStack glass = new ItemStack(Material.BLACK_STAINED_GLASS_PANE, 1);
                    ItemMeta meta3 = glass.getItemMeta();
                    if (meta3 != null) {
                        meta3.setDisplayName(ChatColor.GRAY + "");
                        glass.setItemMeta(meta3);
                        for (int i = 0; i < size; i++) {
                            inventory.setItem(i, glass);
                        }
                    }

                    ItemStack glassNull = new ItemStack(Material.GRAY_STAINED_GLASS_PANE, 1);
                    ItemMeta meta4 = glassNull.getItemMeta();
                    if (meta4 != null) {
                        meta4.setDisplayName(ChatColor.GRAY + "Spieler nicht vorhanden");
                        glassNull.setItemMeta(meta4);
                        for (int i = 1; i < (Queue.maxSize/2); i++) {
                            inventory.setItem(i, glassNull);
                        }
                    }

                    ItemStack weaponNull = new ItemStack(Material.BARRIER, 1);
                    ItemMeta meta5 = weaponNull.getItemMeta();
                    if (meta5 != null) {
                        meta5.setDisplayName("§c");
                        weaponNull.setItemMeta(meta5);
                        for (int i = 10; i < (Queue.maxSize/2)+9; i++) {
                            inventory.setItem(i, weaponNull);
                        }
                    }
                    ItemStack Arsenal = new ItemStack(Material.CHEST, 1);
                    ItemMeta meta6 = Arsenal.getItemMeta();
                    if (meta6 != null) {
                        meta6.setDisplayName(ChatColor.YELLOW + "Arsenal");
                        List<String> lore = new ArrayList<>();
                        lore.add(ChatColor.GRAY + "Klicke, um das Arsenal zu öffnen.");
                        lore.add("");
                        lore.add(ChatColor.GRAY + "Setzte hier dein Kit, um es nachdem du erledigt wurdest zu erhalten!");
                        lore.add(ChatColor.RED + "Ultpunkte gehen dabei verloren!");
                        meta6.setLore(lore);
                        Arsenal.setItemMeta(meta6);
                        inventory.setItem(8, Arsenal);
                    }
                    // Setze Spielerköpfe über die Glascheiben
                    int slot = 1;
                    for (Player teamMember : currentPlayers) {
                        if (slot >= (Queue.maxSize/2)) break;
                        ItemStack playerHead = new ItemStack(Material.PLAYER_HEAD, 1);
                        SkullMeta meta = (SkullMeta) playerHead.getItemMeta();
                        if (meta != null && teamMember != null) {
                            meta.setOwningPlayer(teamMember);
                            meta.setDisplayName(ChatColor.AQUA + teamMember.getName());
                            List<String> lore = new ArrayList<>();
                            lore.add(ChatColor.GRAY + "Klicke, um zu " + teamMember.getName() + " zu teleportieren");
                            meta.setLore(lore);
                            playerHead.setItemMeta(meta);
                            inventory.setItem(slot, playerHead); // Spielerkopf ersetzt Glascheibe
                        }
                        slot++;
                    }
                    for (Player teamMember : currentPlayers) {
                        if (slot+9 >= (Queue.maxSize/2)) break;
                        ArsenalInventoryListener.getWeapons(teamMember, inventory, slot);
                        slot++;
                    }

                    // Open the inventory
                    player.openInventory(inventory);
                }
                // Halloween
                if (item.getType() == Material.JACK_O_LANTERN && item.hasItemMeta() && Objects.requireNonNull(item.getItemMeta()).hasDisplayName() && item.getItemMeta().getDisplayName().equals(ChatColor.DARK_PURPLE + "Gruselschuss")) {
                    event.setCancelled(true);
                    Vernebler.shoot(player, color, true);
                }

                // ---- Primary Weapons ----
                if (item.getType() == Material.STICK && item.hasItemMeta() && Objects.requireNonNull(item.getItemMeta()).hasDisplayName() && item.getItemMeta().getDisplayName().equals(ChatColor.DARK_PURPLE + "Testwaffe")) {
                    Testing.shoot(player);
                }
                if (item.getType() == Material.IRON_HOE && item.hasItemMeta() && Objects.requireNonNull(item.getItemMeta()).hasDisplayName() && item.getItemMeta().getDisplayName().equals(ChatColor.DARK_PURPLE + "Schnips-Leer")) {
                    SchnipsLehr.shoot(player);
                }
                if (item.getType() == Material.WOODEN_HOE && item.hasItemMeta() && Objects.requireNonNull(item.getItemMeta()).hasDisplayName() && item.getItemMeta().getDisplayName().equals(ChatColor.DARK_PURPLE + "Haepec Schnips-Leer")) {
                    HaeSchnipsLehr.shoot(player);
                }
                if (item.getType() == Material.IRON_PICKAXE && item.hasItemMeta() && Objects.requireNonNull(item.getItemMeta()).hasDisplayName() && item.getItemMeta().getDisplayName().equals(ChatColor.DARK_PURPLE + "Musket")) {
                    Musket.shoot(player);
                }
                if (item.getType() == Material.GOLDEN_AXE && item.hasItemMeta() && Objects.requireNonNull(item.getItemMeta()).hasDisplayName() && item.getItemMeta().getDisplayName().equals(ChatColor.DARK_PURPLE + "Prime Millilat")) {
                    PrimeMillilat.shoot(player);
                }
                if (item.getType() == Material.IRON_AXE && item.hasItemMeta() && Objects.requireNonNull(item.getItemMeta()).hasDisplayName() && item.getItemMeta().getDisplayName().equals(ChatColor.DARK_PURPLE + "Mikrolat")) {
                    Mikrolat.shoot(player);
                }
                if (item.getType() == Material.DIAMOND_AXE && item.hasItemMeta() && Objects.requireNonNull(item.getItemMeta()).hasDisplayName() && item.getItemMeta().getDisplayName().equals(ChatColor.DARK_PURPLE + "Pikolat")) {
                    Pikolat.shoot(player);
                }
                if (item.getType() == Material.IRON_SWORD && item.hasItemMeta() && Objects.requireNonNull(item.getItemMeta()).hasDisplayName() && item.getItemMeta().getDisplayName().equals(ChatColor.DARK_PURPLE + "Schweddler")) {
                    Schweddler.shoot(player);
                }
                if (item.getType() == Material.GOLDEN_SWORD && item.hasItemMeta() && Objects.requireNonNull(item.getItemMeta()).hasDisplayName() && item.getItemMeta().getDisplayName().equals(ChatColor.DARK_PURPLE + "Prime Schweddler")) {
                    PrimeSchweddler.shoot(player);
                }
                if (item.getType() == Material.BOW && item.hasItemMeta() && Objects.requireNonNull(item.getItemMeta()).hasDisplayName() && item.getItemMeta().getDisplayName().equals(ChatColor.DARK_PURPLE + "Tri-Atler")) {
                    TriAtler.shoot(player);
                }
                if (item.getType() == Material.BOW && item.hasItemMeta() && Objects.requireNonNull(item.getItemMeta()).hasDisplayName() && item.getItemMeta().getDisplayName().equals(ChatColor.DARK_PURPLE + "Tulipa Tri-Atler")) {
                    TulTriAtler.shoot(player);
                }
                if (item.getType() == Material.CROSSBOW && item.hasItemMeta() && Objects.requireNonNull(item.getItemMeta()).hasDisplayName() && item.getItemMeta().getDisplayName().equals(ChatColor.DARK_PURPLE + "Quinter")) {
                    Quinter.shoot(player);
                }
                if (item.getType() == Material.CROSSBOW && item.hasItemMeta() && Objects.requireNonNull(item.getItemMeta()).hasDisplayName() && item.getItemMeta().getDisplayName().equals(ChatColor.DARK_PURPLE + "Tulipa Quinter")) {
                    TulQuinter.shoot(player);
                }
                if (item.getType() == Material.NETHERITE_SHOVEL && item.hasItemMeta() && Objects.requireNonNull(item.getItemMeta()).hasDisplayName() && item.getItemMeta().getDisplayName().equals(ChatColor.DARK_PURPLE + "Pyrex 25er-SniplEx")) {
                    PyrTwentySniplEx.shoot(player);
                }
                if (item.getType() == Material.IRON_SHOVEL && item.hasItemMeta() && Objects.requireNonNull(item.getItemMeta()).hasDisplayName() && item.getItemMeta().getDisplayName().equals(ChatColor.DARK_PURPLE + "25er-SniplEx")) {
                    SniplEx.shoot(player);
                }
                if (item.getType() == Material.DIAMOND_SHOVEL && item.hasItemMeta() && Objects.requireNonNull(item.getItemMeta()).hasDisplayName() && item.getItemMeta().getDisplayName().equals(ChatColor.DARK_PURPLE + "50er-SniplEx")) {
                    FiftySnipEx.shoot(player);
                }
                if (item.getType() == Material.LEATHER_HORSE_ARMOR && item.hasItemMeta() && Objects.requireNonNull(item.getItemMeta()).hasDisplayName() && item.getItemMeta().getDisplayName().equals(ChatColor.DARK_PURPLE + "Blubber")) {
                    Blubber.shoot(player);
                }
                if (item.getType() == Material.IRON_HORSE_ARMOR && item.hasItemMeta() && Objects.requireNonNull(item.getItemMeta()).hasDisplayName() && item.getItemMeta().getDisplayName().equals(ChatColor.DARK_PURPLE + "Pyrex Blubber")) {
                    PyrBlubber.shoot(player);
                }

                // --- Sekundärwaffen ---
                if (item.getType() == Material.HEART_OF_THE_SEA && item.hasItemMeta() && Objects.requireNonNull(item.getItemMeta()).hasDisplayName() && item.getItemMeta().getDisplayName().equals(ChatColor.DARK_PURPLE + "Dreifärber")) {
                    Dreifarber.shoot(player);
                }
                if (item.getType() == Material.LIGHT_WEIGHTED_PRESSURE_PLATE && item.hasItemMeta() && Objects.requireNonNull(item.getItemMeta()).hasDisplayName() && item.getItemMeta().getDisplayName().equals(ChatColor.DARK_PURPLE + "Klotzmine")) {
                    Klotzmine.shoot(player);
                }
                if (item.getType() == Material.SEAGRASS && item.hasItemMeta() && Objects.requireNonNull(item.getItemMeta()).hasDisplayName() && item.getItemMeta().getDisplayName().equals(ChatColor.DARK_PURPLE + "Amalgamalge")) {
                    Amalgamalge.shoot(player);
                }
                if (item.getType() == Material.COPPER_BULB && item.hasItemMeta() && Objects.requireNonNull(item.getItemMeta()).hasDisplayName() && item.getItemMeta().getDisplayName().equals(ChatColor.DARK_PURPLE + "Klotzbombe")) {
                    Klotzbombe.shoot(player);
                }
                if (item.getType() == Material.BREEZE_ROD && item.hasItemMeta() && Objects.requireNonNull(item.getItemMeta()).hasDisplayName() && item.getItemMeta().getDisplayName().equals(ChatColor.DARK_PURPLE + "Kreuzer")) {
                    Kreuzer.shoot(player);
                }
                if (item.getType() == Material.BEACON && item.hasItemMeta() && Objects.requireNonNull(item.getItemMeta()).hasDisplayName() && item.getItemMeta().getDisplayName().equals(ChatColor.DARK_PURPLE + "Aufdecker")) {
                    Aufdecker.shoot(player);
                }
                if (item.getType() == Material.FIREWORK_STAR && item.hasItemMeta() && Objects.requireNonNull(item.getItemMeta()).hasDisplayName() && item.getItemMeta().getDisplayName().equals(ChatColor.DARK_PURPLE + "Vernebler")) {
                    Vernebler.shoot(player, color,false);
                }
                if (item.getType() == Material.LEVER && item.hasItemMeta() && Objects.requireNonNull(item.getItemMeta()).hasDisplayName() && item.getItemMeta().getDisplayName().equals(ChatColor.DARK_PURPLE + "Marker")) {
                    Marker.shoot(player);
                }
                if (item.getType() == Material.LIGHTNING_ROD && item.hasItemMeta() && Objects.requireNonNull(item.getItemMeta()).hasDisplayName() && item.getItemMeta().getDisplayName().equals(ChatColor.DARK_PURPLE + "Reihenzieher")) {
                    Reihenzieher.shoot(player);
                }
            }
            if (event.getAction() == Action.LEFT_CLICK_AIR || event.getAction() == Action.LEFT_CLICK_BLOCK){
                // Ults
                int ult = UltPoints.getUltWeaponByKit(player);
                if (ult == 0) {
                    event.setCancelled(true);
                    Tornedo.handleItemUsage(player);
                }
                if (ult == 1) {
                    event.setCancelled(true);
                    GenetischerBoost.handleItemUsage(player);
                }
                if (ult == 2) {
                    event.setCancelled(true);
                    Wellenschlag.handleItemUsage(player);
                }
                if (ult == 3) {
                    event.setCancelled(true);
                    Sonnenwindler.handleItemUsage(player);
                }
                if (ult == 4) {
                    event.setCancelled(true);
                    Klotzhagel.handleItemUsage(player);
                }
                if (ult == 5) {
                    event.setCancelled(true);
                    PlatzRegen.handleItemUsage(player);
                }
                if (ult == 6) {
                    event.setCancelled(true);
                    Eruptor.handleItemUsage(player);
                }
                if (ult == 7) {
                    event.setCancelled(true);
                    Gammablitzer.handleItemUsage(player);
                }
                if (ult == 8) {
                    event.setCancelled(true);
                    GlitzerMeteor.handleItemUsage(player);
                }
                if (ult == 9) {
                    event.setCancelled(true);
                    Lauffeuer.handleItemUsage(player);
                }
            }
        }
    }
    //--------------------------------------------------------------------------------------------------------------------
    @EventHandler
    public void onSnowballHit(ProjectileHitEvent event) {
        // Check if the projectile is a snowball
        if (event.getEntity() instanceof Snowball snowball) {

            // Get the player who shot the snowball
            Player player = (Player) snowball.getShooter();
            if (player == null) {
                return;
            }

            int n = Start.getQueueNumber(player);
            if (n == -1) {
                return;
            }

            String team = Verteiler.getTeam(player, n);
            if (team == null || !team.equals("A") && !team.equals("B")) {
                return;
            }

            String color = team.equals("A") ? Stop.getColorNameA(n) : Stop.getColorNameB(n);

            // Check which item spawned the snowball
            String spawnedBy = "SchnipsLehr";
            List<MetadataValue> metadata = snowball.getMetadata("spawnedBy");
            if (!metadata.isEmpty()) {
                spawnedBy = metadata.getFirst().asString();
            }

            PotionEffect strength = player.getPotionEffect(PotionEffectType.STRENGTH);
            int multiplicator;
            if (strength != null) {
                multiplicator = 3;
            } else {
                multiplicator = 2;
            }
            // Check if the snowball hit a player
            Entity hitEntity = event.getHitEntity();
            if (hitEntity instanceof Player hitPlayer) {
                if (strength != null) {
                    hitPlayer.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, 20, 0));
                }
                if (Verteiler.teamB.get(n).contains(hitPlayer) && Verteiler.teamA.get(n).contains(player)) {
                    switch (spawnedBy) {
                        case "GlitzerMeteor","GlitzerMeteorTwo" -> {
                            hitPlayer.getWorld().spawnParticle(Particle.CRIT, hitPlayer.getLocation().add(0.5, 1, 0.5), 2, 0.5, 0.5, 0.5, 0.1);
                            Block newBlock = hitPlayer.getLocation().getBlock();
                            GlitzerMeteor.phaseTwo(snowball,newBlock,player,color);
                            return;
                        }
                        case "Grusel" -> {
                            hitPlayer.getWorld().playSound(hitPlayer.getLocation(), Sound.ENTITY_ZOMBIE_AMBIENT, 1.0f, 0.5f);
                            hitPlayer.getWorld().playSound(hitPlayer.getLocation(), Sound.ENTITY_GHAST_HURT, 1.0f, 0.5f);
                            hitPlayer.getWorld().spawnParticle(Particle.CRIT, hitPlayer.getLocation().add(0.5, 1, 0.5), 2, 0.5, 0.5, 0.5, 0.1);
                            dealDamage(hitPlayer,player, multiplicator);
                            Vernebler.phaseOne(snowball,hitEntity.getLocation().getBlock(),player,color);
                            return;
                        }
                        case "Vernebler" -> {
                            // Add visual effects
                            hitPlayer.getWorld().spawnParticle(Particle.CRIT, hitPlayer.getLocation().add(0.5, 1, 0.5), 2, 0.5, 0.5, 0.5, 0.1);
                            Vernebler.phaseOne(snowball,hitEntity.getLocation().getBlock(),player,color);
                            dealDamage(hitPlayer,player, multiplicator-1);
                            return;
                        }
                        case "Marker" -> {
                            // Add visual effects
                            hitPlayer.getWorld().spawnParticle(Particle.CRIT, hitPlayer.getLocation().add(0.5, 1, 0.5), 2, 0.5, 0.5, 0.5, 0.1);
                            dealDamage(hitPlayer,player, multiplicator);
                            hitPlayer.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, Marker.dauer*20, 0));
                            return;
                        }
                        case "Lower","TulBlubberChild" -> {
                            // Add visual effects
                            hitPlayer.getWorld().spawnParticle(Particle.CRIT, hitPlayer.getLocation().add(0.5, 1, 0.5), 2, 0.5, 0.5, 0.5, 0.1);
                            dealDamage(hitPlayer,player, multiplicator-1);
                            return;
                        }
                        case "Common" -> {
                            // Add visual effects
                            hitPlayer.getWorld().spawnParticle(Particle.CRIT, hitPlayer.getLocation().add(0.5, 1, 0.5), 2, 0.5, 0.5, 0.5, 0.1);
                            dealDamage(hitPlayer,player, multiplicator);
                            return;
                        }
                        case "Higher", "BlubberChild","PrimeSchweddler","TulBlubber" -> {
                            // Add visual effects
                            hitPlayer.getWorld().spawnParticle(Particle.CRIT, hitPlayer.getLocation().add(0.5, 1, 0.5), 2, 0.5, 0.5, 0.5, 0.1);
                            dealDamage(hitPlayer,player, multiplicator+1);
                            return;
                        }
                        case "DoubleHigher" -> {
                            // Add visual effects
                            hitPlayer.getWorld().spawnParticle(Particle.CRIT, hitPlayer.getLocation().add(0.5, 1, 0.5), 2, 0.5, 0.5, 0.5, 0.1);
                            dealDamage(hitPlayer,player, multiplicator+2);
                            return;
                        }
                        case "Blubber", "Snipl" -> {
                            // Add visual effects
                            hitPlayer.getWorld().spawnParticle(Particle.CLOUD, hitPlayer.getLocation().add(0.5, 1, 0.5), 2, 0.5, 0.5, 0.5, 0.1);
                            dealDamage(hitPlayer,player, multiplicator + 3);
                            return;
                        }
                        case "SniplTwo" -> {
                            // Add visual effects
                            hitPlayer.getWorld().spawnParticle(Particle.CRIT, hitPlayer.getLocation().add(0.5, 1, 0.5), 2, 0.5, 0.5, 0.5, 0.1);
                            dealDamage(hitPlayer,player, multiplicator + 8);
                            return;
                        }
                        case "PlatzRegen", "PlumpsBoje", "PlumpsBojeTwo", "PlumpsBojeThree",
                             "PlumpsBojeFour" -> {
                            // Add visual effects
                            hitPlayer.getWorld().spawnParticle(Particle.CRIT, hitPlayer.getLocation().add(0.5, 1, 0.5), 2, 0.5, 0.5, 0.5, 0.1);
                            dealDamage(hitPlayer,player, multiplicator*Tornedo.schaden);
                            return;
                        }
                        default -> {
                            dealDamage(hitPlayer,player, multiplicator);
                            return;
                        }
                    }
                } else if (Verteiler.teamA.get(n).contains(hitPlayer) && Verteiler.teamB.get(n).contains(player)) {
                    switch (spawnedBy) {
                        case "GlitzerMeteor","GlitzerMeteorTwo" -> {
                            hitPlayer.getWorld().spawnParticle(Particle.CRIT, hitPlayer.getLocation().add(0.5, 1, 0.5), 2, 0.5, 0.5, 0.5, 0.1);
                            Block newBlock = hitPlayer.getLocation().getBlock();
                            GlitzerMeteor.phaseTwo(snowball,newBlock,player,color);
                            return;
                        }
                        case "Grusel" -> {
                            hitPlayer.getWorld().playSound(hitPlayer.getLocation(), Sound.ENTITY_ZOMBIE_AMBIENT, 1.0f, 0.5f);
                            hitPlayer.getWorld().playSound(hitPlayer.getLocation(), Sound.ENTITY_GHAST_HURT, 1.0f, 0.5f);
                            hitPlayer.getWorld().spawnParticle(Particle.CRIT, hitPlayer.getLocation().add(0.5, 1, 0.5), 2, 0.5, 0.5, 0.5, 0.1);
                            dealDamage(hitPlayer,player, multiplicator);
                            Vernebler.phaseOne(snowball,hitEntity.getLocation().getBlock(),player,color);
                            return;
                        }
                        case "Vernebler" -> {
                            // Add visual effects
                            hitPlayer.getWorld().spawnParticle(Particle.CRIT, hitPlayer.getLocation().add(0.5, 1, 0.5), 2, 0.5, 0.5, 0.5, 0.1);
                            Vernebler.phaseOne(snowball,hitEntity.getLocation().getBlock(),player,color);
                            dealDamage(hitPlayer,player, multiplicator-1);
                            return;
                        }
                        case "Marker" -> {
                            // Add visual effects
                            hitPlayer.getWorld().spawnParticle(Particle.CRIT, hitPlayer.getLocation().add(0.5, 1, 0.5), 2, 0.5, 0.5, 0.5, 0.1);
                            dealDamage(hitPlayer,player, multiplicator);
                            hitPlayer.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, Marker.dauer*20, 0));
                            return;
                        }
                        case "Blubber", "Snipl" -> {
                            // Add visual effects
                            hitPlayer.getWorld().spawnParticle(Particle.CLOUD, hitPlayer.getLocation().add(0.5, 1, 0.5), 2, 0.5, 0.5, 0.5, 0.1);
                            dealDamage(hitPlayer,player, multiplicator + 3);
                            return;
                        }
                        case "SniplTwo" -> {
                            // Add visual effects
                            hitPlayer.getWorld().spawnParticle(Particle.CRIT, hitPlayer.getLocation().add(0.5, 1, 0.5), 2, 0.5, 0.5, 0.5, 0.1);
                            dealDamage(hitPlayer,player, multiplicator + 5);
                            return;
                        }
                        case "Common" -> {
                            // Add visual effects
                            hitPlayer.getWorld().spawnParticle(Particle.CRIT, hitPlayer.getLocation().add(0.5, 1, 0.5), 2, 0.5, 0.5, 0.5, 0.1);
                            dealDamage(hitPlayer,player, multiplicator);
                            return;
                        }
                        case "Lower","TulBlubberChild" -> {
                            // Add visual effects
                            hitPlayer.getWorld().spawnParticle(Particle.CRIT, hitPlayer.getLocation().add(0.5, 1, 0.5), 2, 0.5, 0.5, 0.5, 0.1);
                            dealDamage(hitPlayer,player, multiplicator-1);
                            return;
                        }
                        case "Higher", "BlubberChild","PrimeSchweddler","TulBlubber" -> {
                            // Add visual effects
                            hitPlayer.getWorld().spawnParticle(Particle.CRIT, hitPlayer.getLocation().add(0.5, 1, 0.5), 2, 0.5, 0.5, 0.5, 0.1);
                            dealDamage(hitPlayer,player, multiplicator+1);
                            return;
                        }
                        case "DoubleHigher" -> {
                            // Add visual effects
                            hitPlayer.getWorld().spawnParticle(Particle.CRIT, hitPlayer.getLocation().add(0.5, 1, 0.5), 2, 0.5, 0.5, 0.5, 0.1);
                            dealDamage(hitPlayer,player, multiplicator+2);
                            return;
                        }
                        case "PlatzRegen", "PlumpsBoje", "PlumpsBojeTwo", "PlumpsBojeThree",
                             "PlumpsBojeFour" -> {
                            // Add visual effects
                            hitPlayer.getWorld().spawnParticle(Particle.CRIT, hitPlayer.getLocation().add(0.5, 1, 0.5), 2, 0.5, 0.5, 0.5, 0.1);
                            dealDamage(hitPlayer,player, multiplicator*Tornedo.schaden);
                            return;
                        }
                        default -> {
                            dealDamage(hitPlayer,player, multiplicator);
                            return;
                        }
                    }
                }
            }

            // Get the block the snowball hit
            Block hitBlock = event.getHitBlock();
            if (hitBlock == null) {
                return;
            }
            if (spawnedBy.equals("TulBlubber")) {
                Location loc = snowball.getLocation().add(0, 1, 0);
                Snowball snowballChild = snowball.getWorld().spawn(snowball.getLocation(), Snowball.class);
                Snowball snowballChild2 = snowball.getWorld().spawn(snowball.getLocation(), Snowball.class);
                // Create a new vector with specific values for forward jumping
                Vector direction = new Vector(snowball.getVelocity().getX(), -snowball.getVelocity().getY() * 0.9, snowball.getVelocity().getZ()); // X, Y, Z components
                Vector leftDirection = direction.clone().rotateAroundY(Math.toRadians(15)); // 15 Grad nach links
                snowballChild.setVelocity(leftDirection);
                snowballChild.setMetadata("spawnedBy", new FixedMetadataValue(plugin, "TulBlubberChild"));
                snowballChild.setShooter(snowball.getShooter());

                Vector rightDirection = direction.clone().rotateAroundY(Math.toRadians(-15)); // 15 Grad nach links
                snowballChild2.setVelocity(rightDirection);
                snowballChild2.setMetadata("spawnedBy", new FixedMetadataValue(plugin, "TulBlubberChild"));
                snowballChild2.setShooter(snowball.getShooter());

                snowball.getWorld().spawnParticle(Particle.SNOWFLAKE, loc, 1, 0, 0, 0, 0.005);
                hitBlock.getWorld().playSound(hitBlock.getLocation(), Sound.BLOCK_SLIME_BLOCK_HIT, 0.5f, 2.0f);
            }
            if (spawnedBy.equals("TulBlubberChild")) {
                Location loc = snowball.getLocation().add(0, 1, 0);
                Painter.explosionAlgorithm(hitBlock, player, n, PyrBlubber.explosion, color, PyrBlubber.schaden);

                snowball.getWorld().spawnParticle(Particle.EXPLOSION, loc, 1, 0, 0, 0, 0.005);
                hitBlock.getWorld().playSound(hitBlock.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 0.1f, 2.0f);
            }
            if (spawnedBy.equals("Blubber")) {
                Location loc = snowball.getLocation().add(0, 1, 0);
                Snowball snowballChild = snowball.getWorld().spawn(snowball.getLocation(), Snowball.class);
                Snowball snowballChild2 = snowball.getWorld().spawn(snowball.getLocation(), Snowball.class);
                // Create a new vector with specific values for forward jumping
                Vector direction = new Vector(snowball.getVelocity().getX(), -snowball.getVelocity().getY() * 0.9, snowball.getVelocity().getZ()); // X, Y, Z components
                Vector leftDirection = direction.clone().rotateAroundY(Math.toRadians(8)); // 15 Grad nach links
                snowballChild.setVelocity(leftDirection);
                snowballChild.setMetadata("spawnedBy", new FixedMetadataValue(plugin, "BlubberChild"));
                snowballChild.setShooter(snowball.getShooter());

                Vector rightDirection = direction.clone().rotateAroundY(Math.toRadians(-8)); // 15 Grad nach links
                snowballChild2.setVelocity(rightDirection);
                snowballChild2.setMetadata("spawnedBy", new FixedMetadataValue(plugin, "BlubberChild"));
                snowballChild2.setShooter(snowball.getShooter());

                snowball.getWorld().spawnParticle(Particle.SNOWFLAKE, loc, 1, 0, 0, 0, 0.005);
                hitBlock.getWorld().playSound(hitBlock.getLocation(), Sound.BLOCK_SLIME_BLOCK_HIT, 0.5f, 2.0f);
            }
            if (spawnedBy.equals("BlubberChild")) {
                Location loc = snowball.getLocation().add(0, 1, 0);
                Snowball snowballChildTwo = snowball.getWorld().spawn(snowball.getLocation(), Snowball.class);
                // Create a new vector with specific values for forward jumping
                Vector direction = new Vector(snowball.getVelocity().getX(), -snowball.getVelocity().getY() * 0.75, snowball.getVelocity().getZ()); // X, Y, Z components
                snowballChildTwo.setVelocity(direction);
                snowballChildTwo.setMetadata("spawnedBy", new FixedMetadataValue(plugin, "Common"));
                snowballChildTwo.setShooter(snowball.getShooter());
                SnowballEffect.sin(snowballChildTwo, player, n);

                snowball.getWorld().spawnParticle(Particle.SNOWFLAKE, loc, 1, 0, 0, 0, 0.005);
                hitBlock.getWorld().playSound(hitBlock.getLocation(), Sound.BLOCK_SLIME_BLOCK_HIT, 0.125f, 2.0f);
            }
            if (spawnedBy.equals("PrimeSchweddler")) {
                List<Block> blocks = new ArrayList<>();
                for (int y = -1; y <= 1; y++) {
                    for (int i = -1; i <= 1; i++) {
                        // Add vertical blocks (x-axis)
                        if (i != 0) {  // Skip the center block which is already added
                            blocks.add(hitBlock.getRelative(i, y, 0));
                        }
                        // Add horizontal blocks (z-axis)
                        if (i != 0) {  // Skip the center block which is already added
                            blocks.add(hitBlock.getRelative(0, y, i));
                        }
                    }
                }
                blocks.add(hitBlock.getRelative(0, 0, 0));
                for (Block block : blocks) {
                    Painter.paintBlockWithUltpoint(block, player, n, color);
                }
            }

            // SEKUNDÄR ___________________________________________________
            if (spawnedBy.equals("Dreifärber")) {
                Dreifarber.phaseOne(snowball,hitBlock,player,color);
            }
            if (spawnedBy.equals("Zweifärber")) {
                Dreifarber.phaseTwo(snowball,hitBlock,player,color);
            }
            if (spawnedBy.equals("Einfärber")) {
                Dreifarber.phaseThree(snowball,hitBlock,player,color);
            }
            if (spawnedBy.equals("Klotzmine")) {
                Klotzmine.phaseOne(snowball,hitBlock,player,color,team);
            }
            if (spawnedBy.equals("Amalgam")) {
                Amalgamalge.phaseOne(snowball,player);
            }
            if (spawnedBy.equals("Marker")) {
                Marker.phaseOne(hitBlock,player,color);
            }
            if (spawnedBy.equals("Reihenzieher")) {
                Painter.paintBlockWithUltpoint(hitBlock,player,n,color);
            }
            if (spawnedBy.equals("Klotzbombe")) {
               Klotzbombe.phaseOne(hitBlock,player,color,multiplicator);
            }
            if (spawnedBy.equals("Kreuzbombe")) {
                Kreuzer.phaseOne(snowball,hitBlock,player,color);
            }
            if (spawnedBy.equals("KreuzbombeChild")) {
                Kreuzer.phaseTwo(hitBlock,player,color,multiplicator);
            }
            if (spawnedBy.equals("Aufdecker")) {
                Aufdecker.phaseOne(hitBlock,player,color);
            }
            if (spawnedBy.equals("Vernebler")) {
                Vernebler.phaseOne(snowball,hitBlock,player,color);
            }


            // ULTS ____________________________________
            if (spawnedBy.equals("Wellenschlag")) {
                Wellenschlag.phaseOne(snowball,hitBlock);
            }
            if (spawnedBy.equals("WellenschlagChild")) {
               Wellenschlag.phaseTwo(hitBlock,player,color);
            }
            if (spawnedBy.equals("PlatzRegen")) {
                PlatzRegen.phaseOne(snowball,hitBlock,player);
            }
            if (spawnedBy.equals("RainChild")) {
                PlatzRegen.phaseThree(snowball,hitBlock,player,color);
            }
            if (spawnedBy.equals("GlitzerMeteor")) {
                GlitzerMeteor.phaseOne(snowball,hitBlock,player);
            }
            if (spawnedBy.equals("GlitzerMeteorTwo")) {
                GlitzerMeteor.phaseTwo(snowball,hitBlock,player,color);
            }
            if (spawnedBy.equals("Sonnenwindler")) {
                Sonnenwindler.phaseOne(snowball,hitBlock,player);
            }
            if (spawnedBy.equals("PlumpsBoje")) {
                Eruptor.phaseOne(snowball,hitBlock);
            }
            if (spawnedBy.equals("PlumpsBojeTwo")) {
                Eruptor.phaseTwo(snowball,hitBlock,color,player);
            }
            if (spawnedBy.equals("PlumpsBojeThree")) {
                Eruptor.phaseThree(snowball,hitBlock,color,player);
            }
            if (spawnedBy.equals("PlumpsBojeFour")) {
                Eruptor.phaseFour(snowball,hitBlock,color,player);
            }
            if (spawnedBy.equals("Hagel")) {
               Klotzhagel.phaseTwo(hitBlock,player,color,multiplicator);
            }
            if (spawnedBy.equals("Grusel")) {
                Vernebler.phaseGrusel(player,hitBlock.getLocation(),n);
            }
        }
    }
    // ----------------------------------------------------------------------------------------------------

    @EventHandler
    public void onArrowlHit(ProjectileHitEvent event) {
        // Check if the projectile is a snowball
        if (event.getEntity() instanceof Arrow arrow) {

            Player player = (Player) arrow.getShooter();
            if (player == null) {
                return;
            }
            int n = Start.getQueueNumber(player);
            if (n == -1) {
                return;
            }
            // Get the block the snowball hit
            Block hitBlock = event.getHitBlock();
            if (hitBlock == null) {
                return;
            }

            // Get the player who shot the snowball
            String spawnedBy = "SchnipsLehr";
            List<MetadataValue> metadata = arrow.getMetadata("spawnedBy");
            if (!metadata.isEmpty()) {
                spawnedBy = metadata.getFirst().asString();
            }
            Entity hitEntity = event.getHitEntity();
            if (hitEntity instanceof Player hitPlayer) {
                if (Verteiler.teamB.get(n).contains(hitPlayer) && Verteiler.teamA.get(n).contains(player)) {
                    // Death
                    if (spawnedBy.equals("Tornedo")) {// Add visual effects
                        hitPlayer.getWorld().spawnParticle(Particle.CRIT, hitPlayer.getLocation().add(0.5, 1, 0.5), 2, 0.5, 0.5, 0.5, 0.1);
                        dealDamage(hitPlayer, player, Configuration.killDamage);
                        Tornedo.phaseOne(hitBlock, player, n);
                        return;
                    }
                    dealDamage(hitPlayer, player, 1);
                    return;
                } else if (Verteiler.teamA.get(n).contains(hitPlayer) && Verteiler.teamB.get(n).contains(player)) {
                    if (spawnedBy.equals("Tornedo")) {// Add visual effects
                        hitPlayer.getWorld().spawnParticle(Particle.CRIT, hitPlayer.getLocation().add(0.5, 1, 0.5), 2, 0.5, 0.5, 0.5, 0.1);
                        dealDamage(hitPlayer, player, Configuration.killDamage);
                        Tornedo.phaseOne(hitBlock, player, n);
                        return;
                    }
                    dealDamage(hitPlayer, player, 1);
                    return;
                }
            }
            if (spawnedBy.equals("Tornedo")) {
                Tornedo.phaseOne(hitBlock, player, n);
            }
        }
    }

    public static void setCooldown(Player player, long time) {
        cantUse.add(player);
        plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
            cantUse.remove(player);
        }, 5L * time);
    }

    public static void applyGravity(Snowball snowball, double strength) {
        Bukkit.getScheduler().runTaskTimer(plugin, task -> {
            if (!snowball.isValid()) {
                task.cancel();
                return;
            }
            // Apply additional downward velocity
            snowball.setVelocity(snowball.getVelocity().add(new org.bukkit.util.Vector(0, -strength, 0)));
        }, 0L, 1L);
    }

    public static void controlKills(Player player) {
        if (Start.kills.get(player) > 20) {
            Start.rampage.put(player, 5);
            return;
        }
        if (Start.kills.get(player) > 16) {
            Start.rampage.put(player, 4);
            return;
        }
        if (Start.kills.get(player) > 12) {
            Start.rampage.put(player, 3);
            return;
        }
        if (Start.kills.get(player) > 8) {
            Start.rampage.put(player, 2);
            return;
        }
        if (Start.kills.get(player) > 4) {
            Start.rampage.put(player, 1);
        }
    }
}