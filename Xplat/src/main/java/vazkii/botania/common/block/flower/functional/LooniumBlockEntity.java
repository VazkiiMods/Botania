/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block.flower.functional;

import com.google.common.base.Suppliers;

import it.unimi.dsi.fastutil.Pair;
import it.unimi.dsi.fastutil.objects.*;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.random.WeightedRandomList;
import net.minecraft.world.Difficulty;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureSpawnOverride;
import net.minecraft.world.level.storage.loot.LootDataManager;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.block_entity.FunctionalFlowerBlockEntity;
import vazkii.botania.api.block_entity.RadiusDescriptor;
import vazkii.botania.api.configdata.LooniumStructureConfiguration;
import vazkii.botania.common.block.BotaniaFlowerBlocks;
import vazkii.botania.common.config.ConfigDataManager;
import vazkii.botania.common.internal_caps.LooniumComponent;
import vazkii.botania.common.lib.BotaniaTags;
import vazkii.botania.xplat.XplatAbstractions;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static vazkii.botania.common.lib.ResourceLocationHelper.prefix;

public class LooniumBlockEntity extends FunctionalFlowerBlockEntity {
	public static final int DEFAULT_COST = 35000;
	public static final int DEFAULT_MAX_NEARBY_MOBS = 10;
	private static final int RANGE = 5;
	private static final int CHECK_RANGE = 9;
	private static final String TAG_LOOT_TABLE = "lootTable";
	private static final String TAG_DETECTED_STRUCTURE = "detectedStructure";
	private static final String TAG_CONFIG_OVERRIDE = "configOverride";
	public static final ResourceLocation DEFAULT_LOOT_TABLE = prefix("loonium/default");
	private static final Supplier<LooniumStructureConfiguration> FALLBACK_CONFIG =
			Suppliers.memoize(() -> new LooniumStructureConfiguration(DEFAULT_COST, DEFAULT_MAX_NEARBY_MOBS,
					StructureSpawnOverride.BoundingBoxType.PIECE,
					WeightedRandomList.create(new LooniumStructureConfiguration.MobSpawnData(EntityType.ZOMBIE, 1)),
					List.of(), List.of(
							new LooniumStructureConfiguration.MobEffectToApply(MobEffects.REGENERATION),
							new LooniumStructureConfiguration.MobEffectToApply(MobEffects.FIRE_RESISTANCE),
							new LooniumStructureConfiguration.MobEffectToApply(MobEffects.DAMAGE_RESISTANCE),
							new LooniumStructureConfiguration.MobEffectToApply(MobEffects.DAMAGE_BOOST))));

	@Nullable
	private ResourceLocation lootTableOverride;
	@Nullable
	private Object2BooleanMap<ResourceLocation> detectedStructures;
	@Nullable
	private ResourceLocation configOverride;

	public LooniumBlockEntity(BlockPos pos, BlockState state) {
		super(BotaniaFlowerBlocks.LOONIUM, pos, state);
	}

	@Override
	public void tickFlower() {
		super.tickFlower();

		if (!(getLevel() instanceof ServerLevel world)) {
			return;
		}

		if (detectedStructures == null) {
			// Detection intentionally uses the flower position, not the effective position,
			// since the latter could change while this detection is only executed once.
			detectStructure(world);
		}

		if (redstoneSignal != 0 || ticksExisted % 100 != 0 || world.getDifficulty() == Difficulty.PEACEFUL
		// so mobs won't spawn in unloaded or border chunks
				|| !world.isPositionEntityTicking(getEffectivePos())) {
			return;
		}

		var configData = BotaniaAPI.instance().getConfigData();
		var structureConfigs = determineStructureConfigs(configData, detectedStructures);
		var lootTables = determineLootTables(world, detectedStructures.keySet());

		if (lootTables.isEmpty()) {
			return;
		}

		var randomPick = lootTables.get(world.random.nextInt(lootTables.size()));
		var pickedConfig = structureConfigs.getOrDefault(randomPick.key(),
				structureConfigs.get(LooniumStructureConfiguration.DEFAULT_CONFIG_ID));
		var pickedLootTable = randomPick.value();

		if (getMana() < pickedConfig.manaCost) {
			return;
		}

		int numberOfMobsAround = countNearbyMobs(world, pickedConfig);
		if (numberOfMobsAround >= pickedConfig.maxNearbyMobs) {
			return;
		}

		var pickedMobType = pickedConfig.spawnedMobs.getRandom(world.random).orElse(null);
		if (pickedMobType == null) {
			return;
		}

		ItemStack stack = pickRandomLootItem(world, pickedLootTable);
		if (stack.isEmpty()) {
			return;
		}

		spawnMob(world, pickedMobType, pickedConfig, stack);
	}

