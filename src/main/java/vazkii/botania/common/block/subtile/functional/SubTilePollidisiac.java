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

import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.api.subtile.RadiusDescriptor;
import vazkii.botania.api.subtile.SubTileFunctional;
import vazkii.botania.common.lexicon.LexiconData;
import vazkii.botania.common.lib.LibObfuscation;

import java.util.List;

public class SubTilePollidisiac extends SubTileFunctional {

	private static final int RANGE = 6;

	@Override
	public void onUpdate() {
		super.onUpdate();

		if(!supertile.getWorld().isRemote) {
			int manaCost = 12;

			List<EntityItem> items = supertile.getWorld().getEntitiesWithinAABB(EntityItem.class, new AxisAlignedBB(supertile.getPos().add(-RANGE, -RANGE, -RANGE), supertile.getPos().add(RANGE + 1, RANGE + 1, RANGE + 1)));
			List<EntityAnimal> animals = supertile.getWorld().getEntitiesWithinAABB(EntityAnimal.class, new AxisAlignedBB(supertile.getPos().add(-RANGE, -RANGE, -RANGE), supertile.getPos().add(RANGE + 1, RANGE + 1, RANGE + 1)));
			int slowdown = getSlowdownFactor();

			for(EntityAnimal animal : animals) {
				if(mana < manaCost)
					break;

				if(animal.getGrowingAge() == 0 && !animal.isInLove()) {
					for(EntityItem item : items) {
						if(item.age < 60 + slowdown || item.isDead)
							continue;

						ItemStack stack = item.getItem();
						if(!stack.isEmpty() && animal.isBreedingItem(stack)) {
							stack.shrink(1);

							mana -= manaCost;

							animal.inLove = 1200;
							supertile.getWorld().setEntityState(animal, (byte)18);
						}
					}
				}
			}
		}
	}

	@Override
	public RadiusDescriptor getRadius() {
		return new RadiusDescriptor.Square(toBlockPos(), RANGE);
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
