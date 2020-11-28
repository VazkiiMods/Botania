/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.mixin;

import net.minecraft.client.render.RenderPhase;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(RenderPhase.class)
public interface AccessorRenderState {
	@Accessor("VIEW_OFFSET_Z_LAYERING")
	static RenderPhase.Layering getViewOffsetZLayer() {
		throw new IllegalStateException();
	}

	@Accessor("TRANSLUCENT_TRANSPARENCY")
	static RenderPhase.Transparency getTranslucentTransparency() {
		throw new IllegalStateException();
	}

	@Accessor("LIGHTNING_TRANSPARENCY")
	static RenderPhase.Transparency getLightningTransparency() {
		throw new IllegalStateException();
	}

	@Accessor("ITEM_TARGET")
	static RenderPhase.Target getItemEntityTarget() {
		throw new IllegalStateException();
	}
}
