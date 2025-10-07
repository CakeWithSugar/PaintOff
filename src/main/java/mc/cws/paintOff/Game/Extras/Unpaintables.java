package mc.cws.paintOff.Game.Extras;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;

import java.util.HashSet;
import java.util.Set;

public class Unpaintables {
    private static final Set<Material> unpaintableBlocks = new HashSet<>();
    
    static {
        unpaintableBlocks.add(Material.AIR);
        unpaintableBlocks.add(Material.WATER);
        unpaintableBlocks.add(Material.LAVA);
        
        // Add all stair blocks
        // Wood stairs
        unpaintableBlocks.add(Material.ACACIA_STAIRS);
        unpaintableBlocks.add(Material.BIRCH_STAIRS);
        unpaintableBlocks.add(Material.DARK_OAK_STAIRS);
        unpaintableBlocks.add(Material.JUNGLE_STAIRS);
        unpaintableBlocks.add(Material.OAK_STAIRS);
        unpaintableBlocks.add(Material.SPRUCE_STAIRS);
        unpaintableBlocks.add(Material.MANGROVE_STAIRS);
        unpaintableBlocks.add(Material.CHERRY_STAIRS);
        unpaintableBlocks.add(Material.PALE_OAK_STAIRS);
        
        // Nether-related stairs
        unpaintableBlocks.add(Material.WARPED_STAIRS);
        unpaintableBlocks.add(Material.CRIMSON_STAIRS);
        
        // Stone stairs
        unpaintableBlocks.add(Material.STONE_STAIRS);
        unpaintableBlocks.add(Material.COBBLESTONE_STAIRS);
        unpaintableBlocks.add(Material.MOSSY_COBBLESTONE_STAIRS);
        unpaintableBlocks.add(Material.END_STONE_BRICK_STAIRS);
        unpaintableBlocks.add(Material.PURPUR_STAIRS);

        // Nether brick stairs
        unpaintableBlocks.add(Material.NETHER_BRICK_STAIRS);
        unpaintableBlocks.add(Material.RED_NETHER_BRICK_STAIRS);
        
        // Sandstone stairs
        unpaintableBlocks.add(Material.SANDSTONE_STAIRS);
        unpaintableBlocks.add(Material.RED_SANDSTONE_STAIRS);
        
        // Stone brick stairs
        unpaintableBlocks.add(Material.STONE_BRICK_STAIRS);
        unpaintableBlocks.add(Material.MOSSY_STONE_BRICK_STAIRS);
        
        // Polished stone stairs
        unpaintableBlocks.add(Material.POLISHED_ANDESITE_STAIRS);
        unpaintableBlocks.add(Material.POLISHED_DIORITE_STAIRS);
        unpaintableBlocks.add(Material.POLISHED_GRANITE_STAIRS);
        
        // Basic stone stairs
        unpaintableBlocks.add(Material.ANDESITE_STAIRS);
        unpaintableBlocks.add(Material.DIORITE_STAIRS);
        unpaintableBlocks.add(Material.GRANITE_STAIRS);
        
        // Brick stairs
        unpaintableBlocks.add(Material.BRICK_STAIRS);
        
        // Prismarine stairs
        unpaintableBlocks.add(Material.PRISMARINE_STAIRS);
        unpaintableBlocks.add(Material.PRISMARINE_BRICK_STAIRS);
        unpaintableBlocks.add(Material.DARK_PRISMARINE_STAIRS);
        
        // Quartz stairs
        unpaintableBlocks.add(Material.QUARTZ_STAIRS);
        unpaintableBlocks.add(Material.SMOOTH_QUARTZ_STAIRS);
        
        // Copper stairs
        unpaintableBlocks.add(Material.CUT_COPPER_STAIRS);
        unpaintableBlocks.add(Material.EXPOSED_CUT_COPPER_STAIRS);
        unpaintableBlocks.add(Material.WEATHERED_CUT_COPPER_STAIRS);
        unpaintableBlocks.add(Material.OXIDIZED_CUT_COPPER_STAIRS);
        unpaintableBlocks.add(Material.WAXED_CUT_COPPER_STAIRS);
        unpaintableBlocks.add(Material.WAXED_EXPOSED_CUT_COPPER_STAIRS);
        unpaintableBlocks.add(Material.WAXED_WEATHERED_CUT_COPPER_STAIRS);
        unpaintableBlocks.add(Material.WAXED_OXIDIZED_CUT_COPPER_STAIRS);
        unpaintableBlocks.add(Material.RESIN_BRICK_STAIRS);
        unpaintableBlocks.add(Material.TUFF_BRICK_STAIRS);
        unpaintableBlocks.add(Material.TUFF_STAIRS);
        unpaintableBlocks.add(Material.POLISHED_TUFF_STAIRS);

        // Copper slabs
        unpaintableBlocks.add(Material.CUT_COPPER_SLAB);
        unpaintableBlocks.add(Material.EXPOSED_CUT_COPPER_SLAB);
        unpaintableBlocks.add(Material.WEATHERED_CUT_COPPER_SLAB);
        unpaintableBlocks.add(Material.OXIDIZED_CUT_COPPER_SLAB);
        unpaintableBlocks.add(Material.WAXED_CUT_COPPER_SLAB);
        unpaintableBlocks.add(Material.WAXED_EXPOSED_CUT_COPPER_SLAB);
        unpaintableBlocks.add(Material.WAXED_WEATHERED_CUT_COPPER_SLAB);
        unpaintableBlocks.add(Material.WAXED_OXIDIZED_CUT_COPPER_SLAB);
        unpaintableBlocks.add(Material.TUFF_SLAB);

        // Bamboo stairs
        unpaintableBlocks.add(Material.BAMBOO_STAIRS);
        unpaintableBlocks.add(Material.BAMBOO_MOSAIC_STAIRS);
        
        // Smooth stone stairs
        unpaintableBlocks.add(Material.SMOOTH_RED_SANDSTONE_STAIRS);
        unpaintableBlocks.add(Material.SMOOTH_SANDSTONE_STAIRS);

        // All fence blocks
        // Wood fences
        unpaintableBlocks.add(Material.ACACIA_FENCE);
        unpaintableBlocks.add(Material.BIRCH_FENCE);
        unpaintableBlocks.add(Material.DARK_OAK_FENCE);
        unpaintableBlocks.add(Material.JUNGLE_FENCE);
        unpaintableBlocks.add(Material.OAK_FENCE);
        unpaintableBlocks.add(Material.SPRUCE_FENCE);
        unpaintableBlocks.add(Material.MANGROVE_FENCE);
        unpaintableBlocks.add(Material.CHERRY_FENCE);
        unpaintableBlocks.add(Material.PALE_OAK_FENCE);
        unpaintableBlocks.add(Material.BAMBOO_FENCE);

        // Nether-related fences
        unpaintableBlocks.add(Material.WARPED_FENCE);
        unpaintableBlocks.add(Material.CRIMSON_FENCE);

        // Stone fences
        unpaintableBlocks.add(Material.NETHER_BRICK_FENCE);
        unpaintableBlocks.add(Material.BLACKSTONE_WALL);
        unpaintableBlocks.add(Material.POLISHED_BLACKSTONE_WALL);
        unpaintableBlocks.add(Material.POLISHED_BLACKSTONE_BRICK_WALL);
        unpaintableBlocks.add(Material.COBWEB);

        // All wall blocks
        unpaintableBlocks.add(Material.ANDESITE_WALL);
        unpaintableBlocks.add(Material.BRICK_WALL);
        unpaintableBlocks.add(Material.COBBLESTONE_WALL);
        unpaintableBlocks.add(Material.DIORITE_WALL);
        unpaintableBlocks.add(Material.END_STONE_BRICK_WALL);
        unpaintableBlocks.add(Material.GRANITE_WALL);
        unpaintableBlocks.add(Material.MOSSY_COBBLESTONE_WALL);
        unpaintableBlocks.add(Material.MOSSY_STONE_BRICK_WALL);
        unpaintableBlocks.add(Material.NETHER_BRICK_WALL);
        unpaintableBlocks.add(Material.PRISMARINE_WALL);
        unpaintableBlocks.add(Material.RED_NETHER_BRICK_WALL);
        unpaintableBlocks.add(Material.RED_SANDSTONE_WALL);
        unpaintableBlocks.add(Material.SANDSTONE_WALL);
        unpaintableBlocks.add(Material.STONE_BRICK_WALL);

        // All leaf blocks
        unpaintableBlocks.add(Material.ACACIA_LEAVES);
        unpaintableBlocks.add(Material.BIRCH_LEAVES);
        unpaintableBlocks.add(Material.DARK_OAK_LEAVES);
        unpaintableBlocks.add(Material.JUNGLE_LEAVES);
        unpaintableBlocks.add(Material.OAK_LEAVES);
        unpaintableBlocks.add(Material.SPRUCE_LEAVES);
        unpaintableBlocks.add(Material.MANGROVE_LEAVES);
        unpaintableBlocks.add(Material.CHERRY_LEAVES);
        unpaintableBlocks.add(Material.PALE_OAK_LEAVES);
        unpaintableBlocks.add(Material.AZALEA_LEAVES);
        unpaintableBlocks.add(Material.FLOWERING_AZALEA_LEAVES);

        // All door blocks
        unpaintableBlocks.add(Material.ACACIA_DOOR);
        unpaintableBlocks.add(Material.BIRCH_DOOR);
        unpaintableBlocks.add(Material.DARK_OAK_DOOR);
        unpaintableBlocks.add(Material.IRON_DOOR);
        unpaintableBlocks.add(Material.JUNGLE_DOOR);
        unpaintableBlocks.add(Material.OAK_DOOR);
        unpaintableBlocks.add(Material.SPRUCE_DOOR);
        unpaintableBlocks.add(Material.MANGROVE_DOOR);
        unpaintableBlocks.add(Material.CHERRY_DOOR);
        unpaintableBlocks.add(Material.PALE_OAK_DOOR);
        unpaintableBlocks.add(Material.CRIMSON_DOOR);
        unpaintableBlocks.add(Material.WARPED_DOOR);
        unpaintableBlocks.add(Material.BAMBOO_DOOR);
        unpaintableBlocks.add(Material.BLACK_STAINED_GLASS_PANE);
        unpaintableBlocks.add(Material.WHITE_STAINED_GLASS_PANE);
        unpaintableBlocks.add(Material.GLASS);
        unpaintableBlocks.add(Material.GLASS_PANE);
        unpaintableBlocks.add(Material.IRON_BARS);

        // All trapdoor blocks
        unpaintableBlocks.add(Material.ACACIA_TRAPDOOR);
        unpaintableBlocks.add(Material.BIRCH_TRAPDOOR);
        unpaintableBlocks.add(Material.DARK_OAK_TRAPDOOR);
        unpaintableBlocks.add(Material.IRON_TRAPDOOR);
        unpaintableBlocks.add(Material.JUNGLE_TRAPDOOR);
        unpaintableBlocks.add(Material.OAK_TRAPDOOR);
        unpaintableBlocks.add(Material.SPRUCE_TRAPDOOR);
        unpaintableBlocks.add(Material.MANGROVE_TRAPDOOR);
        unpaintableBlocks.add(Material.CHERRY_TRAPDOOR);
        unpaintableBlocks.add(Material.PALE_OAK_TRAPDOOR);
        unpaintableBlocks.add(Material.CRIMSON_TRAPDOOR);
        unpaintableBlocks.add(Material.WARPED_TRAPDOOR);
        unpaintableBlocks.add(Material.BAMBOO_TRAPDOOR);
        unpaintableBlocks.add(Material.EXPOSED_COPPER_TRAPDOOR);
        unpaintableBlocks.add(Material.WEATHERED_COPPER_TRAPDOOR);
        unpaintableBlocks.add(Material.OXIDIZED_COPPER_TRAPDOOR);
        unpaintableBlocks.add(Material.WAXED_EXPOSED_COPPER_TRAPDOOR);

        // All button blocks
        unpaintableBlocks.add(Material.ACACIA_BUTTON);
        unpaintableBlocks.add(Material.BIRCH_BUTTON);
        unpaintableBlocks.add(Material.DARK_OAK_BUTTON);
        unpaintableBlocks.add(Material.JUNGLE_BUTTON);
        unpaintableBlocks.add(Material.OAK_BUTTON);
        unpaintableBlocks.add(Material.SPRUCE_BUTTON);
        unpaintableBlocks.add(Material.MANGROVE_BUTTON);
        unpaintableBlocks.add(Material.CHERRY_BUTTON);
        unpaintableBlocks.add(Material.PALE_OAK_BUTTON);
        unpaintableBlocks.add(Material.CRIMSON_BUTTON);
        unpaintableBlocks.add(Material.WARPED_BUTTON);
        unpaintableBlocks.add(Material.BAMBOO_BUTTON);
        unpaintableBlocks.add(Material.STONE_BUTTON);
        unpaintableBlocks.add(Material.POLISHED_BLACKSTONE_BUTTON);

        // All slabs
        unpaintableBlocks.add(Material.ACACIA_SLAB);
        unpaintableBlocks.add(Material.BIRCH_SLAB);
        unpaintableBlocks.add(Material.DARK_OAK_SLAB);
        unpaintableBlocks.add(Material.JUNGLE_SLAB);
        unpaintableBlocks.add(Material.OAK_SLAB);
        unpaintableBlocks.add(Material.SPRUCE_SLAB);
        unpaintableBlocks.add(Material.MANGROVE_SLAB);
        unpaintableBlocks.add(Material.CHERRY_SLAB);
        unpaintableBlocks.add(Material.PALE_OAK_SLAB);
        unpaintableBlocks.add(Material.CRIMSON_SLAB);
        unpaintableBlocks.add(Material.WARPED_SLAB);
        unpaintableBlocks.add(Material.BAMBOO_SLAB);
        unpaintableBlocks.add(Material.BAMBOO_MOSAIC_SLAB);
        unpaintableBlocks.add(Material.STONE_SLAB);
        unpaintableBlocks.add(Material.SMOOTH_STONE_SLAB);
        unpaintableBlocks.add(Material.SANDSTONE_SLAB);
        unpaintableBlocks.add(Material.RED_SANDSTONE_SLAB);
        unpaintableBlocks.add(Material.PURPUR_SLAB);
        unpaintableBlocks.add(Material.BRICK_SLAB);
        unpaintableBlocks.add(Material.STONE_BRICK_SLAB);
        unpaintableBlocks.add(Material.MOSSY_STONE_BRICK_SLAB);
        unpaintableBlocks.add(Material.NETHER_BRICK_SLAB);
        unpaintableBlocks.add(Material.END_STONE_BRICK_SLAB);
        unpaintableBlocks.add(Material.SMOOTH_RED_SANDSTONE_SLAB);
        unpaintableBlocks.add(Material.SMOOTH_SANDSTONE_SLAB);
        unpaintableBlocks.add(Material.SMOOTH_QUARTZ_SLAB);
        unpaintableBlocks.add(Material.POLISHED_GRANITE_SLAB);
        unpaintableBlocks.add(Material.POLISHED_DIORITE_SLAB);
        unpaintableBlocks.add(Material.POLISHED_ANDESITE_SLAB);
        unpaintableBlocks.add(Material.COBBLESTONE_SLAB);
        unpaintableBlocks.add(Material.CUT_RED_SANDSTONE_SLAB);
        unpaintableBlocks.add(Material.CUT_SANDSTONE_SLAB);
        unpaintableBlocks.add(Material.POLISHED_BLACKSTONE_SLAB);
        unpaintableBlocks.add(Material.POLISHED_BLACKSTONE_BRICK_SLAB);
        unpaintableBlocks.add(Material.ANDESITE_SLAB);
        unpaintableBlocks.add(Material.GRANITE_SLAB);
        unpaintableBlocks.add(Material.DIORITE_SLAB);
        // All fence gates
        unpaintableBlocks.add(Material.ACACIA_FENCE_GATE);
        unpaintableBlocks.add(Material.BIRCH_FENCE_GATE);
        unpaintableBlocks.add(Material.DARK_OAK_FENCE_GATE);
        unpaintableBlocks.add(Material.JUNGLE_FENCE_GATE);
        unpaintableBlocks.add(Material.OAK_FENCE_GATE);
        unpaintableBlocks.add(Material.SPRUCE_FENCE_GATE);
        unpaintableBlocks.add(Material.MANGROVE_FENCE_GATE);
        unpaintableBlocks.add(Material.CHERRY_FENCE_GATE);
        unpaintableBlocks.add(Material.PALE_OAK_FENCE_GATE);
        unpaintableBlocks.add(Material.CRIMSON_FENCE_GATE);
        unpaintableBlocks.add(Material.WARPED_FENCE_GATE);
        unpaintableBlocks.add(Material.BAMBOO_FENCE_GATE);

        // All pressure plates
        unpaintableBlocks.add(Material.ACACIA_PRESSURE_PLATE);
        unpaintableBlocks.add(Material.BIRCH_PRESSURE_PLATE);
        unpaintableBlocks.add(Material.DARK_OAK_PRESSURE_PLATE);
        unpaintableBlocks.add(Material.JUNGLE_PRESSURE_PLATE);
        unpaintableBlocks.add(Material.OAK_PRESSURE_PLATE);
        unpaintableBlocks.add(Material.SPRUCE_PRESSURE_PLATE);
        unpaintableBlocks.add(Material.MANGROVE_PRESSURE_PLATE);
        unpaintableBlocks.add(Material.CHERRY_PRESSURE_PLATE);
        unpaintableBlocks.add(Material.PALE_OAK_PRESSURE_PLATE);
        unpaintableBlocks.add(Material.CRIMSON_PRESSURE_PLATE);
        unpaintableBlocks.add(Material.WARPED_PRESSURE_PLATE);
        unpaintableBlocks.add(Material.BAMBOO_PRESSURE_PLATE);
        unpaintableBlocks.add(Material.STONE_PRESSURE_PLATE);
        unpaintableBlocks.add(Material.POLISHED_BLACKSTONE_PRESSURE_PLATE);
        unpaintableBlocks.add(Material.HEAVY_WEIGHTED_PRESSURE_PLATE);
        unpaintableBlocks.add(Material.LIGHT_WEIGHTED_PRESSURE_PLATE);
        unpaintableBlocks.add(Material.SEA_LANTERN);

        // All sign blocks
        unpaintableBlocks.add(Material.ACACIA_SIGN);
        unpaintableBlocks.add(Material.BIRCH_SIGN);
        unpaintableBlocks.add(Material.DARK_OAK_SIGN);
        unpaintableBlocks.add(Material.JUNGLE_SIGN);
        unpaintableBlocks.add(Material.OAK_SIGN);
        unpaintableBlocks.add(Material.SPRUCE_SIGN);
        unpaintableBlocks.add(Material.MANGROVE_SIGN);
        unpaintableBlocks.add(Material.CHERRY_SIGN);
        unpaintableBlocks.add(Material.PALE_OAK_SIGN);
        unpaintableBlocks.add(Material.CRIMSON_SIGN);
        unpaintableBlocks.add(Material.WARPED_SIGN);
        unpaintableBlocks.add(Material.BAMBOO_SIGN);

        // All wall sign blocks
        unpaintableBlocks.add(Material.ACACIA_WALL_SIGN);
        unpaintableBlocks.add(Material.BIRCH_WALL_SIGN);
        unpaintableBlocks.add(Material.DARK_OAK_WALL_SIGN);
        unpaintableBlocks.add(Material.JUNGLE_WALL_SIGN);
        unpaintableBlocks.add(Material.OAK_WALL_SIGN);
        unpaintableBlocks.add(Material.SPRUCE_WALL_SIGN);
        unpaintableBlocks.add(Material.MANGROVE_WALL_SIGN);
        unpaintableBlocks.add(Material.CHERRY_WALL_SIGN);
        unpaintableBlocks.add(Material.PALE_OAK_WALL_SIGN);
        unpaintableBlocks.add(Material.CRIMSON_WALL_SIGN);
        unpaintableBlocks.add(Material.WARPED_WALL_SIGN);
        unpaintableBlocks.add(Material.BAMBOO_WALL_SIGN);

        unpaintableBlocks.add(Material.COPPER_BLOCK);
        unpaintableBlocks.add(Material.WAXED_COPPER_BLOCK);
        unpaintableBlocks.add(Material.COPPER_TRAPDOOR);
        unpaintableBlocks.add(Material.WAXED_COPPER_TRAPDOOR);
        unpaintableBlocks.add(Material.WAXED_WEATHERED_COPPER_TRAPDOOR);
        unpaintableBlocks.add(Material.WAXED_OXIDIZED_COPPER_TRAPDOOR);
        unpaintableBlocks.add(Material.COPPER_DOOR);
        unpaintableBlocks.add(Material.WAXED_COPPER_DOOR);
        unpaintableBlocks.add(Material.WAXED_WEATHERED_COPPER_DOOR);
        unpaintableBlocks.add(Material.WAXED_OXIDIZED_COPPER_DOOR);

        unpaintableBlocks.add(Material.CHAIN);
        unpaintableBlocks.add(Material.COPPER_GRATE);
        unpaintableBlocks.add(Material.WAXED_COPPER_GRATE);
        unpaintableBlocks.add(Material.WAXED_WEATHERED_COPPER_GRATE);
        unpaintableBlocks.add(Material.WAXED_OXIDIZED_COPPER_GRATE);
        unpaintableBlocks.add(Material.EXPOSED_COPPER_GRATE);
        unpaintableBlocks.add(Material.WAXED_EXPOSED_COPPER_GRATE);
        unpaintableBlocks.add(Material.WEATHERED_COPPER_GRATE);
        unpaintableBlocks.add(Material.OXIDIZED_COPPER_GRATE);

        // All colored glass variants
        unpaintableBlocks.add(Material.BLACK_STAINED_GLASS);
        unpaintableBlocks.add(Material.BLUE_STAINED_GLASS);
        unpaintableBlocks.add(Material.BROWN_STAINED_GLASS);
        unpaintableBlocks.add(Material.CYAN_STAINED_GLASS);
        unpaintableBlocks.add(Material.GRAY_STAINED_GLASS);
        unpaintableBlocks.add(Material.GREEN_STAINED_GLASS);
        unpaintableBlocks.add(Material.LIGHT_BLUE_STAINED_GLASS);
        unpaintableBlocks.add(Material.LIGHT_GRAY_STAINED_GLASS);
        unpaintableBlocks.add(Material.LIME_STAINED_GLASS);
        unpaintableBlocks.add(Material.MAGENTA_STAINED_GLASS);
        unpaintableBlocks.add(Material.ORANGE_STAINED_GLASS);
        unpaintableBlocks.add(Material.PINK_STAINED_GLASS);
        unpaintableBlocks.add(Material.PURPLE_STAINED_GLASS);
        unpaintableBlocks.add(Material.RED_STAINED_GLASS);
        unpaintableBlocks.add(Material.WHITE_STAINED_GLASS);
        unpaintableBlocks.add(Material.YELLOW_STAINED_GLASS);
        unpaintableBlocks.add(Material.TINTED_GLASS);

        unpaintableBlocks.add(Material.BARRIER);
        unpaintableBlocks.add(Material.LIGHT);

        // Other interactables
        unpaintableBlocks.add(Material.LEVER);
        unpaintableBlocks.add(Material.CHEST);
        unpaintableBlocks.add(Material.TRAPPED_CHEST);
        unpaintableBlocks.add(Material.BREWING_STAND);
        unpaintableBlocks.add(Material.COMMAND_BLOCK);
        unpaintableBlocks.add(Material.END_PORTAL);
        unpaintableBlocks.add(Material.END_PORTAL_FRAME);
        unpaintableBlocks.add(Material.ENDER_CHEST);
        unpaintableBlocks.add(Material.ENCHANTING_TABLE);
        unpaintableBlocks.add(Material.ANVIL);
        unpaintableBlocks.add(Material.CHIPPED_ANVIL);
        unpaintableBlocks.add(Material.DAMAGED_ANVIL);
        unpaintableBlocks.add(Material.LECTERN);
        unpaintableBlocks.add(Material.BEACON);
        unpaintableBlocks.add(Material.CONDUIT);
        unpaintableBlocks.add(Material.END_GATEWAY);
        unpaintableBlocks.add(Material.NETHER_PORTAL);
        unpaintableBlocks.add(Material.END_ROD);
        unpaintableBlocks.add(Material.END_CRYSTAL);

        // Redstone-activatable blocks
        unpaintableBlocks.add(Material.REDSTONE);
        unpaintableBlocks.add(Material.REDSTONE_TORCH);
        unpaintableBlocks.add(Material.REDSTONE_WALL_TORCH);
        unpaintableBlocks.add(Material.DAYLIGHT_DETECTOR);
        unpaintableBlocks.add(Material.HOPPER);
        unpaintableBlocks.add(Material.SLIME_BLOCK);
        unpaintableBlocks.add(Material.HONEY_BLOCK);

        unpaintableBlocks.add(Material.WHITE_BANNER);
        unpaintableBlocks.add(Material.ORANGE_BANNER);
        unpaintableBlocks.add(Material.MAGENTA_BANNER);
        unpaintableBlocks.add(Material.LIGHT_BLUE_BANNER);
        unpaintableBlocks.add(Material.YELLOW_BANNER);
        unpaintableBlocks.add(Material.LIME_BANNER);
        unpaintableBlocks.add(Material.PINK_BANNER);
        unpaintableBlocks.add(Material.GRAY_BANNER);
        unpaintableBlocks.add(Material.LIGHT_GRAY_BANNER);
        unpaintableBlocks.add(Material.CYAN_BANNER);
        unpaintableBlocks.add(Material.PURPLE_BANNER);
        unpaintableBlocks.add(Material.BLUE_BANNER);
        unpaintableBlocks.add(Material.BROWN_BANNER);
        unpaintableBlocks.add(Material.GREEN_BANNER);
        unpaintableBlocks.add(Material.RED_BANNER);
        unpaintableBlocks.add(Material.BLACK_BANNER);

        // Wall banners
        unpaintableBlocks.add(Material.WHITE_WALL_BANNER);
        unpaintableBlocks.add(Material.ORANGE_WALL_BANNER);
        unpaintableBlocks.add(Material.MAGENTA_WALL_BANNER);
        unpaintableBlocks.add(Material.LIGHT_BLUE_WALL_BANNER);
        unpaintableBlocks.add(Material.YELLOW_WALL_BANNER);
        unpaintableBlocks.add(Material.LIME_WALL_BANNER);
        unpaintableBlocks.add(Material.PINK_WALL_BANNER);
        unpaintableBlocks.add(Material.GRAY_WALL_BANNER);
        unpaintableBlocks.add(Material.LIGHT_GRAY_WALL_BANNER);
        unpaintableBlocks.add(Material.CYAN_WALL_BANNER);
        unpaintableBlocks.add(Material.PURPLE_WALL_BANNER);
        unpaintableBlocks.add(Material.BLUE_WALL_BANNER);
        unpaintableBlocks.add(Material.BROWN_WALL_BANNER);
        unpaintableBlocks.add(Material.GREEN_WALL_BANNER);
        unpaintableBlocks.add(Material.RED_WALL_BANNER);
        unpaintableBlocks.add(Material.BLACK_WALL_BANNER);

        // All glowing/illuminating objects
        unpaintableBlocks.add(Material.TORCH);
        unpaintableBlocks.add(Material.WALL_TORCH);
        unpaintableBlocks.add(Material.GLOWSTONE);
        unpaintableBlocks.add(Material.JACK_O_LANTERN);
        unpaintableBlocks.add(Material.GLOW_LICHEN);
        unpaintableBlocks.add(Material.CAMPFIRE);
        unpaintableBlocks.add(Material.SOUL_CAMPFIRE);
        unpaintableBlocks.add(Material.LANTERN);
        unpaintableBlocks.add(Material.SOUL_LANTERN);
        unpaintableBlocks.add(Material.SHROOMLIGHT);
        unpaintableBlocks.add(Material.GLOW_ITEM_FRAME);

        // Grass and Ferns
        unpaintableBlocks.add(Material.SHORT_GRASS);
        unpaintableBlocks.add(Material.TALL_GRASS);
        unpaintableBlocks.add(Material.FERN);
        unpaintableBlocks.add(Material.LARGE_FERN);
        unpaintableBlocks.add(Material.PITCHER_PLANT);

        // Flowers
        unpaintableBlocks.add(Material.POPPY);
        unpaintableBlocks.add(Material.DANDELION);
        unpaintableBlocks.add(Material.BLUE_ORCHID);
        unpaintableBlocks.add(Material.ALLIUM);
        unpaintableBlocks.add(Material.AZURE_BLUET);
        unpaintableBlocks.add(Material.RED_TULIP);
        unpaintableBlocks.add(Material.ORANGE_TULIP);
        unpaintableBlocks.add(Material.WHITE_TULIP);
        unpaintableBlocks.add(Material.PINK_TULIP);
        unpaintableBlocks.add(Material.OXEYE_DAISY);
        unpaintableBlocks.add(Material.CORNFLOWER);
        unpaintableBlocks.add(Material.LILY_OF_THE_VALLEY);
        unpaintableBlocks.add(Material.WITHER_ROSE);
        unpaintableBlocks.add(Material.SUNFLOWER);
        unpaintableBlocks.add(Material.LILAC);
        unpaintableBlocks.add(Material.ROSE_BUSH);
        unpaintableBlocks.add(Material.PEONY);
        unpaintableBlocks.add(Material.LILY_PAD);

        // Mushrooms
        unpaintableBlocks.add(Material.RED_MUSHROOM);
        unpaintableBlocks.add(Material.BROWN_MUSHROOM);

        // Crops
        unpaintableBlocks.add(Material.WHEAT);
        unpaintableBlocks.add(Material.CARROTS);
        unpaintableBlocks.add(Material.POTATOES);
        unpaintableBlocks.add(Material.BEETROOTS);
        unpaintableBlocks.add(Material.SWEET_BERRY_BUSH);

        // Bamboo
        unpaintableBlocks.add(Material.BAMBOO);
        unpaintableBlocks.add(Material.BAMBOO_SAPLING);

        // Sugar Cane
        unpaintableBlocks.add(Material.SUGAR_CANE);
        
        // Flower Pots (empty and filled)
        unpaintableBlocks.add(Material.FLOWER_POT);
        unpaintableBlocks.add(Material.DECORATED_POT);
        unpaintableBlocks.add(Material.POTTED_POPPY);
        unpaintableBlocks.add(Material.POTTED_DANDELION);
        unpaintableBlocks.add(Material.POTTED_OAK_SAPLING);
        unpaintableBlocks.add(Material.POTTED_SPRUCE_SAPLING);
        unpaintableBlocks.add(Material.POTTED_BIRCH_SAPLING);
        unpaintableBlocks.add(Material.POTTED_JUNGLE_SAPLING);
        unpaintableBlocks.add(Material.POTTED_ACACIA_SAPLING);
        unpaintableBlocks.add(Material.POTTED_DARK_OAK_SAPLING);
        unpaintableBlocks.add(Material.POTTED_RED_MUSHROOM);
        unpaintableBlocks.add(Material.POTTED_BROWN_MUSHROOM);
        unpaintableBlocks.add(Material.POTTED_FERN);
        unpaintableBlocks.add(Material.POTTED_DEAD_BUSH);
        unpaintableBlocks.add(Material.POTTED_CACTUS);
        unpaintableBlocks.add(Material.POTTED_BLUE_ORCHID);
        unpaintableBlocks.add(Material.POTTED_ALLIUM);
        unpaintableBlocks.add(Material.POTTED_AZURE_BLUET);
        unpaintableBlocks.add(Material.POTTED_RED_TULIP);
        unpaintableBlocks.add(Material.POTTED_ORANGE_TULIP);
        unpaintableBlocks.add(Material.POTTED_WHITE_TULIP);
        unpaintableBlocks.add(Material.POTTED_PINK_TULIP);
        unpaintableBlocks.add(Material.POTTED_OXEYE_DAISY);
        unpaintableBlocks.add(Material.POTTED_CORNFLOWER);
        unpaintableBlocks.add(Material.POTTED_LILY_OF_THE_VALLEY);
        unpaintableBlocks.add(Material.POTTED_WITHER_ROSE);
        unpaintableBlocks.add(Material.POTTED_BAMBOO);
        unpaintableBlocks.add(Material.POTTED_CRIMSON_FUNGUS);
        unpaintableBlocks.add(Material.POTTED_WARPED_FUNGUS);
        unpaintableBlocks.add(Material.POTTED_CRIMSON_ROOTS);
        unpaintableBlocks.add(Material.POTTED_WARPED_ROOTS);
        unpaintableBlocks.add(Material.POTTED_AZALEA_BUSH);
        unpaintableBlocks.add(Material.POTTED_FLOWERING_AZALEA_BUSH);
        unpaintableBlocks.add(Material.POTTED_MANGROVE_PROPAGULE);
        unpaintableBlocks.add(Material.POTTED_CHERRY_SAPLING);
        unpaintableBlocks.add(Material.POTTED_TORCHFLOWER);

        // Nether Plants
        unpaintableBlocks.add(Material.WARPED_FUNGUS);
        unpaintableBlocks.add(Material.CRIMSON_FUNGUS);
        unpaintableBlocks.add(Material.WARPED_ROOTS);
        unpaintableBlocks.add(Material.CRIMSON_ROOTS);
        unpaintableBlocks.add(Material.NETHER_SPROUTS);

        // All carpet variants
        unpaintableBlocks.add(Material.BLACK_CARPET);
        unpaintableBlocks.add(Material.BLUE_CARPET);
        unpaintableBlocks.add(Material.BROWN_CARPET);
        unpaintableBlocks.add(Material.CYAN_CARPET);
        unpaintableBlocks.add(Material.GRAY_CARPET);
        unpaintableBlocks.add(Material.GREEN_CARPET);
        unpaintableBlocks.add(Material.LIGHT_BLUE_CARPET);
        unpaintableBlocks.add(Material.LIGHT_GRAY_CARPET);
        unpaintableBlocks.add(Material.LIME_CARPET);
        unpaintableBlocks.add(Material.MAGENTA_CARPET);
        unpaintableBlocks.add(Material.ORANGE_CARPET);
        unpaintableBlocks.add(Material.PINK_CARPET);
        unpaintableBlocks.add(Material.PURPLE_CARPET);
        unpaintableBlocks.add(Material.RED_CARPET);
        unpaintableBlocks.add(Material.WHITE_CARPET);
        unpaintableBlocks.add(Material.YELLOW_CARPET);
        unpaintableBlocks.add(Material.MOSS_CARPET);

        unpaintableBlocks.add(Material.LADDER);
        unpaintableBlocks.add(Material.TARGET);
        unpaintableBlocks.add(Material.CAULDRON);
    }

    public static boolean isUnpaintable(Material material, Location targetBlock) {
        // Get the block above
        Block blockAbove = targetBlock.getBlock().getRelative(0, 1, 0);
        Material materialAbove = blockAbove.getType();

        if ((Underpaintable.underpaintableBlocks.contains(materialAbove))) {
            return unpaintableBlocks.contains(material);
        }
        // If the block is in the resetable list, set it to air
        else if (Resetables.resetable(blockAbove.getType())) {
            blockAbove.setType(Material.AIR);
            return unpaintableBlocks.contains(material);
        } else {
            return true;
        }
    }
}
