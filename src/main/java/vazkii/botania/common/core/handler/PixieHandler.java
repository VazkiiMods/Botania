/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.core.handler;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.attribute.ClampedEntityAttribute;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Util;
import net.minecraft.util.registry.Registry;

import net.minecraft.world.ServerWorldAccess;
import vazkii.botania.common.core.helper.PlayerHelper;
import vazkii.botania.common.entity.EntityPixie;
import vazkii.botania.common.item.ModItems;
import vazkii.botania.common.item.equipment.armor.elementium.ItemElementiumHelm;

import java.util.EnumMap;
import java.util.Map;
import java.util.UUID;

import static vazkii.botania.common.block.ModBlocks.register;
import static vazkii.botania.common.lib.ResourceLocationHelper.prefix;

public final class PixieHandler {

	private PixieHandler() {}

	public static final EntityAttribute PIXIE_SPAWN_CHANCE = new ClampedEntityAttribute("attribute.name.botania.pixieSpawnChance", 0, 0, 1);
	private static final Map<EquipmentSlot, UUID> DEFAULT_MODIFIER_UUIDS = Util.make(new EnumMap<>(EquipmentSlot.class), m -> {
		m.put(EquipmentSlot.HEAD, UUID.fromString("3c1f559c-9ec4-412d-ada0-dbf3e714088e"));
		m.put(EquipmentSlot.CHEST, UUID.fromString("9631121c-16f0-4ed4-ba0a-0e7a063cb71c"));
		m.put(EquipmentSlot.LEGS, UUID.fromString("a87117a1-ac15-4b17-9fd5-e98d5fe31ff1"));
		m.put(EquipmentSlot.FEET, UUID.fromString("ff67d38a-c5be-4a00-90ed-76bb12c45523"));
		m.put(EquipmentSlot.MAINHAND, UUID.fromString("995829fa-94c0-41bd-b046-0468c509a488"));
		m.put(EquipmentSlot.OFFHAND, UUID.fromString("34f62de8-f652-4fe7-899f-a8fc938c4940"));
	});

	private static final StatusEffect[] potions = {
			StatusEffects.BLINDNESS,
			StatusEffects.WITHER,
			StatusEffects.SLOWNESS,
			StatusEffects.WEAKNESS
	};

	public static void registerAttribute() {
		Registry<EntityAttribute> r = Registry.ATTRIBUTE;
		register(r, prefix("pixie_spawn_chance"), PIXIE_SPAWN_CHANCE);
	}

	public static EntityAttributeModifier makeModifier(EquipmentSlot slot, String name, double amount) {
		return new EntityAttributeModifier(DEFAULT_MODIFIER_UUIDS.get(slot), name, amount, EntityAttributeModifier.Operation.ADDITION);
	}

	public static void onDamageTaken(PlayerEntity player, DamageSource source) {
		if (!player.world.isClient && source.getAttacker() instanceof LivingEntity) {
			double chance = player.getAttributeValue(PIXIE_SPAWN_CHANCE);
			ItemStack sword = PlayerHelper.getFirstHeldItem(player, s -> s.getItem() == ModItems.elementiumSword);

			if (Math.random() < chance) {
				EntityPixie pixie = new EntityPixie(player.world);
				pixie.updatePosition(player.getX(), player.getY() + 2, player.getZ());

				if (((ItemElementiumHelm) ModItems.elementiumHelm).hasArmorSet(player)) {
					pixie.setApplyPotionEffect(new StatusEffectInstance(potions[player.world.random.nextInt(potions.length)], 40, 0));
				}

				float dmg = 4;
				if (!sword.isEmpty()) {
					dmg += 2;
				}

				pixie.setProps((LivingEntity) source.getAttacker(), player, 0, dmg);
				pixie.initialize((ServerWorldAccess) player.world, player.world.getLocalDifficulty(pixie.getBlockPos()),
						SpawnReason.EVENT, null, null);
				player.world.spawnEntity(pixie);
			}
		}
	}
}
