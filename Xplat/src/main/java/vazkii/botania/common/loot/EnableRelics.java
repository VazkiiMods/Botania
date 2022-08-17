/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.loot;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;

import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;

import org.jetbrains.annotations.NotNull;

import vazkii.botania.xplat.BotaniaConfig;

public class EnableRelics implements LootItemCondition {

	@Override
	public boolean test(@NotNull LootContext context) {
		return BotaniaConfig.common().relicsEnabled();
	}

	@Override
	public LootItemConditionType getType() {
		return ModLootModifiers.ENABLE_RELICS;
	}

	public static class Serializer implements net.minecraft.world.level.storage.loot.Serializer<EnableRelics> {
		@Override
		public void serialize(@NotNull JsonObject json, @NotNull EnableRelics value, @NotNull JsonSerializationContext context) {}

		@NotNull
		@Override
		public EnableRelics deserialize(@NotNull JsonObject json, @NotNull JsonDeserializationContext context) {
			return new EnableRelics();
		}
	}

}
