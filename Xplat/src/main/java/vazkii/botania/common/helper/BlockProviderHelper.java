/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */

package vazkii.botania.common.helper;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;

import org.jetbrains.annotations.Nullable;

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
			if (canDo && doIt && !player.hasInfiniteMaterials()) {
				stack.shrink(1);
			}
			return canDo;
		}

		@Override
		public int getBlockCount(Player player, ItemStack requester, Block block) {
			if (!stack.is(block.asItem())) {
				return 0;
			}
			return player.hasInfiniteMaterials() ? -1 : stack.getCount();
		}

		@Nullable
		@Override
		public Block getProvidedBlock(Player player, ItemStack requestor) {
			return stack.getItem() instanceof BlockItem blockItem ? blockItem.getBlock() : null;
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

		@Nullable
		@Override
		public Block getProvidedBlock(Player player, ItemStack requestor) {
			return stack.getItem() instanceof BlockItem blockItem ? blockItem.getBlock() : null;
		}
	}
}
