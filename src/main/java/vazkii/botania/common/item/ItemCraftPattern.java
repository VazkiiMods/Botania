/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.item;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import vazkii.botania.api.state.BotaniaStateProps;
import vazkii.botania.api.state.enums.CratePattern;
import vazkii.botania.common.block.ModBlocks;

import javax.annotation.Nonnull;

public class ItemCraftPattern extends Item {
	public final CratePattern pattern;

	public ItemCraftPattern(CratePattern pattern, Properties props) {
		super(props);
		this.pattern = pattern;
	}

	@Nonnull
	@Override
	public InteractionResult useOn(UseOnContext ctx) {
		Level world = ctx.getLevel();
		BlockPos pos = ctx.getClickedPos();
		BlockState state = world.getBlockState(pos);

		if (state.getBlock() == ModBlocks.craftCrate) {
			if (pattern != state.getValue(BotaniaStateProps.CRATE_PATTERN)) {
				world.setBlockAndUpdate(pos, state.setValue(BotaniaStateProps.CRATE_PATTERN, this.pattern));
				return InteractionResult.SUCCESS;
			}
		}

		return InteractionResult.PASS;
	}
}
