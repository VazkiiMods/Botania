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

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import vazkii.botania.api.lexicon.ILexicon;
import vazkii.botania.client.gui.crafting.ContainerCraftingHalo;
import vazkii.botania.client.gui.crafting.GuiCraftingHalo;
import vazkii.botania.client.gui.lexicon.GuiLexicon;
import vazkii.botania.common.lib.LibGuiIDs;
import cpw.mods.fml.common.network.IGuiHandler;

public class GuiHandler implements IGuiHandler {

	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		switch(ID) {
		case LibGuiIDs.CRAFTING_HALO : {
			return new ContainerCraftingHalo(player.inventory, world);
		}
		}

		return null;
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		switch(ID) {
		case LibGuiIDs.LEXICON : {
			GuiLexicon lex = GuiLexicon.currentOpenLexicon;
			return lex;
		}
		case LibGuiIDs.CRAFTING_HALO : {
			return new GuiCraftingHalo(player.inventory, world);
		}
		}

		return null;
	}

}
