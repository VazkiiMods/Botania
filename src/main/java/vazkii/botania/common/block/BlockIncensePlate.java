/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [May 15, 2015, 4:07:09 PM (GMT)]
 */
package vazkii.botania.common.block;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import vazkii.botania.api.state.BotaniaStateProps;
import vazkii.botania.api.internal.VanillaPacketDispatcher;
import vazkii.botania.api.lexicon.ILexiconable;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.client.lib.LibRenderIDs;
import vazkii.botania.common.block.tile.TileIncensePlate;
import vazkii.botania.common.lexicon.LexiconData;
import vazkii.botania.common.lib.LibBlockNames;

public class BlockIncensePlate extends BlockModContainer implements ILexiconable {

	protected BlockIncensePlate() {
		super(Material.wood);
		setUnlocalizedName(LibBlockNames.INCENSE_PLATE);
		setHardness(2.0F);
		setStepSound(soundTypeWood);
		setBlockBounds(EnumFacing.Axis.Z);
		setDefaultState(blockState.getBaseState().withProperty(BotaniaStateProps.CARDINALS, EnumFacing.SOUTH));
	}

	@Override
	public BlockState createBlockState() {
		return new BlockState(this, BotaniaStateProps.CARDINALS);
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return state.getValue(BotaniaStateProps.CARDINALS).getIndex();
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		if (meta < 2 || meta > 5) {
			meta = 2;
		}
		return getDefaultState().withProperty(BotaniaStateProps.CARDINALS, EnumFacing.getFront(meta));
	}

	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumFacing s, float xs, float ys, float zs) {
		TileIncensePlate plate = (TileIncensePlate) world.getTileEntity(pos);
		ItemStack stack = player.getCurrentEquippedItem();
		ItemStack plateStack = plate.getStackInSlot(0);
		boolean did = false;

		if(world.isRemote)
			return true;

		if(plateStack == null && plate.isItemValidForSlot(0, stack)) {
			plate.setInventorySlotContents(0, stack.copy());
			stack.stackSize--;
			did = true;
		} else if(plateStack != null && !plate.burning) {
			if(stack != null && stack.getItem() == Items.flint_and_steel) {
				plate.ignite();
				stack.damageItem(1, player);
				did = true;
			} else {
				ItemStack addStack = plateStack.copy();
				if(!player.inventory.addItemStackToInventory(addStack))
					player.dropPlayerItemWithRandomChoice(addStack, false);
				plate.setInventorySlotContents(0, null);

				did = true;
			}
		}

		if(did)
			VanillaPacketDispatcher.dispatchTEToNearbyPlayers(plate);

		return did;
	}

	@Override
	public void onBlockPlacedBy(World p_149689_1_, BlockPos pos, IBlockState state, EntityLivingBase p_149689_5_, ItemStack p_149689_6_) {
		p_149689_1_.setBlockState(pos, state.withProperty(BotaniaStateProps.CARDINALS, p_149689_5_.getHorizontalFacing().getOpposite()));
	}

	@Override
	public boolean hasComparatorInputOverride() {
		return true;
	}

	@Override
	public int getComparatorInputOverride(World world, BlockPos pos) {
		return ((TileIncensePlate) world.getTileEntity(pos)).comparatorOutput;
	}

	@Override
	public void setBlockBoundsBasedOnState(IBlockAccess w, BlockPos pos) {
		setBlockBounds(w.getBlockState(pos).getValue(BotaniaStateProps.CARDINALS).getAxis());
	}

	public void setBlockBounds(EnumFacing.Axis axis) {
		float f = 1F / 16F;
		float w = 12 * f;
		float l = 4 * f;
		float xs = (1F - w) / 2;
		float zs = (1F - l) / 2;
		if(axis == EnumFacing.Axis.Z)
			setBlockBounds(xs, 0F, zs, 1F - xs, f, 1f - zs);
		else setBlockBounds(zs, 0F, xs, 1F - zs, f, 1f - xs);
	}

	@Override
	public boolean isOpaqueCube() {
		return false;
	}

	@Override
	public boolean isFullCube() {
		return false;
	}

	@Override
	public TileEntity createNewTileEntity(World world, int meta) {
		return new TileIncensePlate();
	}

	@Override
	public LexiconEntry getEntry(World world, BlockPos pos, EntityPlayer player, ItemStack lexicon) {
		return LexiconData.incense;
	}

}
