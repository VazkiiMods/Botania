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

import net.minecraft.advancement.criterion.AbstractCriterion;
import net.minecraft.advancement.criterion.AbstractCriterionConditions;
import net.minecraft.item.ItemStack;
import net.minecraft.predicate.entity.AdvancementEntityPredicateDeserializer;
import net.minecraft.predicate.entity.EntityPredicate;
import net.minecraft.predicate.item.ItemPredicate;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

import vazkii.botania.common.advancements.RelicBindTrigger.Instance;

import javax.annotation.Nonnull;

import static vazkii.botania.common.lib.ResourceLocationHelper.prefix;

public class RelicBindTrigger extends AbstractCriterion<RelicBindTrigger.Instance> {
	public static final Identifier ID = prefix("relic_bind");
	public static final RelicBindTrigger INSTANCE = new RelicBindTrigger();

	private RelicBindTrigger() {}

	@Nonnull
	@Override
	public Identifier getId() {
		return ID;
	}

	@Nonnull
	@Override
	public Instance conditionsFromJson(@Nonnull JsonObject json, @Nonnull EntityPredicate.Extended playerPred, AdvancementEntityPredicateDeserializer conditions) {
		return new Instance(playerPred, ItemPredicate.fromJson(json.get("relic")));
	}

	public void trigger(ServerPlayerEntity player, ItemStack relic) {
		test(player, instance -> instance.test(relic));
	}

	public static class Instance extends AbstractCriterionConditions {
		private final ItemPredicate predicate;

		public Instance(EntityPredicate.Extended playerPred, ItemPredicate predicate) {
			super(ID, playerPred);
			this.predicate = predicate;
		}

		@Nonnull
		@Override
		public Identifier getId() {
			return ID;
		}

		boolean test(ItemStack stack) {
			return predicate.test(stack);
		}

		public ItemPredicate getPredicate() {
			return this.predicate;
		}
	}
}
