package vazkii.botania.mixin;

import net.minecraft.world.item.ItemStack;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import vazkii.botania.common.item.ItemWithBannerPattern;

@Mixin(targets = "net.minecraft.world.inventory.LoomMenu$5")
public class MixinLoomMenuPatternSlot {
	@Inject(at = @At("HEAD"), method = "mayPlace", cancellable = true)
	private void handleBotaniaPatterns(ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
		if (stack.getItem() instanceof ItemWithBannerPattern) {
			cir.setReturnValue(true);
		}
	}
}
