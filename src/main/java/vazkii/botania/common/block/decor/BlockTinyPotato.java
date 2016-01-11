/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [Jul 18, 2014, 7:58:08 PM (GMT)]
 */
package vazkii.botania.common.block.decor;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import vazkii.botania.api.lexicon.ILexiconable;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.api.state.BotaniaStateProps;
import vazkii.botania.api.state.enums.PotatoVariant;
import vazkii.botania.client.core.proxy.ClientProxy;
import vazkii.botania.common.achievement.ModAchievements;
import vazkii.botania.common.block.BlockModContainer;
import vazkii.botania.common.block.tile.TileTinyPotato;
import vazkii.botania.common.item.block.ItemBlockTinyPotato;
import vazkii.botania.common.lexicon.LexiconData;
import vazkii.botania.common.lib.LibBlockNames;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class BlockTinyPotato extends BlockModContainer implements ILexiconable {

	public BlockTinyPotato() {
		super(Material.cloth);
		setHardness(0.25F);
		setUnlocalizedName(LibBlockNames.TINY_POTATO);
		float f = 1F / 16F * 6F;
		setBlockBounds(f, 0, f, 1F - f, f, 1F - f);
		setDefaultState(blockState.getBaseState()
				.withProperty(BotaniaStateProps.CARDINALS, EnumFacing.SOUTH)
				.withProperty(BotaniaStateProps.POTATO_VARIANT, PotatoVariant.DEFAULT)
		);
	}

	@Override
	public BlockState createBlockState() {
		return new BlockState(this, BotaniaStateProps.CARDINALS, BotaniaStateProps.POTATO_VARIANT);
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		switch (state.getValue(BotaniaStateProps.CARDINALS)) {
			case NORTH: return 0;
			case WEST: return 3;
			case EAST: return 1;
			case SOUTH:
			default:
				return 2; // wai vazkii senpai
		}
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		EnumFacing side = null;
		switch (meta) {
			case 3: side = EnumFacing.WEST; break;
			case 0: side = EnumFacing.NORTH; break;
			case 1: side = EnumFacing.EAST; break;
			case 2:
			default:
				side = EnumFacing.SOUTH; break;
		}
		return getDefaultState().withProperty(BotaniaStateProps.CARDINALS, side);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IBlockState getActualState(IBlockState state, IBlockAccess world, BlockPos pos) {
		if (ClientProxy.dootDoot) {
			return state.withProperty(BotaniaStateProps.POTATO_VARIANT, PotatoVariant.HALLOWEEN);
		} else if (world.getTileEntity(pos) instanceof TileTinyPotato) {
			if (((TileTinyPotato) world.getTileEntity(pos)).name.toLowerCase().equals("kyle hyde")) {
				return state.withProperty(BotaniaStateProps.POTATO_VARIANT, PotatoVariant.GRAYSCALE);
			}
		}
		return state.withProperty(BotaniaStateProps.POTATO_VARIANT, PotatoVariant.DEFAULT);
	}

	@Override
	public Block setUnlocalizedName(String par1Str) {
		GameRegistry.registerBlock(this, ItemBlockTinyPotato.class, par1Str);
		return super.setUnlocalizedName(par1Str);
	}

	@Override
	protected boolean shouldRegisterInNameSet() {
		return false;
	}

	@Override
	public boolean onBlockActivated(World par1World, BlockPos pos, IBlockState state, EntityPlayer par5EntityPlayer, EnumFacing par6, float par7, float par8, float par9) {
		TileEntity tile = par1World.getTileEntity(pos);
		if(tile instanceof TileTinyPotato) {
			((TileTinyPotato) tile).interact();
			par5EntityPlayer.addStat(ModAchievements.tinyPotatoPet, 1);
			par1World.spawnParticle(EnumParticleTypes.HEART, pos.getX() + minX + Math.random() * (maxX - minX), pos.getY() + maxY, pos.getZ() + minZ + Math.random() * (maxZ - minZ), 0, 0 ,0);
		}
		return true;
	}

	@Override
	public void onBlockPlacedBy(World par1World, BlockPos pos, IBlockState state, EntityLivingBase par5EntityLiving, ItemStack par6ItemStack) {
		par1World.setBlockState(pos, state.withProperty(BotaniaStateProps.CARDINALS, par5EntityLiving.getHorizontalFacing().getOpposite()));
		if (par6ItemStack.hasDisplayName())
			((TileTinyPotato) par1World.getTileEntity(pos)).name = par6ItemStack.getDisplayName();
	}

	@Override
	public void onBlockHarvested(World par1World, BlockPos pos, IBlockState state, EntityPlayer par6EntityPlayer) {
		if(!par6EntityPlayer.capabilities.isCreativeMode)
			dropBlockAsItem(par1World, pos, state, 0);
	}

	@Override
	public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
		ArrayList<ItemStack> list = new ArrayList();
		TileEntity tile = world.getTileEntity(pos);

		if(tile != null) {
			ItemStack stack = new ItemStack(this);
			String name = ((TileTinyPotato) tile).name;
			if(!name.isEmpty())
				stack.setStackDisplayName(name);
			list.add(stack);
		}

		return list;
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
	public TileEntity createNewTileEntity(World world, int meta) {
		return new TileTinyPotato();
	}

	@Override
	public LexiconEntry getEntry(World world, BlockPos pos, EntityPlayer player, ItemStack lexicon) {
		return LexiconData.tinyPotato;
	}
}
