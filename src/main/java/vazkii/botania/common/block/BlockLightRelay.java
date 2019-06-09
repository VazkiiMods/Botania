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
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.block.BlockRenderType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.IWorldReader;
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
	public final LuminizerVariant variant;

	protected BlockLightRelay(LuminizerVariant variant, Properties builder) {
		super(builder);
		this.variant = variant;
		setDefaultState(stateContainer.getBaseState().with(BotaniaStateProps.POWERED, false));
	}

	@Nonnull
	@Override
	public VoxelShape getShape(BlockState state, IBlockReader world, BlockPos pos, ISelectionContext ctx) {
		return SHAPE;
	}

	@Override
	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
		builder.add(BotaniaStateProps.POWERED);
	}

	@Override
	public boolean onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, Direction s, float xs, float ys, float zs) {
		((TileLightRelay) world.getTileEntity(pos)).mountEntity(player);
		return true;
	}

	@Override
	public int tickRate(IWorldReader world) {
		return 2;
	}

	@Override
	public void neighborChanged(BlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos) {
		if(!worldIn.isRemote && variant == LuminizerVariant.TOGGLE) {
			if(state.get(BotaniaStateProps.POWERED) && !worldIn.isBlockPowered(pos))
				worldIn.setBlockState(pos, state.with(BotaniaStateProps.POWERED, false));
			else if(!state.get(BotaniaStateProps.POWERED) && worldIn.isBlockPowered(pos))
				worldIn.setBlockState(pos, state.with(BotaniaStateProps.POWERED, true));
		}
	}

	@Override
	public void tick(BlockState state, World world, BlockPos pos, Random rand) {
		world.setBlockState(pos, state.with(BotaniaStateProps.POWERED, false), 1 | 2);
	}

	@Override
	public boolean canProvidePower(BlockState state) {
		return variant == LuminizerVariant.DETECTOR;
	}

	@Override
	public int getWeakPower(BlockState state, IBlockReader world, BlockPos pos, Direction s) {
		return variant == LuminizerVariant.DETECTOR
				&& state.get(BotaniaStateProps.POWERED) ? 15 : 0;
	}

	@Override
	public boolean isFullCube(BlockState state) {
		return false;
	}

	@Nonnull
	@Override
	public BlockRenderType getRenderType(BlockState state) {
		return BlockRenderType.ENTITYBLOCK_ANIMATED;
	}

	@Override
	public boolean hasTileEntity(BlockState state) {
		return true;
	}

	@Nonnull
	@Override
	public TileEntity createTileEntity(@Nonnull BlockState state, @Nonnull IBlockReader world) {
		return new TileLightRelay();
	}

	@Override
	public boolean onUsedByWand(PlayerEntity player, ItemStack stack, World world, BlockPos pos, Direction side) {
		return false;
	}

	@Override
	public LexiconEntry getEntry(World world, BlockPos pos, PlayerEntity player, ItemStack lexicon) {
		return LexiconData.luminizerTransport;
	}

}
