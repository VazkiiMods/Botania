/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Jan 14, 2014, 6:50:15 PM (GMT)]
 */
package vazkii.botania.common.network;

import net.minecraft.client.gui.inventory.GuiCrafting;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ContainerWorkbench;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;
import vazkii.botania.client.gui.bag.ContainerFlowerBag;
import vazkii.botania.client.gui.bag.GuiFlowerBag;
import vazkii.botania.client.gui.bag.InventoryFlowerBag;
import vazkii.botania.client.gui.box.ContainerBaubleBox;
import vazkii.botania.client.gui.box.GuiBaubleBox;
import vazkii.botania.client.gui.box.InventoryBaubleBox;
import vazkii.botania.client.gui.crafting.ContainerCraftingHalo;
import vazkii.botania.client.gui.lexicon.GuiLexicon;
import vazkii.botania.common.item.ModItems;
import vazkii.botania.common.lib.LibGuiIDs;

public class GuiHandler implements IGuiHandler {

	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int handId, int unused1, int unused2) {
		EnumHand hand = handId == 1 ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND;
		ItemStack stack = player.getHeldItem(hand);

		switch(ID) {
		case LibGuiIDs.CRAFTING_HALO :
			return new ContainerCraftingHalo(player.inventory, world);
		case LibGuiIDs.FLOWER_BAG :
			if(stack.getItem() == ModItems.flowerBag)
				return new ContainerFlowerBag(player.inventory, new InventoryFlowerBag(stack));
		case LibGuiIDs.BAUBLE_BOX :
			if(stack.getItem() == ModItems.baubleBox)
				return new ContainerBaubleBox(player, new InventoryBaubleBox(stack));
		}

		return null;
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int handId, int unused1, int unused2) {
		EnumHand hand = handId == 1 ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND;
		ItemStack stack = player.getHeldItem(hand);

		switch(ID) {
		case LibGuiIDs.LEXICON :
			return GuiLexicon.currentOpenLexicon;
		case LibGuiIDs.CRAFTING_HALO :
			return new GuiCrafting(player.inventory, world);
		case LibGuiIDs.FLOWER_BAG :
			if(stack.getItem() == ModItems.flowerBag)
				return new GuiFlowerBag(player.inventory, new InventoryFlowerBag(stack));
		case LibGuiIDs.BAUBLE_BOX :
			if(stack.getItem() == ModItems.baubleBox)
				return new GuiBaubleBox(player, new InventoryBaubleBox(stack));
		}

		return null;
	}

}
