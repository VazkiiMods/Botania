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
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.Optional;

@Mixin(net.minecraft.world.level.block.GrowingPlantBodyBlock.class)
public interface GrowingPlantBodyBlockMixin {
	@Invoker("getHeadPos")
	Optional<BlockPos> botania_getHeadPos(BlockGetter level, BlockPos pos, Block block);
}
