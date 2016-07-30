/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Mar 31, 2015, 11:04:12 PM (GMT)]
 */
package vazkii.botania.common.item;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.oredict.RecipeSorter;
import net.minecraftforge.oredict.RecipeSorter.Category;
import vazkii.botania.api.item.IBlockProvider;
import vazkii.botania.client.core.handler.ItemsRemainingRenderHandler;
import vazkii.botania.client.core.handler.ModelHandler;
import vazkii.botania.common.core.helper.InventoryHelper;
import vazkii.botania.common.core.helper.ItemNBTHelper;
import vazkii.botania.common.crafting.recipe.BlackHoleTalismanExtractRecipe;
import vazkii.botania.common.lib.LibItemNames;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.List;

public class ItemBlackHoleTalisman extends ItemMod implements IBlockProvider {

	private static final String TAG_BLOCK_NAME = "blockName";
	private static final String TAG_BLOCK_META = "blockMeta";
	private static final String TAG_BLOCK_COUNT = "blockCount";

	public ItemBlackHoleTalisman() {
		super(LibItemNames.BLACK_HOLE_TALISMAN);
		setMaxStackSize(1);
		setHasSubtypes(true);

		GameRegistry.addRecipe(new BlackHoleTalismanExtractRecipe());
		RecipeSorter.register("botania:blackHoleTalismanExtract", BlackHoleTalismanExtractRecipe.class, Category.SHAPELESS, "");
	}

	@Nonnull
	@Override
	public ActionResult<ItemStack> onItemRightClick(@Nonnull ItemStack par1ItemStack, World world, EntityPlayer player, EnumHand hand) {
		if(getBlock(par1ItemStack) != Blocks.AIR && player.isSneaking()) {
			int dmg = par1ItemStack.getItemDamage();
			par1ItemStack.setItemDamage(~dmg & 1);
			player.playSound(SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, 0.3F, 0.1F);
			return ActionResult.newResult(EnumActionResult.SUCCESS, par1ItemStack);
		}

		return ActionResult.newResult(EnumActionResult.PASS, par1ItemStack);
	}

