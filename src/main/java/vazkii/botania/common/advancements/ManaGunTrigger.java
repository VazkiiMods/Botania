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
import net.minecraft.predicate.item.ItemPredicate;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import vazkii.botania.common.item.ItemManaGun;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import static vazkii.botania.common.lib.ResourceLocationHelper.prefix;

public class ManaGunTrigger extends AbstractCriterion<ManaGunTrigger.Instance> {
	private static final Identifier ID = prefix("fire_mana_blaster");
	public static final ManaGunTrigger INSTANCE = new ManaGunTrigger();

	private ManaGunTrigger() {}

	@Nonnull
	@Override
	public Identifier getId() {
		return ID;
	}

	@Nonnull
	@Override
	public ManaGunTrigger.Instance conditionsFromJson(@Nonnull JsonObject json, EntityPredicate.Extended playerPred, AdvancementEntityPredicateDeserializer conditions) {
		Boolean desu = json.get("desu") == null ? null : json.get("desu").getAsBoolean();
		return new ManaGunTrigger.Instance(playerPred, ItemPredicate.fromJson(json.get("item")), EntityPredicate.fromJson(json.get("user")), desu);
	}

	public void trigger(ServerPlayerEntity player, ItemStack stack) {
		test(player, instance -> instance.test(stack, player));
	}

	static class Instance extends AbstractCriterionConditions {
		private final ItemPredicate item;
		private final EntityPredicate user;
		@Nullable
		private final Boolean desu;

		Instance(EntityPredicate.Extended entityPred, ItemPredicate count, EntityPredicate user, Boolean desu) {
			super(ID, entityPred);
			this.item = count;
			this.user = user;
			this.desu = desu;
		}

		@Nonnull
		@Override
		public Identifier getId() {
			return ID;
		}

		boolean test(ItemStack stack, ServerPlayerEntity entity) {
			return this.item.test(stack) && this.user.test(entity, entity)
					&& (desu == null || desu == ItemManaGun.isSugoiKawaiiDesuNe(stack));
		}
	}
}
