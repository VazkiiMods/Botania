/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block.tile;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.util.Direction;

import vazkii.botania.api.item.IAvatarTile;
import vazkii.botania.api.item.IAvatarWieldable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class TileAvatar extends TileSimpleInventory implements IAvatarTile, ITickableTileEntity {
	private static final int MAX_MANA = 6400;

	private static final String TAG_ENABLED = "enabled";
	private static final String TAG_TICKS_ELAPSED = "ticksElapsed";
	private static final String TAG_MANA = "mana";
	private static final String TAG_COOLDOWNS = "boostCooldowns";

	private boolean enabled;
	private int ticksElapsed;
	private int mana;
	private final Map<UUID, Integer> boostCooldowns = new HashMap<>();

	public TileAvatar() {
		super(ModTiles.AVATAR);
	}

	@Override
	public void tick() {
		enabled = !world.isBlockPowered(pos);

		ItemStack stack = getItemHandler().getStackInSlot(0);
		if (!stack.isEmpty() && stack.getItem() instanceof IAvatarWieldable) {
			IAvatarWieldable wieldable = (IAvatarWieldable) stack.getItem();
			wieldable.onAvatarUpdate(this, stack);
		}

		if (enabled) {
			ticksElapsed++;
		}
	}

	@Override
	public void writePacketNBT(CompoundNBT tag) {
		super.writePacketNBT(tag);
		tag.putBoolean(TAG_ENABLED, enabled);
		tag.putInt(TAG_TICKS_ELAPSED, ticksElapsed);
		tag.putInt(TAG_MANA, mana);
		ListNBT boostCooldowns = new ListNBT();
		for (Map.Entry<UUID, Integer> e : this.boostCooldowns.entrySet()) {
			CompoundNBT cmp = new CompoundNBT();
			cmp.putUniqueId("id", e.getKey());
			cmp.putInt("cooldown", e.getValue());
			boostCooldowns.add(cmp);
		}
		tag.put(TAG_COOLDOWNS, boostCooldowns);
	}

	@Override
	public void readPacketNBT(CompoundNBT tag) {
		super.readPacketNBT(tag);
		enabled = tag.getBoolean(TAG_ENABLED);
		ticksElapsed = tag.getInt(TAG_TICKS_ELAPSED);
		mana = tag.getInt(TAG_MANA);
		boostCooldowns.clear();
		ListNBT boostCooldowns = tag.getList(TAG_COOLDOWNS, 10);
		for (INBT nbt : boostCooldowns) {
			CompoundNBT cmp = ((CompoundNBT) nbt);
			UUID id = cmp.getUniqueId("id");
			int cooldown = cmp.getInt("cooldown");
			this.boostCooldowns.put(id, cooldown);
		}
	}

	@Override
	protected Inventory createItemHandler() {
		return new Inventory(1) {
			@Override
			public int getInventoryStackLimit() {
				return 1;
			}
		};
	}

	@Override
	public boolean isFull() {
		return mana >= MAX_MANA;
	}

	@Override
	public void receiveMana(int mana) {
		this.mana = Math.min(3 * MAX_MANA, this.mana + mana);
	}

	@Override
	public boolean canReceiveManaFromBursts() {
		return !getItemHandler().getStackInSlot(0).isEmpty();
	}

	@Override
	public int getCurrentMana() {
		return mana;
	}

	@Override
	public IInventory getInventory() {
		return getItemHandler();
	}

	@Override
	public Direction getAvatarFacing() {
		return getBlockState().get(BlockStateProperties.HORIZONTAL_FACING);
	}

	@Override
	public int getElapsedFunctionalTicks() {
		return ticksElapsed;
	}

	@Override
	public boolean isEnabled() {
		return enabled;
	}

	@Override
	public Map<UUID, Integer> getBoostCooldowns() {
		return boostCooldowns;
	}
}
