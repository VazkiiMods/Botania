/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.mixin;

import net.minecraft.world.item.AxeItem;
import net.minecraft.world.level.block.state.BlockState;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import vazkii.botania.common.block.ModBlocks;

import java.util.Optional;

@Mixin(AxeItem.class)
public class MixinAxeItem {
	/** Stripping blocks without an axis property is not doable through FAPI, so we do this. */
	@Inject(method = "getStripped", at = @At("RETURN"), cancellable = true)
	private void stripBlocksWithoutAxis(BlockState state, CallbackInfoReturnable<Optional<BlockState>> cir) {
		if (cir.getReturnValue().isEmpty()) {
			BlockState stripped = ModBlocks.getStrippedBlock(state);
			if (stripped != null) {
				cir.setReturnValue(Optional.of(stripped));
			}
		}
	}
}
