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

import net.minecraft.entity.Entity;
import net.minecraft.loot.condition.LootCondition;
import net.minecraft.loot.condition.LootConditionType;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.util.JsonSerializer;
import vazkii.botania.common.entity.EntityDoppleganger;

import javax.annotation.Nonnull;

public class TrueGuardianKiller implements LootCondition {

	@Override
	public boolean test(@Nonnull LootContext context) {
		Entity victim = context.get(LootContextParameters.THIS_ENTITY);
		return victim instanceof EntityDoppleganger
				&& context.get(LootContextParameters.KILLER_ENTITY) == ((EntityDoppleganger) victim).trueKiller;
	}

	@Override
	public LootConditionType getType() {
		return ModLootModifiers.TRUE_GUARDIAN_KILLER;
	}

	public static class Serializer implements JsonSerializer<TrueGuardianKiller> {
		@Override
		public void toJson(JsonObject json, TrueGuardianKiller condition, JsonSerializationContext ctx) {}

		@Override
		public TrueGuardianKiller fromJson(JsonObject json, JsonDeserializationContext ctx) {
			return new TrueGuardianKiller();
		}
	}

}
