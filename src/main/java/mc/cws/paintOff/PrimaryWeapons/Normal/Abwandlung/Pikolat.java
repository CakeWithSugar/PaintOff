package mc.cws.paintOff.PrimaryWeapons.Normal.Abwandlung;

import mc.cws.paintOff.Game.Extras.Painter;
import mc.cws.paintOff.Game.Resources.FulePoints;
import mc.cws.paintOff.Game.Resources.UltPoints;
import mc.cws.paintOff.Game.Management.InGame.Game;
import mc.cws.paintOff.Game.Management.InGame.SnowballEffect;
import mc.cws.paintOff.Game.Management.Start;
import mc.cws.paintOff.Game.Management.Stop;
import mc.cws.paintOff.Game.Management.Verteiler;
import mc.cws.paintOff.PaintOffMain;
import org.bukkit.*;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.ArrayList;
import java.util.List;

public class Pikolat {
    private static PaintOffMain plugin;
    public static void setPlugin(PaintOffMain plugin) {Pikolat.plugin = plugin;}

    public static int requiredPoints = (UltPoints.generalPoints - 20);
    public static String reichweite = "5m";
    public static int verbrauch = 8;
    public static int kugeln = 1;
    public static int explosion = 1;
    public static int damage = 5;
    public static int coolDown = 2;
    public static double coolDownCounter = coolDown/4.0;
    public static int slot = 15;

    public static void getItem(Player player) {
        ItemStack carrot = new ItemStack(Material.DIAMOND_AXE, 1);
        ItemMeta meta = carrot.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(ChatColor.DARK_PURPLE + "Pikolat");
            List<String> lore = new ArrayList<>();
            lore.add(ChatColor.GRAY + "Braucht: " + ChatColor.YELLOW + requiredPoints + ChatColor.GRAY + " Ult-Punkte.");
            lore.add("");
            lore.add(ChatColor.DARK_PURPLE + "Reichweite: " + ChatColor.LIGHT_PURPLE + ChatColor.BOLD + reichweite);
            lore.add(ChatColor.DARK_PURPLE + "Verbrauch: " + ChatColor.LIGHT_PURPLE + ChatColor.BOLD + verbrauch + " Farbe/Schuss");
            lore.add(ChatColor.DARK_PURPLE + "max.Schaden: " + ChatColor.LIGHT_PURPLE + ChatColor.BOLD + damage + " / Schuss");
            lore.add(ChatColor.DARK_PURPLE + "Färberate: " + ChatColor.LIGHT_PURPLE + ChatColor.BOLD + kugeln +" Kugeln");
            lore.add(ChatColor.DARK_PURPLE + "Explosionsradius: " + ChatColor.LIGHT_PURPLE + ChatColor.BOLD + (explosion + 1));
            lore.add(ChatColor.DARK_PURPLE + "Cool-Down: " + ChatColor.LIGHT_PURPLE + ChatColor.BOLD + coolDownCounter + " Sekunden");
            meta.setLore(lore);
            carrot.setItemMeta(meta);
        }
        player.getInventory().setItem(5, carrot);
    }
    public static ItemStack arsenalDisplay() {
        ItemStack paintball = new ItemStack(Material.DIAMOND_AXE, 1);
        ItemMeta meta = paintball.getItemMeta();
        meta.setDisplayName(ChatColor.YELLOW + "Pikolat");
        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.GRAY + "Klein, süß und sehr gefährlich!");
        lore.add(ChatColor.GRAY + "Braucht: " + ChatColor.YELLOW + requiredPoints + ChatColor.GRAY + " Ult-Punkte.");
        lore.add("");
        lore.add(ChatColor.DARK_PURPLE + "Reichweite: " + ChatColor.LIGHT_PURPLE + ChatColor.BOLD + reichweite);
        lore.add(ChatColor.DARK_PURPLE + "Verbrauch: " + ChatColor.LIGHT_PURPLE + ChatColor.BOLD + verbrauch + " Farbe/Schuss");
        lore.add(ChatColor.DARK_PURPLE + "max.Schaden: " + ChatColor.LIGHT_PURPLE + ChatColor.BOLD + damage + " / Schuss");
        lore.add(ChatColor.DARK_PURPLE + "Färberate: " + ChatColor.LIGHT_PURPLE + ChatColor.BOLD + kugeln +" Kugeln");
        lore.add(ChatColor.DARK_PURPLE + "Explosionsradius: " + ChatColor.LIGHT_PURPLE + ChatColor.BOLD + (explosion + 1));
        lore.add(ChatColor.DARK_PURPLE + "Cool-Down: " + ChatColor.LIGHT_PURPLE + ChatColor.BOLD + coolDownCounter + " Sekunden");
        lore.add("");
        lore.add(ChatColor.YELLOW + "Sekundärwaffe: " + ChatColor.LIGHT_PURPLE + "Kreuzer");
        lore.add(ChatColor.YELLOW + "Spezialwaffe: " + ChatColor.LIGHT_PURPLE + "Tornedo");
        meta.setLore(lore);
        paintball.setItemMeta(meta);
        return paintball;
    }

    public static void shoot(Player player) {
        int n = Start.getQueueNumber(player);
        String color;
        if (Verteiler.teamA.get(Start.getQueueNumber(player)).contains(player)) {
            color = Stop.getColorNameA(n);
        } else if (Verteiler.teamB.get(Start.getQueueNumber(player)).contains(player)) {
            color = Stop.getColorNameB(n);
        } else {
            color = "white";
        }
        if (FulePoints.isAbleToPerform(player, verbrauch)) {
            // Play custom sound
            player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_HAT, 0.1f, 2.0f);

            // Launch snowball
            Snowball snowball = player.launchProjectile(Snowball.class);
            snowball.setGravity(false);
            snowball.setVelocity(player.getLocation().getDirection().multiply(1));
            snowball.setMetadata("spawnedBy", new FixedMetadataValue(plugin, "Lower"));
            Bukkit.getScheduler().runTaskLater(plugin, () -> {
                Painter.explosionAlgorithm(snowball.getLocation().getBlock().getRelative(BlockFace.DOWN), player, n, explosion, color, 4);
                Location loc = snowball.getLocation();
                snowball.getWorld().spawnParticle(Particle.EXPLOSION, loc, 1, 0, 0, 0, 0.005);
                snowball.getWorld().playSound(snowball.getLocation(), Sound.ENTITY_DRAGON_FIREBALL_EXPLODE, 0.1f, 4.0f);
                snowball.remove();
            }, 5L);

            // Start continuous effect
            SnowballEffect.dub(snowball, player, n);

            // Handle fuel consumption
            FulePoints.removeFulePoints(player,verbrauch);
            Game.setCooldown(player, coolDown);
        }
    }
}
