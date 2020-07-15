/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.client.render.tile;

import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceResult;

import vazkii.botania.api.subtile.RadiusDescriptor;
import vazkii.botania.api.subtile.TileEntitySpecialFlower;
import vazkii.botania.client.core.handler.BlockHighlightRenderHandler;
import vazkii.botania.common.core.helper.PlayerHelper;
import vazkii.botania.common.item.ItemTwigWand;
import vazkii.botania.common.item.ModItems;
import vazkii.botania.common.item.equipment.bauble.ItemMonocle;

import java.util.Optional;

public class RenderTileSpecialFlower extends TileEntityRenderer<TileEntity> {
	public RenderTileSpecialFlower(TileEntityRendererDispatcher dispatcher) {
		super(dispatcher);
	}

	@Override
	public void render(TileEntity tile, float partialTicks, MatrixStack ms, IRenderTypeBuffer buffers, int light, int overlay) {
		if (!(tile instanceof TileEntitySpecialFlower)
				|| !(Minecraft.getInstance().renderViewEntity instanceof LivingEntity)) {
			return;
		}

		LivingEntity view = (LivingEntity) Minecraft.getInstance().renderViewEntity;
		if (!ItemMonocle.hasMonocle(view)) {
			return;
		}

		RayTraceResult ray = Minecraft.getInstance().objectMouseOver;
		if (ray != null && ray.getType() == RayTraceResult.Type.BLOCK) {
			BlockPos pos = ((BlockRayTraceResult) ray).getPos();

			if (tile.getPos().equals(pos) || hasBindingAttempt(view, tile.getPos())) {
				RadiusDescriptor descriptor = ((TileEntitySpecialFlower) tile).getRadius();
				if (descriptor != null) {
					ms.push();
					ms.translate(-tile.getPos().getX(), -tile.getPos().getY(), -tile.getPos().getZ());
					if (descriptor.isCircle()) {
						BlockHighlightRenderHandler.renderCircle(ms, buffers, descriptor.getSubtileCoords(), descriptor.getCircleRadius());
					} else {
						BlockHighlightRenderHandler.renderRectangle(ms, buffers, descriptor.getAABB(), true, null, (byte) 32);
					}
					ms.pop();
				}
			}
		}

		RenderTileFloatingFlower.renderFloatingIsland(tile, partialTicks, ms, buffers, light, overlay);
	}

	private static boolean hasBindingAttempt(LivingEntity view, BlockPos tilePos) {
		ItemStack stackHeld = PlayerHelper.getFirstHeldItem(view, ModItems.twigWand);
		if (!stackHeld.isEmpty() && ItemTwigWand.getBindMode(stackHeld)) {
			Optional<BlockPos> coords = ItemTwigWand.getBindingAttempt(stackHeld);
			return coords.isPresent() && coords.get().equals(tilePos);
		}
		return false;
	}
}
