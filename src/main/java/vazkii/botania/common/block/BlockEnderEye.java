/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Jul 30, 2014, 1:05:07 PM (GMT)]
 */
package vazkii.botania.common.block;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import vazkii.botania.api.lexicon.ILexiconable;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.api.state.BotaniaStateProps;
import vazkii.botania.common.block.tile.TileEnderEye;
import vazkii.botania.common.lexicon.LexiconData;
import vazkii.botania.common.lib.LibBlockNames;

import javax.annotation.Nonnull;

public class BlockEnderEye extends BlockMod implements ILexiconable {

	protected BlockEnderEye() {
		super(Material.IRON, LibBlockNames.ENDER_EYE_BLOCK);
		setHardness(3F);
		setResistance(10F);
		setSoundType(SoundType.METAL);
		setDefaultState(blockState.getBaseState().withProperty(BotaniaStateProps.POWERED, false));
	}

	@Nonnull
	@Override
	public BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, BotaniaStateProps.POWERED);
	}

	@Nonnull
	@Override
	public IBlockState getStateFromMeta(int meta) {
		return getDefaultState().withProperty(BotaniaStateProps.POWERED, meta == 15);
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return state.getValue(BotaniaStateProps.POWERED) ? 15 : 0;
	}

	@Override
	public boolean canProvidePower(IBlockState state) {
		return true;
	}

	@Override
	public int getWeakPower(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing side) {
		return state.getValue(BotaniaStateProps.POWERED) ? 15 : 0;
	}

	@Override
	public boolean hasTileEntity(IBlockState state) {
		return true;
	}

	@Nonnull
	@Override
	public TileEntity createTileEntity(@Nonnull World world, @Nonnull IBlockState state) {
		return new TileEnderEye();
	}

	@Override
	public LexiconEntry getEntry(World world, BlockPos pos, EntityPlayer player, ItemStack lexicon) {
		return LexiconData.enderEyeBlock;
	}

	@Nonnull
	@Override
	public BlockFaceShape getBlockFaceShape(IBlockAccess world, IBlockState state, BlockPos pos, EnumFacing side) {
		return BlockFaceShape.UNDEFINED;
	}

}
