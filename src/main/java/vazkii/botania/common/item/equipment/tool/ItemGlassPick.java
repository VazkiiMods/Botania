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

import net.minecraft.block.Blocks;
import net.minecraft.block.material.Material;
import net.minecraft.block.Blocks;
import net.minecraft.item.IItemTier;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.BlockEvent.HarvestDropsEvent;
import vazkii.botania.common.item.equipment.tool.manasteel.ItemManasteelPick;

public class ItemGlassPick extends ItemManasteelPick {

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
		MinecraftForge.EVENT_BUS.addListener(this::onBlockDrops);
	}

	private void onBlockDrops(HarvestDropsEvent event) {
		if(event.getHarvester() != null && event.getState() != null && event.getDrops() != null && event.getDrops().isEmpty()
				&& !event.getHarvester().getHeldItemMainhand().isEmpty()
				&& event.getHarvester().getHeldItemMainhand().getItem() == this
				&& event.getState().getMaterial() == Material.GLASS
				&& event.getState().canSilkHarvest(event.getWorld().getWorld(), event.getPos(), event.getHarvester()))
			event.getDrops().add(new ItemStack(event.getState().getBlock()));
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
