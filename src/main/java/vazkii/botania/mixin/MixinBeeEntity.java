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
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.Bee;
import net.minecraft.world.level.Level;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import vazkii.botania.common.lib.ModTags;

@Mixin(Bee.class)
public abstract class MixinBeeEntity extends Animal {
	protected MixinBeeEntity(EntityType<? extends Animal> type, Level worldIn) {
		super(type, worldIn);
	}

	/**
	 * Allows bees to treat special flowers as proper flowers for
	 * pollination despite being excluded from the flower tag.
	 * 
	 * @see MixinPollinateGoal
	 */
	@Inject(
		method = "isFlowerValid", cancellable = true,
		at = @At(value = "FIELD", target = "Lnet/minecraft/world/entity/animal/Bee;level:Lnet/minecraft/world/level/Level;", ordinal = 1)
	)
	private void isSpecialFlower(BlockPos pos, CallbackInfoReturnable<Boolean> cir) {
		if (level.isLoaded(pos) && level.getBlockState(pos).is(ModTags.Blocks.SPECIAL_FLOWERS)) {
			cir.setReturnValue(true);
		}
	}
}
