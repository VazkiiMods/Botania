/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Mar 10, 2014, 7:57:38 PM (GMT)]
 */
package vazkii.botania.common.block.mana;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import vazkii.botania.api.internal.IManaBurst;
import vazkii.botania.api.lexicon.ILexiconable;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.api.mana.IManaCollisionGhost;
import vazkii.botania.api.state.BotaniaStateProps;
import vazkii.botania.common.block.BlockMod;
import vazkii.botania.common.block.tile.mana.TileManaDetector;
import vazkii.botania.common.lexicon.LexiconData;
import vazkii.botania.common.lib.LibBlockNames;

import javax.annotation.Nonnull;
import java.util.List;

public class BlockManaDetector extends BlockMod implements ILexiconable, IManaCollisionGhost {

	public BlockManaDetector() {
		super(Material.ROCK, LibBlockNames.MANA_DETECTOR);
		setHardness(2.0F);
		setResistance(10.0F);
		setSoundType(SoundType.STONE);
		setDefaultState(blockState.getBaseState().withProperty(BotaniaStateProps.POWERED, false));
	}

	@Nonnull
	@Override
	public BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, BotaniaStateProps.POWERED);
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return state.getValue(BotaniaStateProps.POWERED) ? 1 : 0;
	}

	@Nonnull
	@Override
	public IBlockState getStateFromMeta(int meta) {
		return getDefaultState().withProperty(BotaniaStateProps.POWERED, meta == 1);
	}

	@Override
	public boolean canProvidePower(IBlockState state) {
		return true;
	}

	@Override
	public int getWeakPower(IBlockState state, IBlockAccess par1iBlockAccess, BlockPos pos, EnumFacing side) {
		return state.getValue(BotaniaStateProps.POWERED) ? 15 : 0;
	}

	@Override
	public void addCollisionBoxToList(IBlockState state, @Nonnull World world, @Nonnull BlockPos pos, @Nonnull AxisAlignedBB par5AxisAlignedBB, @Nonnull List<AxisAlignedBB> stacks, Entity par7Entity, boolean isActualState) {
		if(par7Entity != null && !(par7Entity instanceof IManaBurst))
			super.addCollisionBoxToList(state, world, pos, par5AxisAlignedBB, stacks, par7Entity, isActualState);
	}

	@Override
	public boolean hasTileEntity(IBlockState state) {
		return true;
	}

	@Nonnull
	@Override
	public TileEntity createTileEntity(@Nonnull World world, @Nonnull IBlockState state) {
		return new TileManaDetector();
	}

	@Override
	public LexiconEntry getEntry(World world, BlockPos pos, EntityPlayer player, ItemStack lexicon) {
		return LexiconData.manaDetector;
	}

	@Override
	public boolean isGhost(IBlockState state, World world, BlockPos pos) {
		return true;
	}
}
