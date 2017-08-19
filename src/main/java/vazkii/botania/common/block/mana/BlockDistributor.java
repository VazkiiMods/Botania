/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Mar 3, 2014, 1:44:29 AM (GMT)]
 */
package vazkii.botania.common.block.mana;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import vazkii.botania.api.lexicon.ILexiconable;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.common.block.BlockMod;
import vazkii.botania.common.block.tile.mana.TileDistributor;
import vazkii.botania.common.lexicon.LexiconData;
import vazkii.botania.common.lib.LibBlockNames;

import javax.annotation.Nonnull;

public class BlockDistributor extends BlockMod implements ILexiconable {

	private static final AxisAlignedBB AABB = new AxisAlignedBB(0.25, 0.0, 0.25, 0.75, 1.0, 0.75);

	public BlockDistributor() {
		super(Material.ROCK, LibBlockNames.DISTRIBUTOR);
		setHardness(2.0F);
		setResistance(10.0F);
		setSoundType(SoundType.STONE);
	}
	
	@Nonnull
	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess world, BlockPos pos) {
		return AABB;
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
		return new TileDistributor();
	}

	@Override
	public LexiconEntry getEntry(World world, BlockPos pos, EntityPlayer player, ItemStack lexicon) {
		return LexiconData.distributor;
	}

}
