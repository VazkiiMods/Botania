package vazkii.botania.mixin;

import net.minecraft.world.entity.projectile.Projectile;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.UUID;

@Mixin(Projectile.class)
public interface ProjectileAccessor {
	@Accessor("leftOwner")
	boolean botania_getLeftOwner();

	@Accessor("ownerUUID")
	UUID botania_getOwnerUUID();

	@Accessor("ownerUUID")
	void botania_setOwnerUUID(UUID ownerUUID);
}
