package vazkii.botania.common.block.dispenser;

import net.minecraft.core.BlockPos;
import net.minecraft.core.dispenser.BlockSource;
import net.minecraft.core.dispenser.OptionalDispenseItemBehavior;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.DispenserBlock;

import vazkii.botania.common.item.GrassSeedsItem;
import vazkii.botania.network.EffectType;
import vazkii.botania.network.clientbound.BotaniaEffectPacket;
import vazkii.botania.xplat.XplatAbstractions;

public class GrassSeedsBehavior extends OptionalDispenseItemBehavior {
	@Override
	public ItemStack execute(BlockSource source, ItemStack stack) {
		if (stack.getItem() instanceof GrassSeedsItem seedsItem) {
			ServerLevel world = source.level();
			BlockPos pos = source.pos().relative(source.state().getValue(DispenserBlock.FACING));

			setSuccess(seedsItem.applySeeds(world, pos, stack).consumesAction());

			if (isSuccess()) {
				XplatAbstractions.INSTANCE.sendToNear(world, pos,
						new BotaniaEffectPacket(EffectType.GRASS_SEED_PARTICLES,
								pos.getX(), pos.getY(), pos.getZ(),
								seedsItem.getColor()));
				return stack;
			}
		}
		return super.execute(source, stack);
	}
}
