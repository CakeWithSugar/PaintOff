package mc.cws.paintOff.Game.Management.Scoreboards;

import mc.cws.paintOff.Game.Management.Stop;
import mc.cws.paintOff.Game.Management.Verteiler;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.scoreboard.Team;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ColorTeam {
    private static final Map<Integer, Scoreboard> scoreboards = new HashMap<>();
    public static final Map<Integer, Team> teamA = new HashMap<>();
    public static final Map<Integer, Team> teamB = new HashMap<>();

    public static void createScoreboardForQueue(int n) {
        ScoreboardManager manager = Bukkit.getScoreboardManager();
        assert manager != null;
        Scoreboard board = manager.getNewScoreboard();

        // Team A erstellen
        Team teamA = board.registerNewTeam("teamA" + n);
        teamA.setAllowFriendlyFire(false);
        teamA.setCanSeeFriendlyInvisibles(true);
        teamA.setOption(Team.Option.NAME_TAG_VISIBILITY, Team.OptionStatus.ALWAYS);
        teamA.setOption(Team.Option.DEATH_MESSAGE_VISIBILITY, Team.OptionStatus.ALWAYS);
        teamA.setOption(Team.Option.COLLISION_RULE, Team.OptionStatus.FOR_OTHER_TEAMS);
        if (Verteiler.colorA[n] != null) {
            String colorName = Stop.getColorNameSmallA(n);
            teamA.setColor(ChatColor.valueOf(colorName.toUpperCase()));
        }
        ColorTeam.teamA.put(n, teamA);

        // Team B erstellen
        Team teamB = board.registerNewTeam("teamB" + n);
        teamB.setAllowFriendlyFire(false);
        teamB.setCanSeeFriendlyInvisibles(true);
        teamB.setOption(Team.Option.NAME_TAG_VISIBILITY, Team.OptionStatus.ALWAYS);
        teamB.setOption(Team.Option.DEATH_MESSAGE_VISIBILITY, Team.OptionStatus.ALWAYS);
        teamB.setOption(Team.Option.COLLISION_RULE, Team.OptionStatus.FOR_OTHER_TEAMS);
        if (Verteiler.colorB[n] != null) {
            String colorName = Stop.getColorNameSmallB(n);
            teamB.setColor(ChatColor.valueOf(colorName.toUpperCase()));
        }
        ColorTeam.teamB.put(n, teamB);

        scoreboards.put(n, board);
    }

    public static void addPlayerToTeam(Player player, int n) {
        if (!scoreboards.containsKey(n)) {
            createScoreboardForQueue(n);
        }

        Scoreboard board = scoreboards.get(n);
        if (board != null) {
            player.setScoreboard(board);
            
            if (Verteiler.teamA.containsKey(n) && Verteiler.teamA.get(n).contains(player)) {
                Team team = teamA.get(n);
                if (team != null) {
                    team.addEntry(player.getName());
                }
            } else {
                Team team = teamB.get(n);
                if (team != null) {
                    team.addEntry(player.getName());
                }
            }
        }
    }

    public static void removePlayerFromTeam(Player player, int n) {
        if (scoreboards.containsKey(n)) {
            Scoreboard board = scoreboards.get(n);
            if (board != null) {
                Team teamA = ColorTeam.teamA.get(n);
                Team teamB = ColorTeam.teamB.get(n);

                if (teamA != null) {
                    teamA.removeEntry(player.getName());
                }
                if (teamB != null) {
                    teamB.removeEntry(player.getName());
                }

                // Reset player's display name and team
                player.setDisplayName(player.getName());
                player.setPlayerListName(player.getName());

                // Set a new scoreboard to clear any team settings
                player.setScoreboard(Objects.requireNonNull(Bukkit.getScoreboardManager()).getNewScoreboard());
            }
        }
    }

    public static void updateTeamColor(int n, boolean isTeamA, String color) {
        Team team;
        if (isTeamA) {
            team = teamA.get(n);
        } else {
            team = teamB.get(n);
        }
        if (team != null) {
            team.setColor(ChatColor.valueOf(color));
        }
    }

    public static void clearScoreboard(int n) {
        if (scoreboards.containsKey(n)) {
            Scoreboard board = scoreboards.get(n);
            if (board != null) {
                Team teamToUnregisterA = teamA.get(n);
                Team teamToUnregisterB = teamB.get(n);

                if (teamToUnregisterA != null) {
                    teamToUnregisterA.unregister();
                }
                if (teamToUnregisterB != null) {
                    teamToUnregisterB.unregister();
                }
            }
            scoreboards.remove(n);
            teamA.remove(n);
            teamB.remove(n);
        }
    }
}
