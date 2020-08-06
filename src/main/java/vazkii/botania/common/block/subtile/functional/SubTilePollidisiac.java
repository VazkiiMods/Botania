/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block.subtile.functional;

import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.AxisAlignedBB;

import vazkii.botania.api.subtile.RadiusDescriptor;
import vazkii.botania.api.subtile.TileEntityFunctionalFlower;
import vazkii.botania.common.block.ModSubtiles;
import vazkii.botania.mixin.AccessorItemEntity;

import java.util.List;

public class SubTilePollidisiac extends TileEntityFunctionalFlower {
	private static final int RANGE = 6;
	private static final int MANA_COST = 12;

	public SubTilePollidisiac() {
		super(ModSubtiles.POLLIDISIAC);
	}

	@Override
	public void tickFlower() {
		super.tickFlower();

		if (!getWorld().isRemote) {

			List<ItemEntity> items = getWorld().getEntitiesWithinAABB(ItemEntity.class, new AxisAlignedBB(getEffectivePos().add(-RANGE, -RANGE, -RANGE), getEffectivePos().add(RANGE + 1, RANGE + 1, RANGE + 1)));
			List<AnimalEntity> animals = getWorld().getEntitiesWithinAABB(AnimalEntity.class, new AxisAlignedBB(getEffectivePos().add(-RANGE, -RANGE, -RANGE), getEffectivePos().add(RANGE + 1, RANGE + 1, RANGE + 1)));
			int slowdown = getSlowdownFactor();

			for (AnimalEntity animal : animals) {
				if (getMana() < MANA_COST) {
					break;
				}

				if (animal.getGrowingAge() == 0 && !animal.isInLove()) {
					for (ItemEntity item : items) {
						int age = ((AccessorItemEntity) item).getAge();
						if (age < 60 + slowdown || !item.isAlive()) {
							continue;
						}

						ItemStack stack = item.getItem();
						if (!stack.isEmpty() && animal.isBreedingItem(stack)) {
							stack.shrink(1);

							addMana(-MANA_COST);
							animal.setInLove(1200);
							getWorld().setEntityState(animal, (byte) 18);
						}
					}
				}
			}
		}
	}

	@Override
	public RadiusDescriptor getRadius() {
		return new RadiusDescriptor.Square(getEffectivePos(), RANGE);
	}

	@Override
	public int getMaxMana() {
		return 120;
	}

	@Override
	public int getColor() {
		return 0xCF4919;
	}

}
