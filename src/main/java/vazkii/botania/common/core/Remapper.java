package vazkii.botania.common.core;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.event.FMLMissingMappingsEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import vazkii.botania.common.Botania;

import java.util.List;
import java.util.stream.Collectors;

public final class Remapper {

    public static void process(List<FMLMissingMappingsEvent.MissingMapping> mappings) {
        boolean quark = Botania.quarkLoaded;
        List<FMLMissingMappingsEvent.MissingMapping> items = mappings.stream().filter(m -> m.type == GameRegistry.Type.ITEM).collect(Collectors.toList());
        List<FMLMissingMappingsEvent.MissingMapping> blocks = mappings.stream().filter(m -> m.type == GameRegistry.Type.BLOCK).collect(Collectors.toList());
        
        for(FMLMissingMappingsEvent.MissingMapping m : blocks) {
            switch (m.name) {
                // Prismarine
                case "botania:prismarine": m.remap(getBlock("minecraft:prismarine")); break;
                case "botania:seaLamp": m.remap(getBlock("minecraft:sea_lantern")); break;
                case "botania:prismarine0Stairs": if (quark) m.remap(getBlock("quark:prismarine_stairs")); else m.warn(); break;
                case "botania:prismarine0Slab": if (quark) m.remap(getBlock("quark:prismarine_slab")); else m.warn(); break;
                case "botania:prismarine0SlabFull": if (quark) m.remap(getBlock("quark:prismarine_slab_double")); else m.warn(); break;
                case "botania:prismarine0Wall": if (quark) m.remap(getBlock("quark:prismarine_rough_wall")); else m.warn(); break;
                case "botania:prismarine1Stairs": if (quark) m.remap(getBlock("quark:prismarine_bricks_stairs")); else m.warn(); break;
                case "botania:prismarine1Slab": if (quark) m.remap(getBlock("quark:prismarine_bricks_slab")); else m.warn(); break;
                case "botania:prismarine1SlabFull": if (quark) m.remap(getBlock("quark:prismarine_bricks_slab_double")); else m.warn(); break;
                case "botania:prismarine2Stairs": if (quark) m.remap(getBlock("quark:prismarine_dark_stairs")); else m.warn(); break;
                case "botania:prismarine2Slab": if (quark) m.remap(getBlock("quark:prismarine_dark_slab")); else m.warn(); break;
                case "botania:prismarine2SlabFull": if (quark) m.remap(getBlock("quark:prismarine_dark_slab_double")); else m.warn(); break;

                //
            }
        }

        for(FMLMissingMappingsEvent.MissingMapping m : items) {
            switch (m.name) {
                // Prismarine
                case "botania:prismarine": m.remap(getItem("minecraft:prismarine")); break;
                case "botania:seaLamp": m.remap(getItem("minecraft:sea_lantern")); break;
                case "botania:prismarine0Stairs": if (quark) m.remap(getItem("quark:prismarine_stairs")); else m.warn(); break;
                case "botania:prismarine0Slab": if (quark) m.remap(getItem("quark:prismarine_slab")); else m.warn(); break;
                case "botania:prismarine0Wall": if (quark) m.remap(getItem("quark:prismarine_rough_wall")); else m.warn(); break;
                case "botania:prismarine1Stairs": if (quark) m.remap(getItem("quark:prismarine_bricks_stairs")); else m.warn(); break;
                case "botania:prismarine1Slab": if (quark) m.remap(getItem("quark:prismarine_bricks_slab")); else m.warn(); break;
                case "botania:prismarine2Stairs": if (quark) m.remap(getItem("quark:prismarine_dark_stairs")); else m.warn(); break;
                case "botania:prismarine2Slab": if (quark) m.remap(getItem("quark:prismarine_dark_slab")); else m.warn(); break;
            }
        }
    }

    private static Block getBlock(String name) {
        return Block.blockRegistry.getObject(new ResourceLocation(name));
    }
    
    private static Item getItem(String name) {
        return Item.itemRegistry.getObject(new ResourceLocation(name));
    }

    private Remapper() {}

}
