package vazkii.botania.client.render;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.Registry;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.FlowerBlock;
import net.minecraft.world.level.block.TallFlowerBlock;

import vazkii.botania.common.block.BotaniaBlocks;
import vazkii.botania.common.block.BotaniaFluffBlocks;
import vazkii.botania.common.block.decor.BotaniaMushroomBlock;
import vazkii.botania.common.block.decor.FloatingFlowerBlock;
import vazkii.botania.common.lib.LibMisc;

import java.util.function.BiConsumer;

public final class BlockRenderLayers {
	public static void init(BiConsumer<Block, RenderType> consumer) {
		consumer.accept(BotaniaBlocks.defaultAltar, RenderType.cutout());
		consumer.accept(BotaniaBlocks.forestAltar, RenderType.cutout());
		consumer.accept(BotaniaBlocks.plainsAltar, RenderType.cutout());
		consumer.accept(BotaniaBlocks.mountainAltar, RenderType.cutout());
		consumer.accept(BotaniaBlocks.fungalAltar, RenderType.cutout());
		consumer.accept(BotaniaBlocks.swampAltar, RenderType.cutout());
		consumer.accept(BotaniaBlocks.desertAltar, RenderType.cutout());
		consumer.accept(BotaniaBlocks.taigaAltar, RenderType.cutout());
		consumer.accept(BotaniaBlocks.mesaAltar, RenderType.cutout());
		consumer.accept(BotaniaBlocks.mossyAltar, RenderType.cutout());
		consumer.accept(BotaniaBlocks.ghostRail, RenderType.cutout());
		consumer.accept(BotaniaBlocks.solidVines, RenderType.cutout());

		consumer.accept(BotaniaBlocks.corporeaCrystalCube, RenderType.translucent());
		consumer.accept(BotaniaBlocks.manaGlass, RenderType.translucent());
		consumer.accept(BotaniaFluffBlocks.managlassPane, RenderType.translucent());
		consumer.accept(BotaniaBlocks.elfGlass, RenderType.translucent());
		consumer.accept(BotaniaFluffBlocks.alfglassPane, RenderType.translucent());
		consumer.accept(BotaniaBlocks.bifrost, RenderType.translucent());
		consumer.accept(BotaniaFluffBlocks.bifrostPane, RenderType.translucent());
		consumer.accept(BotaniaBlocks.bifrostPerm, RenderType.translucent());
		consumer.accept(BotaniaBlocks.prism, RenderType.translucent());

		consumer.accept(BotaniaBlocks.starfield, RenderType.cutoutMipped());
		consumer.accept(BotaniaBlocks.abstrusePlatform, RenderType.translucent());
		consumer.accept(BotaniaBlocks.infrangiblePlatform, RenderType.translucent());
		consumer.accept(BotaniaBlocks.spectralPlatform, RenderType.translucent());

		Registry.BLOCK.stream().filter(b -> Registry.BLOCK.getKey(b).getNamespace().equals(LibMisc.MOD_ID))
				.forEach(b -> {
					if (b instanceof FloatingFlowerBlock || b instanceof FlowerBlock
							|| b instanceof TallFlowerBlock || b instanceof BotaniaMushroomBlock) {
						consumer.accept(b, RenderType.cutout());
					}
				});
	}

	private BlockRenderLayers() {}
}
