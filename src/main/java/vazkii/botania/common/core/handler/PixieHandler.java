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
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.attributes.AbstractAttributeMap;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.entity.ai.attributes.RangedAttribute;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import vazkii.botania.common.core.helper.PlayerHelper;
import vazkii.botania.common.entity.EntityPixie;
import vazkii.botania.common.item.ModItems;
import vazkii.botania.common.item.equipment.armor.elementium.ItemElementiumHelm;
import vazkii.botania.common.lib.LibMisc;

import java.util.EnumMap;
import java.util.Map;
import java.util.UUID;

@Mod.EventBusSubscriber(modid = LibMisc.MOD_ID)
public final class PixieHandler {

	private PixieHandler() {}

	public static final IAttribute PIXIE_SPAWN_CHANCE = new RangedAttribute(null, "botania.pixieSpawnChance", 0, 0, 1);
	private static final Map<EquipmentSlotType, UUID> DEFAULT_MODIFIER_UUIDS = Util.make(new EnumMap<>(EquipmentSlotType.class), m -> {
		m.put(EquipmentSlotType.HEAD, UUID.fromString("3c1f559c-9ec4-412d-ada0-dbf3e714088e"));
		m.put(EquipmentSlotType.CHEST, UUID.fromString("9631121c-16f0-4ed4-ba0a-0e7a063cb71c"));
		m.put(EquipmentSlotType.LEGS, UUID.fromString("a87117a1-ac15-4b17-9fd5-e98d5fe31ff1"));
		m.put(EquipmentSlotType.FEET, UUID.fromString("ff67d38a-c5be-4a00-90ed-76bb12c45523"));
		m.put(EquipmentSlotType.MAINHAND, UUID.fromString("995829fa-94c0-41bd-b046-0468c509a488"));
		m.put(EquipmentSlotType.OFFHAND, UUID.fromString("34f62de8-f652-4fe7-899f-a8fc938c4940"));
	});

	private static final Effect[] potions = {
			Effects.BLINDNESS,
			Effects.WITHER,
			Effects.SLOWNESS,
			Effects.WEAKNESS
	};

	public static AttributeModifier makeModifier(EquipmentSlotType slot, String name, double amount) {
		return new AttributeModifier(DEFAULT_MODIFIER_UUIDS.get(slot), name, amount, AttributeModifier.Operation.ADDITION);
	}

	// Want to do this as early as possible -- doing it at entity join world means attribute modifiers are ignored when loading
	@SubscribeEvent
	public static void registerAttribute(AttachCapabilitiesEvent<Entity> evt) {
		if (evt.getObject() instanceof PlayerEntity) {
			AbstractAttributeMap attributes = ((PlayerEntity) evt.getObject()).getAttributes();
			if (attributes.getAttributeInstance(PIXIE_SPAWN_CHANCE) == null) {
				attributes.registerAttribute(PIXIE_SPAWN_CHANCE);
			}
		}
	}

	@SubscribeEvent
	public static void onDamageTaken(LivingHurtEvent event) {
		if (!event.getEntityLiving().world.isRemote && event.getEntityLiving() instanceof PlayerEntity && event.getSource().getTrueSource() instanceof LivingEntity) {
			PlayerEntity player = (PlayerEntity) event.getEntityLiving();
			double chance = player.getAttribute(PIXIE_SPAWN_CHANCE).getValue();
			ItemStack sword = PlayerHelper.getFirstHeldItem(player, s -> s.getItem() == ModItems.elementiumSword);

			if (Math.random() < chance) {
				EntityPixie pixie = new EntityPixie(player.world);
				pixie.setPosition(player.getX(), player.getY() + 2, player.getZ());

				if (((ItemElementiumHelm) ModItems.elementiumHelm).hasArmorSet(player)) {
					pixie.setApplyPotionEffect(new EffectInstance(potions[event.getEntityLiving().world.rand.nextInt(potions.length)], 40, 0));
				}

				float dmg = 4;
				if (!sword.isEmpty()) {
					dmg += 2;
				}

				pixie.setProps((LivingEntity) event.getSource().getTrueSource(), player, 0, dmg);
				pixie.onInitialSpawn(player.world, player.world.getDifficultyForLocation(new BlockPos(pixie)),
						SpawnReason.EVENT, null, null);
				player.world.addEntity(pixie);
			}
		}
	}
}
