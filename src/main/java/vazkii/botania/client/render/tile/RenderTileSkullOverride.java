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
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySkullRenderer;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.tileentity.TileEntitySkull;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;

import vazkii.botania.client.core.helper.ShaderHelper;
import vazkii.botania.client.model.ModelSkullOverride;
import vazkii.botania.client.render.entity.RenderDoppleganger;
import vazkii.botania.common.block.tile.TileGaiaHead;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.minecraft.MinecraftProfileTexture;

public class RenderTileSkullOverride extends TileEntitySkullRenderer {

	public static final ModelSkullOverride modelSkull = new ModelSkullOverride();

	@Override
	public void renderTileEntityAt(TileEntitySkull p_147500_1_, double p_147500_2_, double p_147500_4_, double p_147500_6_, float p_147500_8_, int digProgress) {
		if (p_147500_1_ != null) {
			render(p_147500_1_, (float) p_147500_2_, (float) p_147500_4_, (float) p_147500_6_, EnumFacing.getFront(p_147500_1_.getBlockMetadata() & 7) /*todo 1.8*/, p_147500_1_.getSkullRotation() * 360 / 16.0F, p_147500_1_.getSkullType(), p_147500_1_.getPlayerProfile(), digProgress);
		} else {
			render(null, ((float) p_147500_2_), ((float) p_147500_4_), ((float) p_147500_6_), EnumFacing.NORTH, 0, 3, null, 0);
		}
	}

	public void render(TileEntitySkull skull, float par1, float par2, float par3, EnumFacing par4, float par5, int par6, GameProfile gameProfile, int digProgress) {
		boolean gaia = skull == null || skull instanceof TileGaiaHead;
		if(par6 == 3 || gaia) {
			ResourceLocation resourcelocation = DefaultPlayerSkin.getDefaultSkinLegacy();
			Minecraft minecraft = Minecraft.getMinecraft();
			if(gaia)
				resourcelocation = minecraft.thePlayer.getLocationSkin();
			else if(gameProfile != null) {
				Map map = minecraft.getSkinManager().loadSkinFromCache(gameProfile);

				if (map.containsKey(MinecraftProfileTexture.Type.SKIN)) {
					resourcelocation = minecraft.getSkinManager().loadSkin((MinecraftProfileTexture)map.get(MinecraftProfileTexture.Type.SKIN), MinecraftProfileTexture.Type.SKIN);
				}
			}
			bindTexture(resourcelocation);
			GlStateManager.pushMatrix();
			GlStateManager.disableCull();
			if (par4 != EnumFacing.UP) {
				switch (par4) {
				case NORTH:
					GlStateManager.translate(par1 + 0.5F, par2 + 0.25F, par3 + 0.74F);
					break;
				case SOUTH:
					GlStateManager.translate(par1 + 0.5F, par2 + 0.25F, par3 + 0.26F);
					par5 = 180.0F;
					break;
				case WEST:
					GlStateManager.translate(par1 + 0.74F, par2 + 0.25F, par3 + 0.5F);
					par5 = 270.0F;
					break;
				case EAST:
				default:
					GlStateManager.translate(par1 + 0.26F, par2 + 0.25F, par3 + 0.5F);
					par5 = 90.0F;
				}
			} else GlStateManager.translate(par1 + 0.5F, par2, par3 + 0.5F);

			GlStateManager.enableRescaleNormal();
			GlStateManager.scale(-1.0F, -1.0F, 1.0F);
			GlStateManager.enableAlpha();
			if(gaia)
				ShaderHelper.useShader(ShaderHelper.doppleganger, RenderDoppleganger.defaultCallback);

			modelSkull.render(null, 0F, 0F, 0F, par5, 0F, 0.0625F);

			if(gaia)
				ShaderHelper.releaseShader();
			GlStateManager.popMatrix();
		} else super.renderSkull(par1, par2, par3, par4, par5, par6, gameProfile, digProgress);
	}
}