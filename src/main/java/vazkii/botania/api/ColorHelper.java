package vazkii.botania.api;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumDyeColor;

import java.util.function.Function;

public final class ColorHelper {
    public static final Function<EnumDyeColor, Block> STAINED_GLASS = e -> {
        switch (e) {
            default:
            case WHITE: return Blocks.WHITE_STAINED_GLASS;
            case ORANGE: return Blocks.ORANGE_STAINED_GLASS;
            case MAGENTA: return Blocks.MAGENTA_STAINED_GLASS;
            case LIGHT_BLUE: return Blocks.LIGHT_BLUE_STAINED_GLASS;
            case YELLOW: return Blocks.YELLOW_STAINED_GLASS;
            case LIME: return Blocks.LIME_STAINED_GLASS;
            case PINK: return Blocks.PINK_STAINED_GLASS;
            case GRAY: return Blocks.GRAY_STAINED_GLASS;
            case LIGHT_GRAY: return Blocks.LIGHT_GRAY_STAINED_GLASS;
            case CYAN: return Blocks.CYAN_STAINED_GLASS;
            case PURPLE: return Blocks.PURPLE_STAINED_GLASS;
            case BLUE: return Blocks.BLUE_STAINED_GLASS;
            case BROWN: return Blocks.BROWN_STAINED_GLASS;
            case GREEN: return Blocks.GREEN_STAINED_GLASS;
            case RED: return Blocks.RED_STAINED_GLASS;
            case BLACK: return Blocks.BLACK_STAINED_GLASS;
        }
    };

    public static final Function<EnumDyeColor, Block> STAINED_GLASS_PANE = e -> {
        switch (e) {
            default:
            case WHITE: return Blocks.WHITE_STAINED_GLASS_PANE;
            case ORANGE: return Blocks.ORANGE_STAINED_GLASS_PANE;
            case MAGENTA: return Blocks.MAGENTA_STAINED_GLASS_PANE;
            case LIGHT_BLUE: return Blocks.LIGHT_BLUE_STAINED_GLASS_PANE;
            case YELLOW: return Blocks.YELLOW_STAINED_GLASS_PANE;
            case LIME: return Blocks.LIME_STAINED_GLASS_PANE;
            case PINK: return Blocks.PINK_STAINED_GLASS_PANE;
            case GRAY: return Blocks.GRAY_STAINED_GLASS_PANE;
            case LIGHT_GRAY: return Blocks.LIGHT_GRAY_STAINED_GLASS_PANE;
            case CYAN: return Blocks.CYAN_STAINED_GLASS_PANE;
            case PURPLE: return Blocks.PURPLE_STAINED_GLASS_PANE;
            case BLUE: return Blocks.BLUE_STAINED_GLASS_PANE;
            case BROWN: return Blocks.BROWN_STAINED_GLASS_PANE;
            case GREEN: return Blocks.GREEN_STAINED_GLASS_PANE;
            case RED: return Blocks.RED_STAINED_GLASS_PANE;
            case BLACK: return Blocks.BLACK_STAINED_GLASS_PANE;
        }
    };

    public static final Function<EnumDyeColor, Block> TERRACOTTA = e -> {
        switch (e) {
            default:
            case WHITE: return Blocks.WHITE_TERRACOTTA;
            case ORANGE: return Blocks.ORANGE_TERRACOTTA;
            case MAGENTA: return Blocks.MAGENTA_TERRACOTTA;
            case LIGHT_BLUE: return Blocks.LIGHT_BLUE_TERRACOTTA;
            case YELLOW: return Blocks.YELLOW_TERRACOTTA;
            case LIME: return Blocks.LIME_TERRACOTTA;
            case PINK: return Blocks.PINK_TERRACOTTA;
            case GRAY: return Blocks.GRAY_TERRACOTTA;
            case LIGHT_GRAY: return Blocks.LIGHT_GRAY_TERRACOTTA;
            case CYAN: return Blocks.CYAN_TERRACOTTA;
            case PURPLE: return Blocks.PURPLE_TERRACOTTA;
            case BLUE: return Blocks.BLUE_TERRACOTTA;
            case BROWN: return Blocks.BROWN_TERRACOTTA;
            case GREEN: return Blocks.GREEN_TERRACOTTA;
            case RED: return Blocks.RED_TERRACOTTA;
            case BLACK: return Blocks.BLACK_TERRACOTTA;
        }
    };

    public static final Function<EnumDyeColor, Block> WOOL = e -> {
        switch (e) {
            default:
            case WHITE: return Blocks.WHITE_WOOL;
            case ORANGE: return Blocks.ORANGE_WOOL;
            case MAGENTA: return Blocks.MAGENTA_WOOL;
            case LIGHT_BLUE: return Blocks.LIGHT_BLUE_WOOL;
            case YELLOW: return Blocks.YELLOW_WOOL;
            case LIME: return Blocks.LIME_WOOL;
            case PINK: return Blocks.PINK_WOOL;
            case GRAY: return Blocks.GRAY_WOOL;
            case LIGHT_GRAY: return Blocks.LIGHT_GRAY_WOOL;
            case CYAN: return Blocks.CYAN_WOOL;
            case PURPLE: return Blocks.PURPLE_WOOL;
            case BLUE: return Blocks.BLUE_WOOL;
            case BROWN: return Blocks.BROWN_WOOL;
            case GREEN: return Blocks.GREEN_WOOL;
            case RED: return Blocks.RED_WOOL;
            case BLACK: return Blocks.BLACK_WOOL;
        }
    };

    public static final Function<EnumDyeColor, Block> CARPET = e -> {
        switch (e) {
            default:
            case WHITE: return Blocks.WHITE_CARPET;
            case ORANGE: return Blocks.ORANGE_CARPET;
            case MAGENTA: return Blocks.MAGENTA_CARPET;
            case LIGHT_BLUE: return Blocks.LIGHT_BLUE_CARPET;
            case YELLOW: return Blocks.YELLOW_CARPET;
            case LIME: return Blocks.LIME_CARPET;
            case PINK: return Blocks.PINK_CARPET;
            case GRAY: return Blocks.GRAY_CARPET;
            case LIGHT_GRAY: return Blocks.LIGHT_GRAY_CARPET;
            case CYAN: return Blocks.CYAN_CARPET;
            case PURPLE: return Blocks.PURPLE_CARPET;
            case BLUE: return Blocks.BLUE_CARPET;
            case BROWN: return Blocks.BROWN_CARPET;
            case GREEN: return Blocks.GREEN_CARPET;
            case RED: return Blocks.RED_CARPET;
            case BLACK: return Blocks.BLACK_CARPET;
        }
    };
    
    private ColorHelper() {}
}
