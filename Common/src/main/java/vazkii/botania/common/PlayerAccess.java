package vazkii.botania.common;

import net.minecraft.world.entity.LivingEntity;

import javax.annotation.Nullable;

/** Mixin duck interface for critical hit handling */
public interface PlayerAccess {
	void botania$setCritTarget(@Nullable LivingEntity entity);
}
