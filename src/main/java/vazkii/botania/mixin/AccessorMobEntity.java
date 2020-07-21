/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.mixin;

import net.minecraft.entity.MobEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

import javax.annotation.Nullable;

@Mixin(MobEntity.class)
public interface AccessorMobEntity {
	@Nullable
	@Invoker
	SoundEvent callGetAmbientSound();

	@Accessor
	void setDeathLootTable(ResourceLocation id);
}
