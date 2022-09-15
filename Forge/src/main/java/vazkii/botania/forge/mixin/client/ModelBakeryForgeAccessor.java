package vazkii.botania.forge.mixin.client;

import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.server.packs.resources.ResourceManager;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ModelBakery.class)
public interface ModelBakeryForgeAccessor {
	@Accessor("resourceManager")
	ResourceManager getResourceManager();
}
