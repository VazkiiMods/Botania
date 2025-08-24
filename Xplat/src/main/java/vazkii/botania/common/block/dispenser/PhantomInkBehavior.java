package vazkii.botania.common.block.dispenser;

import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockSource;
import net.minecraft.core.Direction;
import net.minecraft.core.dispenser.OptionalDispenseItemBehavior;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.level.block.state.BlockState;

import org.jetbrains.annotations.NotNull;

import vazkii.botania.api.block.PhantomInkableBlock;
import vazkii.botania.xplat.XplatAbstractions;

public class PhantomInkBehavior extends OptionalDispenseItemBehavior {
	@NotNull
	@Override
	protected ItemStack execute(BlockSource source, ItemStack stack) {
		Level world = source.getLevel();
		Direction facing = source.getBlockState().getValue(DispenserBlock.FACING);
		BlockPos pos = source.getPos().relative(facing);
		BlockState state = world.getBlockState(pos);
		PhantomInkableBlock inkable = XplatAbstractions.INSTANCE.findPhantomInkable(world, pos, state, world.getBlockEntity(pos));
		setSuccess(inkable != null && inkable.onPhantomInked(null, stack, facing.getOpposite()));
		return stack;
	}
}
