/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.api.block_entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import org.jetbrains.annotations.Nullable;

import vazkii.botania.api.mana.ManaCollector;
import vazkii.botania.common.helper.NbtHelper;

import static vazkii.botania.api.BotaniaAPI.botaniaRL;

/**
 * The basic class for a Generating Flower.
 */
public abstract class GeneratingFlowerBlockEntity extends BindableSpecialFlowerBlockEntity<ManaCollector> {
	private static final ResourceLocation SPREADER_ID = botaniaRL("mana_spreader");

	public static final int LINK_RANGE = 6;
	private static final String TAG_MANA = "mana";

	private int lastMana = -1;
	private int mana;
	private boolean alreadyTicked = false;

	public GeneratingFlowerBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state, ManaCollector.class);
	}

	@Override
	public void tickFlower() {
		super.tickFlower();

		if (getLevel().isClientSide()) {
			doFillLevelSparkles();
		} else {
			emptyManaIntoCollector();

			// if mana after pushing to spreader changed compared to previous tick, we should probably synchronize things
			if (lastMana != mana) {
				lastMana = mana;
				markForPersisting();
				markForPotentialSync();
			}
		}
		alreadyTicked = true;
	}

	@Override
	public void setBindingPos(@Nullable BlockPos bindingPos) {
		super.setBindingPos(bindingPos);
		if (alreadyTicked && getMana() > 0) {
			emptyManaIntoCollector();
		}
	}

	@Override
	public int getBindingRadius() {
		return LINK_RANGE;
	}

	public void emptyManaIntoCollector() {
		ManaCollector collector = findBoundTile();
		if (collector != null && !collector.isFull() && getMana() > 0) {
			int manaval = Math.min(getMana(), collector.getMaxMana() - collector.getCurrentMana());
			addMana(-manaval);
			collector.receiveMana(manaval);
		}
	}

	@Override
	public int getMana() {
		return mana;
	}

	@Override
	public void addMana(int mana) {
		this.mana = Math.min(getMaxMana(), this.getMana() + mana);
	}

	@Override
	public ItemStack getDefaultHudIcon() {
		return BuiltInRegistries.ITEM.getOptional(SPREADER_ID).map(ItemStack::new).orElse(ItemStack.EMPTY);
	}

	@Override
	protected void loadAdditional(CompoundTag cmp, HolderLookup.Provider registries) {
		super.loadAdditional(cmp, registries);
		lastMana = mana = cmp.getInt(TAG_MANA);
	}

	@Override
	protected void saveAdditional(CompoundTag cmp, HolderLookup.Provider registries) {
		super.saveAdditional(cmp, registries);
		cmp.putInt(TAG_MANA, mana);
	}

	@Override
	public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
		var tag = super.getUpdateTag(registries);
		NbtHelper.putVarInt(tag, TAG_MANA, mana);
		return tag;
	}
}
