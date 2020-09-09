package vazkii.botania.mixin;

import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Map;

@Mixin(EntityRenderDispatcher.class)
public interface AccessorEntityRenderDispatcher {
	@Accessor
	Map<String, PlayerEntityRenderer> getModelRenderers();
}
