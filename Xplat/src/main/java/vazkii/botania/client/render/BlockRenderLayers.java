package vazkii.botania.client.render;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.Registry;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.FlowerBlock;
import net.minecraft.world.level.block.TallFlowerBlock;

import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.block.ModFluffBlocks;
import vazkii.botania.common.block.decor.BlockFloatingFlower;
import vazkii.botania.common.block.decor.BlockModMushroom;
import vazkii.botania.common.lib.LibMisc;

import java.util.function.BiConsumer;

public final class BlockRenderLayers {
	public static void init(BiConsumer<Block, RenderType> consumer) {
		consumer.accept(ModBlocks.defaultAltar, RenderType.cutout());
		consumer.accept(ModBlocks.forestAltar, RenderType.cutout());
		consumer.accept(ModBlocks.plainsAltar, RenderType.cutout());
		consumer.accept(ModBlocks.mountainAltar, RenderType.cutout());
		consumer.accept(ModBlocks.fungalAltar, RenderType.cutout());
		consumer.accept(ModBlocks.swampAltar, RenderType.cutout());
		consumer.accept(ModBlocks.desertAltar, RenderType.cutout());
		consumer.accept(ModBlocks.taigaAltar, RenderType.cutout());
		consumer.accept(ModBlocks.mesaAltar, RenderType.cutout());
		consumer.accept(ModBlocks.mossyAltar, RenderType.cutout());
		consumer.accept(ModBlocks.ghostRail, RenderType.cutout());
		consumer.accept(ModBlocks.solidVines, RenderType.cutout());

		consumer.accept(ModBlocks.corporeaCrystalCube, RenderType.translucent());
		consumer.accept(ModBlocks.manaGlass, RenderType.translucent());
		consumer.accept(ModFluffBlocks.managlassPane, RenderType.translucent());
		consumer.accept(ModBlocks.elfGlass, RenderType.translucent());
		consumer.accept(ModFluffBlocks.alfglassPane, RenderType.translucent());
		consumer.accept(ModBlocks.bifrost, RenderType.translucent());
		consumer.accept(ModFluffBlocks.bifrostPane, RenderType.translucent());
		consumer.accept(ModBlocks.bifrostPerm, RenderType.translucent());
		consumer.accept(ModBlocks.prism, RenderType.translucent());

		consumer.accept(ModBlocks.starfield, RenderType.cutoutMipped());
		consumer.accept(ModBlocks.abstrusePlatform, RenderType.translucent());
		consumer.accept(ModBlocks.infrangiblePlatform, RenderType.translucent());
		consumer.accept(ModBlocks.spectralPlatform, RenderType.translucent());

		Registry.BLOCK.stream().filter(b -> Registry.BLOCK.getKey(b).getNamespace().equals(LibMisc.MOD_ID))
				.forEach(b -> {
					if (b instanceof BlockFloatingFlower || b instanceof FlowerBlock
							|| b instanceof TallFlowerBlock || b instanceof BlockModMushroom) {
						consumer.accept(b, RenderType.cutout());
					}
				});
	}

	private BlockRenderLayers() {}
}
