package mc.cws.paintOff.Game.Management.InGame.Listener;

import mc.cws.paintOff.Game.Management.Start;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

import static mc.cws.paintOff.Game.Management.InGame.Game.inGame;
import static mc.cws.paintOff.Game.Management.Queue.queuedPlayers;

public class DamageListener implements Listener {
    @EventHandler
    public void onPlayerDamage(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player player)) return;
        int n = Start.getQueueNumber(player);
        if (n == -1) {return;}
        if (!inGame.get(n).contains(player) || !queuedPlayers.get(n).contains(player)) {return;}
        event.setCancelled(true);
    }
}
