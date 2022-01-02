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
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import vazkii.botania.api.block.*;
import vazkii.botania.api.internal.VanillaPacketDispatcher;
import vazkii.botania.common.Botania;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.block.decor.BlockFloatingFlower;
import vazkii.botania.common.block.tile.string.TileRedStringRelay;
import vazkii.botania.common.lib.ModTags;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Common superclass of all magical flower block entities
 */
public class TileEntitySpecialFlower extends BlockEntity implements IWandBindable, IFloatingFlowerProvider {
	public static final int PODZOL_DELAY = 5;
	public static final int MYCELIUM_DELAY = 10;

	private final IFloatingFlower floatingData = new FloatingFlowerImpl() {
		@Override
		public ItemStack getDisplayStack() {
			ResourceLocation id = Registry.BLOCK_ENTITY_TYPE.getKey(getType());
			return Registry.ITEM.getOptional(id).map(ItemStack::new).orElse(super.getDisplayStack());
		}
	};

	public int ticksExisted = 0;

	/** true if this flower is working on Enchanted Soil **/
	public boolean overgrowth = false;
	/** true if this flower is working on Enchanted Soil and this is the second tick **/
	public boolean overgrowthBoost = false;
	private BlockPos positionOverride;
	private boolean isFloating;

	public static final String TAG_TICKS_EXISTED = "ticksExisted";
	private static final String TAG_FLOATING_DATA = "floating";

	public TileEntitySpecialFlower(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
	}

	public static void commonTick(Level level, BlockPos worldPosition, BlockState state, TileEntitySpecialFlower self) {
		if (self.isFloating != state.is(ModTags.Blocks.FLOATING_FLOWERS)) {
			Botania.LOGGER.error("Special flower changed floating state, this is not supported!", new Throwable());
			self.isFloating = !self.isFloating;
		}
		BlockEntity tileBelow = level.getBlockEntity(worldPosition.below());
		if (tileBelow instanceof TileRedStringRelay) {
			BlockPos coords = ((TileRedStringRelay) tileBelow).getBinding();
			if (coords != null) {
				self.positionOverride = coords;
				self.tickFlower();

				return;
			} else {
				self.positionOverride = null;
			}
		} else {
			self.positionOverride = null;
		}

		boolean special = self.isOnSpecialSoil();
		if (special) {
			self.overgrowth = true;
			if (self.isOvergrowthAffected()) {
				self.tickFlower();
				self.overgrowthBoost = true;
			}
		}
		self.tickFlower();
		self.overgrowth = false;
		self.overgrowthBoost = false;
	}

	@Nullable
	@Override
	public IFloatingFlower getFloatingData() {
		if (hasLevel() && isFloating()) {
			return floatingData;
		}
		return null;
	}

	public final boolean isFloating() {
		return this.isFloating;
	}

	/**
	 * WARNING: This should only be called during or soon after construction.
	 * Switching between nonfloating/floating at play time is not supported and a fresh
	 * Block Entity should be created instead.
	 */
	public final void setFloating(boolean floating) {
		this.isFloating = floating;
	}

	private boolean isOnSpecialSoil() {
		if (isFloating()) {
			return false;
		} else {
			return level.getBlockState(worldPosition.below()).is(ModBlocks.enchantedSoil);
		}
	}

	/**
	 * @return Where this flower's effects are centered at. This can differ from the true TE location due to
	 *         red string spoofers.
	 */
	public final BlockPos getEffectivePos() {
		return positionOverride != null ? positionOverride : getBlockPos();
	}

	protected void tickFlower() {
		ticksExisted++;
	}

