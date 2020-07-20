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
import net.minecraft.world.server.ServerWorld;

import javax.annotation.Nonnull;

import static vazkii.botania.common.lib.ResourceLocationHelper.prefix;

// Catch-all "used an item and it succeeded" trigger for Botania items, because making a separate
// trigger for each one is dumb.
public class UseItemSuccessTrigger extends AbstractCriterionTrigger<UseItemSuccessTrigger.Instance> {
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
	public UseItemSuccessTrigger.Instance func_230241_b_(@Nonnull JsonObject json, @Nonnull EntityPredicate.AndPredicate playerPred, ConditionArrayParser conditions) {
		return new UseItemSuccessTrigger.Instance(playerPred, ItemPredicate.deserialize(json.get("item")), LocationPredicate.deserialize(json.get("location")));
	}

	public void trigger(ServerPlayerEntity player, ItemStack stack, ServerWorld world, double x, double y, double z) {
		func_235959_a_(player, instance -> instance.test(stack, world, x, y, z));
	}

	static class Instance extends CriterionInstance {
		private final ItemPredicate item;
		private final LocationPredicate location;

		Instance(EntityPredicate.AndPredicate playerPred, ItemPredicate count, LocationPredicate indexPos) {
			super(ID, playerPred);
			this.item = count;
			this.location = indexPos;
		}

		@Nonnull
		@Override
		public ResourceLocation getId() {
			return ID;
		}

		boolean test(ItemStack stack, ServerWorld world, double x, double y, double z) {
			return this.item.test(stack) && this.location.test(world, x, y, z);
		}
	}
}