	private void spawnMob(ServerLevel world, LooniumStructureConfiguration.MobSpawnData pickedMobType,
			LooniumStructureConfiguration pickedConfig, ItemStack stack) {
		int bound = RANGE * 2 + 1;
		int xp = getEffectivePos().getX() - RANGE + world.random.nextInt(bound);
		int yp = getEffectivePos().getY();
		int zp = getEffectivePos().getZ() - RANGE + world.random.nextInt(bound);

		BlockPos pos = new BlockPos(xp, yp - 1, zp);
		do {
			pos = pos.above();
			if (pos.getY() >= world.getMaxBuildHeight()) {
				return;
			}
		} while (world.getBlockState(pos).isSuffocating(world, pos));
		pos = pos.above();

		double x = pos.getX() + Math.random();
		double y = pos.getY() + Math.random();
		double z = pos.getZ() + Math.random();

		Entity entity = pickedMobType.type.create(world);
		if (!(entity instanceof Mob mob)) {
			return;
		}

		if (pickedMobType.nbt != null) {
			mob.readAdditionalSaveData(pickedMobType.nbt);
		}

		mob.absMoveTo(x, y, z, world.random.nextFloat() * 360F, 0);
		mob.setDeltaMovement(Vec3.ZERO);

		var attributeModifiers = pickedMobType.attributeModifiers != null
				? pickedMobType.attributeModifiers
				: pickedConfig.attributeModifiers;
		for (var attributeModifier : attributeModifiers) {
			var attribute = mob.getAttribute(attributeModifier.attribute);
			if (attribute != null) {
				attribute.addPermanentModifier(attributeModifier.createAttributeModifier());
				if (attribute.getAttribute() == Attributes.MAX_HEALTH) {
					mob.setHealth(mob.getMaxHealth());
				}
			}
		}

		var effectsToApply = pickedMobType.effectsToApply != null
				? pickedMobType.effectsToApply
				: pickedConfig.effectsToApply;
		for (var effectToApply : effectsToApply) {
			mob.addEffect(effectToApply.createMobEffectInstance());
		}

		LooniumComponent looniumComponent = XplatAbstractions.INSTANCE.looniumComponent(mob);
		if (looniumComponent != null) {
			looniumComponent.setDrop(stack);
			looniumComponent.setSlowDespawn(true);
		}

		mob.finalizeSpawn(world, world.getCurrentDifficultyAt(pos), MobSpawnType.SPAWNER, null, null);
		// in case the mob spawned with a vehicle or passenger(s), ensure those don't drop unexpected loot
		mob.getRootVehicle().getPassengersAndSelf().forEach(e -> {
			if (e != mob && e instanceof Mob otherMob) {
				// prevent armor/weapon drops on player kill, also no nautilus shells from drowned:
				Arrays.stream(EquipmentSlot.values()).forEach(slot -> otherMob.setDropChance(slot, 0));
				LooniumComponent otherLooniumComponent = XplatAbstractions.INSTANCE.looniumComponent(otherMob);
				if (otherLooniumComponent != null) {
					otherLooniumComponent.setDropNothing(true);
				}
			}
		});

		world.addFreshEntity(mob);
		mob.spawnAnim();

		addMana(-DEFAULT_COST);
		sync();
	}

	private int countNearbyMobs(ServerLevel world, LooniumStructureConfiguration pickedConfig) {
		var setOfMobTypes = pickedConfig.spawnedMobs.unwrap().stream().map(msd -> msd.type).collect(Collectors.toSet());
		return world.getEntitiesOfClass(Mob.class, new AABB(getEffectivePos()).inflate(CHECK_RANGE),
				m -> setOfMobTypes.contains(m.getType())).size();
	}

	private static ItemStack pickRandomLootItem(ServerLevel world, LootTable pickedLootTable) {
		LootParams params = new LootParams.Builder(world).create(LootContextParamSets.EMPTY);
		List<ItemStack> stacks = pickedLootTable.getRandomItems(params, world.random.nextLong());
		stacks.removeIf(s -> s.isEmpty() || s.is(BotaniaTags.Items.LOONIUM_BLACKLIST));
		if (stacks.isEmpty()) {
			return ItemStack.EMPTY;
		} else {
			Collections.shuffle(stacks);
			return stacks.get(0);
		}
	}