	@Override
	public final void load(CompoundTag cmp) {
		super.load(cmp);
		if (cmp.contains(TAG_TICKS_EXISTED)) {
			ticksExisted = cmp.getInt(TAG_TICKS_EXISTED);
		}
		if (getBlockState().getBlock() instanceof BlockFloatingFlower) {
			setFloating(true);
		}

		IFloatingFlower.IslandType oldType = floatingData.getIslandType();
		readFromPacketNBT(cmp);
		if (isFloating() && oldType != floatingData.getIslandType() && level != null) {
			level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 0);
		}
	}

	@Override
	public final void saveAdditional(CompoundTag cmp) {
		super.saveAdditional(cmp);
		cmp.putInt(TAG_TICKS_EXISTED, ticksExisted);
		writeToPacketNBT(cmp);
	}

	@Nonnull
	@Override
	public CompoundTag getUpdateTag() {
		var tag = new CompoundTag();
		writeToPacketNBT(tag);
		return tag;
	}

	/**
	 * Writes some extra data to a network packet. This data is read
	 * by readFromPacketNBT on the client that receives the packet.
	 * Note: This method is also used to write to the world NBT.
	 */
	public void writeToPacketNBT(CompoundTag cmp) {
		if (isFloating()) {
			cmp.put(TAG_FLOATING_DATA, floatingData.writeNBT());
		}
	}

	/**
	 * Reads data from a network packet. This data is written by
	 * writeToPacketNBT in the server. Note: This method is also used
	 * to read from the world NBT.
	 */
	public void readFromPacketNBT(CompoundTag cmp) {
		if (cmp.contains(TAG_FLOATING_DATA)) {
			floatingData.readNBT(cmp.getCompound(TAG_FLOATING_DATA));
		}
	}

	@Override
	public Packet<ClientGamePacketListener> getUpdatePacket() {
		return ClientboundBlockEntityDataPacket.create(this);
	}

	public void sync() {
		VanillaPacketDispatcher.dispatchTEToNearbyPlayers(this);
	}

	/**
	 * Called when this sub tile is placed in the world (by an entity).
	 */
	public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {}

	/**
	 * Gets the block coordinates this is bound to, for use with the wireframe render
	 * when the sub tile is being hovered with a wand of the forest.
	 */
	@Override
	public BlockPos getBinding() {
		return null;
	}

	/**
	 * Returns a descriptor for the radius of this sub tile. This is called while a player
	 * is looking at the block with a Manaseer Monocle.
	 */
	@Nullable
	public RadiusDescriptor getRadius() {
		return null;
	}

	/**
	 * Returns a descriptor for this flower's secondary radius.
	 * Use for e.g. when a flower has different ranges for picking up and using dropped items.
	 * Called when the player is looking at the block with a Manaseer Monocle.
	 */
	@Nullable
	public RadiusDescriptor getSecondaryRadius() {
		return null;
	}

	/**
	 * @see IWandBindable#canSelect(Player, ItemStack, net.minecraft.core.BlockPos, Direction)
	 */
	@Override
	public boolean canSelect(Player player, ItemStack wand, BlockPos pos, Direction side) {
		return false;
	}

	/**
	 * @see IWandBindable#bindTo(Player, ItemStack, net.minecraft.core.BlockPos, Direction)
	 */
	@Override
	public boolean bindTo(Player player, ItemStack wand, BlockPos pos, Direction side) {
		return false;
	}

	/**
	 * Gets if this SubTileEntity is affected by Enchanted Soil's speed boost.
	 */
	public boolean isOvergrowthAffected() {
		return true;
	}

	/**
	 * Returns the additional delay in ticks that an item must be on the ground before this flower will act on it.
	 */
	public int getModulatedDelay() {
		if (isFloating()) {
			IFloatingFlower.IslandType type = floatingData.getIslandType();
			if (type == IFloatingFlower.IslandType.MYCEL) {
				return MYCELIUM_DELAY;
			} else if (type == IFloatingFlower.IslandType.PODZOL) {
				return PODZOL_DELAY;
			}
		} else {
			BlockState below = level.getBlockState(getBlockPos().below());
			if (below.is(Blocks.MYCELIUM)) {
				return MYCELIUM_DELAY;
			}

			if (below.is(Blocks.PODZOL)) {
				return PODZOL_DELAY;
			}
		}

		return 0;
	}
}
