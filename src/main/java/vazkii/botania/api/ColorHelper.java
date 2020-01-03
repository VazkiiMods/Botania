package vazkii.botania.api;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.DyeColor;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLModIdMappingEvent;

@Mod.EventBusSubscriber(modid = "botania")
public final class ColorHelper {
    public static final BiMap<DyeColor, Block> STAINED_GLASS_MAP = HashBiMap.create();
    public static final BiMap<DyeColor, Block> STAINED_GLASS_PANE_MAP = HashBiMap.create();
    public static final BiMap<DyeColor, Block> TERRACOTTA_MAP = HashBiMap.create();
    public static final BiMap<DyeColor, Block> GLAZED_TERRACOTTA_MAP = HashBiMap.create();
    public static final BiMap<DyeColor, Block> WOOL_MAP = HashBiMap.create();
    public static final BiMap<DyeColor, Block> CARPET_MAP = HashBiMap.create();
    public static final BiMap<DyeColor, Block> CONCRETE_MAP = HashBiMap.create();
    public static final BiMap<DyeColor, Block> CONCRETE_POWDER_MAP = HashBiMap.create();
    
    @SubscribeEvent
    public static void idShift(FMLModIdMappingEvent evt) {
        STAINED_GLASS_MAP.clear();
        STAINED_GLASS_MAP.put(DyeColor.WHITE, Blocks.WHITE_STAINED_GLASS);
        STAINED_GLASS_MAP.put(DyeColor.ORANGE, Blocks.ORANGE_STAINED_GLASS);
        STAINED_GLASS_MAP.put(DyeColor.MAGENTA, Blocks.MAGENTA_STAINED_GLASS);
        STAINED_GLASS_MAP.put(DyeColor.LIGHT_BLUE, Blocks.LIGHT_BLUE_STAINED_GLASS);
        STAINED_GLASS_MAP.put(DyeColor.YELLOW, Blocks.YELLOW_STAINED_GLASS);
        STAINED_GLASS_MAP.put(DyeColor.LIME, Blocks.LIME_STAINED_GLASS);
        STAINED_GLASS_MAP.put(DyeColor.PINK, Blocks.PINK_STAINED_GLASS);
        STAINED_GLASS_MAP.put(DyeColor.GRAY, Blocks.GRAY_STAINED_GLASS);
        STAINED_GLASS_MAP.put(DyeColor.LIGHT_GRAY, Blocks.LIGHT_GRAY_STAINED_GLASS);
        STAINED_GLASS_MAP.put(DyeColor.CYAN, Blocks.CYAN_STAINED_GLASS);
        STAINED_GLASS_MAP.put(DyeColor.PURPLE, Blocks.PURPLE_STAINED_GLASS);
        STAINED_GLASS_MAP.put(DyeColor.BLUE, Blocks.BLUE_STAINED_GLASS);
        STAINED_GLASS_MAP.put(DyeColor.BROWN, Blocks.BROWN_STAINED_GLASS);
        STAINED_GLASS_MAP.put(DyeColor.GREEN, Blocks.GREEN_STAINED_GLASS);
        STAINED_GLASS_MAP.put(DyeColor.RED, Blocks.RED_STAINED_GLASS);
        STAINED_GLASS_MAP.put(DyeColor.BLACK, Blocks.BLACK_STAINED_GLASS);
        
        STAINED_GLASS_PANE_MAP.clear();
        STAINED_GLASS_PANE_MAP.put(DyeColor.WHITE, Blocks.WHITE_STAINED_GLASS_PANE);
        STAINED_GLASS_PANE_MAP.put(DyeColor.ORANGE, Blocks.ORANGE_STAINED_GLASS_PANE);
        STAINED_GLASS_PANE_MAP.put(DyeColor.MAGENTA, Blocks.MAGENTA_STAINED_GLASS_PANE);
        STAINED_GLASS_PANE_MAP.put(DyeColor.LIGHT_BLUE, Blocks.LIGHT_BLUE_STAINED_GLASS_PANE);
        STAINED_GLASS_PANE_MAP.put(DyeColor.YELLOW, Blocks.YELLOW_STAINED_GLASS_PANE);
        STAINED_GLASS_PANE_MAP.put(DyeColor.LIME, Blocks.LIME_STAINED_GLASS_PANE);
        STAINED_GLASS_PANE_MAP.put(DyeColor.PINK, Blocks.PINK_STAINED_GLASS_PANE);
        STAINED_GLASS_PANE_MAP.put(DyeColor.GRAY, Blocks.GRAY_STAINED_GLASS_PANE);
        STAINED_GLASS_PANE_MAP.put(DyeColor.LIGHT_GRAY, Blocks.LIGHT_GRAY_STAINED_GLASS_PANE);
        STAINED_GLASS_PANE_MAP.put(DyeColor.CYAN, Blocks.CYAN_STAINED_GLASS_PANE);
        STAINED_GLASS_PANE_MAP.put(DyeColor.PURPLE, Blocks.PURPLE_STAINED_GLASS_PANE);
        STAINED_GLASS_PANE_MAP.put(DyeColor.BLUE, Blocks.BLUE_STAINED_GLASS_PANE);
        STAINED_GLASS_PANE_MAP.put(DyeColor.BROWN, Blocks.BROWN_STAINED_GLASS_PANE);
        STAINED_GLASS_PANE_MAP.put(DyeColor.GREEN, Blocks.GREEN_STAINED_GLASS_PANE);
        STAINED_GLASS_PANE_MAP.put(DyeColor.RED, Blocks.RED_STAINED_GLASS_PANE);
        STAINED_GLASS_PANE_MAP.put(DyeColor.BLACK, Blocks.BLACK_STAINED_GLASS_PANE);
        
        TERRACOTTA_MAP.clear();
        TERRACOTTA_MAP.put(DyeColor.WHITE, Blocks.WHITE_TERRACOTTA);
        TERRACOTTA_MAP.put(DyeColor.ORANGE, Blocks.ORANGE_TERRACOTTA);
        TERRACOTTA_MAP.put(DyeColor.MAGENTA, Blocks.MAGENTA_TERRACOTTA);
        TERRACOTTA_MAP.put(DyeColor.LIGHT_BLUE, Blocks.LIGHT_BLUE_TERRACOTTA);
        TERRACOTTA_MAP.put(DyeColor.YELLOW, Blocks.YELLOW_TERRACOTTA);
        TERRACOTTA_MAP.put(DyeColor.LIME, Blocks.LIME_TERRACOTTA);
        TERRACOTTA_MAP.put(DyeColor.PINK, Blocks.PINK_TERRACOTTA);
        TERRACOTTA_MAP.put(DyeColor.GRAY, Blocks.GRAY_TERRACOTTA);
        TERRACOTTA_MAP.put(DyeColor.LIGHT_GRAY, Blocks.LIGHT_GRAY_TERRACOTTA);
        TERRACOTTA_MAP.put(DyeColor.CYAN, Blocks.CYAN_TERRACOTTA);
        TERRACOTTA_MAP.put(DyeColor.PURPLE, Blocks.PURPLE_TERRACOTTA);
        TERRACOTTA_MAP.put(DyeColor.BLUE, Blocks.BLUE_TERRACOTTA);
        TERRACOTTA_MAP.put(DyeColor.BROWN, Blocks.BROWN_TERRACOTTA);
        TERRACOTTA_MAP.put(DyeColor.GREEN, Blocks.GREEN_TERRACOTTA);
        TERRACOTTA_MAP.put(DyeColor.RED, Blocks.RED_TERRACOTTA);
        TERRACOTTA_MAP.put(DyeColor.BLACK, Blocks.BLACK_TERRACOTTA);
        
        GLAZED_TERRACOTTA_MAP.clear();
        GLAZED_TERRACOTTA_MAP.put(DyeColor.WHITE, Blocks.WHITE_GLAZED_TERRACOTTA);
        GLAZED_TERRACOTTA_MAP.put(DyeColor.ORANGE, Blocks.ORANGE_GLAZED_TERRACOTTA);
        GLAZED_TERRACOTTA_MAP.put(DyeColor.MAGENTA, Blocks.MAGENTA_GLAZED_TERRACOTTA);
        GLAZED_TERRACOTTA_MAP.put(DyeColor.LIGHT_BLUE, Blocks.LIGHT_BLUE_GLAZED_TERRACOTTA);
        GLAZED_TERRACOTTA_MAP.put(DyeColor.YELLOW, Blocks.YELLOW_GLAZED_TERRACOTTA);
        GLAZED_TERRACOTTA_MAP.put(DyeColor.LIME, Blocks.LIME_GLAZED_TERRACOTTA);
        GLAZED_TERRACOTTA_MAP.put(DyeColor.PINK, Blocks.PINK_GLAZED_TERRACOTTA);
        GLAZED_TERRACOTTA_MAP.put(DyeColor.GRAY, Blocks.GRAY_GLAZED_TERRACOTTA);
        GLAZED_TERRACOTTA_MAP.put(DyeColor.LIGHT_GRAY, Blocks.LIGHT_GRAY_GLAZED_TERRACOTTA);
        GLAZED_TERRACOTTA_MAP.put(DyeColor.CYAN, Blocks.CYAN_GLAZED_TERRACOTTA);
        GLAZED_TERRACOTTA_MAP.put(DyeColor.PURPLE, Blocks.PURPLE_GLAZED_TERRACOTTA);
        GLAZED_TERRACOTTA_MAP.put(DyeColor.BLUE, Blocks.BLUE_GLAZED_TERRACOTTA);
        GLAZED_TERRACOTTA_MAP.put(DyeColor.BROWN, Blocks.BROWN_GLAZED_TERRACOTTA);
        GLAZED_TERRACOTTA_MAP.put(DyeColor.GREEN, Blocks.GREEN_GLAZED_TERRACOTTA);
        GLAZED_TERRACOTTA_MAP.put(DyeColor.RED, Blocks.RED_GLAZED_TERRACOTTA);
        GLAZED_TERRACOTTA_MAP.put(DyeColor.BLACK, Blocks.BLACK_GLAZED_TERRACOTTA);
        
        WOOL_MAP.clear();
        WOOL_MAP.put(DyeColor.WHITE, Blocks.WHITE_WOOL);
        WOOL_MAP.put(DyeColor.ORANGE, Blocks.ORANGE_WOOL);
        WOOL_MAP.put(DyeColor.MAGENTA, Blocks.MAGENTA_WOOL);
        WOOL_MAP.put(DyeColor.LIGHT_BLUE, Blocks.LIGHT_BLUE_WOOL);
        WOOL_MAP.put(DyeColor.YELLOW, Blocks.YELLOW_WOOL);
        WOOL_MAP.put(DyeColor.LIME, Blocks.LIME_WOOL);
        WOOL_MAP.put(DyeColor.PINK, Blocks.PINK_WOOL);
        WOOL_MAP.put(DyeColor.GRAY, Blocks.GRAY_WOOL);
        WOOL_MAP.put(DyeColor.LIGHT_GRAY, Blocks.LIGHT_GRAY_WOOL);
        WOOL_MAP.put(DyeColor.CYAN, Blocks.CYAN_WOOL);
        WOOL_MAP.put(DyeColor.PURPLE, Blocks.PURPLE_WOOL);
        WOOL_MAP.put(DyeColor.BLUE, Blocks.BLUE_WOOL);
        WOOL_MAP.put(DyeColor.BROWN, Blocks.BROWN_WOOL);
        WOOL_MAP.put(DyeColor.GREEN, Blocks.GREEN_WOOL);
        WOOL_MAP.put(DyeColor.RED, Blocks.RED_WOOL);
        WOOL_MAP.put(DyeColor.BLACK, Blocks.BLACK_WOOL);
        
        CARPET_MAP.clear();
        CARPET_MAP.put(DyeColor.WHITE, Blocks.WHITE_CARPET);
        CARPET_MAP.put(DyeColor.ORANGE, Blocks.ORANGE_CARPET);
        CARPET_MAP.put(DyeColor.MAGENTA, Blocks.MAGENTA_CARPET);
        CARPET_MAP.put(DyeColor.LIGHT_BLUE, Blocks.LIGHT_BLUE_CARPET);
        CARPET_MAP.put(DyeColor.YELLOW, Blocks.YELLOW_CARPET);
        CARPET_MAP.put(DyeColor.LIME, Blocks.LIME_CARPET);
        CARPET_MAP.put(DyeColor.PINK, Blocks.PINK_CARPET);
        CARPET_MAP.put(DyeColor.GRAY, Blocks.GRAY_CARPET);
        CARPET_MAP.put(DyeColor.LIGHT_GRAY, Blocks.LIGHT_GRAY_CARPET);
        CARPET_MAP.put(DyeColor.CYAN, Blocks.CYAN_CARPET);
        CARPET_MAP.put(DyeColor.PURPLE, Blocks.PURPLE_CARPET);
        CARPET_MAP.put(DyeColor.BLUE, Blocks.BLUE_CARPET);
        CARPET_MAP.put(DyeColor.BROWN, Blocks.BROWN_CARPET);
        CARPET_MAP.put(DyeColor.GREEN, Blocks.GREEN_CARPET);
        CARPET_MAP.put(DyeColor.RED, Blocks.RED_CARPET);
        CARPET_MAP.put(DyeColor.BLACK, Blocks.BLACK_CARPET);
        
        CONCRETE_MAP.clear();
        CONCRETE_MAP.put(DyeColor.WHITE, Blocks.WHITE_CONCRETE);
        CONCRETE_MAP.put(DyeColor.ORANGE, Blocks.ORANGE_CONCRETE);
        CONCRETE_MAP.put(DyeColor.MAGENTA, Blocks.MAGENTA_CONCRETE);
        CONCRETE_MAP.put(DyeColor.LIGHT_BLUE, Blocks.LIGHT_BLUE_CONCRETE);
        CONCRETE_MAP.put(DyeColor.YELLOW, Blocks.YELLOW_CONCRETE);
        CONCRETE_MAP.put(DyeColor.LIME, Blocks.LIME_CONCRETE);
        CONCRETE_MAP.put(DyeColor.PINK, Blocks.PINK_CONCRETE);
        CONCRETE_MAP.put(DyeColor.GRAY, Blocks.GRAY_CONCRETE);
        CONCRETE_MAP.put(DyeColor.LIGHT_GRAY, Blocks.LIGHT_GRAY_CONCRETE);
        CONCRETE_MAP.put(DyeColor.CYAN, Blocks.CYAN_CONCRETE);
        CONCRETE_MAP.put(DyeColor.PURPLE, Blocks.PURPLE_CONCRETE);
        CONCRETE_MAP.put(DyeColor.BLUE, Blocks.BLUE_CONCRETE);
        CONCRETE_MAP.put(DyeColor.BROWN, Blocks.BROWN_CONCRETE);
        CONCRETE_MAP.put(DyeColor.GREEN, Blocks.GREEN_CONCRETE);
        CONCRETE_MAP.put(DyeColor.RED, Blocks.RED_CONCRETE);
        CONCRETE_MAP.put(DyeColor.BLACK, Blocks.BLACK_CONCRETE);
        
        CONCRETE_POWDER_MAP.clear();
        CONCRETE_POWDER_MAP.put(DyeColor.WHITE, Blocks.WHITE_CONCRETE_POWDER);
        CONCRETE_POWDER_MAP.put(DyeColor.ORANGE, Blocks.ORANGE_CONCRETE_POWDER);
        CONCRETE_POWDER_MAP.put(DyeColor.MAGENTA, Blocks.MAGENTA_CONCRETE_POWDER);
        CONCRETE_POWDER_MAP.put(DyeColor.LIGHT_BLUE, Blocks.LIGHT_BLUE_CONCRETE_POWDER);
        CONCRETE_POWDER_MAP.put(DyeColor.YELLOW, Blocks.YELLOW_CONCRETE_POWDER);
        CONCRETE_POWDER_MAP.put(DyeColor.LIME, Blocks.LIME_CONCRETE_POWDER);
        CONCRETE_POWDER_MAP.put(DyeColor.PINK, Blocks.PINK_CONCRETE_POWDER);
        CONCRETE_POWDER_MAP.put(DyeColor.GRAY, Blocks.GRAY_CONCRETE_POWDER);
        CONCRETE_POWDER_MAP.put(DyeColor.LIGHT_GRAY, Blocks.LIGHT_GRAY_CONCRETE_POWDER);
        CONCRETE_POWDER_MAP.put(DyeColor.CYAN, Blocks.CYAN_CONCRETE_POWDER);
        CONCRETE_POWDER_MAP.put(DyeColor.PURPLE, Blocks.PURPLE_CONCRETE_POWDER);
        CONCRETE_POWDER_MAP.put(DyeColor.BLUE, Blocks.BLUE_CONCRETE_POWDER);
        CONCRETE_POWDER_MAP.put(DyeColor.BROWN, Blocks.BROWN_CONCRETE_POWDER);
        CONCRETE_POWDER_MAP.put(DyeColor.GREEN, Blocks.GREEN_CONCRETE_POWDER);
        CONCRETE_POWDER_MAP.put(DyeColor.RED, Blocks.RED_CONCRETE_POWDER);
        CONCRETE_POWDER_MAP.put(DyeColor.BLACK, Blocks.BLACK_CONCRETE_POWDER);
    }

    private ColorHelper() {}
}
