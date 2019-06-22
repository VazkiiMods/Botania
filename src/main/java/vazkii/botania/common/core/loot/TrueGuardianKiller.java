package vazkii.botania.common.core.loot;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.LootParameters;
import net.minecraft.world.storage.loot.conditions.ILootCondition;
import net.minecraft.world.storage.loot.conditions.ILootCondition;
import vazkii.botania.common.entity.EntityDoppleganger;
import vazkii.botania.common.lib.LibMisc;

import javax.annotation.Nonnull;
import java.util.Random;

public class TrueGuardianKiller implements ILootCondition {

	@Override
	public boolean test(@Nonnull LootContext context) {
		Entity victim = context.get(LootParameters.THIS_ENTITY);
		return victim instanceof EntityDoppleganger
				&& context.get(LootParameters.KILLER_ENTITY) == ((EntityDoppleganger) victim).trueKiller;
	}

	public static class Serializer extends ILootCondition.AbstractSerializer<TrueGuardianKiller> {
		public Serializer() {
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
