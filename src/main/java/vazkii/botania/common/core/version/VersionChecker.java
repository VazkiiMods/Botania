/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [May 31, 2014, 9:58:26 PM (GMT)]
 */
package vazkii.botania.common.core.version;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import vazkii.botania.common.lib.LibMisc;

public final class VersionChecker {

	private VersionChecker() {}

	private static final int FLAVOUR_MESSAGES = 65;

	public static boolean doneChecking = false;
	public static String onlineVersion = "";
	public static boolean triedToWarnPlayer = false;

	public static boolean startedDownload = false;
	public static boolean downloadedFile = false;

	public static void init() {
		new ThreadVersionChecker();
		MinecraftForge.EVENT_BUS.register(VersionChecker.class);
	}

	@SubscribeEvent
	public static void onTick(ClientTickEvent event) {
		if(doneChecking && event.phase == Phase.END && Minecraft.getMinecraft().thePlayer != null && !triedToWarnPlayer) {
			if(!onlineVersion.isEmpty()) {
				EntityPlayer player = Minecraft.getMinecraft().thePlayer;
				int onlineBuild = Integer.parseInt(onlineVersion.split("-")[1]);
				int clientBuild = LibMisc.BUILD.contains("GRADLE") ? Integer.MIN_VALUE : Integer.parseInt(LibMisc.BUILD);
				if(onlineBuild > clientBuild) {
					player.addChatComponentMessage(new TextComponentTranslation("botania.versioning.flavour" + player.worldObj.rand.nextInt(FLAVOUR_MESSAGES)).setStyle(new Style().setColor(TextFormatting.LIGHT_PURPLE)));
					player.addChatComponentMessage(new TextComponentTranslation("botania.versioning.outdated", clientBuild, onlineBuild));

					ITextComponent component = ITextComponent.Serializer.jsonToComponent(I18n.format("botania.versioning.updateMessage", onlineVersion));
					player.addChatComponentMessage(component);
				}
			}

			triedToWarnPlayer = true;
		}
	}

}
