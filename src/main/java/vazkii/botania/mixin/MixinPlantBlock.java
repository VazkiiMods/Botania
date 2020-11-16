package vazkii.botania.mixin;

import net.minecraft.block.BlockState;
import net.minecraft.block.PlantBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import vazkii.botania.common.block.BlockAltGrass;
import vazkii.botania.common.block.BlockEnchantedSoil;

@Mixin(PlantBlock.class)
public class MixinPlantBlock {
	@Inject(at = @At("HEAD"), method = "canPlantOnTop", cancellable = true)
	private void canPlant(BlockState floor, BlockView world, BlockPos pos, CallbackInfoReturnable<Boolean> cir) {
		if (floor.getBlock() instanceof BlockEnchantedSoil || floor.getBlock() instanceof BlockAltGrass) {
			cir.setReturnValue(true);
		}
	}
}
