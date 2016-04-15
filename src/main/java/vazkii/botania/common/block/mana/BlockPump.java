/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [Mar 17, 2015, 9:46:53 PM (GMT)]
 */
package vazkii.botania.common.block.mana;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.property.ExtendedBlockState;
import net.minecraftforge.common.property.IUnlistedProperty;
import net.minecraftforge.common.property.Properties;
import vazkii.botania.api.lexicon.ILexiconable;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.api.state.BotaniaStateProps;
import vazkii.botania.common.block.BlockMod;
import vazkii.botania.common.block.tile.mana.TilePump;
import vazkii.botania.common.lexicon.LexiconData;
import vazkii.botania.common.lib.LibBlockNames;

public class BlockPump extends BlockMod implements ILexiconable {

	private static final AxisAlignedBB X_AABB = new AxisAlignedBB(0, 0, 0.25, 1, 0.5, 0.75);
	private static final AxisAlignedBB Z_AABB = new AxisAlignedBB(0.25, 0, 0, 0.75, 0.5, 1);

	public BlockPump() {
		super(Material.rock, LibBlockNames.PUMP);
		setHardness(2.0F);
		setResistance(10.0F);
		setSoundType(SoundType.STONE);
		setDefaultState(blockState.getBaseState().withProperty(Properties.StaticProperty, true).withProperty(BotaniaStateProps.CARDINALS, EnumFacing.SOUTH));
	}

	@Override
	public BlockStateContainer createBlockState() {
		return new ExtendedBlockState(this, new IProperty[] { BotaniaStateProps.CARDINALS, Properties.StaticProperty }, new IUnlistedProperty[] { Properties.AnimationProperty });
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return state.getValue(BotaniaStateProps.CARDINALS).getIndex();
	}

	@Override
	public IBlockState getActualState(IBlockState state, IBlockAccess world, BlockPos pos) {
		return state.withProperty(Properties.StaticProperty, true);
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		if (meta < 2 || meta > 5) {
			meta = 2;
		}
		return getDefaultState().withProperty(BotaniaStateProps.CARDINALS, EnumFacing.getFront(meta));
	}

	@Override
	public void onBlockPlacedBy(World p_149689_1_, BlockPos pos, IBlockState state, EntityLivingBase p_149689_5_, ItemStack p_149689_6_) {
		p_149689_1_.setBlockState(pos, state.withProperty(BotaniaStateProps.CARDINALS, p_149689_5_.getHorizontalFacing().getOpposite()), 2);
	}

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess world, BlockPos pos) {
		if(state.getValue(BotaniaStateProps.CARDINALS).getAxis() == EnumFacing.Axis.X) {
			return X_AABB;
		} else {
			return Z_AABB;
		}
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
	public boolean hasComparatorInputOverride(IBlockState state) {
		return true;
	}

	@Override
	public int getComparatorInputOverride(IBlockState state, World world, BlockPos pos) {
		return ((TilePump) world.getTileEntity(pos)).comparator;
	}

	@Override
	public LexiconEntry getEntry(World world, BlockPos pos, EntityPlayer player, ItemStack lexicon) {
		return LexiconData.poolCart;
	}


	@Override
	public boolean hasTileEntity(IBlockState state) {
		return true;
	}

	@Override
	public TileEntity createTileEntity(World world, IBlockState state) {
		return new TilePump();
	}
}
