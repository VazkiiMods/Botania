/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.item;

import net.minecraft.block.BlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import vazkii.botania.api.state.BotaniaStateProps;
import vazkii.botania.api.state.enums.CratePattern;
import vazkii.botania.common.block.ModBlocks;

import javax.annotation.Nonnull;

public class ItemCraftPattern extends Item {
	public final CratePattern pattern;

	public ItemCraftPattern(CratePattern pattern, Settings props) {
		super(props);
		this.pattern = pattern;
	}

	@Nonnull
	@Override
	public ActionResult useOnBlock(ItemUsageContext ctx) {
		World world = ctx.getWorld();
		BlockPos pos = ctx.getBlockPos();
		BlockState state = world.getBlockState(pos);

		if (state.getBlock() == ModBlocks.craftCrate) {
			if (pattern != state.get(BotaniaStateProps.CRATE_PATTERN)) {
				world.setBlockState(pos, state.with(BotaniaStateProps.CRATE_PATTERN, this.pattern));
				return ActionResult.SUCCESS;
			}
		}

		return ActionResult.PASS;
	}
}
