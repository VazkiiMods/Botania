/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.item.equipment.tool.elementium;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.FarmBlock;
import net.minecraft.world.level.block.state.BlockState;

import vazkii.botania.common.item.equipment.tool.manasteel.ManasteelHoeItem;

public class ElementiumHoeItem extends ManasteelHoeItem {
	public ElementiumHoeItem(Tier tier, Properties properties) {
		super(tier, properties);
	}

	@Override
	public InteractionResult useOn(UseOnContext context) {
		InteractionResult result = super.useOn(context);
		if (result.consumesAction()) {
			Level world = context.getLevel();
			BlockPos pos = context.getClickedPos();
			BlockState state = world.getBlockState(pos);
			if (state.hasProperty(FarmBlock.MOISTURE)) {
				world.setBlockAndUpdate(pos, state.setValue(FarmBlock.MOISTURE, 7));
			}
		}
		return result;
	}
}
