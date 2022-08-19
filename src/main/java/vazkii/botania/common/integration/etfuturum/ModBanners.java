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
package vazkii.botania.common.integration.etfuturum;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.EnumHelper;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.item.ModItems;

public final class ModBanners {

	public static void init() {
		try {
			Class<Enum<?>> clazz = (Class<Enum<?>>) Class.forName("ganymedes01.etfuturum.tileentities.TileEntityBanner$EnumBannerPattern");
			addPattern(clazz, "flower", "flr", new ItemStack(ModItems.manaResource, 1, 3));
			addPattern(clazz, "lexicon", "lex", new ItemStack(ModItems.lexicon));
			addPattern(clazz, "logo", "lgo", new ItemStack(ModItems.manaResource, 1, 4));
			addPattern(clazz, "sapling", "spl", new ItemStack(ModItems.manaResource, 1, 13));
			addPattern(clazz, "tiny_potato", "tpt", new ItemStack(ModBlocks.tinyPotato));

			addPattern(clazz, "spark_dispersive", "sds", new ItemStack(ModItems.sparkUpgrade, 1, 0));
			addPattern(clazz, "spark_dominant", "sdm", new ItemStack(ModItems.sparkUpgrade, 1, 1));
			addPattern(clazz, "spark_recessive", "src", new ItemStack(ModItems.sparkUpgrade, 1, 2));
			addPattern(clazz, "spark_isolated", "sis", new ItemStack(ModItems.sparkUpgrade, 1, 3));

			addPattern(clazz, "fish", "fis", new ItemStack(Items.fish));
			addPattern(clazz, "axe", "axe", new ItemStack(Items.iron_axe));
			addPattern(clazz, "hoe", "hoe", new ItemStack(Items.iron_hoe));
			addPattern(clazz, "pickaxe", "pik", new ItemStack(Items.iron_pickaxe));
			addPattern(clazz, "shovel", "shv", new ItemStack(Items.iron_shovel));
			addPattern(clazz, "sword", "srd", new ItemStack(Items.iron_sword));
		} catch (ClassNotFoundException e) {
			// Looks like we don't have EtFuturum around, let's not do any of this nonsense then
		}
	}

	public static void addPattern(Class<Enum<?>> clazz, String name, String id, ItemStack craftingItem) {
		name = "botania_" + name;
		id = "bt_" + id;
		EnumHelper.addEnum(clazz, name.toUpperCase(), new Class[] { String.class, String.class, ItemStack.class }, new Object[] { name, id, craftingItem });
	}
}
