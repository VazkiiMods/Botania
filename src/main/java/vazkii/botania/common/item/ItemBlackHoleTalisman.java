/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.item;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.item.Items;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.text.*;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;

import vazkii.botania.api.item.IBlockProvider;
import vazkii.botania.client.core.handler.ItemsRemainingRenderHandler;
import vazkii.botania.common.core.helper.ItemNBTHelper;
import vazkii.botania.common.core.helper.PlayerHelper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import java.util.List;

public class ItemBlackHoleTalisman extends Item implements IBlockProvider {
	public static final String TAG_ACTIVE = "active";
	private static final String TAG_BLOCK_NAME = "blockName";
	private static final String TAG_BLOCK_COUNT = "blockCount";

	public ItemBlackHoleTalisman(Properties props) {
		super(props);
	}

	@Nonnull
	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, @Nonnull Hand hand) {
		ItemStack stack = player.getHeldItem(hand);
		if (getBlock(stack) != null && player.isSneaking()) {
			ItemNBTHelper.setBoolean(stack, TAG_ACTIVE, !ItemNBTHelper.getBoolean(stack, TAG_ACTIVE, false));
			player.playSound(SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, 0.3F, 0.1F);
			return ActionResult.resultSuccess(stack);
		}

		return ActionResult.resultPass(stack);
	}

	@Nonnull
	@Override
	public ActionResultType onItemUse(ItemUseContext ctx) {
		World world = ctx.getWorld();
		BlockPos pos = ctx.getPos();
		Direction side = ctx.getFace();
		PlayerEntity player = ctx.getPlayer();
		BlockState state = world.getBlockState(pos);
		ItemStack stack = ctx.getItem();

		if (!state.isAir(world, pos) && setBlock(stack, state.getBlock())) {
			return ActionResultType.SUCCESS;
		} else {
			Block bBlock = getBlock(stack);

			if (bBlock == null) {
				return ActionResultType.PASS;
			}

			TileEntity tile = world.getTileEntity(pos);
			if (tile != null && tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, side).isPresent()) {
				if (!world.isRemote) {
					tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, side).ifPresent(inv -> {
						ItemStack toAdd = new ItemStack(bBlock);
						int maxSize = toAdd.getMaxStackSize();
						toAdd.setCount(remove(stack, maxSize));
						ItemStack remainder = ItemHandlerHelper.insertItemStacked(inv, toAdd, false);
						if (!remainder.isEmpty()) {
							add(stack, remainder.getCount());
						}
					});
				}
				return ActionResultType.SUCCESS;
			} else {
				if (player == null || player.abilities.isCreativeMode || getBlockCount(stack) > 0) {
					ItemStack toUse = new ItemStack(bBlock);
					ActionResultType result = PlayerHelper.substituteUse(ctx, toUse);

					if (result.isSuccessOrConsume()) {
						if (!world.isRemote) {
							remove(stack, 1);
							ItemsRemainingRenderHandler.send(player, toUse, getBlockCount(stack));
						}
						return result;
					}
				}
			}
		}

		return ActionResultType.PASS;
	}

	@Override
	public void inventoryTick(ItemStack itemstack, World world, Entity entity, int slot, boolean selected) {
		Block block = getBlock(itemstack);
		if (!entity.world.isRemote && ItemNBTHelper.getBoolean(itemstack, TAG_ACTIVE, false) && block != null && entity instanceof PlayerEntity) {
			PlayerEntity player = (PlayerEntity) entity;

			int highest = -1;
			int[] counts = new int[player.inventory.getSizeInventory() - player.inventory.armorInventory.size()];

			for (int i = 0; i < counts.length; i++) {
				ItemStack stack = player.inventory.getStackInSlot(i);
				if (stack.isEmpty()) {
					continue;
				}

				if (block.asItem() == stack.getItem()) {
					counts[i] = stack.getCount();
					if (highest == -1) {
						highest = i;
					} else {
						highest = counts[i] > counts[highest] && highest > 8 ? i : highest;
					}
				}
			}

			if (highest == -1) {
				/*ItemStack heldItem = player.inventory.getItemStack();
				if(hasFreeSlot && (heldItem == null || Item.getItemFromBlock(block) == heldItem.getItem() || heldItem.getItemDamage() != meta)) {
					ItemStack stack = new ItemStack(block, remove(itemstack, 64), meta);
					if(stack.stackSize != 0)
						player.inventory.addItemStackToInventory(stack);
				}*/
				// Used to keep one stack, disabled for now
			} else {
				for (int i = 0; i < counts.length; i++) {
					int count = counts[i];

					// highest is used to keep one stack, disabled for now
					if (/*i == highest || */count == 0) {
						continue;
					}

					add(itemstack, count);
					player.inventory.setInventorySlotContents(i, ItemStack.EMPTY);
				}

				/*int countInHighest = counts[highest];
				int maxSize = new ItemStack(block, 1, meta).getMaxStackSize();
				if(countInHighest < maxSize) {
					int missing = maxSize - countInHighest;
					ItemStack stackInHighest = player.inventory.getStackInSlot(highest);
					stackInHighest.stackSize += remove(itemstack, missing);
				}*/
				// Used to keep one stack, disabled for now
			}
		}
	}

	@Nonnull
	@Override
	public ITextComponent getDisplayName(@Nonnull ItemStack stack) {
		Block block = getBlock(stack);
		ItemStack bstack = new ItemStack(block);
		IFormattableTextComponent cand = super.getDisplayName(stack).deepCopy();

		if (!bstack.isEmpty()) {
			cand.appendString(" (");
			cand.append(bstack.getDisplayName().deepCopy().mergeStyle(TextFormatting.GREEN));
			cand.appendString(")");
		}

		return cand;
	}

	@Nonnull
	@Override
	public ItemStack getContainerItem(@Nonnull ItemStack itemStack) {
		int count = getBlockCount(itemStack);
		if (count == 0) {
			return ItemStack.EMPTY;
		}

		int extract = Math.min(64, count);
		ItemStack copy = itemStack.copy();
		remove(copy, extract);
		ItemNBTHelper.setBoolean(copy, TAG_ACTIVE, false);

		return copy;
	}

	@Override
	public boolean hasContainerItem(ItemStack stack) {
		return !getContainerItem(stack).isEmpty();
	}

	private boolean setBlock(ItemStack stack, Block block) {
		if (block.asItem() != Items.AIR && (getBlock(stack) == null || getBlockCount(stack) == 0)) {
			ItemNBTHelper.setString(stack, TAG_BLOCK_NAME, Registry.BLOCK.getKey(block).toString());
			return true;
		}
		return false;
	}

	private void add(ItemStack stack, int count) {
		int current = getBlockCount(stack);
		setCount(stack, current + count);
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void addInformation(ItemStack stack, World world, List<ITextComponent> stacks, ITooltipFlag flags) {
		Block block = getBlock(stack);
		if (block != null) {
			int count = getBlockCount(stack);
			stacks.add(new StringTextComponent(count + " ").append(new ItemStack(block).getDisplayName()).mergeStyle(TextFormatting.GRAY));
		}

		if (ItemNBTHelper.getBoolean(stack, TAG_ACTIVE, false)) {
			stacks.add(new TranslationTextComponent("botaniamisc.active"));
		} else {
			stacks.add(new TranslationTextComponent("botaniamisc.inactive"));
		}
	}

	private static void setCount(ItemStack stack, int count) {
		ItemNBTHelper.setInt(stack, TAG_BLOCK_COUNT, count);
	}

	public static int remove(ItemStack stack, int count) {
		int current = getBlockCount(stack);
		setCount(stack, Math.max(current - count, 0));

		return Math.min(current, count);
	}

	private static String getBlockName(ItemStack stack) {
		return ItemNBTHelper.getString(stack, TAG_BLOCK_NAME, "");
	}

	@Nullable
	public static Block getBlock(ItemStack stack) {
		ResourceLocation id = ResourceLocation.tryCreate(getBlockName(stack));
		if (id != null) {
			return Registry.BLOCK.getValue(id).orElse(null);
		}
		return null;
	}

	public static int getBlockCount(ItemStack stack) {
		return ItemNBTHelper.getInt(stack, TAG_BLOCK_COUNT, 0);
	}

	@Override
	public boolean provideBlock(PlayerEntity player, ItemStack requestor, ItemStack stack, Block block, boolean doit) {
		Block stored = getBlock(stack);
		if (stored == block) {
			int count = getBlockCount(stack);
			if (count > 0) {
				if (doit) {
					setCount(stack, count - 1);
				}
				return true;
			}
		}

		return false;
	}

	@Override
	public int getBlockCount(PlayerEntity player, ItemStack requestor, ItemStack stack, Block block) {
		Block stored = getBlock(stack);
		if (stored == block) {
			return getBlockCount(stack);
		}
		return 0;
	}

}
