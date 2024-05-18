package vazkii.botania.api.configdata;

import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.random.Weight;
import net.minecraft.util.random.WeightedEntry;
import net.minecraft.world.entity.EntityType;

import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;

public class LooniumMobSpawnData extends WeightedEntry.IntrusiveBase {
	public static final Codec<LooniumMobSpawnData> CODEC = RecordCodecBuilder.create(
			instance -> instance.group(
					BuiltInRegistries.ENTITY_TYPE.byNameCodec().fieldOf("type").forGetter(o -> o.type),
					Weight.CODEC.fieldOf("weight").forGetter(IntrusiveBase::getWeight),
					CompoundTag.CODEC.optionalFieldOf("nbt").forGetter(msd -> Optional.ofNullable(msd.nbt)),
					Codec.list(LooniumMobEffectToApply.CODEC)
							.optionalFieldOf("effectsToApply")
							.forGetter(msd -> Optional.ofNullable(msd.effectsToApply)),
					Codec.list(LooniumMobAttributeModifier.CODEC)
							.optionalFieldOf("attributeModifiers")
							.forGetter(msd -> Optional.ofNullable(msd.attributeModifiers))
			).apply(instance, LooniumMobSpawnData::create)
	);

	public final EntityType<?> type;
	public final CompoundTag nbt;
	public final List<LooniumMobEffectToApply> effectsToApply;
	public final List<LooniumMobAttributeModifier> attributeModifiers;

	private LooniumMobSpawnData(EntityType<?> type, Weight weight, @Nullable CompoundTag nbt,
			@Nullable List<LooniumMobEffectToApply> effectsToApply,
			@Nullable List<LooniumMobAttributeModifier> attributeModifiers) {
		super(weight);
		this.type = type;
		this.nbt = nbt != null ? nbt.copy() : null;
		this.effectsToApply = effectsToApply != null ? ImmutableList.copyOf(effectsToApply) : null;
		this.attributeModifiers = attributeModifiers != null ? ImmutableList.copyOf(attributeModifiers) : null;
	}

	public static Builder entityWeight(EntityType<?> type, int weight) {
		return new Builder(type, weight);
	}

	@Override
	public String toString() {
		return "MobSpawnData{" +
				"type=" + type +
				", nbt=" + nbt +
				", effectsToApply=" + effectsToApply +
				", attributeModifiers=" + attributeModifiers +
				'}';
	}

	// Codecs don't support setting null as intentional default value for optional fields, so we do this.
	// (blame com.mojang.datafixers.util.Either::getLeft using Optional::of instead Optional.ofNullable)
	@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
	private static LooniumMobSpawnData create(EntityType<?> type, Weight weight, Optional<CompoundTag> nbt,
			Optional<List<LooniumMobEffectToApply>> effectsToApply,
			Optional<List<LooniumMobAttributeModifier>> attributeModifiers) {
		return new LooniumMobSpawnData(type, weight, nbt.orElse(null),
				effectsToApply.orElse(null), attributeModifiers.orElse(null));
	}

	public static class Builder {
		private final EntityType<?> type;
		private final int weight;
		private @Nullable CompoundTag nbt;
		private @Nullable List<LooniumMobEffectToApply> effectsToApply;
		private @Nullable List<LooniumMobAttributeModifier> attributeModifiers;

		private Builder(EntityType<?> type, int weight) {
			this.type = type;
			this.weight = weight;
		}

		public Builder setNbt(CompoundTag nbt) {
			this.nbt = nbt;
			return this;
		}

		public Builder setEffectsToApply(List<LooniumMobEffectToApply> effectsToApply) {
			this.effectsToApply = effectsToApply;
			return this;
		}

		public Builder setAttributeModifiers(
				List<LooniumMobAttributeModifier> attributeModifiers) {
			this.attributeModifiers = attributeModifiers;
			return this;
		}

		public LooniumMobSpawnData build() {
			return new LooniumMobSpawnData(type, Weight.of(weight), nbt, effectsToApply, attributeModifiers);
		}
	}
}
