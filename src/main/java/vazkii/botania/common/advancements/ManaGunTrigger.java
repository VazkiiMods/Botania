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

import net.minecraft.advancements.criterion.AbstractCriterionTrigger;
import net.minecraft.advancements.criterion.CriterionInstance;
import net.minecraft.advancements.criterion.EntityPredicate;
import net.minecraft.advancements.criterion.ItemPredicate;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.ConditionArrayParser;
import net.minecraft.util.ResourceLocation;

import vazkii.botania.common.item.ItemManaGun;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import static vazkii.botania.common.lib.ResourceLocationHelper.prefix;

public class ManaGunTrigger extends AbstractCriterionTrigger<ManaGunTrigger.Instance> {
	public static final ResourceLocation ID = prefix("fire_mana_blaster");
	public static final ManaGunTrigger INSTANCE = new ManaGunTrigger();

	private ManaGunTrigger() {}

	@Nonnull
	@Override
	public ResourceLocation getId() {
		return ID;
	}

	@Nonnull
	@Override
	public ManaGunTrigger.Instance deserializeTrigger(@Nonnull JsonObject json, EntityPredicate.AndPredicate playerPred, ConditionArrayParser conditions) {
		Boolean desu = json.get("desu") == null ? null : json.get("desu").getAsBoolean();
		return new ManaGunTrigger.Instance(playerPred, ItemPredicate.deserialize(json.get("item")), EntityPredicate.deserialize(json.get("user")), desu);
	}

	public void trigger(ServerPlayerEntity player, ItemStack stack) {
		triggerListeners(player, instance -> instance.test(stack, player));
	}

	public static class Instance extends CriterionInstance {
		private final ItemPredicate item;
		private final EntityPredicate user;
		@Nullable
		private final Boolean desu;

		public Instance(EntityPredicate.AndPredicate entityPred, ItemPredicate count, EntityPredicate user, Boolean desu) {
			super(ID, entityPred);
			this.item = count;
			this.user = user;
			this.desu = desu;
		}

		@Nonnull
		@Override
		public ResourceLocation getId() {
			return ID;
		}

		boolean test(ItemStack stack, ServerPlayerEntity entity) {
			return this.item.test(stack) && this.user.test(entity, entity)
					&& (desu == null || desu == ItemManaGun.isSugoiKawaiiDesuNe(stack));
		}

		public ItemPredicate getItem() {
			return this.item;
		}

		public EntityPredicate getUser() {
			return this.user;
		}

		@Nullable
		public Boolean getDesu() {
			return this.desu;
		}
	}
}
