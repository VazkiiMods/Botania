/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.core;

import net.minecraft.block.ShapeContext;
import net.minecraft.entity.Entity;

import javax.annotation.Nullable;

public interface ExtendedShapeContext {
	@Nullable
	Entity botania_getEntity();

	static Entity getEntity(ShapeContext ctx) {
		return ctx instanceof ExtendedShapeContext ? ((ExtendedShapeContext) ctx).botania_getEntity() : null;
	}
}
