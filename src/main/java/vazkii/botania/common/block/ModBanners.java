/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Aug 25, 2015, 7:11:35 PM (GMT)]
 */
package vazkii.botania.common.block;

import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.tileentity.BannerPattern;
import vazkii.botania.common.item.ModItems;

import java.util.Locale;

public final class ModBanners {

	public static void init() {
		addPattern("flower", "flr", new ItemStack(ModItems.livingwoodTwig));
		addPattern("lexicon", "lex", new ItemStack(ModItems.lexicon));
		addPattern("logo", "lgo", new ItemStack(ModItems.terrasteel));
		addPattern("sapling", "spl", new ItemStack(ModItems.dreamwoodTwig));
		addPattern("tiny_potato", "tpt", new ItemStack(ModBlocks.tinyPotato));

		addPattern("spark_dispersive", "sds", new ItemStack(ModItems.sparkUpgradeDispersive));
		addPattern("spark_dominant", "sdm", new ItemStack(ModItems.sparkUpgradeDominant));
		addPattern("spark_recessive", "src", new ItemStack(ModItems.sparkUpgradeRecessive));
		addPattern("spark_isolated", "sis", new ItemStack(ModItems.sparkUpgradeIsolated));

		addPattern("fish", "fis", new ItemStack(Items.COD));
		addPattern("axe", "axe", new ItemStack(Items.IRON_AXE));
		addPattern("hoe", "hoe", new ItemStack(Items.IRON_HOE));
		addPattern("pickaxe", "pik", new ItemStack(Items.IRON_PICKAXE));
		addPattern("shovel", "shv", new ItemStack(Items.IRON_SHOVEL));
		addPattern("sword", "srd", new ItemStack(Items.IRON_SWORD));
	}

	private static void addPattern(String name, String id, ItemStack craftingItem) {
		name = "botania_" + name;
		id = "bt_" + id;
		BannerPattern.create(name.toUpperCase(Locale.ROOT), name, id, craftingItem);
	}
}
