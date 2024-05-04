package vazkii.botania.client.render;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.FlowerBlock;
import net.minecraft.world.level.block.FlowerPotBlock;
import net.minecraft.world.level.block.TallFlowerBlock;

import vazkii.botania.common.block.BotaniaBlocks;
import vazkii.botania.common.block.decor.BotaniaMushroomBlock;
import vazkii.botania.common.block.decor.FloatingFlowerBlock;
import vazkii.botania.common.lib.LibMisc;

import java.util.function.BiConsumer;

public final class BlockRenderLayers {
	public static boolean skipPlatformBlocks;

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
		consumer.accept(BotaniaBlocks.managlassPane, RenderType.translucent());
		consumer.accept(BotaniaBlocks.elfGlass, RenderType.translucent());
		consumer.accept(BotaniaBlocks.alfglassPane, RenderType.translucent());
		consumer.accept(BotaniaBlocks.bifrost, RenderType.translucent());
		consumer.accept(BotaniaBlocks.bifrostPane, RenderType.translucent());
		consumer.accept(BotaniaBlocks.bifrostPerm, RenderType.translucent());
		consumer.accept(BotaniaBlocks.prism, RenderType.translucent());

		consumer.accept(BotaniaBlocks.starfield, RenderType.cutoutMipped());
		if (!skipPlatformBlocks) {
			// Render type is set dynamically on Forge and undisguised platforms should render as "solid",
			// but "translucent" is the best compromise on Fabric.
			// Translucent comes with a couple of downsides, like hidden block breaking animation and bad
			// Z-ordering for non-cubic block models that should be rendered with the "cutout" render type.
			consumer.accept(BotaniaBlocks.abstrusePlatform, RenderType.translucent());
			consumer.accept(BotaniaBlocks.infrangiblePlatform, RenderType.translucent());
			consumer.accept(BotaniaBlocks.spectralPlatform, RenderType.translucent());
		}
		BuiltInRegistries.BLOCK.stream().filter(b -> BuiltInRegistries.BLOCK.getKey(b).getNamespace().equals(LibMisc.MOD_ID))
				.forEach(b -> {
					if (b instanceof FloatingFlowerBlock || b instanceof FlowerBlock
							|| b instanceof TallFlowerBlock || b instanceof BotaniaMushroomBlock
							|| b instanceof FlowerPotBlock) {
						consumer.accept(b, RenderType.cutout());
					}
				});
	}

	private BlockRenderLayers() {}
}
