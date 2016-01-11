package vazkii.botania.common.core.handler;

import java.util.Iterator;

import vazkii.botania.common.item.equipment.bauble.ItemMagnetRing;
import vazkii.botania.common.lib.LibMisc;

import com.google.common.collect.ImmutableList;

import net.minecraftforge.fml.common.event.FMLInterModComms.IMCMessage;

public final class IMCHandler {

	public static void processMessages(ImmutableList<IMCMessage> messageList) {
		for (IMCMessage message : messageList) {
			if (message != null && message.key != null && message.key.equals(LibMisc.BLACKLIST_ITEM) && message.isStringMessage()) {
				String value = message.getStringValue();
				ItemMagnetRing.addItemToBlackList(value);
			}
		}
	}
}
