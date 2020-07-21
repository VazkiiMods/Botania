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
import net.minecraft.predicate.NumberRange;
import net.minecraft.predicate.entity.AdvancementEntityPredicateDeserializer;
import net.minecraft.predicate.entity.EntityPredicate;
import net.minecraft.predicate.item.ItemPredicate;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import vazkii.botania.common.lib.LibMisc;

import javax.annotation.Nonnull;

import static vazkii.botania.common.lib.ResourceLocationHelper.prefix;

public class LokiPlaceTrigger extends AbstractCriterion<LokiPlaceTrigger.Instance> {
	public static final Identifier ID = prefix("loki_placed_blocks");
	public static final LokiPlaceTrigger INSTANCE = new LokiPlaceTrigger();

	private LokiPlaceTrigger() {}

	@Nonnull
	@Override
	public Identifier getId() {
		return ID;
	}

	@Nonnull
	@Override
	public LokiPlaceTrigger.Instance conditionsFromJson(@Nonnull JsonObject json, EntityPredicate.Extended playerPred, AdvancementEntityPredicateDeserializer conditions) {
		return new LokiPlaceTrigger.Instance(playerPred, EntityPredicate.fromJson(json.get("player")), ItemPredicate.fromJson(json.get("ring")), NumberRange.IntRange.fromJson(json.get("blocks_placed")));
	}

	public void trigger(ServerPlayerEntity player, ItemStack ring, int blocksPlaced) {
		test(player, instance -> instance.test(player, ring, blocksPlaced));
	}

	static class Instance extends AbstractCriterionConditions {
		private final EntityPredicate player;
		private final ItemPredicate ring;
		private final NumberRange.IntRange blocksPlaced;

		Instance(EntityPredicate.Extended playerPred, EntityPredicate player, ItemPredicate ring, NumberRange.IntRange blocksPlaced) {
			super(ID, playerPred);
			this.player = player;
			this.ring = ring;
			this.blocksPlaced = blocksPlaced;
		}

		@Nonnull
		@Override
		public Identifier getId() {
			return ID;
		}

		boolean test(ServerPlayerEntity player, ItemStack ring, int blocksPlaced) {
			return this.player.test(player, null) && this.ring.test(ring) && this.blocksPlaced.test(blocksPlaced);
		}
	}
}
