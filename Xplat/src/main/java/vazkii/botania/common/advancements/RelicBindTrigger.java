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

import net.minecraft.advancements.critereon.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;

import org.jetbrains.annotations.NotNull;

import static vazkii.botania.common.lib.ResourceLocationHelper.prefix;

public class RelicBindTrigger extends SimpleCriterionTrigger<RelicBindTrigger.Instance> {
	public static final ResourceLocation ID = prefix("relic_bind");
	public static final RelicBindTrigger INSTANCE = new RelicBindTrigger();

	private RelicBindTrigger() {}

	@NotNull
	@Override
	public ResourceLocation getId() {
		return ID;
	}

	@NotNull
	@Override
	public Instance createInstance(@NotNull JsonObject json, @NotNull ContextAwarePredicate playerPred, DeserializationContext conditions) {
		return new Instance(playerPred, ItemPredicate.fromJson(json.get("relic")));
	}

	public void trigger(ServerPlayer player, ItemStack relic) {
		trigger(player, instance -> instance.test(relic));
	}

	public static class Instance extends AbstractCriterionTriggerInstance {
		private final ItemPredicate predicate;

		public Instance(ContextAwarePredicate playerPred, ItemPredicate predicate) {
			super(ID, playerPred);
			this.predicate = predicate;
		}

		@NotNull
		@Override
		public ResourceLocation getCriterion() {
			return ID;
		}

		boolean test(ItemStack stack) {
			return predicate.matches(stack);
		}

		@Override
		public JsonObject serializeToJson(SerializationContext context) {
			JsonObject json = super.serializeToJson(context);
			if (predicate != ItemPredicate.ANY) {
				json.add("relic", predicate.serializeToJson());
			}
			return json;
		}

		public ItemPredicate getPredicate() {
			return this.predicate;
		}
	}
}
