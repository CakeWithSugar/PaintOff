package mc.cws.paintOff.Game.Resources;

import mc.cws.paintOff.Configuration;
import mc.cws.paintOff.Game.Items.Primarys.None.Abwandlung.FiftySnipEx;
import mc.cws.paintOff.Game.Items.Primarys.None.Abwandlung.Pikolat;
import mc.cws.paintOff.Game.Items.Primarys.Haepec.HaeSchnipsLehr;
import mc.cws.paintOff.Game.Items.Primarys.Prime.PrimeMillilat;
import mc.cws.paintOff.Game.Items.Primarys.Prime.PrimeSchweddler;
import mc.cws.paintOff.Game.Items.Primarys.Pyrex.PyrTwentySniplEx;
import mc.cws.paintOff.Game.Items.Primarys.None.*;
import mc.cws.paintOff.Game.Items.Primarys.Pyrex.PyrBlubber;
import mc.cws.paintOff.Game.Items.Primarys.Tulipa.TulQuinter;
import mc.cws.paintOff.Game.Items.Primarys.Tulipa.TulTriAtler;
import mc.cws.paintOff.Game.Management.Scoreboards.Scoreboards;
import mc.cws.paintOff.Game.Management.Start;
import mc.cws.paintOff.Game.Management.Verteiler;
import mc.cws.paintOff.Po.Modifications;
import org.bukkit.entity.Player;
import org.bukkit.Sound;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class UltPoints {
    public static int generalPoints = Configuration.generalUltPoints;

    public static void giveUltPoint(Player player) {
        PotionEffect fire = player.getPotionEffect(PotionEffectType.FIRE_RESISTANCE);
        if (fire != null) {
            return;
        }
        if (hasEnoughUltPoints(player, getUltPoints(player))) {
            return;
        }
        Start.ultpoints.put(player, Start.ultpoints.get(player)+1);
        Scoreboards.updateScoreboardGame(player);
    }

    public static void removeUltPoints(Player player, int cost) {
        int n = Start.getQueueNumber(player);
        String team = Verteiler.getTeam(player, n);
        if (team == null) return;

        int totalPoints = Start.ultpoints.get(player);

        if (totalPoints <= cost) {
            Start.ultpoints.put(player, totalPoints - cost);
            Scoreboards.updateScoreboardGame(player);
            Start.sound.put(player, true);
        }
    }

    public static boolean hasEnoughUltPoints(Player player,int cost) {
        int n = Start.getQueueNumber(player);
        String team = Verteiler.getTeam(player, n);
        if (team == null) return false;

        int totalPoints = Start.ultpoints.get(player);
        if (Start.sound.get(player) && totalPoints == cost) {
            player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
            Start.sound.put(player, false);
        }
        return totalPoints == cost;
    }

    public static int getUltPoints(Player player) {
        int n = -1;

        for (int i = 0; i <= Configuration.maxWaffen + 1; i++) {
            if (Start.kitNumberBefore.get(i) != null && Start.kitNumberBefore.get(i).contains(player)) {
                n = i;
                break;
            }
        }
        if (n == -1) return 0;
        if (Configuration.testbuild) {
            Configuration.unreleasedWeapons = 0;
        }
        int basePoints;
        if (n == 0) {
            basePoints = SchnipsLehr.requiredPoints;
        } else if (n == 1) {
            basePoints = TriAtler.requiredPoints;
        } else if (n == 2) {
            basePoints = Quinter.requiredPoints;
        } else if (n == 3) {
            basePoints = TulTriAtler.requiredPoints;
        } else if (n == 4) {
            basePoints = Schweddler.requiredPoints;
        } else if (n == 5) {
            basePoints = SniplEx.requiredPoints;
        } else if (n == 6) {
            basePoints = Blubber.requiredPoints;
        } else if (n == 7) {
            basePoints = FiftySnipEx.requiredPoints;
        } else if (n == 8) {
            basePoints = PrimeSchweddler.requiredPoints;
        } else if (n == 9) {
            basePoints = Mikrolat.requiredPoints;
        } else if (n == 10) {
            basePoints = TulQuinter.requiredPoints;
        } else if (n == 11) {
            basePoints = Pikolat.requiredPoints;
        } else if (n == 12) {
            basePoints = PyrBlubber.requiredPoints;
        } else if (n == 13) {
            basePoints = HaeSchnipsLehr.requiredPoints;
        } else if (n == 14) {
            basePoints = PyrTwentySniplEx.requiredPoints;
        } else if (n == 15) {
            basePoints = PrimeMillilat.requiredPoints;
        } else if (n == 16) {
            basePoints = Musket.requiredPoints;
        }  else if (n == Configuration.maxWaffen-Configuration.unreleasedWeapons+1) {  // Test weapon is kit 14 (maxWaffen+1)
            basePoints = Testing.requiredPoints;
        } else {
            return 0;
        }
        if (Modifications.searchMod("HalbierteUltpower")) {
            return (basePoints) / 2;
        }
        return basePoints;
    }

    public static int getUltWeaponByKit(Player player, int kit) {
        int n = -1;

        for (int i = 0; i <= Configuration.maxWaffen + 1; i++) {
            if (Start.kitNumberBefore.get(i) != null && Start.kitNumberBefore.get(i).contains(player)) {
                n = i;
                break;
            }
        }
        int basePoints;
        if (n == 0 || n == 8 || n == 11 || n == -1) { // Tronedo
            basePoints = 0;
        } else if (n == 1 || n == 13) { // Genboost
            basePoints = 1;
        } else if (n == 2 || n == 3) { // Wellenschlag
            basePoints = 2;
        } else if (n == 4 || n == 15) { // Sonnenwindler
            basePoints = 3;
        } else if (n == 5) { // Klotzhagel
            basePoints = 4;
        } else if (n == 6 || n == 10) { // Platzregen
            basePoints = 5;
        } else if (n == 7 || n == 9) { // Eruptor
            basePoints = 6;
        } else if (n == 12 || n == 16) { // Gammablitzer
            basePoints = 7;
        } else if (n == 14) { // Meteor
            basePoints = 8;
        }  else if (n == Configuration.maxWaffen-Configuration.unreleasedWeapons+1) {  // Test weapon is kit 14 (maxWaffen+1)
            basePoints = 9;
        } else {
            return 0;
        }
        return basePoints;
    }
}
