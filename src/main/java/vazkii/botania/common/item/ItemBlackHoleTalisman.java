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

import java.util.Arrays;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IIcon;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.oredict.RecipeSorter;
import net.minecraftforge.oredict.RecipeSorter.Category;
import vazkii.botania.api.item.IBlockProvider;
import vazkii.botania.client.core.handler.ItemsRemainingRenderHandler;
import vazkii.botania.client.core.helper.IconHelper;
import vazkii.botania.common.core.helper.InventoryHelper;
import vazkii.botania.common.core.helper.ItemNBTHelper;
import vazkii.botania.common.crafting.recipe.BlackHoleTalismanExtractRecipe;
import vazkii.botania.common.lib.LibItemNames;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemBlackHoleTalisman extends ItemMod implements IBlockProvider {

	private static final String TAG_BLOCK_NAME = "blockName";
	private static final String TAG_BLOCK_META = "blockMeta";
	private static final String TAG_BLOCK_COUNT = "blockCount";

	IIcon enabledIcon;

	public ItemBlackHoleTalisman() {
		setUnlocalizedName(LibItemNames.BLACK_HOLE_TALISMAN);
		setMaxStackSize(1);
		setHasSubtypes(true);

		GameRegistry.addRecipe(new BlackHoleTalismanExtractRecipe());
		RecipeSorter.register("botania:blackHoleTalismanExtract", BlackHoleTalismanExtractRecipe.class, Category.SHAPELESS, "");
	}

	@Override
	public ItemStack onItemRightClick(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer) {
		if(getBlock(par1ItemStack) != Blocks.air && par3EntityPlayer.isSneaking()) {
			int dmg = par1ItemStack.getItemDamage();
			par1ItemStack.setItemDamage(~dmg & 1);
			par2World.playSoundAtEntity(par3EntityPlayer, "random.orb", 0.3F, 0.1F);
		}

		return par1ItemStack;
	}

	@Override
	public boolean onItemUse(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, World par3World, int par4, int par5, int par6, int par7, float par8, float par9, float par10) {
		Block block = par3World.getBlock(par4, par5, par6);
		int meta = par3World.getBlockMetadata(par4, par5, par6);
		boolean set = setBlock(par1ItemStack, block, meta);

		if(!set) {
			Block bBlock = getBlock(par1ItemStack);
			int bmeta = getBlockMeta(par1ItemStack);

			TileEntity tile = par3World.getTileEntity(par4, par5, par6);
			if(tile != null && tile instanceof IInventory) {
				IInventory inv = (IInventory) tile;
				int[] slots = inv instanceof ISidedInventory ? ((ISidedInventory) inv).getAccessibleSlotsFromSide(par7) : InventoryHelper.buildSlotsForLinearInventory(inv);
				for(int slot : slots) {
					ItemStack stackInSlot = inv.getStackInSlot(slot);
					if(stackInSlot == null) {
						ItemStack stack = new ItemStack(bBlock, 1, bmeta);
						int maxSize = stack.getMaxStackSize();
						stack.stackSize = remove(par1ItemStack, maxSize);
						if(stack.stackSize != 0) {
							if(inv.isItemValidForSlot(slot, stack) && (!(inv instanceof ISidedInventory) || ((ISidedInventory) inv).canInsertItem(slot, stack, par7))) {
								inv.setInventorySlotContents(slot, stack);
								inv.markDirty();
								set = true;
							}
						}
					} else if(stackInSlot.getItem() == Item.getItemFromBlock(bBlock) && stackInSlot.getItemDamage() == bmeta) {
						int maxSize = stackInSlot.getMaxStackSize();
						int missing = maxSize - stackInSlot.stackSize;
						if(inv.isItemValidForSlot(slot, stackInSlot) && (!(inv instanceof ISidedInventory) || ((ISidedInventory) inv).canInsertItem(slot, stackInSlot, par7))) {
							stackInSlot.stackSize += remove(par1ItemStack, missing);
							inv.markDirty();
							set = true;
						}
					}
				}
			} else {
				ForgeDirection dir = ForgeDirection.getOrientation(par7);
				int entities = par3World.getEntitiesWithinAABB(EntityLivingBase.class, AxisAlignedBB.getBoundingBox(par4 + dir.offsetX, par5 + dir.offsetY, par6 + dir.offsetZ, par4 + dir.offsetX + 1, par5 + dir.offsetY + 1, par6 + dir.offsetZ + 1)).size();

				if(entities == 0) {
					int remove = par2EntityPlayer.capabilities.isCreativeMode ? 1 : remove(par1ItemStack, 1);
					if(remove > 0) {
						ItemStack stack = new ItemStack(bBlock, 1, bmeta);
						ItemsRemainingRenderHandler.set(stack, getBlockCount(par1ItemStack));

						Item.getItemFromBlock(bBlock).onItemUse(stack, par2EntityPlayer, par3World, par4, par5, par6, par7, par8, par9, par10);
						set = true;
					}
				}
			}
		}

		par2EntityPlayer.setCurrentItemOrArmor(0, par1ItemStack);
		return set;
	}

	@Override
	public void onUpdate(ItemStack itemstack, World p_77663_2_, Entity entity, int p_77663_4_, boolean p_77663_5_) {
		Block block = getBlock(itemstack);
		if(!entity.worldObj.isRemote && itemstack.getItemDamage() == 1 && block != Blocks.air && entity instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) entity;
			int meta = getBlockMeta(itemstack);

			int highest = -1;
			int[] counts = new int[player.inventory.getSizeInventory() - player.inventory.armorInventory.length];
			Arrays.fill(counts, 0);

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

	@Override
	public String getItemStackDisplayName(ItemStack par1ItemStack) {
		Block block = getBlock(par1ItemStack);
		int meta = getBlockMeta(par1ItemStack);
		ItemStack stack = new ItemStack(block, 1, meta);

		return super.getItemStackDisplayName(par1ItemStack) + (stack == null || stack.getItem() == null ? "" : " (" + EnumChatFormatting.GREEN + stack.getDisplayName() + EnumChatFormatting.RESET + ")");
	}

	@Override
	public ItemStack getContainerItem(ItemStack itemStack) {
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

	@Override
	public boolean doesContainerItemLeaveCraftingGrid(ItemStack p_77630_1_) {
		return false;
	}

	private boolean setBlock(ItemStack stack, Block block, int meta) {
		if(getBlock(stack) == Blocks.air || getBlockCount(stack) == 0) {
			ItemNBTHelper.setString(stack, TAG_BLOCK_NAME, Block.blockRegistry.getNameForObject(block));
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
	public void registerIcons(IIconRegister par1IconRegister) {
		itemIcon = IconHelper.forItem(par1IconRegister, this, 0);
		enabledIcon = IconHelper.forItem(par1IconRegister, this, 1);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIconFromDamage(int par1) {
		return par1 == 1 ? enabledIcon : itemIcon;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4) {
		Block block = getBlock(par1ItemStack);
		if(block != null && block != Blocks.air) {
			int count = getBlockCount(par1ItemStack);
			par3List.add(count + " " + StatCollector.translateToLocal(new ItemStack(block, 1, getBlockMeta(par1ItemStack)).getUnlocalizedName() + ".name"));
		}

		if(par1ItemStack.getItemDamage() == 1)
			addStringToTooltip(StatCollector.translateToLocal("botaniamisc.active"), par3List);
		else addStringToTooltip(StatCollector.translateToLocal("botaniamisc.inactive"), par3List);
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

}
