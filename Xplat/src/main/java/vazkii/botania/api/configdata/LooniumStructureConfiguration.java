package vazkii.botania.api.configdata;

import com.google.common.collect.ImmutableList;
import com.google.gson.JsonSyntaxException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.util.random.Weight;
import net.minecraft.util.random.WeightedEntry;
import net.minecraft.util.random.WeightedRandomList;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.level.levelgen.structure.StructureSpawnOverride;

import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;

public class LooniumStructureConfiguration {
	public static final Codec<LooniumStructureConfiguration> CODEC = RecordCodecBuilder.create(
			instance -> instance.group(
					ExtraCodecs.POSITIVE_INT.fieldOf("manaCost").forGetter(o -> o.manaCost),
					StructureSpawnOverride.BoundingBoxType.CODEC.fieldOf("boundingBoxType").forGetter(o -> o.boundingBoxType),
					WeightedRandomList.codec(MobSpawnData.CODEC).fieldOf("spawnedMobs").forGetter(o -> o.spawnedMobs),
					Codec.list(MobAttributeModifier.CODEC).fieldOf("attributeModifiers").forGetter(o -> o.attributeModifiers),
					Codec.list(MobEffectToApply.CODEC).fieldOf("effectsToApply").forGetter(o -> o.effectsToApply)
			).apply(instance, LooniumStructureConfiguration::new)
	);
	public static final Codec<LooniumStructureConfiguration> OPTIONAL_CODEC = RecordCodecBuilder.create(
			instance -> instance.group(
					ResourceLocation.CODEC.fieldOf("parent").forGetter(o -> o.parent),
					ExtraCodecs.POSITIVE_INT.optionalFieldOf("manaCost").forGetter(o -> Optional.ofNullable(o.manaCost)),
					StructureSpawnOverride.BoundingBoxType.CODEC.optionalFieldOf("boundingBoxType")
							.forGetter(o -> Optional.ofNullable(o.boundingBoxType)),
					WeightedRandomList.codec(MobSpawnData.CODEC).optionalFieldOf("spawnedMobs")
							.forGetter(o -> Optional.ofNullable(o.spawnedMobs)),
					Codec.list(MobAttributeModifier.CODEC).optionalFieldOf("attributeModifiers")
							.forGetter(o -> Optional.ofNullable(o.attributeModifiers)),
					Codec.list(MobEffectToApply.CODEC).optionalFieldOf("effectsToApply")
							.forGetter(o -> Optional.ofNullable(o.effectsToApply))
			).apply(instance, LooniumStructureConfiguration::new)
	);

	public final Integer manaCost;
	public final StructureSpawnOverride.BoundingBoxType boundingBoxType;
	public final WeightedRandomList<MobSpawnData> spawnedMobs;
	public final List<MobAttributeModifier> attributeModifiers;
	public final List<MobEffectToApply> effectsToApply;
	public final ResourceLocation parent;

	public LooniumStructureConfiguration(Integer manaCost, StructureSpawnOverride.BoundingBoxType boundingBoxType,
			WeightedRandomList<MobSpawnData> spawnedMobs, List<MobAttributeModifier> attributeModifiers,
			List<MobEffectToApply> effectsToApply) {
		this(null, manaCost, boundingBoxType, spawnedMobs, attributeModifiers, effectsToApply);
	}

	public LooniumStructureConfiguration(ResourceLocation parent, @Nullable Integer manaCost,
			@Nullable StructureSpawnOverride.BoundingBoxType boundingBoxType, @Nullable WeightedRandomList<MobSpawnData> spawnedMobs,
			@Nullable List<MobAttributeModifier> attributeModifiers, @Nullable List<MobEffectToApply> effectsToApply) {
		this.manaCost = manaCost;
		this.spawnedMobs = spawnedMobs;
		this.boundingBoxType = boundingBoxType;
		this.attributeModifiers = attributeModifiers != null ? ImmutableList.copyOf(attributeModifiers) : null;
		this.effectsToApply = effectsToApply != null ? ImmutableList.copyOf(effectsToApply) : null;
		this.parent = parent;
	}

	private LooniumStructureConfiguration(ResourceLocation parent, Optional<Integer> manaCost,
			Optional<StructureSpawnOverride.BoundingBoxType> boundingBoxType,
			Optional<WeightedRandomList<MobSpawnData>> spawnedMobs,
			Optional<List<MobAttributeModifier>> attributeModifiers,
			Optional<List<MobEffectToApply>> effectsToApply) {
		this(manaCost.orElse(null), boundingBoxType.orElse(null), spawnedMobs.orElse(null),
				attributeModifiers.orElse(null), effectsToApply.orElse(null));
	}

	public LooniumStructureConfiguration(ResourceLocation parent,
			StructureSpawnOverride.BoundingBoxType boundingBoxType) {
		this(parent, null, boundingBoxType, null, null, null);
	}

	public static class MobSpawnData extends WeightedEntry.IntrusiveBase {
		public static final Codec<MobSpawnData> CODEC = RecordCodecBuilder.create(
				instance -> instance.group(
						BuiltInRegistries.ENTITY_TYPE.byNameCodec().fieldOf("type").forGetter(o -> o.type),
						Weight.CODEC.fieldOf("weight").forGetter(IntrusiveBase::getWeight),
						CompoundTag.CODEC.optionalFieldOf("nbt").forGetter(o -> Optional.ofNullable(o.nbt)),
						Codec.list(MobEffectToApply.CODEC).optionalFieldOf("effectsToApply")
								.forGetter(o -> Optional.ofNullable(o.effectsToApply)),
						Codec.list(MobAttributeModifier.CODEC).optionalFieldOf("attributeModifiers")
								.forGetter(o -> Optional.ofNullable(o.attributeModifiers))
				).apply(instance, MobSpawnData::new)
		);

