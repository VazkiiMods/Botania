/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Mar 15, 2014, 5:04:42 PM (GMT)]
 */
package vazkii.botania.client.render.tile;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.item.EntityItem;
import org.lwjgl.opengl.GL11;
import vazkii.botania.client.core.handler.ClientTickHandler;
import vazkii.botania.client.core.handler.MiscellaneousIcons;
import vazkii.botania.client.core.helper.ShaderHelper;
import vazkii.botania.client.core.proxy.ClientProxy;
import vazkii.botania.common.block.tile.TileEnchanter;

import javax.annotation.Nonnull;

public class RenderTileEnchanter extends TileEntitySpecialRenderer<TileEnchanter> {

	private EntityItem item;

	@Override
	public void render(@Nonnull TileEnchanter enchanter, double d0, double d1, double d2, float f, int digProgress, float unused) {
		float alphaMod = 0F;

		if(enchanter.stage == TileEnchanter.State.GATHER_MANA)
			alphaMod = Math.min(20, enchanter.stageTicks) / 20F;
		else if(enchanter.stage == TileEnchanter.State.RESET)
			alphaMod = (20 - enchanter.stageTicks) / 20F;
		else if(enchanter.stage == TileEnchanter.State.DO_ENCHANT)
			alphaMod = 1F;

		if(!enchanter.itemToEnchant.isEmpty()) {
			if(item == null)
				item = new EntityItem(enchanter.getWorld(), enchanter.getPos().getX(), enchanter.getPos().getY() + 1, enchanter.getPos().getZ(), enchanter.itemToEnchant);

			item.age = ClientTickHandler.ticksInGame;
			item.setItem(enchanter.itemToEnchant);

			GlStateManager.color(1F, 1F, 1F, 1F);
			GlStateManager.translate(0.5F, 1.25F, 0.5F);
			((Render) Minecraft.getMinecraft().getRenderManager().entityRenderMap.get(EntityItem.class)).doRender(item, d0, d1, d2, 1F, f);
			GlStateManager.translate(-0.5F, -1.25F, -0.5F);
		}

		GlStateManager.pushMatrix();
		GlStateManager.translate(d0, d1, d2);

		GlStateManager.rotate(90F, 1F, 0F, 0F);
		GlStateManager.translate(-2F, -2F, -0.001F);

		GlStateManager.enableBlend();
		GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GlStateManager.color(1F, 1F, 1F, 1F);
		GlStateManager.disableAlpha();
		float alpha = (float) ((Math.sin((ClientTickHandler.ticksInGame + f) / 8D) + 1D) / 5D + 0.4D) * alphaMod;

		if(alpha > 0) {
			if(ShaderHelper.useShaders())
				GlStateManager.color(1F, 1F, 1F, alpha);
			else {
				int light = 15728880;
				int lightmapX = light % 65536;
				int lightmapY = light / 65536;
				OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, lightmapX, lightmapY);
				GlStateManager.color(0.6F + (float) ((Math.cos((ClientTickHandler.ticksInGame + f) / 6D) + 1D) / 5D), 0.1F, 0.9F, alpha);
			}

			Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);

			if(enchanter.stage == TileEnchanter.State.DO_ENCHANT || enchanter.stage == TileEnchanter.State.RESET) {
				int ticks = enchanter.stageTicks + enchanter.stage3EndTicks;
				int angle = ticks * 2;
				float yTranslation = Math.min(20, ticks) / 20F * 1.15F;
				float scale = ticks < 10 ? 1F : 1F - Math.min(20, ticks - 10) / 20F * 0.75F;

				GlStateManager.translate(2.5F, 2.5F, -yTranslation);
				GlStateManager.scale(scale, scale, 1F);
				GlStateManager.rotate(angle, 0F, 0F, 1F);
				GlStateManager.translate(-2.5F, -2.5F, 0F);
			}

			ShaderHelper.useShader(ShaderHelper.enchanterRune);
			renderIcon(0, 0, MiscellaneousIcons.INSTANCE.enchanterOverlay, 5, 5, 240);
			ShaderHelper.releaseShader();
		}

		GlStateManager.enableAlpha();
		GlStateManager.disableBlend();
		GlStateManager.color(1F, 1F, 1F, 1F);
		GlStateManager.popMatrix();
	}

	private void renderIcon(int par1, int par2, TextureAtlasSprite par3Icon, int par4, int par5, int brightness) {
		Tessellator tessellator = Tessellator.getInstance();
		tessellator.getBuffer().begin(GL11.GL_QUADS, ClientProxy.POSITION_TEX_LMAP);
		tessellator.getBuffer().pos(par1 + 0, par2 + par5, 0).tex(par3Icon.getMinU(), par3Icon.getMaxV()).lightmap(brightness, brightness).endVertex();
		tessellator.getBuffer().pos(par1 + par4, par2 + par5, 0).tex(par3Icon.getMaxU(), par3Icon.getMaxV()).lightmap(brightness, brightness).endVertex();
		tessellator.getBuffer().pos(par1 + par4, par2 + 0, 0).tex(par3Icon.getMaxU(), par3Icon.getMinV()).lightmap(brightness, brightness).endVertex();
		tessellator.getBuffer().pos(par1 + 0, par2 + 0, 0).tex(par3Icon.getMinU(), par3Icon.getMinV()).lightmap(brightness, brightness).endVertex();
		tessellator.draw();
	}

}
