package vazkii.botania.api.configdata;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.random.WeightedRandomList;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.level.storage.loot.LootTable;

import org.jetbrains.annotations.Nullable;

import java.util.List;

import static vazkii.botania.api.BotaniaAPI.botaniaRL;

public record GaiaFightConfiguration(
		ResourceLocation rewardLootTable,
		int teleportDelay,
		IntProvider mineCount,
		float dyingHealthPercent,
		int dyingTeleportDelay,
		IntProvider dyingMineCount,
		IntProvider pixiesAfterHurtCount,
		IntProvider pixiesAfterTeleportCount,
		int missileSpawnInterval,
		IntProvider missileSpawnCount,
		IntProvider mobSpawnsPerPlayer,
		WeightedRandomList<MobSpawnData> spawnedMobs,
		List<MobAttributeModifier> attributeModifiers,
		List<MobEffectToApply> effectsToApply) {

	public static final Codec<GaiaFightConfiguration> CODEC = RecordCodecBuilder.create(
			instance -> instance.group(
					ResourceLocation.CODEC.fieldOf("rewardLootTable")
							.forGetter(GaiaFightConfiguration::rewardLootTable),
					Codec.INT.fieldOf("teleportDelay")
							.forGetter(GaiaFightConfiguration::teleportDelay),
					IntProvider.CODEC.fieldOf("mineCount")
							.forGetter(GaiaFightConfiguration::mineCount),
					Codec.FLOAT.fieldOf("dyingHealthPercent")
							.forGetter(GaiaFightConfiguration::dyingHealthPercent),
					Codec.INT.fieldOf("dyingTeleportDelay")
							.forGetter(GaiaFightConfiguration::dyingTeleportDelay),
					IntProvider.CODEC.fieldOf("dyingMineCount")
							.forGetter(GaiaFightConfiguration::dyingMineCount),
					IntProvider.CODEC.fieldOf("pixiesAfterHurtCount")
							.forGetter(GaiaFightConfiguration::pixiesAfterHurtCount),
					IntProvider.CODEC.fieldOf("pixiesAfterTeleportCount")
							.forGetter(GaiaFightConfiguration::pixiesAfterTeleportCount),
					Codec.INT.optionalFieldOf("missileSpawnInterval", 0)
							.forGetter(GaiaFightConfiguration::missileSpawnInterval),
					IntProvider.CODEC.optionalFieldOf("missileSpawnCount", ConstantInt.ZERO)
							.forGetter(GaiaFightConfiguration::missileSpawnCount),
					IntProvider.CODEC.fieldOf("mobSpawnsPerPlayer")
							.forGetter(GaiaFightConfiguration::mobSpawnsPerPlayer),
					WeightedRandomList.codec(MobSpawnData.CODEC)
							.validate(list -> list.isEmpty()
									? DataResult.error(() -> "Spawned mobs cannot be empty", list)
									: DataResult.success(list))
							.fieldOf("spawnedMobs")
							.forGetter(GaiaFightConfiguration::spawnedMobs),
					Codec.list(MobAttributeModifier.CODEC)
							.validate(MobAttributeModifier::validateList)
							.optionalFieldOf("attributeModifiers", List.of())
							.forGetter(GaiaFightConfiguration::attributeModifiers),
					Codec.list(MobEffectToApply.CODEC)
							.optionalFieldOf("effectsToApply", List.of())
							.forGetter(GaiaFightConfiguration::effectsToApply)
			).apply(instance, GaiaFightConfiguration::new)
	);

	public static final ResourceLocation NORMAL = botaniaRL("normal");
	public static final ResourceLocation HARD = botaniaRL("hard");

	public static GaiaFightConfiguration.Builder builder(ResourceKey<LootTable> playerLoot, int teleportDelay,
			IntProvider mineCount, float dyingHealthPercent, int dyingTeleportDelay, IntProvider dyingMineCount,
			IntProvider pixiesAfterHurtCount) {
		return new GaiaFightConfiguration.Builder(playerLoot.location(), teleportDelay, mineCount,
				dyingHealthPercent, dyingTeleportDelay, dyingMineCount, pixiesAfterHurtCount);
	}

	public ResourceKey<LootTable> getRewardLootTableKey() {
		return ResourceKey.create(Registries.LOOT_TABLE, this.rewardLootTable);
	}

	public static class Builder {
		private final ResourceLocation playerLoot;
		private final int teleportDelay;
		private final IntProvider mineCount;
		private final float dyingHealthPercent;
		private final int dyingTeleportDelay;
		private final IntProvider dyingMineCount;
		private final IntProvider pixiesAfterHurtCount;
		private IntProvider pixiesAfterTeleportCount = ConstantInt.of(1);
		private int missileSpawnInterval;
		private IntProvider missileSpawnCount = ConstantInt.ZERO;
		IntProvider mobSpawnsPerPlayer = ConstantInt.of(3);
		@Nullable
		private WeightedRandomList<MobSpawnData> spawnedMobs;
		@Nullable
		private List<MobAttributeModifier> attributeModifiers;
		@Nullable
		private List<MobEffectToApply> effectsToApply;

		private Builder(ResourceLocation playerLoot, int teleportDelay, IntProvider mineCount,
				float dyingHealthPercent, int dyingTeleportDelay, IntProvider dyingMineCount,
				IntProvider pixiesAfterHurtCount) {
			this.playerLoot = playerLoot;
			this.teleportDelay = teleportDelay;
			this.mineCount = mineCount;
			this.dyingHealthPercent = dyingHealthPercent;
			this.dyingTeleportDelay = dyingTeleportDelay;
			this.dyingMineCount = dyingMineCount;
			this.pixiesAfterHurtCount = pixiesAfterHurtCount;
		}

		public Builder spawnedMobs(IntProvider mobSpawnsPerPlayer, MobSpawnData... spawnedMobs) {
			this.mobSpawnsPerPlayer = mobSpawnsPerPlayer;
			this.spawnedMobs = WeightedRandomList.create(spawnedMobs);
			return this;
		}

		public Builder attributeModifiers(MobAttributeModifier... attributeModifiers) {
			this.attributeModifiers = List.of(attributeModifiers);
			return this;
		}

		public Builder effectsToApply(MobEffectToApply... effectsToApply) {
			this.effectsToApply = List.of(effectsToApply);
			return this;
		}

		public Builder pixiesAfterTeleportCount(IntProvider pixiesAfterTeleportCount) {
			this.pixiesAfterTeleportCount = pixiesAfterTeleportCount;
			return this;
		}

		public Builder spawnMissiles(int interval, IntProvider count) {
			this.missileSpawnInterval = interval;
			this.missileSpawnCount = count;
			return this;
		}

		public GaiaFightConfiguration build() {
			return new GaiaFightConfiguration(playerLoot, teleportDelay, mineCount,
					dyingHealthPercent, dyingTeleportDelay, dyingMineCount,
					pixiesAfterHurtCount, pixiesAfterTeleportCount,
					missileSpawnInterval, missileSpawnCount,
					mobSpawnsPerPlayer, spawnedMobs == null ? WeightedRandomList.create() : spawnedMobs,
					attributeModifiers == null ? List.of() : attributeModifiers,
					effectsToApply == null ? List.of() : effectsToApply);
		}
	}
}
