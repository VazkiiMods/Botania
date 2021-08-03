/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block.tile.mana;

import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

import vazkii.botania.api.internal.IManaBurst;
import vazkii.botania.api.internal.VanillaPacketDispatcher;
import vazkii.botania.api.mana.BurstProperties;
import vazkii.botania.api.mana.ILens;
import vazkii.botania.api.mana.ITinyPlanetExcempt;
import vazkii.botania.api.state.BotaniaStateProps;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.block.tile.ModTiles;
import vazkii.botania.common.block.tile.TileExposedSimpleInventory;

public class TilePrism extends TileExposedSimpleInventory {
	public TilePrism() {
		super(ModTiles.PRISM);
	}

	public void onBurstCollision(IManaBurst burst) {
		ItemStack lens = getItemHandler().getItem(0);
		boolean active = !getBlockState().getValue(BlockStateProperties.POWERED);
		boolean valid = !lens.isEmpty() && lens.getItem() instanceof ILens && (!(lens.getItem() instanceof ITinyPlanetExcempt) || ((ITinyPlanetExcempt) lens.getItem()).shouldPull(lens));

		if (active) {
			burst.setSourceLens(valid ? lens.copy() : ItemStack.EMPTY);
			burst.setColor(0xFFFFFF);
			burst.setGravity(0F);

			if (valid) {
				Entity burstEntity = burst.entity();
				BurstProperties properties = new BurstProperties(burst.getStartingMana(), burst.getMinManaLoss(), burst.getManaLossPerTick(), burst.getBurstGravity(), 1F, burst.getColor());

				((ILens) lens.getItem()).apply(lens, properties);

				burst.setColor(properties.color);
				burst.setStartingMana(properties.maxMana);
				burst.setMinManaLoss(properties.ticksBeforeManaLoss);
				burst.setManaLossPerTick(properties.manaLossPerTick);
				burst.setGravity(properties.gravity);
				burst.setBurstMotion(burstEntity.getDeltaMovement().x() * properties.motionModifier,
						burstEntity.getDeltaMovement().y() * properties.motionModifier,
						burstEntity.getDeltaMovement().z() * properties.motionModifier);
			}
		}
	}

	@Override
	protected SimpleContainer createItemHandler() {
		return new SimpleContainer(1) {
			@Override
			public boolean canPlaceItem(int index, ItemStack stack) {
				return !stack.isEmpty() && stack.getItem() instanceof ILens;
			}
		};
	}

	@Override
	public void setChanged() {
		super.setChanged();
		if (level != null && !level.isClientSide) {
			VanillaPacketDispatcher.dispatchTEToNearbyPlayers(this);
			BlockState state = getBlockState();
			boolean hasLens = !getItemHandler().getItem(0).isEmpty();
			if (state.getBlock() != ModBlocks.prism || state.getValue(BotaniaStateProps.HAS_LENS) != hasLens) {
				BlockState base = state.getBlock() == ModBlocks.prism ? state : ModBlocks.prism.defaultBlockState();
				level.setBlockAndUpdate(worldPosition, base.setValue(BotaniaStateProps.HAS_LENS, hasLens));
			}
		}
	}

}
