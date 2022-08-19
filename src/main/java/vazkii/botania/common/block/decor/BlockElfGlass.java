/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [Sep 8, 2014, 6:40:17 PM (GMT)]
 */
package vazkii.botania.common.block.decor;

import java.util.Random;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import vazkii.botania.api.lexicon.ILexiconable;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.api.recipe.IElvenItem;
import vazkii.botania.client.core.helper.IconHelper;
import vazkii.botania.common.lexicon.LexiconData;
import vazkii.botania.common.lib.LibBlockNames;

public class BlockElfGlass extends BlockManaGlass implements IElvenItem, ILexiconable {

	private static final int ICON_COUNT = 4;
	IIcon[] icons;

	public BlockElfGlass() {
		super(LibBlockNames.ELF_GLASS);
	}

	@Override
	public void registerBlockIcons(IIconRegister par1IconRegister) {
		icons = new IIcon[ICON_COUNT];
		for(int i = 0; i < ICON_COUNT; i++)
			icons[i] = IconHelper.forBlock(par1IconRegister, this, i);
		blockIcon = IconHelper.forBlock(par1IconRegister, this);
	}

	@Override
	public IIcon getIcon(IBlockAccess world, int x, int y, int z, int s) {
		int v = (int) Math.floor(new Random(x * 10 ^ y * 20 ^ z * 30).nextInt(ICON_COUNT * 100) / 100.0);
		return icons[v];
	}

	@Override
	public boolean isElvenItem(ItemStack stack) {
		return true;
	}

	@Override
	public LexiconEntry getEntry(World world, int x, int y, int z, EntityPlayer player, ItemStack lexicon) {
		return LexiconData.elvenResources;
	}

}
