/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [26/10/2016, 18:23:11 (GMT)]
 */
package vazkii.botania.common.item;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.EnumFacing.Axis;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import vazkii.botania.api.item.IBlockProvider;
import vazkii.botania.client.core.handler.ItemsRemainingRenderHandler;
import vazkii.botania.common.core.helper.ItemNBTHelper;
import vazkii.botania.common.item.equipment.tool.ToolCommons;
import vazkii.botania.common.lib.LibItemNames;

public class ItemAstrolabe extends ItemMod {

	private static final String TAG_BLOCK_NAME = "blockName";
	private static final String TAG_BLOCK_META = "blockMeta";
	private static final String TAG_SIZE = "size";

	public ItemAstrolabe() {
		super(LibItemNames.ASTROLABE);
		setMaxStackSize(1);
	}

	@Override
	public EnumActionResult onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		IBlockState state = worldIn.getBlockState(pos);
		Block block = state.getBlock();
		int meta = block.getMetaFromState(state);

		if(playerIn.isSneaking()) {
			if(setBlock(stack, block, meta))
				return EnumActionResult.SUCCESS;
		} else {
			boolean did = placeAllBlocks(stack, playerIn);
			if(!worldIn.isRemote)
				playerIn.swingArm(hand);
			return did ? EnumActionResult.SUCCESS : EnumActionResult.FAIL;
		}

