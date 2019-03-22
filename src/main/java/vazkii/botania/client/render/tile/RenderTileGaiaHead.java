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
import net.minecraft.block.BlockAbstractSkull;
import net.minecraft.block.BlockSkull;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.tileentity.TileEntityItemStackRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
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
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntitySkull;
import net.minecraft.util.EnumFacing;
import vazkii.botania.client.core.helper.ShaderHelper;
import vazkii.botania.client.render.entity.RenderDoppleganger;
import vazkii.botania.common.block.BlockGaiaHead;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.block.tile.TileGaiaHead;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class RenderTileGaiaHead extends TileEntitySkullRenderer {
	public static class TEISR extends TileEntityItemStackRenderer {
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
	public void render(float x, float y, float z, @Nullable EnumFacing facing, float rotationIn, BlockSkull.ISkullType type, @Nullable GameProfile profile, int destroyStage, float animationProgress) {
		Minecraft mc = Minecraft.getInstance();
		Entity view = mc.getRenderViewEntity();

		profile = null;

		type = BlockSkull.Types.PLAYER;
		if(view instanceof EntityPlayer) {
			profile = ((EntityPlayer) mc.getRenderViewEntity()).getGameProfile();
		} else if (view instanceof EntitySkeleton)
			type = BlockSkull.Types.SKELETON;
		else if(view instanceof EntityWitherSkeleton)
			type = BlockSkull.Types.WITHER_SKELETON;
		else if(view instanceof EntityWither)
			type = BlockSkull.Types.WITHER_SKELETON;
		else if(view instanceof EntityZombie)
			type = BlockSkull.Types.ZOMBIE;
		else if(view instanceof EntityCreeper)
			type = BlockSkull.Types.CREEPER;
		else if(view instanceof EntityDragon)
			type = BlockSkull.Types.DRAGON;

		ShaderHelper.useShader(ShaderHelper.doppleganger, RenderDoppleganger.defaultCallback);
		super.render(x, y, z, facing, rotationIn, type, profile, destroyStage, animationProgress);
		ShaderHelper.releaseShader();
	}
}