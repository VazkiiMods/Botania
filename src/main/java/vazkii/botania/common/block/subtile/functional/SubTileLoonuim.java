/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block.subtile.functional;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.CaveSpiderEntity;
import net.minecraft.entity.mob.CreeperEntity;
import net.minecraft.entity.mob.DrownedEntity;
import net.minecraft.entity.mob.EndermanEntity;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.HuskEntity;
import net.minecraft.entity.mob.SkeletonEntity;
import net.minecraft.entity.mob.SpiderEntity;
import net.minecraft.entity.mob.StrayEntity;
import net.minecraft.entity.mob.ZombieEntity;
import net.minecraft.entity.monster.*;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.context.LootContextTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.Difficulty;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingDropsEvent;

import vazkii.botania.api.subtile.RadiusDescriptor;
import vazkii.botania.api.subtile.TileEntityFunctionalFlower;
import vazkii.botania.common.block.ModSubtiles;
import vazkii.botania.common.lib.ModTags;

import java.util.Collections;
import java.util.List;
import java.util.Random;

public class SubTileLoonuim extends TileEntityFunctionalFlower {
	private static final int COST = 35000;
	private static final int RANGE = 5;
	private static final String TAG_LOOT_TABLE = "lootTable";
	private static final String TAG_ITEMSTACK_TO_DROP = "botania:looniumItemStackToDrop";

	private Identifier lootTable = new Identifier("minecraft", "chests/simple_dungeon");

	public SubTileLoonuim() {
		super(ModSubtiles.LOONIUM);
	}

	@Override
	public void tickFlower() {
		super.tickFlower();

		World world = getWorld();
		if (!world.isClient && redstoneSignal == 0 && ticksExisted % 100 == 0
				&& getMana() >= COST && world.getDifficulty() != Difficulty.PEACEFUL) {
			Random rand = world.random;

			ItemStack stack;
			do {
				LootContext ctx = new LootContext.Builder((ServerWorld) world).build(LootContextTypes.EMPTY);
				List<ItemStack> stacks = ((ServerWorld) world).getServer().getLootManager()
						.getTable(lootTable).generateLoot(ctx);
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
				pos = pos.up();
				if (pos.getY() >= 254) {
					return;
				}
			} while (world.getBlockState(pos).shouldSuffocate(world, pos));
			pos = pos.up();

			double x = pos.getX() + Math.random();
			double y = pos.getY() + Math.random();
			double z = pos.getZ() + Math.random();

			HostileEntity entity = null;
			if (world.random.nextInt(50) == 0) {
				entity = new EndermanEntity(EntityType.ENDERMAN, world);
			} else if (world.random.nextInt(10) == 0) {
				entity = new CreeperEntity(EntityType.CREEPER, world);
				if (world.random.nextInt(200) == 0) {
					entity.onStruckByLightning(null);
				}
			} else {
				switch (world.random.nextInt(3)) {
				case 0:
					if (world.random.nextInt(10) == 0) {
						entity = new HuskEntity(EntityType.HUSK, world);
					} else if (world.random.nextInt(5) == 0) {
						entity = new DrownedEntity(EntityType.DROWNED, world);
					} else {
						entity = new ZombieEntity(world);
					}
					break;
				case 1:
					if (world.random.nextInt(10) == 0) {
						entity = new StrayEntity(EntityType.STRAY, world);
					} else {
						entity = new SkeletonEntity(EntityType.SKELETON, world);
					}
					break;
				case 2:
					if (world.random.nextInt(10) == 0) {
						entity = new CaveSpiderEntity(EntityType.CAVE_SPIDER, world);
					} else {
						entity = new SpiderEntity(EntityType.SPIDER, world);
					}
					break;
				}
			}

			entity.updatePositionAndAngles(x, y, z, world.random.nextFloat() * 360F, 0);
			entity.setVelocity(Vec3d.ZERO);

			entity.getAttributeInstance(EntityAttributes.GENERIC_MAX_HEALTH).addPersistentModifier(new EntityAttributeModifier("Loonium Modififer Health", 2, EntityAttributeModifier.Operation.MULTIPLY_BASE));
			entity.getAttributeInstance(EntityAttributes.GENERIC_ATTACK_DAMAGE).addPersistentModifier(new EntityAttributeModifier("Loonium Modififer Damage", 1.5, EntityAttributeModifier.Operation.MULTIPLY_BASE));

			entity.addStatusEffect(new StatusEffectInstance(StatusEffects.FIRE_RESISTANCE,
					entity instanceof CreeperEntity ? 100 : Integer.MAX_VALUE, 0));
			entity.addStatusEffect(new StatusEffectInstance(StatusEffects.REGENERATION,
					entity instanceof CreeperEntity ? 100 : Integer.MAX_VALUE, 0));

			CompoundTag cmp = stack.toTag(new CompoundTag());
			entity.getPersistentData().put(TAG_ITEMSTACK_TO_DROP, cmp);

			entity.initialize(world, world.getLocalDifficulty(pos), SpawnReason.SPAWNER, null, null);
			world.spawnEntity(entity);
			entity.playSpawnEffects();

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
			lootTable = new Identifier(cmp.getString(TAG_LOOT_TABLE));
		}
	}

	@Override
	public void writeToPacketNBT(CompoundTag cmp) {
		super.writeToPacketNBT(cmp);
		cmp.putString(TAG_LOOT_TABLE, lootTable.toString());
	}

	public static void onDrops(LivingDropsEvent event) {
		LivingEntity e = event.getEntityLiving();
		if (e.getPersistentData().contains(TAG_ITEMSTACK_TO_DROP)) {
			CompoundTag cmp = e.getPersistentData().getCompound(TAG_ITEMSTACK_TO_DROP);
			ItemStack stack = ItemStack.fromTag(cmp);
			event.getDrops().clear();
			event.getDrops().add(new ItemEntity(e.world, e.getX(), e.getY(), e.getZ(), stack));
		}
	}
}
