package mc.cws.paintOff.Game.Management;

import mc.cws.paintOff.Configuration;
import mc.cws.paintOff.Game.Arena.Arena;
import mc.cws.paintOff.Game.Extras.ClearInventory;
import mc.cws.paintOff.Game.Management.InGame.Game;
import mc.cws.paintOff.Game.Management.Scoreboards.ColorTeam;
import mc.cws.paintOff.Game.Management.Scoreboards.Scoreboards;
import mc.cws.paintOff.Game.Shop.Lists;
import mc.cws.paintOff.Game.Shop.ShopInventory;
import mc.cws.paintOff.PaintOffMain;
import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import mc.cws.paintOff.Po.ArenaManager;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DeadStop {
    private static PaintOffMain plugin;
    public static void setPlugin(PaintOffMain plugin) {
        DeadStop.plugin = plugin;}
    public static void stopGame(int n) {
        // Kill all snowballs when stopping the game
        killAllSnowballs();
        
        // Get players from both queue sources
        List<Player> players = Start.getPlayersInQueue(n);
        // Create a copy of the players list
        List<Player> playersCopy = new ArrayList<>();
        if (!players.isEmpty()) {
            playersCopy.addAll(players);
        }
        // Clear all game-related data
        ClearInventory.invClear(playersCopy);

        // Reset player visibility
        for (Player player : playersCopy) {
            if (player != null) {
                player.setLevel(0);
                player.setExp(0);
                player.setHealth(20);
                player.setGameMode(GameMode.ADVENTURE);
                player.setCustomName(player.getName());
                player.setCustomNameVisible(true);
                ColorTeam.removePlayerFromTeam(player, n);
            }
        }
        ColorTeam.clearScoreboard(n);
        String arenaName = Start.currentArena[n];

        // Reset arena name
        Start.currentArena[n] = "";

        // Reset game-specific flags
        Queue.lokStartBlock[n] = false;
        
        // Cancel all timers
        if (Start.gameTask[n] != 0) {
            Bukkit.getScheduler().cancelTask(Start.gameTask[n]);
            Start.gameTask[n] = 0;
        }
        if (Start.time[n] != 0) {
            Bukkit.getScheduler().cancelTask(Start.time[n]);
            Start.time[n] = 0;
        }
        if (Start.game[n] != 0) {
            Bukkit.getScheduler().cancelTask(Start.game[n]);
            Start.game[n] = 0;
        }
        if (Start.timer[n] != 0) {
            Bukkit.getScheduler().cancelTask(Start.timer[n]);
            Start.timer[n] = 0;
        }
        if (Queue.secondTimer[n] != 0) {
            Bukkit.getScheduler().cancelTask(Queue.secondTimer[n]);
            Queue.secondTimer[n] = 0;
        }
        if (Queue.countAllow[n] != 0) {
            Bukkit.getScheduler().cancelTask(Queue.countAllow[n]);
            Queue.countAllow[n] = 0;
        }
        if (Start.timeA[n] != 0) {
            Bukkit.getScheduler().cancelTask(Start.timeA[n]);
            Start.timeA[n] = 0;
        }
        if (Start.timeB[n] != 0) {
            Bukkit.getScheduler().cancelTask(Start.timeB[n]);
            Start.timeB[n] = 0;
        }
        if (Start.timeC[n] != 0) {
            Bukkit.getScheduler().cancelTask(Start.timeC[n]);
            Start.timeC[n] = 0;
        }
        if (Start.TaskId[n] != 0) {
            Bukkit.getScheduler().cancelTask(Start.TaskId[n]);
            Start.TaskId[n] = 0;
        }
        Start.gameRunning[n] = false;
        for (Player player : players) {
            player.removePotionEffect(PotionEffectType.WATER_BREATHING);
            player.removePotionEffect(PotionEffectType.GLOWING);
            player.removePotionEffect(PotionEffectType.SPEED);
            player.removePotionEffect(PotionEffectType.SLOWNESS);
            player.removePotionEffect(PotionEffectType.RESISTANCE);
            if (Start.speed1.contains(player)) {
                Start.speed1.remove(player);
            } else if (Start.speed2.contains(player)) {
                Start.speed2.remove(player);
            } else if (Start.spawnSave1.contains(player)) {
                Start.spawnSave1.remove(player);
            } else if (Start.spawnSave2.contains(player)) {
                Start.spawnSave2.remove(player);
            } else if (Start.spawnSave3.contains(player)) {
                Start.spawnSave3.remove(player);
            }
            Scoreboards.removeScoreboard(player);
            downGrade(player);
        }
        // Clean up game groups
        Verteiler.colorA[n] = "Weiß";
        Verteiler.colorB[n] = "Weiß";
        Start.teamColored[n][0][0] = 0;
        Start.teamColored[n][1][0] = 0;
        Start.remainingMinutes[n] = Configuration.gameTime-1;
        Start.remainingSeconds[n] = 9;
        Start.remainingTens[n] = 5;
        Game.removeGroup(n);
        Verteiler.reset(n);
        Queue.lokStartBlock[n] = false; // Reset lokStartBlock

        // Cancel sneaking tasks
        if (Game.sneakTaskIds.containsKey(n)) {
            Bukkit.getScheduler().cancelTask(Game.sneakTaskIds.get(n));
            Game.sneakTaskIds.remove(n);
        }

        // Reset queue
        Queue.stopQueue(n);
        // rejoin
        Arena.portLobbyGroup(playersCopy);
        for (Player player : playersCopy) {
            Queue.queueGameReal(player, false);
        }
        String worldString = Configuration.mainCommand +n;
        World world = Bukkit.getWorld(worldString);
        ArenaManager.deleteArenaBlocks(arenaName, n);
        unloadChunks(world);
    }

    private static void unloadChunks(World world) {
        if (world == null) return;

        // Gehe durch alle geladenen Chunks
        for (Chunk chunk : world.getLoadedChunks()) {
            // Prüfe, ob der Chunk nicht verwendet wird
            if (!chunk.isForceLoaded() && !chunk.isLoaded()) {
                chunk.unload(true);
            }
        }

        // Versuche den World-Garbage-Collector aufzurufen
        world.save();
        System.gc(); // Optional: Fordere Garbage Collection an
    }

    public static void downGrade(Player player) {
        for (int i = 0; i <= ShopInventory.DURATION_ROUNDS; i++) {
            if (i > 0 && Lists.hasBonus.get(i).contains(player)) {
                // Bewege den Spieler eine Runde nach unten
                Lists.hasBonus.get(i).remove(player);

                // Wenn wir nicht bei der letzten Runde sind, füge den Spieler zur nächsten Runde hinzu
                if (i > 1) {
                    Lists.hasBonus.get(i - 1).add(player);

                    // Bewege auch die entsprechenden Bonus-Listen
                    if (Lists.bonus25.get(i).contains(player)) {
                        Lists.bonus25.get(i).remove(player);
                        Lists.bonus25.get(i - 1).add(player);
                    } else if (Lists.bonus50.get(i).contains(player)) {
                        Lists.bonus50.get(i).remove(player);
                        Lists.bonus50.get(i - 1).add(player);
                    } else if (Lists.bonus100.get(i).contains(player)) {
                        Lists.bonus100.get(i).remove(player);
                        Lists.bonus100.get(i - 1).add(player);
                    } else if (Lists.recharge1.get(i).contains(player)) {
                        Lists.recharge1.get(i).remove(player);
                        Lists.recharge1.get(i - 1).add(player);
                    } else if (Lists.recharge2.get(i).contains(player)) {
                        Lists.recharge2.get(i).remove(player);
                        Lists.recharge2.get(i - 1).add(player);
                    } else if (Lists.speed1.get(i).contains(player)) {
                        Lists.speed1.get(i).remove(player);
                        Lists.speed1.get(i - 1).add(player);
                    } else if (Lists.speed2.get(i).contains(player)) {
                        Lists.speed2.get(i).remove(player);
                        Lists.speed2.get(i - 1).add(player);
                    } else if (Lists.protection1.get(i).contains(player)) {
                        Lists.protection1.get(i).remove(player);
                        Lists.protection1.get(i - 1).add(player);
                    } else if (Lists.protection2.get(i).contains(player)) {
                        Lists.protection2.get(i).remove(player);
                        Lists.protection2.get(i - 1).add(player);
                    } else if (Lists.protection3.get(i).contains(player)) {
                        Lists.protection3.get(i).remove(player);
                        Lists.protection3.get(i - 1).add(player);
                    }
                } else {
                    // Letzte Runde erreicht, entferne alle Booster
                    Lists.bonus25.get(i).remove(player);
                    Lists.bonus50.get(i).remove(player);
                    Lists.bonus100.get(i).remove(player);
                    Lists.recharge1.get(i).remove(player);
                    Lists.recharge2.get(i).remove(player);
                    Lists.speed1.get(i).remove(player);
                    Lists.speed2.get(i).remove(player);
                    Lists.protection1.get(i).remove(player);
                    Lists.protection2.get(i).remove(player);
                    Lists.protection3.get(i).remove(player);
                    player.sendMessage(ChatColor.RED + "Dein Booster ist abgelaufen!");
                }
                break; // Beende die Schleife, nachdem wir den Spieler gefunden haben
            }
        }
    }
    private static void killAllSnowballs() {
        // Kill all snowballs in all worlds
        for (org.bukkit.World world : Bukkit.getWorlds()) {
            for (Entity entity : world.getEntities()) {
                if (entity instanceof Snowball) {
                    entity.remove();
                }
            }
        }

        // Also execute the command as a fallback
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "kill @e[type=minecraft:snowball]");
    }
}
