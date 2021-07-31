/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block.subtile.functional;

import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.predicate.entity.EntityPredicates;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;

import vazkii.botania.api.subtile.RadiusDescriptor;
import vazkii.botania.api.subtile.TileEntityFunctionalFlower;
import vazkii.botania.common.block.ModSubtiles;
import vazkii.botania.common.core.handler.ModSounds;
import vazkii.botania.mixin.AccessorItemEntity;

import java.util.List;

public class SubTileLabellia extends TileEntityFunctionalFlower {
	private static final int PICKUP_RANGE = 0;
	private static final int RENAME_RANGE = 2;
	private static final int COST = 500;

	public SubTileLabellia() {
		super(ModSubtiles.LABELLIA);
	}

	@Override
	public boolean acceptsRedstone() {
		return true;
	}

	@Override
	public void tickFlower() {
		super.tickFlower();

		if (!world.isClient && redstoneSignal == 0 && getMana() >= COST) {
			BlockPos effPos = getEffectivePos();
			int x = effPos.getX();
			int y = effPos.getY();
			int z = effPos.getZ();

			for (ItemEntity nameTagEnt : world.getEntitiesByClass(ItemEntity.class,
					new Box(x - PICKUP_RANGE, y, z - PICKUP_RANGE,
							x + PICKUP_RANGE + 1, y + 1, z + PICKUP_RANGE + 1),
					EntityPredicates.VALID_ENTITY)) {
				ItemStack nameTag = nameTagEnt.getStack();
				int age = ((AccessorItemEntity) nameTagEnt).getAge();
				if (age < 60 + getSlowdownFactor() || nameTag.isEmpty()) {
					continue;
				}

				if (nameTag.getItem() == Items.NAME_TAG && nameTag.hasCustomName()) {
					Box renameArea = new Box(x - RENAME_RANGE, y, z - RENAME_RANGE, x + RENAME_RANGE + 1, y + 1, z + RENAME_RANGE + 1);
					Text name = nameTag.getName();
					List<LivingEntity> nameableEntities = world.getEntitiesByClass(LivingEntity.class, renameArea,
							EntityPredicates.VALID_ENTITY.and(e -> !name.equals(e.getCustomName()) && !(e instanceof PlayerEntity)));

					List<ItemEntity> nameableItems = world.getEntitiesByClass(ItemEntity.class, renameArea,
							i -> {
								int iAge = ((AccessorItemEntity) i).getAge();
								return i.isAlive() && i != nameTagEnt && iAge >= 60 + getSlowdownFactor() && !name.equals(i.getStack().getName());
							});

					if (!nameableItems.isEmpty() || !nameableEntities.isEmpty()) {
						for (LivingEntity e : nameableEntities) {
							// [VanillaCopy] from net.minecraft.item.NameTagItem
							e.setCustomName(name);
							if (e instanceof MobEntity) {
								((MobEntity) e).setPersistent();
							}
						}
						for (ItemEntity i : nameableItems) {
							i.getStack().setCustomName(name);
							i.setStack(i.getStack()); // ensure it syncs
							((ServerWorld) world).spawnParticles(ParticleTypes.INSTANT_EFFECT,
									i.getX(), i.getY(), i.getZ(),
									3, 0, 0, 0, 0);

						}
						addMana(-COST);
						nameTag.decrement(1);
						world.playSound(null, x + 0.5, y + 0.5, z + 0.5, ModSounds.labellia,
								SoundCategory.BLOCKS, 1, 1);
						break;
					}
				}
			}
		}
	}

	@Override
	public RadiusDescriptor getRadius() {
		return new RadiusDescriptor.Square(getEffectivePos(), RENAME_RANGE);
	}

	@Override
	public RadiusDescriptor getSecondaryRadius() {
		return new RadiusDescriptor.Square(getEffectivePos(), PICKUP_RANGE);
	}

	@Override
	public int getMaxMana() {
		return 6000;
	}
}
