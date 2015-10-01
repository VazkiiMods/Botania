/**
 * This class was created by <Kihira>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [? (GMT)]
 */
package vazkii.botania.client.render.tile;

import java.util.Map;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.tileentity.TileEntitySkullRenderer;
import net.minecraft.tileentity.TileEntitySkull;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import vazkii.botania.client.core.helper.ShaderHelper;
import vazkii.botania.client.model.ModelSkullOverride;
import vazkii.botania.client.render.entity.RenderDoppleganger;
import vazkii.botania.common.block.tile.TileGaiaHead;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.minecraft.MinecraftProfileTexture;

public class RenderTileSkullOverride extends TileEntitySkullRenderer {

	public static final ModelSkullOverride modelSkull = new ModelSkullOverride();

	@Override
	public void renderTileEntityAt(TileEntitySkull p_147500_1_, double p_147500_2_, double p_147500_4_, double p_147500_6_, float p_147500_8_) {
		render(p_147500_1_, (float) p_147500_2_, (float) p_147500_4_, (float) p_147500_6_, p_147500_1_.getBlockMetadata() & 7, p_147500_1_.func_145906_b() * 360 / 16.0F, p_147500_1_.func_145904_a(), p_147500_1_.func_152108_a());
	}

	public void render(TileEntitySkull skull, float par1, float par2, float par3, int par4, float par5, int par6, GameProfile gameProfile) {
		boolean gaia = skull instanceof TileGaiaHead;
		if(par6 == 3 || gaia) {
			ResourceLocation resourcelocation = AbstractClientPlayer.locationStevePng;
			Minecraft minecraft = Minecraft.getMinecraft();
			if(gaia)
				resourcelocation = minecraft.thePlayer.getLocationSkin();
			else if(gameProfile != null) {
				Map map = minecraft.func_152342_ad().func_152788_a(gameProfile);

				if (map.containsKey(MinecraftProfileTexture.Type.SKIN)) {
					resourcelocation = minecraft.func_152342_ad().func_152792_a((MinecraftProfileTexture)map.get(MinecraftProfileTexture.Type.SKIN), MinecraftProfileTexture.Type.SKIN);
				}
			}
			bindTexture(resourcelocation);
			GL11.glPushMatrix();
			GL11.glDisable(GL11.GL_CULL_FACE);
			if (par4 != 1) {
				switch (par4) {
				case 2:
					GL11.glTranslatef(par1 + 0.5F, par2 + 0.25F, par3 + 0.74F);
					break;
				case 3:
					GL11.glTranslatef(par1 + 0.5F, par2 + 0.25F, par3 + 0.26F);
					par5 = 180.0F;
					break;
				case 4:
					GL11.glTranslatef(par1 + 0.74F, par2 + 0.25F, par3 + 0.5F);
					par5 = 270.0F;
					break;
				case 5:
				default:
					GL11.glTranslatef(par1 + 0.26F, par2 + 0.25F, par3 + 0.5F);
					par5 = 90.0F;
				}
			} else GL11.glTranslatef(par1 + 0.5F, par2, par3 + 0.5F);

			GL11.glEnable(GL12.GL_RESCALE_NORMAL);
			GL11.glScalef(-1.0F, -1.0F, 1.0F);
			GL11.glEnable(GL11.GL_ALPHA_TEST);
			if(gaia)
				ShaderHelper.useShader(ShaderHelper.doppleganger, RenderDoppleganger.defaultCallback);

			modelSkull.render(null, 0F, 0F, 0F, par5, 0F, 0.0625F);

			if(gaia)
				ShaderHelper.releaseShader();
			GL11.glPopMatrix();
		} else super.func_152674_a(par1, par2, par3, par4, par5, par6, gameProfile);
	}
}