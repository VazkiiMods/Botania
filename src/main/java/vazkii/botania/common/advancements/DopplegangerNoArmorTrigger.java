/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.advancements;

import com.google.gson.JsonObject;

import net.minecraft.advancements.critereon.AbstractCriterionTriggerInstance;
import net.minecraft.advancements.critereon.DamageSourcePredicate;
import net.minecraft.advancements.critereon.DeserializationContext;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.SimpleCriterionTrigger;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;

import vazkii.botania.common.entity.EntityDoppleganger;

import javax.annotation.Nonnull;

import static vazkii.botania.common.lib.ResourceLocationHelper.prefix;

public class DopplegangerNoArmorTrigger extends SimpleCriterionTrigger<DopplegangerNoArmorTrigger.Instance> {
	public static final ResourceLocation ID = prefix("gaia_guardian_no_armor");
	public static final DopplegangerNoArmorTrigger INSTANCE = new DopplegangerNoArmorTrigger();

	private DopplegangerNoArmorTrigger() {}

	@Nonnull
	@Override
	public ResourceLocation getId() {
		return ID;
	}

	@Nonnull
	@Override
	public DopplegangerNoArmorTrigger.Instance createInstance(@Nonnull JsonObject json, EntityPredicate.Composite playerPred, DeserializationContext conditions) {
		return new DopplegangerNoArmorTrigger.Instance(playerPred, EntityPredicate.fromJson(json.get("guardian")), DamageSourcePredicate.fromJson(json.get("killing_blow")));
	}

	public void trigger(ServerPlayer player, EntityDoppleganger guardian, DamageSource src) {
		trigger(player, instance -> instance.test(player, guardian, src));
	}

	public static class Instance extends AbstractCriterionTriggerInstance {
		private final EntityPredicate guardian;
		private final DamageSourcePredicate killingBlow;

		public Instance(EntityPredicate.Composite playerPred, EntityPredicate count, DamageSourcePredicate indexPos) {
			super(ID, playerPred);
			this.guardian = count;
			this.killingBlow = indexPos;
		}

		@Nonnull
		@Override
		public ResourceLocation getCriterion() {
			return ID;
		}

		boolean test(ServerPlayer player, EntityDoppleganger guardian, DamageSource src) {
			return this.guardian.matches(player, guardian) && this.killingBlow.matches(player, src);
		}

		public EntityPredicate getGuardian() {
			return this.guardian;
		}

		public DamageSourcePredicate getKillingBlow() {
			return this.killingBlow;
		}
	}
}
