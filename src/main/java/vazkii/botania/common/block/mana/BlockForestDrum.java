/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block.mana;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
<<<<<<< HEAD
import net.minecraft.block.ShapeContext;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.Shearable;
import net.minecraft.entity.mob.MobEntity;
=======
import net.minecraft.entity.Entity;
import net.minecraft.entity.IShearable;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.item.ItemEntity;
>>>>>>> 4c77b50dc7c48e2738e9dca513b25bdb627819fb
import net.minecraft.entity.passive.CowEntity;
import net.minecraft.entity.passive.MooshroomEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

import vazkii.botania.api.internal.IManaBurst;
import vazkii.botania.api.mana.IManaTrigger;
import vazkii.botania.common.block.BlockModWaterloggable;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.item.ItemHorn;
import vazkii.botania.common.item.ModItems;

import javax.annotation.Nonnull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BlockForestDrum extends BlockModWaterloggable implements IManaTrigger {

	public enum Variant {
		WILD,
		GATHERING,
		CANOPY
	}

	private static final VoxelShape SHAPE = Block.createCuboidShape(3, 1, 3, 13, 15, 13);
	private final Variant variant;

	public BlockForestDrum(Variant v, Settings builder) {
		super(builder);
		this.variant = v;
	}

	@Nonnull
	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext ctx) {
		return SHAPE;
	}

	public void convertNearby(Entity entity, Item from, Item to) {
		World world = entity.world;
		List<ItemEntity> items = world.getEntitiesWithinAABB(ItemEntity.class, entity.getBoundingBox());
		for (ItemEntity item : items) {
			ItemStack itemstack = item.getItem();
			if (!itemstack.isEmpty() && itemstack.getItem() == from && !world.isRemote) {
				while (itemstack.getCount() > 0) {
					ItemEntity ent = entity.entityDropItem(new ItemStack(to), 1.0F);
					ent.setMotion(ent.getMotion().add(
							world.rand.nextFloat() * 0.05F,
							(world.rand.nextFloat() - world.rand.nextFloat()) * 0.1F,
							(world.rand.nextFloat() - world.rand.nextFloat()) * 0.1F
					));
					itemstack.shrink(1);
				}
				item.remove();
			}
		}
	}

	@Override
	public void onBurstCollision(IManaBurst burst, World world, BlockPos pos) {
		if (burst.isFake()) {
			return;
		}
		if (world.isClient) {
			world.addParticle(ParticleTypes.NOTE, pos.getX() + 0.5, pos.getY() + 1.2, pos.getZ() + 0.5D, 1.0 / 24.0, 0, 0);
			return;
		}
		if (variant == Variant.WILD) {
			ItemHorn.breakGrass(world, new ItemStack(ModItems.grassHorn), pos);
		} else if (variant == Variant.CANOPY) {
			ItemHorn.breakGrass(world, new ItemStack(ModItems.leavesHorn), pos);
		} else {
			int range = 10;
			List<MobEntity> entities = world.getNonSpectatingEntities(MobEntity.class, new Box(pos.add(-range, -range, -range), pos.add(range + 1, range + 1, range + 1)));
			List<MobEntity> shearables = new ArrayList<>();
			ItemStack stack = new ItemStack(ModBlocks.gatheringDrum);

			for (MobEntity entity : entities) {
				if (entity instanceof CowEntity) {
					convertNearby(entity, Items.BUCKET, Items.MILK_BUCKET);
					if (entity instanceof MooshroomEntity) {
						convertNearby(entity, Items.BOWL, Items.MUSHROOM_STEW);
					}
				} else if (entity instanceof IShearable && ((IShearable) entity).isShearable()
						|| entity instanceof IForgeShearable && ((IForgeShearable) entity).isShearable(stack, world, entity.getPosition())) {
					shearables.add(entity);
				}
			}

			Collections.shuffle(shearables);
			int sheared = 0;

			for (MobEntity entity : shearables) {
				if (sheared > 4) {
					break;
				}

				if (entity instanceof Shearable) {
					((Shearable) entity).sheared(SoundCategory.BLOCKS);
				}

				++sheared;
			}
		}

		for (int i = 0; i < 10; i++) {
			world.playSound(null, pos, SoundEvents.BLOCK_NOTE_BLOCK_BASEDRUM, SoundCategory.BLOCKS, 1F, 1F);
		}
	}
}
