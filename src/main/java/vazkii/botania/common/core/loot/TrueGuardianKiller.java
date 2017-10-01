package vazkii.botania.common.core.loot;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.conditions.LootCondition;
import vazkii.botania.common.entity.EntityDoppleganger;
import vazkii.botania.common.lib.LibMisc;

import javax.annotation.Nonnull;
import java.util.Random;

public class TrueGuardianKiller implements LootCondition {

	@Override
	public boolean testCondition(@Nonnull Random rand, @Nonnull LootContext context) {
		return context.getLootedEntity() instanceof EntityDoppleganger
				&& context.getKillerPlayer() == ((EntityDoppleganger) context.getLootedEntity()).trueKiller;
	}

	public static class Serializer extends LootCondition.Serializer<TrueGuardianKiller> {
		protected Serializer() {
			super(new ResourceLocation(LibMisc.MOD_ID, "true_guardian_killer"), TrueGuardianKiller.class);
		}

		@Override
		public void serialize(@Nonnull JsonObject json, @Nonnull TrueGuardianKiller value, @Nonnull JsonSerializationContext context) {}

		@Nonnull
		@Override
		public TrueGuardianKiller deserialize(@Nonnull JsonObject json, @Nonnull JsonDeserializationContext context) {
			return new TrueGuardianKiller();
		}
	}

}
