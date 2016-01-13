/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [Aug 20, 2015, 8:08:34 PM (GMT)]
 */
package vazkii.botania.common.item.rod;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.Action;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.item.IBlockProvider;
import vazkii.botania.api.item.IManaProficiencyArmor;
import vazkii.botania.api.item.IWireframeCoordinateListProvider;
import vazkii.botania.api.mana.IManaUsingItem;
import vazkii.botania.api.mana.ManaItemHandler;
import vazkii.botania.client.core.handler.ItemsRemainingRenderHandler;
import vazkii.botania.common.block.BlockCamo;
import vazkii.botania.common.core.helper.ItemNBTHelper;
import vazkii.botania.common.item.ItemMod;
import vazkii.botania.common.lib.LibItemNames;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemExchangeRod extends ItemMod implements IManaUsingItem, IWireframeCoordinateListProvider {

	private static final int RANGE = 3;
	private static final int COST = 40;

	private static final String TAG_BLOCK_NAME = "blockName";
	private static final String TAG_BLOCK_META = "blockMeta";
	private static final String TAG_TARGET_BLOCK_NAME = "targetBlockName";
	private static final String TAG_TARGET_BLOCK_META = "targetBlockMeta";
	private static final String TAG_SWAPPING = "swapping";
	private static final String TAG_SELECT_X = "selectX";
	private static final String TAG_SELECT_Y = "selectY";
	private static final String TAG_SELECT_Z = "selectZ";
	private static final String TAG_EXTRA_RANGE = "extraRange";

	public ItemExchangeRod() {
		setMaxStackSize(1);
		setUnlocalizedName(LibItemNames.EXCHANGE_ROD);
		MinecraftForge.EVENT_BUS.register(this);
	}

	@Override
	public boolean onItemUse(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, World par3World, int par4, int par5, int par6, int par7, float par8, float par9, float par10) {
		Block wblock = par3World.getBlock(par4, par5, par6);
		int wmeta = par3World.getBlockMetadata(par4, par5, par6);

		if(par2EntityPlayer.isSneaking()) {
			TileEntity tile = par3World.getTileEntity(par4, par5, par6);
			if(tile == null) {
				if(BlockCamo.isValidBlock(wblock)) {
					Item item = Item.getItemFromBlock(wblock);
					if(!item.getHasSubtypes())
						wmeta = 0;

					boolean set = setBlock(par1ItemStack, wblock, wmeta);
					par2EntityPlayer.setCurrentItemOrArmor(0, par1ItemStack);

					displayRemainderCounter(par2EntityPlayer, par1ItemStack);
					return set;
				}
			}
		} else if(canExchange(par1ItemStack) && !ItemNBTHelper.getBoolean(par1ItemStack, TAG_SWAPPING, false)) {
			Block block = getBlock(par1ItemStack);
			int meta = getBlockMeta(par1ItemStack);
			List<ChunkCoordinates> swap = getBlocksToSwap(par3World, par1ItemStack, block, meta, par4, par5, par6, null, 0);
			if(swap.size() > 0) {
				ItemNBTHelper.setBoolean(par1ItemStack, TAG_SWAPPING, true);
				ItemNBTHelper.setInt(par1ItemStack, TAG_SELECT_X, par4);
				ItemNBTHelper.setInt(par1ItemStack, TAG_SELECT_Y, par5);
				ItemNBTHelper.setInt(par1ItemStack, TAG_SELECT_Z, par6);
				setTargetBlock(par1ItemStack, wblock, wmeta);
				if(par3World.isRemote)
					par2EntityPlayer.swingItem();
			}
		}

		return false;
	}

	@SubscribeEvent
	public void onLeftClick(PlayerInteractEvent event) {
		if(event.action == Action.LEFT_CLICK_BLOCK) {
			ItemStack stack = event.entityPlayer.getCurrentEquippedItem();
			if(stack != null && stack.getItem() == this && canExchange(stack) && ManaItemHandler.requestManaExactForTool(stack, event.entityPlayer, COST, false)) {
				if(exchange(event.world, event.entityPlayer, event.x, event.y, event.z, stack, getBlock(stack), getBlockMeta(stack)))
					ManaItemHandler.requestManaExactForTool(stack, event.entityPlayer, COST, true);
			}
		}
	}

	@Override
	public void onUpdate(ItemStack stack, World world, Entity entity, int something, boolean somethingelse) {
		if(!canExchange(stack) || !(entity instanceof EntityPlayer))
			return;

		EntityPlayer player = (EntityPlayer) entity;

		int extraRange = ItemNBTHelper.getInt(stack, TAG_EXTRA_RANGE, 1);
		int extraRangeNew = IManaProficiencyArmor.Helper.hasProficiency(player) ? 3 : 1;
		if(extraRange != extraRangeNew)
			ItemNBTHelper.setInt(stack, TAG_EXTRA_RANGE, extraRangeNew);

		Block block = getBlock(stack);
		int meta = getBlockMeta(stack);
		if(ItemNBTHelper.getBoolean(stack, TAG_SWAPPING, false)) {
			if(!ManaItemHandler.requestManaExactForTool(stack, player, COST, false)) {
				ItemNBTHelper.setBoolean(stack, TAG_SWAPPING, false);
				return;
			}

			int x = ItemNBTHelper.getInt(stack, TAG_SELECT_X, 0);
			int y = ItemNBTHelper.getInt(stack, TAG_SELECT_Y, 0);
			int z = ItemNBTHelper.getInt(stack, TAG_SELECT_Z, 0);
			Block targetBlock = getTargetBlock(stack);
			int targetMeta = getTargetBlockMeta(stack);
			List<ChunkCoordinates> swap = getBlocksToSwap(world, stack, block, meta, x, y, z, targetBlock, targetMeta);
			if(swap.size() == 0) {
				ItemNBTHelper.setBoolean(stack, TAG_SWAPPING, false);
				return;
			}

			ChunkCoordinates coords = swap.get(world.rand.nextInt(swap.size()));
			boolean exchange = exchange(world, player, coords.posX, coords.posY, coords.posZ, stack, block, meta);
			if(exchange)
				ManaItemHandler.requestManaExactForTool(stack, player, COST, true);
			else ItemNBTHelper.setBoolean(stack, TAG_SWAPPING, false);
		}
	}

	public List<ChunkCoordinates> getBlocksToSwap(World world, ItemStack stack, Block blockToSwap, int metaToSwap, int xc, int yc, int zc, Block targetBlock, int targetMeta) {
		// If we have no target block passed in, infer it to be
		// the block which the swapping is centered on (presumably the block
		// which the player is looking at)
		if(targetBlock == null) {
			targetBlock = world.getBlock(xc, yc, zc);
			targetMeta = world.getBlockMetadata(xc, yc, zc);
		}

		// Our result list
		List<ChunkCoordinates> coordsList = new ArrayList<ChunkCoordinates>();

		// We subtract 1 from the effective range as the center tile is included
		// So, with a range of 3, we are visiting tiles at -2, -1, 0, 1, 2
		int effRange = RANGE + ItemNBTHelper.getInt(stack, TAG_EXTRA_RANGE, 1) - 1;
		
		// Iterate in all 3 dimensions through our possible positions.
		for(int offsetX = -effRange; offsetX <= effRange; offsetX++)
			for(int offsetY = -effRange; offsetY <= effRange; offsetY++)
				for(int offsetZ = -effRange; offsetZ <= effRange; offsetZ++) {
					int x = xc + offsetX, y = yc + offsetY, z = zc + offsetZ;
					
					Block currentBlock = world.getBlock(x, y, z);
					int currentMeta = world.getBlockMetadata(x, y, z);
					
					// If this block is not our target, ignore it, as we don't need
					// to consider replacing it
					if(currentBlock != targetBlock || currentMeta != targetMeta)
						continue;
					
					// If this block is already the block we're swapping to,
					// we don't need to swap again
					if(currentBlock == blockToSwap && currentMeta == metaToSwap)
						continue;
					
					// Check to see if the block is visible on any side:
					for(ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS) {
						int adjX = x + dir.offsetX, adjY = y + dir.offsetY, adjZ = z + dir.offsetZ;
						Block adjBlock = world.getBlock(adjX, adjY, adjZ);
						
						// If the side of the adjacent block facing this block is
						// _not_ solid, then this block is considered "visible"
						// and should be replaced.
						
						// If there is a rendering-specific way to check for this,
						// that should be placed in preference to this.
						if(!adjBlock.isSideSolid(world, adjX, adjY, adjZ, dir.getOpposite())) {
							coordsList.add(new ChunkCoordinates(x, y, z));
							break;
						}
					}
				}

		return coordsList;
	}

	public boolean exchange(World world, EntityPlayer player, int x, int y, int z, ItemStack stack, Block blockToSet, int metaToSet) {
		TileEntity tile = world.getTileEntity(x, y, z);
		if(tile != null)
			return false;

		ItemStack placeStack = removeFromInventory(player, stack, blockToSet, metaToSet, false);
		if(placeStack != null) {
			Block blockAt = world.getBlock(x, y, z);
			int meta = world.getBlockMetadata(x, y, z);
			if(!blockAt.isAir(world, x, y, z) && blockAt.getPlayerRelativeBlockHardness(player, world, x, y, z) > 0 && (blockAt != blockToSet || meta != metaToSet)) {
				if(!world.isRemote) {
					if(!player.capabilities.isCreativeMode) {
						List<ItemStack> drops = blockAt.getDrops(world, x, y, z, meta, 0);
						for(ItemStack drop : drops)
							world.spawnEntityInWorld(new EntityItem(world, x + 0.5, y + 0.5, z + 0.5, drop));
						removeFromInventory(player, stack, blockToSet, metaToSet, true);
					}
					world.playAuxSFX(2001, x, y, z, Block.getIdFromBlock(blockAt) + (meta << 12));
					world.setBlock(x, y, z, blockToSet, metaToSet, 1 | 2);
					blockToSet.onBlockPlacedBy(world, x, y, z, player, placeStack);
				}
				displayRemainderCounter(player, stack);
				return true;
			}
		}

		return false;
	}

	public boolean canExchange(ItemStack stack) {
		Block block = getBlock(stack);
		return block != null && block != Blocks.air;
	}

	public static ItemStack removeFromInventory(EntityPlayer player, IInventory inv, ItemStack stack, Block block, int meta, boolean doit) {
		List<ItemStack> providers = new ArrayList();
		for(int i = inv.getSizeInventory() - 1; i >= 0; i--) {
			ItemStack invStack = inv.getStackInSlot(i);
			if(invStack == null)
				continue;

			Item item = invStack.getItem();
			if(item == Item.getItemFromBlock(block) && invStack.getItemDamage() == meta) {
				ItemStack retStack = invStack.copy();
				if(doit) {
					invStack.stackSize--;
					if(invStack.stackSize == 0)
						inv.setInventorySlotContents(i, null);
				}
				return retStack;
			}

			if(item instanceof IBlockProvider)
				providers.add(invStack);
		}

		for(ItemStack provStack : providers) {
			IBlockProvider prov = (IBlockProvider) provStack.getItem();
			if(prov.provideBlock(player, stack, provStack, block, meta, doit))
				return new ItemStack(block, 1, meta);
		}

		return null;
	}

	public static ItemStack removeFromInventory(EntityPlayer player, ItemStack stack, Block block, int meta, boolean doit) {
		if(player.capabilities.isCreativeMode)
			return new ItemStack(block, 1, meta);

		ItemStack outStack = removeFromInventory(player, BotaniaAPI.internalHandler.getBaublesInventory(player), stack, block, meta, doit);
		if (outStack == null)
			outStack = removeFromInventory(player, player.inventory, stack, block, meta, doit);
		return outStack;
	}

	public static int getInventoryItemCount(EntityPlayer player, ItemStack stack, Block block, int meta) {
		if(player.capabilities.isCreativeMode)
			return -1;

		int baubleCount = getInventoryItemCount(player, BotaniaAPI.internalHandler.getBaublesInventory(player), stack, block, meta);
		if (baubleCount == -1) return -1;

		int count = getInventoryItemCount(player, player.inventory, stack, block, meta);
		if (count == -1) return -1;
		
		return count+baubleCount;
	}

	public static int getInventoryItemCount(EntityPlayer player, IInventory inv, ItemStack stack, Block block, int meta) {
		if(player.capabilities.isCreativeMode)
			return -1;

		int count = 0;
		for(int i = 0; i < inv.getSizeInventory(); i++) {
			ItemStack invStack = inv.getStackInSlot(i);
			if(invStack == null)
				continue;

			Item item = invStack.getItem();
			if(item == Item.getItemFromBlock(block) && invStack.getItemDamage() == meta)
				count += invStack.stackSize;

			if(item instanceof IBlockProvider) {
				IBlockProvider prov = (IBlockProvider) item;
				int provCount = prov.getBlockCount(player, stack, invStack, block, meta);
				if(provCount == -1)
					return -1;
				count += provCount;
			}
		}

		return count;
	}

	public void displayRemainderCounter(EntityPlayer player, ItemStack stack) {
		Block block = getBlock(stack);
		int meta = getBlockMeta(stack);
		int count = getInventoryItemCount(player, stack, block, meta);
		if(!player.worldObj.isRemote)
			ItemsRemainingRenderHandler.set(new ItemStack(block, 1, meta), count);
	}

	@Override
	public boolean isFull3D() {
		return true;
	}

	@Override
	public boolean usesMana(ItemStack stack) {
		return true;
	}

	private boolean setBlock(ItemStack stack, Block block, int meta) {
		ItemNBTHelper.setString(stack, TAG_BLOCK_NAME, Block.blockRegistry.getNameForObject(block));
		ItemNBTHelper.setInt(stack, TAG_BLOCK_META, meta);
		return true;
	}

	@Override
	public String getItemStackDisplayName(ItemStack par1ItemStack) {
		Block block = getBlock(par1ItemStack);
		int meta = getBlockMeta(par1ItemStack);
		return super.getItemStackDisplayName(par1ItemStack) + (block == null ? "" : " (" + EnumChatFormatting.GREEN + new ItemStack(block, 1, meta).getDisplayName() + EnumChatFormatting.RESET + ")");
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

	private boolean setTargetBlock(ItemStack stack, Block block, int meta) {
		ItemNBTHelper.setString(stack, TAG_TARGET_BLOCK_NAME, Block.blockRegistry.getNameForObject(block));
		ItemNBTHelper.setInt(stack, TAG_TARGET_BLOCK_META, meta);
		return true;
	}

	public static String getTargetBlockName(ItemStack stack) {
		return ItemNBTHelper.getString(stack, TAG_TARGET_BLOCK_NAME, "");
	}

	public static Block getTargetBlock(ItemStack stack) {
		Block block = Block.getBlockFromName(getTargetBlockName(stack));
		return block;
	}

	public static int getTargetBlockMeta(ItemStack stack) {
		return ItemNBTHelper.getInt(stack, TAG_TARGET_BLOCK_META, 0);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public List<ChunkCoordinates> getWireframesToDraw(EntityPlayer player, ItemStack stack) {
		ItemStack holding = player.getCurrentEquippedItem();
		if(holding != stack || !canExchange(stack))
			return null;

		Block block = getBlock(stack);
		int meta = getBlockMeta(stack);

		MovingObjectPosition pos = Minecraft.getMinecraft().objectMouseOver;
		if(pos != null) {
			int x = pos.blockX;
			int y = pos.blockY;
			int z = pos.blockZ;
			Block targetBlock = null;
			int targetMeta = 0;
			if(ItemNBTHelper.getBoolean(stack, TAG_SWAPPING, false)) {
				x = ItemNBTHelper.getInt(stack, TAG_SELECT_X, 0);
				y = ItemNBTHelper.getInt(stack, TAG_SELECT_Y, 0);
				z = ItemNBTHelper.getInt(stack, TAG_SELECT_Z, 0);
				targetBlock = getTargetBlock(stack);
				targetMeta = getTargetBlockMeta(stack);
			}

			if(!player.worldObj.isAirBlock(x, y, z)) {
				List<ChunkCoordinates> coordsList = getBlocksToSwap(player.worldObj, stack, block, meta, x, y, z, targetBlock, targetMeta);
				for(ChunkCoordinates coords : coordsList)
					if(coords.posX == x && coords.posY == y && coords.posZ == z) {
						coordsList.remove(coords);
						break;
					}
				return coordsList;
			}

		}
		return null;
	}

}
