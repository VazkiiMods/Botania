/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.api.item;

import com.mojang.blaze3d.vertex.PoseStack;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.entity.BlockEntity;

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

	void onRender(BlockEntity potato, Component name, float tickDelta, PoseStack ms, MultiBufferSource buffers, int light, int overlay);
}
