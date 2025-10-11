package mc.cws.paintOff.Game.Shop;

import mc.cws.paintOff.Configuration;
import mc.cws.paintOff.Game.Points.Points;
import mc.cws.paintOff.PaintOffMain;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class ShopInventory implements Listener {
    private static PaintOffMain plugin; // Your plugin class
    public static void setPlugin(PaintOffMain plugin) {
        ShopInventory.plugin = plugin;
        // Initialize asynchronously after plugin is set
        if (!initializationScheduled) {
            initializationScheduled = true;
            Bukkit.getScheduler().runTask(plugin, () -> {
                try {
                    initialize();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }
    }
    public static final int DURATION_ROUNDS = 10; // 10 Runden Gültigkeit



    private static final String SHOP_TITLE = ChatColor.DARK_GRAY + "- Shop -";
    public static final Inventory Main = Bukkit.createInventory(null, 27, SHOP_TITLE);
    private static final String BOOSTER_TITLE = ChatColor.DARK_GRAY + "- Axol's Keks Hütte -";
    public static final Inventory Booster = Bukkit.createInventory(null, 45, BOOSTER_TITLE);

    private static boolean isInitialized = false;
    private static final Object initLock = new Object();
    private static volatile boolean initializationScheduled = false;

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getView().getTitle().equals(SHOP_TITLE)) {
            event.setCancelled(true);

            if (event.getWhoClicked() instanceof Player player) {
                int clickedSlot = event.getRawSlot();
                if (clickedSlot == 11) {
                    player.closeInventory();
                    player.openInventory(Booster);
                }
            }
        } else if (event.getView().getTitle().equals(BOOSTER_TITLE)) {
            event.setCancelled(true);

            if (event.getWhoClicked() instanceof Player player) {
                int clickedSlot = event.getRawSlot();
                if (clickedSlot == 40) {
                    player.closeInventory();
                    player.openInventory(Main);
                } else if (clickedSlot == 10) {
                    for (int i = 0; i <= DURATION_ROUNDS; i++) {
                        if (Lists.hasBonus.get(i).contains(player)) {
                            player.sendMessage(ChatColor.GRAY + "§o" + "~Keinen zweiten Keks~!");
                            player.playSound(player.getLocation(), Sound.ENTITY_AXOLOTL_HURT, 2.0f, 1.0f);
                            return;
                        }
                    }
                    if (Points.getPoints(player) > Preis.preis25) {
                        Points.addPoints(player, -Preis.preis25);
                        List<Player> liste = Lists.hasBonus.get(DURATION_ROUNDS);
                        liste.add(player);
                        List<Player> bonus = Lists.bonus25.get(DURATION_ROUNDS);
                        bonus.add(player);
                        setup(player);

                        player.sendMessage(ChatColor.GRAY + "§o" + "~Vielen dank für die Bestellung~!");
                        player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.0f);
                        player.closeInventory();
                    } else {
                        player.sendMessage(ChatColor.GRAY + "§o" + "~Keine Punkte, keine Kekse~!");
                        player.playSound(player.getLocation(), Sound.ENTITY_AXOLOTL_DEATH, 1.0f, 1.0f);
                    }
                } else if (clickedSlot == 19) {
                    for (int i = 0; i <= DURATION_ROUNDS; i++) {
                        if (Lists.hasBonus.get(i).contains(player)) {
                            player.sendMessage(ChatColor.GRAY + "§o" + "~Keinen zweiten Keks~!");
                            player.playSound(player.getLocation(), Sound.ENTITY_AXOLOTL_HURT, 2.0f, 1.0f);
                            return;
                        }
                    }
                    if (Points.getPoints(player) > Preis.preis50) {
                        Points.addPoints(player, -Preis.preis50);
                        List<Player> liste = Lists.hasBonus.get(DURATION_ROUNDS);
                        liste.add(player);
                        List<Player> bonus = Lists.bonus50.get(DURATION_ROUNDS);
                        bonus.add(player);
                        setup(player);

                        player.sendMessage(ChatColor.GRAY + "§o" + "~Vielen dank für die Bestellung~!");
                        player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.0f);
                        player.closeInventory();
                    } else {
                        player.sendMessage(ChatColor.GRAY + "§o" + "~Keine Punkte, keine Kekse~!");
                        player.playSound(player.getLocation(), Sound.ENTITY_AXOLOTL_DEATH, 1.0f, 1.0f);
                    }
                } else if (clickedSlot == 28) {
                    for (int i = 0; i <= DURATION_ROUNDS; i++) {
                        if (Lists.hasBonus.get(i).contains(player)) {
                            player.sendMessage(ChatColor.GRAY + "§o" + "~Keinen zweiten Keks~!");
                            player.playSound(player.getLocation(), Sound.ENTITY_AXOLOTL_HURT, 2.0f, 1.0f);
                            return;
                        }
                    }
                    if (Points.getPoints(player) > Preis.preis100) {
                        Points.addPoints(player, -Preis.preis100);
                        List<Player> liste = Lists.hasBonus.get(DURATION_ROUNDS);
                        liste.add(player);
                        List<Player> bonus = Lists.bonus100.get(DURATION_ROUNDS);
                        bonus.add(player);
                        setup(player);

                        player.sendMessage(ChatColor.GRAY + "§o" + "~Vielen dank für die Bestellung~!");
                        player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.0f);
                        player.closeInventory();
                    } else {
                        player.sendMessage(ChatColor.GRAY + "§o" + "~Keine Punkte, keine Kekse~!");
                        player.playSound(player.getLocation(), Sound.ENTITY_AXOLOTL_DEATH, 1.0f, 1.0f);
                    }
                } else if (clickedSlot == 12) {
                    for (int i = 0; i <= DURATION_ROUNDS; i++) {
                        if (Lists.hasBonus.get(i).contains(player)) {
                            player.sendMessage(ChatColor.GRAY + "§o" + "~Keinen zweiten Keks~!");
                            player.playSound(player.getLocation(), Sound.ENTITY_AXOLOTL_HURT, 2.0f, 1.0f);
                            return;
                        }
                    }
                    if (Points.getPoints(player) > Preis.basic) {
                        Points.addPoints(player, -Preis.basic);
                        List<Player> liste = Lists.hasBonus.get(DURATION_ROUNDS);
                        liste.add(player);
                        List<Player> bonus = Lists.recharge1.get(DURATION_ROUNDS);
                        bonus.add(player);
                        setup(player);

                        player.sendMessage(ChatColor.GRAY + "§o" + "~Vielen dank für die Bestellung~!");
                        player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.0f);
                        player.closeInventory();
                    } else {
                        player.sendMessage(ChatColor.GRAY + "§o" + "~Keine Punkte, keine Kekse~!");
                        player.playSound(player.getLocation(), Sound.ENTITY_AXOLOTL_DEATH, 1.0f, 1.0f);
                    }
                } else if (clickedSlot == 21) {
                    for (int i = 0; i <= DURATION_ROUNDS; i++) {
                        if (Lists.hasBonus.get(i).contains(player)) {
                            player.sendMessage(ChatColor.GRAY + "§o" + "~Keinen zweiten Keks~!");
                            player.playSound(player.getLocation(), Sound.ENTITY_AXOLOTL_HURT, 2.0f, 1.0f);
                            return;
                        }
                    }
                    if (Points.getPoints(player) > Preis.basic3) {
                        Points.addPoints(player, -Preis.basic3);
                        List<Player> liste = Lists.hasBonus.get(DURATION_ROUNDS);
                        liste.add(player);
                        List<Player> bonus = Lists.recharge2.get(DURATION_ROUNDS);
                        bonus.add(player);
                        setup(player);

                        player.sendMessage(ChatColor.GRAY + "§o" + "~Vielen dank für die Bestellung~!");
                        player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.0f);
                        player.closeInventory();
                    } else {
                        player.sendMessage(ChatColor.GRAY + "§o" + "~Keine Punkte, keine Kekse~!");
                        player.playSound(player.getLocation(), Sound.ENTITY_AXOLOTL_DEATH, 1.0f, 1.0f);
                    }
                } else if (clickedSlot == 13) {
                    for (int i = 0; i <= DURATION_ROUNDS; i++) {
                        if (Lists.hasBonus.get(i).contains(player)) {
                            player.sendMessage(ChatColor.GRAY + "§o" + "~Keinen zweiten Keks~!");
                            player.playSound(player.getLocation(), Sound.ENTITY_AXOLOTL_HURT, 2.0f, 1.0f);
                            return;
                        }
                    }
                    if (Points.getPoints(player) > Preis.basic2) {
                        Points.addPoints(player, -Preis.basic2);
                        List<Player> liste = Lists.hasBonus.get(DURATION_ROUNDS);
                        liste.add(player);
                        List<Player> bonus = Lists.speed1.get(DURATION_ROUNDS);
                        bonus.add(player);
                        setup(player);

                        player.sendMessage(ChatColor.GRAY + "§o" + "~Vielen dank für die Bestellung~!");
                        player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.0f);
                        player.closeInventory();
                    } else {
                        player.sendMessage(ChatColor.GRAY + "§o" + "~Keine Punkte, keine Kekse~!");
                        player.playSound(player.getLocation(), Sound.ENTITY_AXOLOTL_DEATH, 1.0f, 1.0f);
                    }
                } else if (clickedSlot == 22) {
                    for (int i = 0; i <= DURATION_ROUNDS; i++) {
                        if (Lists.hasBonus.get(i).contains(player)) {
                            player.sendMessage(ChatColor.GRAY + "§o" + "~Keinen zweiten Keks~!");
                            player.playSound(player.getLocation(), Sound.ENTITY_AXOLOTL_HURT, 2.0f, 1.0f);
                            return;
                        }
                    }
                    if (Points.getPoints(player) > Preis.basic3) {
                        Points.addPoints(player, -Preis.basic3);
                        List<Player> liste = Lists.hasBonus.get(DURATION_ROUNDS);
                        liste.add(player);
                        List<Player> bonus = Lists.speed2.get(DURATION_ROUNDS);
                        bonus.add(player);
                        setup(player);

                        player.sendMessage(ChatColor.GRAY + "§o" + "~Vielen dank für die Bestellung~!");
                        player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.0f);
                        player.closeInventory();
                    } else {
                        player.sendMessage(ChatColor.GRAY + "§o" + "~Keine Punkte, keine Kekse~!");
                        player.playSound(player.getLocation(), Sound.ENTITY_AXOLOTL_DEATH, 1.0f, 1.0f);
                    }
                } else if (clickedSlot == 14) {
                    for (int i = 0; i <= DURATION_ROUNDS; i++) {
                        if (Lists.hasBonus.get(i).contains(player)) {
                            player.sendMessage(ChatColor.GRAY + "§o" + "~Keinen zweiten Keks~!");
                            player.playSound(player.getLocation(), Sound.ENTITY_AXOLOTL_HURT, 2.0f, 1.0f);
                            return;
                        }
                    }
                    if (Points.getPoints(player) > Preis.basic) {
                        Points.addPoints(player, -Preis.basic);
                        List<Player> liste = Lists.hasBonus.get(DURATION_ROUNDS);
                        liste.add(player);
                        List<Player> bonus = Lists.protection1.get(DURATION_ROUNDS);
                        bonus.add(player);
                        setup(player);

                        player.sendMessage(ChatColor.GRAY + "§o" + "~Vielen dank für die Bestellung~!");
                        player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.0f);
                        player.closeInventory();
                    } else {
                        player.sendMessage(ChatColor.GRAY + "§o" + "~Keine Punkte, keine Kekse~!");
                        player.playSound(player.getLocation(), Sound.ENTITY_AXOLOTL_DEATH, 1.0f, 1.0f);
                    }
                } else if (clickedSlot == 23) {
                    for (int i = 0; i <= DURATION_ROUNDS; i++) {
                        if (Lists.hasBonus.get(i).contains(player)) {
                            player.sendMessage(ChatColor.GRAY + "§o" + "~Keinen zweiten Keks~!");
                            player.playSound(player.getLocation(), Sound.ENTITY_AXOLOTL_HURT, 2.0f, 1.0f);
                            return;
                        }
                    }
                    if (Points.getPoints(player) > Preis.basic2) {
                        Points.addPoints(player, -Preis.basic2);
                        List<Player> liste = Lists.hasBonus.get(DURATION_ROUNDS);
                        liste.add(player);
                        List<Player> bonus = Lists.protection2.get(DURATION_ROUNDS);
                        bonus.add(player);
                        setup(player);

                        player.sendMessage(ChatColor.GRAY + "§o" + "~Vielen dank für die Bestellung~!");
                        player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.0f);
                        player.closeInventory();
                    } else {
                        player.sendMessage(ChatColor.GRAY + "§o" + "~Keine Punkte, keine Kekse~!");
                        player.playSound(player.getLocation(), Sound.ENTITY_AXOLOTL_DEATH, 1.0f, 1.0f);
                    }
                } else if (clickedSlot == 32) {
                    for (int i = 0; i <= DURATION_ROUNDS; i++) {
                        if (Lists.hasBonus.get(i).contains(player)) {
                            player.sendMessage(ChatColor.GRAY + "§o" + "~Keinen zweiten Keks~!");
                            player.playSound(player.getLocation(), Sound.ENTITY_AXOLOTL_HURT, 2.0f, 1.0f);
                            return;
                        }
                    }
                    if (Points.getPoints(player) > Preis.basic3) {
                        Points.addPoints(player, -Preis.basic3);
                        List<Player> liste = Lists.hasBonus.get(DURATION_ROUNDS);
                        liste.add(player);
                        List<Player> bonus = Lists.protection3.get(DURATION_ROUNDS);
                        bonus.add(player);
                        setup(player);

                        player.sendMessage(ChatColor.GRAY + "§o" + "~Vielen dank für die Bestellung~!");
                        player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.0f);
                        player.closeInventory();
                    } else {
                        player.sendMessage(ChatColor.GRAY + "§o" + "~Keine Punkte, keine Kekse~!");
                        player.playSound(player.getLocation(), Sound.ENTITY_AXOLOTL_DEATH, 1.0f, 1.0f);
                    }
                }
            }
        }
    }

    @EventHandler
    public void onInventoryOpen(InventoryOpenEvent event) {
        Player player = (Player) event.getPlayer();
        if (event.getView().getTitle().equals(SHOP_TITLE)) {
            // Ensure items are up to date when inventory is opened
            for (int i = 0; i < Main.getSize(); i++) {
                ItemStack current = Main.getItem(i);
                if (current == null || current.getType() != Material.GRAY_STAINED_GLASS_PANE) {
                    Main.setItem(i, ShopObjects.createGlass());
                }
            }
            Main.setItem(11, ShopObjects.createBoosterInventory());
            Main.setItem(13, ShopObjects.createNull());
            Main.setItem(15, ShopObjects.createNull());
        } else if (event.getView().getTitle().equals(BOOSTER_TITLE)) {
            // Ensure items are up to date when inventory is opened
            for (int i = 0; i < Booster.getSize(); i++) {
                ItemStack current = Booster.getItem(i);
                if (current == null || current.getType() != Material.GRAY_STAINED_GLASS_PANE) {
                    Booster.setItem(i, ShopObjects.createGlass());
                }
            }
            setup(player);
        }
    }

    public static void initialize() {
        synchronized (initLock) {
            if (isInitialized) {
                return;
            }

            if (plugin == null) {
                throw new IllegalStateException("Plugin instance not set. Call setPlugin() first.");
            }
                ShopObjects.createBoosterInventory();
            isInitialized = true;
        }
    }

    public static String lookForBooster(Player player) {
        for (int i = 0; i <= DURATION_ROUNDS; i++) {
            if (Lists.bonus25.get(i).contains(player)) {
                return "1,25x Points";
            } else if (Lists.bonus50.get(i).contains(player)) {
                return "1,5x Points";
            } else if (Lists.bonus100.get(i).contains(player)) {
                return "2x Points";
            } else if (Lists.recharge1.get(i).contains(player)) {
                return "Aufladerate+";
            } else if (Lists.recharge2.get(i).contains(player)) {
                return "Aufladerate++";
            } else if (Lists.speed1.get(i).contains(player)) {
                return "Eintauchgeschwindigkeit+";
            } else if (Lists.speed2.get(i).contains(player)) {
                return "Eintauchgeschwindigkeit++";
            } else if (Lists.protection1.get(i).contains(player)) {
                return "Spawnschutz";
            } else if (Lists.protection2.get(i).contains(player)) {
                return "Spawnschutz+";
            } else if (Lists.protection3.get(i).contains(player)) {
                return "Spawnschutz++";
            }
        }
        return "§a";
    }

    private static void setup(Player player) {
        Booster.setItem(39, ShopObjects.counter(player));
        Booster.setItem(41, ShopObjects.createPointsShow(player));

        Booster.setItem(10, ShopObjects.points25(player));
        Booster.setItem(19, ShopObjects.points50(player));
        Booster.setItem(28, ShopObjects.points100(player));

        Booster.setItem(12, ShopObjects.recharge1(player));
        Booster.setItem(21, ShopObjects.recharge2(player));

        Booster.setItem(13, ShopObjects.speed1(player));
        Booster.setItem(22, ShopObjects.speed2(player));

        Booster.setItem(14, ShopObjects.protection1(player));
        Booster.setItem(23, ShopObjects.protection2(player));
        Booster.setItem(32, ShopObjects.protection3(player));
    }
}
