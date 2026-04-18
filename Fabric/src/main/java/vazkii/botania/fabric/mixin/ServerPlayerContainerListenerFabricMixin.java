package vazkii.botania.fabric.mixin;

import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import vazkii.botania.common.block.BotaniaBlocks;
import vazkii.botania.integration.speedrunigt.BotaniaSpeedrunCategories;
import vazkii.botania.xplat.XplatAbstractions;

@Mixin(targets = "net.minecraft.server.level.ServerPlayer$2")
public class ServerPlayerContainerListenerFabricMixin {
	@Inject(method = "slotChanged", at = @At(value = "INVOKE", target = "Lnet/minecraft/advancements/critereon/InventoryChangeTrigger;trigger(Lnet/minecraft/server/level/ServerPlayer;Lnet/minecraft/world/entity/player/Inventory;Lnet/minecraft/world/item/ItemStack;)V"))
	public void onInventoryChanged(AbstractContainerMenu inventory, int slot, ItemStack stack, CallbackInfo ci) {
		if (XplatAbstractions.instance().isRunningCategory(BotaniaSpeedrunCategories.OBTAIN_DREAMWOOD) &&
				stack.is(BotaniaBlocks.DREAMWOOD.asItem())) {
			XplatAbstractions.instance().completeSpeedrunTimer();
		}
	}
}
