package vazkii.botania.mixin;

import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.network.ServerPlayerInteractionManager;
import net.minecraft.util.math.BlockPos;
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

@Mixin(ServerPlayerInteractionManager.class)
public class MixinServerPlayerInteractionManager {
	@Shadow public ServerPlayerEntity player;

	@Inject(method = "tryBreakBlock", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/Block;onBreak(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;Lnet/minecraft/entity/player/PlayerEntity;)V"))
	private void onStartBreak(BlockPos pos, CallbackInfoReturnable<Boolean> cir) {
		ServerPlayerEntity player = this.player;
		ItemStack stack = player.getMainHandStack();
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
