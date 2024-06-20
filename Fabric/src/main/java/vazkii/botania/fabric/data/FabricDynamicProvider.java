package vazkii.botania.fabric.data;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricDynamicRegistryProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.world.damagesource.DamageEffects;
import net.minecraft.world.damagesource.DamageScaling;
import net.minecraft.world.damagesource.DamageType;

import java.util.concurrent.CompletableFuture;

import static vazkii.botania.common.BotaniaDamageTypes.*;

public class FabricDynamicProvider extends FabricDynamicRegistryProvider {

	public FabricDynamicProvider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
		super(output, registriesFuture);
	}

	@Override
	protected void configure(HolderLookup.Provider registries, Entries entries) {
		entries.add(RELIC_DAMAGE, new DamageType("botania-relic", DamageScaling.NEVER, 1F, DamageEffects.FREEZING));
		entries.add(PLAYER_ATTACK_ARMOR_PIERCING, new DamageType("player", DamageScaling.WHEN_CAUSED_BY_LIVING_NON_PLAYER, 0.1F));
		entries.add(KEY_EXPLOSION, new DamageType("botania.key_explosion", DamageScaling.ALWAYS, 0.1F));
	}

	@Override
	public String getName() {
		return "damageTypes";
	}
}
