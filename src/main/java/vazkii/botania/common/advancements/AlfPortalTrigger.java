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
import net.minecraft.util.math.BlockPos;
import vazkii.botania.common.advancements.AlfPortalTrigger.Instance;
import javax.annotation.Nonnull;

import static vazkii.botania.common.lib.ResourceLocationHelper.prefix;

public class AlfPortalTrigger extends AbstractCriterion<AlfPortalTrigger.Instance> {
	public static final Identifier ID = prefix("open_elf_portal");
	public static final AlfPortalTrigger INSTANCE = new AlfPortalTrigger();

	private AlfPortalTrigger() {}

	@Nonnull
	@Override
	public Identifier getId() {
		return ID;
	}

	@Nonnull
	@Override
	public Instance conditionsFromJson(@Nonnull JsonObject json, EntityPredicate.Extended playerPred, AdvancementEntityPredicateDeserializer conditions) {
		return new Instance(playerPred, ItemPredicate.fromJson(json.get("wand")), LocationPredicate.fromJson(json.get("location")));
	}

	public void trigger(ServerPlayerEntity player, ServerWorld world, BlockPos pos, ItemStack wand) {
		test(player, instance -> instance.test(world, pos, wand));
	}

	static class Instance extends AbstractCriterionConditions {
		private final ItemPredicate wand;
		private final LocationPredicate pos;

		Instance(EntityPredicate.Extended playerPred, ItemPredicate predicate, LocationPredicate pos) {
			super(ID, playerPred);
			this.wand = predicate;
			this.pos = pos;
		}

		@Nonnull
		@Override
		public Identifier getId() {
			return ID;
		}

		boolean test(ServerWorld world, BlockPos pos, ItemStack wand) {
			return this.wand.test(wand) && this.pos.test(world, pos.getX(), pos.getY(), pos.getZ());
		}
	}
}
