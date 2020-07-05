/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.core.loot;

import com.google.common.collect.ImmutableList;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import net.minecraft.item.ItemStack;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.conditions.ILootCondition;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.common.loot.LootModifier;

import javax.annotation.Nonnull;

import java.util.List;

public class AddRandomItemModifier extends LootModifier {
	private final List<ItemStack> items;

	protected AddRandomItemModifier(ILootCondition[] conditionsIn, List<ItemStack> items) {
		super(conditionsIn);
		this.items = items;
	}

	@Nonnull
	@Override
	protected List<ItemStack> doApply(List<ItemStack> generatedLoot, LootContext context) {
		ItemStack stack = items.get(context.getRandom().nextInt(items.size()));
		generatedLoot.add(stack.copy());
		return generatedLoot;
	}

	public static class Serializer extends GlobalLootModifierSerializer<AddRandomItemModifier> {
		@Override
		public AddRandomItemModifier read(ResourceLocation location, JsonObject object, ILootCondition[] ailootcondition) {
			JsonArray items = JSONUtils.getJsonArray(object, "items");
			ImmutableList.Builder<ItemStack> builder = ImmutableList.builder();
			for (JsonElement item : items) {
				builder.add(CraftingHelper.getItemStack(item.getAsJsonObject(), true));
			}
			return new AddRandomItemModifier(ailootcondition, builder.build());
		}
	}
}
