/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block.mana;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.Shearable;
import net.minecraft.world.entity.animal.Cow;
import net.minecraft.world.entity.animal.MushroomCow;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import org.jetbrains.annotations.NotNull;

import vazkii.botania.api.internal.ManaBurst;
import vazkii.botania.api.mana.ManaTrigger;
import vazkii.botania.common.block.BotaniaWaterloggedBlock;
import vazkii.botania.common.block.flower.functional.BergamuteBlockEntity;
import vazkii.botania.common.handler.BotaniaSounds;
import vazkii.botania.common.item.BotaniaItems;
import vazkii.botania.common.item.HornItem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DrumBlock extends BotaniaWaterloggedBlock {

	public enum Variant {
		WILD,
		GATHERING,
		CANOPY
	}

	private static final VoxelShape SHAPE = Block.box(3, 1, 3, 13, 15, 13);
	private final Variant variant;

	public DrumBlock(Variant v, Properties builder) {
		super(builder);
		this.variant = v;
	}

	@NotNull
	@Override
	public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext ctx) {
		return SHAPE;
	}

	private static void convertNearby(Entity entity, Item from, Item to) {
		Level world = entity.level;
		List<ItemEntity> items = world.getEntitiesOfClass(ItemEntity.class, entity.getBoundingBox());
		for (ItemEntity item : items) {
			ItemStack itemstack = item.getItem();
			if (!itemstack.isEmpty() && itemstack.is(from) && !world.isClientSide) {
				while (itemstack.getCount() > 0) {
					ItemEntity ent = entity.spawnAtLocation(new ItemStack(to), 1.0F);
					ent.setDeltaMovement(ent.getDeltaMovement().add(
							world.random.nextFloat() * 0.05F,
							(world.random.nextFloat() - world.random.nextFloat()) * 0.1F,
							(world.random.nextFloat() - world.random.nextFloat()) * 0.1F
					));
					itemstack.shrink(1);
				}
				item.discard();
			}
		}
	}

	public static class ManaTriggerImpl implements ManaTrigger {
		private final Level world;
		private final BlockPos pos;
		private final Variant variant;

		public ManaTriggerImpl(Level world, BlockPos pos, BlockState state) {
			this.world = world;
			this.pos = pos;
			this.variant = ((DrumBlock) state.getBlock()).variant;
		}

		@Override
		public void onBurstCollision(ManaBurst burst) {
			if (burst.isFake()) {
				return;
			}
			if (world.isClientSide) {
				world.addParticle(ParticleTypes.NOTE, pos.getX() + 0.5, pos.getY() + 1.2, pos.getZ() + 0.5D, 1.0 / 24.0, 0, 0);
				return;
			}
			if (variant == Variant.WILD) {
				HornItem.breakGrass(world, new ItemStack(BotaniaItems.grassHorn), pos, null);
			} else if (variant == Variant.CANOPY) {
				HornItem.breakGrass(world, new ItemStack(BotaniaItems.leavesHorn), pos, null);
			} else {
				int range = 10;
				List<Mob> entities = world.getEntitiesOfClass(Mob.class, new AABB(pos.offset(-range, -range, -range), pos.offset(range + 1, range + 1, range + 1)), e -> !BergamuteBlockEntity.isBergamuteNearby(world, e.getX(), e.getY(), e.getZ()));
				List<Mob> shearables = new ArrayList<>();

				for (Mob entity : entities) {
					if (entity instanceof Cow) {
						convertNearby(entity, Items.BUCKET, Items.MILK_BUCKET);
						if (entity instanceof MushroomCow) {
							convertNearby(entity, Items.BOWL, Items.MUSHROOM_STEW);
						}
					} else if (entity instanceof Shearable shearable && shearable.readyForShearing()) {
						shearables.add(entity);
					}
				}

				Collections.shuffle(shearables);
				int sheared = 0;

				for (Mob entity : shearables) {
					if (sheared > 4) {
						break;
					}

					if (entity instanceof Shearable shearable) {
						shearable.shear(SoundSource.BLOCKS);
					}

					++sheared;
				}
			}

			for (int i = 0; i < 10; i++) {
				world.playSound(null, pos, BotaniaSounds.drum, SoundSource.BLOCKS, 1F, 1F);
			}
		}
	}
}
