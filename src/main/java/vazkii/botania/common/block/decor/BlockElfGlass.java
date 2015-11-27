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

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import vazkii.botania.api.lexicon.ILexiconable;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.api.recipe.IElvenItem;
import vazkii.botania.client.core.helper.IconHelper;
import vazkii.botania.common.lexicon.LexiconData;
import vazkii.botania.common.lib.LibBlockNames;

public class BlockElfGlass extends BlockManaGlass implements IElvenItem, ILexiconable {

	public BlockElfGlass() {
		super(LibBlockNames.ELF_GLASS);
	}

//	@Override
//	public IIcon getIcon(IBlockAccess world, int x, int y, int z, int s) {
//		int v = (int) Math.floor(new Random(x * 10 ^ y * 20 ^ z * 30).nextInt(ICON_COUNT * 100) / 100.0);
//		return icons[v];
//	} // todo 1.8 use randomized states for this

	@Override
	public boolean isElvenItem(ItemStack stack) {
		return true;
	}

	@Override
	public LexiconEntry getEntry(World world, BlockPos pos, EntityPlayer player, ItemStack lexicon) {
		return LexiconData.elvenResources;
	}

}
