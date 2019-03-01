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
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.items.IItemHandler;
import vazkii.botania.common.core.handler.ModSounds;
import vazkii.botania.common.core.helper.ItemNBTHelper;
import vazkii.botania.common.item.ModItems;
import vazkii.botania.common.item.equipment.bauble.ItemDodgeRing;
import vazkii.botania.common.item.equipment.tool.terrasteel.ItemTerraSword;

import java.util.function.Supplier;

public class PacketDodge {
	public static void encode(PacketDodge msg, PacketBuffer buf) {}

	public static PacketDodge decode(PacketBuffer buf) {
		return new PacketDodge();
	}

	public static void handle(PacketDodge msg, Supplier<NetworkEvent.Context> ctx) {
		ctx.get().enqueueWork(() -> {
			EntityPlayerMP player = ctx.get().getSender();
			player.world.playSound(null, player.posX, player.posY, player.posZ, ModSounds.dash, SoundCategory.PLAYERS, 1F, 1F);

			IItemHandler baublesInv = BaublesApi.getBaublesHandler(player);
			int slot = BaublesApi.isBaubleEquipped(player, ModItems.dodgeRing);
			if(slot < 0) {
				ctx.get().getSender().connection.disconnect(new TextComponentTranslation("botaniamisc.invalidDodge"));
				return;
			}
			ItemStack ringStack = baublesInv.getStackInSlot(slot);
			player.addExhaustion(0.3F);
			ItemNBTHelper.setInt(ringStack, ItemDodgeRing.TAG_DODGE_COOLDOWN, ItemDodgeRing.MAX_CD);
		});
		ctx.get().setPacketHandled(true);
	}
}
