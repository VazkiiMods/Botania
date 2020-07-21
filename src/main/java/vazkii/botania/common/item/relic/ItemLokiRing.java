/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.item.relic;

import com.google.common.collect.ImmutableList;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import vazkii.botania.api.item.ISequentialBreaker;
import vazkii.botania.api.item.IWireframeCoordinateListProvider;
import vazkii.botania.api.mana.IManaUsingItem;
import vazkii.botania.api.mana.ManaItemHandler;
import vazkii.botania.common.advancements.LokiPlaceTrigger;
import vazkii.botania.common.core.handler.EquipmentHandler;
import vazkii.botania.common.core.helper.ItemNBTHelper;
import vazkii.botania.common.core.helper.PlayerHelper;
import vazkii.botania.common.item.ModItems;
import vazkii.botania.common.item.equipment.tool.ToolCommons;
import vazkii.botania.common.lib.LibMisc;

import javax.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

import static vazkii.botania.common.lib.ResourceLocationHelper.prefix;

public class ItemLokiRing extends ItemRelicBauble implements IWireframeCoordinateListProvider, IManaUsingItem {

	private static final String TAG_CURSOR_LIST = "cursorList";
	private static final String TAG_CURSOR_PREFIX = "cursor";
	private static final String TAG_CURSOR_COUNT = "cursorCount";
	private static final String TAG_X_OFFSET = "xOffset";
	private static final String TAG_Y_OFFSET = "yOffset";
	private static final String TAG_Z_OFFSET = "zOffset";
	private static final String TAG_X_ORIGIN = "xOrigin";
	private static final String TAG_Y_ORIGIN = "yOrigin";
	private static final String TAG_Z_ORIGIN = "zOrigin";

	public ItemLokiRing(Settings props) {
		super(props);
	}

	public static ActionResult onPlayerInteract(PlayerEntity player, World world, Hand hand, BlockHitResult lookPos) {
		ItemStack lokiRing = getLokiRing(player);
		if (lokiRing.isEmpty() || !player.isSneaking()) {
			return ActionResult.PASS;
		}

		ItemStack stack = player.getStackInHand(hand);
		List<BlockPos> cursors = getCursorList(lokiRing);

		if (lookPos.getType() != HitResult.Type.BLOCK) {
			return ActionResult.PASS;
		}

		BlockPos hit = lookPos.getBlockPos();
		if (stack.isEmpty()) {
			BlockPos originCoords = getBindingCenter(lokiRing);
			if (!world.isClient) {
				if (originCoords.getY() == -1) {
					// Initiate a new pending list of positions
					setBindingCenter(lokiRing, hit);
					setCursorList(lokiRing, null);
				} else {
					if (originCoords.equals(hit)) {
						// Finalize the pending list of positions
						exitBindingMode(lokiRing);
					} else {
						// Toggle offsets on or off from the pending list of positions
						BlockPos relPos = hit.subtract(originCoords);

						boolean removed = cursors.remove(relPos);
						if (!removed) {
							cursors.add(relPos);
						}
						setCursorList(lokiRing, cursors);
					}
				}
			}

			return ActionResult.SUCCESS;
		} else {
			int cost = Math.min(cursors.size(), (int) Math.pow(Math.E, cursors.size() * 0.25));
			ItemStack original = stack.copy();
			int successes = 0;
			for (BlockPos cursor : cursors) {
				BlockPos pos = hit.add(cursor);
				if (ManaItemHandler.instance().requestManaExact(lokiRing, player, cost, false)) {
					Vec3d lookHit = lookPos.getPos();
					Vec3d newHitVec = new Vec3d(pos.getX() + MathHelper.fractionalPart(lookHit.getX()), pos.getY() + MathHelper.fractionalPart(lookHit.getY()), pos.getZ() + MathHelper.fractionalPart(lookHit.getZ()));
					BlockHitResult newHit = new BlockHitResult(newHitVec, lookPos.getSide(), pos, false);
					ItemUsageContext ctx = new ItemUsageContext(player, hand, newHit);

					ActionResult result;
					if (player.isCreative()) {
						result = PlayerHelper.substituteUse(ctx, original.copy());
					} else {
						result = stack.useOnBlock(ctx);
					}

					if (result == ActionResult.SUCCESS) {
						ManaItemHandler.instance().requestManaExact(lokiRing, player, cost, true);
						successes++;
					}
				}
			}
			if (successes > 0 && player instanceof ServerPlayerEntity) {
				LokiPlaceTrigger.INSTANCE.trigger((ServerPlayerEntity) player, lokiRing, successes);
			}
			return successes > 0 ? ActionResult.SUCCESS : ActionResult.PASS;
		}
	}

	public static void breakOnAllCursors(PlayerEntity player, ItemStack stack, BlockPos pos, Direction side) {
		Item item = stack.getItem();
		ItemStack lokiRing = getLokiRing(player);
		if (lokiRing.isEmpty() || player.world.isClient || !(item instanceof ISequentialBreaker)) {
			return;
		}

		List<BlockPos> cursors = getCursorList(lokiRing);
		ISequentialBreaker breaker = (ISequentialBreaker) item;
		boolean dispose = breaker.disposeOfTrashBlocks(stack);

		for (BlockPos offset : cursors) {
			BlockPos coords = pos.add(offset);
			BlockState state = player.world.getBlockState(coords);
			breaker.breakOtherBlock(player, stack, coords, pos, side);
			ToolCommons.removeBlockWithDrops(player, stack, player.world, coords,
					s -> s.getBlock() == state.getBlock() && s.getMaterial() == state.getMaterial(), dispose);
		}
	}

