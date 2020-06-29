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

import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.entity.living.LivingAttackEvent;

import vazkii.botania.common.core.handler.EquipmentHandler;
import vazkii.botania.common.item.ModItems;
import vazkii.botania.common.lib.LibMisc;

import java.util.HashSet;
import java.util.Set;

public class ItemOdinRing extends ItemRelicBauble {

	private static final Set<String> damageNegations = new HashSet<>();
	private static final Set<String> fireNegations = new HashSet<>();

	public ItemOdinRing(Properties props) {
		super(props);

		damageNegations.add(DamageSource.DROWN.damageType);
		damageNegations.add(DamageSource.FALL.damageType);
		damageNegations.add(DamageSource.LAVA.damageType);
		damageNegations.add(DamageSource.IN_WALL.damageType);
		damageNegations.add(DamageSource.STARVE.damageType);
		fireNegations.add(DamageSource.IN_FIRE.damageType);
		fireNegations.add(DamageSource.ON_FIRE.damageType);
	}

	@Override
	public void onValidPlayerWornTick(PlayerEntity player) {
		if (player.isBurning()) {
			player.extinguish();
		}
	}

	@Override
	public Multimap<Attribute, AttributeModifier> getEquippedAttributeModifiers(ItemStack stack) {
		Multimap<Attribute, AttributeModifier> attributes = HashMultimap.create();
		attributes.put(Attributes.field_233818_a_,
				new AttributeModifier(getBaubleUUID(stack), "Odin Ring", 20, AttributeModifier.Operation.ADDITION));
		return attributes;
	}

	public static void onPlayerAttacked(LivingAttackEvent event) {
		if (event.getEntityLiving() instanceof PlayerEntity) {
			PlayerEntity player = (PlayerEntity) event.getEntityLiving();
			boolean negate = damageNegations.contains(event.getSource().damageType)
					|| (fireNegations.contains(event.getSource().damageType));
			boolean hasRing = !EquipmentHandler.findOrEmpty(ModItems.odinRing, player).isEmpty();
			if (hasRing && negate) {
				event.setCanceled(true);
			}
		}
	}

	@Override
	public ResourceLocation getAdvancement() {
		return new ResourceLocation(LibMisc.MOD_ID, "challenge/odin_ring");
	}

}
