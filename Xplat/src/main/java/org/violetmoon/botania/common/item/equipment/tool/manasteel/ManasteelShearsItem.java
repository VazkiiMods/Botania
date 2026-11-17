/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.item.equipment.tool.manasteel;

import net.minecraft.client.Minecraft;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShearsItem;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.state.BlockState;

import vazkii.botania.api.item.SortableTool;

public class ManasteelShearsItem extends ShearsItem implements SortableTool {

	public ManasteelShearsItem(Properties props) {
		super(props);
	}

	@Override
	public int getSortingPriority(ItemStack stack, BlockState state) {
		//Todo test if this works
		HolderLookup<Enchantment> enchLookup = Minecraft.getInstance().level.holderLookup(Registries.ENCHANTMENT);
		return 1000 + EnchantmentHelper.getItemEnchantmentLevel(enchLookup.getOrThrow(Enchantments.EFFICIENCY), stack);
	}
}
