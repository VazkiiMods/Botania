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
import net.minecraft.item.ItemUsageContext;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.common.item.equipment.tool.manasteel.ItemManasteelHoe;

import javax.annotation.Nonnull;

public class ItemElementiumHoe extends ItemManasteelHoe {
	public ItemElementiumHoe(Settings props) {
		super(BotaniaAPI.instance().getElementiumItemTier(), props, -1f);
	}

	@Nonnull
	@Override
	public ActionResult useOnBlock(@Nonnull ItemUsageContext context) {
		ActionResult result = super.useOnBlock(context);
		if (result.isAccepted()) {
			World world = context.getWorld();
			BlockPos pos = context.getBlockPos();
			BlockState state = world.getBlockState(pos);
			if (state.contains(FarmlandBlock.MOISTURE)) {
				world.setBlockState(pos, state.with(FarmlandBlock.MOISTURE, 7));
			}
		}
		return result;
	}
}
