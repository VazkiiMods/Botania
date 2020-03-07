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

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.block.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Atlases;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.Vector3f;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.entity.item.ItemEntity;
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

	public RenderTileCorporeaCrystalCube(TileEntityRendererDispatcher manager) {
		super(manager);
	}

	@Override
	public void render(@Nullable TileCorporeaCrystalCube cube, float f, MatrixStack ms, IRenderTypeBuffer buffers, int light, int overlay) {
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
		ms.push();
		ms.translate(0.5F, 1.5F, 0.5F);
		ms.scale(1F, -1F, -1F);
		ms.translate(0F, (float) Math.sin(worldTicks / 20.0 * 1.55) * 0.025F, 0F);

		if(!stack.isEmpty()) {
		    ms.push();
			float s = stack.getItem() instanceof BlockItem ? 0.7F : 0.5F;
			ms.translate(0F, 0.8F, 0F);
			ms.scale(s, s, s);
			ms.multiply(Vector3f.POSITIVE_Z.getDegreesQuaternion(180F));
			itemRenderer.render(entity, 0, f, ms, buffers, light);
			ms.pop();
		}

		if (cubeModel != null) {
			ms.push();
			ms.translate(-0.5F, 0.25F, -0.5F);
			IVertexBuilder buffer = buffers.getBuffer(Atlases.getEntityTranslucent());
			Minecraft.getInstance().getBlockRendererDispatcher().getBlockModelRenderer().render(ms.peek(), buffer, null, cubeModel, 1, 1, 1, light, overlay);
			ms.pop();
		}

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
			ms.scale(s, s, s);
			int l = mc.fontRenderer.getStringWidth(countStr);

			ms.translate(0F, 55F, 0F);
			float tr = -16.5F;
			for(int i = 0; i < 4; i++) {
				ms.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion(90F));
				ms.translate(0F, 0F, tr);
				mc.fontRenderer.draw(countStr, -l / 2, 0, color, false, ms.peek().getModel(), buffers, false, 0, light);
				ms.translate(0F, 0F, 0.1F);
				mc.fontRenderer.draw(countStr, -l / 2 + 1, 1, colorShade, false, ms.peek().getModel(), buffers, false, 0, light);
				ms.translate(0F, 0F, -tr - 0.1F);
			}
		}

		ms.pop();
	}
}
