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

import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemUseContext;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import vazkii.botania.api.state.BotaniaStateProps;
import vazkii.botania.api.state.enums.CratePattern;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.block.tile.TileCraftCrate;
import vazkii.botania.common.lib.LibItemNames;

import javax.annotation.Nonnull;
import java.util.Locale;

public class ItemCraftPattern extends ItemMod {
	public final CratePattern pattern;

	public ItemCraftPattern(CratePattern pattern, Properties props) {
		super(props);
		this.pattern = pattern;
	}

	@Nonnull
	@Override
	public EnumActionResult onItemUse(ItemUseContext ctx) {
		World world = ctx.getWorld();
		BlockPos pos = ctx.getPos();
		IBlockState state = world.getBlockState(pos);

		if(state.getBlock() == ModBlocks.craftCrate) {
			if(pattern != state.get(BotaniaStateProps.CRATE_PATTERN)) {
				world.setBlockState(pos, state.with(BotaniaStateProps.CRATE_PATTERN, this.pattern));
				return EnumActionResult.SUCCESS;
			}
		}

		return EnumActionResult.PASS;
	}
}
