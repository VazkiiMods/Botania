/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Jul 15, 2015, 8:31:13 PM (GMT)]
 */
package vazkii.botania.common.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import vazkii.botania.api.lexicon.ILexiconable;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.api.state.BotaniaStateProps;
import vazkii.botania.api.state.enums.LuminizerVariant;
import vazkii.botania.api.wand.IWandable;
import vazkii.botania.client.core.handler.ModelHandler;
import vazkii.botania.common.block.tile.TileLightRelay;
import vazkii.botania.common.lexicon.LexiconData;
import vazkii.botania.common.lib.LibBlockNames;

import javax.annotation.Nonnull;
import java.util.Random;

public class BlockLightRelay extends BlockMod implements IWandable, ILexiconable {

	private static final AxisAlignedBB AABB = new AxisAlignedBB(5.0/16, 5.0/16, 5.0/16, 11.0/16, 11.0/16, 11.0/16);

	protected BlockLightRelay() {
		super(Material.GLASS, LibBlockNames.LIGHT_RELAY);
		setDefaultState(blockState.getBaseState().withProperty(BotaniaStateProps.LUMINIZER_VARIANT, LuminizerVariant.DEFAULT).withProperty(BotaniaStateProps.POWERED, false));
	}

	@Nonnull
	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess world, BlockPos pos) {
		return AABB;
	}

	@Nonnull
	@Override
	public BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, BotaniaStateProps.LUMINIZER_VARIANT, BotaniaStateProps.POWERED);
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		int meta = state.getValue(BotaniaStateProps.LUMINIZER_VARIANT).ordinal();
		if (state.getValue(BotaniaStateProps.POWERED)) {
			meta |= 8;
		} else {
			meta &= -9;
		}
		return meta;
	}

	@Nonnull
	@Override
	public IBlockState getStateFromMeta(int meta) {
		boolean powered = (meta & 8) != 0;
		meta &= -9;
		return getDefaultState().withProperty(BotaniaStateProps.POWERED, powered).withProperty(BotaniaStateProps.LUMINIZER_VARIANT, LuminizerVariant.class.getEnumConstants()[meta]);
	}

	@Override
	public void getSubBlocks(CreativeTabs tab, NonNullList<ItemStack> list) {
		for(int i = 0; i < 4; i++)
			list.add(new ItemStack(this, 1, i));
	}

	@Override
	public int damageDropped(IBlockState state) {
		return state.getValue(BotaniaStateProps.LUMINIZER_VARIANT).ordinal();
	}

	@Override
	public boolean isPassable(IBlockAccess world, BlockPos pos) {
		return false;
	}

	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing s, float xs, float ys, float zs) {
		((TileLightRelay) world.getTileEntity(pos)).mountEntity(player);
		return true;
	}

	@Override
	public AxisAlignedBB getCollisionBoundingBox(IBlockState state, @Nonnull IBlockAccess world, @Nonnull BlockPos pos) {
		return NULL_AABB;
	}

	@Override
	public int tickRate(World world) {
		return 2;
	}

	@Override
	public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos) {
		if(!worldIn.isRemote && state.getValue(BotaniaStateProps.LUMINIZER_VARIANT) == LuminizerVariant.TOGGLE) {
			if(state.getValue(BotaniaStateProps.POWERED) && !worldIn.isBlockPowered(pos))
				worldIn.setBlockState(pos, state.withProperty(BotaniaStateProps.POWERED, false));
			else if(!state.getValue(BotaniaStateProps.POWERED) && worldIn.isBlockPowered(pos))
				worldIn.setBlockState(pos, state.withProperty(BotaniaStateProps.POWERED, true));
		}
	}

	@Override
	public void updateTick(World world, BlockPos pos, IBlockState state, Random rand) {
		world.setBlockState(pos, state.withProperty(BotaniaStateProps.POWERED, false), 1 | 2);
	}

	@Override
	public boolean canProvidePower(IBlockState state) {
		return state.getValue(BotaniaStateProps.LUMINIZER_VARIANT) == LuminizerVariant.DETECTOR;
	}

	@Override
	public int getWeakPower(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing s) {
		return state.getValue(BotaniaStateProps.LUMINIZER_VARIANT) == LuminizerVariant.DETECTOR
				&& state.getValue(BotaniaStateProps.POWERED) ? 15 : 0;
	}

	@Override
	public boolean isFullCube(IBlockState state) {
		return false;
	}

	@Override
	public boolean isOpaqueCube(IBlockState state) {
		return false;
	}

	@Nonnull
	@Override
	public EnumBlockRenderType getRenderType(IBlockState state) {
		return EnumBlockRenderType.ENTITYBLOCK_ANIMATED;
	}

	@Override
	public boolean hasTileEntity(IBlockState state) {
		return true;
	}

	@Nonnull
	@Override
	public TileEntity createTileEntity(@Nonnull World world, @Nonnull IBlockState state) {
		return new TileLightRelay();
	}

	@Override
	public boolean onUsedByWand(EntityPlayer player, ItemStack stack, World world, BlockPos pos, EnumFacing side) {
		return false;
	}

	@Override
	public LexiconEntry getEntry(World world, BlockPos pos, EntityPlayer player, ItemStack lexicon) {
		return LexiconData.luminizerTransport;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void registerModels() {
		int i = 0;
		for(LuminizerVariant v : LuminizerVariant.values())
			ModelHandler.registerBlockToState(this, i++, getDefaultState().withProperty(BotaniaStateProps.LUMINIZER_VARIANT, v));
	}

	@Nonnull
	@Override
	public BlockFaceShape getBlockFaceShape(IBlockAccess world, IBlockState state, BlockPos pos, EnumFacing side) {
		return BlockFaceShape.UNDEFINED;
	}

}
