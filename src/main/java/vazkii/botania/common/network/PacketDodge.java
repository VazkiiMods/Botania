/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [22/09/2016, 13:06:44 (GMT)]
 */
package vazkii.botania.common.network;

import baubles.api.BaublesApi;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.items.IItemHandler;
import vazkii.botania.common.core.handler.ModSounds;
import vazkii.botania.common.core.helper.ItemNBTHelper;
import vazkii.botania.common.item.ModItems;
import vazkii.botania.common.item.equipment.bauble.ItemDodgeRing;

public class PacketDodge implements IMessage {

	@Override
	public void fromBytes(ByteBuf buf) {}

	@Override
	public void toBytes(ByteBuf buf) {}

	public static class Handler implements IMessageHandler<PacketDodge, IMessage> {

		@Override
		public IMessage onMessage(PacketDodge message, MessageContext ctx) {
			EntityPlayerMP player = ctx.getServerHandler().player;
			player.server.addScheduledTask(() -> {
				player.world.playSound(null, player.posX, player.posY, player.posZ, ModSounds.dash, SoundCategory.PLAYERS, 1F, 1F);
				IItemHandler baublesInv = BaublesApi.getBaublesHandler(player);
				int slot = BaublesApi.isBaubleEquipped(player, ModItems.dodgeRing);
				if(slot < 0) {
					ctx.getServerHandler().disconnect(new TextComponentTranslation("botaniamisc.invalidDodge"));
					return;
				}
				ItemStack ringStack = baublesInv.getStackInSlot(slot);
				player.addExhaustion(0.3F);
				ItemNBTHelper.setInt(ringStack, ItemDodgeRing.TAG_DODGE_COOLDOWN, ItemDodgeRing.MAX_CD);
			});
			return null;
		}
	}

}
