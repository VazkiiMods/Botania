/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.internal_caps;

import net.minecraft.nbt.CompoundTag;

import vazkii.botania.common.block.block_entity.AlfheimPortalBlockEntity;
import vazkii.botania.common.block.block_entity.PetalApothecaryBlockEntity;

// Component for misc internal Botania flags
public class ItemFlagsComponent extends SerializableComponent {
	public boolean alfPortalSpawned = false;
	public boolean apothecarySpawned = false;
	/**
	 * Similar to the age field on the actual entity, but always increases by 1 every tick,
	 * no magic values like vanilla -32768, etc.
	 * Initialized to zero by default, but may still be initialized to values less
	 * than zero in certain scenarios.
	 */
	public int timeCounter = 0;
	/**
	 * Set to {@link #INITIAL_MANA_INFUSION_COOLDOWN} when an item is output by the pool then cools off to 0.
	 * Used so certain mechanics don't interact with items immediately after they're produced.
	 */
	private int manaInfusionCooldown = 0;
	/**
	 * Similar to {@link #manaInfusionCooldown} but for the Runic Altar.
	 */
	private int runicAltarCooldown = 0;
	public static final int INITIAL_MANA_INFUSION_COOLDOWN = 25;
	public static final int INITIAL_RUNIC_ALTAR_COOLDOWN = 15;

	@Override
	public void readFromNbt(CompoundTag tag) {
		alfPortalSpawned = tag.getBoolean(AlfheimPortalBlockEntity.TAG_PORTAL_FLAG);
		apothecarySpawned = tag.getBoolean(PetalApothecaryBlockEntity.ITEM_TAG_APOTHECARY_SPAWNED);
		timeCounter = tag.getInt("timeCounter");
		manaInfusionCooldown = tag.getInt("manaInfusionCooldown");
		runicAltarCooldown = tag.getInt("runicAltarCooldown");
	}

	@Override
	public void writeToNbt(CompoundTag tag) {
		tag.putBoolean(AlfheimPortalBlockEntity.TAG_PORTAL_FLAG, alfPortalSpawned);
		tag.putBoolean(PetalApothecaryBlockEntity.ITEM_TAG_APOTHECARY_SPAWNED, apothecarySpawned);
		tag.putInt("timeCounter", timeCounter);
		tag.putInt("manaInfusionCooldown", manaInfusionCooldown);
		tag.putInt("runicAltarCooldown", runicAltarCooldown);
	}

	public void tick() {
		timeCounter++;
		if (manaInfusionCooldown > 0) {
			manaInfusionCooldown--;
		}
		if (runicAltarCooldown > 0) {
			runicAltarCooldown--;
		}
	}

	public int getManaInfusionCooldown() {
		return manaInfusionCooldown;
	}

	public void markNewlyInfused() {
		manaInfusionCooldown = INITIAL_MANA_INFUSION_COOLDOWN;
	}

	public int getRunicAltarCooldown() {
		return runicAltarCooldown;
	}

	public void markAltarOutput() {
		runicAltarCooldown = INITIAL_RUNIC_ALTAR_COOLDOWN;
	}
}
