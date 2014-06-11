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

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import vazkii.botania.api.lexicon.ILexiconable;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.common.Botania;
import vazkii.botania.common.core.helper.MathHelper;
import vazkii.botania.common.lib.LibGuiIDs;
import vazkii.botania.common.lib.LibItemNames;
import vazkii.botania.common.lib.LibMisc;

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
	public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4) {
		String edition = String.format(StatCollector.translateToLocal("botaniamisc.edition"), getEdition());
		if(!edition.isEmpty())
			par3List.add(edition);
	}
	
	public static String getEdition() {
		String version = LibMisc.BUILD;
		int build = version.contains("ANT") ? 0 : Integer.parseInt(version);
		return build == 0 ? StatCollector.translateToLocal("botaniamisc.devEdition") : MathHelper.numberToOrdinal(build);
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
