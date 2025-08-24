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
