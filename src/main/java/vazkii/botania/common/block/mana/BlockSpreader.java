/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [Jan 25, 2014, 9:38:23 PM (GMT)]
 */
package vazkii.botania.common.block.mana;

import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockPistonBase;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import vazkii.botania.api.lexicon.ILexiconable;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.api.mana.ILens;
import vazkii.botania.api.state.BotaniaStateProps;
import vazkii.botania.api.state.enums.SpreaderVariant;
import vazkii.botania.api.wand.IWandHUD;
import vazkii.botania.api.wand.IWandable;
import vazkii.botania.api.wand.IWireframeAABBProvider;
import vazkii.botania.client.lib.LibRenderIDs;
import vazkii.botania.common.block.BlockModContainer;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.block.tile.mana.TileSpreader;
import vazkii.botania.common.item.ModItems;
import vazkii.botania.common.item.block.ItemBlockWithMetadataAndName;
import vazkii.botania.common.lexicon.LexiconData;
import vazkii.botania.common.lib.LibBlockNames;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class BlockSpreader extends BlockModContainer implements IWandable, IWandHUD, ILexiconable, IWireframeAABBProvider {

	Random random;

	public BlockSpreader() {
		super(Material.wood);
		setHardness(2.0F);
		setStepSound(soundTypeWood);
		setUnlocalizedName(LibBlockNames.SPREADER);
		setDefaultState(blockState.getBaseState().withProperty(BotaniaStateProps.SPREADER_VARIANT, SpreaderVariant.MANA));
		random = new Random();
	}

	@Override
	public BlockState createBlockState() {
		return new BlockState(this, BotaniaStateProps.SPREADER_VARIANT);
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return state.getValue(BotaniaStateProps.SPREADER_VARIANT).ordinal();
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		if (meta > SpreaderVariant.values().length) {
			meta = 0;
		}
		return getDefaultState().withProperty(BotaniaStateProps.SPREADER_VARIANT, SpreaderVariant.values()[meta]);
	}

	@Override
	protected boolean shouldRegisterInNameSet() {
		return false;
	}

	@Override
	public Block setUnlocalizedName(String par1Str) {
		GameRegistry.registerBlock(this, ItemBlockWithMetadataAndName.class, par1Str);
		return super.setUnlocalizedName(par1Str);
	}

	@Override
	public void getSubBlocks(Item par1, CreativeTabs par2, List par3) {
		for(int i = 0; i < 4; i++)
			par3.add(new ItemStack(par1, 1, i));
	}

	@Override
	public void onBlockPlacedBy(World par1World, BlockPos pos, IBlockState state, EntityLivingBase par5EntityLivingBase, ItemStack par6ItemStack) {
		EnumFacing orientation = BlockPistonBase.getFacingFromEntity(par1World, pos, par5EntityLivingBase);
		TileSpreader spreader = (TileSpreader) par1World.getTileEntity(pos);
		par1World.setBlockState(pos, getStateFromMeta(par6ItemStack.getItemDamage()), 1 | 2);

		switch(orientation) {
		case DOWN:
			spreader.rotationY = -90F;
			break;
		case UP:
			spreader.rotationY = 90F;
			break;
		case NORTH:
			spreader.rotationX = 270F;
			break;
		case SOUTH:
			spreader.rotationX = 90F;
			break;
		case WEST:
			break;
		default:
			spreader.rotationX = 180F;
			break;
		}
	}

	@Override
	public int damageDropped(IBlockState par1) {
		return getMetaFromState(par1);
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
	public int getRenderType() {
		return 2;
	}

	@Override
	public boolean onBlockActivated(World par1World, BlockPos pos, IBlockState state, EntityPlayer par5EntityPlayer, EnumFacing par6, float par7, float par8, float par9) {
		TileEntity tile = par1World.getTileEntity(pos);
		if(!(tile instanceof TileSpreader))
			return false;

		TileSpreader spreader = (TileSpreader) tile;
		ItemStack lens = spreader.getStackInSlot(0);
		ItemStack heldItem = par5EntityPlayer.getCurrentEquippedItem();
		boolean isHeldItemLens = heldItem != null && heldItem.getItem() instanceof ILens;
		boolean wool = heldItem != null && heldItem.getItem() == Item.getItemFromBlock(Blocks.wool);

		if(heldItem != null)
			if(heldItem.getItem() == ModItems.twigWand)
				return false;

		if(lens == null && isHeldItemLens) {
			if (!par5EntityPlayer.capabilities.isCreativeMode)
				par5EntityPlayer.inventory.setInventorySlotContents(par5EntityPlayer.inventory.currentItem, null);

			spreader.setInventorySlotContents(0, heldItem.copy());
			spreader.markDirty();
		} else if(lens != null && !wool) {
			ItemStack add = lens.copy();
			if(!par5EntityPlayer.inventory.addItemStackToInventory(add))
				par5EntityPlayer.dropPlayerItemWithRandomChoice(add, false);
			spreader.setInventorySlotContents(0, null);
			spreader.markDirty();
		}

		if(wool && spreader.paddingColor == -1) {
			spreader.paddingColor = heldItem.getItemDamage();
			heldItem.stackSize--;
			if(heldItem.stackSize == 0)
				par5EntityPlayer.inventory.setInventorySlotContents(par5EntityPlayer.inventory.currentItem, null);
		} else if(heldItem == null && spreader.paddingColor != -1 && lens == null) {
			ItemStack pad = new ItemStack(Blocks.wool, 1, spreader.paddingColor);
			if(!par5EntityPlayer.inventory.addItemStackToInventory(pad))
				par5EntityPlayer.dropPlayerItemWithRandomChoice(pad, false);
			spreader.paddingColor = -1;
			spreader.markDirty();
		}

		return true;
	}

	@Override
	public void breakBlock(World par1World, BlockPos pos, IBlockState state) {
		TileEntity tile = par1World.getTileEntity(pos);
		if(!(tile instanceof TileSpreader))
			return;

		TileSpreader inv = (TileSpreader) tile;

		if (inv != null) {
			for (int j1 = 0; j1 < inv.getSizeInventory() + 1; ++j1) {
				ItemStack itemstack = j1 >= inv.getSizeInventory() ? inv.paddingColor == -1 ? null : new ItemStack(Blocks.wool, 1, inv.paddingColor) : inv.getStackInSlot(j1);

				if(itemstack != null) {
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
	public boolean onUsedByWand(EntityPlayer player, ItemStack stack, World world, BlockPos pos, EnumFacing side) {
		((TileSpreader) world.getTileEntity(pos)).onWanded(player, stack);
		return true;
	}

	@Override
	public TileEntity createNewTileEntity(World world, int meta) {
		return new TileSpreader();
	}

	@Override
	public void renderHUD(Minecraft mc, ScaledResolution res, World world, BlockPos pos) {
		((TileSpreader) world.getTileEntity(pos)).renderHUD(mc, res);
	}

	@Override
	public LexiconEntry getEntry(World world, BlockPos pos, EntityPlayer player, ItemStack lexicon) {
		SpreaderVariant variant = world.getBlockState(pos).getValue(BotaniaStateProps.SPREADER_VARIANT);
		return variant == SpreaderVariant.MANA ? LexiconData.spreader : variant == SpreaderVariant.REDSTONE ? LexiconData.redstoneSpreader : LexiconData.dreamwoodSpreader;
	}

	@Override
	public AxisAlignedBB getWireframeAABB(World world, BlockPos pos) {
		float f = 1F / 16F;
		return new AxisAlignedBB(pos.getX() + f, pos.getY() + f, pos.getZ() + f,
				pos.getX() + 1 - f, pos.getY() + 1 - f, pos.getZ() + 1 - f);
	}

}
