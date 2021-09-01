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
import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.ItemStackTileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.LazyValue;
import net.minecraft.util.registry.Registry;
import vazkii.botania.client.core.handler.ClientTickHandler;

public class TEISR extends ItemStackTileEntityRenderer {
	private final Block block;
	private final LazyValue<TileEntity> dummy;

	public TEISR(Block block) {
		this.block = Preconditions.checkNotNull(block);
		this.dummy = new LazyValue<>(() -> {
			TileEntityType<?> type = Registry.BLOCK_ENTITY_TYPE.getOptional(Registry.BLOCK.getKey(block)).get();
			return type.create();
		});
	}

	@Override
	public void func_239207_a_(ItemStack stack, ItemCameraTransforms.TransformType transform, MatrixStack ms, IRenderTypeBuffer buffers, int light, int overlay) {
		if (stack.getItem() == block.asItem()) {
			TileEntityRenderer<?> r = TileEntityRendererDispatcher.instance.getRenderer(dummy.getValue());
			if (r != null) {
				r.render(null, ClientTickHandler.partialTicks, ms, buffers, light, overlay);
			}
		}
	}
}
