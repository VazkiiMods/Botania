/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.core.loot;

import com.google.gson.JsonObject;

import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.conditions.ILootCondition;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.common.loot.LootModifier;

import javax.annotation.Nonnull;

import java.util.List;

/**
 * Global loot modifier that calls another loot table and appends all of the yielded stacks to the existing loot
 */
public class CallTableModifier extends LootModifier {
	private final ResourceLocation tableId;

	protected CallTableModifier(ILootCondition[] conditionsIn, ResourceLocation table) {
		super(conditionsIn);
		this.tableId = table;
	}

	@Nonnull
	@Override
	protected List<ItemStack> doApply(List<ItemStack> generatedLoot, LootContext context) {
		LootTable table = context.getWorld().getServer().getLootTableManager().getLootTableFromLocation(tableId);
		table.generate(context, generatedLoot::add);
		return generatedLoot;
	}

	public static class Serializer extends GlobalLootModifierSerializer<CallTableModifier> {
		@Override
		public CallTableModifier read(ResourceLocation location, JsonObject object, ILootCondition[] ailootcondition) {
			String table = JSONUtils.getString(object, "table");
			return new CallTableModifier(ailootcondition, new ResourceLocation(table));
		}
	}
}
