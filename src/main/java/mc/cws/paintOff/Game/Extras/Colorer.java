package mc.cws.paintOff.Game.Extras;

import mc.cws.paintOff.Game.Management.Start;
import mc.cws.paintOff.Game.Management.Verteiler;
import org.bukkit.entity.Player;

import java.util.Objects;

public class Colorer {
    public static String getTeamColor(Player player, boolean oposite) {
        int n = Start.getQueueNumber(player);
        String team = Verteiler.getTeam(player, n);
        String teamColor = "";
        if (!oposite) {
            if (Objects.equals(team, "A")) {
                teamColor = Verteiler.colorA[n];
            } else if (Objects.equals(team, "B")) {
                teamColor = Verteiler.colorB[n];
            }
        } else {
            if (Objects.equals(team, "A")) {
                teamColor = Verteiler.colorB[n];
            } else if (Objects.equals(team, "B")) {
                teamColor = Verteiler.colorA[n];
            }
        }
        return teamColor;
    }
}
