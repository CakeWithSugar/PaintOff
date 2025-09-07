package mc.cws.paintOff.Game.Items.Ultimates;

import mc.cws.paintOff.Game.Extras.Painter;
import mc.cws.paintOff.Game.Management.InGame.Game;
import mc.cws.paintOff.Game.Resources.UltPoints;
import mc.cws.paintOff.Game.Management.Start;
import mc.cws.paintOff.Game.Management.Stop;
import mc.cws.paintOff.Game.Management.Verteiler;
import mc.cws.paintOff.PaintOffMain;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class Lauffeuer {
    private static PaintOffMain plugin;
    public static void setPlugin(PaintOffMain plugin) {Lauffeuer.plugin = plugin;}
    public static int requiredPoints = 0;
    public static int reichweite = 3;
    public static long dauer = 8;

    public static void getItem(Player player) {
        ItemStack carrot = new ItemStack(Material.HONEYCOMB, 1);
        ItemMeta meta = carrot.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(ChatColor.DARK_PURPLE + "Lauffeuer");
            List<String> lore = new ArrayList<>();
            lore.add(ChatColor.GRAY + "Wandle dich zu einem Lauffeuer um.");
            lore.add("");
            lore.add(ChatColor.GRAY + "Reichweite: " + ChatColor.GOLD + reichweite + ChatColor.DARK_GRAY + " Blöcke");
            lore.add(ChatColor.GRAY + "Dauer: " + ChatColor.GOLD + dauer + ChatColor.DARK_GRAY + " Sekunden");
            meta.setLore(lore);
            carrot.setItemMeta(meta);
        }
        player.getInventory().setItem(1, carrot);
    }

    public static void handleItemUsage(Player player) {
        if (UltPoints.hasEnoughUltPoints(player,UltPoints.getUltPoints(player))) {
            // Remove ult points
            requiredPoints = (UltPoints.getUltPoints(player));
            UltPoints.removeUltPoints(player,requiredPoints );
            AtomicBoolean stop = new AtomicBoolean(false);
            AtomicInteger i = new AtomicInteger((int) (dauer/2));
            Bukkit.getScheduler().runTaskTimer(plugin, () -> {
                if (stop.get()) {
                    return;
                }
                float addition = i.get()*0.5f;
                float soundVolume = 0.1f+i.get()*2;
                player.playSound(player.getLocation(), Sound.ENTITY_WARDEN_HEARTBEAT, soundVolume, addition);
                int current = i.decrementAndGet();
                if (current == 0) {
                    stop.set(true);
                }
            }, 0, 40);
            player.playSound(player.getLocation(), Sound.BLOCK_TRIAL_SPAWNER_OMINOUS_ACTIVATE, 2.0f, 0.25f);
            launch(player);
        }
    }

    public static void launch(Player player) {
        // Play custom sound
        player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_HAT, 0.1f, 2.0f);
        // Überprüfen, ob plugin nicht null ist
        if (plugin == null) {
            System.err.println("Plugin ist null in launchLauffeuer!");
            return;
        }
        // Add particle trail
        String colorPara = Painter.getColorPara(player);
        AtomicBoolean stop = new AtomicBoolean(false);
        int n = Start.getQueueNumber(player);
        String team = Verteiler.getTeam(player, n);
        player.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, (int) (Lauffeuer.dauer * 20), 0, false, false));
        player.addPotionEffect(new PotionEffect(PotionEffectType.RESISTANCE, (int) (Lauffeuer.dauer * 20), 0, false, true));
        if (team == null || !team.equals("A") && !team.equals("B")) {
            return;
        }
        String color = team.equals("A") ? Stop.getColorNameA(n) : Stop.getColorNameB(n);
        Game.cantUse.add(player);
        // Run explosion effect every 10 ticks (0.5 seconds) for the duration
        Bukkit.getScheduler().runTaskTimer(plugin, task -> {
            if (stop.get()) {
                task.cancel();
                return;
            }
            Painter.explosionAlgorithmWithoutUltpoint(player.getLocation().add(0,-1,0).getBlock(), player, n, (Lauffeuer.reichweite-1), color, 4);
            Verteiler.playColorParticleBubble(colorPara, player.getLocation(), 0.25, 5, 0.25, Particle.DUST);
        }, 0, 10);

        // Stop all effects after the duration
        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            stop.set(true);
            // Final particle effect when duration ends
            player.playSound(player.getLocation(), Sound.ENTITY_WARDEN_DEATH, 2.0f, 1.0f);
            player.getWorld().spawnParticle(Particle.END_ROD, player.getLocation(), 40, 0, 0, 0, 0.5);
            Game.cantUse.remove(player);
        }, 20L * Lauffeuer.dauer);
    }
}
