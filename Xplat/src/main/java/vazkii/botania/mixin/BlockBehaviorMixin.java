package vazkii.botania.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import vazkii.botania.common.item.equipment.bauble.RingOfTheMantleItem;

@Mixin(BlockBehaviour.class)
public class BlockBehaviorMixin {
	@WrapOperation(
		method = "getDestroyProgress", at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/world/level/block/state/BlockState;getDestroySpeed(Lnet/minecraft/world/level/BlockGetter;Lnet/minecraft/core/BlockPos;)F"
		)
	)
	private float applyMantleRingBoost(BlockState instance, BlockGetter blockGetter,
			BlockPos pos, Operation<Float> original, @Local(argsOnly = true) Player player) {
		return RingOfTheMantleItem.getModifiedBlockDestroySpeed(instance, player, original.call(instance, blockGetter, pos));
	}
}
