/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block.subtile.functional;

import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.AABB;

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

		if (!getLevel().isClientSide) {

			List<ItemEntity> items = getLevel().getEntitiesOfClass(ItemEntity.class, new AABB(getEffectivePos().offset(-RANGE, -RANGE, -RANGE), getEffectivePos().offset(RANGE + 1, RANGE + 1, RANGE + 1)));
			List<Animal> animals = getLevel().getEntitiesOfClass(Animal.class, new AABB(getEffectivePos().offset(-RANGE, -RANGE, -RANGE), getEffectivePos().offset(RANGE + 1, RANGE + 1, RANGE + 1)));
			int slowdown = getSlowdownFactor();

			for (Animal animal : animals) {
				if (getMana() < MANA_COST) {
					break;
				}

				if (animal.getAge() == 0 && !animal.isInLove()) {
					for (ItemEntity item : items) {
						int age = ((AccessorItemEntity) item).getAge();
						if (age < 60 + slowdown || !item.isAlive()) {
							continue;
						}

						ItemStack stack = item.getItem();
						if (!stack.isEmpty() && animal.isFood(stack)) {
							stack.shrink(1);

							addMana(-MANA_COST);
							animal.setInLoveTime(1200);
							getLevel().broadcastEntityEvent(animal, (byte) 18);
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
