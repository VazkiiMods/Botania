/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under a
 * Creative Commons Attribution-NonCommercial-ShareAlike 3.0 License
 * (http://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB)
 * 
 * File Created @ [Jan 14, 2014, 5:17:47 PM (GMT)]
 */
package vazkii.botania.common.item;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import vazkii.botania.common.lib.LibOreDict;

public final class ModItems {

	public static Item lexicon;
	public static Item petal;
	public static Item dye;
	public static Item pestleAndMortar;
	public static Item twigWand;
	public static Item manaResource;
	public static Item lens;
	public static Item manaPetal;
	public static Item rune;

	public static void init() {
		lexicon = new ItemLexicon();
		petal = new ItemPetal();
		dye = new ItemDye();
		pestleAndMortar = new ItemPestleAndMortar();
		twigWand = new ItemTwigWand();
		manaResource = new ItemManaResource();
		lens = new ItemLens();
		manaPetal = new ItemManaPetal();
		rune = new ItemRune();

		OreDictionary.registerOre(LibOreDict.LEXICON, lexicon);
		for(int i = 0; i < 16; i++) {
			OreDictionary.registerOre(LibOreDict.PETAL[i], new ItemStack(petal.itemID, 1, i));
			OreDictionary.registerOre(LibOreDict.DYE[i], new ItemStack(dye.itemID, 1, i));
			OreDictionary.registerOre(LibOreDict.MANA_PETAL[i], new ItemStack(manaPetal.itemID, 1, i));
			OreDictionary.registerOre(LibOreDict.RUNE[i], new ItemStack(rune.itemID, 1, i));
		}
		OreDictionary.registerOre(LibOreDict.PESTLE_AND_MORTAR, pestleAndMortar);
		OreDictionary.registerOre(LibOreDict.MANA_STEEL, new ItemStack(manaResource, 1, 0));
		OreDictionary.registerOre(LibOreDict.MANA_PEARL, new ItemStack(manaResource, 1, 1));
		OreDictionary.registerOre(LibOreDict.MANA_DIAMOND, new ItemStack(manaResource, 1, 1));

		// Vanilla ores (because forge doesn't do this by default for some reason)
		OreDictionary.registerOre("ingotIron", Item.ingotIron);
		OreDictionary.registerOre("ingotGold", Item.ingotGold);
	}
}
