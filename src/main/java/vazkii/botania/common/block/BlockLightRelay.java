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
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorldReaderBase;
import net.minecraft.world.World;
import vazkii.botania.api.lexicon.ILexiconable;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.api.state.BotaniaStateProps;
import vazkii.botania.api.state.enums.LuminizerVariant;
import vazkii.botania.api.wand.IWandable;
import vazkii.botania.common.block.tile.TileLightRelay;
import vazkii.botania.common.lexicon.LexiconData;

import javax.annotation.Nonnull;
import java.util.Random;

public class BlockLightRelay extends BlockMod implements IWandable, ILexiconable {

	private static final VoxelShape SHAPE = makeCuboidShape(5, 5, 5, 11, 11, 11);
	private final LuminizerVariant variant;

	protected BlockLightRelay(LuminizerVariant variant, Builder builder) {
		super(builder);
		this.variant = variant;
		setDefaultState(stateContainer.getBaseState().with(BotaniaStateProps.POWERED, false));
	}

	@Nonnull
	@Override
	public VoxelShape getShape(IBlockState state, IBlockReader world, BlockPos pos) {
		return SHAPE;
	}

	@Override
	protected void fillStateContainer(StateContainer.Builder<Block, IBlockState> builder) {
		builder.add(BotaniaStateProps.POWERED);
	}

	@Override
	public boolean onBlockActivated(IBlockState state, World world, BlockPos pos, EntityPlayer player, EnumHand hand, EnumFacing s, float xs, float ys, float zs) {
		((TileLightRelay) world.getTileEntity(pos)).mountEntity(player);
		return true;
	}

	@Override
	public int tickRate(IWorldReaderBase world) {
		return 2;
	}

	@Override
	public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos) {
		if(!worldIn.isRemote && variant == LuminizerVariant.TOGGLE) {
			if(state.get(BotaniaStateProps.POWERED) && !worldIn.isBlockPowered(pos))
				worldIn.setBlockState(pos, state.with(BotaniaStateProps.POWERED, false));
			else if(!state.get(BotaniaStateProps.POWERED) && worldIn.isBlockPowered(pos))
				worldIn.setBlockState(pos, state.with(BotaniaStateProps.POWERED, true));
		}
	}

	@Override
	public void tick(IBlockState state, World world, BlockPos pos, Random rand) {
		world.setBlockState(pos, state.with(BotaniaStateProps.POWERED, false), 1 | 2);
	}

	@Override
	public boolean canProvidePower(IBlockState state) {
		return variant == LuminizerVariant.DETECTOR;
	}

	@Override
	public int getWeakPower(IBlockState state, IBlockReader world, BlockPos pos, EnumFacing s) {
		return variant == LuminizerVariant.DETECTOR
				&& state.get(BotaniaStateProps.POWERED) ? 15 : 0;
	}

	@Override
	public boolean isFullCube(IBlockState state) {
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
	public TileEntity createTileEntity(@Nonnull IBlockState state, @Nonnull IBlockReader world) {
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

	@Nonnull
	@Override
	public BlockFaceShape getBlockFaceShape(IBlockReader world, IBlockState state, BlockPos pos, EnumFacing side) {
		return BlockFaceShape.UNDEFINED;
	}

}
