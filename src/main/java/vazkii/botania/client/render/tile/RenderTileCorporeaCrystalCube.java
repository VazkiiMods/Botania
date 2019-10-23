/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Apr 30, 2015, 4:10:14 PM (GMT)]
 */
package vazkii.botania.client.render.tile;

import net.minecraft.client.Minecraft;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.block.Blocks;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import vazkii.botania.client.core.handler.ClientTickHandler;
import vazkii.botania.common.block.tile.corporea.TileCorporeaCrystalCube;

import javax.annotation.Nullable;

public class RenderTileCorporeaCrystalCube extends TileEntityRenderer<TileCorporeaCrystalCube> {
	// Ugly but there's no other way to get the model besides grabbing it from the event
	public static IBakedModel cubeModel = null;
	private ItemEntity entity = null;
	private ItemRenderer itemRenderer = null;

	@Override
	public void render(@Nullable TileCorporeaCrystalCube cube, double d0, double d1, double d2, float f, int digProgress) {
		ItemStack stack = ItemStack.EMPTY;
		if (cube != null) {
			if(entity == null)
				entity = new ItemEntity(cube.getWorld(), cube.getPos().getX(), cube.getPos().getY(), cube.getPos().getZ(), new ItemStack(Blocks.STONE));

			if(itemRenderer == null)
				itemRenderer = new ItemRenderer(Minecraft.getInstance().getRenderManager(), Minecraft.getInstance().getItemRenderer()) {
				@Override
				public boolean shouldBob() {
					return false;
				}
			};

			entity.age = ClientTickHandler.ticksInGame;
			stack = cube.getRequestTarget();
			entity.setItem(stack);
		}

		double time = ClientTickHandler.ticksInGame + f;
		double worldTicks = cube == null || cube.getWorld() == null ? 0 : time;

		Minecraft mc = Minecraft.getInstance();
		GlStateManager.pushMatrix();
		GlStateManager.enableRescaleNormal();
		GlStateManager.enableBlend();
		GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
		GlStateManager.color4f(1F, 1F, 1F, 1F);
		GlStateManager.translated(d0, d1, d2);
		GlStateManager.translatef(0.5F, 1.5F, 0.5F);
		GlStateManager.scalef(1F, -1F, -1F);
		GlStateManager.translatef(0F, (float) Math.sin(worldTicks / 20.0 * 1.55) * 0.025F, 0F);
		if(!stack.isEmpty()) {
			GlStateManager.pushMatrix();
			float s = stack.getItem() instanceof BlockItem ? 0.7F : 0.5F;
			GlStateManager.translatef(0F, 0.8F, 0F);
			GlStateManager.scalef(s, s, s);
			GlStateManager.rotatef(180F, 0F, 0F, 1F);
			itemRenderer.doRender(entity, 0, 0, 0, 1F, f);
			GlStateManager.popMatrix();
		}

		if (cubeModel != null) {
			GlStateManager.pushMatrix();
			GlStateManager.enableBlend();
			GlStateManager.translatef(-0.5F, 0.25F, -0.5F);
			Minecraft.getInstance().getBlockRendererDispatcher().getBlockModelRenderer().renderModelBrightnessColor(cubeModel, 1, 1, 1, 1);
			GlStateManager.popMatrix();
		}

		GlStateManager.color3f(1F, 1F, 1F);

		if(!stack.isEmpty()) {
			int count = cube.getItemCount();
			String countStr = "" + count;
			int color = 0xFFFFFF;
			if(count > 9999) {
				countStr = count / 1000 + "K";
				color = 0xFFFF00;
				if(count > 9999999) {
					countStr = count / 10000000 + "M";
					color = 0x00FF00;
				}
			}
			color |= 0xA0 << 24;
			int colorShade = (color & 16579836) >> 2 | color & -16777216;

			float s = 1F / 64F;
			GlStateManager.scalef(s, s, s);
			GlStateManager.disableLighting();
			int l = mc.fontRenderer.getStringWidth(countStr);

			GlStateManager.translatef(0F, 55F, 0F);
			float tr = -16.5F;
			for(int i = 0; i < 4; i++) {
				GlStateManager.rotatef(90F, 0F, 1F, 0F);
				GlStateManager.translatef(0F, 0F, tr);
				mc.fontRenderer.drawString(countStr, -l / 2, 0, color);
				GlStateManager.translatef(0F, 0F, 0.1F);
				mc.fontRenderer.drawString(countStr, -l / 2 + 1, 1, colorShade);
				GlStateManager.translatef(0F, 0F, -tr - 0.1F);
			}
			GlStateManager.enableLighting();
		}

		GlStateManager.enableRescaleNormal();
		GlStateManager.popMatrix();
	}
}
