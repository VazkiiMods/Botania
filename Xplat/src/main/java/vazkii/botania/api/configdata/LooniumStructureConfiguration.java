package vazkii.botania.api.configdata;

import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.util.random.WeightedRandomList;
import net.minecraft.world.level.levelgen.structure.StructureSpawnOverride;

import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import static vazkii.botania.api.BotaniaAPI.botaniaRL;

public class LooniumStructureConfiguration {
	public static final int DEFAULT_COST = 35000;
	public static final int DEFAULT_MAX_NEARBY_MOBS = 10;
	public static final Codec<LooniumStructureConfiguration> CODEC = RecordCodecBuilder.<LooniumStructureConfiguration>create(
			instance -> instance.group(
					ResourceLocation.CODEC.optionalFieldOf("parent")
							.forGetter(lsc -> Optional.ofNullable(lsc.parent)),
					ExtraCodecs.NON_NEGATIVE_INT.optionalFieldOf("manaCost")
							.forGetter(lsc -> Optional.ofNullable(lsc.manaCost)),
					ExtraCodecs.POSITIVE_INT.optionalFieldOf("maxNearbyMobs")
							.forGetter(lsc -> Optional.ofNullable(lsc.maxNearbyMobs)),
					StructureSpawnOverride.BoundingBoxType.CODEC
							.optionalFieldOf("boundingBoxType")
							.forGetter(lsc -> Optional.ofNullable(lsc.boundingBoxType)),
					WeightedRandomList.codec(MobSpawnData.CODEC)
							.optionalFieldOf("spawnedMobs")
							.forGetter(lsc -> Optional.ofNullable(lsc.spawnedMobs)),
					Codec.list(MobAttributeModifier.CODEC).validate(MobAttributeModifier::validateList)
							.optionalFieldOf("attributeModifiers")
							.forGetter(lsc -> Optional.ofNullable(lsc.attributeModifiers)),
					Codec.list(MobEffectToApply.CODEC)
							.optionalFieldOf("effectsToApply")
							.forGetter(lsc -> Optional.ofNullable(lsc.effectsToApply))
			).apply(instance, LooniumStructureConfiguration::create)
	).validate(lsc -> {
		if (lsc.parent == null && (lsc.manaCost == null || lsc.boundingBoxType == null || lsc.spawnedMobs == null)) {
			return DataResult.error(() -> "Mana cost, bounding box type, and spawned mobs must be specified if there is no parent configuration");
		}
		if (lsc.spawnedMobs != null && lsc.spawnedMobs.isEmpty()) {
			return DataResult.error(() -> "Spawned mobs cannot be empty");
		}
		if (lsc.manaCost != null && lsc.manaCost > DEFAULT_COST) {
			return DataResult.error(() -> "Mana costs above %d are currently not supported"
					.formatted(DEFAULT_COST));
		}
		return DataResult.success(lsc);
	});
	public static final ResourceLocation DEFAULT_CONFIG_ID = botaniaRL("default");

	@Nullable
	public final Integer manaCost;
	@Nullable
	public final Integer maxNearbyMobs;
	@Nullable
	public final StructureSpawnOverride.BoundingBoxType boundingBoxType;
	@Nullable
	public final WeightedRandomList<MobSpawnData> spawnedMobs;
	@Nullable
	public final List<MobAttributeModifier> attributeModifiers;
	@Nullable
	public final List<MobEffectToApply> effectsToApply;
	@Nullable
	public final ResourceLocation parent;

	private LooniumStructureConfiguration(@Nullable ResourceLocation parent, @Nullable Integer manaCost, @Nullable Integer maxNearbyMobs,
			@Nullable StructureSpawnOverride.BoundingBoxType boundingBoxType, @Nullable WeightedRandomList<MobSpawnData> spawnedMobs,
			@Nullable List<MobAttributeModifier> attributeModifiers, @Nullable List<MobEffectToApply> effectsToApply) {
		this.manaCost = manaCost;
		this.maxNearbyMobs = maxNearbyMobs;
		this.spawnedMobs = spawnedMobs;
		this.boundingBoxType = boundingBoxType;
		this.attributeModifiers = attributeModifiers != null ? ImmutableList.copyOf(attributeModifiers) : null;
		this.effectsToApply = effectsToApply != null ? ImmutableList.copyOf(effectsToApply) : null;
		this.parent = parent;
	}

