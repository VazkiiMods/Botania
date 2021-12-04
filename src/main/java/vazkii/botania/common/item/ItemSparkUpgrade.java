/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.item;

import net.minecraft.world.item.ItemStack;

import vazkii.botania.api.mana.spark.SparkUpgradeType;


public class ItemSparkUpgrade extends ItemModPattern {
	public final SparkUpgradeType type;

	public ItemSparkUpgrade(/*LoomPattern pattern, */Properties builder, SparkUpgradeType type) {
		super(/*pattern, */builder);
		this.type = type;
	}

	public static ItemStack getByType(SparkUpgradeType type) {
		return switch (type) {
			case DOMINANT -> new ItemStack(ModItems.sparkUpgradeDominant);
			case RECESSIVE -> new ItemStack(ModItems.sparkUpgradeRecessive);
			case DISPERSIVE -> new ItemStack(ModItems.sparkUpgradeDispersive);
			case ISOLATED -> new ItemStack(ModItems.sparkUpgradeIsolated);
			default -> ItemStack.EMPTY;
		};
	}

}
