/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.mixin;

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;

import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import vazkii.botania.common.item.ItemSpellCloth;

@Mixin(AnvilMenu.class)
public abstract class MixinAnvilMenu extends ItemCombinerMenu {
	@Shadow
	@Final
	private DataSlot cost;

	public MixinAnvilMenu(MenuType<?> type, int i, Inventory inv, ContainerLevelAccess cla) {
		super(type, i, inv, cla);
	}

	@Inject(method = "createResult", at = @At("HEAD"), cancellable = true)
	private void checkIfCloth(CallbackInfo ci) {
		if (ItemSpellCloth.shouldDenyAnvil(inputSlots)) {
			this.resultSlots.setItem(0, ItemStack.EMPTY);
			this.cost.set(0);
			ci.cancel();
		}
	}
}
