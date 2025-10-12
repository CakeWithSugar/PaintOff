package mc.cws.paintOff.Listener;

import mc.cws.paintOff.Game.Management.Start;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import static mc.cws.paintOff.Game.Management.InGame.Game.inGame;
import static mc.cws.paintOff.Game.Management.Queue.queuedPlayers;

public class BlockBreakListener implements Listener {
    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        int n = Start.getQueueNumber(player);
        if (n == -1) {return;}
        if (!inGame.get(n).contains(player) || !queuedPlayers.get(n).contains(player)) {return;}
        event.setCancelled(true);
    }
}
