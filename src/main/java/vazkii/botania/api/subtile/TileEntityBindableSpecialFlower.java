/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.api.subtile;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import vazkii.botania.api.block.IWandBindable;
import vazkii.botania.common.core.helper.MathHelper;

import javax.annotation.Nullable;

import java.util.Objects;

/**
 * Superclass of all flowers which are bound to something with the Wand of the Forest,
 * such as generating flowers to mana collectors, or mana-using functional flowers to pools.
 * Implements bindability logic common to both types of flower.
 */
public abstract class TileEntityBindableSpecialFlower<T> extends TileEntitySpecialFlower implements IWandBindable {
	protected @Nullable BlockPos bindingPos = null;
	private final Class<T> tileClass;

	private static final String TAG_BINDING = "binding";

	public TileEntityBindableSpecialFlower(BlockEntityType<?> type, BlockPos pos, BlockState state, Class<T> tileClass) {
		super(type, pos, state);
		this.tileClass = tileClass;
	}

	public abstract int getBindingRange();

	//TODO: Implementations of this method are pretty much the only thing still using IManaNetwork.
	// This function has room to be a little expensive, as it's only ever called once per flower.
	// It could be replaced with a naive loop to find nearby TileEntities.
	// Maybe PoIs if you wanna get fancy. After that, IManaNetwork can pretty much be removed.
	public abstract void bindToNearest();

	@Override
	protected void tickFlower() {
		super.tickFlower();

		//First time the flower has been placed. This is the best time to check it; /setblock and friends don't call
		//the typical setPlacedBy method that player-placements do.
		if (ticksExisted == 1 && !level.isClientSide) {
			//Situations to consider:
			// the flower has been placed in the void, and there is nothing for it to bind to;
			// the flower has been placed next to a bind target, and I want to automatically bind to it;
			// the flower already has a valid binding due to ctrl-pick placement, and I should keep it;
			// the flower already has a binding from ctrl-pick placement, but it's invalid (out of range etc) and I should delete it.
			if (bindingPos == null || !isValidBinding()) {
				setBindingPos(null); //in case bindToNearest doesn't find any targets, don't keep invalid bindings around
				bindToNearest();
			}
		}
	}

	public @Nullable BlockPos getBindingPos() {
		return bindingPos;
	}

	public void setBindingPos(@Nullable BlockPos bindingPos) {
		boolean changed = !Objects.equals(this.bindingPos, bindingPos);

		this.bindingPos = bindingPos;

		if (changed) {
			setChanged();
			sync();
		}
	}

	@SuppressWarnings("unchecked")
	public @Nullable T findBindCandidateAt(BlockPos pos) {
		if (level == null || pos == null) {
			return null;
		}

		BlockEntity be = level.getBlockEntity(pos);
		return be != null && tileClass.isAssignableFrom(be.getClass()) ? (T) be : null;
	}

	public @Nullable T findBoundTile() {
		return findBindCandidateAt(bindingPos);
	}

	public boolean wouldBeValidBinding(@Nullable BlockPos pos) {
		if (level == null || pos == null || !level.isLoaded(pos) || MathHelper.distSqr(getBlockPos(), pos) > (long) getBindingRange() * getBindingRange()) {
			return false;
		} else {
			return findBindCandidateAt(pos) != null;
		}
	}

	public boolean isValidBinding() {
		return wouldBeValidBinding(bindingPos);
	}

	@Override
	public BlockPos getBinding() {
		//Used for Wand of the Forest overlays; only return the binding if it's valid.
		return isValidBinding() ? bindingPos : null;
	}

	@Override
	public boolean canSelect(Player player, ItemStack wand, BlockPos pos, Direction side) {
		return true;
	}

	@Override
	public boolean bindTo(Player player, ItemStack wand, BlockPos pos, Direction side) {
		if (wouldBeValidBinding(pos)) {
			setBindingPos(pos);
			return true;
		}

		return false;
	}

	@Override
	public void writeToPacketNBT(CompoundTag cmp) {
		super.writeToPacketNBT(cmp);

		if (bindingPos != null) {
			cmp.put(TAG_BINDING, NbtUtils.writeBlockPos(bindingPos));
		}
	}

	@Override
	public void readFromPacketNBT(CompoundTag cmp) {
		super.readFromPacketNBT(cmp);

		if (cmp.contains(TAG_BINDING)) {
			bindingPos = NbtUtils.readBlockPos(cmp.getCompound(TAG_BINDING));
		} else {
			//In older versions of the mod (1.16, early 1.17), TileEntityGeneratingFlower and TileEntitySpecialFlower
			//implemented their own copies of the binding logic. Read data from the old locations.
			if (cmp.contains("collectorX")) {
				bindingPos = new BlockPos(cmp.getInt("collectorX"), cmp.getInt("collectorY"), cmp.getInt("collectorZ"));
			} else if (cmp.contains("poolX")) {
				bindingPos = new BlockPos(cmp.getInt("poolX"), cmp.getInt("poolY"), cmp.getInt("poolZ"));
			}
			//These versions of the mod also sometimes used a binding with a Y of -1 to signify an unbound flower.
			//Currently, `null` is always used for unbound flowers. Coerce these positions to `null`.
			if (bindingPos != null && bindingPos.getY() == -1) {
				bindingPos = null;
			}
		}
	}
}
