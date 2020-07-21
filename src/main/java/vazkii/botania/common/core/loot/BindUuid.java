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

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.condition.LootCondition;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.loot.function.ConditionalLootFunction;
import net.minecraft.loot.function.LootFunctionType;
import vazkii.botania.common.item.ModItems;
import vazkii.botania.common.item.relic.ItemRelic;

import javax.annotation.Nonnull;

public class BindUuid extends ConditionalLootFunction {

	protected BindUuid(LootCondition[] conditionsIn) {
		super(conditionsIn);
	}

	@Nonnull
	@Override
	public ItemStack process(@Nonnull ItemStack stack, @Nonnull LootContext context) {
		if (context.get(LootContextParameters.KILLER_ENTITY) instanceof PlayerEntity) {
			((ItemRelic) ModItems.dice).bindToUUID(context.get(LootContextParameters.KILLER_ENTITY).getUuid(), stack);
		}

		return stack;
	}

	@Override
	public LootFunctionType getType() {
		return ModLootModifiers.BIND_UUID;
	}

	public static class Serializer extends ConditionalLootFunction.Serializer<BindUuid> {
		@Nonnull
		@Override
		public BindUuid fromJson(@Nonnull JsonObject object, @Nonnull JsonDeserializationContext deserializationContext, @Nonnull LootCondition[] conditionsIn) {
			return new BindUuid(conditionsIn);
		}
	}

}
