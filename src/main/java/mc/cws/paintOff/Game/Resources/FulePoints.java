package mc.cws.paintOff.Game.Resources;

import mc.cws.paintOff.Configuration;
import mc.cws.paintOff.Game.Management.Start;
import mc.cws.paintOff.Game.Management.Verteiler;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class FulePoints implements Listener {
    public static void giveFulePoint(Player player,int amount) {
        PotionEffect unluck = player.getPotionEffect(PotionEffectType.UNLUCK);
        if (unluck != null) {
            return;
        }
        for (int i = 0; i < amount; i++) {
            if (isAbleToCollect(player)) {
                Start.fule.put(player, Start.fule.get(player)+1);
            }
        }
    }

    public static void removeFulePoints(Player player, int cost) {
        int n = Start.getQueueNumber(player);
        String team = Verteiler.getTeam(player, n);
        if (team == null) return;

        int totalPoints = Start.fule.get(player);

        if (totalPoints >= cost) {
            Start.fule.put(player, totalPoints - cost);
        }
    }

    public static boolean isAbleToPerform(Player player, int cost) {
        int n = Start.getQueueNumber(player);
        String team = Verteiler.getTeam(player, n);
        if (team == null) return false;

        int totalPoints = Start.fule.get(player);
        return totalPoints >= cost;
    }

    public static boolean isAbleToCollect(Player player) {
        int n = Start.getQueueNumber(player);
        String team = Verteiler.getTeam(player, n);
        if (team == null) return false;

        int totalPoints = Start.fule.get(player);
        return totalPoints < Configuration.maxFulePoints;
    }
}
