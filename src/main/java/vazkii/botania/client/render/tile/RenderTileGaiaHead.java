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
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.renderer.tileentity.TileEntitySkullRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.boss.EntityWither;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.EntityWitherSkeleton;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntitySkull;
import net.minecraft.util.EnumFacing;
import vazkii.botania.client.core.helper.ShaderHelper;
import vazkii.botania.client.render.entity.RenderDoppleganger;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class RenderTileGaiaHead extends TileEntitySkullRenderer {
	@Override
	public void render(TileEntitySkull skull, double x, double y, double z, float partialTicks, int digProgress, float unused) {
		ShaderHelper.useShader(ShaderHelper.doppleganger, RenderDoppleganger.defaultCallback);

		// Null-safe copy of super
		renderSkull((float)x, (float)y, (float)z,
				skull == null ? EnumFacing.NORTH : EnumFacing.byIndex(skull.getBlockMetadata() & 7),
						skull == null ? 0 : skull.getSkullRotation() * 360 / 16.0F,
								3, null, digProgress, partialTicks);

		ShaderHelper.releaseShader();
	}

	@Override
	public void setRendererDispatcher(@Nonnull TileEntityRendererDispatcher dispatcher) {
		rendererDispatcher = dispatcher;
		// Do not set `instance` to us, interferes with vanilla skulls
	}

	@Override
	public void renderSkull(float x, float y, float z, @Nonnull EnumFacing facing, float rotation, int skullType, @Nullable GameProfile profile, int destroyStage, float animateTicks) {
		Minecraft mc = Minecraft.getMinecraft();
		Entity view = mc.getRenderViewEntity();

		profile = null;

		if(view instanceof EntityPlayer) {
			skullType = 3;
			profile = ((EntityPlayer) mc.getRenderViewEntity()).getGameProfile();
		} else if (view instanceof EntitySkeleton)
			skullType = 0;
		else if(view instanceof EntityWitherSkeleton)
			skullType = 1;
		else if(view instanceof EntityWither)
			skullType = 1;
		else if(view instanceof EntityZombie)
			skullType = 2;
		else if(view instanceof EntityCreeper)
			skullType = 4;
		else if(view instanceof EntityDragon)
			skullType = 5;

		super.renderSkull(x, y, z, facing, rotation, skullType, profile, destroyStage, animateTicks);
	}
}