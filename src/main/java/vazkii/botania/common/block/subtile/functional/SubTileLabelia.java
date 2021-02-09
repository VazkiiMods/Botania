/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block.subtile.functional;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.EntityPredicates;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.server.ServerWorld;

import vazkii.botania.api.subtile.RadiusDescriptor;
import vazkii.botania.api.subtile.TileEntityFunctionalFlower;
import vazkii.botania.common.block.ModSubtiles;
import vazkii.botania.common.core.handler.ModSounds;
import vazkii.botania.mixin.AccessorItemEntity;

import java.util.List;

public class SubTileLabelia extends TileEntityFunctionalFlower {
	private static final int PICKUP_RANGE = 0;
	private static final int RENAME_RANGE = 2;
	private static final int COST = 500;

	public SubTileLabelia() {
		super(ModSubtiles.LABELIA);
	}

	@Override
	public boolean acceptsRedstone() {
		return true;
	}

	@Override
	public void tickFlower() {
		super.tickFlower();

		if (!world.isRemote && redstoneSignal == 0 && getMana() >= COST) {
			BlockPos effPos = getEffectivePos();
			int x = effPos.getX();
			int y = effPos.getY();
			int z = effPos.getZ();

			for (ItemEntity nameTagEnt : world.getEntitiesWithinAABB(ItemEntity.class,
					new AxisAlignedBB(x - PICKUP_RANGE, y, z - PICKUP_RANGE,
							x + PICKUP_RANGE + 1, y + 1, z + PICKUP_RANGE + 1),
					EntityPredicates.IS_ALIVE)) {
				ItemStack nameTag = nameTagEnt.getItem();
				int age = ((AccessorItemEntity) nameTagEnt).getAge();
				if (age < 60 + getSlowdownFactor() || nameTag.isEmpty()) {
					continue;
				}

				if (nameTag.getItem() == Items.NAME_TAG && nameTag.hasDisplayName()) {
					AxisAlignedBB renameArea = new AxisAlignedBB(x - RENAME_RANGE, y, z - RENAME_RANGE, x + RENAME_RANGE + 1, y + 1, z + RENAME_RANGE + 1);
					ITextComponent name = nameTag.getDisplayName();
					List<LivingEntity> nameableEntities = world.getEntitiesWithinAABB(LivingEntity.class, renameArea,
							EntityPredicates.IS_ALIVE.and(e -> !name.equals(e.getCustomName()) && !(e instanceof PlayerEntity)));

					List<ItemEntity> nameableItems = world.getEntitiesWithinAABB(ItemEntity.class, renameArea,
							i -> {
								int iAge = ((AccessorItemEntity) i).getAge();
								return i.isAlive() && i != nameTagEnt && iAge >= 60 + getSlowdownFactor() && !name.equals(i.getItem().getDisplayName());
							});

					if (!nameableItems.isEmpty() || !nameableEntities.isEmpty()) {
						for (LivingEntity e : nameableEntities) {
							// [VanillaCopy] from net.minecraft.item.NameTagItem
							e.setCustomName(name);
							if (e instanceof MobEntity) {
								((MobEntity) e).enablePersistence();
							}
						}
						for (ItemEntity i : nameableItems) {
							i.getItem().setDisplayName(name);
							i.setItem(i.getItem()); // ensure it syncs
							((ServerWorld) world).spawnParticle(ParticleTypes.INSTANT_EFFECT,
									i.getPosX(), i.getPosY(), i.getPosZ(),
									3, 0, 0, 0, 0);

						}
						addMana(-COST);
						nameTag.shrink(1);
						world.playSound(null, x + 0.5, y + 0.5, z + 0.5, ModSounds.labelia,
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
