package vazkii.botania.common.core.loot;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.conditions.LootCondition;
import vazkii.botania.common.core.handler.ConfigHandler;
import vazkii.botania.common.lib.LibMisc;

import java.util.Random;

public class EnableRelics implements LootCondition {

    @Override
    public boolean testCondition(Random rand, LootContext context) {
        return ConfigHandler.relicsEnabled;
    }

    public static class Serializer extends LootCondition.Serializer<EnableRelics> {
        protected Serializer() {
            super(new ResourceLocation(LibMisc.MOD_ID, "enable_relics"), EnableRelics.class);
        }

        @Override
        public void serialize(JsonObject json, EnableRelics value, JsonSerializationContext context) {}

        @Override
        public EnableRelics deserialize(JsonObject json, JsonDeserializationContext context) {
            return new EnableRelics();
        }
    }

}
