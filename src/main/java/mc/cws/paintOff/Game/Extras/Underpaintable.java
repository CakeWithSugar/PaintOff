package mc.cws.paintOff.Game.Extras;

import org.bukkit.Material;

import java.util.HashSet;
import java.util.Set;

public class Underpaintable {
    public static final Set<Material> underpaintableBlocks = new HashSet<>();

    static {
        underpaintableBlocks.add(Material.AIR);

        underpaintableBlocks.add(Material.ACACIA_FENCE);
        underpaintableBlocks.add(Material.BIRCH_FENCE);
        underpaintableBlocks.add(Material.DARK_OAK_FENCE);
        underpaintableBlocks.add(Material.JUNGLE_FENCE);
        underpaintableBlocks.add(Material.OAK_FENCE);
        underpaintableBlocks.add(Material.SPRUCE_FENCE);
        underpaintableBlocks.add(Material.MANGROVE_FENCE);
        underpaintableBlocks.add(Material.CHERRY_FENCE);
        underpaintableBlocks.add(Material.PALE_OAK_FENCE);
        underpaintableBlocks.add(Material.BAMBOO_FENCE);
        underpaintableBlocks.add(Material.WARPED_FENCE);
        underpaintableBlocks.add(Material.CRIMSON_FENCE);
        underpaintableBlocks.add(Material.NETHER_BRICK_FENCE);
        
        // Add all fence gate blocks
        underpaintableBlocks.add(Material.ACACIA_FENCE_GATE);
        underpaintableBlocks.add(Material.BIRCH_FENCE_GATE);
        underpaintableBlocks.add(Material.DARK_OAK_FENCE_GATE);
        underpaintableBlocks.add(Material.JUNGLE_FENCE_GATE);
        underpaintableBlocks.add(Material.OAK_FENCE_GATE);
        underpaintableBlocks.add(Material.SPRUCE_FENCE_GATE);
        underpaintableBlocks.add(Material.MANGROVE_FENCE_GATE);
        underpaintableBlocks.add(Material.CHERRY_FENCE_GATE);
        underpaintableBlocks.add(Material.PALE_OAK_FENCE_GATE);
        underpaintableBlocks.add(Material.CRIMSON_FENCE_GATE);
        underpaintableBlocks.add(Material.WARPED_FENCE_GATE);
        underpaintableBlocks.add(Material.BAMBOO_FENCE_GATE);
        
        // Add all trapdoor blocks
        underpaintableBlocks.add(Material.ACACIA_TRAPDOOR);
        underpaintableBlocks.add(Material.BIRCH_TRAPDOOR);
        underpaintableBlocks.add(Material.DARK_OAK_TRAPDOOR);
        underpaintableBlocks.add(Material.IRON_TRAPDOOR);
        underpaintableBlocks.add(Material.JUNGLE_TRAPDOOR);
        underpaintableBlocks.add(Material.OAK_TRAPDOOR);
        underpaintableBlocks.add(Material.SPRUCE_TRAPDOOR);
        underpaintableBlocks.add(Material.MANGROVE_TRAPDOOR);
        underpaintableBlocks.add(Material.CHERRY_TRAPDOOR);
        underpaintableBlocks.add(Material.PALE_OAK_TRAPDOOR);
        underpaintableBlocks.add(Material.CRIMSON_TRAPDOOR);
        underpaintableBlocks.add(Material.WARPED_TRAPDOOR);
        underpaintableBlocks.add(Material.BAMBOO_TRAPDOOR);
        underpaintableBlocks.add(Material.COPPER_TRAPDOOR);
        underpaintableBlocks.add(Material.WAXED_COPPER_TRAPDOOR);
        underpaintableBlocks.add(Material.WAXED_WEATHERED_COPPER_TRAPDOOR);
        underpaintableBlocks.add(Material.WAXED_OXIDIZED_COPPER_TRAPDOOR);
        
        // Add all wall blocks
        underpaintableBlocks.add(Material.ANDESITE_WALL);
        underpaintableBlocks.add(Material.BRICK_WALL);
        underpaintableBlocks.add(Material.COBBLESTONE_WALL);
        underpaintableBlocks.add(Material.DIORITE_WALL);
        underpaintableBlocks.add(Material.END_STONE_BRICK_WALL);
        underpaintableBlocks.add(Material.GRANITE_WALL);
        underpaintableBlocks.add(Material.MOSSY_COBBLESTONE_WALL);
        underpaintableBlocks.add(Material.MOSSY_STONE_BRICK_WALL);
        underpaintableBlocks.add(Material.NETHER_BRICK_WALL);
        underpaintableBlocks.add(Material.PRISMARINE_WALL);
        underpaintableBlocks.add(Material.RED_NETHER_BRICK_WALL);
        underpaintableBlocks.add(Material.RED_SANDSTONE_WALL);
        underpaintableBlocks.add(Material.SANDSTONE_WALL);
        underpaintableBlocks.add(Material.STONE_BRICK_WALL);
        underpaintableBlocks.add(Material.BLACKSTONE_WALL);
        underpaintableBlocks.add(Material.POLISHED_BLACKSTONE_WALL);
        underpaintableBlocks.add(Material.POLISHED_BLACKSTONE_BRICK_WALL);
        
        // Add wall signs
        underpaintableBlocks.add(Material.ACACIA_WALL_SIGN);
        underpaintableBlocks.add(Material.BIRCH_WALL_SIGN);
        underpaintableBlocks.add(Material.DARK_OAK_WALL_SIGN);
        underpaintableBlocks.add(Material.JUNGLE_WALL_SIGN);
        underpaintableBlocks.add(Material.OAK_WALL_SIGN);
        underpaintableBlocks.add(Material.SPRUCE_WALL_SIGN);
        underpaintableBlocks.add(Material.MANGROVE_WALL_SIGN);
        underpaintableBlocks.add(Material.CHERRY_WALL_SIGN);
        underpaintableBlocks.add(Material.PALE_OAK_WALL_SIGN);
        underpaintableBlocks.add(Material.CRIMSON_WALL_SIGN);
        underpaintableBlocks.add(Material.WARPED_WALL_SIGN);
        underpaintableBlocks.add(Material.BAMBOO_WALL_SIGN);
        
        // Add wall banners
        underpaintableBlocks.add(Material.WHITE_WALL_BANNER);
        underpaintableBlocks.add(Material.ORANGE_WALL_BANNER);
        underpaintableBlocks.add(Material.MAGENTA_WALL_BANNER);
        underpaintableBlocks.add(Material.LIGHT_BLUE_WALL_BANNER);
        underpaintableBlocks.add(Material.YELLOW_WALL_BANNER);
        underpaintableBlocks.add(Material.LIME_WALL_BANNER);
        underpaintableBlocks.add(Material.PINK_WALL_BANNER);
        underpaintableBlocks.add(Material.GRAY_WALL_BANNER);
        underpaintableBlocks.add(Material.LIGHT_GRAY_WALL_BANNER);
        underpaintableBlocks.add(Material.CYAN_WALL_BANNER);
        underpaintableBlocks.add(Material.PURPLE_WALL_BANNER);
        underpaintableBlocks.add(Material.BLUE_WALL_BANNER);
        underpaintableBlocks.add(Material.BROWN_WALL_BANNER);
        underpaintableBlocks.add(Material.GREEN_WALL_BANNER);
        underpaintableBlocks.add(Material.RED_WALL_BANNER);
        underpaintableBlocks.add(Material.BLACK_WALL_BANNER);
        underpaintableBlocks.add(Material.MUD_BRICK_WALL);
        underpaintableBlocks.add(Material.RESIN_BRICK_WALL);

        underpaintableBlocks.add(Material.REDSTONE_WALL_TORCH);
        underpaintableBlocks.add(Material.SOUL_WALL_TORCH);
        underpaintableBlocks.add(Material.WALL_TORCH);
        underpaintableBlocks.add(Material.SOUL_LANTERN);
        underpaintableBlocks.add(Material.LANTERN);
        underpaintableBlocks.add(Material.IRON_BARS);
        underpaintableBlocks.add(Material.CHAIN);
        
        // Add all door blocks
        underpaintableBlocks.add(Material.ACACIA_DOOR);
        underpaintableBlocks.add(Material.BIRCH_DOOR);
        underpaintableBlocks.add(Material.DARK_OAK_DOOR);
        underpaintableBlocks.add(Material.IRON_DOOR);
        underpaintableBlocks.add(Material.JUNGLE_DOOR);
        underpaintableBlocks.add(Material.OAK_DOOR);
        underpaintableBlocks.add(Material.SPRUCE_DOOR);
        underpaintableBlocks.add(Material.MANGROVE_DOOR);
        underpaintableBlocks.add(Material.CHERRY_DOOR);
        underpaintableBlocks.add(Material.PALE_OAK_DOOR);
        underpaintableBlocks.add(Material.CRIMSON_DOOR);
        underpaintableBlocks.add(Material.WARPED_DOOR);
        underpaintableBlocks.add(Material.BAMBOO_DOOR);
        underpaintableBlocks.add(Material.COPPER_DOOR);
        underpaintableBlocks.add(Material.WAXED_COPPER_DOOR);
        underpaintableBlocks.add(Material.WAXED_WEATHERED_COPPER_DOOR);
        underpaintableBlocks.add(Material.WAXED_OXIDIZED_COPPER_DOOR);
    }
}
