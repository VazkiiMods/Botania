package vazkii.botania.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.piston.PistonBaseBlock;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import vazkii.botania.common.block.flower.generating.EntropinnyumBlockEntity;

@Mixin(PistonBaseBlock.class)
public class PistonBaseBlockMixin {
	@Inject(
		at = @At("HEAD"),
		method = "moveBlocks(Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;Lnet/minecraft/core/Direction;Z)Z"
	)
	private void preMoveBlocks(Level level, BlockPos pos, Direction dir, boolean retract,
			CallbackInfoReturnable<Boolean> cir) {
		if (!level.isClientSide()) {
			EntropinnyumBlockEntity.startTrackingTntEntities();
		}
	}

	@Inject(
		at = @At(value = "RETURN"),
		method = "moveBlocks(Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;Lnet/minecraft/core/Direction;Z)Z"
	)
	private void postMoveBlocks(Level level, BlockPos pos, Direction dir, boolean retract,
			CallbackInfoReturnable<Boolean> cir) {
		if (!level.isClientSide()) {
			EntropinnyumBlockEntity.endTrackingTntEntitiesAndCheck();
		}
	}
}
