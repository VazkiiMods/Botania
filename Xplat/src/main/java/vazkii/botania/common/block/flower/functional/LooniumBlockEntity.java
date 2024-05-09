/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block.flower.functional;

import it.unimi.dsi.fastutil.objects.ObjectBooleanPair;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.CaveSpider;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.monster.Drowned;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.entity.monster.Husk;
import net.minecraft.world.entity.monster.Skeleton;
import net.minecraft.world.entity.monster.Spider;
import net.minecraft.world.entity.monster.Stray;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.phys.Vec3;

import org.jetbrains.annotations.Nullable;

import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.block_entity.FunctionalFlowerBlockEntity;
import vazkii.botania.api.block_entity.RadiusDescriptor;
import vazkii.botania.common.block.BotaniaFlowerBlocks;
import vazkii.botania.common.lib.BotaniaTags;
import vazkii.botania.xplat.XplatAbstractions;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static vazkii.botania.common.lib.ResourceLocationHelper.prefix;

public class LooniumBlockEntity extends FunctionalFlowerBlockEntity {
	private static final int COST = 35000;
	private static final int RANGE = 5;
	private static final String TAG_LOOT_TABLE = "lootTable";
	public static final ResourceLocation[] DEFAULT_LOOT_TABLES = { prefix("loonium/default") };

	private ResourceLocation[] lootTables = DEFAULT_LOOT_TABLES;

	public LooniumBlockEntity(BlockPos pos, BlockState state) {
		super(BotaniaFlowerBlocks.LOONIUM, pos, state);
	}

	@Override
	public void tickFlower() {
		super.tickFlower();

		if (!(getLevel() instanceof ServerLevel world)) {
			return;
		}

		if (ticksExisted == 1 && lootTables == DEFAULT_LOOT_TABLES) {
			autodetectStructureLootTables(world);
		}

		if (redstoneSignal == 0 && ticksExisted % 100 == 0
				&& getMana() >= COST && world.getDifficulty() != Difficulty.PEACEFUL) {
			var rand = world.random;

			ItemStack stack = pickRandomLoot(world, rand);
			if (stack.isEmpty()) {
				return;
			}

			int bound = RANGE * 2 + 1;
			int xp = getEffectivePos().getX() - RANGE + rand.nextInt(bound);
			int yp = getEffectivePos().getY();
			int zp = getEffectivePos().getZ() - RANGE + rand.nextInt(bound);

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

			// TODO: Mob types and weights should be defined per structure
			Mob entity = spawnMob(world);
			if (entity == null) {
				return;
			}

			entity.absMoveTo(x, y, z, world.random.nextFloat() * 360F, 0);
			entity.setDeltaMovement(Vec3.ZERO);

			// TODO: mob attribute modifications should be defined along with mob configuration
			entity.getAttribute(Attributes.MAX_HEALTH).addPermanentModifier(new AttributeModifier("Loonium Modififer Health", 2, AttributeModifier.Operation.MULTIPLY_BASE));
			entity.setHealth(entity.getMaxHealth());
			entity.getAttribute(Attributes.ATTACK_DAMAGE).addPermanentModifier(new AttributeModifier("Loonium Modififer Damage", 1.5, AttributeModifier.Operation.MULTIPLY_BASE));

			// TODO: mob potion effects should be defined in the mob configuration
			entity.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE,
					entity instanceof Creeper ? 100 : MobEffectInstance.INFINITE_DURATION, 0));
			entity.addEffect(new MobEffectInstance(MobEffects.REGENERATION,
					entity instanceof Creeper ? 100 : MobEffectInstance.INFINITE_DURATION, 0));

			XplatAbstractions.INSTANCE.looniumComponent(entity).setDrop(stack);

			entity.finalizeSpawn(world, world.getCurrentDifficultyAt(pos), MobSpawnType.SPAWNER, null, null);
			// prevent armor/weapon drops on player kill, also no nautilus shells from drowned:
			Arrays.stream(EquipmentSlot.values()).forEach(slot -> entity.setDropChance(slot, 0));
			world.addFreshEntity(entity);
			entity.spawnAnim();

