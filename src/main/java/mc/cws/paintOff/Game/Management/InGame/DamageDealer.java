package mc.cws.paintOff.Game.Management.InGame;

import mc.cws.paintOff.Configuration;
import mc.cws.paintOff.Game.Arena.Arena;
import mc.cws.paintOff.Listener.ArsenalInventoryListener;
import mc.cws.paintOff.Game.Management.Start;
import mc.cws.paintOff.Game.Management.Verteiler;
import mc.cws.paintOff.Game.Points.Points;
import mc.cws.paintOff.Game.Resources.ExtraItems;
import mc.cws.paintOff.Po.TeleportInventoryListener;
import org.bukkit.ChatColor;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Objects;

import static mc.cws.paintOff.Game.Management.InGame.Game.*;

public class DamageDealer implements Listener {

    public static void dealDamage(Player player,Player hurter, double damage) {
        PotionEffect shield = player.getPotionEffect(PotionEffectType.RESISTANCE);
        if (shield != null) {
            return;
        }
        double health = player.getHealth();
        if (health - damage <= 0) {
            playDead(player, hurter, Start.getQueueNumber(player));
        } else {
            player.getWorld().playSound(player.getLocation(), Sound.ENTITY_PLAYER_HURT_SWEET_BERRY_BUSH, 0.5f, 1.0f);
            player.setHealth(health - damage);
        }
    }

