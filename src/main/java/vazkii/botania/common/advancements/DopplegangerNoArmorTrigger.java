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
import vazkii.botania.common.entity.EntityDoppleganger;

import javax.annotation.Nonnull;
import net.minecraft.advancement.criterion.AbstractCriterion;
import net.minecraft.advancement.criterion.AbstractCriterionConditions;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.predicate.entity.AdvancementEntityPredicateDeserializer;
import net.minecraft.predicate.entity.DamageSourcePredicate;
import net.minecraft.predicate.entity.EntityPredicate;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

import static vazkii.botania.common.lib.ResourceLocationHelper.prefix;

public class DopplegangerNoArmorTrigger extends AbstractCriterion<DopplegangerNoArmorTrigger.Instance> {
	public static final Identifier ID = prefix("gaia_guardian_no_armor");
	public static final DopplegangerNoArmorTrigger INSTANCE = new DopplegangerNoArmorTrigger();

	private DopplegangerNoArmorTrigger() {}

	@Nonnull
	@Override
	public Identifier getId() {
		return ID;
	}

	@Nonnull
	@Override
	public DopplegangerNoArmorTrigger.Instance conditionsFromJson(@Nonnull JsonObject json, EntityPredicate.Extended playerPred, AdvancementEntityPredicateDeserializer conditions) {
		return new DopplegangerNoArmorTrigger.Instance(playerPred, EntityPredicate.fromJson(json.get("guardian")), DamageSourcePredicate.fromJson(json.get("killing_blow")));
	}

	public void trigger(ServerPlayerEntity player, EntityDoppleganger guardian, DamageSource src) {
		test(player, instance -> instance.test(player, guardian, src));
	}

	static class Instance extends AbstractCriterionConditions {
		private final EntityPredicate guardian;
		private final DamageSourcePredicate killingBlow;

		Instance(EntityPredicate.Extended playerPred, EntityPredicate count, DamageSourcePredicate indexPos) {
			super(ID, playerPred);
			this.guardian = count;
			this.killingBlow = indexPos;
		}

		@Nonnull
		@Override
		public Identifier getId() {
			return ID;
		}

		boolean test(ServerPlayerEntity player, EntityDoppleganger guardian, DamageSource src) {
			return this.guardian.test(player, guardian) && this.killingBlow.test(player, src);
		}
	}
}
