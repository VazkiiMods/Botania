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

import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

import vazkii.botania.common.core.handler.EquipmentHandler;
import vazkii.botania.common.item.ModItems;

import java.util.HashSet;
import java.util.Set;

import static vazkii.botania.common.lib.ResourceLocationHelper.prefix;

public class ItemOdinRing extends ItemRelicBauble {

	private static final Set<String> damageNegations = new HashSet<>();
	private static final Set<String> fireNegations = new HashSet<>();

	public ItemOdinRing(Settings props) {
		super(props);

		damageNegations.add(DamageSource.DROWN.name);
		damageNegations.add(DamageSource.FALL.name);
		damageNegations.add(DamageSource.LAVA.name);
		damageNegations.add(DamageSource.IN_WALL.name);
		damageNegations.add(DamageSource.STARVE.name);
		fireNegations.add(DamageSource.IN_FIRE.name);
		fireNegations.add(DamageSource.ON_FIRE.name);
	}

	@Override
	public void onValidPlayerWornTick(PlayerEntity player) {
		if (player.isOnFire()) {
			player.extinguish();
		}
	}

	@Override
	public Multimap<EntityAttribute, EntityAttributeModifier> getEquippedAttributeModifiers(ItemStack stack) {
		Multimap<EntityAttribute, EntityAttributeModifier> attributes = HashMultimap.create();
		attributes.put(EntityAttributes.GENERIC_MAX_HEALTH,
				new EntityAttributeModifier(getBaubleUUID(stack), "Odin Ring", 20, EntityAttributeModifier.Operation.ADDITION));
		return attributes;
	}

	public static boolean onPlayerAttacked(PlayerEntity player, DamageSource src) {
		boolean negate = damageNegations.contains(src.name)
				|| (fireNegations.contains(src.name));
		boolean hasRing = !EquipmentHandler.findOrEmpty(ModItems.odinRing, player).isEmpty();
		return negate && hasRing;
	}

	@Override
	public Identifier getAdvancement() {
		return prefix("challenge/odin_ring");
	}

}
