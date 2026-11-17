/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */

package vazkii.botania.neoforge.client;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;

import vazkii.botania.client.render.block_entity.BlockEntityItemRenderer;
import vazkii.botania.client.render.entity.EntityRenderers;

import java.util.IdentityHashMap;
import java.util.Map;

public class NeoForgeBlockEntityItemRendererHelper {
	// Nulls in ctor call are fine, we don't use those fields
	private static final BlockEntityWithoutLevelRenderer RENDERER = new BlockEntityWithoutLevelRenderer(null, null) {
		private final Map<Item, BlockEntityItemRenderer> renderers = new IdentityHashMap<>();

		@Override
		public void renderByItem(ItemStack stack, ItemDisplayContext transform,
				PoseStack ps, MultiBufferSource buffers, int light, int overlay) {
			var renderer = renderers.computeIfAbsent(stack.getItem(), i -> {
				var block = Block.byItem(i);
				return EntityRenderers.BE_ITEM_RENDERER_FACTORIES.get(block).apply(block);
			});
			renderer.render(stack, transform, ps, buffers, light, overlay);
		}
	};

	public static final IClientItemExtensions PROPS = new IClientItemExtensions() {
		@Override
		public BlockEntityWithoutLevelRenderer getCustomRenderer() {
			return NeoForgeBlockEntityItemRendererHelper.RENDERER;
		}
	};
}
