/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under a
 * Creative Commons Attribution-NonCommercial-ShareAlike 3.0 License
 * (http://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB)
 * 
 * File Created @ [Jan 25, 2014, 9:42:31 PM (GMT)]
 */
package vazkii.botania.client.render.tile;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import vazkii.botania.api.mana.ILens;
import vazkii.botania.client.core.handler.ClientTickHandler;
import vazkii.botania.client.lib.LibResources;
import vazkii.botania.client.model.ModelSpreader;
import vazkii.botania.client.render.item.RenderLens;
import vazkii.botania.common.block.tile.mana.TileSpreader;

public class RenderTileSpreader extends TileEntitySpecialRenderer {

	private static final ResourceLocation texture = new ResourceLocation(LibResources.MODEL_SPREADER);
	private static final ResourceLocation textureRs = new ResourceLocation(LibResources.MODEL_SPREADER_REDSTONE);
	private static final ResourceLocation textureDw = new ResourceLocation(LibResources.MODEL_SPREADER_DREAMWOOD);

	private static final ModelSpreader model = new ModelSpreader();

	@Override
	public void renderTileEntityAt(TileEntity tileentity, double d0, double d1, double d2, float f) {
		TileSpreader spreader = (TileSpreader) tileentity;
		GL11.glPushMatrix();
		GL11.glEnable(GL12.GL_RESCALE_NORMAL);
		GL11.glColor4f(1F, 1F, 1F, 1F);
		GL11.glTranslated(d0, d1, d2);

		GL11.glTranslatef(0.5F, 1.5F, 0.5F);
		GL11.glRotatef(spreader.rotationX + 90F, 0F, 1F, 0F);
		GL11.glTranslatef(0F, -1F, 0F);
		GL11.glRotatef(spreader.rotationY, 1F, 0F, 0F);
		GL11.glTranslatef(0F, 1F, 0F);

		Minecraft.getMinecraft().renderEngine.bindTexture(spreader.isRedstone() ? textureRs : spreader.isDreamwood() ? textureDw : texture);
		GL11.glScalef(1F, -1F, -1F);
		model.render();

		GL11.glPushMatrix();
		long worldTicks = tileentity.getWorldObj() == null ? 0 : ClientTickHandler.ticksInGame;
		GL11.glRotatef(worldTicks % 360, 0F, 1F, 0F);
		GL11.glTranslatef(0F, (float) Math.sin(worldTicks / 20.0) * 0.05F, 0F);
		model.renderCube();
		GL11.glPopMatrix();
		GL11.glScalef(1F, -1F, -1F);
		ItemStack stack = spreader.getStackInSlot(0);

		if(stack != null) {
			Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.locationItemsTexture);
			ILens lens = (ILens) stack.getItem();
			GL11.glPushMatrix();
			GL11.glTranslatef(-0.4F, -1.4F, -0.4375F);
			GL11.glScalef(0.8F, 0.8F, 0.8F);
			RenderLens.render(stack, lens.getLensColor(stack));
			GL11.glPopMatrix();
		}
		GL11.glEnable(GL12.GL_RESCALE_NORMAL);
		GL11.glPopMatrix();
	}

}
