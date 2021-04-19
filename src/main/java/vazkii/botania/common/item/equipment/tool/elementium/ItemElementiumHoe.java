/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.item.equipment.tool.elementium;

import net.minecraft.block.BlockState;
import net.minecraft.block.FarmlandBlock;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.common.item.equipment.tool.manasteel.ItemManasteelHoe;

import javax.annotation.Nonnull;

public class ItemElementiumHoe extends ItemManasteelHoe {
	public ItemElementiumHoe(Properties props) {
		super(BotaniaAPI.instance().getElementiumItemTier(), props, -1f);
	}

	@Nonnull
	@Override
	public ActionResultType onItemUse(@Nonnull ItemUseContext context) {
		ActionResultType result = super.onItemUse(context);
		if (result.isSuccessOrConsume()) {
			World world = context.getWorld();
			BlockPos pos = context.getPos();
			BlockState state = world.getBlockState(pos);
			if (state.hasProperty(FarmlandBlock.MOISTURE)) {
				world.setBlockState(pos, state.with(FarmlandBlock.MOISTURE, 7));
			}
		}
		return result;
	}
}
