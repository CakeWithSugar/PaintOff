package mc.cws.paintOff.Game.Items.Secondarys;

import mc.cws.paintOff.Game.Extras.Painter;
import mc.cws.paintOff.Game.Resources.FulePoints;
import mc.cws.paintOff.Game.Management.Start;
import mc.cws.paintOff.Game.Management.Verteiler;
import mc.cws.paintOff.PaintOffMain;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

public class Klotzmine {
    private static PaintOffMain plugin;
    public static void setPlugin(PaintOffMain plugin) {
        Klotzmine.plugin = plugin;}
    public static int verbrauch = 160;
    public static final List<Player> plantedMine = new ArrayList<>();

    public static void getItem(Player player) {
        ItemStack carrot = new ItemStack(Material.LIGHT_WEIGHTED_PRESSURE_PLATE, 1);
        ItemMeta meta = carrot.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(ChatColor.DARK_PURPLE + "Klotzmine");
            List<String> lore = new ArrayList<>();
            lore.add(ChatColor.GRAY + "Platziert eine Klotzmine.");
            lore.add("");
            lore.add(ChatColor.DARK_PURPLE + "Verbrauch: " + ChatColor.LIGHT_PURPLE + ChatColor.BOLD + verbrauch + " Farbe/Schuss");
            lore.add(ChatColor.DARK_PURPLE + "Besteht: " + ChatColor.LIGHT_PURPLE + ChatColor.BOLD + "30" + " Sekunden");
            lore.add(ChatColor.RED + "§o" +"Kann nur 1 mal aktiviert und platziert werden!");
            meta.setLore(lore);
            carrot.setItemMeta(meta);
        }
        player.getInventory().setItem(6, carrot);
    }

    public static void shoot(Player player) {
        if (FulePoints.isAbleToPerform(player, verbrauch) && !plantedMine.contains(player)) {
            // Play custom sound
            player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_HAT, 0.1f, 2.0f);
            // Shoot snowball
            Snowball snowball = player.launchProjectile(Snowball.class);
            snowball.setVelocity(player.getLocation().add(0, -1, 0).getDirection().multiply(0));
            snowball.setMetadata("spawnedBy", new FixedMetadataValue(plugin, "Klotzmine"));

            // Create a task to show particles while flying
            Bukkit.getScheduler().runTaskTimer(plugin, () -> {
                if (snowball.isDead() || !snowball.isValid()) {
                    return;
                }
                Location loc = snowball.getLocation();
                snowball.getWorld().spawnParticle(Particle.CRIT, loc, 1, 0.1, 0.1, 0.1, 0.1);
            }, 0, 1);

            // Handle fuel consumption
            FulePoints.removeFulePoints(player, verbrauch);
        }
    }

    public static void phaseOne(Snowball snowball, Block hitBlock, Player player, String color, String team) {
        int n = Start.getQueueNumber(player);
        plantedMine.add(player);
        AtomicBoolean stop = new AtomicBoolean(false);
        Bukkit.getScheduler().runTaskTimer(plugin, () -> {
            if (stop.get()) {
                return;
            }
            Location loc = snowball.getLocation();
            snowball.getWorld().spawnParticle(Particle.CRIT, loc.add(0,0.5,0), 5, 1, 0.1, 1, 0.1);
            if (team.equals("A")) {
                for (Player p : Verteiler.teamB.get(n)) {
                    Location playerLoc = p.getLocation().add(0, 1, 0); // Check at player's head level
                    if (Objects.requireNonNull(playerLoc.getWorld()).equals(loc.getWorld()) &&
                            playerLoc.distanceSquared(loc) <= 4) { // 3 blocks radius (using distanceSquared is more efficient)
                        Painter.explosionAlgorithm(hitBlock, player, n, 2, color, 4);
                        plantedMine.remove(player);
                        stop.set(true);
                        break; // Exit loop after finding first match
                    }
                }
            } else {
                for (Player p : Verteiler.teamA.get(n)) { // Note: Changed from teamA to teamB
                    Location playerLoc = p.getLocation().add(0, 1, 0);
                    if (Objects.requireNonNull(playerLoc.getWorld()).equals(loc.getWorld()) &&
                            playerLoc.distanceSquared(loc) <= 4) {
                        Painter.explosionAlgorithm(hitBlock, player, n, 2, color, 4);
                        plantedMine.remove(player);
                        stop.set(true);
                        break; // Exit loop after finding first match
                    }
                }
            }
        }, 0, 10);
        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            if (stop.get()) {
                return;
            }
            plantedMine.remove(player);
            stop.set(true);
        }, 20L*30);
    }
}
