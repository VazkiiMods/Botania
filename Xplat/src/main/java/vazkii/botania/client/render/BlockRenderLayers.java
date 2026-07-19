/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */

package vazkii.botania.client.render;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.FlowerBlock;
import net.minecraft.world.level.block.FlowerPotBlock;
import net.minecraft.world.level.block.TallFlowerBlock;

import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.common.block.BotaniaBlocks;
import vazkii.botania.common.block.ShimmeringMushroomBlock;
import vazkii.botania.common.block.flower.FloatingFlowerBaseBlock;

import java.util.function.BiConsumer;

public final class BlockRenderLayers {
	public static boolean skipPlatformBlocks;

	public static void init(BiConsumer<Block, RenderType> consumer) {
		consumer.accept(BotaniaBlocks.PETAL_APOTHECARY, RenderType.cutout());
		consumer.accept(BotaniaBlocks.PETAL_APOTHECARY_FUCHSITE, RenderType.cutout());
		consumer.accept(BotaniaBlocks.PETAL_APOTHECARY_TALC, RenderType.cutout());
		consumer.accept(BotaniaBlocks.PETAL_APOTHECARY_GNEISS, RenderType.cutout());
		consumer.accept(BotaniaBlocks.PETAL_APOTHECARY_MYCELITE, RenderType.cutout());
		consumer.accept(BotaniaBlocks.PETAL_APOTHECARY_CATACLASITE, RenderType.cutout());
		consumer.accept(BotaniaBlocks.PETAL_APOTHECARY_SOLITE, RenderType.cutout());
		consumer.accept(BotaniaBlocks.PETAL_APOTHECARY_LUNITE, RenderType.cutout());
		consumer.accept(BotaniaBlocks.PETAL_APOTHECARY_ROSY_TALC, RenderType.cutout());
		consumer.accept(BotaniaBlocks.PETAL_APOTHECARY_MOSSY, RenderType.cutout());
		consumer.accept(BotaniaBlocks.SPECTRAL_RAIL, RenderType.cutout());
		consumer.accept(BotaniaBlocks.SOLID_VINE, RenderType.cutout());
		consumer.accept(BotaniaBlocks.LIVINGWOOD_DOOR, RenderType.cutout());
		consumer.accept(BotaniaBlocks.LIVINGWOOD_TRAPDOOR, RenderType.cutout());
		consumer.accept(BotaniaBlocks.DREAMWOOD_DOOR, RenderType.cutout());
		consumer.accept(BotaniaBlocks.DREAMWOOD_TRAPDOOR, RenderType.cutout());

		consumer.accept(BotaniaBlocks.CORPOREA_CRYSTAL_CUBE, RenderType.translucent());
		consumer.accept(BotaniaBlocks.MANAGLASS, RenderType.translucent());
		consumer.accept(BotaniaBlocks.MANAGLASS_PANE, RenderType.translucent());
		consumer.accept(BotaniaBlocks.ALFGLASS, RenderType.translucent());
		consumer.accept(BotaniaBlocks.ALFGLASS_PANE, RenderType.translucent());
		consumer.accept(BotaniaBlocks.BIFROST_BRIDGE, RenderType.translucent());
		consumer.accept(BotaniaBlocks.BIFROST_PANE, RenderType.translucent());
		consumer.accept(BotaniaBlocks.BIFROST, RenderType.translucent());
		consumer.accept(BotaniaBlocks.MANA_PRISM, RenderType.translucent());

		consumer.accept(BotaniaBlocks.STARFIELD_CREATOR, RenderType.cutoutMipped());
		if (!skipPlatformBlocks) {
			// Render type is set dynamically on NeoForge and undisguised platforms should render as "solid",
			// but "translucent" is the best compromise on Fabric.
			// Translucent comes with a couple of downsides, like hidden block breaking animation and bad
			// Z-ordering for non-cubic block models that should be rendered with the "cutout" render type.
			consumer.accept(BotaniaBlocks.ABSTRUSE_PLATFORM, RenderType.translucent());
			consumer.accept(BotaniaBlocks.INFRANGIBLE_PLATFORM, RenderType.translucent());
			consumer.accept(BotaniaBlocks.SPECTRAL_PLATFORM, RenderType.translucent());
		}
		BuiltInRegistries.BLOCK.stream().filter(b -> BuiltInRegistries.BLOCK.getKey(b).getNamespace().equals(
				BotaniaAPI.MODID))
				.forEach(b -> {
					if (b instanceof FloatingFlowerBaseBlock || b instanceof FlowerBlock
							|| b instanceof TallFlowerBlock || b instanceof ShimmeringMushroomBlock
							|| b instanceof FlowerPotBlock) {
						consumer.accept(b, RenderType.cutout());
					}
				});
	}

	private BlockRenderLayers() {}
}
