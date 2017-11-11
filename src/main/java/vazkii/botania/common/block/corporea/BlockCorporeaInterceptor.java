/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Apr 19, 2015, 6:18:03 PM (GMT)]
 */
package vazkii.botania.common.block.corporea;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import vazkii.botania.api.lexicon.ILexiconable;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.api.state.BotaniaStateProps;
import vazkii.botania.common.block.tile.corporea.TileCorporeaBase;
import vazkii.botania.common.block.tile.corporea.TileCorporeaInterceptor;
import vazkii.botania.common.lexicon.LexiconData;
import vazkii.botania.common.lib.LibBlockNames;

import javax.annotation.Nonnull;
import java.util.Random;

public class BlockCorporeaInterceptor extends BlockCorporeaBase implements ILexiconable {

	public BlockCorporeaInterceptor() {
		super(Material.IRON, LibBlockNames.CORPOREA_INTERCEPTOR);
		setHardness(5.5F);
		setSoundType(SoundType.METAL);
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
	public void updateTick(World world, BlockPos pos, IBlockState state, Random rand) {
		world.setBlockState(pos, state.withProperty(BotaniaStateProps.POWERED, false), 1 | 2);
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
	public int tickRate(World world) {
		return 2;
	}

	@Nonnull
	@Override
	public TileCorporeaBase createTileEntity(@Nonnull World world, @Nonnull IBlockState state) {
		return new TileCorporeaInterceptor();
	}

	@Override
	public LexiconEntry getEntry(World world, BlockPos pos, EntityPlayer player, ItemStack lexicon) {
		return LexiconData.corporeaInterceptor;
	}

}
