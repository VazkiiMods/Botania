/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Oct 31, 2014, 4:53:15 PM (GMT)]
 */
package vazkii.botania.client.render.tile;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import vazkii.botania.client.core.handler.ClientTickHandler;
import vazkii.botania.client.lib.LibResources;
import vazkii.botania.client.model.ModelBrewery;
import vazkii.botania.common.block.tile.TileBrewery;

import javax.annotation.Nullable;

public class RenderTileBrewery extends TileEntitySpecialRenderer<TileBrewery> {

	private static final ResourceLocation texture = new ResourceLocation(LibResources.MODEL_BREWERY);
	final ModelBrewery model = new ModelBrewery();
	public TileBrewery brewery;

	@Override
	public void render(@Nullable TileBrewery brewery, double d0, double d1, double d2, float f, int digProgress, float unused) {
		this.brewery = brewery;
		GlStateManager.pushMatrix();
		GlStateManager.enableRescaleNormal();
		GlStateManager.color(1F, 1F, 1F, 1F);
		GlStateManager.translate(d0, d1, d2);

		Minecraft.getMinecraft().renderEngine.bindTexture(texture);
		GlStateManager.scale(1F, -1F, -1F);
		GlStateManager.translate(0.5F, -1.5F, -0.5F);

		double time = ClientTickHandler.ticksInGame + f;

		model.render(this, time);
		GlStateManager.enableRescaleNormal();
		GlStateManager.popMatrix();
	}

	public void renderItemStack(ItemStack stack) {
		if(!stack.isEmpty()) {
			Minecraft mc = Minecraft.getMinecraft();
			GlStateManager.pushMatrix();
			mc.renderEngine.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);

			float s = 0.25F;
			GlStateManager.scale(s, s, s);
			mc.getRenderItem().renderItem(stack, ItemCameraTransforms.TransformType.GROUND);
			GlStateManager.scale(1F / s, 1F / s, 1F / s);
			GlStateManager.popMatrix();
			Minecraft.getMinecraft().renderEngine.bindTexture(texture);
		}
	}

}
