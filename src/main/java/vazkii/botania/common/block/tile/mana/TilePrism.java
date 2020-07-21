/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block.tile.mana;

import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.state.property.Properties;
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
		ItemStack lens = getItemHandler().getStack(0);
		boolean active = !getCachedState().get(Properties.POWERED);
		boolean valid = !lens.isEmpty() && lens.getItem() instanceof ILens && (!(lens.getItem() instanceof ITinyPlanetExcempt) || ((ITinyPlanetExcempt) lens.getItem()).shouldPull(lens));

		if (active) {
			burst.setSourceLens(valid ? lens.copy() : ItemStack.EMPTY);
			burst.setColor(0xFFFFFF);
			burst.setGravity(0F);

			if (valid) {
				Entity burstEntity = (Entity) burst;
				BurstProperties properties = new BurstProperties(burst.getStartingMana(), burst.getMinManaLoss(), burst.getManaLossPerTick(), burst.getGravity(), 1F, burst.getColor());

				((ILens) lens.getItem()).apply(lens, properties);

				burst.setColor(properties.color);
				burst.setStartingMana(properties.maxMana);
				burst.setMinManaLoss(properties.ticksBeforeManaLoss);
				burst.setManaLossPerTick(properties.manaLossPerTick);
				burst.setGravity(properties.gravity);
				burst.setBurstMotion(burstEntity.getVelocity().getX() * properties.motionModifier,
						burstEntity.getVelocity().getY() * properties.motionModifier,
						burstEntity.getVelocity().getZ() * properties.motionModifier);
			}
		}
	}

	@Override
	protected SimpleInventory createItemHandler() {
		return new SimpleInventory(1) {
			@Override
			public boolean isValid(int index, ItemStack stack) {
				return !stack.isEmpty() && stack.getItem() instanceof ILens;
			}
		};
	}

	@Override
	public void markDirty() {
		super.markDirty();
		if (world != null && !world.isClient) {
			VanillaPacketDispatcher.dispatchTEToNearbyPlayers(this);
			BlockState state = getCachedState();
			boolean hasLens = !getItemHandler().getStack(0).isEmpty();
			if (state.getBlock() != ModBlocks.prism || state.get(BotaniaStateProps.HAS_LENS) != hasLens) {
				BlockState base = state.getBlock() == ModBlocks.prism ? state : ModBlocks.prism.getDefaultState();
				world.setBlockState(pos, base.with(BotaniaStateProps.HAS_LENS, hasLens));
			}
		}
	}

}
