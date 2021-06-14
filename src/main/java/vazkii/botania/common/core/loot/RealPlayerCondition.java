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
import net.minecraft.loot.ILootSerializer;
import net.minecraft.loot.LootConditionType;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootParameter;
import net.minecraft.loot.LootParameters;
import net.minecraft.loot.conditions.ILootCondition;

import vazkii.botania.common.entity.EntityDoppleganger;

import javax.annotation.Nonnull;

import java.util.Set;

public class RealPlayerCondition implements ILootCondition {
	public static final RealPlayerCondition INSTANCE = new RealPlayerCondition();

	private RealPlayerCondition() {}

	@Override
	public boolean test(LootContext lootContext) {
		PlayerEntity player = lootContext.get(LootParameters.LAST_DAMAGE_PLAYER);
		return EntityDoppleganger.isTruePlayer(player);
	}

	@Nonnull
	@Override
	public Set<LootParameter<?>> getRequiredParameters() {
		return ImmutableSet.of(LootParameters.LAST_DAMAGE_PLAYER);
	}

	@Nonnull
	@Override
	public LootConditionType func_230419_b_() {
		return ModLootModifiers.KILLED_BY_REAL_PLAYER;
	}

	public static class Serializer implements ILootSerializer<RealPlayerCondition> {
		@Override
		public void serialize(@Nonnull JsonObject json, @Nonnull RealPlayerCondition value, @Nonnull JsonSerializationContext context) {}

		@Nonnull
		@Override
		public RealPlayerCondition deserialize(@Nonnull JsonObject json, @Nonnull JsonDeserializationContext context) {
			return INSTANCE;
		}
	}
}
