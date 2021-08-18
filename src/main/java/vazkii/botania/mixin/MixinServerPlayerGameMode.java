/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.ServerPlayerGameMode;
import net.minecraft.world.item.ItemStack;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import vazkii.botania.common.item.ModItems;
import vazkii.botania.common.item.equipment.tool.ItemGlassPick;
import vazkii.botania.common.item.equipment.tool.elementium.ItemElementiumShovel;
import vazkii.botania.common.item.equipment.tool.terrasteel.ItemTerraAxe;
import vazkii.botania.common.item.equipment.tool.terrasteel.ItemTerraPick;

@Mixin(ServerPlayerGameMode.class)
public class MixinServerPlayerGameMode {
	@Shadow
	@Final
	protected ServerPlayer player;

	@Inject(method = "destroyBlock", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/Block;playerWillDestroy(Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/entity/player/Player;)V"))
	private void onStartBreak(BlockPos pos, CallbackInfoReturnable<Boolean> cir) {
		ServerPlayer player = this.player;
		ItemStack stack = player.getMainHandItem();
		if (stack.getItem() == ModItems.terraAxe) {
			((ItemTerraAxe) ModItems.terraAxe).onBlockStartBreak(stack, pos, player);
		} else if (stack.getItem() == ModItems.terraPick) {
			((ItemTerraPick) ModItems.terraPick).onBlockStartBreak(stack, pos, player);
		} else if (stack.getItem() == ModItems.elementiumShovel) {
			((ItemElementiumShovel) ModItems.elementiumShovel).onBlockStartBreak(stack, pos, player);
		} else if (stack.getItem() == ModItems.glassPick) {
			((ItemGlassPick) ModItems.glassPick).onBlockStartBreak(stack, pos, player);
		}
	}
}
