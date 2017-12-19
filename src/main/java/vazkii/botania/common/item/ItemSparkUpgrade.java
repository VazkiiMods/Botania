/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Aug 21, 2014, 5:32:06 PM (GMT)]
 */
package vazkii.botania.common.item;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import vazkii.botania.api.mana.spark.SparkUpgradeType;
import vazkii.botania.common.lib.LibItemNames;

import java.util.Locale;

public class ItemSparkUpgrade extends ItemMod {
	public final SparkUpgradeType type;

	public ItemSparkUpgrade(SparkUpgradeType type) {
		super(LibItemNames.SPARK_UPGRADE + "_" + type.name().toLowerCase(Locale.ROOT));
		this.type = type;
	}

	public static ItemStack getByType(SparkUpgradeType type) {
		switch(type) {
			case DOMINANT: return new ItemStack(ModItems.sparkUpgradeDominant);
			case RECESSIVE: return new ItemStack(ModItems.sparkUpgradeRecessive);
			case DISPERSIVE: return new ItemStack(ModItems.sparkUpgradeDispersive);
			case ISOLATED: return new ItemStack(ModItems.sparkUpgradeIsolated);
			default: return ItemStack.EMPTY;
		}
	}

}
