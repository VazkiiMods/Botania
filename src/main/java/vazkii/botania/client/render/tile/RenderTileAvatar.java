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
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.platform.GLX;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.tileentity.ItemStackTileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import vazkii.botania.api.item.IAvatarWieldable;
import vazkii.botania.api.state.BotaniaStateProps;
import vazkii.botania.client.core.handler.ClientTickHandler;
import vazkii.botania.client.lib.LibResources;
import vazkii.botania.client.model.ModelAvatar;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.block.tile.TileAvatar;

import javax.annotation.Nullable;

public class RenderTileAvatar extends TileEntityRenderer<TileAvatar> {

	private static final float[] ROTATIONS = new float[] {
			180F, 0F, 90F, 270F
	};

	private static final ResourceLocation texture = new ResourceLocation(LibResources.MODEL_AVATAR);
	private static final ModelAvatar model = new ModelAvatar();

	@Override
	public void render(@Nullable TileAvatar avatar, double d0, double d1, double d2, float pticks, int digProgress) {
		if (avatar != null)
			if (!avatar.getWorld().isBlockLoaded(avatar.getPos())
					|| avatar.getWorld().getBlockState(avatar.getPos()).getBlock() != ModBlocks.avatar)
				return;

		GlStateManager.pushMatrix();
		GlStateManager.enableRescaleNormal();
		GlStateManager.color4f(1F, 1F, 1F, 1F);
		GlStateManager.translated(d0, d1, d2);

		Minecraft.getInstance().textureManager.bindTexture(texture);
		Direction facing = avatar != null && avatar.getWorld() != null
				? avatar.getWorld().getBlockState(avatar.getPos()).get(BotaniaStateProps.CARDINALS)
						: Direction.SOUTH;

				GlStateManager.translatef(0.5F, 1.6F, 0.5F);
				GlStateManager.scalef(1F, -1F, -1F);
				GlStateManager.rotatef(ROTATIONS[Math.max(Math.min(ROTATIONS.length - 1, facing.getIndex() - 2), 0)], 0F, 1F, 0F);
				model.render();

				if (avatar == null) {
					GlStateManager.color3f(1F, 1F, 1F);
					GlStateManager.scalef(1F, -1F, -1F);
					GlStateManager.enableRescaleNormal();
					GlStateManager.popMatrix();
					return;
				}

				ItemStack stack = avatar.getItemHandler().getStackInSlot(0);
				if(!stack.isEmpty()) {
					GlStateManager.pushMatrix();
					Minecraft.getInstance().textureManager.bindTexture(AtlasTexture.LOCATION_BLOCKS_TEXTURE);
					float s = 0.6F;
					GlStateManager.scalef(s, s, s);
					GlStateManager.translatef(-0.5F, 2F, -0.25F);
					GlStateManager.rotatef(-70, 1, 0, 0);
					Minecraft.getInstance().getItemRenderer().renderItem(stack, ItemCameraTransforms.TransformType.THIRD_PERSON_RIGHT_HAND);
					GlStateManager.popMatrix();

					IAvatarWieldable wieldable = (IAvatarWieldable) stack.getItem();
					Minecraft.getInstance().textureManager.bindTexture(wieldable.getOverlayResource(avatar, stack));
					s = 1.01F;

					GlStateManager.pushMatrix();
					GlStateManager.enableBlend();
					GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
					GlStateManager.scalef(s, s, s);
					GlStateManager.translatef(0F, -0.01F, 0F);
					int light = 15728880;
					int lightmapX = light % 65536;
					int lightmapY = light / 65536;
					GLX.glMultiTexCoord2f(GLX.GL_TEXTURE1, lightmapX, lightmapY);
					float alpha = (float) Math.sin(ClientTickHandler.ticksInGame / 20D) / 2F + 0.5F;
					GlStateManager.color4f(1F, 1F, 1F, alpha + 0.183F);
					model.render();
					GlStateManager.popMatrix();
				}
				GlStateManager.color3f(1F, 1F, 1F);
				GlStateManager.scalef(1F, -1F, -1F);
				GlStateManager.enableRescaleNormal();
				GlStateManager.popMatrix();
	}

}
