/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [Jan 17, 2015, 7:16:48 PM (GMT)]
 */
package vazkii.botania.common.block.mana;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import vazkii.botania.api.internal.IManaBurst;
import vazkii.botania.api.lexicon.ILexiconable;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.api.mana.ILens;
import vazkii.botania.api.mana.IManaTrigger;
import vazkii.botania.api.state.BotaniaStateProps;
import vazkii.botania.common.block.BlockMod;
import vazkii.botania.common.block.tile.TileSimpleInventory;
import vazkii.botania.common.block.tile.mana.TilePrism;
import vazkii.botania.common.lexicon.LexiconData;
import vazkii.botania.common.lib.LibBlockNames;

import java.util.Random;

public class BlockPrism extends BlockMod implements IManaTrigger, ILexiconable {

	private static final AxisAlignedBB AABB = new AxisAlignedBB(0.25, 0, 0.25, 0.75, 1, 0.75);

	private final Random random = new Random();

	public BlockPrism() {
		super(Material.glass, LibBlockNames.PRISM);
		setHardness(0.3F);
		setSoundType(SoundType.GLASS);
		setLightLevel(1.0F);
		setDefaultState(blockState.getBaseState()
				.withProperty(BotaniaStateProps.POWERED, false)
				.withProperty(BotaniaStateProps.HAS_LENS, false)
		);
	}

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess world, BlockPos pos) {
		return AABB;
	}

	@Override
	public BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, BotaniaStateProps.POWERED, BotaniaStateProps.HAS_LENS);
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return (state.getValue(BotaniaStateProps.POWERED) ? 8 : 0)
				+ (state.getValue(BotaniaStateProps.HAS_LENS) ? 1 : 0);
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		return getDefaultState().withProperty(BotaniaStateProps.POWERED, (meta & 8) > 0)
				.withProperty(BotaniaStateProps.HAS_LENS, (meta & 1) > 0);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public BlockRenderLayer getBlockLayer() {
		return BlockRenderLayer.TRANSLUCENT;
	}

	@Override
	public AxisAlignedBB getCollisionBoundingBox(IBlockState state, World world, BlockPos pos) {
		return null;
	}

	@Override
	public boolean isOpaqueCube(IBlockState state) {
		return false;
	}

	@Override
	public boolean isFullCube(IBlockState state) {
		return false;
	}

	@Override
	public boolean onBlockActivated(World par1World, BlockPos pos, IBlockState state, EntityPlayer par5EntityPlayer, EnumHand hand, ItemStack heldItem, EnumFacing side, float par7, float par8, float par9) {
		TileEntity tile = par1World.getTileEntity(pos);
		if(!(tile instanceof TilePrism))
			return false;

		TilePrism prism = (TilePrism) tile;
		ItemStack lens = prism.getItemHandler().getStackInSlot(0);
		boolean isHeldItemLens = heldItem != null && heldItem.getItem() instanceof ILens;

		if(lens == null && isHeldItemLens) {
			if(!par5EntityPlayer.capabilities.isCreativeMode)
				par5EntityPlayer.inventory.setInventorySlotContents(par5EntityPlayer.inventory.currentItem, null);

			prism.getItemHandler().setStackInSlot(0, heldItem.copy());
			prism.markDirty();
			par1World.setBlockState(pos, state.withProperty(BotaniaStateProps.HAS_LENS, true), 1 | 2);
		} else if(lens != null) {
			ItemStack add = lens.copy();
			if(!par5EntityPlayer.inventory.addItemStackToInventory(add))
				par5EntityPlayer.dropPlayerItemWithRandomChoice(add, false);
			prism.getItemHandler().setStackInSlot(0, null);
			prism.markDirty();
			par1World.setBlockState(pos, state.withProperty(BotaniaStateProps.HAS_LENS, false), 1 | 2);
		}

		return true;
	}

	@Override
	public void onNeighborBlockChange(World world, BlockPos pos, IBlockState state, Block block) {
		boolean power = world.isBlockIndirectlyGettingPowered(pos) > 0 || world.isBlockIndirectlyGettingPowered(pos.up()) > 0;
		boolean powered = state.getValue(BotaniaStateProps.POWERED);

		if(!world.isRemote) {
			if(power && !powered)
				world.setBlockState(pos, state.withProperty(BotaniaStateProps.POWERED, true), 1 | 2);
			else if(!power && powered)
				world.setBlockState(pos, state.withProperty(BotaniaStateProps.POWERED, false), 1 | 2);
		}
	}

	@Override
	public void breakBlock(World par1World, BlockPos pos, IBlockState state) {
		TileEntity tile = par1World.getTileEntity(pos);
		if(!(tile instanceof TileSimpleInventory))
			return;

		TileSimpleInventory inv = (TileSimpleInventory) tile;

		if (inv != null) {
			for (int j1 = 0; j1 < inv.getSizeInventory(); ++j1) {
				ItemStack itemstack = inv.getItemHandler().getStackInSlot(j1);

				if (itemstack != null) {
					float f = random.nextFloat() * 0.8F + 0.1F;
					float f1 = random.nextFloat() * 0.8F + 0.1F;
					EntityItem entityitem;

					for (float f2 = random.nextFloat() * 0.8F + 0.1F; itemstack.stackSize > 0; par1World.spawnEntityInWorld(entityitem)) {
						int k1 = random.nextInt(21) + 10;

						if (k1 > itemstack.stackSize)
							k1 = itemstack.stackSize;

						itemstack.stackSize -= k1;
						entityitem = new EntityItem(par1World, pos.getX() + f, pos.getY() + f1, pos.getZ() + f2, new ItemStack(itemstack.getItem(), k1, itemstack.getItemDamage()));
						float f3 = 0.05F;
						entityitem.motionX = (float)random.nextGaussian() * f3;
						entityitem.motionY = (float)random.nextGaussian() * f3 + 0.2F;
						entityitem.motionZ = (float)random.nextGaussian() * f3;

						if (itemstack.hasTagCompound())
							entityitem.getEntityItem().setTagCompound((NBTTagCompound)itemstack.getTagCompound().copy());
					}
				}
			}

			par1World.updateComparatorOutputLevel(pos, state.getBlock());
		}

		super.breakBlock(par1World, pos, state);
	}

	@Override
	public boolean hasTileEntity(IBlockState state) {
		return true;
	}

	@Override
	public TileEntity createTileEntity(World world, IBlockState state) {
		return new TilePrism();
	}

	@Override
	public void onBurstCollision(IManaBurst burst, World world, BlockPos pos) {
		TileEntity tile = world.getTileEntity(pos);
		if(tile != null && tile instanceof TilePrism)
			((TilePrism) tile).onBurstCollision(burst);
	}

	@Override
	public LexiconEntry getEntry(World world, BlockPos pos, EntityPlayer player, ItemStack lexicon) {
		return LexiconData.prism;
	}

}
