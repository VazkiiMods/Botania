/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.item.rod;

import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

import vazkii.botania.api.item.BlockProvider;
import vazkii.botania.api.mana.ManaItemHandler;

public class DepthsRodItem extends Item {

	public static final int COST = 150;

	public DepthsRodItem(Properties props) {
		super(props);
	}

	@Override
	public InteractionResult useOn(UseOnContext ctx) {
		return LandsRodItem.place(ctx, Blocks.COBBLESTONE, COST, 0.3F, 0.3F, 0.3F);
	}

	public record BlockProviderImpl(ItemStack requestor, Player player) implements BlockProvider {
		@Override
		public boolean provideBlock(Block block, boolean doit) {
			return block == Blocks.COBBLESTONE
					&& ManaItemHandler.instance().requestManaExactForTool(requestor, player, COST, doit);
		}

		@Override
		public int getBlockCount(Block block) {
			return block == Blocks.COBBLESTONE
					? ManaItemHandler.instance().getInvocationCountForTool(requestor, player, COST)
					: 0;
		}

		@Override
		public Block getProvidedBlock() {
			return Blocks.COBBLESTONE;
		}
	}

}
