/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */

package vazkii.botania.neoforge.mixin;

import net.minecraft.world.item.HoeItem;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.common.ItemAbilities;
import net.neoforged.neoforge.common.ItemAbility;
import net.neoforged.neoforge.common.extensions.IBlockExtension;

import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;

import vazkii.botania.common.block.BotaniaGrassBlock;

// Self-mixin to implement a method which has a forge-only param
@Mixin(BotaniaGrassBlock.class)
public abstract class BotaniaGrassBlockForgeMixin implements IBlockExtension {
	@Override
	public @Nullable BlockState getToolModifiedState(BlockState state, UseOnContext context, ItemAbility itemAbility, boolean simulate) {
		if (itemAbility == ItemAbilities.HOE_TILL && HoeItem.onlyIfAirAbove(context)) {
			return Blocks.FARMLAND.defaultBlockState();
		} else if (itemAbility == ItemAbilities.SHOVEL_FLATTEN) {
			return Blocks.DIRT_PATH.defaultBlockState();
		}
		return null;
	}
}
