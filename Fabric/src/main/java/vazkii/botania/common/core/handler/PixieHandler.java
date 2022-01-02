/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.core.handler;

import net.minecraft.Util;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ServerLevelAccessor;

import vazkii.botania.common.core.helper.PlayerHelper;
import vazkii.botania.common.entity.EntityPixie;
import vazkii.botania.common.item.ModItems;
import vazkii.botania.common.item.equipment.armor.elementium.ItemElementiumHelm;

import java.util.EnumMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.BiConsumer;

import static vazkii.botania.common.block.ModBlocks.register;
import static vazkii.botania.common.lib.ResourceLocationHelper.prefix;

public final class PixieHandler {

	private PixieHandler() {}

	public static final Attribute PIXIE_SPAWN_CHANCE = new RangedAttribute("attribute.name.botania.pixieSpawnChance", 0, 0, 1);
	private static final Map<EquipmentSlot, UUID> DEFAULT_MODIFIER_UUIDS = Util.make(new EnumMap<>(EquipmentSlot.class), m -> {
		m.put(EquipmentSlot.HEAD, UUID.fromString("3c1f559c-9ec4-412d-ada0-dbf3e714088e"));
		m.put(EquipmentSlot.CHEST, UUID.fromString("9631121c-16f0-4ed4-ba0a-0e7a063cb71c"));
		m.put(EquipmentSlot.LEGS, UUID.fromString("a87117a1-ac15-4b17-9fd5-e98d5fe31ff1"));
		m.put(EquipmentSlot.FEET, UUID.fromString("ff67d38a-c5be-4a00-90ed-76bb12c45523"));
		m.put(EquipmentSlot.MAINHAND, UUID.fromString("995829fa-94c0-41bd-b046-0468c509a488"));
		m.put(EquipmentSlot.OFFHAND, UUID.fromString("34f62de8-f652-4fe7-899f-a8fc938c4940"));
	});

	private static final MobEffect[] potions = {
			MobEffects.BLINDNESS,
			MobEffects.WITHER,
			MobEffects.MOVEMENT_SLOWDOWN,
			MobEffects.WEAKNESS
	};

	public static void registerAttribute(BiConsumer<Attribute, ResourceLocation> r) {
		register(r, prefix("pixie_spawn_chance"), PIXIE_SPAWN_CHANCE);
	}

	public static AttributeModifier makeModifier(EquipmentSlot slot, String name, double amount) {
		return new AttributeModifier(DEFAULT_MODIFIER_UUIDS.get(slot), name, amount, AttributeModifier.Operation.ADDITION);
	}

	public static void onDamageTaken(Player player, DamageSource source) {
		if (!player.level.isClientSide && source.getEntity() instanceof LivingEntity) {
			double chance = player.getAttributeValue(PIXIE_SPAWN_CHANCE);
			ItemStack sword = PlayerHelper.getFirstHeldItem(player, s -> s.is(ModItems.elementiumSword));

			if (Math.random() < chance) {
				EntityPixie pixie = new EntityPixie(player.level);
				pixie.setPos(player.getX(), player.getY() + 2, player.getZ());

				if (((ItemElementiumHelm) ModItems.elementiumHelm).hasArmorSet(player)) {
					pixie.setApplyPotionEffect(new MobEffectInstance(potions[player.level.random.nextInt(potions.length)], 40, 0));
				}

				float dmg = 4;
				if (!sword.isEmpty()) {
					dmg += 2;
				}

				pixie.setProps((LivingEntity) source.getEntity(), player, 0, dmg);
				pixie.finalizeSpawn((ServerLevelAccessor) player.level, player.level.getCurrentDifficultyAt(pixie.blockPosition()),
						MobSpawnType.EVENT, null, null);
				player.level.addFreshEntity(pixie);
			}
		}
	}
}
