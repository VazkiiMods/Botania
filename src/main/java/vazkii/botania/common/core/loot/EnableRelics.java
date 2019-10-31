package vazkii.botania.common.core.loot;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.conditions.ILootCondition;
import vazkii.botania.common.core.handler.ConfigHandler;
import vazkii.botania.common.lib.LibMisc;

import javax.annotation.Nonnull;

public class EnableRelics implements ILootCondition {

	@Override
	public boolean test(@Nonnull LootContext context) {
		return ConfigHandler.COMMON.relicsEnabled.get();
	}

	public static class Serializer extends ILootCondition.AbstractSerializer<EnableRelics> {
		public Serializer() {
			super(new ResourceLocation(LibMisc.MOD_ID, "enable_relics"), EnableRelics.class);
		}

		@Override
		public void serialize(@Nonnull JsonObject json, @Nonnull EnableRelics value, @Nonnull JsonSerializationContext context) {}

		@Nonnull
		@Override
		public EnableRelics deserialize(@Nonnull JsonObject json, @Nonnull JsonDeserializationContext context) {
			return new EnableRelics();
		}
	}

}
