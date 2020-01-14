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
import net.minecraft.block.SkullBlock;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.tileentity.ItemStackTileEntityRenderer;
import net.minecraft.client.renderer.tileentity.SkullTileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.entity.Entity;
import net.minecraft.entity.boss.WitherEntity;
import net.minecraft.entity.boss.dragon.EnderDragonEntity;
import net.minecraft.entity.monster.CreeperEntity;
import net.minecraft.entity.monster.SkeletonEntity;
import net.minecraft.entity.monster.WitherSkeletonEntity;
import net.minecraft.entity.monster.ZombieEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Direction;
import vazkii.botania.client.core.helper.ShaderHelper;
import vazkii.botania.client.render.entity.RenderDoppleganger;
import vazkii.botania.common.block.BlockGaiaHead;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class RenderTileGaiaHead extends SkullTileEntityRenderer {
	private SkullTileEntityRenderer original = null;

	@Override
	public void setRendererDispatcher(@Nonnull TileEntityRendererDispatcher dispatcher) {
		original = instance;
		super.setRendererDispatcher(dispatcher);
	}

	@Override
	public void render(float x, float y, float z, @Nullable Direction facing, float rotationIn, SkullBlock.ISkullType type, @Nullable GameProfile profile, int destroyStage, float animationProgress) {
		// we overwrite the vanilla `instance` singleton in setRendererDispatcher, so call back to the original one if type isn't gaia
		if (type != BlockGaiaHead.GAIA_TYPE) {
			original.render(x, y, z, facing, rotationIn, type, profile, destroyStage, animationProgress);
			return;
		}

		Minecraft mc = Minecraft.getInstance();
		Entity view = mc.getRenderViewEntity();

		profile = null;

		type = SkullBlock.Types.PLAYER;
		if(view instanceof PlayerEntity) {
			profile = ((PlayerEntity) mc.getRenderViewEntity()).getGameProfile();
		} else if (view instanceof SkeletonEntity)
			type = SkullBlock.Types.SKELETON;
		else if(view instanceof WitherSkeletonEntity)
			type = SkullBlock.Types.WITHER_SKELETON;
		else if(view instanceof WitherEntity)
			type = SkullBlock.Types.WITHER_SKELETON;
		else if(view instanceof ZombieEntity)
			type = SkullBlock.Types.ZOMBIE;
		else if(view instanceof CreeperEntity)
			type = SkullBlock.Types.CREEPER;
		else if(view instanceof EnderDragonEntity)
			type = SkullBlock.Types.DRAGON;

		ShaderHelper.useShader(ShaderHelper.BotaniaShader.DOPPLEGANGER, RenderDoppleganger.defaultCallback);
		original.render(x, y, z, facing, rotationIn, type, profile, destroyStage, animationProgress);
		ShaderHelper.releaseShader();
	}
}