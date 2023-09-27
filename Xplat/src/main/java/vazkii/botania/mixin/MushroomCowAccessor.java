package vazkii.botania.mixin;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.animal.MushroomCow;

import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(MushroomCow.class)
public interface MushroomCowAccessor {
	@Accessor
	@Nullable
	MobEffect getEffect();

	@Accessor
	void setEffect(@Nullable MobEffect effect);

	@Accessor
	int getEffectDuration();

	@Accessor
	void setEffectDuration(int effectDuration);
}
