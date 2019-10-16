/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Aug 6, 2014, 9:55:23 PM (GMT)]
 */
package vazkii.botania.common.item.equipment.tool;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.material.Material;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.IItemTier;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.Tags;
import vazkii.botania.common.item.equipment.tool.manasteel.ItemManasteelPick;

import java.util.Map;

public class ItemGlassPick extends ItemManasteelPick {
	private static final String TAG_SILK_HACK = "botania:silk_hack";
	private static final int MANA_PER_DAMAGE = 160;
	private static final IItemTier MATERIAL = new IItemTier() {
		@Override
		public int getMaxUses() {
			return 125;
		}

		@Override
		public float getEfficiency() {
			return 4.8F;
		}

		@Override
		public float getAttackDamage() {
			return 0;
		}

		@Override
		public int getHarvestLevel() {
			return 0;
		}

		@Override
		public int getEnchantability() {
			return 10;
		}

		@Override
		public Ingredient getRepairMaterial() {
			return Ingredient.fromItems(Blocks.GLASS);
		}
	};

	public ItemGlassPick(Properties props) {
		super(MATERIAL, props, -1);
	}

	/*
	 * No way to modify the loot context so we're gonna go braindead hack workaround here:
	 * - When block starting to break, if the tool doesn't have silktouch already, add it and add a "temp silk touch" flag
	 * - Every tick, if the "temp silk touch" flag is present, remove it and remove any silk touch enchants from the stack
 	 */

	@Override
	public boolean onBlockStartBreak(ItemStack itemstack, BlockPos pos, PlayerEntity player) {
		BlockState state = player.world.getBlockState(pos);
		boolean isGlass = state.getMaterial() == Material.GLASS || Tags.Blocks.GLASS.contains(state.getBlock());
		boolean hasSilk = EnchantmentHelper.getEnchantmentLevel(Enchantments.SILK_TOUCH, itemstack) > 0;
		if (hasSilk || !isGlass)
			return super.onBlockStartBreak(itemstack, pos, player);

		itemstack.addEnchantment(Enchantments.SILK_TOUCH, 1);
		itemstack.getTag().putBoolean(TAG_SILK_HACK, true);

		return false;
	}

	@Override
	public void inventoryTick(ItemStack stack, World world, Entity player, int slot, boolean selected) {
		super.inventoryTick(stack, world, player, slot, selected);
		if (stack.getOrCreateTag().getBoolean(TAG_SILK_HACK)) {
			stack.getTag().remove(TAG_SILK_HACK);
			Map<Enchantment, Integer> ench = EnchantmentHelper.getEnchantments(stack);
			ench.remove(Enchantments.SILK_TOUCH);
			EnchantmentHelper.setEnchantments(ench, stack);
		}
	}

	@Override
	public int getManaPerDmg() {
		return MANA_PER_DAMAGE;
	}

	@Override
	public int getSortingPriority(ItemStack stack) {
		return 0;
	}

}
