package vazkii.botania.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.piston.PistonBaseBlock;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(PistonBaseBlock.class)
public interface PistonBaseBlockAccessor {
	@Invoker("moveBlocks")
	boolean botania_moveBlocks(Level level, BlockPos pistonPos, Direction direction, boolean extending);
}
