/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.List;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.block.entity.EndPortalBlockEntityRenderer;

@Mixin(EndPortalBlockEntityRenderer.class)
public interface AccessorEndPortalTileEntityRenderer {
	@Accessor("field_21732")
	static List<RenderLayer> getLayers() {
		throw new IllegalStateException();
	}
}
