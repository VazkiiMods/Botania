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
import net.minecraftforge.common.ForgeVersion;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.ModContainer;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import vazkii.botania.common.lib.LibMisc;

public final class VersionChecker {

	private VersionChecker() {}

	private static final int FLAVOUR_MESSAGES = 65;

	public static boolean doneChecking = false;
	private static boolean triedToWarnPlayer = false;

	public static boolean startedDownload = false;
	public static boolean downloadedFile = false;

	public static void init() {
		MinecraftForge.EVENT_BUS.register(VersionChecker.class);
	}

	@SubscribeEvent
	public static void onTick(ClientTickEvent event) {
		if(!triedToWarnPlayer && event.phase == Phase.END && Minecraft.getMinecraft().thePlayer != null) {
			ModContainer botaniaContainer = Loader.instance().getIndexedModList().get(LibMisc.MOD_ID);
			ForgeVersion.CheckResult result = ForgeVersion.getResult(botaniaContainer);

			if(result.status != ForgeVersion.Status.PENDING)
				triedToWarnPlayer = true;

			if(result.status == ForgeVersion.Status.OUTDATED) {
				EntityPlayer player = Minecraft.getMinecraft().thePlayer;
				player.addChatComponentMessage(new TextComponentTranslation("botania.versioning.flavour" + player.worldObj.rand.nextInt(FLAVOUR_MESSAGES)).setStyle(new Style().setColor(TextFormatting.LIGHT_PURPLE)));
				player.addChatComponentMessage(new TextComponentTranslation("botania.versioning.outdated", botaniaContainer.getVersion(), result.target));

				ITextComponent component = ITextComponent.Serializer.jsonToComponent(I18n.format("botania.versioning.updateMessage", result.target));
				player.addChatComponentMessage(component);
			}
		}
	}

}
