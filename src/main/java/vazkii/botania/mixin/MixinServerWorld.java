package vazkii.botania.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import vazkii.botania.common.Botania;
import vazkii.botania.common.block.BlockGhostRail;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.world.SkyblockWorldEvents;

@Mixin(ServerWorld.class)
public class MixinServerWorld {
	@Inject(at = @At("RETURN"), method = "loadEntityUnchecked")
	private void onEntityAdd(Entity entity, CallbackInfo ci) {
		if (Botania.gardenOfGlassLoaded) {
			SkyblockWorldEvents.syncGogStatus(entity);
		}
		((BlockGhostRail) ModBlocks.ghostRail).cartSpawn(entity);
	}
}