	public static Builder builder() {
		return new Builder();
	}

	public static Builder forParent(ResourceLocation parent) {
		return builder().parent(parent);
	}

	public LooniumStructureConfiguration getEffectiveConfig(
			Function<ResourceLocation, LooniumStructureConfiguration> parentSupplier) {
		if (parent == null) {
			return this;
		}
		var parentConfig = parentSupplier.apply(parent).getEffectiveConfig(parentSupplier);

		return new LooniumStructureConfiguration(null,
				manaCost != null ? manaCost : parentConfig.manaCost,
				maxNearbyMobs != null ? maxNearbyMobs : parentConfig.maxNearbyMobs,
				boundingBoxType != null ? boundingBoxType : parentConfig.boundingBoxType,
				spawnedMobs != null ? spawnedMobs : parentConfig.spawnedMobs,
				attributeModifiers != null ? attributeModifiers : parentConfig.attributeModifiers,
				effectsToApply != null ? effectsToApply : parentConfig.effectsToApply);
	}

	@Override
	public String toString() {
		return "LooniumStructureConfiguration{" +
				"manaCost=" + manaCost +
				", maxNearbyMobs=" + maxNearbyMobs +
				", boundingBoxType=" + boundingBoxType +
				", spawnedMobs=" + (spawnedMobs != null ? spawnedMobs.unwrap() : null) +
				", attributeModifiers=" + attributeModifiers +
				", effectsToApply=" + effectsToApply +
				", parent=" + parent +
				'}';
	}

	// Codecs don't support setting null as intentional default value for optional fields, so we do this.
	// (blame com.mojang.datafixers.util.Either::getLeft using Optional::of instead Optional.ofNullable)
	@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
	private static LooniumStructureConfiguration create(Optional<ResourceLocation> parent,
			Optional<Integer> manaCost, Optional<Integer> maxNearbyMobs,
			Optional<StructureSpawnOverride.BoundingBoxType> boundingBoxType,
			Optional<WeightedRandomList<MobSpawnData>> spawnedMobs,
			Optional<List<MobAttributeModifier>> attributeModifiers,
			Optional<List<MobEffectToApply>> effectsToApply) {
		return new LooniumStructureConfiguration(
				parent.orElse(null), manaCost.orElse(null), maxNearbyMobs.orElse(null),
				boundingBoxType.orElse(null), spawnedMobs.orElse(null),
				attributeModifiers.orElse(null), effectsToApply.orElse(null));
	}

	public static class Builder {
		@Nullable
		private ResourceLocation parent;
		@Nullable
		private Integer manaCost;
		@Nullable
		private Integer maxNearbyMobs;
		@Nullable
		private StructureSpawnOverride.BoundingBoxType boundingBoxType;
		@Nullable
		private WeightedRandomList<MobSpawnData> spawnedMobs;
		@Nullable
		private List<MobAttributeModifier> attributeModifiers;
		@Nullable
		private List<MobEffectToApply> effectsToApply;

		private Builder() {}

		private Builder parent(ResourceLocation parent) {
			this.parent = parent;
			return this;
		}

		public Builder manaCost(Integer manaCost) {
			this.manaCost = manaCost;
			return this;
		}

		public Builder maxNearbyMobs(Integer maxNearbyMobs) {
			this.maxNearbyMobs = maxNearbyMobs;
			return this;
		}

		public Builder boundingBoxType(StructureSpawnOverride.BoundingBoxType boundingBoxType) {
			this.boundingBoxType = boundingBoxType;
			return this;
		}

		public Builder spawnedMobs(MobSpawnData... spawnedMobs) {
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

		public LooniumStructureConfiguration build() {
			return new LooniumStructureConfiguration(parent, manaCost, maxNearbyMobs, boundingBoxType, spawnedMobs,
					attributeModifiers, effectsToApply);
		}
	}
}
