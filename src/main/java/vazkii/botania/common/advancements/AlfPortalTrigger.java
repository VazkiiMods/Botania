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
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;

import vazkii.botania.common.advancements.AlfPortalTrigger.Instance;

import javax.annotation.Nonnull;

import static vazkii.botania.common.lib.ResourceLocationHelper.prefix;

public class AlfPortalTrigger extends SimpleCriterionTrigger<AlfPortalTrigger.Instance> {
	public static final ResourceLocation ID = prefix("open_elf_portal");
	public static final AlfPortalTrigger INSTANCE = new AlfPortalTrigger();

	private AlfPortalTrigger() {}

	@Nonnull
	@Override
	public ResourceLocation getId() {
		return ID;
	}

	@Nonnull
	@Override
	public Instance createInstance(@Nonnull JsonObject json, EntityPredicate.Composite playerPred, DeserializationContext conditions) {
		return new Instance(playerPred, ItemPredicate.fromJson(json.get("wand")), LocationPredicate.fromJson(json.get("location")));
	}

	public void trigger(ServerPlayer player, ServerLevel world, BlockPos pos, ItemStack wand) {
		trigger(player, instance -> instance.test(world, pos, wand));
	}

	public static class Instance extends AbstractCriterionTriggerInstance {
		private final ItemPredicate wand;
		private final LocationPredicate pos;

		public Instance(EntityPredicate.Composite playerPred, ItemPredicate predicate, LocationPredicate pos) {
			super(ID, playerPred);
			this.wand = predicate;
			this.pos = pos;
		}

		@Nonnull
		@Override
		public ResourceLocation getCriterion() {
			return ID;
		}

		boolean test(ServerLevel world, BlockPos pos, ItemStack wand) {
			return this.wand.matches(wand) && this.pos.matches(world, pos.getX(), pos.getY(), pos.getZ());
		}

		public ItemPredicate getWand() {
			return this.wand;
		}

		public LocationPredicate getPos() {
			return this.pos;
		}
	}
}
