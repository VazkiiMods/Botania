/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under a
 * Creative Commons Attribution-NonCommercial-ShareAlike 3.0 License
 * (http://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB)
 * 
 * File Created @ [Apr 30, 2014, 11:15:16 PM (GMT)]
 */
package vazkii.botania.test;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.Random;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.client.renderer.ThreadDownloadImageData;
import vazkii.botania.common.lib.LibObfuscation;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent.ClientTickEvent;
import cpw.mods.fml.common.gameevent.TickEvent.Phase;
import cpw.mods.fml.relauncher.ReflectionHelper;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class Corruption {

	Random rand = new Random();

	public static void init() {
		FMLCommonHandler.instance().bus().register(new Corruption());
	}

	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public void onPlayerTick(ClientTickEvent event) {
		if(event.phase == Phase.END) {
			EntityClientPlayerMP player = Minecraft.getMinecraft().thePlayer;
			if(player != null && player.ticksExisted > 300) {
				ThreadDownloadImageData tex = player.getTextureSkin();
				BufferedImage img = ReflectionHelper.getPrivateValue(ThreadDownloadImageData.class, tex, LibObfuscation.BUFFERED_IMAGE);
				if(img != null)
					for(int i = 0; i < 1 + (player.ticksExisted - 300) / 30; i++)
						corruptRandomPixel(tex, img);
			}
		}
	}

	public void corruptRandomPixel(ThreadDownloadImageData tex, BufferedImage img) {
		int width = img.getWidth();
		int height = img.getHeight();

		int x = rand.nextInt(width);
		int y = rand.nextInt(height);
		Color color = new Color(img.getRGB(x, y));
		if(color.getRed() + color.getGreen() + color.getRed() > 0)
			img.setRGB(x, y, color.darker().darker().getRGB());
		ReflectionHelper.setPrivateValue(ThreadDownloadImageData.class, tex, false, LibObfuscation.TEXTURE_UPLOADED);
	}

}
