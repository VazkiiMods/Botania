/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.mixin;

import net.minecraft.world.entity.animal.Bee;
import net.minecraft.world.level.block.state.BlockState;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import vazkii.botania.common.lib.BotaniaTags;

import java.util.function.Predicate;

@Mixin(targets = "net.minecraft.world.entity.animal.Bee$BeePollinateGoal")
public class PollinateGoalMixin {
	@Shadow
	@Mutable
	@Final
	private Predicate<BlockState> VALID_POLLINATION_BLOCKS;

	/**
	 * Allows bees to detect special flowers when looking for a block to pollinate.
	 * 
	 * @see BeeMixin
	 */
	@Inject(method = "<init>", at = @At("TAIL"))
	private void extendPredicate(Bee outer, CallbackInfo ci) {
		VALID_POLLINATION_BLOCKS = VALID_POLLINATION_BLOCKS.or(b -> b.is(BotaniaTags.Blocks.SPECIAL_FLOWERS));
	}
}
