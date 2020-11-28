/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.mixin;

import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import vazkii.botania.common.item.ItemFlowerBag;
import vazkii.botania.common.item.ItemManaTablet;
import vazkii.botania.common.item.equipment.bauble.ItemManaRing;
import vazkii.botania.common.item.equipment.tool.terrasteel.ItemTerraPick;
import vazkii.botania.common.item.relic.ItemRelic;
import vazkii.botania.common.item.relic.ItemRelicBauble;

@Mixin(ItemEntity.class)
public class MixinItemEntity {
	@Inject(at = @At("HEAD"), method = "onPlayerCollision", cancellable = true)
	private void onPickup(PlayerEntity player, CallbackInfo ci) {
		ItemEntity self = (ItemEntity) (Object) this;
		if (ItemFlowerBag.onPickupItem(self, player)) {
			ci.cancel();
		}
	}

	@ModifyConstant(method = "tick", constant = @Constant(intValue = 6000))
	private int disableDespawn(int value) {
		ItemEntity self = (ItemEntity) (Object) this;
		Item item = self.getStack().getItem();
		if (item instanceof ItemManaTablet || item instanceof ItemManaRing || item instanceof ItemTerraPick || item instanceof ItemRelic || item instanceof ItemRelicBauble) {
			return Integer.MAX_VALUE;
		}
		return value;
	}
}
