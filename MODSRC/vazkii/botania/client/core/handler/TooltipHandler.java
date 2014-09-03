/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under a
 * Creative Commons Attribution-NonCommercial-ShareAlike 3.0 License
 * (http://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB)
 * 
 * File Created @ [Jul 26, 2014, 2:33:04 AM (GMT)]
 */
package vazkii.botania.client.core.handler;

import vazkii.botania.common.core.helper.ItemNBTHelper;
import vazkii.botania.common.item.ItemRegenIvy;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.util.StatCollector;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public final class TooltipHandler {

	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public void onTooltipEvent(ItemTooltipEvent event) {
		if(event.itemStack.getItem() == Item.getItemFromBlock(Blocks.dirt) && event.itemStack.getItemDamage() == 1) {
			event.toolTip.add(StatCollector.translateToLocal("botaniamisc.coarseDirt0"));
			event.toolTip.add(StatCollector.translateToLocal("botaniamisc.coarseDirt1"));
		} else if(event.itemStack.getItem() == Item.getItemFromBlock(Blocks.mob_spawner) && event.entityPlayer.capabilities.isCreativeMode)
			event.toolTip.add(StatCollector.translateToLocal("botaniamisc.spawnerTip"));
		
		if(ItemNBTHelper.getBoolean(event.itemStack, ItemRegenIvy.TAG_REGEN, false))
			event.toolTip.add(StatCollector.translateToLocal("botaniamisc.hasIvy"));
	}

}
