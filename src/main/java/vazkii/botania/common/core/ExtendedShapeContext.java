package vazkii.botania.common.core;

import net.minecraft.block.ShapeContext;
import net.minecraft.entity.Entity;

import javax.annotation.Nullable;

public interface ExtendedShapeContext {
	@Nullable
	Entity getEntity();

	static Entity getEntity(ShapeContext ctx) {
		return ctx instanceof ExtendedShapeContext ? ((ExtendedShapeContext) ctx).getEntity() : null;
	}
}
