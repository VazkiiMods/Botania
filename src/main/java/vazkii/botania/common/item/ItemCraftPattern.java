/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Jan 28, 2015, 2:59:06 PM (GMT)]
 */
package vazkii.botania.common.item;

import net.minecraft.block.BlockState;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import vazkii.botania.api.state.BotaniaStateProps;
import vazkii.botania.api.state.enums.CratePattern;
import vazkii.botania.common.block.ModBlocks;

import javax.annotation.Nonnull;

public class ItemCraftPattern extends ItemMod {
	public final CratePattern pattern;

	public ItemCraftPattern(CratePattern pattern, Properties props) {
		super(props);
		this.pattern = pattern;
	}

	@Nonnull
	@Override
	public ActionResultType onItemUse(ItemUseContext ctx) {
		World world = ctx.getWorld();
		BlockPos pos = ctx.getPos();
		BlockState state = world.getBlockState(pos);

		if(state.getBlock() == ModBlocks.craftCrate) {
			if(pattern != state.get(BotaniaStateProps.CRATE_PATTERN)) {
				world.setBlockState(pos, state.with(BotaniaStateProps.CRATE_PATTERN, this.pattern));
				return ActionResultType.SUCCESS;
			}
		}

		return ActionResultType.PASS;
	}
}
