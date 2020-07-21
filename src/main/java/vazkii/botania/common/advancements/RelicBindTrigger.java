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
import net.minecraft.advancements.criterion.EntityPredicate;
import net.minecraft.advancements.criterion.ItemPredicate;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.ConditionArrayParser;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;

import static vazkii.botania.common.lib.ResourceLocationHelper.prefix;

public class RelicBindTrigger extends AbstractCriterionTrigger<RelicBindTrigger.Instance> {
	public static final ResourceLocation ID = prefix("relic_bind");
	public static final RelicBindTrigger INSTANCE = new RelicBindTrigger();

	private RelicBindTrigger() {}

	@Nonnull
	@Override
	public ResourceLocation getId() {
		return ID;
	}

	@Nonnull
	@Override
	public Instance func_230241_b_(@Nonnull JsonObject json, @Nonnull EntityPredicate.AndPredicate playerPred, ConditionArrayParser conditions) {
		return new Instance(playerPred, ItemPredicate.deserialize(json.get("relic")));
	}

	public void trigger(ServerPlayerEntity player, ItemStack relic) {
		func_235959_a_(player, instance -> instance.test(relic));
	}

	static class Instance extends CriterionInstance {
		private final ItemPredicate predicate;

		Instance(EntityPredicate.AndPredicate playerPred, ItemPredicate predicate) {
			super(ID, playerPred);
			this.predicate = predicate;
		}

		@Nonnull
		@Override
		public ResourceLocation getId() {
			return ID;
		}

		boolean test(ItemStack stack) {
			return predicate.test(stack);
		}
	}
}
