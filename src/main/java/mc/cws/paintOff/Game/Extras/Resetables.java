package mc.cws.paintOff.Game.Extras;

import org.bukkit.Material;

import java.util.HashSet;
import java.util.Set;

public class Resetables {
    public static final Set<Material> resetableBlocks = new HashSet<>();

    static {
        // Grass and Ferns
        resetableBlocks.add(Material.SHORT_GRASS);
        resetableBlocks.add(Material.TALL_GRASS);
        resetableBlocks.add(Material.FERN);
        resetableBlocks.add(Material.LARGE_FERN);
        resetableBlocks.add(Material.PITCHER_PLANT);

        // Flowers
        resetableBlocks.add(Material.POPPY);
        resetableBlocks.add(Material.DANDELION);
        resetableBlocks.add(Material.BLUE_ORCHID);
        resetableBlocks.add(Material.ALLIUM);
        resetableBlocks.add(Material.AZURE_BLUET);
        resetableBlocks.add(Material.RED_TULIP);
        resetableBlocks.add(Material.ORANGE_TULIP);
        resetableBlocks.add(Material.WHITE_TULIP);
        resetableBlocks.add(Material.PINK_TULIP);
        resetableBlocks.add(Material.OXEYE_DAISY);
        resetableBlocks.add(Material.CORNFLOWER);
        resetableBlocks.add(Material.LILY_OF_THE_VALLEY);
        resetableBlocks.add(Material.WITHER_ROSE);
        resetableBlocks.add(Material.SUNFLOWER);
        resetableBlocks.add(Material.LILAC);
        resetableBlocks.add(Material.ROSE_BUSH);
        resetableBlocks.add(Material.PEONY);
        resetableBlocks.add(Material.LILY_PAD);

        // Mushrooms
        resetableBlocks.add(Material.RED_MUSHROOM);
        resetableBlocks.add(Material.BROWN_MUSHROOM);

        // Crops
        resetableBlocks.add(Material.WHEAT);
        resetableBlocks.add(Material.CARROTS);
        resetableBlocks.add(Material.POTATOES);
        resetableBlocks.add(Material.BEETROOTS);
        resetableBlocks.add(Material.SWEET_BERRY_BUSH);

        // Bamboo
        resetableBlocks.add(Material.BAMBOO);
        resetableBlocks.add(Material.BAMBOO_SAPLING);

        // Pressure Plates
        resetableBlocks.add(Material.OAK_PRESSURE_PLATE);
        resetableBlocks.add(Material.SPRUCE_PRESSURE_PLATE);
        resetableBlocks.add(Material.BIRCH_PRESSURE_PLATE);
        resetableBlocks.add(Material.JUNGLE_PRESSURE_PLATE);
        resetableBlocks.add(Material.ACACIA_PRESSURE_PLATE);
        resetableBlocks.add(Material.DARK_OAK_PRESSURE_PLATE);
        resetableBlocks.add(Material.MANGROVE_PRESSURE_PLATE);
        resetableBlocks.add(Material.CHERRY_PRESSURE_PLATE);
        resetableBlocks.add(Material.BAMBOO_PRESSURE_PLATE);
        resetableBlocks.add(Material.CRIMSON_PRESSURE_PLATE);
        resetableBlocks.add(Material.WARPED_PRESSURE_PLATE);
        resetableBlocks.add(Material.STONE_PRESSURE_PLATE);
        resetableBlocks.add(Material.POLISHED_BLACKSTONE_PRESSURE_PLATE);
        resetableBlocks.add(Material.LIGHT_WEIGHTED_PRESSURE_PLATE);
        resetableBlocks.add(Material.HEAVY_WEIGHTED_PRESSURE_PLATE);

        // Buttons
        resetableBlocks.add(Material.OAK_BUTTON);
        resetableBlocks.add(Material.SPRUCE_BUTTON);
        resetableBlocks.add(Material.BIRCH_BUTTON);
        resetableBlocks.add(Material.JUNGLE_BUTTON);
        resetableBlocks.add(Material.ACACIA_BUTTON);
        resetableBlocks.add(Material.DARK_OAK_BUTTON);
        resetableBlocks.add(Material.MANGROVE_BUTTON);
        resetableBlocks.add(Material.CHERRY_BUTTON);
        resetableBlocks.add(Material.BAMBOO_BUTTON);
        resetableBlocks.add(Material.CRIMSON_BUTTON);
        resetableBlocks.add(Material.WARPED_BUTTON);
        resetableBlocks.add(Material.STONE_BUTTON);
        resetableBlocks.add(Material.POLISHED_BLACKSTONE_BUTTON);

        // Sugar Cane
        resetableBlocks.add(Material.SUGAR_CANE);

        // Nether Plants
        resetableBlocks.add(Material.WARPED_FUNGUS);
        resetableBlocks.add(Material.CRIMSON_FUNGUS);
        resetableBlocks.add(Material.WARPED_ROOTS);
        resetableBlocks.add(Material.CRIMSON_ROOTS);
        resetableBlocks.add(Material.NETHER_SPROUTS);
    }
    public static boolean resetable(Material material) {
        return resetableBlocks.contains(material);
    }

}
