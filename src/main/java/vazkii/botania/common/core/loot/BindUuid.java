package vazkii.botania.common.core.loot;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.conditions.LootCondition;
import net.minecraft.world.storage.loot.functions.LootFunction;
import vazkii.botania.common.item.ModItems;
import vazkii.botania.common.item.relic.ItemRelic;
import vazkii.botania.common.lib.LibMisc;

import javax.annotation.Nonnull;
import java.util.Random;

public class BindUuid extends LootFunction {

	protected BindUuid(LootCondition[] conditionsIn) {
		super(conditionsIn);
	}

	@Nonnull
	@Override
	public ItemStack apply(@Nonnull ItemStack stack, @Nonnull Random rand, @Nonnull LootContext context) {
		if (context.getKillerPlayer() != null) {
			((ItemRelic) ModItems.dice).bindToUUID(context.getKillerPlayer().getUniqueID(), stack);
		}

		return stack;
	}

	public static class Serializer extends LootFunction.Serializer<BindUuid> {
		protected Serializer() {
			super(new ResourceLocation(LibMisc.MOD_ID, "bind_uuid"), BindUuid.class);
		}

		@Override
		public void serialize(@Nonnull JsonObject object, @Nonnull BindUuid functionClazz, @Nonnull JsonSerializationContext serializationContext) {}

		@Nonnull
		@Override
		public BindUuid deserialize(@Nonnull JsonObject object, @Nonnull JsonDeserializationContext deserializationContext, @Nonnull LootCondition[] conditionsIn) {
			return new BindUuid(conditionsIn);
		}
	}

}
