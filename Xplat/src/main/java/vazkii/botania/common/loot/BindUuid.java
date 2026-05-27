/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.loot;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.functions.LootItemConditionalFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;

import vazkii.botania.api.item.Relic;

import java.util.List;

public class BindUuid extends LootItemConditionalFunction {
	public static final MapCodec<BindUuid> CODEC = RecordCodecBuilder.mapCodec(instance -> commonFields(instance).apply(instance, BindUuid::new));

	public BindUuid(List<LootItemCondition> predicates) {
		super(predicates);
	}

	@Override
	public ItemStack run(ItemStack stack, LootContext context) {
		if (context.getParamOrNull(LootContextParams.ATTACKING_ENTITY) instanceof Player player) {
			var relic = Relic.LOOKUP.find(stack);
			if (relic != null) {
				relic.bindToUUID(player.getUUID());
			}
		}

		return stack;
	}

	@Override
	public LootItemFunctionType<? extends BindUuid> getType() {
		return BotaniaLootModifiers.BIND_UUID;
	}

	public static LootItemConditionalFunction.Builder<?> builder() {
		return new Builder();
	}

	private static class Builder extends LootItemConditionalFunction.Builder<Builder> {

		@Override
		protected Builder getThis() {
			return this;
		}

		@Override
		public LootItemFunction build() {
			return new BindUuid(getConditions());
		}
	}
}
