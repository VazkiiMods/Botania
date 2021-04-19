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
import net.minecraft.advancements.criterion.LocationPredicate;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.loot.ConditionArrayParser;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;

import javax.annotation.Nonnull;

import static vazkii.botania.common.lib.ResourceLocationHelper.prefix;

public class AlfPortalBreadTrigger extends AbstractCriterionTrigger<AlfPortalBreadTrigger.Instance> {
	public static final ResourceLocation ID = prefix("alf_portal_bread");
	public static final AlfPortalBreadTrigger INSTANCE = new AlfPortalBreadTrigger();

	private AlfPortalBreadTrigger() {}

	@Nonnull
	@Override
	public ResourceLocation getId() {
		return ID;
	}

	@Nonnull
	@Override
	public AlfPortalBreadTrigger.Instance deserializeTrigger(@Nonnull JsonObject json, EntityPredicate.AndPredicate playerPredicate, ConditionArrayParser conditions) {
		return new AlfPortalBreadTrigger.Instance(playerPredicate, LocationPredicate.deserialize(json.get("portal_location")));
	}

	public void trigger(ServerPlayerEntity player, BlockPos portal) {
		this.triggerListeners(player, instance -> instance.test(player.getServerWorld(), portal));
	}

	public static class Instance extends CriterionInstance {
		private final LocationPredicate portal;

		public Instance(EntityPredicate.AndPredicate playerPredicate, LocationPredicate portal) {
			super(ID, playerPredicate);
			this.portal = portal;
		}

		@Nonnull
		@Override
		public ResourceLocation getId() {
			return ID;
		}

		boolean test(ServerWorld world, BlockPos portal) {
			return this.portal.test(world, portal.getX(), portal.getY(), portal.getZ());
		}

		public LocationPredicate getPortal() {
			return this.portal;
		}
	}
}
