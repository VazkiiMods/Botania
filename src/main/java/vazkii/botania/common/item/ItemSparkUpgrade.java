/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.item;

import io.github.fablabsmc.fablabs.api.bannerpattern.v1.LoomPattern;
import io.github.fablabsmc.fablabs.api.bannerpattern.v1.LoomPatternItem;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import vazkii.botania.api.mana.spark.SparkUpgradeType;

import java.util.List;

public class ItemSparkUpgrade extends LoomPatternItem {
	public final SparkUpgradeType type;

	public ItemSparkUpgrade(Properties builder, SparkUpgradeType type, LoomPattern pattern) {
		super(pattern, builder);
		this.type = type;
	}

	public static ItemStack getByType(SparkUpgradeType type) {
		switch (type) {
		case DOMINANT:
			return new ItemStack(ModItems.sparkUpgradeDominant);
		case RECESSIVE:
			return new ItemStack(ModItems.sparkUpgradeRecessive);
		case DISPERSIVE:
			return new ItemStack(ModItems.sparkUpgradeDispersive);
		case ISOLATED:
			return new ItemStack(ModItems.sparkUpgradeIsolated);
		default:
			return ItemStack.EMPTY;
		}
	}
	
	@Override
	public void appendHoverText(ItemStack stack, Level world, List<Component> lines, TooltipFlag ctx) {
		//no-op
	}
	
}
