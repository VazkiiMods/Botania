/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [22/10/2016, 18:18:19 (GMT)]
 */
package vazkii.botania.common.network;

import baubles.api.BaublesApi;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.items.IItemHandler;
import vazkii.botania.common.item.ModItems;
import vazkii.botania.common.item.equipment.bauble.CloudPendantShim;
import vazkii.botania.common.item.equipment.bauble.ItemTravelBelt;
import vazkii.botania.common.item.equipment.tool.terrasteel.ItemTerraSword;

import java.util.function.Supplier;

public class PacketJump {
	public static void encode(PacketJump msg, PacketBuffer buf) {}

	public static PacketJump decode(PacketBuffer buf) {
		return new PacketJump();
	}

	public static void handle(PacketJump msg, Supplier<NetworkEvent.Context> ctx) {
		ctx.get().enqueueWork(() -> {
			EntityPlayerMP player = ctx.get().getSender();
			IItemHandler baublesInv = BaublesApi.getBaublesHandler(player);
			ItemStack amuletStack = baublesInv.getStackInSlot(0);

			if(!amuletStack.isEmpty() && amuletStack.getItem() instanceof CloudPendantShim) {
				player.addExhaustion(0.3F);
				player.fallDistance = 0;

				ItemStack belt = BaublesApi.getBaublesHandler(player).getStackInSlot(3);

				if(!belt.isEmpty() && belt.getItem() instanceof ItemTravelBelt)
					player.fallDistance = -((ItemTravelBelt) belt.getItem()).fallBuffer * ((CloudPendantShim) amuletStack.getItem()).getMaxAllowedJumps();
			}
		});
		ctx.get().setPacketHandled(true);
	}
}
