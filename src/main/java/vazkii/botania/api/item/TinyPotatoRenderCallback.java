/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.api.item;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

/**
 * Why would you ever want this ._.
 */
public interface TinyPotatoRenderCallback {
	Event<TinyPotatoRenderCallback> EVENT = EventFactory.createArrayBacked(TinyPotatoRenderCallback.class,
		listeners -> (be, n, td, ms, buf, light, ov) -> {
			for (TinyPotatoRenderCallback listener : listeners) {
				listener.onRender(be, n, td, ms, buf, light, ov);
			}
		});

	void onRender(BlockEntity potato, Text name, float tickDelta, MatrixStack ms, VertexConsumerProvider buffers, int light, int overlay);
}
