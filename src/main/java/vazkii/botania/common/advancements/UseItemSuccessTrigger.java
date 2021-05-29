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
import net.minecraft.predicate.entity.LocationPredicate;
import net.minecraft.predicate.item.ItemPredicate;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;

import javax.annotation.Nonnull;

import static vazkii.botania.common.lib.ResourceLocationHelper.prefix;

// Catch-all "used an item and it succeeded" trigger for Botania items, because making a separate
// trigger for each one is dumb.
public class UseItemSuccessTrigger extends AbstractCriterion<UseItemSuccessTrigger.Instance> {
	public static final Identifier ID = prefix("use_item_success");
	public static final UseItemSuccessTrigger INSTANCE = new UseItemSuccessTrigger();

	private UseItemSuccessTrigger() {}

	@Nonnull
	@Override
	public Identifier getId() {
		return ID;
	}

	@Nonnull
	@Override
	public UseItemSuccessTrigger.Instance conditionsFromJson(@Nonnull JsonObject json, @Nonnull EntityPredicate.Extended playerPred, AdvancementEntityPredicateDeserializer conditions) {
		return new UseItemSuccessTrigger.Instance(playerPred, ItemPredicate.fromJson(json.get("item")), LocationPredicate.fromJson(json.get("location")));
	}

	public void trigger(ServerPlayerEntity player, ItemStack stack, ServerWorld world, double x, double y, double z) {
		test(player, instance -> instance.test(stack, world, x, y, z));
	}

	public static class Instance extends AbstractCriterionConditions {
		private final ItemPredicate item;
		private final LocationPredicate location;

		public Instance(EntityPredicate.Extended playerPred, ItemPredicate count, LocationPredicate indexPos) {
			super(ID, playerPred);
			this.item = count;
			this.location = indexPos;
		}

		@Nonnull
		@Override
		public Identifier getId() {
			return ID;
		}

		boolean test(ItemStack stack, ServerWorld world, double x, double y, double z) {
			return this.item.test(stack) && this.location.test(world, x, y, z);
		}

		public ItemPredicate getItem() {
			return this.item;
		}

		public LocationPredicate getLocation() {
			return this.location;
		}
	}
}
