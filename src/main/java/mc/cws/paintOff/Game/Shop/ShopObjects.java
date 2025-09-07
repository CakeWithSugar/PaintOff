package mc.cws.paintOff.Game.Shop;

import mc.cws.paintOff.Game.Points.Points;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class ShopObjects {
    public static ItemStack createPointsShow(Player player) {
        ItemStack paintball = new ItemStack(Material.BOOK, 1);
        ItemMeta meta = paintball.getItemMeta();
        meta.setDisplayName(ChatColor.GREEN + "Deine Punkte: " + ChatColor.GOLD + Points.getPoints(player));
        List<String> lore = new ArrayList<>();
        lore.add("");
        lore.add(ChatColor.GRAY + "Verdine Punkte mit:");
        lore.add(ChatColor.GRAY + "- Spieler erledigen " + ChatColor.YELLOW + "+10" + ChatColor.GRAY + " Punkte");
        lore.add(ChatColor.GRAY + "- Spiel gewinnen " + ChatColor.YELLOW + "+25" + ChatColor.GRAY + " Punkte");
        lore.add(ChatColor.GRAY + "- Spiel verlieren " + ChatColor.YELLOW + "+5" + ChatColor.GRAY + " Punkte");
        meta.setLore(lore);
        paintball.setItemMeta(meta);
        return paintball;
    }

    public static ItemStack counter(Player player) {
        ItemStack paintball = new ItemStack(Material.MAP, 1);
        ItemMeta meta = paintball.getItemMeta();
        List<String> lore = new ArrayList<>();


        int roundsLeft = -1;

        // Durchsuche alle Runden
        for (int i = 0; i <= ShopInventory.DURATION_ROUNDS; i++) {
            if (Lists.hasBonus.get(i).contains(player)) {
                roundsLeft = i;
                break;
            }
        }
        // Wenn ein Booster gefunden wurde, zeige die verbleibenden Runden an
        assert meta != null;
        if (roundsLeft != -1) {
            // Wenn roundsLeft == DURATION_ROUNDS, dann ist es Runde 1 von DURATION_ROUNDS
            // ODER: DURATION_ROUNDS - roundsLeft + 1 je nach gewünschter Zählweise
            meta.setDisplayName(ChatColor.GREEN + "Booster Aktiv: " +
                    ChatColor.LIGHT_PURPLE + roundsLeft +
                    ChatColor.GREEN + "/" + ChatColor.LIGHT_PURPLE + ShopInventory.DURATION_ROUNDS + ChatColor.GREEN + " Runden");
            lore.add(ChatColor.GRAY + "Booster: " + ChatColor.GOLD + ShopInventory.lookForBooster(player));
            lore.add(ChatColor.RED + "§o" +"Nach einem Server restart wird der Booster automatisch gelöscht!");
        } else {
            meta.setDisplayName(ChatColor.RED + "Kein Booster aktiv!");
        }
        meta.setLore(lore);
        paintball.setItemMeta(meta);
        return paintball;
    }

    public static ItemStack createBoosterInventory() {
        ItemStack paintball = new ItemStack(Material.COOKIE, 1);
        ItemMeta meta = paintball.getItemMeta();
        meta.setDisplayName(ChatColor.GOLD + "Booster");
        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.GRAY + "Wähle ein Booster!");
        meta.setLore(lore);
        paintball.setItemMeta(meta);
        return paintball;
    }

    public static ItemStack createBack() {
        ItemStack paintball = new ItemStack(Material.BARRIER, 1);
        ItemMeta meta = paintball.getItemMeta();
        meta.setDisplayName(ChatColor.RED + "Zurück");
        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.GRAY + "Kehre zur vorherigen Seite zurück!");
        meta.setLore(lore);
        paintball.setItemMeta(meta);
        return paintball;
    }
    public static ItemStack createNull() {
        ItemStack paintball = new ItemStack(Material.RED_STAINED_GLASS_PANE, 1);
        ItemMeta meta = paintball.getItemMeta();
        meta.setDisplayName(ChatColor.RED + "In arbeit");
        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.GRAY + "Sei gespannt!");
        meta.setLore(lore);
        paintball.setItemMeta(meta);
        return paintball;
    }

    public static ItemStack createGlass() {
        ItemStack glass = new ItemStack(Material.GRAY_STAINED_GLASS_PANE, 1);
        ItemMeta meta = glass.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(" ");
            glass.setItemMeta(meta);
        }
        return glass;
    }

    // BOOSTER --------------------------------
    public static ItemStack points25(Player player) {
        ItemStack paintball = new ItemStack(Material.GOLD_NUGGET, 1);
        ItemMeta meta = paintball.getItemMeta();
        meta.setDisplayName(ChatColor.GOLD + "x1,25 Punkte Keks");
        List<String> lore = new ArrayList<>();
        // Check if player has any booster active
        boolean hasAnyBooster = false;
        for (int i = 0; i <= ShopInventory.DURATION_ROUNDS; i++) {
            if (Lists.hasBonus.get(i).contains(player)) {
                hasAnyBooster = true;
                break;
            }
        }

        if (hasAnyBooster) {
            lore.add(ChatColor.RED + "Du hast bereits einen Booster gekauft!");
        } else {
            lore.add(ChatColor.GRAY + "Kostet: " + ChatColor.GREEN + Preis.preis25 + ChatColor.GRAY +  " Punkte");
        }
        meta.setLore(lore);
        paintball.setItemMeta(meta);
        return paintball;
    }

    public static ItemStack points50(Player player) {
        ItemStack paintball = new ItemStack(Material.GOLD_INGOT, 1);
        ItemMeta meta = paintball.getItemMeta();
        meta.setDisplayName(ChatColor.GOLD + "x1,5 Punkte Keks");
        List<String> lore = new ArrayList<>();
        // Check if player has any booster active
        boolean hasAnyBooster = false;
        for (int i = 0; i <= ShopInventory.DURATION_ROUNDS; i++) {
            if (Lists.hasBonus.get(i).contains(player)) {
                hasAnyBooster = true;
                break;
            }
        }

        if (hasAnyBooster) {
            lore.add(ChatColor.RED + "Du hast bereits einen Booster gekauft!");
        } else {
            lore.add(ChatColor.GRAY + "Kostet: " + ChatColor.GREEN + Preis.preis50 + ChatColor.GRAY +  " Punkte");
        }
        meta.setLore(lore);
        paintball.setItemMeta(meta);
        return paintball;
    }

    public static ItemStack points100(Player player) {
        ItemStack paintball = new ItemStack(Material.GOLD_BLOCK, 1);
        ItemMeta meta = paintball.getItemMeta();
        assert meta != null;
        meta.setDisplayName(ChatColor.GOLD + "x2 Punkte Keks");
        List<String> lore = new ArrayList<>();

        // Check if player has any booster active
        boolean hasAnyBooster = false;
        for (int i = 0; i <= ShopInventory.DURATION_ROUNDS; i++) {
            if (Lists.hasBonus.get(i).contains(player)) {
                hasAnyBooster = true;
                break;
            }
        }

        if (hasAnyBooster) {
            lore.add(ChatColor.RED + "Du hast bereits einen Booster gekauft!");
        } else {
            lore.add(ChatColor.GRAY + "Kostet: " + ChatColor.GREEN + Preis.preis100 + ChatColor.GRAY +  " Punkte");
        }
        meta.setLore(lore);
        paintball.setItemMeta(meta);
        return paintball;
    }

    public static ItemStack recharge1(Player player) {
        ItemStack paintball = new ItemStack(Material.EXPERIENCE_BOTTLE, 1);
        ItemMeta meta = paintball.getItemMeta();
        assert meta != null;
        meta.setDisplayName(ChatColor.AQUA + "Aufladerate+");
        List<String> lore = new ArrayList<>();

        // Check if player has any booster active
        boolean hasAnyBooster = false;
        for (int i = 0; i <= ShopInventory.DURATION_ROUNDS; i++) {
            if (Lists.hasBonus.get(i).contains(player)) {
                hasAnyBooster = true;
                break;
            }
        }

        if (hasAnyBooster) {
            lore.add(ChatColor.RED + "Du hast bereits einen Booster gekauft!");
        } else {
            lore.add(ChatColor.GRAY + "Kostet: " + ChatColor.GREEN + Preis.basic + ChatColor.GRAY +  " Punkte");
        }
        meta.setLore(lore);
        paintball.setItemMeta(meta);
        return paintball;
    }

    public static ItemStack recharge2(Player player) {
        ItemStack paintball = new ItemStack(Material.EXPERIENCE_BOTTLE, 2);
        ItemMeta meta = paintball.getItemMeta();
        assert meta != null;
        meta.setDisplayName(ChatColor.AQUA + "Aufladerate++");
        List<String> lore = new ArrayList<>();

        // Check if player has any booster active
        boolean hasAnyBooster = false;
        for (int i = 0; i <= ShopInventory.DURATION_ROUNDS; i++) {
            if (Lists.hasBonus.get(i).contains(player)) {
                hasAnyBooster = true;
                break;
            }
        }

        if (hasAnyBooster) {
            lore.add(ChatColor.RED + "Du hast bereits einen Booster gekauft!");
        } else {
            lore.add(ChatColor.GRAY + "Kostet: " + ChatColor.GREEN + Preis.basic3 + ChatColor.GRAY +  " Punkte");
        }
        meta.setLore(lore);
        paintball.setItemMeta(meta);
        return paintball;
    }

    public static ItemStack speed1(Player player) {
        ItemStack paintball = new ItemStack(Material.RABBIT_FOOT, 1);
        ItemMeta meta = paintball.getItemMeta();
        assert meta != null;
        meta.setDisplayName(ChatColor.AQUA + "Eintauchgeschwindigkeit+");
        List<String> lore = new ArrayList<>();

        // Check if player has any booster active
        boolean hasAnyBooster = false;
        for (int i = 0; i <= ShopInventory.DURATION_ROUNDS; i++) {
            if (Lists.hasBonus.get(i).contains(player)) {
                hasAnyBooster = true;
                break;
            }
        }

        if (hasAnyBooster) {
            lore.add(ChatColor.RED + "Du hast bereits einen Booster gekauft!");
        } else {
            lore.add(ChatColor.GRAY + "Kostet: " + ChatColor.GREEN + Preis.basic2 + ChatColor.GRAY +  " Punkte");
        }
        meta.setLore(lore);
        paintball.setItemMeta(meta);
        return paintball;
    }
    public static ItemStack speed2(Player player) {
        ItemStack paintball = new ItemStack(Material.RABBIT_FOOT, 2);
        ItemMeta meta = paintball.getItemMeta();
        assert meta != null;
        meta.setDisplayName(ChatColor.AQUA + "Eintauchgeschwindigkeit++");
        List<String> lore = new ArrayList<>();

        // Check if player has any booster active
        boolean hasAnyBooster = false;
        for (int i = 0; i <= ShopInventory.DURATION_ROUNDS; i++) {
            if (Lists.hasBonus.get(i).contains(player)) {
                hasAnyBooster = true;
                break;
            }
        }

        if (hasAnyBooster) {
            lore.add(ChatColor.RED + "Du hast bereits einen Booster gekauft!");
        } else {
            lore.add(ChatColor.GRAY + "Kostet: " + ChatColor.GREEN + Preis.basic3 + ChatColor.GRAY +  " Punkte");
        }
        meta.setLore(lore);
        paintball.setItemMeta(meta);
        return paintball;
    }
    public static ItemStack protection1(Player player) {
        ItemStack paintball = new ItemStack(Material.IRON_CHESTPLATE, 1);
        ItemMeta meta = paintball.getItemMeta();
        assert meta != null;
        meta.setDisplayName(ChatColor.AQUA + "Spawnschutz");
        List<String> lore = new ArrayList<>();

        // Check if player has any booster active
        boolean hasAnyBooster = false;
        for (int i = 0; i <= ShopInventory.DURATION_ROUNDS; i++) {
            if (Lists.hasBonus.get(i).contains(player)) {
                hasAnyBooster = true;
                break;
            }
        }

        if (hasAnyBooster) {
            lore.add(ChatColor.RED + "Du hast bereits einen Booster gekauft!");
        } else {
            lore.add(ChatColor.GRAY + "Kostet: " + ChatColor.GREEN + Preis.basic + ChatColor.GRAY +  " Punkte");
        }
        meta.setLore(lore);
        paintball.setItemMeta(meta);
        return paintball;
    }
    public static ItemStack protection2(Player player) {
        ItemStack paintball = new ItemStack(Material.DIAMOND_CHESTPLATE, 1);
        ItemMeta meta = paintball.getItemMeta();
        assert meta != null;
        meta.setDisplayName(ChatColor.AQUA + "Spawnschutz+");
        List<String> lore = new ArrayList<>();

        // Check if player has any booster active
        boolean hasAnyBooster = false;
        for (int i = 0; i <= ShopInventory.DURATION_ROUNDS; i++) {
            if (Lists.hasBonus.get(i).contains(player)) {
                hasAnyBooster = true;
                break;
            }
        }

        if (hasAnyBooster) {
            lore.add(ChatColor.RED + "Du hast bereits einen Booster gekauft!");
        } else {
            lore.add(ChatColor.GRAY + "Kostet: " + ChatColor.GREEN + Preis.basic2 + ChatColor.GRAY +  " Punkte");
        }
        meta.setLore(lore);
        paintball.setItemMeta(meta);
        return paintball;
    }
    public static ItemStack protection3(Player player) {
        ItemStack paintball = new ItemStack(Material.NETHERITE_CHESTPLATE, 1);
        ItemMeta meta = paintball.getItemMeta();
        assert meta != null;
        meta.setDisplayName(ChatColor.AQUA + "Spawnschutz++");
        List<String> lore = new ArrayList<>();

        // Check if player has any booster active
        boolean hasAnyBooster = false;
        for (int i = 0; i <= ShopInventory.DURATION_ROUNDS; i++) {
            if (Lists.hasBonus.get(i).contains(player)) {
                hasAnyBooster = true;
                break;
            }
        }

        if (hasAnyBooster) {
            lore.add(ChatColor.RED + "Du hast bereits einen Booster gekauft!");
        } else {
            lore.add(ChatColor.GRAY + "Kostet: " + ChatColor.GREEN + Preis.basic3 + ChatColor.GRAY +  " Punkte");
        }
        meta.setLore(lore);
        paintball.setItemMeta(meta);
        return paintball;
    }
}
