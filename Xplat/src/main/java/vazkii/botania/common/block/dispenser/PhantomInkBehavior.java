package vazkii.botania.common.block.dispenser;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.dispenser.BlockSource;
import net.minecraft.core.dispenser.OptionalDispenseItemBehavior;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DispenserBlock;

import org.jetbrains.annotations.NotNull;

import vazkii.botania.api.block.PhantomInkableBlock;

public class PhantomInkBehavior extends OptionalDispenseItemBehavior {
	@NotNull
	@Override
	protected ItemStack execute(BlockSource source, ItemStack stack) {
		Level world = source.level();
		Direction facing = source.state().getValue(DispenserBlock.FACING);
		BlockPos pos = source.pos().relative(facing);
		PhantomInkableBlock inkable = PhantomInkableBlock.LOOKUP.find(world, pos);
		setSuccess(inkable != null && inkable.onPhantomInked(null, stack, facing.getOpposite()));
		return stack;
	}
}