	@NotNull
	private List<Pair<ResourceLocation, LootTable>> determineLootTables(ServerLevel world,
			Set<ResourceLocation> structureIds) {
		var lootTables = new ArrayList<Pair<ResourceLocation, LootTable>>();
		LootDataManager lootData = world.getServer().getLootData();
		if (lootTableOverride != null) {
			LootTable lootTable = lootData.getLootTable(lootTableOverride);
			if (lootTable != LootTable.EMPTY) {
				lootTables.add(Pair.of(LooniumStructureConfiguration.DEFAULT_CONFIG_ID, lootTable));
			}
		} else {
			for (var structureId : structureIds) {
				var lootTableId = structureId.equals(LooniumStructureConfiguration.DEFAULT_CONFIG_ID)
						? DEFAULT_LOOT_TABLE
						: prefix("loonium/%s/%s".formatted(structureId.getNamespace(), structureId.getPath()));
				LootTable lootTable = lootData.getLootTable(lootTableId);
				if (lootTable != LootTable.EMPTY) {
					lootTables.add(Pair.of(structureId, lootTable));
				}
			}
		}
		if (lootTables.isEmpty()) {
			LootTable lootTable = lootData.getLootTable(DEFAULT_LOOT_TABLE);
			if (lootTable != LootTable.EMPTY) {
				lootTables.add(Pair.of(LooniumStructureConfiguration.DEFAULT_CONFIG_ID, lootTable));
			}
		}
		return lootTables;
	}

	/**
	 * Build a map of structure IDs to resolved Loonium configurations, i.e. no need to traverse any parents.
	 * 
	 * @param configData Configuration data to read from.
	 * @param structures Detected structures to work with.
	 * @return The map, which is guaranteed to not be empty.
	 */
	@NotNull
	private Map<ResourceLocation, LooniumStructureConfiguration> determineStructureConfigs(
			@NotNull ConfigDataManager configData, @NotNull Object2BooleanMap<ResourceLocation> structures) {
		if (configOverride != null) {
			LooniumStructureConfiguration overrideConfig =
					configData.getEffectiveLooniumStructureConfiguration(configOverride);
			return Map.of(LooniumStructureConfiguration.DEFAULT_CONFIG_ID,
					overrideConfig != null ? overrideConfig : getDefaultConfig(configData));
		}

		var structureConfigs = new HashMap<ResourceLocation, LooniumStructureConfiguration>();
		for (var structureEntry : structures.object2BooleanEntrySet()) {
			var structureConfig = configData.getEffectiveLooniumStructureConfiguration(structureEntry.getKey());
			if (structureConfig != null && (structureEntry.getBooleanValue()
					|| structureConfig.boundingBoxType == StructureSpawnOverride.BoundingBoxType.STRUCTURE)) {
				structureConfigs.put(structureEntry.getKey(), structureConfig);
			}
		}

		structureConfigs.put(LooniumStructureConfiguration.DEFAULT_CONFIG_ID, getDefaultConfig(configData));
		return structureConfigs;
	}

	private static LooniumStructureConfiguration getDefaultConfig(ConfigDataManager configData) {
		var defaultConfig = configData.getEffectiveLooniumStructureConfiguration(
				LooniumStructureConfiguration.DEFAULT_CONFIG_ID);
		return defaultConfig != null ? defaultConfig : FALLBACK_CONFIG.get();
	}

	private void detectStructure(ServerLevel world) {
		// structure ID and whether the position is inside a structure piece (false = only overall bounding box)
		var structureMap = new Object2BooleanRBTreeMap<ResourceLocation>();
		StructureManager structureManager = world.structureManager();
		BlockPos pos = getBlockPos();
		var structures = structureManager.getAllStructuresAt(pos);
		for (var entry : structures.entrySet()) {
			Structure structure = entry.getKey();
			var start = structureManager.getStructureAt(pos, structure);
			if (start.isValid()) {
				ResourceLocation structureId =
						world.registryAccess().registryOrThrow(Registries.STRUCTURE).getKey(structure);
				boolean insidePiece = structureManager.structureHasPieceAt(pos, start);
				if (insidePiece || !structureMap.getBoolean(structureId)) {
					structureMap.put(structureId, insidePiece);
				}
			}
		}

		if (structureMap.isEmpty()) {
			detectedStructures = Object2BooleanMaps.emptyMap();
		} else if (structureMap.size() == 1) {
			var key = structureMap.firstKey();
			detectedStructures = Object2BooleanMaps.singleton(key, structureMap.getBoolean(key));
		} else {
			detectedStructures = new Object2BooleanArrayMap<>(structureMap);
		}

		setChanged();
		sync();
	}

