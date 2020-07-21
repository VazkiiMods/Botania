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
import net.minecraft.loot.LootTable;
import net.minecraft.loot.condition.LootCondition;
import net.minecraft.loot.context.LootContext;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;

import javax.annotation.Nonnull;

import java.util.List;

/**
 * Global loot modifier that calls another loot table and appends all of the yielded stacks to the existing loot
 */
public class CallTableModifier extends LootModifier {
	private final Identifier tableId;

	protected CallTableModifier(LootCondition[] conditionsIn, Identifier table) {
		super(conditionsIn);
		this.tableId = table;
	}

	@Nonnull
	@Override
	protected List<ItemStack> doApply(List<ItemStack> generatedLoot, LootContext context) {
		LootTable table = context.getWorld().getServer().getLootManager().getTable(tableId);
		table.generateLoot(context, generatedLoot::add);
		return generatedLoot;
	}

	public static class Serializer extends GlobalLootModifierSerializer<CallTableModifier> {
		@Override
		public CallTableModifier read(Identifier location, JsonObject object, LootCondition[] ailootcondition) {
			String table = JsonHelper.getString(object, "table");
			return new CallTableModifier(ailootcondition, new Identifier(table));
		}
	}
}
