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

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.functions.LootItemConditionalFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;

import vazkii.botania.common.item.ModItems;
import vazkii.botania.common.item.relic.ItemRelic;

import javax.annotation.Nonnull;

public class BindUuid extends LootItemConditionalFunction {

	protected BindUuid(LootItemCondition[] conditionsIn) {
		super(conditionsIn);
	}

	@Nonnull
	@Override
	public ItemStack run(@Nonnull ItemStack stack, @Nonnull LootContext context) {
		if (context.getParamOrNull(LootContextParams.KILLER_ENTITY) instanceof Player) {
			((ItemRelic) ModItems.dice).bindToUUID(context.getParamOrNull(LootContextParams.KILLER_ENTITY).getUUID(), stack);
		}

		return stack;
	}

	@Override
	public LootItemFunctionType getType() {
		return ModLootModifiers.BIND_UUID;
	}

	public static class Serializer extends LootItemConditionalFunction.Serializer<BindUuid> {
		@Nonnull
		@Override
		public BindUuid deserialize(@Nonnull JsonObject object, @Nonnull JsonDeserializationContext deserializationContext, @Nonnull LootItemCondition[] conditionsIn) {
			return new BindUuid(conditionsIn);
		}
	}

}
