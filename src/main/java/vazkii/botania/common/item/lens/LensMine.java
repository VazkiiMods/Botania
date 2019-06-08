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

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.ShulkerBoxBlock;
import net.minecraft.block.ShulkerBoxBlock;
import net.minecraft.block.BlockState;
import net.minecraft.entity.projectile.ThrowableEntity;
import net.minecraft.entity.projectile.ThrowableEntity;
import net.minecraft.block.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.NonNullList;
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
	public boolean collideBurst(IManaBurst burst, ThrowableEntity entity, RayTraceResult rtr, boolean isManaBlock, boolean dead, ItemStack stack) {
		World world = entity.world;

		BlockPos collidePos = rtr.getBlockPos();
		if (world.isRemote)
			return false;

		BlockState state = world.getBlockState(collidePos);
		Block block = state.getBlock();

		ItemStack composite = ((ItemLens) stack.getItem()).getCompositeLens(stack);
		boolean warp = !composite.isEmpty() && composite.getItem() == ModItems.lensWarp;

		if(warp && (block == ModBlocks.pistonRelay || block == Blocks.PISTON || block == Blocks.MOVING_PISTON || block == Blocks.PISTON_HEAD))
			return false;

		int harvestLevel = ConfigHandler.COMMON.harvestLevelBore.get();

		TileEntity tile = world.getTileEntity(collidePos);

		float hardness = state.getBlockHardness(world, collidePos);
		int neededHarvestLevel = block.getHarvestLevel(state);
		int mana = burst.getMana();

		BlockPos source = burst.getBurstSourceBlockPos();
		if(!source.equals(rtr.getBlockPos()) && !(tile instanceof IManaBlock) && neededHarvestLevel <= harvestLevel && hardness != -1 && hardness < 50F && (burst.isFake() || mana >= 24)) {
			if(!burst.hasAlreadyCollidedAt(collidePos)) {
				if(!burst.isFake()) {
					NonNullList<ItemStack> items = NonNullList.create();
					state.getDrops(items, world, collidePos, 0);
					float chance = net.minecraftforge.event.ForgeEventFactory.fireBlockHarvesting(items, world, collidePos, state, 0, 1.0f, false, null);

					world.removeBlock(collidePos);
					if(ConfigHandler.COMMON.blockBreakParticles.get())
						world.playEvent(2001, collidePos, Block.getStateId(state));

					boolean offBounds = source.getY() < 0;
					boolean doWarp = warp && !offBounds;
					BlockPos dropCoord = doWarp ? source : collidePos;

					for(ItemStack stack_ : items) {
						// Shulker boxes do weird things and drop themselves in breakBlock, so don't drop any dupes
						if(block instanceof ShulkerBoxBlock && Block.getBlockFromItem(stack_.getItem()) instanceof ShulkerBoxBlock)
							continue;
						if(world.rand.nextFloat() <= chance)
							Block.spawnAsEntity(world, dropCoord, stack_);
					}

					burst.setMana(mana - 24);
				}
			}

			dead = false;
		}

		return dead;
	}

}
