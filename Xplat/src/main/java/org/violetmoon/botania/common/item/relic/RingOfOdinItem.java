/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package org.violetmoon.botania.common.item.relic;

import com.google.common.collect.Multimap;

import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import org.violetmoon.botania.api.item.Relic;
import org.violetmoon.botania.common.handler.EquipmentHandler;
import org.violetmoon.botania.common.item.BotaniaItems;
import org.violetmoon.botania.common.lib.BotaniaTags;

import static org.violetmoon.botania.api.BotaniaAPI.botaniaRL;

public class RingOfOdinItem extends RelicBaubleItem {

	public RingOfOdinItem(Properties props) {
		super(props);
	}

	@Override
	public void onValidPlayerWornTick(Player player) {
		if (player.isOnFire()) {
			player.clearFire();
		}
	}

	@Override
	public Multimap<Holder<Attribute>, AttributeModifier> getEquippedAttributeModifiers(ItemStack stack, ResourceLocation slotId) {
		Multimap<Holder<Attribute>, AttributeModifier> attributes = super.getEquippedAttributeModifiers(stack, slotId);
		attributes.put(Attributes.MAX_HEALTH,
				new AttributeModifier(slotId, 20, AttributeModifier.Operation.ADD_VALUE));
		return attributes;
	}

	public static boolean onPlayerAttacked(Player player, DamageSource src) {
		return (src.is(BotaniaTags.DamageTypes.RING_OF_ODIN_IMMUNE))
				&& !EquipmentHandler.findOrEmpty(BotaniaItems.RING_OF_ODIN, player).isEmpty();
	}

	public static Relic makeRelic(ItemStack stack) {
		return new RelicImpl(stack, botaniaRL("challenge/ring_of_odin"));
	}

}
