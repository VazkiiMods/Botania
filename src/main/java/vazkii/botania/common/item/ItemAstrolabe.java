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

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumFacing.Axis;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import vazkii.botania.api.item.IBlockProvider;
import vazkii.botania.api.mana.ManaItemHandler;
import vazkii.botania.client.core.handler.ItemsRemainingRenderHandler;
import vazkii.botania.common.core.helper.ItemNBTHelper;
import vazkii.botania.common.item.equipment.tool.ToolCommons;
import vazkii.botania.common.item.rod.ItemExchangeRod;
import vazkii.botania.common.lib.LibItemNames;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class ItemAstrolabe extends ItemMod {

	private static final String TAG_BLOCK_NAME = "blockName";
	private static final String TAG_BLOCK_META = "blockMeta";
	private static final String TAG_SIZE = "size";

	public ItemAstrolabe() {
		super(LibItemNames.ASTROLABE);
		setMaxStackSize(1);
	}

	@Nonnull
	@Override
	public EnumActionResult onItemUse(EntityPlayer playerIn, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		ItemStack stack = playerIn.getHeldItem(hand);
		IBlockState state = worldIn.getBlockState(pos);
		Block block = state.getBlock();
		int meta = block.getMetaFromState(state);

		if(playerIn.isSneaking()) {
			if(setBlock(stack, block, meta)) {
				displayRemainderCounter(playerIn, stack);
				return EnumActionResult.SUCCESS;
			}
		} else {
			boolean did = placeAllBlocks(stack, playerIn);
			if(did) {
				displayRemainderCounter(playerIn, stack);
				if(!worldIn.isRemote)
					playerIn.swingArm(hand);
			}

			return did ? EnumActionResult.SUCCESS : EnumActionResult.FAIL;
		}

		return EnumActionResult.PASS;
	}

	@Nonnull
	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, @Nonnull EnumHand hand) {
		ItemStack stack = playerIn.getHeldItem(hand);
		if(playerIn.isSneaking()) {
			int size = getSize(stack);
			int newSize = size == 11 ? 3 : size + 2;
			setSize(stack, newSize);
			ItemsRemainingRenderHandler.set(stack, newSize + "x" + newSize);
			worldIn.playSound(playerIn, playerIn.getPosition(), SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.PLAYERS, 0.5F, 1F);
			
			return ActionResult.newResult(EnumActionResult.SUCCESS, stack);
		}

		return ActionResult.newResult(EnumActionResult.PASS, stack);
	}

	public boolean placeAllBlocks(ItemStack stack, EntityPlayer player) {
		List<BlockPos> blocksToPlace = getBlocksToPlace(stack, player);
		if(!hasBlocks(stack, player, blocksToPlace))
			return false;

		int size = getSize(stack);
		int cost = size * 320;
		if(!ManaItemHandler.requestManaExact(stack, player, cost, false))
			return false;
		
		ItemStack stackToPlace = new ItemStack(getBlock(stack), 1, getBlockMeta(stack));
		for(BlockPos coords : blocksToPlace)
			placeBlockAndConsume(player, stack, stackToPlace, coords);
		ManaItemHandler.requestManaExact(stack, player, cost, true);
		
		return true;
	}

	private void placeBlockAndConsume(EntityPlayer player, ItemStack requestor, ItemStack blockToPlace, BlockPos coords) {
		if(blockToPlace.getItem() == null)
			return;
		
		Block block = Block.getBlockFromItem(blockToPlace.getItem());
		int meta = blockToPlace.getItemDamage();
		IBlockState state = block.getStateFromMeta(meta);
		player.world.setBlockState(coords, state, 1 | 2);
		player.world.playEvent(2001, coords, Block.getStateId(state));

		if(player.capabilities.isCreativeMode)
			return;
		
		List<ItemStack> stacksToCheck = new ArrayList<>();
		for(int i = 0; i < player.inventory.getSizeInventory(); i++) {
			ItemStack stackInSlot = player.inventory.getStackInSlot(i);
			if(!stackInSlot.isEmpty() && stackInSlot.getItem() == blockToPlace.getItem() && stackInSlot.getItemDamage() == blockToPlace.getItemDamage()) {
				stackInSlot.shrink(1);
				return;
			}

			if (!stackInSlot.isEmpty() && stackInSlot.getItem() instanceof IBlockProvider)
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

	public static boolean hasBlocks(ItemStack stack, EntityPlayer player, List<BlockPos> blocks) {
		if (player.capabilities.isCreativeMode)
			return true;

		Block block = getBlock(stack);
		int meta = getBlockMeta(stack);
		ItemStack reqStack = new ItemStack(block, 1, meta);
		
		int required = blocks.size();
		int current = 0;
		List<ItemStack> stacksToCheck = new ArrayList<>();
		for (int i = 0; i < player.inventory.getSizeInventory(); i++) {
			ItemStack stackInSlot = player.inventory.getStackInSlot(i);
			if (!stackInSlot.isEmpty() && stackInSlot.getItem() == reqStack.getItem() && stackInSlot.getItemDamage() == reqStack.getItemDamage()) {
				current += stackInSlot.getCount();
				if (current >= required)
					return true;
			}
			if(!stackInSlot.isEmpty() && stackInSlot.getItem() instanceof IBlockProvider)
				stacksToCheck.add(stackInSlot);
		}
		
		for(ItemStack providerStack : stacksToCheck) {
			IBlockProvider prov = (IBlockProvider) providerStack.getItem();
			int count = prov.getBlockCount(player, stack, providerStack, block, meta);
			if(count == -1)
				return true;
			
			current += count; 
			
			if(current >= required)
				return true;
		}

		return false;
	}

	public static List<BlockPos> getBlocksToPlace(ItemStack stack, EntityPlayer player) {
		List<BlockPos> coords = new ArrayList<>();
		RayTraceResult rtr = ToolCommons.raytraceFromEntity(player.world, player, true, 5);
		if(rtr != null) {
			BlockPos pos = rtr.getBlockPos();
			IBlockState state = player.world.getBlockState(pos);
			Block block = state.getBlock();
			if(block.isReplaceable(player.world, pos))
				pos = pos.down();

			int range = (getSize(stack) ^ 1) / 2;

			EnumFacing dir = rtr.sideHit;
			EnumFacing rotationDir = EnumFacing.fromAngle(player.rotationYaw);
			
			boolean pitchedVertically = player.rotationPitch > 70 || player.rotationPitch < -70;
			
			boolean axisX = rotationDir.getAxis() == Axis.X;
			boolean axisZ = rotationDir.getAxis() == Axis.Z;
			
			int xOff = axisZ || pitchedVertically ? range : 0;
			int yOff = pitchedVertically ? 0 : range;
			int zOff = axisX || pitchedVertically ? range : 0;
			
			for(int x = -xOff; x < xOff + 1; x++)
				for(int y = 0; y < yOff * 2 + 1; y++) {
					for(int z = -zOff; z < zOff + 1; z++) {
						int xp = pos.getX() + x + dir.getXOffset();
						int yp = pos.getY() + y + dir.getYOffset();
						int zp = pos.getZ() + z + dir.getZOffset();

						BlockPos newPos = new BlockPos(xp, yp, zp);
						IBlockState state1 = player.world.getBlockState(newPos);
						Block block1 = state1.getBlock();
						if(player.world.getWorldBorder().contains(newPos)
								&& (block1.isAir(state1, player.world, newPos) || block1.isReplaceable(player.world, newPos)))
							coords.add(newPos);
					}
				}

		}

		return coords;
	}
	

	public void displayRemainderCounter(EntityPlayer player, ItemStack stack) {
		Block block = getBlock(stack);
		int meta = getBlockMeta(stack);
		int count = ItemExchangeRod.getInventoryItemCount(player, stack, block, meta);
		if(!player.world.isRemote)
			ItemsRemainingRenderHandler.set(new ItemStack(block, 1, meta), count);
	}

	private boolean setBlock(ItemStack stack, Block block, int meta) {
		if(Item.getItemFromBlock(block) != Items.AIR) {
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
	public void addInformation(ItemStack par1ItemStack, World world, List par3List, ITooltipFlag flags) {
		Block block = getBlock(par1ItemStack);
		int size = getSize(par1ItemStack);

		par3List.add(size + " x " + size);
		if (block != null && block != Blocks.AIR)
			par3List.add(new ItemStack(block, 1, getBlockMeta(par1ItemStack)).getDisplayName());
	}

}
