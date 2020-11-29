/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.mixin;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.EnchantmentScreenHandler;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import vazkii.botania.common.block.BlockPylon;
import vazkii.botania.common.block.ModBlocks;

@Mixin(EnchantmentScreenHandler.class)
public abstract class MixinEnchantmentScreenHandler {

	@ModifyVariable(method = "method_17411", at = @At(value = "STORE", ordinal = 0), ordinal = 0, remap = false)
	private int prependPylonChecks(int i, ItemStack stack, World world, BlockPos pos) {
		// [VanillaCopy] most of the loop, replaced checks with a method call. ineffective, but I don't know if any enchantment power hooks exist
		for (int z = -1; z <= 1; ++z) {
			for (int x = -1; x <= 1; ++x) {
				if ((z != 0 || x != 0) && world.isAir(pos.add(x, 0, z)) && world.isAir(pos.add(x, 1, z))) {
					for (int y = 0; y < 2; y++) {
						i += botania_getPower(world.getBlockState(pos.add(x * 2, y, z * 2)));
						if (x != 0 && z != 0) {
							i += botania_getPower(world.getBlockState(pos.add(x * 2, y, z)));
							i += botania_getPower(world.getBlockState(pos.add(x, y, z * 2)));
						}
					}
				}
			}
		}
		return i;
	}

	private int botania_getPower(BlockState state) {
		Block block = state.getBlock();
		if (block instanceof BlockPylon) {
			return block == ModBlocks.manaPylon ? 8 : 15;
		}
		return 0;
	}
}
