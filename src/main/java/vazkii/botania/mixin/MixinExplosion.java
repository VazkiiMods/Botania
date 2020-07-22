package vazkii.botania.mixin;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import vazkii.botania.common.item.equipment.bauble.ItemGoddessCharm;

import java.util.List;

@Mixin(Explosion.class)
public class MixinExplosion {
	@Shadow @Final private World world;

	@Shadow @Final private double x;

	@Shadow @Final private double y;

	@Shadow @Final private double z;

	@Shadow @Final private List<BlockPos> affectedBlocks;

	@Inject(method = "affectWorld", at = @At("HEAD"))
	private void onAffectWorld(boolean particles, CallbackInfo ci) {
		ItemGoddessCharm.onExplosion(world, new Vec3d(x, y, z), affectedBlocks);
	}
}
