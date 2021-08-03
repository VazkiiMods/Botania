/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.mixin;

import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
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

import java.util.UUID;

@Mixin(ItemEntity.class)
public class MixinItemEntity {
	@Shadow
	private int pickupDelay;

	@Shadow
	private UUID owner;

	@Inject(at = @At("HEAD"), method = "playerTouch", cancellable = true)
	private void onPickup(Player player, CallbackInfo ci) {
		ItemEntity self = (ItemEntity) (Object) this;
		if (!player.level.isClientSide && pickupDelay == 0 && (owner == null || owner.equals(player.getUUID()))
				&& ItemFlowerBag.onPickupItem(self, player)) {
			ci.cancel();
		}
	}

	@ModifyConstant(method = "tick", constant = @Constant(intValue = 6000))
	private int disableDespawn(int value) {
		ItemEntity self = (ItemEntity) (Object) this;
		Item item = self.getItem().getItem();
		if (item instanceof ItemManaTablet || item instanceof ItemManaRing || item instanceof ItemTerraPick || item instanceof ItemRelic || item instanceof ItemRelicBauble) {
			return Integer.MAX_VALUE;
		}
		return value;
	}
}
