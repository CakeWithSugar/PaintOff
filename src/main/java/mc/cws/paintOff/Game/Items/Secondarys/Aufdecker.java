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
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Aufdecker {
    private static PaintOffMain plugin;
    public static void setPlugin(PaintOffMain plugin) {Aufdecker.plugin = plugin;}
    public static int verbrauch = 180;

    public static void getItem(Player player) {
        ItemStack carrot = new ItemStack(Material.BEACON, 1);
        ItemMeta meta = carrot.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(ChatColor.DARK_PURPLE + "Aufdecker");
            List<String> lore = new ArrayList<>();
            lore.add(ChatColor.GRAY + "Wirft einen Aufdecker.");
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
            snowball.setVelocity(player.getLocation().getDirection().multiply(1.0));
            snowball.setMetadata("spawnedBy", new FixedMetadataValue(plugin, "Aufdecker"));

            // Create a task to show particles while flying
            Bukkit.getScheduler().runTaskTimer(plugin, () -> {
                if (snowball.isDead() || !snowball.isValid()) {
                    return;
                }
                Location loc = snowball.getLocation();
                snowball.getWorld().spawnParticle(Particle.PORTAL, loc, 6, 0.1, 0.1, 0.1, 0.1);
                snowball.getWorld().spawnParticle(Particle.CRIT, loc, 1, 0.1, 0.1, 0.1, 0.1);
            }, 0, 1);

            // Handle fuel consumption
            FulePoints.removeFulePoints(player, verbrauch);
        }
    }

    public static void phaseOne(Block hitBlock,Player player, String color) {
        int n = Start.getQueueNumber(player);
        Location hitLocation = hitBlock.getLocation();
        if (Verteiler.teamB.get(n).contains(player)) {
            for (Player targetPlayer : Verteiler.teamA.get(n)) {
                Location playerLoc = targetPlayer.getLocation();
                if (playerLoc.distance(hitLocation) <= 3) {
                    targetPlayer.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, 60, 0));
                }
            }
        } else if (Verteiler.teamA.get(n).contains(player)) {
            for (Player targetPlayer : Verteiler.teamB.get(n)) {
                Location playerLoc = targetPlayer.getLocation();
                if (playerLoc.distance(hitLocation) <= 3) {
                    targetPlayer.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, 60, 0));
                }
            }
        }
        hitLocation.getWorld().spawnParticle(Particle.FIREWORK, hitLocation.add(0, 1, 0), 10, 0, 0, 0, 0.5);
        hitLocation.getWorld().playSound(hitLocation, Sound.BLOCK_BEACON_ACTIVATE, 1.0f, 1.0f);
        Painter.paintBlockWithUltpoint(hitBlock,player,n,color);
    }
}
