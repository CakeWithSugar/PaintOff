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

public class Gammablitzer {
    public static int requiredPoints = 0;
    public static int lifecycle = 14;
    public static String effekte = "Glowing";

    public static void getItem(Player player) {
        ItemStack carrot = new ItemStack(Material.NETHER_STAR, 1);
        ItemMeta meta = carrot.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(ChatColor.DARK_PURPLE + "Gammablitzer");
            List<String> lore = new ArrayList<>();
            lore.add(ChatColor.GRAY + "Dekt alle Gegner auf.");
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
            UltPoints.removeUltPoints(player, requiredPoints);
            player.playSound(player.getLocation(), Sound.ENTITY_BEE_POLLINATE, 1f, 0.5f);
            player.playSound(player.getLocation(), Sound.ENTITY_BEE_DEATH, 0.5f, 2f);
            player.playSound(player.getLocation(), Sound.EVENT_MOB_EFFECT_BAD_OMEN, 0.5f, 0.5f);
            launch(player);
        }
    }

    public static void launch(Player player) {
        // Play custom sound
        player.playSound(player.getLocation(), Sound.BLOCK_AMETHYST_BLOCK_RESONATE, 0.1f, 2.0f);
        int n = Start.getQueueNumber(player);
        if (Verteiler.teamA.get(n).contains(player)) {
            for (Player p : Verteiler.teamB.get(n)) {
                p.playSound(p.getLocation(), Sound.BLOCK_BELL_RESONATE, 0.1f, 2.0f);
                p.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING,(Gammablitzer.lifecycle * 20) , 0, false, false));
                p.playSound(p.getLocation(), Sound.EVENT_MOB_EFFECT_BAD_OMEN, 0.5f, 0.5f);
            }
        } else {
            for (Player p : Verteiler.teamA.get(n)) {
                p.playSound(p.getLocation(), Sound.BLOCK_BELL_RESONATE, 0.1f, 2.0f);
                p.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING,(Gammablitzer.lifecycle * 20) , 0, false, false));
                p.playSound(p.getLocation(), Sound.EVENT_MOB_EFFECT_BAD_OMEN, 0.5f, 0.5f);
            }
        }
    }
}
