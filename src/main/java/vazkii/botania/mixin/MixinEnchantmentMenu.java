/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.world.inventory.EnchantmentMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockState;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import vazkii.botania.common.block.BlockPylon;

@Mixin(EnchantmentMenu.class)
public class MixinEnchantmentMenu {
	@SuppressWarnings("target")
	@ModifyVariable(
		method = "lambda$slotsChanged$0(Lnet/minecraft/world/item/ItemStack;"
				+ "Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;)V",
		at = @At(value = "STORE", ordinal = 0),
		ordinal = 0
	)
	private int botaniaPylonEnchanting(int obj, ItemStack stack, Level level, BlockPos pos) {
		for (int x = -1; x <= 1; ++x) {
			for (int z = -1; z <= 1; ++z) {
				if ((x != 0 || z != 0) && level.isEmptyBlock(pos.offset(x, 0, z)) && level.isEmptyBlock(pos.offset(x, 1, z))) {
					obj += getPylonValue(level.getBlockState(pos.offset(x * 2, 0, z * 2)), level, pos);
					obj += getPylonValue(level.getBlockState(pos.offset(x * 2, 1, z * 2)), level, pos);
					if (x != 0 && z != 0) {
						obj += getPylonValue(level.getBlockState(pos.offset(x * 2, 0, z)), level, pos);
						obj += getPylonValue(level.getBlockState(pos.offset(x * 2, 1, z)), level, pos);
						obj += getPylonValue(level.getBlockState(pos.offset(x, 0, z * 2)), level, pos);
						obj += getPylonValue(level.getBlockState(pos.offset(x, 1, z * 2)), level, pos);
					}
				}
			}
		}
		return obj;
	}

	@Unique
	private float getPylonValue(BlockState state, LevelReader world, BlockPos pos) {
		if (state.getBlock() instanceof BlockPylon pylon) {
			return pylon.getEnchantPowerBonus(state, world, pos);
		}
		return 0;
	}
}
