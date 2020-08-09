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

import net.minecraft.advancements.criterion.*;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.loot.ConditionArrayParser;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;

import javax.annotation.Nonnull;

import static vazkii.botania.common.lib.ResourceLocationHelper.prefix;

public class CorporeaRequestTrigger extends AbstractCriterionTrigger<CorporeaRequestTrigger.Instance> {
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
	protected Instance deserializeTrigger(JsonObject json, EntityPredicate.AndPredicate playerPredicate, ConditionArrayParser conditions) {
		return new Instance(playerPredicate, MinMaxBounds.IntBound.fromJson(json.get("extracted")), LocationPredicate.deserialize(json.get("location")));
	}

	public void trigger(ServerPlayerEntity player, ServerWorld world, BlockPos pos, int count) {
		this.triggerListeners(player, instance -> instance.test(world, pos, count));
	}

	static class Instance extends CriterionInstance {
		private final MinMaxBounds.IntBound count;
		private final LocationPredicate indexPos;

		Instance(EntityPredicate.AndPredicate playerPredicate, MinMaxBounds.IntBound count, LocationPredicate indexPos) {
			super(ID, playerPredicate);
			this.count = count;
			this.indexPos = indexPos;
		}

		@Nonnull
		@Override
		public ResourceLocation getId() {
			return ID;
		}

		boolean test(ServerWorld world, BlockPos pos, int count) {
			return this.count.test(count) && this.indexPos.test(world, pos.getX(), pos.getY(), pos.getZ());
		}
	}

}
