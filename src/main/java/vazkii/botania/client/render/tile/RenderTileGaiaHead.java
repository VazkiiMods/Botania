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
import net.minecraft.block.AbstractSkullBlock;
import net.minecraft.block.SkullBlock;
import net.minecraft.block.SkullBlock;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.tileentity.ItemStackTileEntityRenderer;
import net.minecraft.client.renderer.tileentity.ItemStackTileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.renderer.tileentity.SkullTileEntityRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.boss.WitherEntity;
import net.minecraft.entity.boss.dragon.EnderDragonEntity;
import net.minecraft.entity.boss.WitherEntity;
import net.minecraft.entity.monster.CreeperEntity;
import net.minecraft.entity.monster.SkeletonEntity;
import net.minecraft.entity.monster.WitherSkeletonEntity;
import net.minecraft.entity.monster.ZombieEntity;
import net.minecraft.entity.monster.SkeletonEntity;
import net.minecraft.entity.monster.WitherSkeletonEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.SkullTileEntity;
import net.minecraft.util.Direction;
import vazkii.botania.client.core.helper.ShaderHelper;
import vazkii.botania.client.render.entity.RenderDoppleganger;
import vazkii.botania.common.block.BlockGaiaHead;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.block.tile.TileGaiaHead;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class RenderTileGaiaHead extends SkullTileEntityRenderer {
	public static class TEISR extends ItemStackTileEntityRenderer {
		@Override
		public void renderByItem(ItemStack stack) {
		    if(stack.getItem() == ModBlocks.gaiaHead.asItem()) {
		    	TileEntityRenderer r = TileEntityRendererDispatcher.instance.getRenderer(TileGaiaHead.class);
				((RenderTileGaiaHead) r).render(0, 0, 0, null, 180, BlockGaiaHead.GAIA_TYPE, null, -1, 0);
			}
		}
	}
	@Override
	public void setRendererDispatcher(@Nonnull TileEntityRendererDispatcher dispatcher) {
		rendererDispatcher = dispatcher;
		// Do not set `instance` to us, interferes with vanilla skulls
	}

	@Override
	public void render(float x, float y, float z, @Nullable Direction facing, float rotationIn, SkullBlock.ISkullType type, @Nullable GameProfile profile, int destroyStage, float animationProgress) {
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

		ShaderHelper.useShader(ShaderHelper.doppleganger, RenderDoppleganger.defaultCallback);
		super.render(x, y, z, facing, rotationIn, type, profile, destroyStage, animationProgress);
		ShaderHelper.releaseShader();
	}
}