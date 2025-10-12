package mc.cws.paintOff.PrimarysWeapons.Pyrex;

import mc.cws.paintOff.Game.Extras.Painter;
import mc.cws.paintOff.Game.Management.InGame.SnowballEffect;
import mc.cws.paintOff.Game.Management.Stop;
import mc.cws.paintOff.Game.Management.InGame.Game;
import mc.cws.paintOff.Game.Resources.FulePoints;
import mc.cws.paintOff.Game.Resources.UltPoints;
import mc.cws.paintOff.Game.Management.Start;
import mc.cws.paintOff.Game.Management.Verteiler;
import mc.cws.paintOff.PaintOffMain;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.ArrayList;
import java.util.List;

public class PyrTwentySniplEx {
    private static PaintOffMain plugin;
    public static void setPlugin(PaintOffMain plugin) {PyrTwentySniplEx.plugin = plugin;}

    public static int requiredPoints = (UltPoints.generalPoints + 70);
    public static String reichweite = "24m";
    public static int verbrauch = 26;
    public static int explosion = 2;
    public static int kugeln = 1;
    public static int coolDown = 4;
    public static double coolDownCounter = coolDown/4.0;
    public static int slot = 32;
    public static int damage = 5;

    public static void getItem(Player player) {
        ItemStack carrot = new ItemStack(Material.NETHERITE_SHOVEL, 1);
        ItemMeta meta = carrot.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(ChatColor.DARK_PURPLE + "Pyrex 25er-SniplEx");
            List<String> lore = new ArrayList<>();
            lore.add(ChatColor.GRAY + "Braucht: " + ChatColor.YELLOW + requiredPoints + ChatColor.GRAY + " Ult-Punkte.");
            lore.add("");
            lore.add(ChatColor.DARK_PURPLE + "Reichweite: " + ChatColor.LIGHT_PURPLE + ChatColor.BOLD + reichweite);
            lore.add(ChatColor.DARK_PURPLE + "Verbrauch: " + ChatColor.LIGHT_PURPLE + ChatColor.BOLD + verbrauch + " Farbe/Schuss");
            lore.add(ChatColor.DARK_PURPLE + "Schaden: " + ChatColor.LIGHT_PURPLE + ChatColor.BOLD + damage + " / Schuss");
            lore.add(ChatColor.DARK_PURPLE + "Färberate: " + ChatColor.LIGHT_PURPLE + ChatColor.BOLD + kugeln + " Kugeln");
            lore.add(ChatColor.DARK_PURPLE + "Explosionsradius: " + ChatColor.LIGHT_PURPLE + ChatColor.BOLD + explosion + " Blöcke");
            lore.add(ChatColor.DARK_PURPLE + "CoolDown: " + ChatColor.LIGHT_PURPLE + ChatColor.BOLD + coolDownCounter + " Sekunden");
            meta.setLore(lore);
            carrot.setItemMeta(meta);
        }
        player.getInventory().setItem(5, carrot);
    }

    public static ItemStack arsenalDisplay() {
        ItemStack paintball = new ItemStack(Material.NETHERITE_SHOVEL, 1);
        ItemMeta meta = paintball.getItemMeta();
        meta.setDisplayName(ChatColor.YELLOW + "Pyrex 25er-SniplEx");
        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.GRAY + "Eine explosive Version des 25er-SniplEx!");
        lore.add(ChatColor.GRAY + "Braucht: " + ChatColor.YELLOW + requiredPoints + ChatColor.GRAY + " Ult-Punkte.");
        lore.add("");
        lore.add(ChatColor.DARK_PURPLE + "Reichweite: " + ChatColor.LIGHT_PURPLE + ChatColor.BOLD + reichweite);
        lore.add(ChatColor.DARK_PURPLE + "Verbrauch: " + ChatColor.LIGHT_PURPLE + ChatColor.BOLD + verbrauch + " Farbe/Schuss");
        lore.add(ChatColor.DARK_PURPLE + "Schaden: " + ChatColor.LIGHT_PURPLE + ChatColor.BOLD + damage + " / Schuss");
        lore.add(ChatColor.DARK_PURPLE + "Färberate: " + ChatColor.LIGHT_PURPLE + ChatColor.BOLD + kugeln + " Kugeln");
        lore.add(ChatColor.DARK_PURPLE + "Explosionsradius: " + ChatColor.LIGHT_PURPLE + ChatColor.BOLD + explosion + " Blöcke");
        lore.add(ChatColor.DARK_PURPLE + "CoolDown: " + ChatColor.LIGHT_PURPLE + ChatColor.BOLD + coolDownCounter + " Sekunden");
        lore.add("");
        lore.add(ChatColor.YELLOW + "Sekundärwaffe: " + ChatColor.LIGHT_PURPLE + "Reihenzieher");
        lore.add(ChatColor.YELLOW + "Spezialwaffe: " + ChatColor.LIGHT_PURPLE + "Glitzer Meteor");
        meta.setLore(lore);
        paintball.setItemMeta(meta);
        return paintball;
    }

    public static void shoot(Player player) {
        if (FulePoints.isAbleToPerform(player, verbrauch)) {
            int n = Start.getQueueNumber(player);
            String team = Verteiler.getTeam(player, n);
            if (team == null || !team.equals("A") && !team.equals("B")) {
                return;
            }

            String color = team.equals("A") ? Stop.getColorNameA(n) : Stop.getColorNameB(n);
            // Play custom sound
            player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_HAT, 0.4f, 2.0f);

            // Launch snowball
            Snowball snowball = player.launchProjectile(Snowball.class);
            snowball.setGravity(false);
            snowball.setVelocity(player.getLocation().getDirection().multiply(1.75));
            snowball.setMetadata("spawnedBy", new FixedMetadataValue(plugin, "Higher"));
            SnowballEffect.tri(snowball, player, Start.getQueueNumber(player));

            Bukkit.getScheduler().runTaskTimer(plugin, () -> {
                if (snowball.isDead() || !snowball.isValid()) {
                    return;
                }
                Location loc = snowball.getLocation();
                snowball.getWorld().spawnParticle(Particle.ASH, loc, 6, 0, 0, 0, 0.001);
            }, 0, 1);
            Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                int f = (explosion-1);
                if (snowball.isDead() || !snowball.isValid()) {
                    snowball.getWorld().playSound(snowball.getLocation(), Sound.ENTITY_DRAGON_FIREBALL_EXPLODE, 0.25f, 4.0f);
                    snowball.getWorld().spawnParticle(Particle.EXPLOSION, snowball.getLocation(), 1, 0, 0, 0, 0.005);
                    Painter.explosionAlgorithm(snowball.getLocation().getBlock(),player,n,f,color,2);
                    return;
                }
                snowball.getWorld().playSound(snowball.getLocation(), Sound.ENTITY_DRAGON_FIREBALL_EXPLODE, 0.25f, 4.0f);
                snowball.getWorld().spawnParticle(Particle.EXPLOSION, snowball.getLocation(), 1, 0, 0, 0, 0.005);
                Painter.explosionAlgorithm(snowball.getLocation().getBlock(),player,n,f,color,2);
                snowball.remove();
            }, (15));
            // Handle fuel consumption
            FulePoints.removeFulePoints(player,verbrauch);
            Game.setCooldown(player, coolDown);
        }
    }
}
