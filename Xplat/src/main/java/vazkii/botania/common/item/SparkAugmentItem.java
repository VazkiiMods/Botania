/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.item;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import vazkii.botania.api.mana.spark.SparkUpgradeType;

public class SparkAugmentItem extends Item {
	public final SparkUpgradeType type;

	public SparkAugmentItem(SparkUpgradeType type, Properties properties) {
		super(properties);
		this.type = type;
	}

	public static ItemStack getByType(SparkUpgradeType type) {
		return switch (type) {
			case DOMINANT -> new ItemStack(BotaniaItems.SPARK_AUGMENT_DOMINANT);
			case RECESSIVE -> new ItemStack(BotaniaItems.SPARK_AUGMENT_RECESSIVE);
			case DISPERSIVE -> new ItemStack(BotaniaItems.SPARK_AUGMENT_DISPERSIVE);
			case ISOLATED -> new ItemStack(BotaniaItems.SPARK_AUGMENT_ISOLATED);
			default -> ItemStack.EMPTY;
		};
	}

}
