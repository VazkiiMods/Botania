/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block.flower.functional;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.random.WeightedEntry;
import net.minecraft.util.random.WeightedRandom;
import net.minecraft.world.Difficulty;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.CaveSpider;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.monster.Drowned;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.entity.monster.Husk;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.Skeleton;
import net.minecraft.world.entity.monster.Spider;
import net.minecraft.world.entity.monster.Stray;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.phys.Vec3;

import vazkii.botania.api.block_entity.FunctionalFlowerBlockEntity;
import vazkii.botania.api.block_entity.RadiusDescriptor;
import vazkii.botania.common.block.BotaniaFlowerBlocks;
import vazkii.botania.common.helper.StructureHelper;
import vazkii.botania.common.lib.BotaniaTags;
import vazkii.botania.xplat.XplatAbstractions;

import javax.annotation.Nullable;

import java.util.*;
import java.util.function.Consumer;

public class LooniumBlockEntity extends FunctionalFlowerBlockEntity {
	private static final int COST = 35000;
	private static final int RANGE = 5;
	public static final Set<Class<? extends Monster>> VALID_MOBS = Set.of(
			Creeper.class,
			EnderMan.class,
			Skeleton.class,
			Stray.class,
			Spider.class,
			Zombie.class
	);
	private static final String TAG_CUSTOM_LOOT_TABLE = "lootTable";
	private @Nullable ResourceLocation customLootTable = null;

	//TODO make this pull from a JSON file in a datapack instead of being hardcoded
	private static final Map<ResourceLocation, WeightedEntry.Wrapper<ResourceLocation>> lootTables = Map.of(
			new ResourceLocation("minecraft:ancient_city"), WeightedEntry.wrap(new ResourceLocation("botania:loonium/ancient_city"), 1),
			new ResourceLocation("minecraft:bastion"), WeightedEntry.wrap(new ResourceLocation("botania:loonium/bastion"), 1),
			new ResourceLocation("minecraft:end_city"), WeightedEntry.wrap(new ResourceLocation("botania:loonium/end_city"), 1),
			new ResourceLocation("minecraft:fortress"), WeightedEntry.wrap(new ResourceLocation("minecraft:nether_bridge"), 1),
			new ResourceLocation("minecraft:stronghold"), WeightedEntry.wrap(new ResourceLocation("botania:loonium/stronghold"), 1),
			new ResourceLocation("minecraft:mansion"), WeightedEntry.wrap(new ResourceLocation("minecraft:woodland_mansion"), 1)
	);
	private static final ResourceLocation defaultLootTable = new ResourceLocation("minecraft", "chests/simple_dungeon");

	//Returns an empty loot table if the selected ResourceLocation is invalid
	private LootTable getLootTable(ServerLevel level) {
		List<WeightedEntry.Wrapper<ResourceLocation>> validLoot = new ArrayList<>();
		for (var entry : lootTables.entrySet()) {
			ResourceLocation key = entry.getKey();
			WeightedEntry.Wrapper<ResourceLocation> value = entry.getValue();
			Structure structure = StructureHelper.getStructure(level, key);
			if (StructureHelper.isInStructureBounds(level, getBlockPos(), structure)) {
				validLoot.add(value);
			}
		}
		Optional<WeightedEntry.Wrapper<ResourceLocation>> roll = WeightedRandom.getRandomItem(level.random, validLoot);
		if (roll.isPresent()) {
			return level.getServer().getLootTables().get(roll.get().getData());
		} else {
			return level.getServer().getLootTables().get(defaultLootTable);
		}
	}

	public LooniumBlockEntity(BlockPos pos, BlockState state) {
		super(BotaniaFlowerBlocks.LOONIUM, pos, state);
	}

	@Override
	public void tickFlower() {
		super.tickFlower();

		Level world = getLevel();
		if (!world.isClientSide && redstoneSignal == 0 && ticksExisted % 100 == 0
				&& getMana() >= COST && world.getDifficulty() != Difficulty.PEACEFUL) {
			var rand = world.random;

			ItemStack stack;
			do {
				LootContext ctx = new LootContext.Builder((ServerLevel) world).create(LootContextParamSets.EMPTY);
				LootTable table = customLootTable == null ? getLootTable((ServerLevel) world) : level.getServer().getLootTables().get(customLootTable);
				List<ItemStack> stacks = table.getRandomItems(ctx);
				if (stacks.isEmpty()) {
					return;
				} else {
					Collections.shuffle(stacks);
					stack = stacks.get(0);
				}
			} while (stack.isEmpty() || stack.is(BotaniaTags.Items.LOONIUM_BLACKLIST));

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

			Monster entity = null;
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
							entity = new Spider(EntityType.SPIDER, world);
						}
						break;
				}
			}

			entity.absMoveTo(x, y, z, world.random.nextFloat() * 360F, 0);
			entity.setDeltaMovement(Vec3.ZERO);

			entity.getAttribute(Attributes.MAX_HEALTH).addPermanentModifier(new AttributeModifier("Loonium Modififer Health", 2, AttributeModifier.Operation.MULTIPLY_BASE));
			entity.setHealth(entity.getMaxHealth());
			entity.getAttribute(Attributes.ATTACK_DAMAGE).addPermanentModifier(new AttributeModifier("Loonium Modififer Damage", 1.5, AttributeModifier.Operation.MULTIPLY_BASE));

			entity.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE,
					entity instanceof Creeper ? 100 : Integer.MAX_VALUE, 0));
			entity.addEffect(new MobEffectInstance(MobEffects.REGENERATION,
					entity instanceof Creeper ? 100 : Integer.MAX_VALUE, 0));

			XplatAbstractions.INSTANCE.looniumComponent(entity).setDrop(stack);

			entity.finalizeSpawn((ServerLevelAccessor) world, world.getCurrentDifficultyAt(pos), MobSpawnType.SPAWNER, null, null);
			world.addFreshEntity(entity);
			entity.spawnAnim();

			addMana(-COST);
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
		if (cmp.contains(TAG_CUSTOM_LOOT_TABLE)) {
			customLootTable = new ResourceLocation(cmp.getString(TAG_CUSTOM_LOOT_TABLE));
		}
	}

	@Override
	public void writeToPacketNBT(CompoundTag cmp) {
		super.writeToPacketNBT(cmp);
		if (customLootTable != null) {
			cmp.putString(TAG_CUSTOM_LOOT_TABLE, customLootTable.toString());
		}
	}

	public static void dropLooniumItems(LivingEntity living, Consumer<ItemStack> consumer) {
		var comp = XplatAbstractions.INSTANCE.looniumComponent(living);
		if (comp != null && !comp.getDrop().isEmpty()) {
			consumer.accept(comp.getDrop());
		}
	}
}
