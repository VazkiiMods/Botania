/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.handler;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ServerLevelAccessor;

import vazkii.botania.common.entity.PixieEntity;
import vazkii.botania.common.helper.PlayerHelper;
import vazkii.botania.common.helper.RegistryHelper;
import vazkii.botania.common.item.BotaniaItems;
import vazkii.botania.common.item.equipment.armor.elementium.ElementiumHelmItem;

import java.util.List;
import java.util.function.Supplier;

import static vazkii.botania.api.BotaniaAPI.botaniaRL;

public final class PixieHandler {

	private PixieHandler() {}

	public static final RegistryHelper.HolderProxy<Attribute> PIXIE_SPAWN_CHANCE = RegistryHelper.holderProxy(
			Registries.ATTRIBUTE, botaniaRL("pixie_spawn_chance"),
			new RangedAttribute("attribute.name.botania.pixieSpawnChance", 0, 0, 1));

	private static final List<Supplier<MobEffectInstance>> effectSuppliers = List.of(
			() -> new MobEffectInstance(MobEffects.BLINDNESS, 40, 0),
			() -> new MobEffectInstance(MobEffects.WITHER, 50, 0),
			() -> new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 40, 0),
			() -> new MobEffectInstance(MobEffects.WEAKNESS, 40, 0)
	);

	public static void registerAttribute(Registry<Attribute> registry) {
		PIXIE_SPAWN_CHANCE.register(registry);
	}

	public static AttributeModifier makeModifier(ResourceLocation slotId, double amount) {
		return new AttributeModifier(slotId, amount, AttributeModifier.Operation.ADD_VALUE);
	}

	public static void onDamageTaken(Player player, DamageSource source) {
		if (!player.level().isClientSide() && source.getEntity() instanceof LivingEntity livingSource) {
			// Sometimes the player doesn't have the attribute, not sure why.
			// Could be badly-written mixins on Fabric.
			double chance = player.getAttributes().hasAttribute(PIXIE_SPAWN_CHANCE)
					? player.getAttributeValue(PIXIE_SPAWN_CHANCE) : 0;
			ItemStack sword = PlayerHelper.getFirstHeldItem(player, s -> s.is(BotaniaItems.elementiumSword));

			if (Math.random() < chance) {
				PixieEntity pixie = new PixieEntity(player.level());
				pixie.setPos(player.getX(), player.getY() + 2, player.getZ());

				if (((ElementiumHelmItem) BotaniaItems.elementiumHelm).hasArmorSet(player)) {
					pixie.setApplyPotionEffect(effectSuppliers.get(player.level().getRandom().nextInt(effectSuppliers.size())).get());
				}

				float dmg = 4;
				if (!sword.isEmpty()) {
					dmg += 2;
				}

				pixie.setProps(livingSource, player, 0, dmg);
				pixie.finalizeSpawn((ServerLevelAccessor) player.level(), player.level().getCurrentDifficultyAt(pixie.blockPosition()),
						MobSpawnType.EVENT, null);
				player.level().addFreshEntity(pixie);
			}
		}
	}
}
