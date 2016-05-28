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

import com.mojang.authlib.GameProfile;
import com.sun.istack.internal.Nullable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySkullRenderer;
import net.minecraft.tileentity.TileEntitySkull;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import vazkii.botania.client.core.helper.ShaderHelper;
import vazkii.botania.client.model.ModelSkullOverride;
import vazkii.botania.client.render.entity.RenderDoppleganger;
import vazkii.botania.common.block.tile.TileGaiaHead;

import javax.annotation.Nonnull;

public class RenderTileSkullOverride extends TileEntitySkullRenderer {
	@Override
	public void renderTileEntityAt(TileEntitySkull skull, double x, double y, double z, float partialTicks, int digProgress) {
		boolean gaia = skull == null || skull instanceof TileGaiaHead;

		if(gaia)
			ShaderHelper.useShader(ShaderHelper.doppleganger, RenderDoppleganger.defaultCallback);

		if(skull == null)
			// Copy of super that is null safe
			renderSkull((float) x, (float) y, (float) z, EnumFacing.NORTH, 0, 3, null, digProgress, partialTicks);
		else super.renderTileEntityAt(skull, x, y, z, partialTicks, digProgress);

		if(gaia)
			ShaderHelper.releaseShader();
	}

	@Override
	public void renderSkull(float x, float y, float z, @Nonnull EnumFacing facing, float rotation, int skullType, @Nullable GameProfile profile, int destroyStage, float animateTicks) {
		// Null out profile so Steve skin renders at all times
		super.renderSkull(x, y, z, facing, rotation, skullType, null, destroyStage, animateTicks);
	}
}