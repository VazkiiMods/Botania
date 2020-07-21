/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.core.loot;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import vazkii.botania.common.core.handler.ConfigHandler;

import javax.annotation.Nonnull;
import net.minecraft.loot.condition.LootCondition;
import net.minecraft.loot.condition.LootConditionType;
import net.minecraft.loot.context.LootContext;
import net.minecraft.util.JsonSerializer;

public class EnableRelics implements LootCondition {

	@Override
	public boolean test(@Nonnull LootContext context) {
		return ConfigHandler.COMMON.relicsEnabled.get();
	}

	@Override
	public LootConditionType getType() {
		return ModLootModifiers.ENABLE_RELICS;
	}

	public static class Serializer implements JsonSerializer<EnableRelics> {
		@Override
		public void func_230424_a_(@Nonnull JsonObject json, @Nonnull EnableRelics value, @Nonnull JsonSerializationContext context) {}

		@Nonnull
		@Override
		public EnableRelics fromJson(@Nonnull JsonObject json, @Nonnull JsonDeserializationContext context) {
			return new EnableRelics();
		}
	}

}
