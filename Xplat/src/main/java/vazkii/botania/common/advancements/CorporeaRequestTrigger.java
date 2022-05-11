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
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;

import javax.annotation.Nonnull;

import static vazkii.botania.common.lib.ResourceLocationHelper.prefix;

public class CorporeaRequestTrigger extends SimpleCriterionTrigger<CorporeaRequestTrigger.Instance> {
	public static final ResourceLocation ID = prefix("corporea_index_request");
	public static final CorporeaRequestTrigger INSTANCE = new CorporeaRequestTrigger();

	private CorporeaRequestTrigger() {}

	@Nonnull
	@Override
	public ResourceLocation getId() {
		return ID;
	}

	@Nonnull
	@Override
	protected Instance createInstance(JsonObject json, EntityPredicate.Composite playerPredicate, DeserializationContext conditions) {
		return new Instance(playerPredicate, MinMaxBounds.Ints.fromJson(json.get("extracted")), LocationPredicate.fromJson(json.get("location")));
	}

	public void trigger(ServerPlayer player, ServerLevel world, BlockPos pos, int count) {
		this.trigger(player, instance -> instance.test(world, pos, count));
	}

	public static class Instance extends AbstractCriterionTriggerInstance {
		private final MinMaxBounds.Ints count;
		private final LocationPredicate indexPos;

		public Instance(EntityPredicate.Composite playerPredicate, MinMaxBounds.Ints count, LocationPredicate indexPos) {
			super(ID, playerPredicate);
			this.count = count;
			this.indexPos = indexPos;
		}

		@Nonnull
		@Override
		public ResourceLocation getCriterion() {
			return ID;
		}

		boolean test(ServerLevel world, BlockPos pos, int count) {
			return this.count.matches(count) && this.indexPos.matches(world, pos.getX(), pos.getY(), pos.getZ());
		}

		@Override
		public JsonObject serializeToJson(SerializationContext context) {
			JsonObject json = super.serializeToJson(context);
			if (count != MinMaxBounds.Ints.ANY) {
				json.add("extracted", count.serializeToJson());
			}
			if (indexPos != LocationPredicate.ANY) {
				json.add("location", indexPos.serializeToJson());
			}
			return json;
		}

		public MinMaxBounds.Ints getCount() {
			return this.count;
		}

		public LocationPredicate getIndexPos() {
			return this.indexPos;
		}
	}

}
