/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.item.lens;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import vazkii.botania.api.internal.IManaBurst;
import vazkii.botania.api.mana.IManaBlock;
import vazkii.botania.common.Botania;
import vazkii.botania.common.block.BlockPistonRelay;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.core.handler.ConfigHandler;
import vazkii.botania.common.item.ModItems;

import java.util.List;

public class LensMine extends Lens {
	@Override
	public boolean collideBurst(IManaBurst burst, HitResult rtr, boolean isManaBlock, boolean dead, ItemStack stack) {
		Entity entity = burst.entity();
		World world = entity.world;

		if (world.isClient || rtr.getType() != HitResult.Type.BLOCK) {
			return false;
		}

		BlockPos collidePos = ((BlockHitResult) rtr).getBlockPos();
		BlockState state = world.getBlockState(collidePos);
		Block block = state.getBlock();

		ItemStack composite = ((ItemLens) stack.getItem()).getCompositeLens(stack);
		boolean warp = !composite.isEmpty() && composite.getItem() == ModItems.lensWarp;

		if (warp && (block == ModBlocks.pistonRelay || block == Blocks.PISTON || block == Blocks.MOVING_PISTON
				|| block == Blocks.PISTON_HEAD)) {
			return false;
		}

		int harvestLevel = ConfigHandler.COMMON.harvestLevelBore.getValue();

		BlockEntity tile = world.getBlockEntity(collidePos);

		float hardness = state.getHardness(world, collidePos);
		int neededHarvestLevel = -1 /* todo 1.16-fabric block.getHarvestLevel(state) */;
		int mana = burst.getMana();

		BlockPos source = burst.getBurstSourceBlockPos();
		if (!source.equals(collidePos) && !(tile instanceof IManaBlock) && neededHarvestLevel <= harvestLevel
				&& hardness != -1 && hardness < 50F && (burst.isFake() || mana >= 24)) {
			if (!burst.hasAlreadyCollidedAt(collidePos)) {
				if (!burst.isFake()) {
					List<ItemStack> items = Block.getDroppedStacks(state, (ServerWorld) world, collidePos, tile);

					if (!(world.getBlockState(collidePos).getBlock() instanceof BlockPistonRelay)) {
						world.removeBlock(collidePos, false);
						if (ConfigHandler.COMMON.blockBreakParticles.getValue()) {
							world.syncWorldEvent(2001, collidePos, Block.getRawIdFromState(state));
						}
					}

					boolean offBounds = source.getY() < 0;
					boolean doWarp = warp && !offBounds;
					BlockPos dropCoord = doWarp ? source : collidePos;

					for (ItemStack stack_ : items) {
						Block.dropStack(world, dropCoord, stack_);
					}

					burst.setMana(mana - 24);
				}
			}

			dead = false;
		}

		return dead;
	}

}
