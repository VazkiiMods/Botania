package vazkii.botania.common.core.loot;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.LootFunction;
import net.minecraft.world.storage.loot.conditions.ILootCondition;
import net.minecraft.world.storage.loot.functions.ILootFunction;
import vazkii.botania.common.item.ModItems;
import vazkii.botania.common.item.relic.ItemRelic;
import vazkii.botania.common.lib.LibMisc;

import javax.annotation.Nonnull;
import java.util.Random;

public class BindUuid extends LootFunction {

	protected BindUuid(ILootCondition[] conditionsIn) {
		super(conditionsIn);
	}

	@Nonnull
	@Override
	public ItemStack doApply(@Nonnull ItemStack stack, @Nonnull LootContext context) {
		if (context.getKillerPlayer() != null) {
			((ItemRelic) ModItems.dice).bindToUUID(context.getKillerPlayer().getUniqueID(), stack);
		}

		return stack;
	}

	public static class Serializer extends ILootFunction.Serializer<BindUuid> {
		public Serializer() {
			super(new ResourceLocation(LibMisc.MOD_ID, "bind_uuid"), BindUuid.class);
		}

		@Override
		public void serialize(@Nonnull JsonObject object, @Nonnull BindUuid functionClazz, @Nonnull JsonSerializationContext serializationContext) {}

		@Nonnull
		@Override
		public BindUuid deserialize(@Nonnull JsonObject object, @Nonnull JsonDeserializationContext deserializationContext, @Nonnull ILootCondition[] conditionsIn) {
			return new BindUuid(conditionsIn);
		}
	}

}
