package mc.cws.paintOff.Ultimates;

import mc.cws.paintOff.Game.Extras.Painter;
import mc.cws.paintOff.Game.Resources.UltPoints;
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
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

public class Klotzhagel {
    private static PaintOffMain plugin;
    public static void setPlugin(PaintOffMain plugin) {
        Klotzhagel.plugin = plugin;}

    static final List<Snowball> snowballsXTerminer = new ArrayList<>();

    public static int requiredPoints = 0;
    public static int anzahl = 20;
    public static int maxHoehe  = 6;
    public static int minHoehe = 3;
    public static int radius = 3;
    public static double spawnTicks = 5;
    public static double fireTicks = 5;
    public static double speed = 1;
    public static int deleteTime = 10;

    public static void getItem(Player player) {
        ItemStack carrot = new ItemStack(Material.OMINOUS_TRIAL_KEY, 1);
        ItemMeta meta = carrot.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(ChatColor.DARK_PURPLE + "Klotzhagel");
            List<String> lore = new ArrayList<>();
            lore.add(ChatColor.GRAY + "Terminiere den Gegner!");
            lore.add("");
            lore.add(ChatColor.GRAY + "Anzahl der Klotzbomben: " + ChatColor.GOLD + anzahl + ChatColor.DARK_GRAY + " Blöcke");
            lore.add(ChatColor.GRAY + "Höhenspanne: " + ChatColor.GOLD + (maxHoehe - minHoehe) + ChatColor.DARK_GRAY + " Blöcke");
            lore.add(ChatColor.GRAY + "Spawnradius: " + ChatColor.GOLD + radius + ChatColor.DARK_GRAY + " Herzen");
            lore.add(ChatColor.GRAY + "Spawnrate: " + ChatColor.GOLD + "1 alle " + (spawnTicks/20) + ChatColor.DARK_GRAY + " Sekunden");
            lore.add(ChatColor.GRAY + "Feuerrate: " + ChatColor.GOLD + "1 alle " + (fireTicks/20) + ChatColor.DARK_GRAY + " Sekunden");
            lore.add(ChatColor.GRAY + "Geschwindigkeit: " + ChatColor.GOLD + speed + ChatColor.DARK_GRAY + " m/s");
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
            player.playSound(player.getLocation(), Sound.ENTITY_WITHER_AMBIENT, 0.5f, 0.75f);
            launch(player);
        }
    }

    public static void launch(Player player) {
        // Play custom sound
        player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_HAT, 0.1f, 2.0f);

        if (plugin == null) {
            System.err.println("Plugin ist null in launchXTerminer!");
            return;
        }

        // Erstelle einen Task, der alle 5 Ticks (0.25 Sekunden) einen neuen Snowball erstellt
        final AtomicReference<BukkitTask> taskRef = new AtomicReference<>();
        String colorPara = Painter.getColorPara(player);
        taskRef.set(Bukkit.getScheduler().runTaskTimer(plugin, () -> {
            if (snowballsXTerminer.size() == Klotzhagel.anzahl) {
                // Wenn wir die maximale Anzahl Schneebälle haben, feuere sie ab und beende den Task
                phaseOne(player);
                BukkitTask task = taskRef.get();
                if (task != null) {
                    task.cancel();
                }
                return;
            }

            // Erstelle einen Snowball in Kreisform um den Spieler
            Location playerLoc = player.getLocation();
            double radius = Klotzhagel.radius; // Radius des Kreises
            double angle = (2 * Math.PI / Klotzhagel.anzahl) * snowballsXTerminer.size(); // Gleichmäßig verteilter Winkel
            double x = Math.cos(angle) * radius;
            double z = Math.sin(angle) * radius;

            // Wähle eine zufällige Höhe zwischen 0 und XTerminer.hoehe
            double randomHeight = ThreadLocalRandom.current().nextDouble(Klotzhagel.minHoehe, Klotzhagel.maxHoehe);

            // Erstelle den Snowball an der berechneten Position
            Location spawnLoc = playerLoc.clone().add(x, randomHeight, z);
            Snowball snowball = spawnLoc.getWorld().spawn(spawnLoc, Snowball.class);
            snowball.setShooter(player);

            // Deaktiviere sofort die Bewegung
            snowball.setGravity(false);
            snowball.setVelocity(new Vector(0, 0, 0));

            snowball.setMetadata("spawnedBy", new FixedMetadataValue(plugin, "Hagel"));

            // Füge den Snowball zur Liste hinzu
            snowballsXTerminer.add(snowball);
            for (Snowball sno : snowballsXTerminer) {
                Location loc = sno.getLocation();
                Verteiler.playColorParticleBubble(colorPara, loc, 0.1, 1, 0.1, Particle.DUST);
            }
        }, 0, (long) Klotzhagel.spawnTicks));

        // Partikelfolge für alle Snowballs
        Bukkit.getScheduler().runTaskTimer(plugin, () -> {
            if (snowballsXTerminer.size() == Klotzhagel.anzahl) {
                for (Snowball sno : snowballsXTerminer) {
                    Location loc = sno.getLocation();
                    Verteiler.playColorParticleBubble(colorPara, loc, 0.1, 1, 0.1, Particle.DUST);
                }
            }

        }, 0, 1);
    }

    public static void phaseOne(Player player) {
        if (snowballsXTerminer.isEmpty()) {
            return;
        }
        Snowball snowball = snowballsXTerminer.get(0);

        Vector direction = player.getLocation().getDirection().normalize();
        snowball.setVelocity(direction.multiply(Klotzhagel.speed));

        snowball.getWorld().spawnParticle(Particle.FIREWORK, snowball.getLocation(), 5, 0.1, 0.1, 0.1, 0.5);
        snowball.setGlowing(true);
        snowball.setGravity(true);

        String colorPara = Painter.getColorPara(player);
        for (Snowball sno : snowballsXTerminer) {
            Location loc = sno.getLocation();
            sno.getWorld().spawnParticle(Particle.WITCH, loc, 2, 0, 0, 0, 0.01);
            Verteiler.playColorParticleBubble(colorPara, loc, 0.1, 3, 0.1, Particle.DUST);
        }

        player.playSound(player.getLocation(), Sound.ENTITY_ENDER_DRAGON_SHOOT, 0.1f, 3.0f);

        // Entferne ihn jetzt aus der Liste
        snowballsXTerminer.remove(0);

        // Plane den nächsten Schuss
        if (!snowballsXTerminer.isEmpty()) {
            Bukkit.getScheduler().runTaskLater(plugin, () -> phaseOne(player), (long) Klotzhagel.fireTicks);
        }

        // Plane Löschung nach Zeit
        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            snowball.remove();
        }, 20L * Klotzhagel.deleteTime);
    }

    public static void phaseTwo(Block hitBlock,Player player,String color,int multiplicator) {
        int n = Start.getQueueNumber(player);
        AtomicBoolean stop = new AtomicBoolean(false);
        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            if (stop.get()) {
                return;
            }
            Painter.explosionAlgorithmWithoutUltpoint(hitBlock, player, n, 2,color, multiplicator*3);
            hitBlock.getWorld().playSound(hitBlock.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 0.5f, 1.5f);
            if (!stop.get()) {
                stop.set(true);
            }
            hitBlock.getWorld().spawnParticle(Particle.FLAME, hitBlock.getLocation().add(0, 0.5, 0), 5, 0, 1, 0, 0.2);
        }, 20);
        Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, () -> {
            if (stop.get()) {
                return;
            }
            hitBlock.getWorld().spawnParticle(Particle.FIREWORK, hitBlock.getLocation().add(0, 0.5, 0), 5, 0, 0, 0, 0.1);
        },0,2);
    }
}
