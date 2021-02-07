/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.mixin;

import net.minecraft.block.BlockState;
import net.minecraft.entity.passive.BeeEntity;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import vazkii.botania.common.lib.ModTags;

import java.util.function.Predicate;

@Mixin(targets = "net.minecraft.entity.passive.BeeEntity$PollinateGoal")
public class MixinPollinateGoal {
	@Shadow
	@Mutable
	@Final
	private Predicate<BlockState> flowerPredicate;

	/**
	 * Allows bees to detect special flowers when looking for a block to pollinate.
	 * 
	 * @see MixinBeeEntity
	 */
	@SuppressWarnings("UnresolvedMixinReference") // MCDev warning
	@Inject(method = "<init>(Lnet/minecraft/entity/passive/BeeEntity;)V", at = @At("TAIL"))
	private void extendPredicate(BeeEntity outer, CallbackInfo ci) {
		flowerPredicate = flowerPredicate.or(b -> b.isIn(ModTags.Blocks.SPECIAL_FLOWERS));
	}
}
