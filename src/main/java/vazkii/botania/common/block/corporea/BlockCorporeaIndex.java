/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [Feb 15, 2015, 12:49:58 AM (GMT)]
 */
package vazkii.botania.common.block.corporea;

import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import vazkii.botania.api.lexicon.ILexiconable;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.client.lib.LibRenderIDs;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.block.tile.corporea.TileCorporeaBase;
import vazkii.botania.common.block.tile.corporea.TileCorporeaIndex;
import vazkii.botania.common.lexicon.LexiconData;
import vazkii.botania.common.lib.LibBlockNames;

public class BlockCorporeaIndex extends BlockCorporeaBase implements ILexiconable {

	public BlockCorporeaIndex() {
		super(Material.iron, LibBlockNames.CORPOREA_INDEX);
		setHardness(5.5F);
		setStepSound(soundTypeMetal);
	}

	@Override
	public boolean isOpaqueCube() {
		return false;
	}

	@Override
	public boolean renderAsNormalBlock() {
		return false;
	}

	@Override
	public void registerBlockIcons(IIconRegister par1IconRegister) {
		// NO-OP
	}

	@Override
	public IIcon getIcon(int side, int meta) {
		return ModBlocks.storage.getIcon(0, 2);
	}

	@Override
	public int getRenderType() {
		return LibRenderIDs.idCorporeaIndex;
	}

	@Override
	public TileCorporeaBase createNewTileEntity(World world, int meta) {
		return new TileCorporeaIndex();
	}

	@Override
	public LexiconEntry getEntry(World world, int x, int y, int z, EntityPlayer player, ItemStack lexicon) {
		return LexiconData.corporeaIndex;
	}

}
