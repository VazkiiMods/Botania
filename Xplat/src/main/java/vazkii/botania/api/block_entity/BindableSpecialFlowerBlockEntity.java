/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.api.block_entity;

import com.mojang.blaze3d.platform.Window;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.IntArrayTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.phys.Vec3;

import org.jetbrains.annotations.Nullable;

import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.BotaniaAPIClient;
import vazkii.botania.api.block.Bound;
import vazkii.botania.api.block.WandBindable;
import vazkii.botania.api.block.WandHUD;
import vazkii.botania.client.core.helper.RenderHelper;
import vazkii.botania.common.helper.MathHelper;
import vazkii.botania.common.item.BotaniaItems;
import vazkii.botania.xplat.XplatAbstractions;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Predicate;

/**
 * Superclass of flowers that can be bound to some kind of target with the Wand of the Forest,
 * such as generating flowers to mana collectors, or functional flowers to pools.
 * Implements the bindability logic common to both types of flower.
 *
 * @param <T> Type of block entity this special flower can bind to.
 */
public abstract class BindableSpecialFlowerBlockEntity<T> extends SpecialFlowerBlockEntity implements WandBindable, Bound {
	private static final String TAG_BINDING = "binding";
	private static final String TAG_AUTO_BINDING = "autoBinding";

	/**
	 * Superclass (or interface) of all BlockEntities that this flower is able to bind to.
	 */
	private final Class<T> bindClass;

	private boolean autoBinding = true;
	private @Nullable BlockPos bindingPos = null;