	@Override
	public int getColor() {
		return 0x274A00;
	}

	@Override
	public int getMaxMana() {
		return DEFAULT_COST;
	}

	@Override
	public boolean acceptsRedstone() {
		return true;
	}

	@Override
	public RadiusDescriptor getRadius() {
		return RadiusDescriptor.Rectangle.square(getEffectivePos(), RANGE);
	}

	@Override
	public RadiusDescriptor getSecondaryRadius() {
		return RadiusDescriptor.Rectangle.square(getEffectivePos(), CHECK_RANGE);
	}

	@Override
	public void readFromPacketNBT(CompoundTag cmp) {
		super.readFromPacketNBT(cmp);
		if (cmp.contains(TAG_LOOT_TABLE)) {
			lootTableOverride = new ResourceLocation(cmp.getString(TAG_LOOT_TABLE));
		}
		if (cmp.contains(TAG_CONFIG_OVERRIDE)) {
			configOverride = new ResourceLocation(cmp.getString(TAG_CONFIG_OVERRIDE));
		}
		if (cmp.contains(TAG_DETECTED_STRUCTURE)) {
			var rawString = cmp.getString(TAG_DETECTED_STRUCTURE);
			if (rawString.isEmpty()) {
				detectedStructures = Object2BooleanMaps.emptyMap();
			} else {
				var structureList = Arrays.stream(rawString.split(",")).map(part -> {
					if (part.contains("|")) {
						var components = part.split("\\|", 2);
						return ObjectBooleanPair.of(new ResourceLocation(components[0]), Boolean.parseBoolean(components[1]));
					} else {
						return ObjectBooleanPair.of(new ResourceLocation(part), false);
					}
				}).toList();
				if (structureList.size() == 1) {
					detectedStructures = Object2BooleanMaps.singleton(structureList.get(0).key(), structureList.get(0).valueBoolean());
				} else {
					// list should never contain more than a few entries, so array is fine and retains entry order
					var map = new Object2BooleanArrayMap<ResourceLocation>(structureList.size());
					structureList.forEach(entry -> map.put(entry.key(), entry.valueBoolean()));
					detectedStructures = map;
				}
			}
		}
	}

	@Override
	public void writeToPacketNBT(CompoundTag cmp) {
		super.writeToPacketNBT(cmp);
		if (lootTableOverride != null) {
			cmp.putString(TAG_LOOT_TABLE, lootTableOverride.toString());
		}
		if (configOverride != null) {
			cmp.putString(TAG_CONFIG_OVERRIDE, configOverride.toString());
		}
		if (detectedStructures != null) {
			var stringBuilder = new StringBuilder();
			boolean first = true;
			for (var entry : detectedStructures.object2BooleanEntrySet()) {
				if (first) {
					first = false;
				} else {
					stringBuilder.append(',');
				}
				stringBuilder.append(entry.getKey()).append('|').append(entry.getBooleanValue());
			}
			cmp.putString(TAG_DETECTED_STRUCTURE, stringBuilder.toString());
		}
	}

	public static void dropLooniumItems(LivingEntity living, Consumer<ItemStack> consumer) {
		var comp = XplatAbstractions.INSTANCE.looniumComponent(living);
		if (comp != null)
			if (!comp.getDrop().isEmpty() || comp.isDropNothing()) {
				consumer.accept(comp.getDrop());
			}
	}

	public static class WandHud extends BindableFlowerWandHud<LooniumBlockEntity> {
		public WandHud(LooniumBlockEntity flower) {
			super(flower);
		}

		@Override
		public void renderHUD(GuiGraphics gui, Minecraft mc) {
			String lootType;
			if (flower.lootTableOverride != null) {
				lootType = "custom_loot";
			} else if (flower.detectedStructures == null || flower.detectedStructures.isEmpty()) {
				lootType = "generic_loot";
			} else {
				lootType = "structure_loot";
			}
			String lootTypeMessage = I18n.get("botaniamisc.loonium." + lootType);
			int filterWidth = mc.font.width(lootTypeMessage);
			int filterTextStart = (mc.getWindow().getGuiScaledWidth() - filterWidth) / 2;
			int halfMinWidth = (filterWidth + 4) / 2;
			int centerY = mc.getWindow().getGuiScaledHeight() / 2;

			super.renderHUD(gui, mc, halfMinWidth, halfMinWidth, 40);
			gui.drawString(mc.font, lootTypeMessage, filterTextStart, centerY + 30, flower.getColor());
		}
	}
}