			addMana(-COST);
			sync();
		}
	}

	private static @Nullable Mob spawnMob(ServerLevel world) {
		Mob entity = null;
		if (world.random.nextInt(50) == 0) {
			entity = new EnderMan(EntityType.ENDERMAN, world);
		} else if (world.random.nextInt(10) == 0) {
			entity = new Creeper(EntityType.CREEPER, world);
			if (world.random.nextInt(200) == 0) {
				CompoundTag charged = new CompoundTag();
				charged.putBoolean("powered", true);
				entity.readAdditionalSaveData(charged);
			}
		} else {
			switch (world.random.nextInt(3)) {
				case 0:
					if (world.random.nextInt(10) == 0) {
						entity = new Husk(EntityType.HUSK, world);
					} else if (world.random.nextInt(5) == 0) {
						entity = new Drowned(EntityType.DROWNED, world);
					} else {
						entity = new Zombie(world);
					}
					break;
				case 1:
					if (world.random.nextInt(10) == 0) {
						entity = new Stray(EntityType.STRAY, world);
					} else {
						entity = new Skeleton(EntityType.SKELETON, world);
					}
					break;
				case 2:
					if (world.random.nextInt(10) == 0) {
						entity = new CaveSpider(EntityType.CAVE_SPIDER, world);
					} else {
						// Note: could spawn as spider jockey, and we have no control over the skeleton
						entity = new Spider(EntityType.SPIDER, world);
					}
					break;
			}
		}
		return entity;
	}

	private ItemStack pickRandomLoot(ServerLevel world, RandomSource rand) {
		var lootTableId = lootTables[lootTables.length > 1 ? new Random().nextInt(lootTables.length) : 0];
		LootParams params = new LootParams.Builder(world).create(LootContextParamSets.EMPTY);
		List<ItemStack> stacks = world.getServer().getLootData().getLootTable(lootTableId).getRandomItems(params, rand.nextLong());
		stacks.removeIf(s -> s.isEmpty() || s.is(BotaniaTags.Items.LOONIUM_BLACKLIST));
		if (stacks.isEmpty()) {
			return ItemStack.EMPTY;
		} else {
			Collections.shuffle(stacks);
			return stacks.get(0);
		}
	}

	private void autodetectStructureLootTables(ServerLevel world) {
		// structure ID -> whether the position is inside a structure piece (false = only overall bounding box)
		var detectedStructures = new ArrayList<ObjectBooleanPair<ResourceLocation>>();
		StructureManager structureManager = world.structureManager();
		BlockPos pos = getBlockPos();
		var structures = structureManager.getAllStructuresAt(pos);
		for (var entry : structures.entrySet()) {
			Structure structure = entry.getKey();
			var start = structureManager.getStructureAt(pos, structure);
			if (start.isValid()) {
				ResourceLocation structureId = world.registryAccess().registryOrThrow(Registries.STRUCTURE).getKey(structure);
				boolean insidePiece = structureManager.structureHasPieceAt(pos, start);
				BotaniaAPI.LOGGER.info("Found structure {}, inside piece: {}", structureId, insidePiece);
				detectedStructures.add(ObjectBooleanPair.of(structureId, insidePiece));
			}
		}

		if (detectedStructures.isEmpty()) {
			// not within any structures, keep default loot table
			return;
		}

		var lootTableCandidates = new ArrayList<ResourceLocation>(detectedStructures.size());
		for (var entry : detectedStructures) {
			// TODO: grab structure configuration data from registry (assume must be inside a piece for now)
			if (!entry.valueBoolean()) {
				continue;
			}
			var structureId = entry.key();
			var candidateId = prefix("loonium/%s/%s".formatted(structureId.getNamespace(), structureId.getPath()));
			LootTable lootTable = world.getServer().getLootData().getLootTable(candidateId);
			if (lootTable != LootTable.EMPTY) {
				lootTableCandidates.add(candidateId);
			}
		}

		if (!lootTableCandidates.isEmpty()) {
			BotaniaAPI.LOGGER.info("Using loot tables: {}", lootTableCandidates);
			lootTables = lootTableCandidates.toArray(ResourceLocation[]::new);
			setChanged();
			sync();
		}
	}

	@Override
	public int getColor() {
		return 0x274A00;
	}

	@Override
	public int getMaxMana() {
		return COST;
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
	public void readFromPacketNBT(CompoundTag cmp) {
		super.readFromPacketNBT(cmp);
		if (cmp.contains(TAG_LOOT_TABLE)) {
			var lootTableString = cmp.getString(TAG_LOOT_TABLE);
			lootTables = Arrays.stream(lootTableString.split(","))
					.map(ResourceLocation::new).toArray(ResourceLocation[]::new);
		}
	}

	@Override
	public void writeToPacketNBT(CompoundTag cmp) {
		super.writeToPacketNBT(cmp);
		if (lootTables.length >= 1) {
			cmp.putString(TAG_LOOT_TABLE,
					Arrays.stream(lootTables).map(ResourceLocation::toString).collect(Collectors.joining(",")));
		}
	}

	public static void dropLooniumItems(LivingEntity living, Consumer<ItemStack> consumer) {
		var comp = XplatAbstractions.INSTANCE.looniumComponent(living);
		if (comp != null && !comp.getDrop().isEmpty()) {
			consumer.accept(comp.getDrop());
		}
	}

	public static class WandHud extends BindableFlowerWandHud<LooniumBlockEntity> {
		public WandHud(LooniumBlockEntity flower) {
			super(flower);
		}

		@Override
		public void renderHUD(GuiGraphics gui, Minecraft mc) {
			String attuneType;
			if (Arrays.equals(flower.lootTables, DEFAULT_LOOT_TABLES)) {
				attuneType = "generic_drops";
			} else if (flower.lootTables.length == 1) {
				attuneType = "structure_drops";
			} else {
				attuneType = "multiple_structure_drops";
			}
			String attuned = I18n.get("botaniamisc.loonium." + attuneType).formatted(flower.lootTables.length);
			int filterWidth = mc.font.width(attuned);
			int filterTextStart = (mc.getWindow().getGuiScaledWidth() - filterWidth) / 2;
			int halfMinWidth = (filterWidth + 4) / 2;
			int centerY = mc.getWindow().getGuiScaledHeight() / 2;

			super.renderHUD(gui, mc, halfMinWidth, halfMinWidth, 40);
			gui.drawString(mc.font, attuned, filterTextStart, centerY + 30, flower.getColor());
		}
	}
}
