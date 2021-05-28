/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block;

import net.minecraft.block.entity.BannerPattern;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.Items;

import vazkii.botania.common.item.ModItems;

import java.util.LinkedHashMap;
import java.util.Map;

public final class ModBanners {

	public static final Map<Item, BannerPattern> PATTERNS = new LinkedHashMap<>();

	public static void init() {
		addPattern("flower", "flr", ModItems.livingwoodTwig);
		addPattern("lexicon", "lex", ModItems.lexicon);
		addPattern("logo", "lgo", ModItems.terrasteel);
		addPattern("sapling", "spl", ModItems.dreamwoodTwig);
		addPattern("tiny_potato", "tpt", ModBlocks.tinyPotato);

		addPattern("spark_dispersive", "sds", ModItems.sparkUpgradeDispersive);
		addPattern("spark_dominant", "sdm", ModItems.sparkUpgradeDominant);
		addPattern("spark_recessive", "src", ModItems.sparkUpgradeRecessive);
		addPattern("spark_isolated", "sis", ModItems.sparkUpgradeIsolated);

		addPattern("fish", "fis", Items.COD);
		addPattern("axe", "axe", Items.IRON_AXE);
		addPattern("hoe", "hoe", Items.IRON_HOE);
		addPattern("pickaxe", "pik", Items.IRON_PICKAXE);
		addPattern("shovel", "shv", Items.IRON_SHOVEL);
		addPattern("sword", "srd", Items.IRON_SWORD);
	}

	private static void addPattern(String name, String id, ItemConvertible craftingItem) {
		name = "botania_" + name;
		id = "bt_" + id;
		/* todo 1.16-fabric
		BannerPattern pattern = BannerPattern.create(name.toUpperCase(Locale.ROOT), name, id, false);
		PATTERNS.put(craftingItem.asItem().delegate, pattern);
		*/
	}
}
