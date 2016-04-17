package vazkii.botania.common.core.loot;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.conditions.LootCondition;
import vazkii.botania.common.entity.EntityDoppleganger;
import vazkii.botania.common.lib.LibMisc;

import java.util.Random;

public class TrueGuardianKiller implements LootCondition {

    @Override
    public boolean testCondition(Random rand, LootContext context) {
        return context.getLootedEntity() instanceof EntityDoppleganger
                && context.getKillerPlayer() == ((EntityDoppleganger) context.getLootedEntity()).trueKiller;
    }

    public static class Serializer extends LootCondition.Serializer<TrueGuardianKiller> {
        protected Serializer() {
            super(new ResourceLocation(LibMisc.MOD_ID, "true_guardian_killer"), TrueGuardianKiller.class);
        }

        @Override
        public void serialize(JsonObject json, TrueGuardianKiller value, JsonSerializationContext context) {}

        @Override
        public TrueGuardianKiller deserialize(JsonObject json, JsonDeserializationContext context) {
            return new TrueGuardianKiller();
        }
    }

}
