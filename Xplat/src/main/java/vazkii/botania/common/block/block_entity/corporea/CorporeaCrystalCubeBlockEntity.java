/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block.block_entity.corporea;

import com.mojang.blaze3d.platform.Window;
import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.gameevent.GameEvent;

import org.jetbrains.annotations.Nullable;

import vazkii.botania.api.block.PhantomInkableBlock;
import vazkii.botania.api.block.Wandable;
import vazkii.botania.api.corporea.CorporeaHelper;
import vazkii.botania.api.corporea.CorporeaRequestMatcher;
import vazkii.botania.api.corporea.CorporeaRequestor;
import vazkii.botania.api.corporea.CorporeaSpark;
import vazkii.botania.api.state.BotaniaStateProperties;
import vazkii.botania.client.core.helper.RenderHelper;
import vazkii.botania.common.block.block_entity.BotaniaBlockEntities;
import vazkii.botania.common.helper.NbtHelper;

import java.util.List;

public class CorporeaCrystalCubeBlockEntity extends BaseCorporeaBlockEntity implements CorporeaRequestor, Wandable, PhantomInkableBlock {
	private static final String TAG_REQUEST_TARGET = "requestTarget";
	private static final String TAG_ITEM_COUNT = "itemCount";

	private ItemStack requestTarget = ItemStack.EMPTY;
	private int itemCount = 0;
	private int ticks = 0;
	private int compValue = 0;

	public CorporeaCrystalCubeBlockEntity(BlockPos pos, BlockState state) {
		super(BotaniaBlockEntities.CORPOREA_CRYSTAL_CUBE, pos, state);
	}

	public static void serverTick(Level level, BlockPos pos, BlockState state, CorporeaCrystalCubeBlockEntity self) {
		++self.ticks;
		if (self.ticks % 20 == 0) {
			self.updateCount();
		}
	}

	public boolean isLocked() {
		return getBlockState().getValue(BlockStateProperties.LOCKED);
	}

	public boolean isHiddenCount() {
		return getBlockState().getValue(BotaniaStateProperties.HIDDEN);
	}

