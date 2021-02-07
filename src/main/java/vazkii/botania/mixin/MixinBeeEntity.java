/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.mixin;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.BeeEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import vazkii.botania.common.lib.ModTags;

@Mixin(BeeEntity.class)
public abstract class MixinBeeEntity extends AnimalEntity {
	protected MixinBeeEntity(EntityType<? extends AnimalEntity> type, World worldIn) {
		super(type, worldIn);
	}

	/**
	 * Allows bees to treat special flowers as proper flowers for
	 * pollination despite being excluded from the flower tag.
	 * 
	 * @see MixinPollinateGoal
	 */
	@Inject(
		method = "isFlowers", cancellable = true,
		at = @At(value = "FIELD", target = "Lnet/minecraft/entity/passive/BeeEntity;world:Lnet/minecraft/world/World;", ordinal = 1)
	)
	private void isSpecialFlower(BlockPos pos, CallbackInfoReturnable<Boolean> cir) {
		if (world.getBlockState(pos).isIn(ModTags.Blocks.SPECIAL_FLOWERS)) {
			cir.setReturnValue(true);
		}
	}
}
