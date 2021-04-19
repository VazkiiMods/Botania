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

import net.minecraft.advancements.criterion.AbstractCriterionTrigger;
import net.minecraft.advancements.criterion.CriterionInstance;
import net.minecraft.advancements.criterion.DamageSourcePredicate;
import net.minecraft.advancements.criterion.EntityPredicate;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.loot.ConditionArrayParser;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;

import vazkii.botania.common.entity.EntityDoppleganger;

import javax.annotation.Nonnull;

import static vazkii.botania.common.lib.ResourceLocationHelper.prefix;

public class DopplegangerNoArmorTrigger extends AbstractCriterionTrigger<DopplegangerNoArmorTrigger.Instance> {
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
	public DopplegangerNoArmorTrigger.Instance deserializeTrigger(@Nonnull JsonObject json, EntityPredicate.AndPredicate playerPred, ConditionArrayParser conditions) {
		return new DopplegangerNoArmorTrigger.Instance(playerPred, EntityPredicate.deserialize(json.get("guardian")), DamageSourcePredicate.deserialize(json.get("killing_blow")));
	}

	public void trigger(ServerPlayerEntity player, EntityDoppleganger guardian, DamageSource src) {
		triggerListeners(player, instance -> instance.test(player, guardian, src));
	}

	public static class Instance extends CriterionInstance {
		private final EntityPredicate guardian;
		private final DamageSourcePredicate killingBlow;

		public Instance(EntityPredicate.AndPredicate playerPred, EntityPredicate count, DamageSourcePredicate indexPos) {
			super(ID, playerPred);
			this.guardian = count;
			this.killingBlow = indexPos;
		}

		@Nonnull
		@Override
		public ResourceLocation getId() {
			return ID;
		}

		boolean test(ServerPlayerEntity player, EntityDoppleganger guardian, DamageSource src) {
			return this.guardian.test(player, guardian) && this.killingBlow.test(player, src);
		}

		public EntityPredicate getGuardian() {
			return this.guardian;
		}

		public DamageSourcePredicate getKillingBlow() {
			return this.killingBlow;
		}
	}
}
