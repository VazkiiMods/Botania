/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */

package vazkii.botania.api.configdata;

import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.random.Weight;
import net.minecraft.util.random.WeightedEntry;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.storage.loot.LootTable;

import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;

public class MobSpawnData extends WeightedEntry.IntrusiveBase {
	public static final Codec<MobSpawnData> CODEC = RecordCodecBuilder.create(
			instance -> instance.group(
					BuiltInRegistries.ENTITY_TYPE.byNameCodec().fieldOf("type").forGetter(msd -> msd.type),
					Weight.CODEC.fieldOf("weight").forGetter(IntrusiveBase::getWeight),
					IntProvider.CODEC.optionalFieldOf("count", ConstantInt.of(1))
							.forGetter(msd -> msd.count),
					Codec.BOOL.optionalFieldOf("spawnAsBaby").forGetter(msd -> Optional.ofNullable(msd.spawnAsBaby)),
					CompoundTag.CODEC.optionalFieldOf("nbt").forGetter(msd -> Optional.ofNullable(msd.nbt)),
					Codec.BOOL.optionalFieldOf("allowEquipmentDrops", false).forGetter(msd -> msd.allowEquipmentDrops),
					ResourceKey.codec(Registries.LOOT_TABLE).optionalFieldOf("equipmentTable")
							.forGetter(msd -> Optional.ofNullable(msd.equipmentTable)),
					Codec.list(MobEffectToApply.CODEC)
							.optionalFieldOf("effectsToApply")
							.forGetter(msd -> Optional.ofNullable(msd.effectsToApply)),
					Codec.list(MobAttributeModifier.CODEC).validate(MobAttributeModifier::validateList)
							.optionalFieldOf("attributeModifiers")
							.forGetter(msd -> Optional.ofNullable(msd.attributeModifiers))
			).apply(instance, MobSpawnData::create)
	);

	public final EntityType<?> type;
	public final IntProvider count;
	@Nullable
	public final Boolean spawnAsBaby;
	@Nullable
	public final CompoundTag nbt;
	public final boolean allowEquipmentDrops;
	@Nullable
	public final ResourceKey<LootTable> equipmentTable;
	@Nullable
	public final List<MobEffectToApply> effectsToApply;
	@Nullable
	public final List<MobAttributeModifier> attributeModifiers;

	private MobSpawnData(EntityType<?> type, Weight weight, IntProvider count, @Nullable Boolean spawnAsBaby,
			@Nullable CompoundTag nbt,
			boolean allowEquipmentDrops,
			@Nullable ResourceKey<LootTable> equipmentTable,
			@Nullable List<MobEffectToApply> effectsToApply,
			@Nullable List<MobAttributeModifier> attributeModifiers) {
		super(weight);
		this.type = type;
		this.count = count;
		this.spawnAsBaby = spawnAsBaby;
		this.nbt = nbt != null ? nbt.copy() : null;
		this.allowEquipmentDrops = allowEquipmentDrops;
		this.equipmentTable = equipmentTable;
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
				", count=" + count +
				", spawnAsBaby=" + spawnAsBaby +
				", nbt=" + nbt +
				", allowEquipmentDrops=" + allowEquipmentDrops +
				", equipmentTable=" + equipmentTable +
				", effectsToApply=" + effectsToApply +
				", attributeModifiers=" + attributeModifiers +
				'}';
	}

	// Codecs don't support setting null as intentional default value for optional fields, so we do this.
	// (blame com.mojang.datafixers.util.Either::getLeft using Optional::of instead Optional.ofNullable)
	@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
	private static MobSpawnData create(EntityType<?> type, Weight weight, IntProvider count,
			Optional<Boolean> spawnAsBaby,
			Optional<CompoundTag> nbt,
			boolean allowEquipmentDrops,
			Optional<ResourceKey<LootTable>> equipmentTable,
			Optional<List<MobEffectToApply>> effectsToApply,
			Optional<List<MobAttributeModifier>> attributeModifiers) {
		return new MobSpawnData(type, weight, count,
				spawnAsBaby.orElse(null),
				nbt.orElse(null),
				allowEquipmentDrops,
				equipmentTable.orElse(null),
				effectsToApply.orElse(null),
				attributeModifiers.orElse(null));
	}

	public static class Builder {
		private final EntityType<?> type;
		private final int weight;
		private IntProvider count = ConstantInt.of(1);
		private @Nullable Boolean spawnAsBaby;
		private @Nullable CompoundTag nbt;
		private boolean allowEquipmentDrops;
		private @Nullable ResourceKey<LootTable> equipmentTable;
		private @Nullable List<MobEffectToApply> effectsToApply;
		private @Nullable List<MobAttributeModifier> attributeModifiers;

		private Builder(EntityType<?> type, int weight) {
			this.type = type;
			this.weight = weight;
		}

		/**
		 * Make the mob spawn as a baby. (This will not prevent AgeableMobs from growing up.)
		 */
		public Builder spawnAsBaby() {
			this.spawnAsBaby = true;
			return this;
		}

		/**
		 * Force conversion of a baby mob to be reverted. This may have unintended side effects,
		 * like an adult zombie sitting on a chicken or an adult piglin not having a weapon.
		 * The latter case can usually be taken care of via an equipment table.
		 */
		public Builder spawnAsAdult() {
			this.spawnAsBaby = false;
			return this;
		}

		/**
		 * Custom NBT data to apply to the mob before finalizing its spawning.
		 */
		public Builder nbt(CompoundTag nbt) {
			this.nbt = nbt;
			return this;
		}

		/**
		 * Allow the mob to drop the equipment it spawned with.
		 */
		public Builder allowEquipmentDrops() {
			this.allowEquipmentDrops = true;
			return this;
		}

		/**
		 * A loot table to define equipment to apply to the mob after it spawned.
		 */
		public Builder equipmentTable(ResourceKey<LootTable> equipmentTable) {
			this.equipmentTable = equipmentTable;
			return this;
		}

		/**
		 * A list of potion effects to apply to the mob.
		 * (These are applied instead of any mob effects from the structure configuration.)
		 */
		public Builder effectsToApply(MobEffectToApply... effectsToApply) {
			this.effectsToApply = List.of(effectsToApply);
			return this;
		}

		/**
		 * A list of attribute modifiers to apply to the mob.
		 * (These are applied instead of any attribute modifiers from the structure configuration.)
		 */
		public Builder attributeModifiers(MobAttributeModifier... attributeModifiers) {
			this.attributeModifiers = List.of(attributeModifiers);
			return this;
		}

		/**
		 * A number provider for how many of these mobs are spawned at the same time.
		 */
		public Builder count(IntProvider count) {
			this.count = count;
			return this;
		}

		public MobSpawnData build() {
			return new MobSpawnData(type, Weight.of(weight), count, spawnAsBaby, nbt, allowEquipmentDrops,
					equipmentTable, effectsToApply, attributeModifiers);
		}
	}
}
