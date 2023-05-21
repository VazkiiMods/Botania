package vazkii.botania.common.helper;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;

import vazkii.botania.api.item.BlockProvider;

public class BlockProviderHelper {
	public static BlockProvider asBlockProvider(final ItemStack stack) {
		return new ItemStackBlockProvider(stack);
	}

	public static BlockProvider asInfiniteBlockProvider(final ItemStack stack) {
		return new InfiniteItemStackBlockProvider(stack);
	}

	private record ItemStackBlockProvider(ItemStack stack) implements BlockProvider {

		@Override
		public boolean provideBlock(Player player, ItemStack requester, Block block, boolean doIt) {
			final boolean canDo = !stack.isEmpty() && stack.is(block.asItem());
			if (canDo && doIt && !player.getAbilities().instabuild) {
				stack.shrink(1);
			}
			return canDo;
		}

		@Override
		public int getBlockCount(Player player, ItemStack requester, Block block) {
			if (!stack.is(block.asItem())) {
				return 0;
			}
			return player.getAbilities().instabuild ? -1 : stack.getCount();
		}
	}

	private record InfiniteItemStackBlockProvider(ItemStack stack) implements BlockProvider {

		private InfiniteItemStackBlockProvider(ItemStack stack) {
			this.stack = stack.copy();
		}

		@Override
		public boolean provideBlock(Player player, ItemStack requester, Block block, boolean doIt) {
			return stack.is(block.asItem());
		}

		@Override
		public int getBlockCount(Player player, ItemStack requester, Block block) {
			return stack.is(block.asItem()) ? -1 : 0;
		}
	}
}
