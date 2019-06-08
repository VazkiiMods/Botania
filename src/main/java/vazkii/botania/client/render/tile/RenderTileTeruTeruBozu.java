/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Oct 1, 2015, 9:02:30 PM (GMT)]
 */
package vazkii.botania.client.render.tile;

import net.minecraft.client.Minecraft;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import vazkii.botania.client.core.proxy.ClientProxy;
import vazkii.botania.client.lib.LibResources;
import vazkii.botania.client.model.ModelTeruTeruBozu;
import vazkii.botania.common.Botania;
import vazkii.botania.common.block.tile.TileTeruTeruBozu;

import javax.annotation.Nullable;
import java.util.Random;

public class RenderTileTeruTeruBozu extends TileEntityRenderer<TileTeruTeruBozu> {

	private static final ResourceLocation texture = new ResourceLocation(LibResources.MODEL_TERU_TERU_BOZU);
	private static final ResourceLocation textureHalloween = new ResourceLocation(LibResources.MODEL_TERU_TERU_BOZU_HALLOWEEN);
	final ModelTeruTeruBozu model = new ModelTeruTeruBozu();

	@Override
	public void render(@Nullable TileTeruTeruBozu tileentity, double d0, double d1, double d2, float f, int digProgress) {
		GlStateManager.pushMatrix();
		GlStateManager.enableRescaleNormal();
		GlStateManager.enableBlend();
		GlStateManager.disableCull();
		GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GlStateManager.color4f(1F, 1F, 1F, 1F);
		GlStateManager.translated(d0, d1, d2);
		Minecraft.getInstance().textureManager.bindTexture(ClientProxy.dootDoot ? textureHalloween : texture);
		GlStateManager.rotatef(180F, 1F, 0F, 0F);
		double time = Botania.proxy.getWorldElapsedTicks() + f;
		boolean hasWorld = tileentity != null && tileentity.getWorld() != null;
		if(hasWorld)
			time += new Random(tileentity.getPos().hashCode()).nextInt(1000);

		GlStateManager.translatef(0.5F, -1.25F + (hasWorld ? (float) Math.sin(time * 0.01F) * 0.05F : 0F), -0.5F);
		if(hasWorld) {
			GlStateManager.rotatef((float) (time * 0.3), 0F, 1F, 0F);
			GlStateManager.rotatef(4F * (float) Math.sin(time * 0.05F), 0F, 0F, 1F);
			float s = 0.75F;
			GlStateManager.scalef(s, s, s);
		}

		model.render();
		GlStateManager.color3f(1F, 1F, 1F);
		GlStateManager.enableRescaleNormal();
		GlStateManager.enableCull();
		GlStateManager.popMatrix();
	}

}