	public BindableSpecialFlowerBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state, Class<T> bindClass) {
		super(type, pos, state);
		this.bindClass = bindClass;
	}

	/**
	 * Instructs the flower to make an attempt to find a valid binding target next time it is ticked.
	 * Has no effect if the flower already has a valid binding.
	 */
	public void attemptAutoBinding() {
		autoBinding = !isValidBinding();
		// no explicit chunk updating here, this will be cleared next tick anyway
	}

	public abstract int getBindingRadius();

	/**
	 * Returns the BlockPos of the nearest target within the binding radius, or `null` if there aren't any.
	 */
	@Nullable
	public BlockPos findClosestTarget() {
		return getClosestManaReceiver(bindClass, getLevel(), getBlockPos(), getBindingRadius());
	}

	@Nullable
	protected BlockPos getClosestManaReceiver(Class<T> receiverType, Level level, BlockPos center, int rangeLimit) {
		return getClosestMatchingBlockEntity(
				level, center, rangeLimit, blockEntity -> receiverType.isInstance(
						XplatAbstractions.instance().findManaReceiver(blockEntity))
		);
	}

	// TODO: maybe find a better home for this very generic helper method
	@Nullable
	public static BlockPos getClosestMatchingBlockEntity(Level level, BlockPos center,
			int rangeLimit, Predicate<BlockEntity> blockEntityPredicate) {
		long minDist = Long.MAX_VALUE;
		long limitSquared = (long) rangeLimit * rangeLimit;
		BlockPos closestPos = null;

		// TODO: if we get around to doing data fixers, using POIs for this would be even more efficient
		List<ChunkPos> chunkPosList = ChunkPos.rangeClosed(
				new ChunkPos(center.offset(-rangeLimit, 0, -rangeLimit)),
				new ChunkPos(center.offset(rangeLimit, 0, rangeLimit))
		).toList();

		for (ChunkPos chunkPos : chunkPosList) {
			LevelChunk chunk = level.getChunkSource().getChunkNow(chunkPos.x, chunkPos.z);
			if (chunk != null) {
				for (Map.Entry<BlockPos, BlockEntity> entry : chunk.getBlockEntities().entrySet()) {
					BlockPos pos = entry.getKey();
					long dist = MathHelper.distSqr(center, pos);
					if (dist > minDist || dist > limitSquared) {
						continue;
					}
					if (blockEntityPredicate.test(entry.getValue())) {
						minDist = dist;
						closestPos = pos;
					}
				}
			}
		}

		return closestPos;
	}

	@Override
	protected void tickFlower() {
		super.tickFlower();

		//First time the flower has been placed. This is the best time to check it; /setblock and friends don't call
		//the typical setPlacedBy method that player-placements do.
		if (autoBinding && !level.isClientSide()) {
			autoBinding = false;
			setChanged();
			//Situations to consider:
			// the flower has been placed in the void, and there is nothing for it to bind to;
			// the flower has been placed next to a bind target, and I want to automatically bind to it;
			// the flower already has a valid binding due to ctrl-pick placement, and I should keep it;
			// the flower already has a binding from ctrl-pick placement, but it's invalid (out of range etc) and I should delete it.
			if (!isValidBinding()) {
				setBindingPos(findClosestTarget());
			}
		}
	}

	public void doFillLevelSparkles() {
		double particleChance = 1 - getMana() / (3.5 * getMaxMana());

		RandomSource rng = level.getRandom();
		if (rng.nextDouble() > particleChance) {
			BlockPos pos = getBlockPos();
			Vec3 offset = level.getBlockState(pos).getOffset(level, pos);
			double x = pos.getX() + offset.x;
			double y = pos.getY() + offset.y;
			double z = pos.getZ() + offset.z;

			int color = getColor();
			float red = (color >> 16 & 0xFF) / 255F;
			float green = (color >> 8 & 0xFF) / 255F;
			float blue = (color & 0xFF) / 255F;

			BotaniaAPI.instance().sparkleFX(level,
					x + 0.25 + rng.nextDouble() * 0.5,
					y + 0.5 + rng.nextDouble() * 0.5,
					z + 0.25 + rng.nextDouble() * 0.5,
					red, green, blue, rng.nextFloat(), 5);
		}
	}

	@Override
	public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
		if (placer != null && placer.isHolding(BotaniaItems.obedienceStick)) {
			autoBinding = false;
		}
		super.setPlacedBy(level, pos, state, placer, stack);
	}

	@Nullable
	public BlockPos getBindingPos() {
		return bindingPos;
	}

	public void setBindingPos(@Nullable BlockPos bindingPos) {
		boolean changed = !Objects.equals(this.bindingPos, bindingPos);

		this.bindingPos = bindingPos;

		if (changed) {
			setChanged();
			markForImmediateSync();
		}
	}

	public @Nullable T findBindCandidateAt(@Nullable BlockPos pos) {
		if (level == null || pos == null) {
			return null;
		}

		BlockEntity be = level.getBlockEntity(pos);
		return bindClass.isInstance(be) ? bindClass.cast(be) : null;
	}

	public @Nullable T findBoundTile() {
		return findBindCandidateAt(bindingPos);
	}

	public boolean wouldBeValidBinding(@Nullable BlockPos pos) {
		if (level == null || pos == null || !level.isLoaded(pos) || MathHelper.distSqr(getBlockPos(), pos) > (long) getBindingRadius() * getBindingRadius()) {
			return false;
		} else {
			return findBindCandidateAt(pos) != null;
		}
	}

	public boolean isValidBinding() {
		return wouldBeValidBinding(bindingPos);
	}

	@Nullable
	@Override
	public BlockPos getBinding() {
		//Used for Wand of the Forest overlays; only return the binding if it's valid.
		return isValidBinding() ? bindingPos : null;
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
	protected void saveAdditional(CompoundTag cmp, HolderLookup.Provider registries) {
		super.saveAdditional(cmp, registries);

		if (bindingPos != null) {
			cmp.put(TAG_BINDING, NbtUtils.writeBlockPos(bindingPos));
		}
		cmp.putBoolean(TAG_AUTO_BINDING, autoBinding);
	}

	@Override
	protected void loadAdditional(CompoundTag cmp, HolderLookup.Provider registries) {
		super.loadAdditional(cmp, registries);

		NbtUtils.readBlockPos(cmp, TAG_BINDING).ifPresentOrElse(this::setBindingPos, () -> setBindingPos(null));
		autoBinding = !cmp.contains(TAG_AUTO_BINDING) || cmp.getBoolean(TAG_AUTO_BINDING);
	}

	@Override
	public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
		var tag = super.getUpdateTag(registries);
		tag.put(TAG_BINDING, bindingPos != null ? NbtUtils.writeBlockPos(bindingPos) : new IntArrayTag(List.of()));
		return tag;
	}

	@Override
	public Packet<ClientGamePacketListener> getUpdatePacket() {
		return ClientboundBlockEntityDataPacket.create(this);
	}

	public abstract int getMana();

	public abstract void addMana(int mana);

	public abstract int getMaxMana();

	public abstract int getColor();

	public abstract ItemStack getDefaultHudIcon();

	public ItemStack getHudIcon() {
		T boundTile = findBoundTile();
		if (boundTile != null) {
			return new ItemStack(((BlockEntity) boundTile).getBlockState().getBlock().asItem());
		}
		return getDefaultHudIcon();
	}

	public static class BindableFlowerWandHud<F extends BindableSpecialFlowerBlockEntity<?>> implements WandHUD {
		protected final F flower;

		public BindableFlowerWandHud(F flower) {
			this.flower = flower;
		}

		public void renderHUD(GuiGraphics gui, Window window, Font font, int minLeft, int minRight, int minDown) {
			String name = I18n.get(flower.getBlockState().getBlock().getDescriptionId());
			int color = flower.getColor();

			int centerX = window.getGuiScaledWidth() / 2;
			int centerY = window.getGuiScaledHeight() / 2;
			int left = (Math.max(102, font.width(name)) + 4) / 2;
			// padding + item
			int right = left + 20;

			left = Math.max(left, minLeft);
			right = Math.max(right, minRight);

			RenderHelper.renderHUDBox(gui, centerX - left, centerY + 8, centerX + right, centerY + Math.max(30, minDown));

			BotaniaAPIClient.instance().drawComplexManaHUD(gui, window, font, color, flower.getMana(), flower.getMaxMana(),
					name, flower.getHudIcon(), flower.isValidBinding());
		}

		@Override
		public void renderHUD(GuiGraphics gui, Window window, Font font, float partialTick) {
			renderHUD(gui, window, font, 0, 0, 0);
		}
	}
}
