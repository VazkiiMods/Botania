/**
 * This class was created by <The TConstruct Team>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under a
 * Creative Commons Attribution-NonCommercial-ShareAlike 3.0 License
 * (http://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB)
 * 
 * File Created @ [? (GMT)]
 */
package vazkii.botania.client.core.handler;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.ThreadDownloadImageData;
import net.minecraftforge.client.event.RenderPlayerEvent;
import vazkii.botania.client.lib.LibResources;
import vazkii.botania.common.Botania;
import vazkii.botania.common.lib.LibObfuscation;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.ReflectionHelper;

public class CapeHandler {

	private ArrayList<AbstractClientPlayer> capePlayers = new ArrayList<AbstractClientPlayer>();

	public static CapeHandler instance;

	public CapeHandler() {
		instance = this;
	}

	@SubscribeEvent
	public void onPreRenderSpecials (RenderPlayerEvent.Specials.Pre event) {
		if(Loader.isModLoaded("shadersmod"))
			return;

		if(event.entityPlayer instanceof AbstractClientPlayer && event.entityPlayer.getCommandSenderName().equals("Vazkii")) {
			AbstractClientPlayer abstractClientPlayer = (AbstractClientPlayer) event.entityPlayer;

			if (!capePlayers.contains(abstractClientPlayer)) {
				ReflectionHelper.setPrivateValue(ThreadDownloadImageData.class, abstractClientPlayer.getTextureCape(), false, LibObfuscation.TEXTURE_UPLOADED);
				new Thread(new CloakThread(abstractClientPlayer)).start();
				event.renderCape = true;
			}
		}
	}

	private class CloakThread implements Runnable {

		AbstractClientPlayer abstractClientPlayer;

		public CloakThread(AbstractClientPlayer player) {
			abstractClientPlayer = player;
		}

		@Override
		public void run() {
			try {
				BufferedImage bo = ImageIO.read(Botania.class.getResourceAsStream(LibResources.MODEL_CAPE));
				ReflectionHelper.setPrivateValue(ThreadDownloadImageData.class, abstractClientPlayer.getTextureCape(), bo, LibObfuscation.BUFFERED_IMAGE);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void refreshCapes() {
		capePlayers.clear();
	}
}