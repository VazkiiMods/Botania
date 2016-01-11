/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [Oct 24, 2015, 3:51:35 PM (GMT)]
 */
package vazkii.botania.client.render.tile;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import vazkii.botania.api.item.IAvatarWieldable;
import vazkii.botania.api.state.BotaniaStateProps;
import vazkii.botania.client.core.handler.ClientTickHandler;
import vazkii.botania.client.lib.LibResources;
import vazkii.botania.client.model.ModelAvatar;
import vazkii.botania.common.block.tile.TileAvatar;

public class RenderTileAvatar extends TileEntitySpecialRenderer<TileAvatar> {

	private static final float[] ROTATIONS = new float[] {
		180F, 0F, 90F, 270F
	};

	private static final ResourceLocation texture = new ResourceLocation(LibResources.MODEL_AVATAR);
	private static final ModelAvatar model = new ModelAvatar();

	@Override
	public void renderTileEntityAt(TileAvatar avatar, double d0, double d1, double d2, float pticks, int digProgress) {
		if (avatar != null && avatar.getWorld() != null && !avatar.getWorld().isBlockLoaded(avatar.getPos(), false)) {
			return;
		}

		GlStateManager.pushMatrix();
		GlStateManager.enableRescaleNormal();
		GlStateManager.color(1F, 1F, 1F, 1F);
		GlStateManager.translate(d0, d1, d2);

		Minecraft.getMinecraft().renderEngine.bindTexture(texture);
		EnumFacing facing = avatar != null && avatar.getWorld() != null
				? avatar.getWorld().getBlockState(avatar.getPos()).getValue(BotaniaStateProps.CARDINALS)
				: EnumFacing.SOUTH;

		GlStateManager.translate(0.5F, 1.6F, 0.5F);
		GlStateManager.scale(1F, -1F, -1F);
		GlStateManager.rotate(ROTATIONS[Math.max(Math.min(ROTATIONS.length - 1, facing.getIndex() - 2), 0)], 0F, 1F, 0F);
		model.render();

		if (avatar == null) {
			GlStateManager.color(1F, 1F, 1F);
			GlStateManager.scale(1F, -1F, -1F);
			GlStateManager.enableRescaleNormal();
			GlStateManager.popMatrix();
			return;
		}

		ItemStack stack = avatar.getStackInSlot(0);
		if(stack != null) {
			GlStateManager.pushMatrix();
			Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.locationBlocksTexture);
			float s = 0.6F;
			GlStateManager.scale(s, s, s);
			GlStateManager.translate(-0.5F, 2.125F, 0F); // todo 1.8 tweak held item render
			Minecraft.getMinecraft().getRenderItem().renderItem(stack, ItemCameraTransforms.TransformType.THIRD_PERSON);
			GlStateManager.popMatrix();

			IAvatarWieldable wieldable = (IAvatarWieldable) stack.getItem();
			Minecraft.getMinecraft().renderEngine.bindTexture(wieldable.getOverlayResource(avatar, stack));
			s = 1.01F;

			GlStateManager.pushMatrix();
			GlStateManager.enableBlend();
			GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
			GlStateManager.scale(s, s, s);
			GlStateManager.translate(0F, -0.01F, 0F);
			int light = 15728880;
			int lightmapX = light % 65536;
			int lightmapY = light / 65536;
			OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, lightmapX, lightmapY);
			float alpha = (float) Math.sin(ClientTickHandler.ticksInGame / 20D) / 2F + 0.5F;
			GlStateManager.color(1F, 1F, 1F, alpha + 0.183F);
			model.render();
			GlStateManager.popMatrix();
		}
		GlStateManager.color(1F, 1F, 1F);
		GlStateManager.scale(1F, -1F, -1F);
		GlStateManager.enableRescaleNormal();
		GlStateManager.popMatrix();
	}

}
