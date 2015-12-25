/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [25/12/2015, 02:22:32 (GMT)]
 */
package vazkii.botania.client.integration.nei;

import vazkii.botania.api.corporea.CorporeaHelper;
import vazkii.botania.common.block.tile.corporea.TileCorporeaIndex;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import codechicken.nei.LayoutManager;
import codechicken.nei.NEIClientConfig;
import codechicken.nei.guihook.GuiContainerManager;
import codechicken.nei.guihook.IContainerInputHandler;

public class NEIInputHandler implements IContainerInputHandler {

	@Override
	public boolean keyTyped(GuiContainer gui, char c, int i) {
		Minecraft mc = Minecraft.getMinecraft();
		if(TileCorporeaIndex.InputHandler.getNearbyIndexes(mc.thePlayer).isEmpty())
			return false;
		
		int bind = NEIClientConfig.getKeyBinding(NEIBotaniaConfig.CORPOREA_KEY);
		
		if(i == bind) {
		    LayoutManager layoutManager = LayoutManager.instance();
		    if(layoutManager != null && LayoutManager.itemPanel != null && !NEIClientConfig.isHidden()) {
		    	ItemStack stack = GuiContainerManager.getStackMouseOver(gui);
		    	if(stack != null && stack.getItem() != null) {
		    		int count = 1;
		    		int max = stack.getMaxStackSize();
		    		if(gui.isShiftKeyDown()) {
		    			count = max;
		    			if(gui.isCtrlKeyDown())
		    				count /= 4;
		    		} else if(gui.isCtrlKeyDown())
		    			count = max / 2;
		    		
		    		if(count > 0) {
		    			String name = CorporeaHelper.stripControlCodes(stack.getDisplayName());
		    			String full = count + " " + name;
		    			
		    			mc.ingameGUI.getChatGUI().addToSentMessages(full);
						mc.thePlayer.sendChatMessage(full);
		    			return true;
		    		}
		    	}
		    }
		}
		
		return false;
	}

	@Override
	public boolean lastKeyTyped(GuiContainer arg0, char arg1, int arg2) {
		return false;
	}

	@Override
	public boolean mouseClicked(GuiContainer arg0, int arg1, int arg2, int arg3) {
		return false;
	}

	@Override
	public boolean mouseScrolled(GuiContainer arg0, int arg1, int arg2, int arg3) {
		return false;
	}

	@Override
	public void onKeyTyped(GuiContainer arg0, char arg1, int arg2) {
		// NO-OP
	}

	@Override
	public void onMouseClicked(GuiContainer arg0, int arg1, int arg2, int arg3) {
	}

	@Override
	public void onMouseDragged(GuiContainer arg0, int arg1, int arg2, int arg3, long arg4) {
		// NO-OP
	}

	@Override
	public void onMouseScrolled(GuiContainer arg0, int arg1, int arg2, int arg3) {
		// NO-OP
	}

	@Override
	public void onMouseUp(GuiContainer arg0, int arg1, int arg2, int arg3) {
		// NO-OP
	}

}
