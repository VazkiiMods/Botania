/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.item.equipment.tool.elementium;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;

import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.common.annotations.SoftImplement;
import vazkii.botania.common.item.equipment.tool.manasteel.ItemManasteelAxe;
import vazkii.botania.mixin.AccessorLivingEntity;

import java.util.function.Consumer;

import static vazkii.botania.common.lib.ResourceLocationHelper.prefix;

public class ItemElementiumAxe extends ItemManasteelAxe {
	private static final ResourceLocation BEHEADING_LOOT_TABLE = prefix("elementium_axe_beheading");

	public ItemElementiumAxe(Properties props) {
		super(BotaniaAPI.instance().getElementiumItemTier(), props);
	}

	public static void onEntityDrops(boolean hitRecently, DamageSource source, LivingEntity target,
			Consumer<ItemStack> consumer) {
		var ctx = ((AccessorLivingEntity) target).callCreateLootContext(hitRecently, source);
		target.level.getServer().getLootTables().get(BEHEADING_LOOT_TABLE)
				.getRandomItems(ctx.create(LootContextParamSets.ENTITY), consumer);
	}

	@SoftImplement("IForgeItem")
	public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
		if (enchantment == Enchantments.MOB_LOOTING) {
			return true;
		} else {
			// Copy the default impl
			return enchantment.category.canEnchant(this);
		}

	}

}
