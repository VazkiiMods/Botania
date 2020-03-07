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
import com.mojang.blaze3d.vertex.IVertexBuilder;

import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.Vector3f;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import vazkii.botania.client.core.handler.ClientTickHandler;
import vazkii.botania.client.lib.LibResources;
import vazkii.botania.client.model.ModelHourglass;
import vazkii.botania.common.block.tile.TileHourglass;

import javax.annotation.Nullable;

import java.util.Random;

public class RenderTileHourglass extends TileEntityRenderer<TileHourglass> {

	final ResourceLocation texture = new ResourceLocation(LibResources.MODEL_HOURGLASS);
	final ModelHourglass model = new ModelHourglass();

	public RenderTileHourglass(TileEntityRendererDispatcher manager) {
		super(manager);
	}

	@Override
	public void render(@Nullable TileHourglass hourglass, float ticks, MatrixStack ms, IRenderTypeBuffer buffers, int light, int overlay) {
		ms.push();
		boolean hasWorld = hourglass != null && hourglass.getWorld() != null;
		int wtime = !hasWorld ? 0 : ClientTickHandler.ticksInGame;
		if (wtime != 0) {
			wtime += new Random(hourglass.getPos().hashCode()).nextInt(360);
		}

		float time = wtime == 0 ? 0 : wtime + ticks;
		float x = 0.5F + (float) Math.cos(time * 0.05F) * 0.025F;
		float y = 0.55F + (float) (Math.sin(time * 0.04F) + 1F) * 0.05F;
		float z = 0.5F + (float) Math.sin(time * 0.05F) * 0.025F;
		ItemStack stack = hasWorld ? hourglass.getItemHandler().getStackInSlot(0) : ItemStack.EMPTY;

		float activeFraction = stack.isEmpty() ? 0 : hourglass.lastFraction + (hourglass.timeFraction - hourglass.lastFraction) * ticks;
		float fract1 = stack.isEmpty() ? 0 : activeFraction;
		float fract2 = stack.isEmpty() ? 0 : 1F - activeFraction;
		ms.translate(x, y, z);

		float rot = hasWorld && hourglass.flip ? 180F : 1F;
		if (hasWorld && hourglass.flipTicks > 0) {
			rot += (hourglass.flipTicks - ticks) * (180F / 4F);
		}
		ms.multiply(Vector3f.POSITIVE_Z.getDegreesQuaternion(rot));

		ms.scale(1F, -1F, -1F);
		int color = hasWorld ? hourglass.getColor() : 0;
		float r = (color >> 16) / 255.0F;
		float g = (color >> 8) / 255.0F;
		float b = (color & 0xFF) / 255.0F;
		IVertexBuilder buffer = buffers.getBuffer(model.getLayer(texture));
		model.render(ms, buffer, light, overlay, r, g, b, 1, fract1, fract2, hasWorld && hourglass.flip);
		ms.pop();
	}

}
