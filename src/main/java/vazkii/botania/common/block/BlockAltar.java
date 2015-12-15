/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [Jan 21, 2014, 7:48:54 PM (GMT)]
 */
package vazkii.botania.common.block;

import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidContainerItem;
import vazkii.botania.api.internal.VanillaPacketDispatcher;
import vazkii.botania.api.lexicon.ILexiconable;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.api.mana.ManaItemHandler;
import vazkii.botania.client.lib.LibRenderIDs;
import vazkii.botania.common.Botania;
import vazkii.botania.common.block.tile.TileAltar;
import vazkii.botania.common.block.tile.TileSimpleInventory;
import vazkii.botania.common.item.ModItems;
import vazkii.botania.common.item.block.ItemBlockWithMetadataAndName;
import vazkii.botania.common.item.rod.ItemWaterRod;
import vazkii.botania.common.lexicon.LexiconData;
import vazkii.botania.common.lib.LibBlockNames;
import cpw.mods.fml.common.registry.GameRegistry;

public class BlockAltar extends BlockModContainer implements ILexiconable {

	Random random;

	protected BlockAltar() {
		super(Material.rock);
		setHardness(3.5F);
		setStepSound(soundTypeStone);
		setBlockName(LibBlockNames.ALTAR);

		float f = 1F / 16F * 2F;
		setBlockBounds(f, f, f, 1F - f, 1F / 16F * 20F, 1F - f);

		random = new Random();
	}

	@Override
	public void registerBlockIcons(IIconRegister par1IconRegister) {
		// NO-OP
	}

	@Override
	protected boolean shouldRegisterInNameSet() {
		return false;
	}

	@Override
	public Block setBlockName(String par1Str) {
		GameRegistry.registerBlock(this, ItemBlockWithMetadataAndName.class, par1Str);
		return super.setBlockName(par1Str);
	}

	@Override
	public void getSubBlocks(Item item, CreativeTabs tab, List list) {
		for(int i = 0; i < 9; i++)
			list.add(new ItemStack(item, 1, i));
	}

	@Override
	public void onEntityCollidedWithBlock(World par1World, int par2, int par3, int par4, Entity par5Entity) {
		if(par5Entity instanceof EntityItem) {
			TileAltar tile = (TileAltar) par1World.getTileEntity(par2, par3, par4);
			if(tile.collideEntityItem((EntityItem) par5Entity))
				VanillaPacketDispatcher.dispatchTEToNearbyPlayers(tile);
		}
	}

	@Override
	public int getLightValue(IBlockAccess world, int x, int y, int z) {
		TileAltar tile = (TileAltar) world.getTileEntity(x, y, z);
		return tile.hasLava ? 15 : 0;
	}

	@Override
	public boolean onBlockActivated(World par1World, int par2, int par3, int par4, EntityPlayer par5EntityPlayer, int par6, float par7, float par8, float par9) {
		ItemStack stack = par5EntityPlayer.getCurrentEquippedItem();
		TileAltar tile = (TileAltar) par1World.getTileEntity(par2, par3, par4);

		if(par5EntityPlayer.isSneaking()) {
			for(int i = tile.getSizeInventory() - 1; i >= 0; i--) {
				ItemStack stackAt = tile.getStackInSlot(i);
				if(stackAt != null) {
					ItemStack copy = stackAt.copy();
					if(!par5EntityPlayer.inventory.addItemStackToInventory(copy))
						par5EntityPlayer.dropPlayerItemWithRandomChoice(copy, false);
					tile.setInventorySlotContents(i, null);
					par1World.func_147453_f(par2, par3, par4, this);
					break;
				}
			}
		} else if(tile.isEmpty() && tile.hasWater && stack == null)
			tile.trySetLastRecipe(par5EntityPlayer);
		else {
			if(stack != null && (isValidWaterContainer(stack) || stack.getItem() == ModItems.waterRod && ManaItemHandler.requestManaExact(stack, par5EntityPlayer, ItemWaterRod.COST, false))) {
				if(!tile.hasWater) {
					if(stack.getItem() == ModItems.waterRod)
						ManaItemHandler.requestManaExact(stack, par5EntityPlayer, ItemWaterRod.COST, true);
					else if(!par5EntityPlayer.capabilities.isCreativeMode)
						par5EntityPlayer.inventory.setInventorySlotContents(par5EntityPlayer.inventory.currentItem, getContainer(stack));

					tile.setWater(true);
					par1World.func_147453_f(par2, par3, par4, this);
				}

				return true;
			} else if(stack != null && stack.getItem() == Items.lava_bucket) {
				if(!par5EntityPlayer.capabilities.isCreativeMode)
					par5EntityPlayer.inventory.setInventorySlotContents(par5EntityPlayer.inventory.currentItem, getContainer(stack));

				tile.setLava(true);
				tile.setWater(false);
				par1World.func_147453_f(par2, par3, par4, this);

				return true;
			} else if(stack != null && stack.getItem() == Items.bucket && (tile.hasWater || tile.hasLava) && !Botania.gardenOfGlassLoaded) {
				ItemStack bucket = tile.hasLava ? new ItemStack(Items.lava_bucket) : new ItemStack(Items.water_bucket);
				if(stack.stackSize == 1)
					par5EntityPlayer.inventory.setInventorySlotContents(par5EntityPlayer.inventory.currentItem, bucket);
				else {
					if(!par5EntityPlayer.inventory.addItemStackToInventory(bucket))
						par5EntityPlayer.dropPlayerItemWithRandomChoice(bucket, false);
					stack.stackSize--;
				}

				if(tile.hasLava)
					tile.setLava(false);
				else tile.setWater(false);
				par1World.func_147453_f(par2, par3, par4, this);

				return true;
			}
		}

		return false;
	}

