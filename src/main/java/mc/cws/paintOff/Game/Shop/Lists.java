package mc.cws.paintOff.Game.Shop;

import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Lists {
    public static Map<Integer, List<Player>> hasBonus = new HashMap<>(); // Rundenzahl | Spieler
    public static Map<Integer, List<Player>> bonus25 = new HashMap<>();
    public static Map<Integer, List<Player>> bonus50 = new HashMap<>();
    public static Map<Integer, List<Player>> bonus100 = new HashMap<>();

    public static Map<Integer, List<Player>> recharge1 = new HashMap<>();
    public static Map<Integer, List<Player>> recharge2 = new HashMap<>();

    public static Map<Integer, List<Player>> speed1 = new HashMap<>();
    public static Map<Integer, List<Player>> speed2 = new HashMap<>();

    public static Map<Integer, List<Player>> protection1 = new HashMap<>();
    public static Map<Integer, List<Player>> protection2 = new HashMap<>();
    public static Map<Integer, List<Player>> protection3 = new HashMap<>();

    public static void initializeMaps() {
        for (int i = 0; i <= ShopInventory.DURATION_ROUNDS; i++) {
            hasBonus.put(i, new ArrayList<>());
            bonus25.put(i, new ArrayList<>());
            bonus50.put(i, new ArrayList<>());
            bonus100.put(i, new ArrayList<>());
            recharge1.put(i, new ArrayList<>());
            recharge2.put(i, new ArrayList<>());
            speed1.put(i, new ArrayList<>());
            speed2.put(i, new ArrayList<>());
            protection1.put(i, new ArrayList<>());
            protection2.put(i, new ArrayList<>());
            protection3.put(i, new ArrayList<>());
        }
    }
}
