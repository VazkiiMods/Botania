/**
 * This class was created by <Flaxbeard>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under a
 * Creative Commons Attribution-NonCommercial-ShareAlike 3.0 License
 * (http://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB)
 * 
 * File Created @ [? (GMT)]
 */
package vazkii.botania.common.network;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufOutputStream;
import io.netty.buffer.Unpooled;

import java.io.IOException;

import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.entity.player.EntityPlayer;
import vazkii.botania.common.Botania;
import vazkii.botania.common.lib.LibMisc;
import cpw.mods.fml.common.network.internal.FMLProxyPacket;

public class BotaniaClientPacketHandler extends BotaniaServerPacketHandler {

	public static void sendClickPacket(EntityPlayer player) {
	//	System.out.println("GE1T");

		ByteBuf buf = Unpooled.buffer();
		ByteBufOutputStream out = new ByteBufOutputStream(buf);
		try
	    {
	    	out.writeInt(player.worldObj.provider.dimensionId);
	    	out.writeInt(player.getEntityId());
	    }
	    catch (IOException e) {}
	    FMLProxyPacket packet = new FMLProxyPacket(buf,LibMisc.MOD_ID);
	    Botania.channel.sendToServer(packet);
	    try {
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
