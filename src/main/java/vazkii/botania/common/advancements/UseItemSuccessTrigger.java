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

import net.minecraft.advancements.critereon.AbstractCriterionTriggerInstance;
import net.minecraft.advancements.critereon.DeserializationContext;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.advancements.critereon.LocationPredicate;
import net.minecraft.advancements.critereon.SimpleCriterionTrigger;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nonnull;

import static vazkii.botania.common.lib.ResourceLocationHelper.prefix;

// Catch-all "used an item and it succeeded" trigger for Botania items, because making a separate
// trigger for each one is dumb.
public class UseItemSuccessTrigger extends SimpleCriterionTrigger<UseItemSuccessTrigger.Instance> {
	public static final ResourceLocation ID = prefix("use_item_success");
	public static final UseItemSuccessTrigger INSTANCE = new UseItemSuccessTrigger();

	private UseItemSuccessTrigger() {}

	@Nonnull
	@Override
	public ResourceLocation getId() {
		return ID;
	}

	@Nonnull
	@Override
	public UseItemSuccessTrigger.Instance createInstance(@Nonnull JsonObject json, @Nonnull EntityPredicate.Composite playerPred, DeserializationContext conditions) {
		return new UseItemSuccessTrigger.Instance(playerPred, ItemPredicate.fromJson(json.get("item")), LocationPredicate.fromJson(json.get("location")));
	}

	public void trigger(ServerPlayer player, ItemStack stack, ServerLevel world, double x, double y, double z) {
		trigger(player, instance -> instance.test(stack, world, x, y, z));
	}

	public static class Instance extends AbstractCriterionTriggerInstance {
		private final ItemPredicate item;
		private final LocationPredicate location;

		public Instance(EntityPredicate.Composite playerPred, ItemPredicate count, LocationPredicate indexPos) {
			super(ID, playerPred);
			this.item = count;
			this.location = indexPos;
		}

		@Nonnull
		@Override
		public ResourceLocation getCriterion() {
			return ID;
		}

		boolean test(ItemStack stack, ServerLevel world, double x, double y, double z) {
			return this.item.matches(stack) && this.location.matches(world, x, y, z);
		}

		public ItemPredicate getItem() {
			return this.item;
		}

		public LocationPredicate getLocation() {
			return this.location;
		}
	}
}
