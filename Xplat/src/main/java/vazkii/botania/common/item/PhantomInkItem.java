package vazkii.botania.common.item;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import vazkii.botania.api.block.PhantomInkableBlock;
import vazkii.botania.xplat.XplatAbstractions;

public class PhantomInkItem extends Item {

	public PhantomInkItem(Properties properties) {
		super(properties);
	}

	@Override
	public InteractionResult useOn(UseOnContext ctx) {
		Player player = ctx.getPlayer();

		if (player == null) {
			return InteractionResult.PASS;
		}

		Level level = ctx.getLevel();
		BlockPos pos = ctx.getClickedPos();
		BlockState state = level.getBlockState(pos);
		BlockEntity be = level.getBlockEntity(pos);
		PhantomInkableBlock inkable = XplatAbstractions.INSTANCE.findPhantomInkable(level, pos, state, be);
		if (inkable != null) {
			ItemStack stack = ctx.getItemInHand();
			Direction side = ctx.getClickedFace();
			return inkable.onPhantomInked(player, stack, side) ? InteractionResult.SUCCESS : InteractionResult.PASS;
		}
		return InteractionResult.PASS;
	}
}
