/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.mixin;

import net.minecraft.client.renderer.RenderState;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(RenderState.class)
public interface AccessorRenderState {
	@Accessor("field_239235_M_")
	static RenderState.LayerState getViewOffsetZLayer() {
		throw new IllegalStateException();
	}

	@Accessor("TRANSLUCENT_TRANSPARENCY")
	static RenderState.TransparencyState getTranslucentTransparency() {
		throw new IllegalStateException();
	}

	@Accessor("LIGHTNING_TRANSPARENCY")
	static RenderState.TransparencyState getLightningTransparency() {
		throw new IllegalStateException();
	}

	@Accessor("field_241712_U_")
	static RenderState.TargetState getItemEntityTarget() {
		throw new IllegalStateException();
	}
}
