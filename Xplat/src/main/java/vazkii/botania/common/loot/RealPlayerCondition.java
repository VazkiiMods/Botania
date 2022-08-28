/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.loot;

import com.google.common.collect.ImmutableSet;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParam;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;

import org.jetbrains.annotations.NotNull;

import vazkii.botania.common.helper.PlayerHelper;

import java.util.Set;

public class RealPlayerCondition implements LootItemCondition {
	public static final RealPlayerCondition INSTANCE = new RealPlayerCondition();

	private RealPlayerCondition() {}

	@Override
	public boolean test(LootContext lootContext) {
		Player player = lootContext.getParamOrNull(LootContextParams.LAST_DAMAGE_PLAYER);
		return PlayerHelper.isTruePlayer(player);
	}

	@NotNull
	@Override
	public Set<LootContextParam<?>> getReferencedContextParams() {
		return ImmutableSet.of(LootContextParams.LAST_DAMAGE_PLAYER);
	}

	@NotNull
	@Override
	public LootItemConditionType getType() {
		return ModLootModifiers.KILLED_BY_REAL_PLAYER;
	}

	public static class Serializer implements net.minecraft.world.level.storage.loot.Serializer<RealPlayerCondition> {
		@Override
		public void serialize(@NotNull JsonObject json, @NotNull RealPlayerCondition value, @NotNull JsonSerializationContext context) {}

		@NotNull
		@Override
		public RealPlayerCondition deserialize(@NotNull JsonObject json, @NotNull JsonDeserializationContext context) {
			return INSTANCE;
		}
	}
}
