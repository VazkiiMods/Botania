/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.item.equipment.tool;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.Material;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolMaterial;
import net.minecraft.recipe.Ingredient;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import vazkii.botania.common.item.equipment.tool.manasteel.ItemManasteelPick;

import java.util.Map;

public class ItemGlassPick extends ItemManasteelPick {
	private static final String TAG_SILK_HACK = "botania:silk_hack";
	private static final int MANA_PER_DAMAGE = 160;
	private static final ToolMaterial MATERIAL = new ToolMaterial() {
		@Override
		public int getDurability() {
			return 125;
		}

		@Override
		public float getMiningSpeedMultiplier() {
			return 4.8F;
		}

		@Override
		public float getAttackDamage() {
			return 0;
		}

		@Override
		public int getMiningLevel() {
			return 0;
		}

		@Override
		public int getEnchantability() {
			return 10;
		}

		@Override
		public Ingredient getRepairIngredient() {
			return Ingredient.ofItems(Blocks.GLASS);
		}
	};

	public ItemGlassPick(Settings props) {
		super(MATERIAL, props, -1);
	}

	/*
	* No way to modify the loot context so we're gonna go braindead hack workaround here:
	* - When block starting to break, if the tool doesn't have silktouch already, add it and add a "temp silk touch" flag
	* - Every tick, if the "temp silk touch" flag is present, remove it and remove any silk touch enchants from the stack
	*/

	public boolean onBlockStartBreak(ItemStack itemstack, BlockPos pos, PlayerEntity player) {
		BlockState state = player.world.getBlockState(pos);
		boolean hasSilk = EnchantmentHelper.getLevel(Enchantments.SILK_TOUCH, itemstack) > 0;
		if (hasSilk || !isGlass(state)) {
			return false;
		}

		itemstack.addEnchantment(Enchantments.SILK_TOUCH, 1);
		itemstack.getTag().putBoolean(TAG_SILK_HACK, true);

		return false;
	}

	@Override
	public void inventoryTick(ItemStack stack, World world, Entity player, int slot, boolean selected) {
		super.inventoryTick(stack, world, player, slot, selected);
		if (stack.getOrCreateTag().getBoolean(TAG_SILK_HACK)) {
			stack.getTag().remove(TAG_SILK_HACK);
			Map<Enchantment, Integer> ench = EnchantmentHelper.get(stack);
			ench.remove(Enchantments.SILK_TOUCH);
			EnchantmentHelper.set(ench, stack);
		}
	}

	private boolean isGlass(BlockState state) {
		return state.getMaterial() == Material.GLASS; // todo 1.16-fabric || Tags.Blocks.GLASS.contains(state.getBlock());
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
