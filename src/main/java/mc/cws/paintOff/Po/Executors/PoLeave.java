package mc.cws.paintOff.Po.Executors;

import mc.cws.paintOff.Configuration;
import mc.cws.paintOff.Game.Arena.Arena;
import mc.cws.paintOff.Game.Extras.ClearInventory;
import mc.cws.paintOff.Game.Management.*;
import mc.cws.paintOff.Game.Management.DeadStop;
import mc.cws.paintOff.Game.Management.InGame.Game;
import mc.cws.paintOff.Game.Management.Queue;
import mc.cws.paintOff.Scoreboards.Scoreboards;
import mc.cws.paintOff.Game.Management.Start;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import java.util.List;

public class PoLeave implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        leaveGame(sender);
        return true;
    }

    public static void leaveGame(CommandSender sender) {
        if (sender instanceof Player player) {
            int n = Start.getQueueNumber(player);
            if (Start.isPlayerInQueue(n, player)) {
                Arena.portLobby(player);
                player.setExp(0);
                player.setLevel(0);

                // Remove player from all lists
                if (Queue.queuedPlayers.containsKey(n)) {
                    List<Player> queue = Queue.queuedPlayers.get(n);
                    if (queue != null) {
                        queue.remove(player);
                        Queue.playerCount[n]--; // Decrease player count
                    }
                }

                List<Player> inGameList = Game.inGame.get(n);
                if (inGameList != null) {
                    inGameList.remove(player);
                }

                List<Player> teamAList = Verteiler.teamA.get(n);
                if (teamAList != null) {
                    teamAList.remove(player);
                }

                List<Player> teamBList = Verteiler.teamB.get(n);
                if (teamBList != null) {
                    teamBList.remove(player);
                }

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
                ClearInventory.invClearPlayer(player);
                DeadStop.downGrade(player);


                // Let the queue system handle the rest
                Queue.checkQueue(n);

                if (Configuration.testbuild) {
                    player.sendMessage(ChatColor.GOLD + Configuration.name + " | " + ChatColor.GRAY + "Du hast das Spiel verlassen!");
                } else {
                    player.kickPlayer("Dann geh halt!");
                }
            } else {
                player.sendMessage(ChatColor.RED + Configuration.name + " | " + ChatColor.GRAY + "Du bist nicht in einer Queue!");
            }
        }
    }
}