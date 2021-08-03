/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block.tile;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.TickableBlockEntity;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

import vazkii.botania.api.item.IAvatarTile;
import vazkii.botania.api.item.IAvatarWieldable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class TileAvatar extends TileSimpleInventory implements IAvatarTile, TickableBlockEntity {
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
		enabled = !level.hasNeighborSignal(worldPosition);

		ItemStack stack = getItemHandler().getItem(0);
		if (!stack.isEmpty() && stack.getItem() instanceof IAvatarWieldable) {
			IAvatarWieldable wieldable = (IAvatarWieldable) stack.getItem();
			wieldable.onAvatarUpdate(this, stack);
		}

		if (enabled) {
			ticksElapsed++;
		}
	}

	@Override
	public void writePacketNBT(CompoundTag tag) {
		super.writePacketNBT(tag);
		tag.putBoolean(TAG_ENABLED, enabled);
		tag.putInt(TAG_TICKS_ELAPSED, ticksElapsed);
		tag.putInt(TAG_MANA, mana);
		ListTag boostCooldowns = new ListTag();
		for (Map.Entry<UUID, Integer> e : this.boostCooldowns.entrySet()) {
			CompoundTag cmp = new CompoundTag();
			cmp.putUUID("id", e.getKey());
			cmp.putInt("cooldown", e.getValue());
			boostCooldowns.add(cmp);
		}
		tag.put(TAG_COOLDOWNS, boostCooldowns);
	}

	@Override
	public void readPacketNBT(CompoundTag tag) {
		super.readPacketNBT(tag);
		enabled = tag.getBoolean(TAG_ENABLED);
		ticksElapsed = tag.getInt(TAG_TICKS_ELAPSED);
		mana = tag.getInt(TAG_MANA);
		boostCooldowns.clear();
		ListTag boostCooldowns = tag.getList(TAG_COOLDOWNS, 10);
		for (Tag nbt : boostCooldowns) {
			CompoundTag cmp = ((CompoundTag) nbt);
			UUID id = cmp.getUUID("id");
			int cooldown = cmp.getInt("cooldown");
			this.boostCooldowns.put(id, cooldown);
		}
	}

	@Override
	protected SimpleContainer createItemHandler() {
		return new SimpleContainer(1) {
			@Override
			public int getMaxStackSize() {
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
		return !getItemHandler().getItem(0).isEmpty();
	}

	@Override
	public int getCurrentMana() {
		return mana;
	}

	@Override
	public Container getInventory() {
		return getItemHandler();
	}

	@Override
	public Direction getAvatarFacing() {
		return getBlockState().getValue(BlockStateProperties.HORIZONTAL_FACING);
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
