/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.item.equipment.tool;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Unit;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.component.Tool;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

import vazkii.botania.common.component.BotaniaDataComponents;
import vazkii.botania.common.item.equipment.tool.manasteel.ManasteelPickaxeItem;
import vazkii.botania.common.lib.BotaniaTags;

import java.util.List;

public class VitreousPickaxeItem extends ManasteelPickaxeItem implements
		vazkii.botania.api.item.SpecialBlockBreakingHandler {
	private static final int MANA_PER_DAMAGE = 160;
	private static final Tier MATERIAL = new Tier() {
		@Override
		public int getUses() {
			return 125;
		}

		@Override
		public float getSpeed() {
			return 3.9f;
		}

		@Override
		public float getAttackDamageBonus() {
			return 0;
		}

		@Override
		public TagKey<Block> getIncorrectBlocksForDrops() {
			return BlockTags.INCORRECT_FOR_WOODEN_TOOL;
		}

		@Override
		public int getEnchantmentValue() {
			return 10;
		}

		@Override
		public Ingredient getRepairIngredient() {
			return Ingredient.of(TagKey.create(Registries.ITEM, ResourceLocation.parse("c:glass_blocks/colorless")));
		}

		@Override
		public Tool createToolProperties(TagKey<Block> block) {
			return new Tool(
					List.of(
							// always correct tool for silktouched blocks, relevant e.g. for copper bulb
							Tool.Rule.minesAndDrops(BotaniaTags.Blocks.VITREOUS_PICKAXE_SILKTOUCHED, this.getSpeed()),
							Tool.Rule.deniesDrops(this.getIncorrectBlocksForDrops()),
							Tool.Rule.minesAndDrops(BotaniaTags.Blocks.MINEABLE_WITH_VITREOUS_PICKAXE, this.getSpeed())
					),
					1.0F, 1);
		}
	};

	public VitreousPickaxeItem(Properties props) {
		super(MATERIAL, props, -1);
	}

	/*
	* No way to modify the loot context so we're gonna go braindead hack workaround here:
	* - When block starting to break, if the tool doesn't have silktouch already, add it and add a "temp silk touch" flag
	* - Every tick, if the "temp silk touch" flag is present, remove it and remove any silk touch enchants from the stack
	*/
	@Override
	public void onBlockStartBreak(ServerLevel level, ItemStack itemstack, BlockPos pos, Player player) {
		BlockState state = level.getBlockState(pos);
		HolderLookup<Enchantment> enchantmentLookup = level.holderLookup(Registries.ENCHANTMENT);
		boolean hasSilk = EnchantmentHelper.getItemEnchantmentLevel(enchantmentLookup.getOrThrow(Enchantments.SILK_TOUCH), itemstack) > 0;
		if (hasSilk || !isGlass(state)) {
			return;
		}

		itemstack.enchant(enchantmentLookup.getOrThrow(Enchantments.SILK_TOUCH), 1);
		itemstack.set(BotaniaDataComponents.SILK_HACK, Unit.INSTANCE);
	}

	@Override
	public void inventoryTick(ItemStack stack, Level level, Entity player, int slot, boolean selected) {
		super.inventoryTick(stack, level, player, slot, selected);

		if (stack.has(BotaniaDataComponents.SILK_HACK)) {
			stack.remove(BotaniaDataComponents.SILK_HACK);
			EnchantmentHelper.updateEnchantments(stack, mutable -> mutable.removeIf(
					enchantmentHolder -> enchantmentHolder.is(Enchantments.SILK_TOUCH)));
		}
	}

	private boolean isGlass(BlockState state) {
		return state.is(BotaniaTags.Blocks.VITREOUS_PICKAXE_SILKTOUCHED);
	}

	@Override
	public int getManaPerDamage() {
		return MANA_PER_DAMAGE;
	}

	@Override
	public int getSortingPriority(ItemStack stack, BlockState state) {
		return isGlass(state) ? Integer.MAX_VALUE : 0;
	}

}
