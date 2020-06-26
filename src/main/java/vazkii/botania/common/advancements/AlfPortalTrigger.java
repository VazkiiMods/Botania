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
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;

import vazkii.botania.common.lib.LibMisc;

import javax.annotation.Nonnull;

public class AlfPortalTrigger extends AbstractCriterionTrigger<AlfPortalTrigger.Instance> {
	public static final ResourceLocation ID = new ResourceLocation(LibMisc.MOD_ID, "open_elf_portal");
	public static final AlfPortalTrigger INSTANCE = new AlfPortalTrigger();

	private AlfPortalTrigger() {}

	@Nonnull
	@Override
	public ResourceLocation getId() {
		return ID;
	}

	@Nonnull
	@Override
	public Instance func_230241_b_(@Nonnull JsonObject json, EntityPredicate.AndPredicate playerPred, ConditionArrayParser conditions) {
		return new Instance(playerPred, ItemPredicate.deserialize(json.get("wand")), LocationPredicate.deserialize(json.get("location")));
	}

	public void trigger(ServerPlayerEntity player, ServerWorld world, BlockPos pos, ItemStack wand) {
		func_235959_a_(player, instance -> instance.test(world, pos, wand));
	}

	static class Instance extends CriterionInstance {
		private final ItemPredicate wand;
		private final LocationPredicate pos;

		Instance(EntityPredicate.AndPredicate playerPred, ItemPredicate predicate, LocationPredicate pos) {
			super(ID, playerPred);
			this.wand = predicate;
			this.pos = pos;
		}

		@Nonnull
		@Override
		public ResourceLocation getId() {
			return ID;
		}

		boolean test(ServerWorld world, BlockPos pos, ItemStack wand) {
			return this.wand.test(wand) && this.pos.test(world, pos.getX(), pos.getY(), pos.getZ());
		}
	}
}
