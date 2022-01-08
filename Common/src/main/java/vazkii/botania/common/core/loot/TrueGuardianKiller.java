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

import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;

import vazkii.botania.common.entity.EntityDoppleganger;

import javax.annotation.Nonnull;

public class TrueGuardianKiller implements LootItemCondition {

	@Override
	public boolean test(@Nonnull LootContext context) {
		Entity victim = context.getParamOrNull(LootContextParams.THIS_ENTITY);
		return victim instanceof EntityDoppleganger
				&& context.getParamOrNull(LootContextParams.KILLER_ENTITY) == ((EntityDoppleganger) victim).trueKiller;
	}

	@Override
	public LootItemConditionType getType() {
		return ModLootModifiers.TRUE_GUARDIAN_KILLER;
	}

	public static class Serializer implements net.minecraft.world.level.storage.loot.Serializer<TrueGuardianKiller> {
		@Override
		public void serialize(JsonObject json, TrueGuardianKiller condition, JsonSerializationContext ctx) {}

		@Override
		public TrueGuardianKiller deserialize(JsonObject json, JsonDeserializationContext ctx) {
			return new TrueGuardianKiller();
		}
	}

}
