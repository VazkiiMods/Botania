package vazkii.botania.api;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumDyeColor;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLModIdMappingEvent;

import java.util.function.Function;

@Mod.EventBusSubscriber(modid = "botania")
public final class ColorHelper {
    public static final BiMap<EnumDyeColor, Block> STAINED_GLASS_MAP = HashBiMap.create();
    public static final BiMap<EnumDyeColor, Block> STAINED_GLASS_PANE_MAP = HashBiMap.create();
    public static final BiMap<EnumDyeColor, Block> TERRACOTTA_MAP = HashBiMap.create();
    public static final BiMap<EnumDyeColor, Block> WOOL_MAP = HashBiMap.create();
    public static final BiMap<EnumDyeColor, Block> CARPET_MAP = HashBiMap.create();
    
    @SubscribeEvent
    public static void idShift(FMLModIdMappingEvent evt) {
        STAINED_GLASS_MAP.clear();
        STAINED_GLASS_MAP.put(EnumDyeColor.WHITE, Blocks.WHITE_STAINED_GLASS);
        STAINED_GLASS_MAP.put(EnumDyeColor.ORANGE, Blocks.ORANGE_STAINED_GLASS);
        STAINED_GLASS_MAP.put(EnumDyeColor.MAGENTA, Blocks.MAGENTA_STAINED_GLASS);
        STAINED_GLASS_MAP.put(EnumDyeColor.LIGHT_BLUE, Blocks.LIGHT_BLUE_STAINED_GLASS);
        STAINED_GLASS_MAP.put(EnumDyeColor.YELLOW, Blocks.YELLOW_STAINED_GLASS);
        STAINED_GLASS_MAP.put(EnumDyeColor.LIME, Blocks.LIME_STAINED_GLASS);
        STAINED_GLASS_MAP.put(EnumDyeColor.PINK, Blocks.PINK_STAINED_GLASS);
        STAINED_GLASS_MAP.put(EnumDyeColor.GRAY, Blocks.GRAY_STAINED_GLASS);
        STAINED_GLASS_MAP.put(EnumDyeColor.LIGHT_GRAY, Blocks.LIGHT_GRAY_STAINED_GLASS);
        STAINED_GLASS_MAP.put(EnumDyeColor.CYAN, Blocks.CYAN_STAINED_GLASS);
        STAINED_GLASS_MAP.put(EnumDyeColor.PURPLE, Blocks.PURPLE_STAINED_GLASS);
        STAINED_GLASS_MAP.put(EnumDyeColor.BLUE, Blocks.BLUE_STAINED_GLASS);
        STAINED_GLASS_MAP.put(EnumDyeColor.BROWN, Blocks.BROWN_STAINED_GLASS);
        STAINED_GLASS_MAP.put(EnumDyeColor.GREEN, Blocks.GREEN_STAINED_GLASS);
        STAINED_GLASS_MAP.put(EnumDyeColor.RED, Blocks.RED_STAINED_GLASS);
        STAINED_GLASS_MAP.put(EnumDyeColor.BLACK, Blocks.BLACK_STAINED_GLASS);
        
        STAINED_GLASS_PANE_MAP.clear();
        STAINED_GLASS_PANE_MAP.put(EnumDyeColor.WHITE, Blocks.WHITE_STAINED_GLASS_PANE);
        STAINED_GLASS_PANE_MAP.put(EnumDyeColor.ORANGE, Blocks.ORANGE_STAINED_GLASS_PANE);
        STAINED_GLASS_PANE_MAP.put(EnumDyeColor.MAGENTA, Blocks.MAGENTA_STAINED_GLASS_PANE);
        STAINED_GLASS_PANE_MAP.put(EnumDyeColor.LIGHT_BLUE, Blocks.LIGHT_BLUE_STAINED_GLASS_PANE);
        STAINED_GLASS_PANE_MAP.put(EnumDyeColor.YELLOW, Blocks.YELLOW_STAINED_GLASS_PANE);
        STAINED_GLASS_PANE_MAP.put(EnumDyeColor.LIME, Blocks.LIME_STAINED_GLASS_PANE);
        STAINED_GLASS_PANE_MAP.put(EnumDyeColor.PINK, Blocks.PINK_STAINED_GLASS_PANE);
        STAINED_GLASS_PANE_MAP.put(EnumDyeColor.GRAY, Blocks.GRAY_STAINED_GLASS_PANE);
        STAINED_GLASS_PANE_MAP.put(EnumDyeColor.LIGHT_GRAY, Blocks.LIGHT_GRAY_STAINED_GLASS_PANE);
        STAINED_GLASS_PANE_MAP.put(EnumDyeColor.CYAN, Blocks.CYAN_STAINED_GLASS_PANE);
        STAINED_GLASS_PANE_MAP.put(EnumDyeColor.PURPLE, Blocks.PURPLE_STAINED_GLASS_PANE);
        STAINED_GLASS_PANE_MAP.put(EnumDyeColor.BLUE, Blocks.BLUE_STAINED_GLASS_PANE);
        STAINED_GLASS_PANE_MAP.put(EnumDyeColor.BROWN, Blocks.BROWN_STAINED_GLASS_PANE);
        STAINED_GLASS_PANE_MAP.put(EnumDyeColor.GREEN, Blocks.GREEN_STAINED_GLASS_PANE);
        STAINED_GLASS_PANE_MAP.put(EnumDyeColor.RED, Blocks.RED_STAINED_GLASS_PANE);
        STAINED_GLASS_PANE_MAP.put(EnumDyeColor.BLACK, Blocks.BLACK_STAINED_GLASS_PANE);
        
        TERRACOTTA_MAP.clear();
        TERRACOTTA_MAP.put(EnumDyeColor.WHITE, Blocks.WHITE_TERRACOTTA);
        TERRACOTTA_MAP.put(EnumDyeColor.ORANGE, Blocks.ORANGE_TERRACOTTA);
        TERRACOTTA_MAP.put(EnumDyeColor.MAGENTA, Blocks.MAGENTA_TERRACOTTA);
        TERRACOTTA_MAP.put(EnumDyeColor.LIGHT_BLUE, Blocks.LIGHT_BLUE_TERRACOTTA);
        TERRACOTTA_MAP.put(EnumDyeColor.YELLOW, Blocks.YELLOW_TERRACOTTA);
        TERRACOTTA_MAP.put(EnumDyeColor.LIME, Blocks.LIME_TERRACOTTA);
        TERRACOTTA_MAP.put(EnumDyeColor.PINK, Blocks.PINK_TERRACOTTA);
        TERRACOTTA_MAP.put(EnumDyeColor.GRAY, Blocks.GRAY_TERRACOTTA);
        TERRACOTTA_MAP.put(EnumDyeColor.LIGHT_GRAY, Blocks.LIGHT_GRAY_TERRACOTTA);
        TERRACOTTA_MAP.put(EnumDyeColor.CYAN, Blocks.CYAN_TERRACOTTA);
        TERRACOTTA_MAP.put(EnumDyeColor.PURPLE, Blocks.PURPLE_TERRACOTTA);
        TERRACOTTA_MAP.put(EnumDyeColor.BLUE, Blocks.BLUE_TERRACOTTA);
        TERRACOTTA_MAP.put(EnumDyeColor.BROWN, Blocks.BROWN_TERRACOTTA);
        TERRACOTTA_MAP.put(EnumDyeColor.GREEN, Blocks.GREEN_TERRACOTTA);
        TERRACOTTA_MAP.put(EnumDyeColor.RED, Blocks.RED_TERRACOTTA);
        TERRACOTTA_MAP.put(EnumDyeColor.BLACK, Blocks.BLACK_TERRACOTTA);
        
        WOOL_MAP.clear();
        WOOL_MAP.put(EnumDyeColor.WHITE, Blocks.WHITE_WOOL);
        WOOL_MAP.put(EnumDyeColor.ORANGE, Blocks.ORANGE_WOOL);
        WOOL_MAP.put(EnumDyeColor.MAGENTA, Blocks.MAGENTA_WOOL);
        WOOL_MAP.put(EnumDyeColor.LIGHT_BLUE, Blocks.LIGHT_BLUE_WOOL);
        WOOL_MAP.put(EnumDyeColor.YELLOW, Blocks.YELLOW_WOOL);
        WOOL_MAP.put(EnumDyeColor.LIME, Blocks.LIME_WOOL);
        WOOL_MAP.put(EnumDyeColor.PINK, Blocks.PINK_WOOL);
        WOOL_MAP.put(EnumDyeColor.GRAY, Blocks.GRAY_WOOL);
        WOOL_MAP.put(EnumDyeColor.LIGHT_GRAY, Blocks.LIGHT_GRAY_WOOL);
        WOOL_MAP.put(EnumDyeColor.CYAN, Blocks.CYAN_WOOL);
        WOOL_MAP.put(EnumDyeColor.PURPLE, Blocks.PURPLE_WOOL);
        WOOL_MAP.put(EnumDyeColor.BLUE, Blocks.BLUE_WOOL);
        WOOL_MAP.put(EnumDyeColor.BROWN, Blocks.BROWN_WOOL);
        WOOL_MAP.put(EnumDyeColor.GREEN, Blocks.GREEN_WOOL);
        WOOL_MAP.put(EnumDyeColor.RED, Blocks.RED_WOOL);
        WOOL_MAP.put(EnumDyeColor.BLACK, Blocks.BLACK_WOOL);
        
        CARPET_MAP.clear();
        CARPET_MAP.put(EnumDyeColor.WHITE, Blocks.WHITE_CARPET);
        CARPET_MAP.put(EnumDyeColor.ORANGE, Blocks.ORANGE_CARPET);
        CARPET_MAP.put(EnumDyeColor.MAGENTA, Blocks.MAGENTA_CARPET);
        CARPET_MAP.put(EnumDyeColor.LIGHT_BLUE, Blocks.LIGHT_BLUE_CARPET);
        CARPET_MAP.put(EnumDyeColor.YELLOW, Blocks.YELLOW_CARPET);
        CARPET_MAP.put(EnumDyeColor.LIME, Blocks.LIME_CARPET);
        CARPET_MAP.put(EnumDyeColor.PINK, Blocks.PINK_CARPET);
        CARPET_MAP.put(EnumDyeColor.GRAY, Blocks.GRAY_CARPET);
        CARPET_MAP.put(EnumDyeColor.LIGHT_GRAY, Blocks.LIGHT_GRAY_CARPET);
        CARPET_MAP.put(EnumDyeColor.CYAN, Blocks.CYAN_CARPET);
        CARPET_MAP.put(EnumDyeColor.PURPLE, Blocks.PURPLE_CARPET);
        CARPET_MAP.put(EnumDyeColor.BLUE, Blocks.BLUE_CARPET);
        CARPET_MAP.put(EnumDyeColor.BROWN, Blocks.BROWN_CARPET);
        CARPET_MAP.put(EnumDyeColor.GREEN, Blocks.GREEN_CARPET);
        CARPET_MAP.put(EnumDyeColor.RED, Blocks.RED_CARPET);
        CARPET_MAP.put(EnumDyeColor.BLACK, Blocks.BLACK_CARPET);
    }

    private ColorHelper() {}
}
