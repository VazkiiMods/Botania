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

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.functions.LootItemConditionalFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;

import org.jetbrains.annotations.NotNull;

import vazkii.botania.xplat.IXplatAbstractions;

public class BindUuid extends LootItemConditionalFunction {

	protected BindUuid(LootItemCondition[] conditionsIn) {
		super(conditionsIn);
	}

	@NotNull
	@Override
	public ItemStack run(@NotNull ItemStack stack, @NotNull LootContext context) {
		if (context.getParamOrNull(LootContextParams.KILLER_ENTITY) instanceof Player player) {
			var relic = IXplatAbstractions.INSTANCE.findRelic(stack);
			if (relic != null) {
				relic.bindToUUID(player.getUUID());
			}
		}

		return stack;
	}

	@Override
	public LootItemFunctionType getType() {
		return BotaniaLootModifiers.BIND_UUID;
	}

	public static class Serializer extends LootItemConditionalFunction.Serializer<BindUuid> {
		@NotNull
		@Override
		public BindUuid deserialize(@NotNull JsonObject object, @NotNull JsonDeserializationContext deserializationContext, @NotNull LootItemCondition[] conditionsIn) {
			return new BindUuid(conditionsIn);
		}
	}

}