	@Override
	public void fillWithRain(World world, int x, int y, int z) {
		if(world.rand.nextInt(20) == 1) {
			TileEntity tile = world.getTileEntity(x, y, z);
			if(tile instanceof TileAltar) {
				TileAltar altar = (TileAltar) tile;
				if(!altar.hasLava && !altar.hasWater)
					altar.setWater(true);
				world.func_147453_f(x, y, z, this);
			}
		}
	}

	@Override
	public int damageDropped(int meta) {
		return meta;
	}

	private boolean isValidWaterContainer(ItemStack stack) {
		if(stack == null || stack.stackSize != 1)
			return false;
		if(stack.getItem() == ModItems.waterBowl)
			return true;

		if(stack.getItem() instanceof IFluidContainerItem) {
			FluidStack fluidStack = ((IFluidContainerItem) stack.getItem()).getFluid(stack);
			return fluidStack != null && fluidStack.getFluid() == FluidRegistry.WATER && fluidStack.amount >= FluidContainerRegistry.BUCKET_VOLUME;
		}
		FluidStack fluidStack = FluidContainerRegistry.getFluidForFilledItem(stack);
		return fluidStack != null && fluidStack.getFluid() == FluidRegistry.WATER && fluidStack.amount >= FluidContainerRegistry.BUCKET_VOLUME;
	}

	private ItemStack getContainer(ItemStack stack) {
		if(stack.getItem() == ModItems.waterBowl)
			return new ItemStack(Items.bowl);

		if (stack.getItem().hasContainerItem(stack))
			return stack.getItem().getContainerItem(stack);
		else if (stack.getItem() instanceof IFluidContainerItem) {
			((IFluidContainerItem) stack.getItem()).drain(stack, FluidContainerRegistry.BUCKET_VOLUME, true);
			return stack;
		}
		return FluidContainerRegistry.drainFluidContainer(stack);
	}

	@Override
	public IIcon getIcon(int par1, int par2) {
		return par2 == 0 ? Blocks.cobblestone.getIcon(par1, par2) : ModFluffBlocks.biomeStoneA.getIcon(par1, par2 + 7);
	}

	@Override
	public boolean isOpaqueCube() {
		return false;
	}

	@Override
	public boolean renderAsNormalBlock() {
		return false;
	}

	@Override
	public int getRenderType() {
		return LibRenderIDs.idAltar;
	}

	@Override
	public TileEntity createNewTileEntity(World world, int meta) {
		return new TileAltar();
	}

	@Override
	public void breakBlock(World par1World, int par2, int par3, int par4, Block par5, int par6) {
		TileSimpleInventory inv = (TileSimpleInventory) par1World.getTileEntity(par2, par3, par4);

		if (inv != null) {
			for (int j1 = 0; j1 < inv.getSizeInventory(); ++j1) {
				ItemStack itemstack = inv.getStackInSlot(j1);

				if (itemstack != null) {
					float f = random.nextFloat() * 0.8F + 0.1F;
					float f1 = random.nextFloat() * 0.8F + 0.1F;
					EntityItem entityitem;

					for (float f2 = random.nextFloat() * 0.8F + 0.1F; itemstack.stackSize > 0; par1World.spawnEntityInWorld(entityitem)) {
						int k1 = random.nextInt(21) + 10;

						if (k1 > itemstack.stackSize)
							k1 = itemstack.stackSize;

						itemstack.stackSize -= k1;
						entityitem = new EntityItem(par1World, par2 + f, par3 + f1, par4 + f2, new ItemStack(itemstack.getItem(), k1, itemstack.getItemDamage()));
						float f3 = 0.05F;
						entityitem.motionX = (float)random.nextGaussian() * f3;
						entityitem.motionY = (float)random.nextGaussian() * f3 + 0.2F;
						entityitem.motionZ = (float)random.nextGaussian() * f3;

						if (itemstack.hasTagCompound())
							entityitem.getEntityItem().setTagCompound((NBTTagCompound)itemstack.getTagCompound().copy());
					}
				}
			}

			par1World.func_147453_f(par2, par3, par4, par5);
		}

		super.breakBlock(par1World, par2, par3, par4, par5, par6);
	}

	@Override
	public boolean hasComparatorInputOverride() {
		return true;
	}

	@Override
	public int getComparatorInputOverride(World par1World, int par2, int par3, int par4, int par5) {
		TileAltar altar = (TileAltar) par1World.getTileEntity(par2, par3, par4);
		return altar.hasWater ? 15 : 0;
	}

	@Override
	public LexiconEntry getEntry(World world, int x, int y, int z, EntityPlayer player, ItemStack lexicon) {
		return LexiconData.apothecary;
	}

}