		return EnumActionResult.PASS;
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn, EnumHand hand) {
		if(playerIn.isSneaking()) {
			int size = getSize(itemStackIn);
			int newSize = size == 11 ? 3 : size + 2;
			setSize(itemStackIn, newSize);
			ItemsRemainingRenderHandler.set(itemStackIn, newSize + "x" + newSize);
			worldIn.playSound(playerIn, playerIn.getPosition(), SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.PLAYERS, 0.5F, 1F);
			
			return new ActionResult(EnumActionResult.SUCCESS, itemStackIn);
		}

		return new ActionResult(EnumActionResult.PASS, itemStackIn);
	}

	public boolean placeAllBlocks(ItemStack stack, EntityPlayer player) {
		BlockPos[] blocksToPlace = getBlocksToPlace(stack, player);
		if(!hasBlocks(stack, player, blocksToPlace))
			return false;

		ItemStack stackToPlace = new ItemStack(getBlock(stack), 1, getBlockMeta(stack));
		for(BlockPos coords : blocksToPlace)
			placeBlockAndConsume(player, stack, stackToPlace, coords);
		return true;
	}

	private void placeBlockAndConsume(EntityPlayer player, ItemStack requestor, ItemStack blockToPlace, BlockPos coords) {
		if(blockToPlace.getItem() == null)
			return;
		
		Block block = Block.getBlockFromItem(blockToPlace.getItem());
		int meta = blockToPlace.getItemDamage();
		IBlockState state = block.getStateFromMeta(meta);
		player.worldObj.setBlockState(coords, state, 1 | 2);
		player.worldObj.playEvent(2001, coords, Block.getStateId(state));

		if(player.capabilities.isCreativeMode)
			return;
		
		List<ItemStack> stacksToCheck = new ArrayList();
		for(int i = 0; i < player.inventory.getSizeInventory(); i++) {
			ItemStack stackInSlot = player.inventory.getStackInSlot(i);
			if(stackInSlot != null && stackInSlot.getItem() == blockToPlace.getItem() && stackInSlot.getItemDamage() == blockToPlace.getItemDamage()) {
				stackInSlot.stackSize--;
				if(stackInSlot.stackSize == 0)
					player.inventory.setInventorySlotContents(i, null);

				return;
			}

			if (stackInSlot != null && stackInSlot.getItem() instanceof IBlockProvider)
				stacksToCheck.add(stackInSlot);
		}

		for(ItemStack providerStack : stacksToCheck) {
			IBlockProvider prov = (IBlockProvider) providerStack.getItem();
			
			if(prov.provideBlock(player, requestor, providerStack, block, meta, false)) {
				prov.provideBlock(player, requestor, providerStack, block, meta, true);
				return;
			}
		}
	}

	public static boolean hasBlocks(ItemStack stack, EntityPlayer player, BlockPos[] blocks) {
		if (player.capabilities.isCreativeMode)
			return true;

		
		Block block = getBlock(stack);
		int meta = getBlockMeta(stack);
		ItemStack reqStack = new ItemStack(block, 1, meta);
		
		int required = blocks.length;
		int current = 0;
		List<ItemStack> stacksToCheck = new ArrayList();
		for (int i = 0; i < player.inventory.getSizeInventory(); i++) {
			ItemStack stackInSlot = player.inventory.getStackInSlot(i);
			if (stackInSlot != null && stackInSlot.getItem() == reqStack.getItem() && stackInSlot.getItemDamage() == reqStack.getItemDamage()) {
				current += stackInSlot.stackSize;
				if (current >= required)
					return true;
			}
			if(stackInSlot != null && stackInSlot.getItem() instanceof IBlockProvider)
				stacksToCheck.add(stackInSlot);
		}
		
		for(ItemStack providerStack : stacksToCheck) {
			IBlockProvider prov = (IBlockProvider) providerStack.getItem();
			current += prov.getBlockCount(player, stack, providerStack, block, meta);
			
			if(current >= required)
				return true;
		}

		return false;
	}

	public static BlockPos[] getBlocksToPlace(ItemStack stack, EntityPlayer player) {
		List<BlockPos> coords = new ArrayList();
		RayTraceResult pos = ToolCommons.raytraceFromEntity(player.worldObj, player, true, 5);
		if(pos != null) {
			BlockPos bpos = pos.getBlockPos();
			IBlockState state = player.worldObj.getBlockState(bpos);
			Block block = state.getBlock();
			if(block != null && block.isReplaceable(player.worldObj, bpos))
				bpos = bpos.down();;

			int rotation = MathHelper.floor_double(player.rotationYaw * 4F / 360F + 0.5D) & 3;
			int range = (getSize(stack) ^ 1) / 2;

			EnumFacing dir = pos.sideHit;
			EnumFacing rotationDir = EnumFacing.fromAngle(player.rotationYaw);
			
			boolean pitchedVertically = player.rotationPitch > 70 || player.rotationPitch < -70;
			
			boolean axisX = rotationDir.getAxis() == Axis.X;
			boolean axisZ = rotationDir.getAxis() == Axis.Z;
			
			int xOff, yOff, zOff;
			
			xOff = axisZ || pitchedVertically ? range : 0;
			yOff = pitchedVertically ? 0 : range;
			zOff = axisX || pitchedVertically ? range : 0;
			
			for(int x = -xOff; x < xOff + 1; x++)
				for(int y = 0; y < yOff * 2 + 1; y++) {
					for(int z = -zOff; z < zOff + 1; z++) {
						int xp = bpos.getX() + x + dir.getFrontOffsetX();
						int yp = bpos.getY() + y + dir.getFrontOffsetY();
						int zp = bpos.getZ() + z + dir.getFrontOffsetZ();

						BlockPos newPos = new BlockPos(xp, yp, zp);
						IBlockState state1 = player.worldObj.getBlockState(newPos);
						Block block1 = state1.getBlock();
						if(block1 == null || block1.isAir(state1, player.worldObj, newPos) || block1.isReplaceable(player.worldObj, newPos))
							coords.add(new BlockPos(xp, yp, zp));
					}
				}

		}

		return coords.toArray(new BlockPos[coords.size()]);
	}

	private boolean setBlock(ItemStack stack, Block block, int meta) {
		if(Item.getItemFromBlock(block) != null) {
			ItemNBTHelper.setString(stack, TAG_BLOCK_NAME, Block.REGISTRY.getNameForObject(block).toString());
			ItemNBTHelper.setInt(stack, TAG_BLOCK_META, meta);
			return true;
		}
		return false;
	}	

	private static void setSize(ItemStack stack, int size) {
		ItemNBTHelper.setInt(stack, TAG_SIZE, size | 1);
	}

	public static int getSize(ItemStack stack) {
		return ItemNBTHelper.getInt(stack, TAG_SIZE, 3) | 1;
	}

	public static Block getBlock(ItemStack stack) {
		Block block = Block.getBlockFromName(getBlockName(stack));
		if(block == null)
			return Blocks.AIR;
		
		return block;
	}

	public static String getBlockName(ItemStack stack) {
		return ItemNBTHelper.getString(stack, TAG_BLOCK_NAME, "");
	}

	public static int getBlockMeta(ItemStack stack) {
		return ItemNBTHelper.getInt(stack, TAG_BLOCK_META, 0);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4) {
		Block block = getBlock(par1ItemStack);
		int size = getSize(par1ItemStack);

		par3List.add(size + " x " + size);
		if (block != null && block != Blocks.AIR)
			par3List.add(new ItemStack(block, 1, getBlockMeta(par1ItemStack)).getDisplayName());
	}

}
