/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [28/09/2016, 16:14:04 (GMT)]
 */
package vazkii.botania.common.block;

import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import vazkii.botania.api.internal.IManaBurst;
import vazkii.botania.api.item.IHourglassTrigger;
import vazkii.botania.api.lexicon.ILexiconable;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.api.mana.IManaTrigger;
import vazkii.botania.api.wand.IWandHUD;
import vazkii.botania.api.wand.IWandable;
import vazkii.botania.common.block.tile.TileAnimatedTorch;
import vazkii.botania.common.lexicon.LexiconData;

import javax.annotation.Nonnull;

public class BlockAnimatedTorch extends BlockMod implements IWandable, IManaTrigger, IHourglassTrigger, IWandHUD, ILexiconable {

	private static final VoxelShape SHAPE = makeCuboidShape(0, 0, 0, 16, 4, 16);

	public BlockAnimatedTorch(Properties builder) {
		super(builder);
	}

	@Override
	public boolean onBlockActivated(IBlockState state, World worldIn, BlockPos pos, EntityPlayer playerIn, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
		if(hand == EnumHand.MAIN_HAND && playerIn.isSneaking() && playerIn.getHeldItem(hand).isEmpty()) {
			((TileAnimatedTorch) worldIn.getTileEntity(pos)).handRotate();
			return true;
		}

		return false;
	}
	
	@Override
	public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase entity, ItemStack stack) {
		((TileAnimatedTorch) world.getTileEntity(pos)).onPlace(entity);
	}

	@Override
	public void onBurstCollision(IManaBurst burst, World world, BlockPos pos) {
		if(!burst.isFake())
			((TileAnimatedTorch) world.getTileEntity(pos)).toggle();
	}

	@Override
	public void onTriggeredByHourglass(World world, BlockPos pos, TileEntity hourglass) {
		((TileAnimatedTorch) world.getTileEntity(pos)).toggle();
	}

	@Override
	public boolean onUsedByWand(EntityPlayer player, ItemStack stack, World world, BlockPos pos, EnumFacing side) {
		((TileAnimatedTorch) world.getTileEntity(pos)).onWanded();
		return true;
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void renderHUD(Minecraft mc, World world, BlockPos pos) {
		((TileAnimatedTorch) world.getTileEntity(pos)).renderHUD(mc);
	}

	@Override
	public boolean canProvidePower(IBlockState state) {
		return true;
	}

	@Override
	public int getStrongPower(IBlockState blockState, IBlockReader blockAccess, BlockPos pos, EnumFacing side) {
		return getWeakPower(blockState, blockAccess, pos, side);
	}

	@Override
	public int getWeakPower(IBlockState blockState, IBlockReader blockAccess, BlockPos pos, EnumFacing side) {
		TileAnimatedTorch tile = (TileAnimatedTorch) blockAccess.getTileEntity(pos);

		if(tile.rotating)
			return 0;

		if(TileAnimatedTorch.SIDES[tile.side] == side)
			return 15;

		return 0;
	}

	@Nonnull
	@Override
	public EnumBlockRenderType getRenderType(IBlockState state) {
		return EnumBlockRenderType.ENTITYBLOCK_ANIMATED;
	}

	@Override
	public boolean isFullCube(IBlockState state) {
		return false;
	}

	@Nonnull
	@Override
	public VoxelShape getShape(IBlockState state, IBlockReader world, BlockPos pos) {
		return SHAPE;
	}

	@Override
	public TileEntity createTileEntity(@Nonnull IBlockState state, @Nonnull IBlockReader world) {
		return new TileAnimatedTorch();
	}

	@Override
	public boolean hasTileEntity(IBlockState state) {
		return true;
	}

	@Nonnull
	@Override
	public BlockFaceShape getBlockFaceShape(IBlockReader world, IBlockState state, BlockPos pos, EnumFacing side) {
		return BlockFaceShape.UNDEFINED;
	}

	@Override
	public void onPlayerDestroy(IWorld world, BlockPos pos, IBlockState state) {
		// TE is already gone so best we can do is just notify everyone
		world.notifyNeighbors(pos, this);
		super.onPlayerDestroy(world, pos, state);
	}

	@Override
	public LexiconEntry getEntry(World world, BlockPos pos, EntityPlayer player, ItemStack lexicon) {
		return LexiconData.animatedTorch;
	}

}
