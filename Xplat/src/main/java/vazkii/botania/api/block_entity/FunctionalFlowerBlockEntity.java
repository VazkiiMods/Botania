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
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import vazkii.botania.api.mana.ManaPool;
import vazkii.botania.common.helper.NbtHelper;

import static vazkii.botania.api.BotaniaAPI.botaniaRL;

/**
 * The basic class for a Functional Flower.
 */
public abstract class FunctionalFlowerBlockEntity extends BindableSpecialFlowerBlockEntity<ManaPool> {
	private static final ResourceLocation POOL_ID = botaniaRL("mana_pool");

	public static final int LINK_RANGE = 10;
	private static final String TAG_MANA = "mana";

	private int lastMana = -1;
	private int mana;

	public FunctionalFlowerBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state, ManaPool.class);
	}

	@Override
	public void tickFlower() {
		super.tickFlower();

		if (getLevel().isClientSide) {
			doFillLevelSparkles();
		} else {
			drawManaFromPool();

			// if mana after drawing from pool changed compared to previous tick, we probably need to synchronize things
			if (lastMana != mana) {
				lastMana = mana;
				markForPersisting();
				markForSync();
			}
		}
	}

	@Override
	public int getBindingRadius() {
		return LINK_RANGE;
	}

	public void drawManaFromPool() {
		ManaPool pool = findBoundTile();
		if (pool != null) {
			int manaInPool = pool.getCurrentMana();
			int manaMissing = getMaxMana() - mana;
			int manaToRemove = Math.min(manaMissing, manaInPool);
			pool.receiveMana(-manaToRemove);
			addMana(manaToRemove);
		}
	}

	@Override
	public int getMana() {
		return mana;
	}

	@Override
	public void addMana(int mana) {
		this.mana = Mth.clamp(this.mana + mana, 0, getMaxMana());
	}

	@Override
	public ItemStack getDefaultHudIcon() {
		return BuiltInRegistries.ITEM.getOptional(POOL_ID).map(ItemStack::new).orElse(ItemStack.EMPTY);
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
