package vazkii.botania.mixin;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.tileentity.EndPortalTileEntityRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.List;

@Mixin(EndPortalTileEntityRenderer.class)
public interface AccessorEndPortalTileEntityRenderer {
	@Accessor("RENDER_TYPES")
	static List<RenderType> getLayers() {
		throw new IllegalStateException();
	}
}
