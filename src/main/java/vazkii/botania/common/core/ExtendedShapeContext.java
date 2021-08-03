/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.core;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.shapes.CollisionContext;

import javax.annotation.Nullable;

public interface ExtendedShapeContext {
	@Nullable
	Entity botania_getEntity();

	static Entity getEntity(CollisionContext ctx) {
		return ctx instanceof ExtendedShapeContext ? ((ExtendedShapeContext) ctx).botania_getEntity() : null;
	}
}