    public static void playDead(Player player, Player killer, int n) {
        if (player == killer) {
            playDeadSingle(player,n);
        }
        player.addPotionEffect(new PotionEffect(PotionEffectType.DARKNESS,(2 * 20) , 0, false, false));
        getCauseOfDeath(player, killer, n);
        Points.givePoints(killer, 10);
        player.getWorld().spawnParticle(Particle.END_ROD, player.getLocation(), 20, 0.1, 0.1, 0.1, 0.05);
        Objects.requireNonNull(killer.getLocation().getWorld()).playSound(killer.getLocation(), Sound.ENTITY_IRON_GOLEM_REPAIR, 0.5f, 2.0f);
        Arena.portToArena(player, n);
        player.setHealth(20.0);
        player.getInventory().setItem(1, null);
        player.getInventory().setItem(5, null);
        player.getInventory().setItem(6, null);
        player.getInventory().setItem(7, null);
        ExtraItems.getKills(killer);
        Start.kills.put(killer, Start.kills.get(killer)+1);
        Game.controlKills(killer);
        for (int i = 0; i < Configuration.maxWaffen+1; i++) {
            if (Start.kitNumberBefore.get(i).contains(player)) {
                Start.kitNumberBefore.get(i).remove(player);
                break;
            }
        }
        if (Start.spawnSave1.contains(player)) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.RESISTANCE,(10 * 20) , 0, false, false));
        } else if (Start.spawnSave2.contains(player)) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.RESISTANCE,(12 * 20) , 0, false, false));
        } else if (Start.spawnSave3.contains(player)) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.RESISTANCE,(14 * 20) , 0, false, false));
        }
        Start.kitNumberBefore.get(ArsenalInventoryListener.getKitNumber(player)).add(player);
        player.getWorld().playSound(player.getLocation(), Sound.ENTITY_GHAST_DEATH, 0.5f, 1.0f);
        int kitNumber = ArsenalInventoryListener.getKitNumber(player);
        int kitNumberThen = kitNumber;
        for (int i = 0; i < Configuration.maxWaffen + 1; i++) {
            if (TeleportInventoryListener.kitChange.containsKey(i) && TeleportInventoryListener.kitChange.get(i).contains(player)) {
                kitNumberThen = i;
            }
        }
        if (kitNumber != kitNumberThen) {
            TeleportInventoryListener.kitChange.get(kitNumberThen).add(player);
            Start.ultpoints.put(player, 0);
        }
        ArsenalInventoryListener.getArsenal(player);
        player.getInventory().setItem(3, null);
    }
    public static void playDeadSingle(Player player, int n) {
        player.addPotionEffect(new PotionEffect(PotionEffectType.DARKNESS,(2 * 20) , 0, false, false));
        getSelfDeath(player, n);
        player.getWorld().spawnParticle(Particle.END_ROD, player.getLocation(), 20, 0.1, 0.1, 0.1, 0.05);
        Arena.portToArena(player, n);
        player.setHealth(20.0);
        player.getInventory().setItem(1, null);
        player.getInventory().setItem(5, null);
        player.getInventory().setItem(6, null);
        player.getInventory().setItem(7, null);
        for (int i = 0; i < Configuration.maxWaffen+1; i++) {
            if (Start.kitNumberBefore.get(i).contains(player)) {
                Start.kitNumberBefore.get(i).remove(player);
                break;
            }
        }
        Start.kitNumberBefore.get(ArsenalInventoryListener.getKitNumber(player)).add(player);
        player.getWorld().playSound(player.getLocation(), Sound.ENTITY_GHAST_DEATH, 0.5f, 1.0f);

        if (Start.spawnSave1.contains(player)) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.RESISTANCE,(10 * 20) , 0, false, false));
        } else if (Start.spawnSave2.contains(player)) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.RESISTANCE,(12 * 20) , 0, false, false));
        } else if (Start.spawnSave3.contains(player)) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.RESISTANCE,(14 * 20) , 0, false, false));
        }
        int kitNumber = ArsenalInventoryListener.getKitNumber(player);
        int kitNumberThen = kitNumber;
        for (int i = 0; i < Configuration.maxWaffen + 1; i++) {
            if (TeleportInventoryListener.kitChange.containsKey(i) && TeleportInventoryListener.kitChange.get(i).contains(player)) {
                kitNumberThen = i;
            }
        }
        if (kitNumber != kitNumberThen) {
            TeleportInventoryListener.kitChange.get(kitNumberThen).add(player);
            Start.ultpoints.put(player, 0);
        }
        ArsenalInventoryListener.getArsenal(player);
    }

    public static void getCauseOfDeath(Player victim, Player killer, int n) {
        String teamVictim = Verteiler.getTeam(victim, n);
        if (teamVictim == null || !teamVictim.equals("A") && !teamVictim.equals("B")) {
            return;
        }
        String teamKiller = Verteiler.getTeam(killer, n);
        if (teamKiller == null || !teamKiller.equals("A") && !teamKiller.equals("B")) {
            return;
        }
        String colorA = Verteiler.colorA[n];
        String colorB = Verteiler.colorB[n];
        if (teamVictim.equals("A") && (teamKiller.equals("B"))) {
            for (Player p : inGame.get(n)) {
                p.sendMessage(colorB + killer.getName() + ChatColor.GRAY + "  --> x  " + colorA + ChatColor.STRIKETHROUGH + victim.getName());
            }
        } else {
            for (Player p : inGame.get(n)) {
                p.sendMessage(colorA + killer.getName() + ChatColor.GRAY + "  --> x  " + colorB + ChatColor.STRIKETHROUGH + victim.getName());
            }
        }
    }
    public static void getSelfDeath(Player victim, int n) {
        String teamVictim = Verteiler.getTeam(victim, n);
        if (teamVictim == null || !teamVictim.equals("A") && !teamVictim.equals("B")) {
            return;
        }
        String colorA = Verteiler.colorA[n];
        String colorB = Verteiler.colorB[n];
        if (teamVictim.equals("A")) {
            for (Player p : inGame.get(n)) {
                p.sendMessage(ChatColor.GRAY + "--> x  " + colorA + ChatColor.STRIKETHROUGH + victim.getName());
            }
        } else {
            for (Player p : inGame.get(n)) {
                p.sendMessage(ChatColor.GRAY + "--> x  " + colorB + ChatColor.STRIKETHROUGH + victim.getName());
            }
        }
    }
}
