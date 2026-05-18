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
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import org.jetbrains.annotations.Nullable;

import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.block.*;
import vazkii.botania.api.block.RedstoneSensitiveBlock;
import vazkii.botania.common.annotations.SoftImplement;
import vazkii.botania.common.block.block_entity.red_string.RedStringSpooferBlockEntity;
import vazkii.botania.common.block.flower.FloatingSpecialFlowerBlock;
import vazkii.botania.common.lib.BotaniaTags;

/**
 * Common superclass of all magical flower block entities
 */
public abstract class SpecialFlowerBlockEntity extends BlockEntity implements FloatingFlowerProvider {
	public static final int PODZOL_DELAY = 5;
	public static final int MYCELIUM_DELAY = 10;
	/** Block state seeds are 48 bit values, but appear to potentially fill the higher bits with ones */
	public static final long SEED_MASK = 0x0000_FFFF_FFFF_FFFFL;

	private final FloatingFlower floatingData = new FloatingFlowerImpl();

	@Nullable
	private BlockPos positionOverride;
	private boolean isFloating;
	private long cachedSeed;

	private static final String TAG_FLOATING_DATA = "floating";

	public SpecialFlowerBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
		this.cachedSeed = state.getSeed(pos) & SEED_MASK;
	}

	@SuppressWarnings("deprecation")
	@Override
	public void setBlockState(BlockState state) {
		super.setBlockState(state);
		this.cachedSeed = state.getSeed(getBlockPos()) & SEED_MASK;
	}

	public static void commonTick(Level level, BlockPos worldPosition, BlockState state, SpecialFlowerBlockEntity self) {
		if (self.isFloating != state.is(BotaniaTags.Blocks.FLOATING_FLOWERS)) {
			BotaniaAPI.LOGGER.error("Special flower changed floating state, this is not supported!", new Throwable());
			self.isFloating = !self.isFloating;
		}
		BlockEntity tileBelow = level.getBlockEntity(worldPosition.below());
		self.positionOverride = tileBelow instanceof RedStringSpooferBlockEntity relay ? relay.getBinding() : null;

		self.tickFlower();
	}

	public boolean isPowered() {
		BlockState state = getBlockState();
		if (!(state.getBlock() instanceof RedstoneSensitiveBlock powered)) {
			throw new IllegalStateException(state + " should support redstone power");
		}
		return powered.isPowered(state);
	}

	@Nullable
	@Override
	public FloatingFlower getFloatingData() {
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

	/**
	 * @return Where this flower's effects are centered at. This can differ from the true TE location due to
	 *         red string spoofers.
	 */
	public final BlockPos getEffectivePos() {
		return positionOverride != null ? positionOverride : getBlockPos();
	}

	protected void tickFlower() {}

	protected int getUpdateInterval() {
		return 1;
	}

	public boolean shouldUpdateThisTick() {
		return shouldTick(level.getGameTime(), getUpdateInterval());
	}

	protected boolean shouldTick(long gameTime, int interval) {
		if (interval <= 1) {
			return true;
		}
		return (int) ((gameTime + cachedSeed) % interval) == 0;
	}

	@Override
	protected void loadAdditional(CompoundTag cmp, HolderLookup.Provider registries) {
		if (getBlockState().getBlock() instanceof FloatingSpecialFlowerBlock) {
			setFloating(true);
		}

		IslandType oldType = floatingData.getIslandType();
		if (cmp.contains(TAG_FLOATING_DATA)) {
			floatingData.readNBT(cmp.getCompound(TAG_FLOATING_DATA), registries);
		}
		if (isFloating() && oldType != floatingData.getIslandType() && level != null && level.isClientSide) {
			level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 0);
		}
	}

	@Override
	protected void saveAdditional(CompoundTag cmp, HolderLookup.Provider registries) {
		if (isFloating()) {
			cmp.put(TAG_FLOATING_DATA, floatingData.writeNBT(registries));
		}
	}

	@Override
	public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
		var tag = super.getUpdateTag(registries);
		if (isFloating()) {
			tag.put(TAG_FLOATING_DATA, floatingData.writeNBT(registries));
		}
		return tag;
	}

	@Nullable
	@Override
	public Packet<ClientGamePacketListener> getUpdatePacket() {
		return isFloating() ? ClientboundBlockEntityDataPacket.create(this) : null;
	}

	public void sync() {
		if (level != null) {
			level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), Block.UPDATE_CLIENTS);
		}
	}

	/**
	 * Called when this sub tile is placed in the world (by an entity).
	 */
	public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {}

	/**
	 * Returns a descriptor for the radius of this sub tile. This is called while a player
	 * is looking at the block with a Manaseer Monocle.
	 */
	@Nullable
	public abstract RadiusDescriptor getRadius();

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
	 * Returns the additional delay in ticks that an item must be on the ground before this flower will act on it.
	 */
	public int getModulatedDelay() {
		if (isFloating()) {
			IslandType type = floatingData.getIslandType();
			return type.modulatedDelay();
		}

		// TODO: should be synced with island types somehow
		BlockState below = level.getBlockState(getBlockPos().below());
		if (below.is(Blocks.MYCELIUM)) {
			return MYCELIUM_DELAY;
		}

		if (below.is(Blocks.PODZOL)) {
			return PODZOL_DELAY;
		}

		return 0;
	}

	@SoftImplement("RenderDataBlockEntity")
	@Nullable
	public Object getRenderData() {
		if (isFloating()) {
			return floatingData.getIslandType();
		}
		return null;
	}

	public void emitParticle(ParticleOptions options, double xOffset, double yOffset, double zOffset, double xSpeed, double ySpeed, double zSpeed) {
		if (!level.isClientSide) {
			return;
		}
		Vec3 offset = level.getBlockState(getEffectivePos()).getOffset(level, getEffectivePos());
		level.addParticle(options,
				getEffectivePos().getX() + offset.x + xOffset,
				getEffectivePos().getY() + offset.y + yOffset,
				getEffectivePos().getZ() + offset.z + zOffset,
				xSpeed, ySpeed, zSpeed
		);
	}
}
