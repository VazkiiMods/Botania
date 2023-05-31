package vazkii.botania.mixin;

import com.google.common.collect.ImmutableList;

import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.inventory.LoomMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BannerPattern;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import vazkii.botania.common.item.ItemWithBannerPattern;

import java.util.List;

@Mixin(LoomMenu.class)
public class LoomMenuMixin {
	@Inject(at = @At("HEAD"), method = "getSelectablePatterns(Lnet/minecraft/world/item/ItemStack;)Ljava/util/List;", cancellable = true)
	private void handleBotaniaPatternItems(ItemStack stack, CallbackInfoReturnable<List<Holder<BannerPattern>>> cir) {
		if (stack.getItem() instanceof ItemWithBannerPattern p) {
			cir.setReturnValue(BuiltInRegistries.BANNER_PATTERN.getTag(p.getBannerPattern())
					.map(ImmutableList::copyOf).orElse(ImmutableList.of()));
		}
	}

	// todo also mixin into quickMoveStack to support shift clicking of our banner pattern items
}
