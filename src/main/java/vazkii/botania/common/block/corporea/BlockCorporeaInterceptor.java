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

import java.util.Random;

import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import vazkii.botania.api.lexicon.ILexiconable;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.client.core.helper.IconHelper;
import vazkii.botania.common.block.tile.corporea.TileCorporeaBase;
import vazkii.botania.common.block.tile.corporea.TileCorporeaInterceptor;
import vazkii.botania.common.lexicon.LexiconData;
import vazkii.botania.common.lib.LibBlockNames;

public class BlockCorporeaInterceptor extends BlockCorporeaBase implements ILexiconable {

	IIcon[] icons;

	public BlockCorporeaInterceptor() {
		super(Material.iron, LibBlockNames.CORPOREA_INTERCEPTOR);
		setHardness(5.5F);
		setStepSound(soundTypeMetal);
	}

	@Override
	public void registerBlockIcons(IIconRegister par1IconRegister) {
		icons = new IIcon[2];
		for(int i = 0; i < icons.length; i++)
			icons[i] = IconHelper.forBlock(par1IconRegister, this, i);
	}

	@Override
	public IIcon getIcon(int par1, int par2) {
		return icons[par1 > 1 ? 1 : 0];
	}

	@Override
	public void updateTick(World world, int x, int y, int z, Random rand) {
		world.setBlockMetadataWithNotify(x, y, z, 0, 1 | 2);
	}

	@Override
	public boolean canProvidePower() {
		return true;
	}

	@Override
	public int isProvidingWeakPower(IBlockAccess world, int x, int y, int z, int side) {
		return world.getBlockMetadata(x, y, z) != 0 ? 15 : 0;
	}

	@Override
	public int tickRate(World p_149738_1_) {
		return 2;
	}

	@Override
	public TileCorporeaBase createNewTileEntity(World world, int meta) {
		return new TileCorporeaInterceptor();
	}

	@Override
	public LexiconEntry getEntry(World world, int x, int y, int z, EntityPlayer player, ItemStack lexicon) {
		return LexiconData.corporeaInterceptor;
	}

}
