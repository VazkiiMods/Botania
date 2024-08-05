package vazkii.botania.mixin;

import net.minecraft.world.entity.ExperienceOrb;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ExperienceOrb.class)
public interface ExperienceOrbAccessor {
	@Accessor("count")
	int botania_getCount();

	@Accessor("count")
	void botania_setCount(int c);
}
