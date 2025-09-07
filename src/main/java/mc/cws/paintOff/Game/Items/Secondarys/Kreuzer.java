package mc.cws.paintOff.Game.Items.Secondarys;

import mc.cws.paintOff.Game.Extras.Painter;
import mc.cws.paintOff.Game.Resources.FulePoints;
import mc.cws.paintOff.Game.Management.InGame.Game;
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
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

public class Kreuzer {
    private static PaintOffMain plugin;
    public static void setPlugin(PaintOffMain plugin) {Kreuzer.plugin = plugin;}
    public static int verbrauch = 100;

    public static void getItem(Player player) {
        ItemStack carrot = new ItemStack(Material.BREEZE_ROD, 1);
        ItemMeta meta = carrot.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(ChatColor.DARK_PURPLE + "Kreuzer");
            List<String> lore = new ArrayList<>();
            lore.add(ChatColor.GRAY + "Wirft eine Kreuzbombe.");
            lore.add("");
            lore.add(ChatColor.DARK_PURPLE + "Verbrauch: " + ChatColor.LIGHT_PURPLE + ChatColor.BOLD + verbrauch + " Farbe/Schuss");
            meta.setLore(lore);
            carrot.setItemMeta(meta);
        }
        player.getInventory().setItem(6, carrot);
    }

    public static void shoot(Player player) {
        if (FulePoints.isAbleToPerform(player, verbrauch)) {
            // Play custom sound
            player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_HAT, 0.1f, 2.0f);
            // Shoot snowball
            Snowball snowball = player.launchProjectile(Snowball.class);
            snowball.setVelocity(player.getLocation().getDirection().multiply(0.8));
            snowball.setMetadata("spawnedBy", new FixedMetadataValue(plugin, "Kreuzbombe"));

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

    public static void phaseOne(Snowball snowball, Block hitBlock, Player player, String color) {
        int n = Start.getQueueNumber(player);
        Location loc = snowball.getLocation().add(0, 1, 0);
        Snowball snowballChild = snowball.getWorld().spawn(snowball.getLocation(), Snowball.class);
        Vector direction = snowball.getLocation().getDirection();
        direction.setY(0.5); // Set Y component to 1.0 for upward motion
        direction.setX(0); // Set X component to 0
        direction.setZ(0); // Set Z component to 0
        snowballChild.setVelocity(direction);
        snowballChild.setMetadata("spawnedBy", new FixedMetadataValue(plugin, "KreuzbombeChild"));
        snowballChild.setShooter(snowball.getShooter());
        snowball.getWorld().spawnParticle(Particle.SONIC_BOOM, loc, 1, 0, 0, 0, 0.005);
        hitBlock.getWorld().playSound(hitBlock.getLocation(), Sound.BLOCK_SLIME_BLOCK_HIT, 1.0f, 2.0f);
        Painter.paintBlockWithUltpoint(hitBlock,player,n,color);
    }

    public static void phaseTwo(Block hitBlock,Player player,String color, int multiplicator) {
        int n = Start.getQueueNumber(player);
        Block targetBlock = hitBlock;
        for (int x = -3; x <= 3; x++) {
            for (int y = -3; y <= 3; y++) {
                for (int z = -3; z <= 3; z++) {
                    // Add vertical blocks (x-axis)
                    if (x == 0 || z == 0) {  // Skip the center block which is already added
                        targetBlock = hitBlock.getRelative(x, y, z);
                    }
                    if (Verteiler.teamA.get(n).contains(player)) {
                        for (Player players : Verteiler.teamB.get(n)) {
                            Location playerLoc = players.getLocation();
                            if (playerLoc.getBlockX() == targetBlock.getX() &&
                                    playerLoc.getBlockY() == targetBlock.getY() &&
                                    playerLoc.getBlockZ() == targetBlock.getZ()) {
                                Game.dealDamage(players,player, multiplicator + 2);
                            }
                        }
                    } else if (Verteiler.teamB.get(n).contains(player)) {
                        for (Player players : Verteiler.teamA.get(n)) {
                            Location playerLoc = players.getLocation();
                            if (playerLoc.getBlockX() == targetBlock.getX() &&
                                    playerLoc.getBlockY() == targetBlock.getY() &&
                                    playerLoc.getBlockZ() == targetBlock.getZ()) {
                                Game.dealDamage(players,player, multiplicator + 2);
                            }
                        }
                    }
                    Painter.paintBlockWithUltpoint(targetBlock,player,n,color);
                }
            }
        }
        // Add visual effects
        hitBlock.getWorld().spawnParticle(Particle.FLASH, hitBlock.getLocation(), 3, 0, 1, 0, 0.1);
        // Play sound effect
        hitBlock.getWorld().playSound(hitBlock.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 0.5f, 4.0f);
    }
}
