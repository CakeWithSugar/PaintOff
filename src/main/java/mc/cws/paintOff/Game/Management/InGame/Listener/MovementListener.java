package mc.cws.paintOff.Game.Management.InGame.Listener;

import mc.cws.paintOff.Game.Extras.Painter;
import mc.cws.paintOff.Game.Management.Start;
import mc.cws.paintOff.Game.Management.Stop;
import mc.cws.paintOff.Game.Management.Verteiler;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import static mc.cws.paintOff.Game.Management.InGame.Game.inGame;
import static mc.cws.paintOff.Game.Management.InGame.DamageDealer.playDeadSingle;

public class MovementListener implements Listener {

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        int n = Start.getQueueNumber(player);
        if (n == -1) return;
        String team = Verteiler.getTeam(player, n);
        if (team == null) return;
        if (!inGame.get(n).contains(player)) {return;}


        Block blockUnder = player.getLocation().getBlock().getRelative(0, -1, 0);
        Material blockType = blockUnder.getType();
        if (blockType == Material.valueOf(Stop.getColorNameA(n) + Painter.getBlockType()) ||
                blockType == Material.valueOf(Stop.getColorNameB(n) + Painter.getBlockType())) {
            String blockColor = blockType.name().replace(Painter.getBlockType(), "");
            boolean isEnemyColor = false;

            if (team.equals("A")) {
                isEnemyColor = blockColor.equals(Stop.getColorNameB(n));
            } else if (team.equals("B")) {
                isEnemyColor = blockColor.equals(Stop.getColorNameA(n));
            }
            if (isEnemyColor) {
                player.addPotionEffect(new PotionEffect(PotionEffectType.SLOWNESS, 1, 2, false, false));
            } else {
                player.removePotionEffect(PotionEffectType.SLOWNESS);
            }
        }

        Block block = player.getLocation().getBlock();
        if (block.getType() == Material.WATER ||
                block.getType() == Material.POWDER_SNOW ||
                block.getType() == Material.LAVA ||
                block.getType() == Material.CAMPFIRE ||
                block.getType() == Material.FIRE ||
                block.getType() == Material.SOUL_CAMPFIRE) {
            playDeadSingle(player, n);
        }
    }
}
