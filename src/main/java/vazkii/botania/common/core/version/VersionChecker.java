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
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.StatCollector;
import vazkii.botania.common.lib.LibMisc;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent.ClientTickEvent;
import cpw.mods.fml.common.gameevent.TickEvent.Phase;

public final class VersionChecker {

	private static final int FLAVOUR_MESSAGES = 65;

	public static boolean doneChecking = false;
	public static String onlineVersion = "";
	public static boolean triedToWarnPlayer = false;

	public static boolean startedDownload = false;
	public static boolean downloadedFile = false;

	public void init() {
		new ThreadVersionChecker();
		FMLCommonHandler.instance().bus().register(this);
	}

	@SubscribeEvent
	public void onTick(ClientTickEvent event) {
		if(doneChecking && event.phase == Phase.END && Minecraft.getMinecraft().thePlayer != null && !triedToWarnPlayer) {
			if(!onlineVersion.isEmpty()) {
				EntityPlayer player = Minecraft.getMinecraft().thePlayer;
				int onlineBuild = Integer.parseInt(onlineVersion.split("-")[1]);
				int clientBuild = LibMisc.BUILD.contains("GRADLE") ? Integer.MAX_VALUE : Integer.parseInt(LibMisc.BUILD);
				if(onlineBuild > clientBuild) {
					player.addChatComponentMessage(new ChatComponentTranslation("botania.versioning.flavour" + player.worldObj.rand.nextInt(FLAVOUR_MESSAGES)).setChatStyle(new ChatStyle().setColor(EnumChatFormatting.LIGHT_PURPLE)));
					player.addChatComponentMessage(new ChatComponentTranslation("botania.versioning.outdated", clientBuild, onlineBuild));

					IChatComponent component = IChatComponent.Serializer.func_150699_a(StatCollector.translateToLocal("botania.versioning.updateMessage").replaceAll("%version%", onlineVersion));
					player.addChatComponentMessage(component);
				}
			}

			triedToWarnPlayer = true;
		}
	}

}
