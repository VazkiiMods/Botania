/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block.subtile.functional;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Difficulty;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
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
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.phys.Vec3;

import vazkii.botania.api.subtile.RadiusDescriptor;
import vazkii.botania.api.subtile.TileEntityFunctionalFlower;
import vazkii.botania.common.block.ModSubtiles;
import vazkii.botania.common.components.EntityComponents;
import vazkii.botania.common.lib.ModTags;

import java.util.Collections;
import java.util.List;
import java.util.Random;

public class SubTileLoonuim extends TileEntityFunctionalFlower {
	private static final int COST = 35000;
	private static final int RANGE = 5;
	private static final String TAG_LOOT_TABLE = "lootTable";
	private static final String TAG_ITEMSTACK_TO_DROP = "botania:looniumItemStackToDrop";

	private ResourceLocation lootTable = new ResourceLocation("minecraft", "chests/simple_dungeon");

	public SubTileLoonuim(BlockPos pos, BlockState state) {
		super(ModSubtiles.LOONIUM, pos, state);
	}

	@Override
	public void tickFlower() {
		super.tickFlower();

		Level world = getLevel();
		if (!world.isClientSide && redstoneSignal == 0 && ticksExisted % 100 == 0
				&& getMana() >= COST && world.getDifficulty() != Difficulty.PEACEFUL) {
			Random rand = world.random;

			ItemStack stack;
			do {
				LootContext ctx = new LootContext.Builder((ServerLevel) world).create(LootContextParamSets.EMPTY);
				List<ItemStack> stacks = ((ServerLevel) world).getServer().getLootTables()
						.get(lootTable).getRandomItems(ctx);
				if (stacks.isEmpty()) {
					return;
				} else {
					Collections.shuffle(stacks);
					stack = stacks.get(0);
				}
			} while (stack.isEmpty() || ModTags.Items.LOONIUM_BLACKLIST.contains(stack.getItem()));

			int bound = RANGE * 2 + 1;
			int xp = getEffectivePos().getX() - RANGE + rand.nextInt(bound);
			int yp = getEffectivePos().getY();
			int zp = getEffectivePos().getZ() - RANGE + rand.nextInt(bound);

			BlockPos pos = new BlockPos(xp, yp - 1, zp);
			do {
				pos = pos.above();
				if (pos.getY() >= 254) {
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
			entity.getAttribute(Attributes.ATTACK_DAMAGE).addPermanentModifier(new AttributeModifier("Loonium Modififer Damage", 1.5, AttributeModifier.Operation.MULTIPLY_BASE));

			entity.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE,
					entity instanceof Creeper ? 100 : Integer.MAX_VALUE, 0));
			entity.addEffect(new MobEffectInstance(MobEffects.REGENERATION,
					entity instanceof Creeper ? 100 : Integer.MAX_VALUE, 0));

			EntityComponents.LOONIUM_DROP.get(entity).setDrop(stack);

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
		return new RadiusDescriptor.Square(getEffectivePos(), RANGE);
	}

	@Override
	public void readFromPacketNBT(CompoundTag cmp) {
		super.readFromPacketNBT(cmp);
		if (cmp.contains(TAG_LOOT_TABLE)) {
			lootTable = new ResourceLocation(cmp.getString(TAG_LOOT_TABLE));
		}
	}

	@Override
	public void writeToPacketNBT(CompoundTag cmp) {
		super.writeToPacketNBT(cmp);
		cmp.putString(TAG_LOOT_TABLE, lootTable.toString());
	}
}
