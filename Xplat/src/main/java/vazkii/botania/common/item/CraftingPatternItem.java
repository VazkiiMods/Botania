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
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.CrafterBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;

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
		} else if (world.getBlockEntity(pos) instanceof CrafterBlockEntity crafter) {
			if (!world.isClientSide) {
				float clickPitch;
				if (canApplyConfiguration(pattern, crafter)) {
					if (isConfigurationDifferent(pattern, crafter)) {
						for (int slot = 0; slot < CrafterBlockEntity.CONTAINER_SIZE; slot++) {
							crafter.setSlotState(slot, pattern.openSlots.get(slot));
						}
						clickPitch = 0.75f;
					} else {
						clickPitch = 1f;
					}
				} else {
					clickPitch = 1.2f;
				}
				// TODO: should probably define sound events for this
				world.playSound(null, pos, SoundEvents.UI_BUTTON_CLICK.value(), SoundSource.BLOCKS, 0.5f, clickPitch);
				world.gameEvent(ctx.getPlayer(), GameEvent.BLOCK_ACTIVATE, pos);
			}
			return InteractionResult.sidedSuccess(world.isClientSide());
		}
		return InteractionResult.PASS;
	}

	private boolean canApplyConfiguration(CraftyCratePattern pattern, CrafterBlockEntity crafter) {
		if (pattern.openSlots.size() != CrafterBlockEntity.CONTAINER_SIZE) {
			return false;
		}
		for (int slot = 0; slot < CrafterBlockEntity.CONTAINER_SIZE; slot++) {
			if (!pattern.openSlots.get(slot) && !crafter.getItem(slot).isEmpty()) {
				return false;
			}
		}
		return true;
	}

	private boolean isConfigurationDifferent(CraftyCratePattern pattern, CrafterBlockEntity crafter) {
		for (int slot = 0; slot < CrafterBlockEntity.CONTAINER_SIZE; slot++) {
			if (pattern.openSlots.get(slot) == crafter.isSlotDisabled(slot)) {
				return true;
			}
		}
		return false;
	}
}
