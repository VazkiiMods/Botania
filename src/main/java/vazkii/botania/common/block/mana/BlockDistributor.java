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

import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import vazkii.botania.api.lexicon.ILexiconable;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.client.core.helper.IconHelper;
import vazkii.botania.common.block.BlockModContainer;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.block.tile.mana.TileDistributor;
import vazkii.botania.common.lexicon.LexiconData;
import vazkii.botania.common.lib.LibBlockNames;

public class BlockDistributor extends BlockModContainer implements ILexiconable {

	IIcon iconSide, iconTop;

	public BlockDistributor() {
		super(Material.rock);
		setHardness(2.0F);
		setResistance(10.0F);
		setStepSound(soundTypeStone);
		setBlockName(LibBlockNames.DISTRIBUTOR);
	}

	@Override
	public void registerBlockIcons(IIconRegister par1IconRegister) {
		iconTop = IconHelper.forBlock(par1IconRegister, this, 0);
		iconSide = IconHelper.forBlock(par1IconRegister, this, 1);
	}

	@Override
	public IIcon getIcon(int par1, int par2) {
		return par1 == 0 ? ModBlocks.livingrock.getIcon(0, 0) : par1 == 1 ? iconTop : iconSide;
	}

	@Override
	public TileEntity createNewTileEntity(World world, int meta) {
		return new TileDistributor();
	}

	@Override
	public LexiconEntry getEntry(World world, int x, int y, int z, EntityPlayer player, ItemStack lexicon) {
		return LexiconData.distributor;
	}

}
