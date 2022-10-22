package vazkii.botania.fabric.mixin;

import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.crafting.RepairItemRecipe;
import net.minecraft.world.level.Level;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import vazkii.botania.common.item.BotaniaItems;

@Mixin(RepairItemRecipe.class)
public class RepairItemRecipeFabricMixin {
	@Inject(
		at = @At("RETURN"),
		method = "matches(Lnet/minecraft/world/inventory/CraftingContainer;Lnet/minecraft/world/level/Level;)Z",
		cancellable = true
	)
	private void preventSpellClothRepair(CraftingContainer craftingContainer, Level level,
			CallbackInfoReturnable<Boolean> cir) {
		if (craftingContainer.hasAnyMatching(s -> s.is(BotaniaItems.spellCloth))) {
			cir.setReturnValue(false);
		}
	}
}
