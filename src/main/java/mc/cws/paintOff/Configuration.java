package mc.cws.paintOff;

public class Configuration {
    public static String name = "PaintOff";
    public static String mainCommand = "po";

    public static boolean testbuild = true;
    public static boolean jumpAir = true;
    public static boolean debugger = false;

    public static int maxFulePoints = 200;
    public static int generalUltPoints = 500;

    // In start
    public static int spawnHight = 80; // Blocks
    public static int tick = 20; // 1/20 sec
    public static int gameTime = 4; // Minutes

    // In Queue
    public static int finalCounter = 10;
    public static int queueWaiting = 30;
    public static int minSizeAddition = 2;
    public static int normalMaxSize = 4;
    public static int maxQueues = 20;

    // In arsenal
    public static int unreleasedWeapons = 5; // if testbuild = true --> canceled
    public final static int maxWaffen = 16;

    public static int teleportTime = 6; // Seconds
    public static int respawnTime = 6;// Seconds
    public static int killDamage = 20;
}
