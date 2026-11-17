/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */

package vazkii.botania.common.item;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.component.Tool;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Block;

import vazkii.botania.common.lib.BotaniaTags;

import java.util.List;
import java.util.function.Supplier;

public enum BotaniaItemTiers implements Tier {
	MANASTEEL(
			300, 6.2F, 2, 20,
			() -> BotaniaItems.MANASTEEL_INGOT, BlockTags.INCORRECT_FOR_DIAMOND_TOOL),
	ELEMENTIUM(
			720, 6.2F, 2, 20,
			() -> BotaniaItems.ELEMENTIUM_INGOT, BlockTags.INCORRECT_FOR_DIAMOND_TOOL),
	TERRASTEEL(
			2300, 9, 4, 26,
			() -> BotaniaItems.TERRASTEEL_INGOT, BlockTags.INCORRECT_FOR_NETHERITE_TOOL),
	VITREOUS(
			125, 3.9f, 0, 10,
			() -> Items.AIR, BlockTags.INCORRECT_FOR_WOODEN_TOOL) {
		@Override
		public Tool createToolProperties(TagKey<Block> block) {
			return new Tool(
					List.of(
							// always correct tool for silktouched blocks, relevant e.g. for copper bulb
							Tool.Rule.minesAndDrops(BotaniaTags.Blocks.VITREOUS_PICKAXE_SILKTOUCHED, this.getSpeed()),
							Tool.Rule.deniesDrops(this.getIncorrectBlocksForDrops()),
							Tool.Rule.minesAndDrops(BotaniaTags.Blocks.MINEABLE_WITH_VITREOUS_PICKAXE, this.getSpeed())
					),
					1.0F, 1
			);
		}

		@Override
		public Ingredient getRepairIngredient() {
			return Ingredient.of(TagKey.create(Registries.ITEM, ResourceLocation.parse("c:glass_blocks/colorless")));
		}
	};

	private final int maxUses;
	private final float efficiency;
	private final float attackDamage;
	private final int enchantability;
	private final Supplier<Item> repairItem;
	private final TagKey<Block> incorrectBlockForDrops;

	BotaniaItemTiers(int maxUses, float efficiency, float attackDamage, int enchantability,
			Supplier<Item> repairItem, TagKey<Block> incorrectBlockForDrops) {
		this.maxUses = maxUses;
		this.efficiency = efficiency;
		this.attackDamage = attackDamage;
		this.enchantability = enchantability;
		this.repairItem = repairItem;
		this.incorrectBlockForDrops = incorrectBlockForDrops;
	}

	@Override
	public int getUses() {
		return maxUses;
	}

	@Override
	public float getSpeed() {
		return efficiency;
	}

	@Override
	public float getAttackDamageBonus() {
		return attackDamage;
	}

	@Override
	public TagKey<Block> getIncorrectBlocksForDrops() {
		return incorrectBlockForDrops;
	}

	@Override
	public int getEnchantmentValue() {
		return enchantability;
	}

	@Override
	public Ingredient getRepairIngredient() {
		return Ingredient.of(repairItem.get());
	}
}
