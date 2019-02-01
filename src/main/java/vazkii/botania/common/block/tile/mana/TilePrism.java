/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Jan 17, 2015, 7:27:04 PM (GMT)]
 */
package vazkii.botania.common.block.tile.mana;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import vazkii.botania.api.internal.IManaBurst;
import vazkii.botania.api.internal.VanillaPacketDispatcher;
import vazkii.botania.api.mana.BurstProperties;
import vazkii.botania.api.mana.ILens;
import vazkii.botania.api.mana.ITinyPlanetExcempt;
import vazkii.botania.api.state.BotaniaStateProps;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.block.tile.TileSimpleInventory;

import javax.annotation.Nonnull;

public class TilePrism extends TileSimpleInventory {

	public void onBurstCollision(IManaBurst burst) {
		ItemStack lens = itemHandler.getStackInSlot(0);
		boolean active = !world.getBlockState(getPos()).getValue(BotaniaStateProps.POWERED);
		boolean valid = !lens.isEmpty() && lens.getItem() instanceof ILens && (!(lens.getItem() instanceof ITinyPlanetExcempt) || ((ITinyPlanetExcempt) lens.getItem()).shouldPull(lens));

		if(active) {
			burst.setSourceLens(valid ? lens.copy() : ItemStack.EMPTY);
			burst.setColor(0xFFFFFF);
			burst.setGravity(0F);

			if(valid) {
				Entity burstEntity = (Entity) burst;
				BurstProperties properties = new BurstProperties(burst.getStartingMana(), burst.getMinManaLoss(), burst.getManaLossPerTick(), burst.getGravity(), 1F, burst.getColor());

				((ILens) lens.getItem()).apply(lens, properties);

				burst.setColor(properties.color);
				burst.setStartingMana(properties.maxMana);
				burst.setMinManaLoss(properties.ticksBeforeManaLoss);
				burst.setManaLossPerTick(properties.manaLossPerTick);
				burst.setGravity(properties.gravity);
				burst.setMotion(burstEntity.motionX * properties.motionModifier, burstEntity.motionY * properties.motionModifier,burstEntity.motionZ * properties.motionModifier);
			}
		}
	}

	@Override
	public int getSizeInventory() {
		return 1;
	}

	@Override
	protected SimpleItemStackHandler createItemHandler() {
		return new SimpleItemStackHandler(this, true) {
			@Nonnull
			@Override
			public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
				if(!stack.isEmpty() && stack.getItem() instanceof ILens)
					return super.insertItem(slot, stack, simulate);
				else return stack;
			}
		};
	}

	@Override
	public void markDirty() {
		super.markDirty();
		VanillaPacketDispatcher.dispatchTEToNearbyPlayers(this);
		if(world != null && !world.isRemote) {
			IBlockState state = world.getBlockState(pos);
			boolean hasLens = !itemHandler.getStackInSlot(0).isEmpty();
			if (state.getBlock() != ModBlocks.prism || state.getValue(BotaniaStateProps.HAS_LENS) != hasLens) {
				IBlockState base = state.getBlock() == ModBlocks.prism ? state : ModBlocks.prism.getDefaultState();
				world.setBlockState(pos, base.withProperty(BotaniaStateProps.HAS_LENS, hasLens));
			}
		}
	}

}
