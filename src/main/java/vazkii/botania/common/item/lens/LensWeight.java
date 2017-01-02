/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Jan 24, 2015, 4:43:16 PM (GMT)]
 */
package vazkii.botania.common.item.lens;

import java.util.Map;

import com.google.common.collect.ImmutableMap;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityFallingBlock;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.RayTraceResult;
import vazkii.botania.api.internal.IManaBurst;
import vazkii.botania.common.core.handler.ConfigHandler;

public class LensWeight extends Lens {

	private static final Map<IBlockState, IBlockState> TECHNICAL_BLOCK_REMAP = ImmutableMap.of(
			Blocks.LIT_REDSTONE_ORE.getDefaultState(), Blocks.REDSTONE_ORE.getDefaultState(),
			Blocks.LIT_REDSTONE_LAMP.getDefaultState(), Blocks.REDSTONE_LAMP.getDefaultState()
			);

	@Override
	public boolean collideBurst(IManaBurst burst, EntityThrowable entity, RayTraceResult pos, boolean isManaBlock, boolean dead, ItemStack stack) {
		if(!entity.world.isRemote && !burst.isFake() && pos.getBlockPos() != null) {
			int harvestLevel = ConfigHandler.harvestLevelWeight;

			Block block = entity.world.getBlockState(pos.getBlockPos()).getBlock();
			IBlockState state = entity.world.getBlockState(pos.getBlockPos());
			int neededHarvestLevel = block.getHarvestLevel(state);

			if(entity.world.isAirBlock(pos.getBlockPos().down()) && state.getBlockHardness(entity.world, pos.getBlockPos()) != -1 && neededHarvestLevel <= harvestLevel && entity.world.getTileEntity(pos.getBlockPos()) == null && block.canSilkHarvest(entity.world, pos.getBlockPos(), state, null)) {
				state = TECHNICAL_BLOCK_REMAP.getOrDefault(state, state);
				EntityFallingBlock falling = new EntityFallingBlock(entity.world, pos.getBlockPos().getX() + 0.5, pos.getBlockPos().getY() + 0.5, pos.getBlockPos().getZ() + 0.5, state);
				falling.fallTime = 1;
				entity.world.setBlockToAir(pos.getBlockPos());
				entity.world.spawnEntity(falling);
			}
		}

		return dead;
	}

}
