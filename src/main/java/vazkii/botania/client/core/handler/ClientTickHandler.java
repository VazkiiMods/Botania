/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Feb 3, 2014, 9:59:17 PM (GMT)]
 */
package vazkii.botania.client.core.handler;

import com.google.common.collect.ImmutableList;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.common.gameevent.TickEvent.RenderTickEvent;
import net.minecraftforge.fml.relauncher.Side;
import vazkii.botania.api.mana.IManaCollector;
import vazkii.botania.api.mana.TileSignature;
import vazkii.botania.client.gui.lexicon.GuiLexicon;
import vazkii.botania.common.block.subtile.functional.SubTileVinculotus;
import vazkii.botania.common.block.tile.corporea.TileCorporeaIndex;
import vazkii.botania.common.core.handler.ManaNetworkHandler;
import vazkii.botania.common.core.helper.PlayerHelper;
import vazkii.botania.common.item.ModItems;
import vazkii.botania.common.lib.LibMisc;

@Mod.EventBusSubscriber(value = Side.CLIENT, modid = LibMisc.MOD_ID)
public final class ClientTickHandler {

	private ClientTickHandler() {}

	public static int ticksWithLexicaOpen = 0;
	public static int pageFlipTicks = 0;
	public static int ticksInGame = 0;
	public static float partialTicks = 0;
	public static float delta = 0;
	public static float total = 0;

	private static void calcDelta() {
		float oldTotal = total;
		total = ticksInGame + partialTicks;
		delta = total - oldTotal;
	}

	@SubscribeEvent
	public static void renderTick(RenderTickEvent event) {
		if(event.phase == Phase.START)
			partialTicks = event.renderTickTime;
		else {
			calcDelta();
		}
	}

	@SubscribeEvent
	public static void clientTickEnd(ClientTickEvent event) {
		if(event.phase == Phase.END) {
			RedStringRenderer.tick();
			ItemsRemainingRenderHandler.tick();

			if(Minecraft.getMinecraft().world == null) {
				ManaNetworkHandler.instance.clear();
				SubTileVinculotus.existingFlowers.clear();
			}

			GuiScreen gui = Minecraft.getMinecraft().currentScreen;
			if(gui == null || !gui.doesGuiPauseGame()) {
				ticksInGame++;
				partialTicks = 0;

				EntityPlayer player = Minecraft.getMinecraft().player;
				if(player != null) {
					if(PlayerHelper.hasHeldItemClass(player, ModItems.twigWand)) {
						for(TileSignature sig : ImmutableList.copyOf(ManaNetworkHandler.instance.getAllCollectorsInWorld(Minecraft.getMinecraft().world))) {
							if(!sig.isRemote())
								continue;

							TileEntity tile = sig.getTile();
							if(tile instanceof IManaCollector)
								((IManaCollector) tile).onClientDisplayTick();
						}
					}
				}
			}

			int ticksToOpen = 10;
			if(gui instanceof GuiLexicon) {
				if(ticksWithLexicaOpen < 0)
					ticksWithLexicaOpen = 0;
				if(ticksWithLexicaOpen < ticksToOpen)
					ticksWithLexicaOpen++;
				if(pageFlipTicks > 0)
					pageFlipTicks--;
			} else {
				pageFlipTicks = 0;
				if(ticksWithLexicaOpen > 0) {
					if(ticksWithLexicaOpen > ticksToOpen)
						ticksWithLexicaOpen = ticksToOpen;
					ticksWithLexicaOpen--;
				}
			}

			calcDelta();
		}
	}

	public static void notifyPageChange() {
		if(pageFlipTicks == 0)
			pageFlipTicks = 5;
	}

}
