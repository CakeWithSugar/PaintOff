package mc.cws.paintOff.Game.Items.Secondarys;

import mc.cws.paintOff.Game.Resources.FulePoints;
import mc.cws.paintOff.Game.Management.InGame.SnowballEffect;
import mc.cws.paintOff.Game.Management.Start;
import mc.cws.paintOff.PaintOffMain;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Amalgamalge {
    private static PaintOffMain plugin;
    public static void setPlugin(PaintOffMain plugin) {Amalgamalge.plugin = plugin;}
    public static int verbrauch = 180;

    public static void getItem(Player player) {
        ItemStack carrot = new ItemStack(Material.SEAGRASS, 1);
        ItemMeta meta = carrot.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(ChatColor.DARK_PURPLE + "Amalgamalge");
            List<String> lore = new ArrayList<>();
            lore.add(ChatColor.GRAY + "Wirft eine Amalgamalge.");
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
            snowball.setVelocity(player.getLocation().getDirection().multiply(0.7));
            snowball.setMetadata("spawnedBy", new FixedMetadataValue(plugin, "Amalgam"));

            Bukkit.getScheduler().runTaskTimer(plugin, () -> {
                if (snowball.isDead() || !snowball.isValid()) {
                    return;
                }
                Location loc = snowball.getLocation();
                snowball.getWorld().spawnParticle(Particle.HAPPY_VILLAGER, loc, 2, 0.2, 0.2, 0.2, 0.1);
                snowball.getWorld().spawnParticle(Particle.CRIT, loc, 1, 0.1, 0.1, 0.1, 0.1);
            }, 0, 1);

            // Handle fuel consumption
            FulePoints.removeFulePoints(player, verbrauch);
        }
    }

    public static void phaseOne(Snowball snowball,Player player) {
        int n = Start.getQueueNumber(player);
        // Create a task to show particles while flying
        Location loc = snowball.getLocation().add(0, 1, 0);
        Snowball snowballChild = snowball.getWorld().spawn(snowball.getLocation(), Snowball.class);
        Snowball snowballChild2 = snowball.getWorld().spawn(snowball.getLocation(), Snowball.class);
        Snowball snowballChild3 = snowball.getWorld().spawn(snowball.getLocation(), Snowball.class);
        Snowball snowballChild4 = snowball.getWorld().spawn(snowball.getLocation(), Snowball.class);
        Snowball snowballChild5 = snowball.getWorld().spawn(snowball.getLocation(), Snowball.class);
        Snowball snowballChild6 = snowball.getWorld().spawn(snowball.getLocation(), Snowball.class);
        Snowball snowballChild7 = snowball.getWorld().spawn(snowball.getLocation(), Snowball.class);
        Snowball snowballChild8 = snowball.getWorld().spawn(snowball.getLocation(), Snowball.class);
        Snowball snowballChild9 = snowball.getWorld().spawn(snowball.getLocation(), Snowball.class);
        // Create a new vector with specific values for forward jumping
        Vector direction1 = new Vector(snowball.getVelocity().getX(), -snowball.getVelocity().getY() * 1.25, snowball.getVelocity().getZ()); // X, Y, Z components
        Vector direction2 = new Vector(snowball.getVelocity().getX(), -snowball.getVelocity().getY() * 1, snowball.getVelocity().getZ()); // X, Y, Z components
        Vector direction3 = new Vector(snowball.getVelocity().getX(), -snowball.getVelocity().getY() * 0.75, snowball.getVelocity().getZ()); // X, Y, Z components

        Vector leftDirection = direction3.clone().rotateAroundY(Math.toRadians(8)); // 15 Grad nach links
        snowballChild.setVelocity(leftDirection);
        snowballChild.setMetadata("spawnedBy", new FixedMetadataValue(plugin, "Lower"));
        snowballChild.setShooter(snowball.getShooter());

        Vector rightDirection = direction3.clone().rotateAroundY(Math.toRadians(-8)); // 15 Grad nach links
        snowballChild2.setVelocity(rightDirection);
        snowballChild2.setMetadata("spawnedBy", new FixedMetadataValue(plugin, "Lower"));
        snowballChild2.setShooter(snowball.getShooter());

        Vector leftDirection2 = direction3.clone().rotateAroundY(Math.toRadians(16)); // 15 Grad nach links
        snowballChild3.setVelocity(leftDirection2);
        snowballChild3.setMetadata("spawnedBy", new FixedMetadataValue(plugin, "Lower"));
        snowballChild3.setShooter(snowball.getShooter());
        SnowballEffect.sin(snowballChild3,player,n);

        Vector rightDirection2 = direction3.clone().rotateAroundY(Math.toRadians(-16)); // 15 Grad nach links
        snowballChild4.setVelocity(rightDirection2);
        snowballChild4.setMetadata("spawnedBy", new FixedMetadataValue(plugin, "Lower"));
        snowballChild4.setShooter(snowball.getShooter());
        SnowballEffect.sin(snowballChild4,player,n);

        Vector leftDirection3 = direction2.clone().rotateAroundY(Math.toRadians(8)); // 15 Grad nach links
        snowballChild5.setVelocity(leftDirection3);
        snowballChild5.setMetadata("spawnedBy", new FixedMetadataValue(plugin, "Lower"));
        snowballChild5.setShooter(snowball.getShooter());

        Vector rightDirection3 = direction2.clone().rotateAroundY(Math.toRadians(-8)); // 15 Grad nach links
        snowballChild6.setVelocity(rightDirection3);
        snowballChild6.setMetadata("spawnedBy", new FixedMetadataValue(plugin, "Lower"));
        snowballChild6.setShooter(snowball.getShooter());

        Vector leftDirection4 = direction1.clone().rotateAroundY(Math.toRadians(0)); // 15 Grad nach links
        snowballChild7.setVelocity(leftDirection4);
        snowballChild7.setMetadata("spawnedBy", new FixedMetadataValue(plugin, "Lower"));
        snowballChild7.setShooter(snowball.getShooter());
        SnowballEffect.sin(snowballChild7,player,n);

        Vector rightDirection5 = direction2.clone().rotateAroundY(Math.toRadians(0)); // 15 Grad nach links
        snowballChild8.setVelocity(rightDirection5);
        snowballChild8.setMetadata("spawnedBy", new FixedMetadataValue(plugin, "Lower"));
        snowballChild8.setShooter(snowball.getShooter());

        Vector leftDirection5 = direction3.clone().rotateAroundY(Math.toRadians(0)); // 15 Grad nach links
        snowballChild9.setVelocity(leftDirection5);
        snowballChild9.setMetadata("spawnedBy", new FixedMetadataValue(plugin, "Lower"));
        snowballChild9.setShooter(snowball.getShooter());

        snowball.getWorld().spawnParticle(Particle.SNOWFLAKE, loc, 1, 0, 0, 0, 0.005);
        Objects.requireNonNull(loc.getWorld()).playSound(loc, Sound.BLOCK_SLIME_BLOCK_HIT, 0.5f, 2.0f);
    }
}