	@Nonnull
	@Override
	public EnumActionResult onItemUse(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
		IBlockState state = world.getBlockState(pos);
		boolean set = setBlock(stack, state.getBlock(), state.getBlock().getMetaFromState(state));

		if(!set) {
			Block bBlock = getBlock(stack);
			int bmeta = getBlockMeta(stack);

			TileEntity tile = world.getTileEntity(pos);
			if(tile != null && tile.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, side)) {
				IItemHandler inv = tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, side);
				ItemStack toAdd = new ItemStack(bBlock, 1, bmeta);
				int maxSize = toAdd.getMaxStackSize();
				toAdd.stackSize = remove(stack, maxSize);
				ItemStack remainder = ItemHandlerHelper.insertItemStacked(inv, toAdd, false);
				if(remainder != null)
					add(stack, remainder.stackSize);
			} else {
				int entities = world.getEntitiesWithinAABB(EntityLivingBase.class, new AxisAlignedBB(pos.offset(side), pos.offset(side).add(1, 1, 1))).size();
				BlockPos correctedPos = bBlock.isReplaceable(world, pos) ? pos : pos.offset(side);

				if(entities == 0 && !(correctedPos.getY() < 0) && !(correctedPos.getY() >= 256)) {
					int remove = player.capabilities.isCreativeMode ? 1 : remove(stack, 1);
					if(remove > 0) {
						ItemStack toUse = new ItemStack(bBlock, 1, bmeta);
						ItemsRemainingRenderHandler.set(toUse, getBlockCount(stack));

						Item.getItemFromBlock(bBlock).onItemUse(toUse, player, world, pos, hand, side, hitX, hitY, hitZ);
						set = true;
					}
				}
			}
		}

		player.setItemStackToSlot(hand == EnumHand.MAIN_HAND ? EntityEquipmentSlot.MAINHAND : EntityEquipmentSlot.OFFHAND, stack);
		return set ? EnumActionResult.SUCCESS : EnumActionResult.PASS;
	}

	@Override
	public void onUpdate(ItemStack itemstack, World world, Entity entity, int slot, boolean selected) {
		Block block = getBlock(itemstack);
		if(!entity.worldObj.isRemote && itemstack.getItemDamage() == 1 && block != Blocks.AIR && entity instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) entity;
			int meta = getBlockMeta(itemstack);

			int highest = -1;
			int[] counts = new int[player.inventory.getSizeInventory() - player.inventory.armorInventory.length];

			for(int i = 0; i < counts.length; i++) {
				ItemStack stack = player.inventory.getStackInSlot(i);
				if(stack == null) {
					continue;
				}

				if(Item.getItemFromBlock(block) == stack.getItem() && stack.getItemDamage() == meta) {
					counts[i] = stack.stackSize;
					if(highest == -1)
						highest = i;
					else highest = counts[i] > counts[highest] && highest > 8 ? i : highest;
				}
			}

			if(highest == -1) {
				/*ItemStack heldItem = player.inventory.getItemStack();
				if(hasFreeSlot && (heldItem == null || Item.getItemFromBlock(block) == heldItem.getItem() || heldItem.getItemDamage() != meta)) {
					ItemStack stack = new ItemStack(block, remove(itemstack, 64), meta);
					if(stack.stackSize != 0)
						player.inventory.addItemStackToInventory(stack);
				}*/
				// Used to keep one stack, disabled for now
			} else {
				for(int i = 0; i < counts.length; i++) {
					int count = counts[i];

					// highest is used to keep one stack, disabled for now
					if(/*i == highest || */count == 0)
						continue;

					add(itemstack, count);
					player.inventory.setInventorySlotContents(i, null);
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
	public String getItemStackDisplayName(@Nonnull ItemStack par1ItemStack) {
		Block block = getBlock(par1ItemStack);
		int meta = getBlockMeta(par1ItemStack);
		ItemStack stack = new ItemStack(block, 1, meta);

		return super.getItemStackDisplayName(par1ItemStack) + (stack == null || stack.getItem() == null ? "" : " (" + TextFormatting.GREEN + stack.getDisplayName() + TextFormatting.RESET + ")");
	}

	@Nonnull
	@Override
	public ItemStack getContainerItem(@Nonnull ItemStack itemStack) {
		int count = getBlockCount(itemStack);
		if(count == 0)
			return null;

		int extract = Math.min(64, count);
		ItemStack copy = itemStack.copy();
		remove(copy, extract);

		int dmg = copy.getItemDamage();
		if(dmg == 1)
			copy.setItemDamage(0);

		return copy;
	}

	@Override
	public boolean hasContainerItem(ItemStack stack) {
		return getContainerItem(stack) != null;
	}

	private boolean setBlock(ItemStack stack, Block block, int meta) {
		if(getBlock(stack) == Blocks.AIR || getBlockCount(stack) == 0) {
			ItemNBTHelper.setString(stack, TAG_BLOCK_NAME, Block.REGISTRY.getNameForObject(block).toString());
			ItemNBTHelper.setInt(stack, TAG_BLOCK_META, meta);
			return true;
		}
		return false;
	}

	private void add(ItemStack stack, int count) {
		int current = getBlockCount(stack);
		setCount(stack, current + count);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack par1ItemStack, EntityPlayer player, List<String> stacks, boolean par4) {
		Block block = getBlock(par1ItemStack);
		if(block != null && block != Blocks.AIR) {
			int count = getBlockCount(par1ItemStack);
			stacks.add(count + " " + I18n.format(new ItemStack(block, 1, getBlockMeta(par1ItemStack)).getUnlocalizedName() + ".name"));
		}

		if(par1ItemStack.getItemDamage() == 1)
			addStringToTooltip(I18n.format("botaniamisc.active"), stacks);
		else addStringToTooltip(I18n.format("botaniamisc.inactive"), stacks);
	}

	void addStringToTooltip(String s, List<String> tooltip) {
		tooltip.add(s.replaceAll("&", "\u00a7"));
	}

	private static void setCount(ItemStack stack, int count) {
		ItemNBTHelper.setInt(stack, TAG_BLOCK_COUNT, count);
	}

	public static int remove(ItemStack stack, int count) {
		int current = getBlockCount(stack);
		setCount(stack, Math.max(current - count, 0));

		return Math.min(current, count);
	}

	public static String getBlockName(ItemStack stack) {
		return ItemNBTHelper.getString(stack, TAG_BLOCK_NAME, "");
	}

	public static Block getBlock(ItemStack stack) {
		Block block = Block.getBlockFromName(getBlockName(stack));
		return block;
	}

	public static int getBlockMeta(ItemStack stack) {
		return ItemNBTHelper.getInt(stack, TAG_BLOCK_META, 0);
	}

	public static int getBlockCount(ItemStack stack) {
		return ItemNBTHelper.getInt(stack, TAG_BLOCK_COUNT, 0);
	}

	@Override
	public boolean provideBlock(EntityPlayer player, ItemStack requestor, ItemStack stack, Block block, int meta, boolean doit) {
		Block stored = getBlock(stack);
		int storedMeta = getBlockMeta(stack);
		if(stored == block && storedMeta == meta) {
			int count = getBlockCount(stack);
			if(count > 0) {
				if(doit)
					setCount(stack, count - 1);
				return true;
			}
		}

		return false;
	}

	@Override
	public int getBlockCount(EntityPlayer player, ItemStack requestor, ItemStack stack, Block block, int meta) {
		Block stored = getBlock(stack);
		int storedMeta = getBlockMeta(stack);
		if(stored == block && storedMeta == meta)
			return getBlockCount(stack);
		return 0;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void registerModels() {
		ModelHandler.registerItemAppendMeta(this, 2, LibItemNames.BLACK_HOLE_TALISMAN);
	}

}
