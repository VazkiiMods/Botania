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
import net.minecraft.loot.ILootSerializer;
import net.minecraft.loot.LootConditionType;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootParameters;
import net.minecraft.loot.conditions.ILootCondition;

import vazkii.botania.common.entity.EntityDoppleganger;

import javax.annotation.Nonnull;

public class TrueGuardianKiller implements ILootCondition {

	@Override
	public boolean test(@Nonnull LootContext context) {
		Entity victim = context.get(LootParameters.THIS_ENTITY);
		return victim instanceof EntityDoppleganger
				&& context.get(LootParameters.KILLER_ENTITY) == ((EntityDoppleganger) victim).trueKiller;
	}

	@Override
	public LootConditionType func_230419_b_() {
		return ModLootModifiers.TRUE_GUARDIAN_KILLER;
	}

	public static class Serializer implements ILootSerializer<TrueGuardianKiller> {
		@Override
		public void serialize(JsonObject json, TrueGuardianKiller condition, JsonSerializationContext ctx) {}

		@Override
		public TrueGuardianKiller deserialize(JsonObject json, JsonDeserializationContext ctx) {
			return new TrueGuardianKiller();
		}
	}

}
