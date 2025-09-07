package mc.cws.paintOff.Game.Items.Ultimates;

import mc.cws.paintOff.Game.Resources.UltPoints;
import mc.cws.paintOff.Game.Management.Start;
import mc.cws.paintOff.Game.Management.Verteiler;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.List;

public class GenetischerBoost {
    public static int requiredPoints = 0;
    public static int lifecycle = 16;
    public static String effekte = "Waffenschaden+, XP-Regeneration+";

    public static void getItem(Player player) {
        ItemStack carrot = new ItemStack(Material.BLAZE_POWDER, 1);
        ItemMeta meta = carrot.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(ChatColor.DARK_PURPLE + "Genetischer Boost");
            List<String> lore = new ArrayList<>();
            lore.add(ChatColor.GRAY + "Löse für dein Team einen Boost aus.");
            lore.add("");
            lore.add(ChatColor.GRAY + "Verweildauer: " + ChatColor.GOLD + lifecycle + ChatColor.DARK_GRAY + " Sekunden");
            lore.add(ChatColor.GRAY + "Effekte: " + ChatColor.GOLD + effekte);
            meta.setLore(lore);
            carrot.setItemMeta(meta);
        }
        player.getInventory().setItem(1, carrot);
    }

    public static void handleItemUsage(Player player) {
        if (UltPoints.hasEnoughUltPoints(player,UltPoints.getUltPoints(player))) {
            // Remove ult points
            requiredPoints = (UltPoints.getUltPoints(player));
            UltPoints.removeUltPoints(player, requiredPoints );
            player.playSound(player.getLocation(), Sound.BLOCK_TRIAL_SPAWNER_ABOUT_TO_SPAWN_ITEM, 0.5f, 1f);
            player.playSound(player.getLocation(), Sound.BLOCK_BELL_RESONATE, 0.5f, 2f);
            launch(player);
        }
    }

    public static void launch(Player player) {
        // Play custom sound
        player.playSound(player.getLocation(), Sound.BLOCK_AMETHYST_BLOCK_RESONATE, 0.1f, 2.0f);
        int n = Start.getQueueNumber(player);
        player.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE,(GenetischerBoost.lifecycle * 20), 0, false, true));
        if (Verteiler.teamA.get(n).contains(player)) {
            for (Player p : Verteiler.teamA.get(n)) {
                p.addPotionEffect(new PotionEffect(PotionEffectType.STRENGTH,(GenetischerBoost.lifecycle * 20) , 0, false, true));
            }
        } else {
            for (Player p : Verteiler.teamB.get(n)) {
                p.addPotionEffect(new PotionEffect(PotionEffectType.STRENGTH,(GenetischerBoost.lifecycle * 20) , 0, false, true));
            }
        }
    }
}
