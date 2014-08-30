/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under a
 * Creative Commons Attribution-NonCommercial-ShareAlike 3.0 License
 * (http://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB)
 * 
 * File Created @ [Aug 29, 2014, 9:51:58 PM (GMT)]
 */
package vazkii.botania.common.block;

import cpw.mods.fml.common.Optional;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.client.core.helper.IconHelper;
import vazkii.botania.common.block.tile.mana.TileDistributor;
import vazkii.botania.common.block.tile.mana.TileRFGenerator;
import vazkii.botania.common.lexicon.LexiconData;
import vazkii.botania.common.lib.LibBlockNames;

public class BlockRFGenerator extends BlockModContainer {

	protected BlockRFGenerator() {
		super(Material.rock);
		setHardness(2.0F);
		setResistance(10.0F);
		setStepSound(soundTypeStone);
		setBlockName(LibBlockNames.RF_GENERATOR);
	}

	@Override
	@Optional.Method(modid = "CoFHAPI")
	public void onNeighborChange(IBlockAccess world, int x, int y, int z, int tileX, int tileY, int tileZ) {
		TileEntity tile = world.getTileEntity(x, y, z);
		if(tile != null && tile instanceof TileRFGenerator)
			((TileRFGenerator) tile).onNeighborTileChange(tileX, tileY, tileZ);
	}
	
	@Override
	public TileEntity createNewTileEntity(World world, int meta) {
		return new TileRFGenerator();
	}

}
