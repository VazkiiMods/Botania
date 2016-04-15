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

import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;
import vazkii.botania.api.lexicon.ILexiconable;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.api.state.BotaniaStateProps;
import vazkii.botania.common.achievement.ModAchievements;
import vazkii.botania.common.block.BlockMod;
import vazkii.botania.common.block.tile.TileTinyPotato;
import vazkii.botania.common.item.block.ItemBlockTinyPotato;
import vazkii.botania.common.lexicon.LexiconData;
import vazkii.botania.common.lib.LibBlockNames;

import java.util.ArrayList;
import java.util.List;

public class BlockTinyPotato extends BlockMod implements ILexiconable {

	private static final AxisAlignedBB AABB = new AxisAlignedBB(0.375, 0, 0.375, 0.625, 0.375, 0.625);

	public BlockTinyPotato() {
		super(Material.cloth, LibBlockNames.TINY_POTATO);
		setHardness(0.25F);
		setDefaultState(blockState.getBaseState()
				.withProperty(BotaniaStateProps.CARDINALS, EnumFacing.SOUTH)
		);
	}

	@Override
	public BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, BotaniaStateProps.CARDINALS);
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
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess world, BlockPos pos) {
		return AABB;
	}

	@Override
	public void registerItemForm() {
		GameRegistry.register(new ItemBlockTinyPotato(this), getRegistryName());
	}
	@Override
	public boolean onBlockActivated(World par1World, BlockPos pos, IBlockState state, EntityPlayer par5EntityPlayer, EnumHand hand, ItemStack stack, EnumFacing par6, float par7, float par8, float par9) {
		TileEntity tile = par1World.getTileEntity(pos);
		if(tile instanceof TileTinyPotato) {
			((TileTinyPotato) tile).interact();
			par5EntityPlayer.addStat(ModAchievements.tinyPotatoPet, 1);
			par1World.spawnParticle(EnumParticleTypes.HEART, pos.getX() + AABB.minX + Math.random() * (AABB.maxX - AABB.minX), pos.getY() + AABB.maxY, pos.getZ() + AABB.minZ + Math.random() * (AABB.maxZ - AABB.minZ), 0, 0 ,0);
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
		ArrayList<ItemStack> list = new ArrayList<>();
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
	public boolean isOpaqueCube(IBlockState state) {
		return false;
	}

	@Override
	public boolean isFullCube(IBlockState state) {
		return false;
	}

	@Override
	public EnumBlockRenderType getRenderType(IBlockState state) {
		return EnumBlockRenderType.ENTITYBLOCK_ANIMATED;
	}

	@Override
	public boolean hasTileEntity(IBlockState state) {
		return true;
	}

	@Override
	public TileEntity createTileEntity(World world, IBlockState state) {
		return new TileTinyPotato();
	}

	@Override
	public LexiconEntry getEntry(World world, BlockPos pos, EntityPlayer player, ItemStack lexicon) {
		return LexiconData.tinyPotato;
	}
}
