/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [May 4, 2014, 12:29:56 PM (GMT)]
 */
package vazkii.botania.common.block;

import java.util.List;
import java.util.Random;

import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import vazkii.botania.api.lexicon.ILexiconable;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.api.state.BotaniaStateProps;
import vazkii.botania.api.state.enums.CratePattern;
import vazkii.botania.api.state.enums.CrateVariant;
import vazkii.botania.api.wand.IWandHUD;
import vazkii.botania.api.wand.IWandable;
import vazkii.botania.client.core.helper.IconHelper;
import vazkii.botania.common.block.tile.TileCraftCrate;
import vazkii.botania.common.block.tile.TileOpenCrate;
import vazkii.botania.common.block.tile.TileSimpleInventory;
import vazkii.botania.common.item.block.ItemBlockWithMetadataAndName;
import vazkii.botania.common.lexicon.LexiconData;
import vazkii.botania.common.lib.LibBlockNames;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class BlockOpenCrate extends BlockModContainer implements ILexiconable, IWandable, IWandHUD {

	Random random;

	public BlockOpenCrate() {
		super(Material.wood);
		setHardness(2.0F);
		setStepSound(soundTypeWood);
		setUnlocalizedName(LibBlockNames.OPEN_CRATE);
		setDefaultState(blockState.getBaseState()
				.withProperty(BotaniaStateProps.CRATE_VARIANT, CrateVariant.OPEN)
				.withProperty(BotaniaStateProps.CRATE_PATTERN, CratePattern.NONE));
		random = new Random();
	}

	@Override
	public BlockState createBlockState() {
		return new BlockState(this, BotaniaStateProps.CRATE_VARIANT, BotaniaStateProps.CRATE_PATTERN);
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return state.getValue(BotaniaStateProps.CRATE_VARIANT).ordinal();
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		if (meta >= CrateVariant.values().length) {
			meta = 0;
		}
		return getDefaultState().withProperty(BotaniaStateProps.CRATE_VARIANT, CrateVariant.values()[meta]);
	}

	@Override
	public IBlockState getActualState(IBlockState state, IBlockAccess world, BlockPos pos) {
		if (world.getTileEntity(pos) instanceof TileCraftCrate) {
			TileCraftCrate tile = ((TileCraftCrate) world.getTileEntity(pos));
			state = state.withProperty(BotaniaStateProps.CRATE_PATTERN, CratePattern.values()[tile.pattern + 1]);
		} else {
			state = state.withProperty(BotaniaStateProps.CRATE_PATTERN, CratePattern.NONE);
		}

		return state;
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
	public void getSubBlocks(Item par1, CreativeTabs par2CreativeTabs, List par3List) {
		for(int i = 0; i < CrateVariant.values().length; i++)
			par3List.add(new ItemStack(par1, 1, i));
	}

	@Override
	public int damageDropped(IBlockState state) {
		return getMetaFromState(state);
	}


	@Override
	public boolean hasComparatorInputOverride() {
		return true;
	}

	@Override
	public int getComparatorInputOverride(World par1World, BlockPos pos) {
		TileOpenCrate crate = (TileOpenCrate) par1World.getTileEntity(pos);
		return crate.getSignal();
	}

	@Override
	public void breakBlock(World par1World, BlockPos pos, IBlockState state) {
		TileSimpleInventory inv = (TileSimpleInventory) par1World.getTileEntity(pos);

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
	public TileEntity createNewTileEntity(World world, int meta) {
		return meta == 0 ? new TileOpenCrate() : new TileCraftCrate();
	}

	@Override
	public LexiconEntry getEntry(World world, BlockPos pos, EntityPlayer player, ItemStack lexicon) {
		return world.getBlockState(pos).getValue(BotaniaStateProps.CRATE_VARIANT) == CrateVariant.OPEN ? LexiconData.openCrate : LexiconData.craftCrate;
	}

	@Override
	public boolean onUsedByWand(EntityPlayer player, ItemStack stack, World world, BlockPos pos, EnumFacing side) {
		TileOpenCrate crate = (TileOpenCrate) world.getTileEntity(pos);
		return crate.onWanded(player, stack);
	}

	@Override
	public void renderHUD(Minecraft mc, ScaledResolution res, World world, BlockPos pos) {
		TileEntity tile = world.getTileEntity(pos);
		if(tile instanceof TileCraftCrate) {
			TileCraftCrate craft = (TileCraftCrate) tile;

			int width = 52;
			int height = 52;
			int xc = res.getScaledWidth() / 2 + 20;
			int yc = res.getScaledHeight() / 2 - height / 2;

			Gui.drawRect(xc - 6, yc - 6, xc + width + 6, yc + height + 6, 0x22000000);
			Gui.drawRect(xc - 4, yc - 4, xc + width + 4, yc + height + 4, 0x22000000);

			for(int i = 0; i < 3; i++)
				for(int j = 0; j < 3; j++) {
					int index = i * 3 + j;
					int xp = xc + j * 18;
					int yp = yc + i * 18;

					boolean enabled = true;
					if(craft.pattern > -1)
						enabled = TileCraftCrate.PATTERNS[craft.pattern][index];

					Gui.drawRect(xp, yp, xp + 16, yp + 16, enabled ? 0x22FFFFFF : 0x22FF0000);

					ItemStack item = craft.getStackInSlot(index);
					net.minecraft.client.renderer.RenderHelper.enableGUIStandardItemLighting();
					GlStateManager.enableRescaleNormal();
					mc.getRenderItem().renderItemAndEffectIntoGUI(item, xp, yp);
					net.minecraft.client.renderer.RenderHelper.disableStandardItemLighting();
				}
		}
	}

}