	@Override
	public void onUnequipped(ItemStack stack, LivingEntity living) {
		setCursorList(stack, null);
	}

	// onUnequipped has itemstack identity issues and doesn't actually fully work, so do this here every tick.
	// This prevents a player from accidentally entering binding mode, then forgetting where the binding center
	// is and thus being unable to exit binding mode.
	@Override
	public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean held) {
		super.inventoryTick(stack, world, entity, slot, held);
		// Curios actually calls this method, but with a negative slot, so we can check if we're in the "real" inventory this way
		if (slot >= 0) {
			exitBindingMode(stack);
		}
	}

	@Override
	@Environment(EnvType.CLIENT)
	public List<BlockPos> getWireframesToDraw(PlayerEntity player, ItemStack stack) {
		if (getLokiRing(player) != stack) {
			return ImmutableList.of();
		}

		HitResult lookPos = MinecraftClient.getInstance().crosshairTarget;

		if (lookPos != null
				&& lookPos.getType() == HitResult.Type.BLOCK
				&& !player.world.isAir(((BlockHitResult) lookPos).getBlockPos())) {
			List<BlockPos> list = getCursorList(stack);
			BlockPos origin = getBindingCenter(stack);

			for (int i = 0; i < list.size(); i++) {
				if (origin.getY() != -1) {
					list.set(i, list.get(i).add(origin));
				} else {
					list.set(i, list.get(i).add(((BlockHitResult) lookPos).getBlockPos()));
				}
			}

			return list;
		}

		return ImmutableList.of();
	}

	@Override
	@Environment(EnvType.CLIENT)
	public BlockPos getSourceWireframe(PlayerEntity player, ItemStack stack) {
		MinecraftClient mc = MinecraftClient.getInstance();
		if (getLokiRing(player) == stack) {
			BlockPos currentBuildCenter = getBindingCenter(stack);
			if (currentBuildCenter.getY() != -1) {
				return currentBuildCenter;
			} else if (mc.crosshairTarget instanceof BlockHitResult
					&& mc.crosshairTarget.getType() == HitResult.Type.BLOCK
					&& !getCursorList(stack).isEmpty()) {
				return ((BlockHitResult) mc.crosshairTarget).getBlockPos();
			}
		}

		return null;
	}

	private static ItemStack getLokiRing(PlayerEntity player) {
		return EquipmentHandler.findOrEmpty(ModItems.lokiRing, player);
	}

	private static BlockPos getBindingCenter(ItemStack stack) {
		int x = ItemNBTHelper.getInt(stack, TAG_X_ORIGIN, 0);
		int y = ItemNBTHelper.getInt(stack, TAG_Y_ORIGIN, -1);
		int z = ItemNBTHelper.getInt(stack, TAG_Z_ORIGIN, 0);
		return new BlockPos(x, y, z);
	}

	private static void exitBindingMode(ItemStack stack) {
		setBindingCenter(stack, new BlockPos(0, -1, 0));
	}

	private static void setBindingCenter(ItemStack stack, BlockPos pos) {
		ItemNBTHelper.setInt(stack, TAG_X_ORIGIN, pos.getX());
		ItemNBTHelper.setInt(stack, TAG_Y_ORIGIN, pos.getY());
		ItemNBTHelper.setInt(stack, TAG_Z_ORIGIN, pos.getZ());
	}

	private static List<BlockPos> getCursorList(ItemStack stack) {
		CompoundTag cmp = ItemNBTHelper.getCompound(stack, TAG_CURSOR_LIST, false);
		List<BlockPos> cursors = new ArrayList<>();

		int count = cmp.getInt(TAG_CURSOR_COUNT);
		for (int i = 0; i < count; i++) {
			CompoundTag cursorCmp = cmp.getCompound(TAG_CURSOR_PREFIX + i);
			int x = cursorCmp.getInt(TAG_X_OFFSET);
			int y = cursorCmp.getInt(TAG_Y_OFFSET);
			int z = cursorCmp.getInt(TAG_Z_OFFSET);
			cursors.add(new BlockPos(x, y, z));
		}

		return cursors;
	}

	private static void setCursorList(ItemStack stack, @Nullable List<BlockPos> cursors) {
		CompoundTag cmp = new CompoundTag();
		if (cursors != null) {
			int i = 0;
			for (BlockPos cursor : cursors) {
				CompoundTag cursorCmp = cursorToCmp(cursor);
				cmp.put(TAG_CURSOR_PREFIX + i, cursorCmp);
				i++;
			}
			cmp.putInt(TAG_CURSOR_COUNT, i);
		}

		ItemNBTHelper.setCompound(stack, TAG_CURSOR_LIST, cmp);
	}

	private static CompoundTag cursorToCmp(BlockPos pos) {
		CompoundTag cmp = new CompoundTag();
		cmp.putInt(TAG_X_OFFSET, pos.getX());
		cmp.putInt(TAG_Y_OFFSET, pos.getY());
		cmp.putInt(TAG_Z_OFFSET, pos.getZ());
		return cmp;
	}

	@Override
	public boolean usesMana(ItemStack stack) {
		return true;
	}

	@Override
	public Identifier getAdvancement() {
		return prefix("challenge/loki_ring");
	}

}
