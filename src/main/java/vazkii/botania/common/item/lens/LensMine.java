/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Jan 24, 2015, 4:36:20 PM (GMT)]
 */
package vazkii.botania.common.item.lens;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import vazkii.botania.api.internal.IManaBurst;
import vazkii.botania.api.mana.IManaBlock;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.core.handler.ConfigHandler;
import vazkii.botania.common.item.ModItems;

public class LensMine extends Lens {

	@Override
	public boolean collideBurst(IManaBurst burst, EntityThrowable entity, RayTraceResult pos, boolean isManaBlock, boolean dead, ItemStack stack) {
		World world = entity.worldObj;

		BlockPos pos_ = pos.getBlockPos();
		if (world.isRemote || pos_ == null)
			return false;

		Block block = world.getBlockState(pos_).getBlock();

		ItemStack composite = ((ItemLens) ModItems.lens).getCompositeLens(stack);
		boolean warp = composite != null && composite.getItem() == ModItems.lens && composite.getItemDamage() == ItemLens.WARP;

		if(warp && (block == ModBlocks.pistonRelay || block == Blocks.PISTON || block == Blocks.PISTON_EXTENSION || block == Blocks.PISTON_HEAD))
			return false;

		int harvestLevel = ConfigHandler.harvestLevelBore;

		TileEntity tile = world.getTileEntity(pos_);

		IBlockState state = world.getBlockState(pos_);
		float hardness = state.getBlockHardness(world, pos_);
		int neededHarvestLevel = block.getHarvestLevel(state);
		int mana = burst.getMana();

		BlockPos coords = burst.getBurstSourceBlockPos();
		if(!coords.equals(pos.getBlockPos()) && !(tile instanceof IManaBlock) && neededHarvestLevel <= harvestLevel && hardness != -1 && hardness < 50F && (burst.isFake() || mana >= 24)) {
			List<ItemStack> items = new ArrayList<>();

			items.addAll(block.getDrops(world, pos_, world.getBlockState(pos_), 0));

			if(!burst.hasAlreadyCollidedAt(pos_)) {
				if(!burst.isFake() && !entity.worldObj.isRemote) {
					world.setBlockToAir(pos_);
					if(ConfigHandler.blockBreakParticles)
						entity.worldObj.playEvent(2001, pos_, Block.getStateId(state));

					boolean offBounds = coords.getY() < 0;
					boolean doWarp = warp && !offBounds;
					int dropX = doWarp ? coords.getX() : pos_.getX();
					int dropY = doWarp ? coords.getY() : pos_.getY();
					int dropZ = doWarp ? coords.getZ() : pos_.getZ();

					for(ItemStack stack_ : items)
						world.spawnEntityInWorld(new EntityItem(world, dropX + 0.5, dropY + 0.5, dropZ + 0.5, stack_));

					burst.setMana(mana - 24);
				}
			}

			dead = false;
		}

		return dead;
	}

}
