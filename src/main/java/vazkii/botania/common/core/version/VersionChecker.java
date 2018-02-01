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
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.relauncher.Side;
import vazkii.botania.common.lib.LibMisc;

@Mod.EventBusSubscriber(value = Side.CLIENT, modid = LibMisc.MOD_ID)
public final class VersionChecker {

	private VersionChecker() {}

	private static final int FLAVOUR_MESSAGES = 65;

	public static volatile boolean doneChecking = false;
	public static volatile String onlineVersion = "";
	private static boolean triedToWarnPlayer = false;

	public static volatile boolean startedDownload = false;
	public static volatile boolean downloadedFile = false;

	public static void init() {
		new ThreadVersionChecker();
	}

	@SubscribeEvent
	public static void onTick(ClientTickEvent event) {
		if(event.phase == Phase.END && Minecraft.getMinecraft().player != null && !triedToWarnPlayer && doneChecking) {
			if(!onlineVersion.isEmpty()) {
				EntityPlayer player = Minecraft.getMinecraft().player;
				int onlineBuild = Integer.parseInt(onlineVersion.split("-")[1]);
				int clientBuild = LibMisc.BUILD.contains("GRADLE") ? Integer.MAX_VALUE : Integer.parseInt(LibMisc.BUILD);
				if(onlineBuild > clientBuild) {
					player.sendMessage(new TextComponentTranslation("botania.versioning.flavour" + player.world.rand.nextInt(FLAVOUR_MESSAGES)).setStyle(new Style().setColor(TextFormatting.LIGHT_PURPLE)));
					player.sendMessage(new TextComponentTranslation("botania.versioning.outdated", clientBuild, onlineBuild));

					ITextComponent component = ITextComponent.Serializer.fromJsonLenient(I18n.translateToLocal("botania.versioning.updateMessage").replaceAll("%version%", onlineVersion));
					player.sendMessage(component);
				}
			}

			triedToWarnPlayer = true;
		}
	}

}
