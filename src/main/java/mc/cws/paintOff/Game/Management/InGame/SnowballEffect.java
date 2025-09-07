package mc.cws.paintOff.Game.Management.InGame;

import mc.cws.paintOff.Game.Extras.Painter;
import mc.cws.paintOff.Game.Management.Stop;
import mc.cws.paintOff.Game.Management.Verteiler;
import mc.cws.paintOff.PaintOffMain;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.data.type.Snow;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.Map;

public class SnowballEffect {
    private static PaintOffMain plugin;
    public static void setPlugin(PaintOffMain plugin) {
        SnowballEffect.plugin = plugin;}
    private static final Map<Snowball, BukkitTask> snowballTasks = new HashMap<>();

    public static void sin(Snowball snowball, Player player, int n) {
        String team = Verteiler.getTeam(player, n);
        if (team == null || !team.equals("A") && !team.equals("B")) {
            return;
        }

        String color = team.equals("A") ? Stop.getColorNameA(n) : Stop.getColorNameB(n);

        // Use a final wrapper class to hold the task
        final class TaskHolder {
            BukkitTask task;
        }
        TaskHolder holder = new TaskHolder();

        holder.task = Bukkit.getScheduler().runTaskTimer(plugin, () -> {
            // Get current position of snowball
            Location loc = snowball.getLocation();
            // Get the block under the snowball
            Block block = loc.getBlock().getRelative(0, -1, 0);
            Painter.paintBlockWithUltpoint(block,player,n,color);


            // If snowball is dead, cancel the task
            if (!snowball.isValid()) {
                holder.task.cancel();
                snowballTasks.remove(snowball);
            }
        }, 0L, 1L); // Run every tick

        snowballTasks.put(snowball, holder.task);
    }
    public static void dub(Snowball snowball, Player player, int n) {
        String team = Verteiler.getTeam(player, n);
        if (team == null || !team.equals("A") && !team.equals("B")) {
            return;
        }

        String color = team.equals("A") ? Stop.getColorNameA(n) : Stop.getColorNameB(n);

        // Use a final wrapper class to hold the task
        final class TaskHolder {
            BukkitTask task;
        }
        TaskHolder holder = new TaskHolder();

        holder.task = Bukkit.getScheduler().runTaskTimer(plugin, () -> {
            // Get current position of snowball
            Location loc = snowball.getLocation();
            // Get the block under the snowball
            Block block = loc.getBlock().getRelative(0, -1, 0);
            Painter.paintBlockWithUltpoint(block,player,n,color);

            Block blockUnder = loc.getBlock().getRelative(0, -2, 0);
            Painter.paintBlockWithUltpoint(blockUnder,player,n,color);

            // If snowball is dead, cancel the task
            if (!snowball.isValid()) {
                holder.task.cancel();
                snowballTasks.remove(snowball);
            }
        }, 0L, 1L); // Run every tick

        snowballTasks.put(snowball, holder.task);
    }
    public static void tri(Snowball snowball, Player player, int n) {
        String team = Verteiler.getTeam(player, n);
        if (team == null || !team.equals("A") && !team.equals("B")) {
            return;
        }

        String color = team.equals("A") ? Stop.getColorNameA(n) : Stop.getColorNameB(n);

        // Use a final wrapper class to hold the task
        final class TaskHolder {
            BukkitTask task;
        }
        TaskHolder holder = new TaskHolder();

        holder.task = Bukkit.getScheduler().runTaskTimer(plugin, () -> {
            // Get current position of snowball
            Location loc = snowball.getLocation();
            // Get the block under the snowball
            Block block = loc.getBlock().getRelative(0, -1, 0);
            Painter.paintBlockWithUltpoint(block,player,n,color);

            Block blockUnder = loc.getBlock().getRelative(0, -2, 0);
            Painter.paintBlockWithUltpoint(blockUnder,player,n,color);

            Block blockUnderThat = loc.getBlock().getRelative(0, -3, 0);
            Painter.paintBlockWithUltpoint(blockUnderThat,player,n,color);

            // If snowball is dead, cancel the task
            if (!snowball.isValid()) {
                holder.task.cancel();
                snowballTasks.remove(snowball);
            }
        }, 0L, 1L); // Run every tick

        snowballTasks.put(snowball, holder.task);
    }
}
