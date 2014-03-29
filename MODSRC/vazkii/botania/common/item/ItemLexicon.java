/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under a
 * Creative Commons Attribution-NonCommercial-ShareAlike 3.0 License
 * (http://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB)
 * 
 * File Created @ [Jan 14, 2014, 5:53:00 PM (GMT)]
 */
package vazkii.botania.common.item;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import vazkii.botania.api.ILexiconable;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.common.Botania;
import vazkii.botania.common.lib.LibGuiIDs;
import vazkii.botania.common.lib.LibItemNames;

public class ItemLexicon extends ItemMod {

	public ItemLexicon() {
		super();
		setMaxStackSize(1);
		setUnlocalizedName(LibItemNames.LEXICON);
	}

	@Override
	public boolean onItemUse(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, World par3World, int par4, int par5, int par6, int par7, float par8, float par9, float par10) {
		if(par2EntityPlayer.isSneaking()) {
			Block block = par3World.getBlock(par4, par5, par6);

			if(block != null && block instanceof ILexiconable) {
				LexiconEntry entry = ((ILexiconable) block).getEntry(par3World, par4, par5, par6, par2EntityPlayer, par1ItemStack);
				if(entry != null) {
					Botania.proxy.setEntryToOpen(entry);
					par2EntityPlayer.openGui(Botania.instance, LibGuiIDs.LEXICON, par3World, 0, 0, 0);
					return true;
				}
			}
		}

		return false;
	}

	@Override
	public ItemStack onItemRightClick(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer) {
		par3EntityPlayer.openGui(Botania.instance, LibGuiIDs.LEXICON, par2World, 0, 0, 0);
		return par1ItemStack;
	}

	@Override
	public EnumRarity getRarity(ItemStack par1ItemStack) {
		return EnumRarity.uncommon;
	}

}
