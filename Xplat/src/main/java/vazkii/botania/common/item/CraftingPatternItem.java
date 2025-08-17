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

import org.jetbrains.annotations.NotNull;

import vazkii.botania.api.state.BotaniaStateProperties;
import vazkii.botania.api.state.enums.CraftyCratePattern;
import vazkii.botania.common.block.BotaniaBlocks;
import vazkii.botania.common.block.block_entity.BotaniaBlockEntities;
import vazkii.botania.common.block.block_entity.CraftyCrateBlockEntity;

public class CraftingPatternItem extends Item {
	public final CraftyCratePattern pattern;

	public CraftingPatternItem(CraftyCratePattern pattern, Properties props) {
		super(props);
		this.pattern = pattern;
	}

	@NotNull
	@Override
	public InteractionResult useOn(UseOnContext ctx) {
		Level world = ctx.getLevel();
		BlockPos pos = ctx.getClickedPos();
		BlockState state = world.getBlockState(pos);

		if (state.is(BotaniaBlocks.craftCrate)) {
			if (pattern != state.getValue(BotaniaStateProperties.CRATE_PATTERN)) {
				world.setBlockAndUpdate(pos, state.setValue(BotaniaStateProperties.CRATE_PATTERN, this.pattern));
				if (!world.isClientSide) {
					world.getBlockEntity(pos, BotaniaBlockEntities.CRAFT_CRATE)
							.ifPresent(CraftyCrateBlockEntity::ejectLocked);
				}
				return InteractionResult.sidedSuccess(world.isClientSide());
			}
		}

		return InteractionResult.PASS;
	}
}
