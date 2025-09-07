package mc.cws.paintOff.Game.Management.Scoreboards;

import mc.cws.paintOff.Configuration;
import mc.cws.paintOff.Game.Arena.ArenaAuswahl;
import mc.cws.paintOff.Game.Extras.Colorer;
import mc.cws.paintOff.Game.Management.Queue;
import mc.cws.paintOff.Game.Management.Start;
import mc.cws.paintOff.Game.Management.Verteiler;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Scoreboards {
    private static final Map<Player, Scoreboard> playerScoreboards = new HashMap<>();
    private static final String TITLE = ChatColor.YELLOW + "" + ChatColor.BOLD + Configuration.name;

    public static void updateScoreboardQueue(Player player) {
        int n = Start.getQueueNumber(player);
        Scoreboard scoreboard = playerScoreboards.getOrDefault(player,
                Bukkit.getScoreboardManager().getNewScoreboard());

        Objective oldObj = scoreboard.getObjective("playerDisplay");
        if (oldObj != null) {
            oldObj.unregister();
        }
        scoreboard.getTeams().forEach(Team::unregister);
        Objective objective = scoreboard.registerNewObjective("playerDisplay", Criteria.DUMMY, TITLE);
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);

        String[] entries = {
                "§b",
                ChatColor.GRAY + "Queue: " + ChatColor.GOLD + "(" + Queue.playerCount[n] + "/" + Queue.maxSize + ")",
                "§a",
                ChatColor.GRAY + "Maps: ",
                ChatColor.GREEN + ArenaAuswahl.arena1[n] + ChatColor.GRAY + " | Votes: " + ChatColor.YELLOW + ArenaAuswahl.vote[n][0],
                ChatColor.GREEN + ArenaAuswahl.arena2[n] + ChatColor.GRAY + " | Votes: " + ChatColor.YELLOW + ArenaAuswahl.vote[n][1],
                "§c",
        };


        Team line1 = scoreboard.registerNewTeam("l1_" + n);
        Team line2 = scoreboard.registerNewTeam("l2_" + n);
        Team line3 = scoreboard.registerNewTeam("l3_" + n);
        Team line4 = scoreboard.registerNewTeam("l4_" + n);
        Team line5 = scoreboard.registerNewTeam("l5_" + n);
        Team line6 = scoreboard.registerNewTeam("l6_" + n);
        Team line7 = scoreboard.registerNewTeam("l7_" + n);

        line1.addEntry(entries[0]);
        line2.addEntry(entries[1]);
        line3.addEntry(entries[2]);
        line4.addEntry(entries[3]);
        line5.addEntry(entries[4]);
        line6.addEntry(entries[5]);
        line7.addEntry(entries[6]);

        objective.getScore(entries[0]).setScore(6);
        objective.getScore(entries[1]).setScore(5);
        objective.getScore(entries[2]).setScore(4);
        objective.getScore(entries[3]).setScore(3);
        objective.getScore(entries[4]).setScore(2);
        objective.getScore(entries[5]).setScore(1);
        objective.getScore(entries[6]).setScore(0);

        player.setScoreboard(scoreboard);
        playerScoreboards.put(player, scoreboard);
    }

    public static void updateScoreboardGame(Player player) {
        int n = Start.getQueueNumber(player);
        String color = Colorer.getTeamColor(player,false);
        String oponentColor = Colorer.getTeamColor(player,true);
        String team = Verteiler.getTeam(player, n);
        int teamNumber = 1;
        int oponentNumber = 0;
        if (team == null) {
            return;
        }
        if (team.equals("A")) {
            teamNumber = 0;
            oponentNumber = 1;
        }
        Scoreboard scoreboard = playerScoreboards.getOrDefault(player, Objects.requireNonNull(Bukkit.getScoreboardManager()).getNewScoreboard());
        Objective oldObj = scoreboard.getObjective("playerDisplay");
        if (oldObj != null) {
            oldObj.unregister();
        }
        scoreboard.getTeams().forEach(Team::unregister);
        Objective objective = scoreboard.registerNewObjective("playerDisplay", Criteria.DUMMY, TITLE);
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);

        String[] entries = {
                ChatColor.YELLOW + "               " + (Start.remainingMinutes[n]) + ":" + (Start.remainingTens[n]) + (Start.remainingSeconds[n]),
                "§c",
                ChatColor.GRAY + "Eingefärbt: " + color + Start.colored.get(player),
                ChatColor.GRAY + "Punkte: " + color + Start.points.get(player),
                "§b",
                ChatColor.GRAY + "Eleminierungen: " + color + Start.kills.get(player),
                ChatColor.GRAY + "Rampage Level: " + color + ChatColor.BOLD + Start.rampage.get(player),
                "§a",
                ChatColor.GRAY + "Team A: " + oponentColor + Start.teamColored[n][oponentNumber][0],
                ChatColor.GRAY + "Team B: " + color + Start.teamColored[n][teamNumber][0],
                "§e",
        };


        Team line1 = scoreboard.registerNewTeam("l1_" + n+color);
        Team line2 = scoreboard.registerNewTeam("l2_" + n+color);
        Team line3 = scoreboard.registerNewTeam("l3_" + n+color);
        Team line4 = scoreboard.registerNewTeam("l4_" + n+color);
        Team line5 = scoreboard.registerNewTeam("l5_" + n+color);
        Team line6 = scoreboard.registerNewTeam("l6_" + n+color);
        Team line7 = scoreboard.registerNewTeam("l7_" + n+color);
        Team line8 = scoreboard.registerNewTeam("l8_" + n+color);
        Team line9 = scoreboard.registerNewTeam("l9_" + n+color);
        Team line10 = scoreboard.registerNewTeam("l10_" + n+color);
        Team line11 = scoreboard.registerNewTeam("l11_" + n+color);

        line1.addEntry(entries[0]);
        line2.addEntry(entries[1]);
        line3.addEntry(entries[2]);
        line4.addEntry(entries[3]);
        line5.addEntry(entries[4]);
        line6.addEntry(entries[5]);
        line7.addEntry(entries[6]);
        line8.addEntry(entries[7]);
        line9.addEntry(entries[8]);
        line10.addEntry(entries[9]);
        line11.addEntry(entries[10]);

        objective.getScore(entries[0]).setScore(10);
        objective.getScore(entries[1]).setScore(9);
        objective.getScore(entries[2]).setScore(8);
        objective.getScore(entries[3]).setScore(7);
        objective.getScore(entries[4]).setScore(6);
        objective.getScore(entries[5]).setScore(5);
        objective.getScore(entries[6]).setScore(4);
        objective.getScore(entries[7]).setScore(3);
        objective.getScore(entries[8]).setScore(2);
        objective.getScore(entries[9]).setScore(1);
        objective.getScore(entries[10]).setScore(0);

        player.setScoreboard(scoreboard);
        playerScoreboards.put(player, scoreboard);
    }

    public static void removeScoreboard(Player player) {
        playerScoreboards.remove(player);
        player.setScoreboard(Objects.requireNonNull(Bukkit.getScoreboardManager()).getNewScoreboard());
    }
}