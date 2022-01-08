/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.fabric.mixin;

import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BeaconBlockEntity;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import vazkii.botania.common.block.BlockBifrostPerm;
import vazkii.botania.common.block.ModBlocks;

@Mixin(BeaconBlockEntity.class)
public class MixinBeaconBlockEntity {
	@Unique
	private static boolean bifrost = false;

	@ModifyVariable(method = "tick", at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/world/level/block/state/BlockState;getBlock()Lnet/minecraft/world/level/block/Block;"))
	private static Block captureBifrost(Block obj) {
		bifrost = obj == ModBlocks.bifrostPerm;
		return obj;
	}

	@ModifyVariable(method = "tick", at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/world/item/DyeColor;getTextureDiffuseColors()[F"))
	private static float[] bifrostColor(float[] obj, Level level) {
		if (bifrost) {
			return BlockBifrostPerm.getBeaconColorMultiplier(level);
		}
		return obj;
	}
}
