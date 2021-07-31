/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.core.loot;

import com.google.common.collect.ImmutableSet;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.loot.condition.LootCondition;
import net.minecraft.loot.condition.LootConditionType;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.context.LootContextParameter;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.util.JsonSerializer;

import vazkii.botania.common.entity.EntityDoppleganger;

import javax.annotation.Nonnull;

import java.util.Set;

public class RealPlayerCondition implements LootCondition {
	public static final RealPlayerCondition INSTANCE = new RealPlayerCondition();

	private RealPlayerCondition() {}

	@Override
	public boolean test(LootContext lootContext) {
		PlayerEntity player = lootContext.get(LootContextParameters.LAST_DAMAGE_PLAYER);
		return EntityDoppleganger.isTruePlayer(player);
	}

	@Nonnull
	@Override
	public Set<LootContextParameter<?>> getRequiredParameters() {
		return ImmutableSet.of(LootContextParameters.LAST_DAMAGE_PLAYER);
	}

	@Nonnull
	@Override
	public LootConditionType getType() {
		return ModLootModifiers.KILLED_BY_REAL_PLAYER;
	}

	public static class Serializer implements JsonSerializer<RealPlayerCondition> {
		@Override
		public void toJson(@Nonnull JsonObject json, @Nonnull RealPlayerCondition value, @Nonnull JsonSerializationContext context) {}

		@Nonnull
		@Override
		public RealPlayerCondition fromJson(@Nonnull JsonObject json, @Nonnull JsonDeserializationContext context) {
			return INSTANCE;
		}
	}
}
