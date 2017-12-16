/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Jul 26, 2014, 2:33:04 AM (GMT)]
 */
package vazkii.botania.client.core.handler;

import net.minecraft.client.resources.I18n;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import vazkii.botania.common.core.helper.ItemNBTHelper;
import vazkii.botania.common.item.ItemKeepIvy;
import vazkii.botania.common.lib.LibMisc;

@Mod.EventBusSubscriber(value = Side.CLIENT, modid = LibMisc.MOD_ID)
public final class TooltipHandler {

	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public static void onTooltipEvent(ItemTooltipEvent event) {
		if(event.getItemStack().hasTagCompound() && ItemNBTHelper.getBoolean(event.getItemStack(), ItemKeepIvy.TAG_KEEP, false))
			event.getToolTip().add(I18n.format("botaniamisc.hasKeepIvy"));
	}

}
