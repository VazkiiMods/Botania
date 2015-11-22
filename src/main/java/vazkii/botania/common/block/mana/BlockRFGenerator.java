/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [Aug 29, 2014, 9:51:58 PM (GMT)]
 */
package vazkii.botania.common.block.mana;

import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import vazkii.botania.api.lexicon.ILexiconable;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.common.block.BlockModContainer;
import vazkii.botania.common.block.tile.mana.TileRFGenerator;
import vazkii.botania.common.lexicon.LexiconData;
import vazkii.botania.common.lib.LibBlockNames;
import net.minecraftforge.fml.common.Optional;

public class BlockRFGenerator extends BlockModContainer implements ILexiconable {

	public BlockRFGenerator() {
		super(Material.rock);
		setHardness(2.0F);
		setResistance(10.0F);
		setStepSound(soundTypeStone);
		setUnlocalizedName(LibBlockNames.RF_GENERATOR);
	}

	@Override
	@Optional.Method(modid = "CoFHAPI|energy")
	public void onNeighborChange(IBlockAccess world, BlockPos pos, BlockPos tilePos) {
		TileEntity tile = world.getTileEntity(pos);
		if(tile != null && tile instanceof TileRFGenerator)
			((TileRFGenerator) tile).onNeighborTileChange(tilePos);
	}

	@Override
	public TileEntity createNewTileEntity(World world, int meta) {
		return new TileRFGenerator();
	}

	@Override
	public LexiconEntry getEntry(World world, BlockPos pos, EntityPlayer player, ItemStack lexicon) {
		return LexiconData.rfGenerator;
	}

}
