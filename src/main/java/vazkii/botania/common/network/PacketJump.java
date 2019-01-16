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
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.items.IItemHandler;
import vazkii.botania.common.item.equipment.bauble.CloudPendantShim;
import vazkii.botania.common.item.equipment.bauble.ItemTravelBelt;

public class PacketJump implements IMessage {

	@Override
	public void fromBytes(ByteBuf buf) {}

	@Override
	public void toBytes(ByteBuf buf) {}

	public static class Handler implements IMessageHandler<PacketJump, IMessage> {

		@Override
		public IMessage onMessage(PacketJump message, MessageContext ctx) {
			EntityPlayerMP player = ctx.getServerHandler().player;
			player.server.addScheduledTask(() -> {
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
			return null;
		}
	}

}
