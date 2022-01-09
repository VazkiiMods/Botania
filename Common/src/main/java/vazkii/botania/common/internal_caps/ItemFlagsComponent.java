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

import vazkii.botania.common.block.subtile.functional.SubTileSpectranthemum;
import vazkii.botania.common.block.tile.TileAlfPortal;
import vazkii.botania.common.block.tile.TileAltar;

// Component for misc internal Botania flags
public class ItemFlagsComponent {
	public boolean spectranthemumTeleported = false;
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
	public static final int INITIAL_MANA_INFUSION_COOLDOWN = 25;

	public void readFromNbt(CompoundTag tag) {
		spectranthemumTeleported = tag.getBoolean(SubTileSpectranthemum.TAG_TELEPORTED);
		alfPortalSpawned = tag.getBoolean(TileAlfPortal.TAG_PORTAL_FLAG);
		apothecarySpawned = tag.getBoolean(TileAltar.ITEM_TAG_APOTHECARY_SPAWNED);
		timeCounter = tag.getInt("timeCounter");
		manaInfusionCooldown = tag.getInt("manaInfusionCooldown");
	}

	public void writeToNbt(CompoundTag tag) {
		tag.putBoolean(SubTileSpectranthemum.TAG_TELEPORTED, spectranthemumTeleported);
		tag.putBoolean(TileAlfPortal.TAG_PORTAL_FLAG, alfPortalSpawned);
		tag.putBoolean(TileAltar.ITEM_TAG_APOTHECARY_SPAWNED, apothecarySpawned);
		tag.putInt("timeCounter", timeCounter);
		tag.putInt("manaInfusionCooldown", manaInfusionCooldown);
	}

	public void tick() {
		timeCounter++;
		if (manaInfusionCooldown > 0) {
			manaInfusionCooldown--;
		}
	}

	public int getManaInfusionCooldown() {
		return manaInfusionCooldown;
	}

	public void markNewlyInfused() {
		manaInfusionCooldown = INITIAL_MANA_INFUSION_COOLDOWN;
	}
}
