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

import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.event.world.BlockEvent.HarvestDropsEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import vazkii.botania.common.item.equipment.tool.manasteel.ItemManasteelPick;
import vazkii.botania.common.lib.LibItemNames;

import javax.annotation.Nonnull;

public class ItemGlassPick extends ItemManasteelPick {

	private static final int MANA_PER_DAMAGE = 160;
	private static final ToolMaterial MATERIAL = EnumHelper.addToolMaterial("MANASTEEL_GLASS", 0, 125, 4.8F, 0F, 10);

	public ItemGlassPick() {
		super(MATERIAL, LibItemNames.GLASS_PICK);
		MinecraftForge.EVENT_BUS.register(this);
		attackSpeed = -1;
	}

	@SubscribeEvent
	public void onBlockDrops(HarvestDropsEvent event) {
		if(event.getHarvester() != null && event.getState() != null && event.getDrops() != null && event.getDrops().isEmpty() && !event.getHarvester().getHeldItemMainhand().isEmpty() && event.getHarvester().getHeldItemMainhand().getItem() == this && event.getState().getMaterial() == Material.GLASS && event.getState().getBlock().canSilkHarvest(event.getWorld(), event.getPos(), event.getState(), event.getHarvester()))
			event.getDrops().add(new ItemStack(event.getState().getBlock(), 1, event.getState().getBlock().getMetaFromState(event.getState())));
	}

	@Override
	public int getManaPerDmg() {
		return MANA_PER_DAMAGE;
	}

	@Override
	public boolean getIsRepairable(ItemStack par1ItemStack, @Nonnull ItemStack par2ItemStack) {
		return par2ItemStack.getItem() == Item.getItemFromBlock(Blocks.GLASS) ? true : super.getIsRepairable(par1ItemStack, par2ItemStack);
	}

	@Override
	public int getSortingPriority(ItemStack stack) {
		return 0;
	}

}
