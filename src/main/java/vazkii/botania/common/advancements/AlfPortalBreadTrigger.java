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
import net.minecraft.predicate.entity.AdvancementEntityPredicateDeserializer;
import net.minecraft.predicate.entity.EntityPredicate;
import net.minecraft.predicate.entity.LocationPredicate;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

import javax.annotation.Nonnull;

import static vazkii.botania.common.lib.ResourceLocationHelper.prefix;

public class AlfPortalBreadTrigger extends AbstractCriterion<AlfPortalBreadTrigger.Instance> {
	public static final Identifier ID = prefix("alf_portal_bread");
	public static final AlfPortalBreadTrigger INSTANCE = new AlfPortalBreadTrigger();

	private AlfPortalBreadTrigger() {}

	@Nonnull
	@Override
	public Identifier getId() {
		return ID;
	}

	@Nonnull
	@Override
	public AlfPortalBreadTrigger.Instance conditionsFromJson(@Nonnull JsonObject json, EntityPredicate.Extended playerPredicate, AdvancementEntityPredicateDeserializer conditions) {
		return new AlfPortalBreadTrigger.Instance(playerPredicate, LocationPredicate.fromJson(json.get("portal_location")));
	}

	public void trigger(ServerPlayerEntity player, BlockPos portal) {
		this.test(player, instance -> instance.test(player.getServerWorld(), portal));
	}

	static class Instance extends AbstractCriterionConditions {
		private final LocationPredicate portal;

		Instance(EntityPredicate.Extended playerPredicate, LocationPredicate portal) {
			super(ID, playerPredicate);
			this.portal = portal;
		}

		@Nonnull
		@Override
		public Identifier getId() {
			return ID;
		}

		boolean test(ServerWorld world, BlockPos portal) {
			return this.portal.test(world, portal.getX(), portal.getY(), portal.getZ());
		}
	}
}
