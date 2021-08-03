/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.mixin;

import net.minecraft.client.renderer.RenderStateShard;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(RenderStateShard.class)
public interface AccessorRenderState {
	@Accessor("VIEW_OFFSET_Z_LAYERING")
	static RenderStateShard.LayeringStateShard getViewOffsetZLayer() {
		throw new IllegalStateException();
	}

	@Accessor("TRANSLUCENT_TRANSPARENCY")
	static RenderStateShard.TransparencyStateShard getTranslucentTransparency() {
		throw new IllegalStateException();
	}

	@Accessor("LIGHTNING_TRANSPARENCY")
	static RenderStateShard.TransparencyStateShard getLightningTransparency() {
		throw new IllegalStateException();
	}

	@Accessor("ITEM_ENTITY_TARGET")
	static RenderStateShard.OutputStateShard getItemEntityTarget() {
		throw new IllegalStateException();
	}
}
