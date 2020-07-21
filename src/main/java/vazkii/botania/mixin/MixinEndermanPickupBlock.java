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
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import vazkii.botania.common.lib.ModTags;

import java.util.Random;

/**
 * This Mixin prevents Endermen from picking up special flowers, even though they are in the small_flowers tag
 * and thus the endermen_holdable tag.
 *
 * We want them in the small_flowers tag so things such as suspicious stew crafting and bee pollination will work,
 * but Endermen griefing Botania machinery setups is not good.
 */
@Mixin(targets = { "net.minecraft.entity.monster.EndermanEntity$TakeBlockGoal" })
public class MixinEndermanPickupBlock {
	@Inject(
		at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/world/World;getBlockState(Lnet/minecraft/util/math/BlockPos;)Lnet/minecraft/block/BlockState;"),
		method = "tick()V",
		locals = LocalCapture.CAPTURE_FAILSOFT,
		cancellable = true
	)
	private void dontTakeMagicFlowers(CallbackInfo ci, Random rand, World world, int i, int j, int k, BlockPos takePos, BlockState takeState) {
		if (takeState.isIn(ModTags.Blocks.SPECIAL_FLOWERS)) {
			ci.cancel();
		}
	}
}
