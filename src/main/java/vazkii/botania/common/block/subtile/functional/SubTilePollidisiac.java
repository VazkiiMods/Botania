/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [May 15, 2014, 5:56:47 PM (GMT)]
 */
package vazkii.botania.common.block.subtile.functional;

import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.registries.ObjectHolder;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.api.subtile.RadiusDescriptor;
import vazkii.botania.api.subtile.TileEntityFunctionalFlower;
import vazkii.botania.common.lexicon.LexiconData;
import vazkii.botania.common.lib.LibMisc;

import java.util.List;

public class SubTilePollidisiac extends TileEntityFunctionalFlower {
	@ObjectHolder(LibMisc.MOD_ID + ":pollidisiac")
	public static TileEntityType<SubTilePollidisiac> TYPE;

	private static final int RANGE = 6;

	public SubTilePollidisiac() {
		super(TYPE);
	}

	@Override
	public void tickFlower() {
		super.tickFlower();

		if(!getWorld().isRemote) {
			int manaCost = 12;

			List<ItemEntity> items = getWorld().getEntitiesWithinAABB(ItemEntity.class, new AxisAlignedBB(getPos().add(-RANGE, -RANGE, -RANGE), getPos().add(RANGE + 1, RANGE + 1, RANGE + 1)));
			List<AnimalEntity> animals = getWorld().getEntitiesWithinAABB(AnimalEntity.class, new AxisAlignedBB(getPos().add(-RANGE, -RANGE, -RANGE), getPos().add(RANGE + 1, RANGE + 1, RANGE + 1)));
			int slowdown = getSlowdownFactor();

			for(AnimalEntity animal : animals) {
				if(mana < manaCost)
					break;

				if(animal.getGrowingAge() == 0 && !animal.isInLove()) {
					for(ItemEntity item : items) {
						if(item.age < 60 + slowdown || !item.isAlive())
							continue;

						ItemStack stack = item.getItem();
						if(!stack.isEmpty() && animal.isBreedingItem(stack)) {
							stack.shrink(1);

							mana -= manaCost;

							animal.setInLove(1200);
							getWorld().setEntityState(animal, (byte)18);
						}
					}
				}
			}
		}
	}

	@Override
	public RadiusDescriptor getRadius() {
        return new RadiusDescriptor.Square(getPos(), RANGE);
	}

	@Override
	public int getMaxMana() {
		return 120;
	}

	@Override
	public int getColor() {
		return 0xCF4919;
	}

	@Override
	public LexiconEntry getEntry() {
		return LexiconData.pollidisiac;
	}
}
