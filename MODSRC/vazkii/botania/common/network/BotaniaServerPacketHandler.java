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

import io.netty.buffer.ByteBufInputStream;

import java.io.IOException;

import vazkii.botania.common.item.ItemGravityRod;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.network.FMLNetworkEvent.ServerCustomPacketEvent;

public class BotaniaServerPacketHandler {
	
	@SubscribeEvent
	public void onServerPacket(ServerCustomPacketEvent event) {
	//	System.out.println("GET");
		ByteBufInputStream bbis = new ByteBufInputStream(event.packet.payload());
        int dimension;

        try
        {
        	dimension = bbis.readInt();
            World world = DimensionManager.getWorld(dimension);
            int playerID = bbis.readInt();
            EntityPlayer player = (EntityPlayer) world.getEntityByID(playerID);
            ItemGravityRod.leftClick(player);
            bbis.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
            return;
        }
	}
}
