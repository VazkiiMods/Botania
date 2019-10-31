/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Mar 29, 2015, 10:13:32 PM (GMT)]
 */
package vazkii.botania.common.item.relic;

import com.google.common.collect.ImmutableList;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.math.RayTraceResult;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import vazkii.botania.api.item.ISequentialBreaker;
import vazkii.botania.api.item.IWireframeCoordinateListProvider;
import vazkii.botania.api.mana.IManaUsingItem;
import vazkii.botania.api.mana.ManaItemHandler;
import vazkii.botania.common.core.handler.EquipmentHandler;
import vazkii.botania.common.core.helper.ItemNBTHelper;
import vazkii.botania.common.item.ModItems;
import vazkii.botania.common.item.equipment.tool.ToolCommons;
import vazkii.botania.common.lib.LibMisc;

import java.util.ArrayList;
import java.util.List;

@Mod.EventBusSubscriber(modid = LibMisc.MOD_ID)
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

	public ItemLokiRing(Properties props) {
		super(props);
	}

	@SubscribeEvent
	public static void onPlayerInteract(PlayerInteractEvent.RightClickBlock event) {
		PlayerEntity player = event.getEntityPlayer();
		ItemStack lokiRing = getLokiRing(player);
		if(lokiRing.isEmpty() || !player.isSneaking())
			return;

		ItemStack heldItemStack = event.getItemStack();
		BlockPos originCoords = getOriginPos(lokiRing);
		RayTraceResult lookPos = ToolCommons.raytraceFromEntity(player.world, player, RayTraceContext.FluidMode.ANY, 10F);
		List<BlockPos> cursors = getCursorList(lokiRing);
		int cost = Math.min(cursors.size(), (int) Math.pow(Math.E, cursors.size() * 0.25));

		if(lookPos.getType() != RayTraceResult.Type.BLOCK)
			return;

		BlockPos hit = ((BlockRayTraceResult) lookPos).getPos();
		if(heldItemStack.isEmpty()) {
			if(!event.getWorld().isRemote) {
				if(originCoords.getY() == -1) {
					setOriginPos(lokiRing, hit);
					setCursorList(lokiRing, null);
				} else {
					if(originCoords.equals(hit)) {
						setOriginPos(lokiRing, new BlockPos(0, -1, 0));
					} else {
						addCursor : {
							BlockPos relPos = hit.subtract(originCoords);

							for(BlockPos cursor : cursors)
								if(cursor.equals(relPos)) {
									cursors.remove(cursor);
									setCursorList(lokiRing, cursors);
									break addCursor;
								}

							addCursor(lokiRing, relPos);
						}
					}
				}
			}

			event.setCanceled(true);
			event.setCancellationResult(ActionResultType.SUCCESS);
		} else {
			for(BlockPos cursor : cursors) {
				BlockPos pos = hit.add(cursor);
				if(player.world.isAirBlock(pos) && ManaItemHandler.requestManaExact(lokiRing, player, cost, true)) {
					ItemStack saveHeld = heldItemStack.copy();
					BlockRayTraceResult newHit = new BlockRayTraceResult(
							lookPos.getHitVec().subtract(pos.getX(), pos.getY(), pos.getZ()),
							((BlockRayTraceResult) lookPos).getFace(), pos, false);
					heldItemStack.onItemUse(new ItemUseContext(player, event.getHand(), newHit));
					if(player.isCreative())
						player.setHeldItem(event.getHand(), saveHeld);
				}
			}
		}
	}

	public static void breakOnAllCursors(PlayerEntity player, Item item, ItemStack stack, BlockPos pos, Direction side) {
		ItemStack lokiRing = getLokiRing(player);
		if(lokiRing.isEmpty() || player.world.isRemote || !(item instanceof ISequentialBreaker))
			return;

		List<BlockPos> cursors = getCursorList(lokiRing);
		ISequentialBreaker breaker = (ISequentialBreaker) item;
		boolean dispose = breaker.disposeOfTrashBlocks(stack);

		for(BlockPos offset : cursors) {
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

	@Override
	@OnlyIn(Dist.CLIENT)
	public List<BlockPos> getWireframesToDraw(PlayerEntity player, ItemStack stack) {
		if(getLokiRing(player) != stack)
			return ImmutableList.of();

		RayTraceResult lookPos = Minecraft.getInstance().objectMouseOver;

		if(lookPos != null
				&& lookPos.getType() == RayTraceResult.Type.BLOCK
				&& !player.world.isAirBlock(((BlockRayTraceResult) lookPos).getPos())) {
			List<BlockPos> list = getCursorList(stack);
			BlockPos origin = getOriginPos(stack);

			for(int i = 0; i < list.size(); i++) {
				if(origin.getY() != -1) {
					list.set(i, list.get(i).add(origin));
				} else {
					list.set(i, list.get(i).add(((BlockRayTraceResult) lookPos).getPos()));
				}
			}

			return list;
		}

		return ImmutableList.of();
	}

	@Override
	public BlockPos getSourceWireframe(PlayerEntity player, ItemStack stack) {
		return getLokiRing(player) == stack ? getOriginPos(stack) : null;
	}

	private static ItemStack getLokiRing(PlayerEntity player) {
		return EquipmentHandler.findOrEmpty(ModItems.lokiRing, player);
	}


	private static BlockPos getOriginPos(ItemStack stack) {
		int x = ItemNBTHelper.getInt(stack, TAG_X_ORIGIN, 0);
		int y = ItemNBTHelper.getInt(stack, TAG_Y_ORIGIN, -1);
		int z = ItemNBTHelper.getInt(stack, TAG_Z_ORIGIN, 0);
		return new BlockPos(x, y, z);
	}

	private static void setOriginPos(ItemStack stack, BlockPos pos) {
		ItemNBTHelper.setInt(stack, TAG_X_ORIGIN, pos.getX());
		ItemNBTHelper.setInt(stack, TAG_Y_ORIGIN, pos.getY());
		ItemNBTHelper.setInt(stack, TAG_Z_ORIGIN, pos.getZ());
	}

	private static List<BlockPos> getCursorList(ItemStack stack) {
		CompoundNBT cmp = ItemNBTHelper.getCompound(stack, TAG_CURSOR_LIST, false);
		List<BlockPos> cursors = new ArrayList<>();

		int count = cmp.getInt(TAG_CURSOR_COUNT);
		for(int i = 0; i < count; i++) {
			CompoundNBT cursorCmp = cmp.getCompound(TAG_CURSOR_PREFIX + i);
			int x = cursorCmp.getInt(TAG_X_OFFSET);
			int y = cursorCmp.getInt(TAG_Y_OFFSET);
			int z = cursorCmp.getInt(TAG_Z_OFFSET);
			cursors.add(new BlockPos(x, y, z));
		}

		return cursors;
	}

	private static void setCursorList(ItemStack stack, List<BlockPos> cursors) {
		if(stack == null)
			return;

		CompoundNBT cmp = new CompoundNBT();
		if(cursors != null) {
			int i = 0;
			for(BlockPos cursor : cursors) {
				CompoundNBT cursorCmp = cursorToCmp(cursor);
				cmp.put(TAG_CURSOR_PREFIX + i, cursorCmp);
				i++;
			}
			cmp.putInt(TAG_CURSOR_COUNT, i);
		}

		ItemNBTHelper.setCompound(stack, TAG_CURSOR_LIST, cmp);
	}

	private static CompoundNBT cursorToCmp(BlockPos pos) {
		CompoundNBT cmp = new CompoundNBT();
		cmp.putInt(TAG_X_OFFSET, pos.getX());
		cmp.putInt(TAG_Y_OFFSET, pos.getY());
		cmp.putInt(TAG_Z_OFFSET, pos.getZ());
		return cmp;
	}

	private static void addCursor(ItemStack stack, BlockPos pos) {
		CompoundNBT cmp = ItemNBTHelper.getCompound(stack, TAG_CURSOR_LIST, false);
		int count = cmp.getInt(TAG_CURSOR_COUNT);
		cmp.put(TAG_CURSOR_PREFIX + count, cursorToCmp(pos));
		cmp.putInt(TAG_CURSOR_COUNT, count + 1);
		ItemNBTHelper.setCompound(stack, TAG_CURSOR_LIST, cmp);
	}

	@Override
	public boolean usesMana(ItemStack stack) {
		return true;
	}

	@Override
	public ResourceLocation getAdvancement() {
		return new ResourceLocation(LibMisc.MOD_ID, "challenge/loki_ring");
	}

}