	public void setRequestTarget(ItemStack stack) {
		if (!stack.isEmpty() && !isLocked()) {
			requestTarget = stack.copyWithCount(1);
			setChanged();
			updateCount();
		} else {
			setChanged();
			level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), Block.UPDATE_CLIENTS);
		}
	}

	public ItemStack getRequestTarget() {
		return requestTarget;
	}

	public int getItemCount() {
		return itemCount;
	}

	public void doRequest(Player player) {
		if (level.isClientSide()) {
			return;
		}

		CorporeaSpark spark = getSpark();
		if (spark != null && spark.getMaster() != null && !requestTarget.isEmpty()) {
			int count = player.isShiftKeyDown() ? requestTarget.getMaxStackSize() : 1;
			var matcher = CorporeaHelper.instance().createMatcher(requestTarget, true);
			doCorporeaRequest(matcher, count, spark, player);
		}
	}

	private void updateCount() {
		if (level.isClientSide()) {
			return;
		}

		int sum = 0;
		CorporeaSpark spark = getSpark();
		if (spark != null && spark.getMaster() != null && !requestTarget.isEmpty()) {
			var matcher = CorporeaHelper.instance().createMatcher(requestTarget, true);
			List<ItemStack> stacks = CorporeaHelper.instance().requestItem(matcher, -1, spark, null, false).stacks();
			for (ItemStack stack : stacks) {
				sum += stack.getCount();
			}
		}

		setCount(sum);
	}

	private void setCount(int count) {
		int oldCount = this.itemCount;
		this.itemCount = count;
		if (this.itemCount != oldCount) {
			this.compValue = CorporeaHelper.instance().signalStrengthForRequestSize(itemCount);
			setChanged();
			level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), Block.UPDATE_CLIENTS);
		}
	}

	@Override
	protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
		if (!requestTarget.isEmpty()) {
			tag.put(TAG_REQUEST_TARGET, requestTarget.save(registries));
		}
		tag.putInt(TAG_ITEM_COUNT, itemCount);
	}

	@Override
	protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
		CompoundTag cmp = tag.getCompound(TAG_REQUEST_TARGET);
		requestTarget = ItemStack.parse(registries, cmp).orElse(ItemStack.EMPTY);
		setCount(tag.getInt(TAG_ITEM_COUNT));
	}

	@Override
	public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
		var tag = super.getUpdateTag(registries);
		if (!requestTarget.isEmpty()) {
			tag.put(TAG_REQUEST_TARGET, requestTarget.save(registries));
		}
		NbtHelper.putVarInt(tag, TAG_ITEM_COUNT, itemCount);
		return tag;
	}

	@Nullable
	@Override
	public Packet<ClientGamePacketListener> getUpdatePacket() {
		return ClientboundBlockEntityDataPacket.create(this);
	}

	public int getComparatorValue() {
		return compValue;
	}

	@Override
	public void doCorporeaRequest(CorporeaRequestMatcher request, int count, CorporeaSpark spark, @Nullable LivingEntity entity) {
		if (!requestTarget.isEmpty()) {
			List<ItemStack> stacks = CorporeaHelper.instance().requestItem(request, count, spark, entity, true).stacks();
			spark.onItemsRequested(stacks);
			boolean did = false;
			int sum = 0;
			for (ItemStack reqStack : stacks) {
				ItemEntity item = new ItemEntity(level, worldPosition.getX() + 0.5, worldPosition.getY() + 1.5, worldPosition.getZ() + 0.5, reqStack);
				level.addFreshEntity(item);
				if (ItemStack.isSameItem(requestTarget, reqStack)) {
					sum += reqStack.getCount();
					did = true;
				}
			}

			if (did) {
				setCount(getItemCount() - sum);
			}
		}
	}

	@Override
	public boolean onUsedByWand(@Nullable Player player, ItemStack stack, Direction side) {
		if (player == null || player.isShiftKeyDown()) {
			level.setBlock(getBlockPos(), getBlockState().cycle(BlockStateProperties.LOCKED), Block.UPDATE_CLIENTS);
			return true;
		}
		return false;
	}

	@Override
	public boolean onPhantomInked(@Nullable Player player, ItemStack stack, Direction side) {
		if (isHiddenCount()) {
			return false;
		}
		if (!level.isClientSide()) {
			if (player == null || !player.hasInfiniteMaterials()) {
				stack.shrink(1);
			}
			level.setBlock(getBlockPos(), getBlockState().setValue(BotaniaStateProperties.HIDDEN, true),
					Block.UPDATE_CLIENTS);
			level.gameEvent(null, GameEvent.BLOCK_CHANGE, getBlockPos());
		}
		return true;
	}

	public static class Hud {
		public static void render(CorporeaCrystalCubeBlockEntity cube, GuiGraphics gui, Window window, Font font, float partialTick) {
			PoseStack ps = gui.pose();

			ItemStack target = cube.getRequestTarget();
			if (!target.isEmpty()) {
				String nameStr = target.getHoverName().getString();
				String countStr = cube.getItemCount() + "x";
				String lockedStr = I18n.get("botaniamisc.locked");

				int strlen = Math.max(font.width(nameStr), font.width(countStr));
				boolean locked = cube.isLocked();
				if (locked) {
					strlen = Math.max(strlen, font.width(lockedStr));
				}

				int centerX = window.getGuiScaledWidth() / 2;
				int centerY = window.getGuiScaledHeight() / 2;
				ps.pushPose();
				ps.translate(centerX, centerY, 0);

				RenderHelper.renderHUDBox(gui, 8, -11, strlen + 32, locked ? 21 : 11);

				gui.drawString(font, nameStr, 30, -9, 0x6666FF);
				gui.drawString(font, countStr, 30, 1, 0xFFFFFF);
				if (locked) {
					gui.drawString(font, lockedStr, 30, 11, 0xFFAA00);
				}

				gui.renderItem(target, 10, -9);

				ps.popPose();
			}
		}
	}
}
