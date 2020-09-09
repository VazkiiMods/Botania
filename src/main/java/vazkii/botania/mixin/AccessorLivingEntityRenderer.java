package vazkii.botania.mixin;

import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(LivingEntityRenderer.class)
public interface AccessorLivingEntityRenderer {
	@Invoker
	<T extends LivingEntity, M extends EntityModel<T>> boolean callAddFeature(FeatureRenderer<T, M> feature);
}
