/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.mixin;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.shapes.EntityCollisionContext;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import vazkii.botania.common.core.ExtendedShapeContext;

import javax.annotation.Nullable;

@Mixin(EntityCollisionContext.class)
public class MixinEntityShapeContext implements ExtendedShapeContext {
	@Nullable
	@Unique
	private Entity entity;

	@Inject(at = @At("RETURN"), method = "<init>(Lnet/minecraft/world/entity/Entity;)V")
	private void captureEntity(Entity entity, CallbackInfo ci) {
		this.entity = entity;
	}

	@Nullable
	@Override
	public Entity botania_getEntity() {
		return entity;
	}
}
