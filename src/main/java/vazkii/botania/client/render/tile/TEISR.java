/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.client.render.tile;

import com.google.common.base.Preconditions;
import com.mojang.blaze3d.vertex.PoseStack;

import net.fabricmc.fabric.api.client.rendering.v1.BuiltinItemRendererRegistry;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.core.Registry;
import net.minecraft.util.LazyLoadedValue;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class TEISR implements BuiltinItemRendererRegistry.DynamicItemRenderer {
	private final Block block;
	private final LazyLoadedValue<BlockEntity> dummy;

	public TEISR(Block block) {
		this.block = Preconditions.checkNotNull(block);
		this.dummy = new LazyLoadedValue<>(() -> {
			BlockEntityType<?> type = Registry.BLOCK_ENTITY_TYPE.getOptional(Registry.BLOCK.getKey(block)).get();
			return type.create();
		});
	}

	@Override
	public void render(ItemStack stack, ItemTransforms.TransformType mode, PoseStack ms, MultiBufferSource buffers, int light, int overlay) {
		if (stack.getItem() == block.asItem()) {
			BlockEntityRenderer<?> r = BlockEntityRenderDispatcher.instance.getRenderer(dummy.get());
			if (r != null) {
				r.render(null, 0, ms, buffers, light, overlay);
			}
		}
	}
}
