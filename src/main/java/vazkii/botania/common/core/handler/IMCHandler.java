package vazkii.botania.common.core.handler;

import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;

public final class IMCHandler {
	public static void handle(InterModProcessEvent evt) {
		evt.getIMCStream().forEach(message -> {
		});
	}
}
