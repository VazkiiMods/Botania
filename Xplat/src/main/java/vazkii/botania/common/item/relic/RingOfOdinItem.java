/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.item.relic;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import vazkii.botania.api.item.Relic;
import vazkii.botania.common.handler.EquipmentHandler;
import vazkii.botania.common.item.ModItems;

import java.util.HashSet;
import java.util.Set;

import static vazkii.botania.common.lib.ResourceLocationHelper.prefix;

public class RingOfOdinItem extends RelicBaubleItem {

	private static final Set<String> damageNegations = new HashSet<>();

	public RingOfOdinItem(Properties props) {
		super(props);

		damageNegations.add(DamageSource.DROWN.msgId);
		damageNegations.add(DamageSource.FALL.msgId);
		damageNegations.add(DamageSource.IN_WALL.msgId);
		damageNegations.add(DamageSource.STARVE.msgId);
		damageNegations.add(DamageSource.FLY_INTO_WALL.msgId);
	}

	@Override
	public void onValidPlayerWornTick(Player player) {
		if (player.isOnFire()) {
			player.clearFire();
		}
	}

	@Override
	public Multimap<Attribute, AttributeModifier> getEquippedAttributeModifiers(ItemStack stack) {
		Multimap<Attribute, AttributeModifier> attributes = HashMultimap.create();
		attributes.put(Attributes.MAX_HEALTH,
				new AttributeModifier(getBaubleUUID(stack), "Odin Ring", 20, AttributeModifier.Operation.ADDITION));
		return attributes;
	}

	public static boolean onPlayerAttacked(Player player, DamageSource src) {
		return (src.isFire() || damageNegations.contains(src.msgId))
				&& !EquipmentHandler.findOrEmpty(ModItems.odinRing, player).isEmpty();
	}

	public static Relic makeRelic(ItemStack stack) {
		return new RelicImpl(stack, prefix("challenge/odin_ring"));
	}

}
