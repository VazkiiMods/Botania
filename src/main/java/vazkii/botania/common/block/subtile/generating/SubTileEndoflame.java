/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Feb 15, 2014, 9:47:56 PM (GMT)]
 */
package vazkii.botania.common.block.subtile.generating;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.api.subtile.RadiusDescriptor;
import vazkii.botania.api.subtile.SubTileGenerating;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.core.handler.MethodHandles;
import vazkii.botania.common.core.handler.ModSounds;
import vazkii.botania.common.lexicon.LexiconData;

import java.util.List;

public class SubTileEndoflame extends SubTileGenerating {

	private static final String TAG_BURN_TIME = "burnTime";
	private static final int FUEL_CAP = 32000;
	private static final int RANGE = 3;

	int burnTime = 0;

	@Override
	public void onUpdate() {
		super.onUpdate();

		if(linkedCollector != null) {
			if(burnTime == 0) {
				if(mana < getMaxMana()) {
					boolean didSomething = false;

					int slowdown = getSlowdownFactor();

					List<EntityItem> items = supertile.getWorld().getEntitiesWithinAABB(EntityItem.class, new AxisAlignedBB(supertile.getPos().add(-RANGE, -RANGE, -RANGE), supertile.getPos().add(RANGE + 1, RANGE + 1, RANGE + 1)));
					for(EntityItem item : items) {
						int age;
						try {
							age = (int) MethodHandles.itemAge_getter.invokeExact(item);
						} catch (Throwable t) {
							continue;
						}

						if(age >= 59 + slowdown && !item.isDead) {
							ItemStack stack = item.getItem();
							if(stack.isEmpty() || stack.getItem().hasContainerItem(stack))
								continue;

							int burnTime = stack.getItem() == Item.getItemFromBlock(ModBlocks.spreader) ? 0 : TileEntityFurnace.getItemBurnTime(stack);
							if(burnTime > 0 && stack.getCount() > 0) {
								this.burnTime = Math.min(FUEL_CAP, burnTime) / 2;

								if(!supertile.getWorld().isRemote) {
									stack.shrink(1);
									supertile.getWorld().playSound(null, supertile.getPos(), ModSounds.endoflame, SoundCategory.BLOCKS, 0.2F, 1F);

									didSomething = true;
								} else {
									item.world.spawnParticle(EnumParticleTypes.SMOKE_LARGE, item.posX, item.posY + 0.1, item.posZ, 0.0D, 0.0D, 0.0D);
									item.world.spawnParticle(EnumParticleTypes.FLAME, item.posX, item.posY, item.posZ, 0.0D, 0.0D, 0.0D);
								}


								break;
							}
						}
					}

					if(didSomething)
						sync();
				}
			} else {
				if(supertile.getWorld().rand.nextInt(10) == 0)
					supertile.getWorld().spawnParticle(EnumParticleTypes.FLAME, supertile.getPos().getX() + 0.4 + Math.random() * 0.2, supertile.getPos().getY() + 0.65, supertile.getPos().getZ() + 0.4 + Math.random() * 0.2, 0.0D, 0.0D, 0.0D);

				burnTime--;
			}
		}
	}

	@Override
	public int getMaxMana() {
		return 300;
	}

	@Override
	public int getValueForPassiveGeneration() {
		return 3;
	}

	@Override
	public int getColor() {
		return 0x785000;
	}

	@Override
	public RadiusDescriptor getRadius() {
		return new RadiusDescriptor.Square(toBlockPos(), RANGE);
	}

	@Override
	public LexiconEntry getEntry() {
		return LexiconData.endoflame;
	}

	@Override
	public void writeToPacketNBT(NBTTagCompound cmp) {
		super.writeToPacketNBT(cmp);

		cmp.setInteger(TAG_BURN_TIME, burnTime);
	}

	@Override
	public void readFromPacketNBT(NBTTagCompound cmp) {
		super.readFromPacketNBT(cmp);

		burnTime = cmp.getInteger(TAG_BURN_TIME);
	}

	@Override
	public boolean canGeneratePassively() {
		return burnTime > 0;
	}

	@Override
	public int getDelayBetweenPassiveGeneration() {
		return 2;
	}

}
