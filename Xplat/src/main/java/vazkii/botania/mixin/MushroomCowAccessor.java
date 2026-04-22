package vazkii.botania.mixin;

import net.minecraft.world.entity.animal.MushroomCow;
import net.minecraft.world.item.component.SuspiciousStewEffects;

import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(MushroomCow.class)
public interface MushroomCowAccessor {
	@Nullable
	@Accessor("stewEffects")
	SuspiciousStewEffects botania_getStewEffects();

	@Accessor("stewEffects")
	void botania_setStewEffects(@Nullable SuspiciousStewEffects stewEffects);
}