		public final EntityType<?> type;
		public final CompoundTag nbt;
		public final List<MobEffectToApply> effectsToApply;
		public final List<MobAttributeModifier> attributeModifiers;

		public MobSpawnData(EntityType<?> type, int weight) {
			this(type, weight, null);
		}

		public MobSpawnData(EntityType<?> type, int weight, @Nullable List<MobEffectToApply> effectsToApply) {
			this(type, weight, effectsToApply, null, null);
		}

		public MobSpawnData(EntityType<?> type, int weight, @Nullable List<MobEffectToApply> effectsToApply,
				@Nullable List<MobAttributeModifier> attributeModifiers, @Nullable CompoundTag nbt) {
			this(type, Weight.of(weight), effectsToApply, attributeModifiers, nbt);
		}

		public MobSpawnData(EntityType<?> type, Weight weight, @Nullable List<MobEffectToApply> effectsToApply,
				@Nullable List<MobAttributeModifier> attributeModifiers, @Nullable CompoundTag nbt) {
			super(weight);
			this.type = type;
			this.nbt = nbt != null ? nbt.copy() : null;
			this.effectsToApply = effectsToApply != null ? ImmutableList.copyOf(effectsToApply) : null;
			this.attributeModifiers = attributeModifiers != null ? ImmutableList.copyOf(attributeModifiers) : null;
		}

		private MobSpawnData(EntityType<?> type, Weight weight, Optional<CompoundTag> nbt,
				Optional<List<MobEffectToApply>> effectsToApply, Optional<List<MobAttributeModifier>> attributeModifiers) {
			this(type, weight, effectsToApply.orElse(null), attributeModifiers.orElse(null), nbt.orElse(null));
		}
	}

	public static class MobAttributeModifier {
		public static final Codec<MobAttributeModifier> CODEC = RecordCodecBuilder.create(
				instance -> instance.group(
						Codec.STRING.fieldOf("name").forGetter(o -> o.name),
						BuiltInRegistries.ATTRIBUTE.byNameCodec().fieldOf("attribute").forGetter(o -> o.attribute),
						Codec.DOUBLE.fieldOf("amount").forGetter(o -> o.amount),
						Codec.STRING.xmap(MobAttributeModifier::operationFromString, MobAttributeModifier::operationToString)
								.fieldOf("operation").forGetter(o -> o.operation)
				).apply(instance, MobAttributeModifier::new)
		);

		private final String name;
		public final Attribute attribute;
		private final double amount;
		private final AttributeModifier.Operation operation;

		public MobAttributeModifier(String name, Attribute attribute, double amount, AttributeModifier.Operation operation) {
			this.name = name;
			this.attribute = attribute;
			this.amount = amount;
			this.operation = operation;
		}

		public AttributeModifier createAttributeModifier() {
			return new AttributeModifier(name, amount, operation);
		}

		private static String operationToString(AttributeModifier.Operation operation) {
			return switch (operation) {
				case ADDITION -> "addition";
				case MULTIPLY_BASE -> "multiply_base";
				case MULTIPLY_TOTAL -> "multiply_total";
				default -> throw new IllegalArgumentException("Unknown operation " + operation);
			};
		}

		private static AttributeModifier.Operation operationFromString(String operation) {
			return switch (operation) {
				case "addition" -> AttributeModifier.Operation.ADDITION;
				case "multiply_base" -> AttributeModifier.Operation.MULTIPLY_BASE;
				case "multiply_total" -> AttributeModifier.Operation.MULTIPLY_TOTAL;
				default -> throw new JsonSyntaxException("Unknown attribute modifier operation " + operation);
			};
		}
	}

	public static class MobEffectToApply {
		public static final Codec<MobEffectToApply> CODEC = RecordCodecBuilder.create(
				instance -> instance.group(
						BuiltInRegistries.MOB_EFFECT.byNameCodec().fieldOf("effect").forGetter(o -> o.effect),
						ExtraCodecs.validate(Codec.INT,
								duration -> duration > 0
										? DataResult.success(duration)
										: DataResult.error(() -> "Invalid effect duration"))
								.optionalFieldOf("duration").forGetter(MobEffectToApply::getOptionalDuration),
						Codec.intRange(0, 255)
								.optionalFieldOf("amplifier").forGetter(MobEffectToApply::getOptionalAmplifier)
				).apply(instance, MobEffectToApply::new)
		);

		public final MobEffect effect;
		public final int duration;
		public final int amplifier;

		public MobEffectToApply(MobEffect effect) {
			this(effect, MobEffectInstance.INFINITE_DURATION);
		}

		public MobEffectToApply(MobEffect effect, int duration) {
			this(effect, duration, 0);
		}

		public MobEffectToApply(MobEffect effect, int duration, int amplifier) {
			this.effect = effect;
			this.duration = duration;
			this.amplifier = amplifier;
		}

		private MobEffectToApply(MobEffect effect, Optional<Integer> optionalDuration, Optional<Integer> optionalAmplifier) {
			this(effect, optionalDuration.orElse(MobEffectInstance.INFINITE_DURATION), optionalAmplifier.orElse(0));
		}

		private Optional<Integer> getOptionalDuration() {
			return duration != MobEffectInstance.INFINITE_DURATION ? Optional.of(duration) : Optional.empty();
		}

		private Optional<Integer> getOptionalAmplifier() {
			return amplifier > 0 ? Optional.of(amplifier) : Optional.empty();
		}
	}
}
