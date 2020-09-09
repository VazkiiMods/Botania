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
import net.fabricmc.fabric.api.client.rendering.v1.BuiltinItemRenderer;
import net.fabricmc.fabric.api.client.rendering.v1.BuiltinItemRendererRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Lazy;
import net.minecraft.util.registry.Registry;

public class TEISR implements BuiltinItemRendererRegistry.DynamicItemRenderer {
	private final Block block;
	private final Lazy<BlockEntity> dummy;

	public TEISR(Block block) {
		this.block = Preconditions.checkNotNull(block);
		this.dummy = new Lazy<>(() -> {
			BlockEntityType<?> type = Registry.BLOCK_ENTITY_TYPE.getOrEmpty(Registry.BLOCK.getId(block)).get();
			return type.instantiate();
		});
	}

	@Override
	public void render(ItemStack stack, ModelTransformation.Mode mode, MatrixStack ms, VertexConsumerProvider buffers, int light, int overlay) {
		if (stack.getItem() == block.asItem()) {
			BlockEntityRenderer<?> r = BlockEntityRenderDispatcher.INSTANCE.get(dummy.get());
			if (r != null) {
				r.render(null, 0, ms, buffers, light, overlay);
			}
		}
	}
}
