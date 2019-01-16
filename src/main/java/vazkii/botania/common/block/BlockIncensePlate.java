/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [May 15, 2015, 4:07:09 PM (GMT)]
 */
package vazkii.botania.common.block;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.items.ItemHandlerHelper;
import vazkii.botania.api.internal.VanillaPacketDispatcher;
import vazkii.botania.api.lexicon.ILexiconable;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.api.state.BotaniaStateProps;
import vazkii.botania.common.block.tile.TileIncensePlate;
import vazkii.botania.common.lexicon.LexiconData;
import vazkii.botania.common.lib.LibBlockNames;

import javax.annotation.Nonnull;

public class BlockIncensePlate extends BlockMod implements ILexiconable {

	private static final AxisAlignedBB X_AABB = new AxisAlignedBB(0.375, 0, 0.125, 0.625, 1/16.0, 0.875);
	private static final AxisAlignedBB Z_AABB = new AxisAlignedBB(0.125, 0, 0.375, 0.875, 1/16.0, 0.625);

	protected BlockIncensePlate() {
		super(Material.WOOD, LibBlockNames.INCENSE_PLATE);
		setHardness(2.0F);
		setSoundType(SoundType.WOOD);
		setDefaultState(blockState.getBaseState().withProperty(BotaniaStateProps.CARDINALS, EnumFacing.SOUTH));
	}

	@Nonnull
	@Override
	public BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, BotaniaStateProps.CARDINALS);
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return state.getValue(BotaniaStateProps.CARDINALS).getIndex();
	}

	@Nonnull
	@Override
	public IBlockState getStateFromMeta(int meta) {
		if (meta < 2 || meta > 5) {
			meta = 2;
		}
		return getDefaultState().withProperty(BotaniaStateProps.CARDINALS, EnumFacing.byIndex(meta));
	}

	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing s, float xs, float ys, float zs) {
		TileIncensePlate plate = (TileIncensePlate) world.getTileEntity(pos);
		ItemStack plateStack = plate.getItemHandler().getStackInSlot(0);
		ItemStack stack = player.getHeldItem(hand);
		boolean did = false;

		if(world.isRemote)
			return true;

		if(plateStack.isEmpty() && plate.acceptsItem(stack)) {
			plate.getItemHandler().setStackInSlot(0, stack.copy());
			stack.shrink(1);
			did = true;
		} else if(!plateStack.isEmpty() && !plate.burning) {
			if(!stack.isEmpty() && stack.getItem() == Items.FLINT_AND_STEEL) {
				plate.ignite();
				stack.damageItem(1, player);
				did = true;
			} else {
				ItemHandlerHelper.giveItemToPlayer(player, plateStack);
				plate.getItemHandler().setStackInSlot(0, ItemStack.EMPTY);

				did = true;
			}
		}

		if(did)
			VanillaPacketDispatcher.dispatchTEToNearbyPlayers(plate);

		return did;
	}

	@Override
	public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
		world.setBlockState(pos, state.withProperty(BotaniaStateProps.CARDINALS, placer.getHorizontalFacing().getOpposite()));
	}

	@Override
	public boolean hasComparatorInputOverride(IBlockState state) {
		return true;
	}

	@Override
	public int getComparatorInputOverride(IBlockState state, World world, BlockPos pos) {
		return ((TileIncensePlate) world.getTileEntity(pos)).comparatorOutput;
	}

	@Nonnull
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
	public boolean hasTileEntity(IBlockState state) {
		return true;
	}

	@Nonnull
	@Override
	public TileEntity createTileEntity(@Nonnull World world, @Nonnull IBlockState state) {
		return new TileIncensePlate();
	}

	@Override
	public LexiconEntry getEntry(World world, BlockPos pos, EntityPlayer player, ItemStack lexicon) {
		return LexiconData.incense;
	}

	@Nonnull
	@Override
	public BlockFaceShape getBlockFaceShape(IBlockAccess world, IBlockState state, BlockPos pos, EnumFacing side) {
		return BlockFaceShape.UNDEFINED;
	}

}
