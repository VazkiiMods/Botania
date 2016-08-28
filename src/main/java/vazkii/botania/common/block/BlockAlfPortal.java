/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [Jun 9, 2014, 7:17:46 PM (GMT)]
 */
package vazkii.botania.common.block;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
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
import vazkii.botania.api.state.enums.AlfPortalState;
import vazkii.botania.api.wand.IWandable;
import vazkii.botania.common.achievement.ModAchievements;
import vazkii.botania.common.block.tile.TileAlfPortal;
import vazkii.botania.common.lexicon.LexiconData;
import vazkii.botania.common.lib.LibBlockNames;

import javax.annotation.Nonnull;

public class BlockAlfPortal extends BlockMod implements IWandable, ILexiconable {

	public BlockAlfPortal() {
		super(Material.WOOD, LibBlockNames.ALF_PORTAL);
		setHardness(10F);
		setSoundType(SoundType.WOOD);
	}

	@Nonnull
	@Override
	public BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, BotaniaStateProps.ALFPORTAL_STATE);
	}

	@Nonnull
	@Override
	public IBlockState getStateFromMeta(int meta) {
		if (meta > AlfPortalState.values().length) {
			meta = 0;
		}
		return getDefaultState().withProperty(BotaniaStateProps.ALFPORTAL_STATE, AlfPortalState.values()[meta]);
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return state.getValue(BotaniaStateProps.ALFPORTAL_STATE).ordinal();
	}

	@Override
	public IBlockState pickDefaultState() {
		return blockState.getBaseState().withProperty(BotaniaStateProps.ALFPORTAL_STATE, AlfPortalState.OFF);
	}

	@Override
	public boolean hasTileEntity(IBlockState state) {
		return true;
	}

	@Nonnull
	@Override
	public TileEntity createTileEntity(@Nonnull World world, @Nonnull IBlockState state) {
		return new TileAlfPortal();
	}

	@Override
	public LexiconEntry getEntry(World world, BlockPos pos, EntityPlayer player, ItemStack lexicon) {
		return LexiconData.alfhomancyIntro;
	}

	@Override
	public boolean onUsedByWand(EntityPlayer player, ItemStack stack, World world, BlockPos pos, EnumFacing side) {
		boolean did = ((TileAlfPortal) world.getTileEntity(pos)).onWanded();
		if(did && player != null)
			player.addStat(ModAchievements.elfPortalOpen, 1);
		return did;
	}

	@Override
	public int getLightValue(@Nonnull IBlockState state, IBlockAccess world, @Nonnull BlockPos pos) {
		if(world.getBlockState(pos).getBlock() != this)
			return world.getBlockState(pos).getLightValue(world, pos);
		return state.getValue(BotaniaStateProps.ALFPORTAL_STATE) != AlfPortalState.OFF ? 15 : 0;
	}

}
