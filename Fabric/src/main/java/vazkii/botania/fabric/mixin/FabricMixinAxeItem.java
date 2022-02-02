/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.fabric.mixin;

import net.minecraft.world.item.AxeItem;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import vazkii.botania.fabric.xplat.FabricXplatImpl;

import java.util.Optional;

@Mixin(AxeItem.class)
public class FabricMixinAxeItem {
	/** Stripping blocks without an axis property is not doable through FAPI, so we do this. */
	@Inject(method = "getStripped", at = @At("RETURN"), cancellable = true)
	private void stripBlocksWithoutAxis(BlockState state, CallbackInfoReturnable<Optional<BlockState>> cir) {
		if (cir.getReturnValue().isEmpty()) {
			Block block = FabricXplatImpl.CUSTOM_STRIPPING.get(state.getBlock());
			if (block != null) {
				cir.setReturnValue(Optional.of(block.withPropertiesOf(state)));
			}
		}
	}
}
