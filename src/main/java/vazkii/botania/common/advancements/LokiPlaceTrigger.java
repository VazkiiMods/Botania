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
import net.minecraft.item.ItemStack;
import net.minecraft.loot.ConditionArrayParser;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;

import static vazkii.botania.common.lib.ResourceLocationHelper.prefix;

public class LokiPlaceTrigger extends AbstractCriterionTrigger<LokiPlaceTrigger.Instance> {
	public static final ResourceLocation ID = prefix("loki_placed_blocks");
	public static final LokiPlaceTrigger INSTANCE = new LokiPlaceTrigger();

	private LokiPlaceTrigger() {}

	@Nonnull
	@Override
	public ResourceLocation getId() {
		return ID;
	}

	@Nonnull
	@Override
	public LokiPlaceTrigger.Instance func_230241_b_(@Nonnull JsonObject json, EntityPredicate.AndPredicate playerPred, ConditionArrayParser conditions) {
		return new LokiPlaceTrigger.Instance(playerPred, EntityPredicate.deserialize(json.get("player")), ItemPredicate.deserialize(json.get("ring")), MinMaxBounds.IntBound.fromJson(json.get("blocks_placed")));
	}

	public void trigger(ServerPlayerEntity player, ItemStack ring, int blocksPlaced) {
		func_235959_a_(player, instance -> instance.test(player, ring, blocksPlaced));
	}

	static class Instance extends CriterionInstance {
		private final EntityPredicate player;
		private final ItemPredicate ring;
		private final MinMaxBounds.IntBound blocksPlaced;

		Instance(EntityPredicate.AndPredicate playerPred, EntityPredicate player, ItemPredicate ring, MinMaxBounds.IntBound blocksPlaced) {
			super(ID, playerPred);
			this.player = player;
			this.ring = ring;
			this.blocksPlaced = blocksPlaced;
		}

		@Nonnull
		@Override
		public ResourceLocation getId() {
			return ID;
		}

		boolean test(ServerPlayerEntity player, ItemStack ring, int blocksPlaced) {
			return this.player.test(player, null) && this.ring.test(ring) && this.blocksPlaced.test(blocksPlaced);
		}
	}
}
