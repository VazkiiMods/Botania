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
import net.minecraft.advancements.criterion.*;
import net.minecraft.predicate.NumberRange;
import net.minecraft.predicate.entity.AdvancementEntityPredicateDeserializer;
import net.minecraft.predicate.entity.EntityPredicate;
import net.minecraft.predicate.entity.LocationPredicate;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import vazkii.botania.common.advancements.CorporeaRequestTrigger.Instance;
import javax.annotation.Nonnull;

import static vazkii.botania.common.lib.ResourceLocationHelper.prefix;

public class CorporeaRequestTrigger extends AbstractCriterion<CorporeaRequestTrigger.Instance> {
	public static final Identifier ID = prefix("corporea_index_request");
	public static final CorporeaRequestTrigger INSTANCE = new CorporeaRequestTrigger();

	private CorporeaRequestTrigger() {}

	@Nonnull
	@Override
	public Identifier getId() {
		return ID;
	}

	@Nonnull
	@Override
	protected Instance conditionsFromJson(JsonObject json, EntityPredicate.Extended playerPredicate, AdvancementEntityPredicateDeserializer conditions) {
		return new Instance(playerPredicate, NumberRange.IntRange.fromJson(json.get("extracted")), LocationPredicate.fromJson(json.get("location")));
	}

	public void trigger(ServerPlayerEntity player, ServerWorld world, BlockPos pos, int count) {
		this.test(player, instance -> instance.test(world, pos, count));
	}

	static class Instance extends AbstractCriterionConditions {
		private final NumberRange.IntRange count;
		private final LocationPredicate indexPos;

		Instance(EntityPredicate.Extended playerPredicate, NumberRange.IntRange count, LocationPredicate indexPos) {
			super(ID, playerPredicate);
			this.count = count;
			this.indexPos = indexPos;
		}

		@Nonnull
		@Override
		public Identifier getId() {
			return ID;
		}

		boolean test(ServerWorld world, BlockPos pos, int count) {
			return this.count.test(count) && this.indexPos.test(world, pos.getX(), pos.getY(), pos.getZ());
		}
	}

}
