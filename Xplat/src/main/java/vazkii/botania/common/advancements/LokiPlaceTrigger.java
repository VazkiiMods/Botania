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

public class LokiPlaceTrigger extends SimpleCriterionTrigger<LokiPlaceTrigger.Instance> {
	public static final ResourceLocation ID = prefix("loki_placed_blocks");
	public static final LokiPlaceTrigger INSTANCE = new LokiPlaceTrigger();

	private LokiPlaceTrigger() {}

	@NotNull
	@Override
	public ResourceLocation getId() {
		return ID;
	}

	@NotNull
	@Override
	public LokiPlaceTrigger.Instance createInstance(@NotNull JsonObject json, ContextAwarePredicate playerPred, DeserializationContext conditions) {
		return new LokiPlaceTrigger.Instance(playerPred, EntityPredicate.fromJson(json.get("player")),
				ItemPredicate.fromJson(json.get("ring")), MinMaxBounds.Ints.fromJson(json.get("blocks_placed")));
	}

	public void trigger(ServerPlayer player, ItemStack ring, int blocksPlaced) {
		trigger(player, instance -> instance.test(player, ring, blocksPlaced));
	}

	public static class Instance extends AbstractCriterionTriggerInstance {
		private final EntityPredicate player;
		private final ItemPredicate ring;
		private final MinMaxBounds.Ints blocksPlaced;

		public Instance(ContextAwarePredicate playerPred, EntityPredicate player, ItemPredicate ring, MinMaxBounds.Ints blocksPlaced) {
			super(ID, playerPred);
			this.player = player;
			this.ring = ring;
			this.blocksPlaced = blocksPlaced;
		}

		@NotNull
		@Override
		public ResourceLocation getCriterion() {
			return ID;
		}

		boolean test(ServerPlayer player, ItemStack ring, int blocksPlaced) {
			return this.player.matches(player, null) && this.ring.matches(ring) && this.blocksPlaced.matches(blocksPlaced);
		}

		@Override
		public JsonObject serializeToJson(SerializationContext context) {
			JsonObject json = super.serializeToJson(context);
			if (ring != ItemPredicate.ANY) {
				json.add("ring", ring.serializeToJson());
			}
			if (blocksPlaced != MinMaxBounds.Ints.ANY) {
				json.add("blocks_placed", blocksPlaced.serializeToJson());
			}
			return json;
		}

		public EntityPredicate getPlayer() {
			return this.player;
		}

		public ItemPredicate getRing() {
			return this.ring;
		}

		public MinMaxBounds.Ints getBlocksPlaced() {
			return this.blocksPlaced;
		}
	}
}
