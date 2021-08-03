/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block.subtile.generating;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import vazkii.botania.api.subtile.RadiusDescriptor;
import vazkii.botania.api.subtile.TileEntityGeneratingFlower;
import vazkii.botania.common.block.ModSubtiles;
import vazkii.botania.common.block.mana.BlockSpreader;
import vazkii.botania.common.core.handler.ModSounds;
import vazkii.botania.mixin.AccessorItemEntity;

public class SubTileEndoflame extends TileEntityGeneratingFlower {
	private static final String TAG_BURN_TIME = "burnTime";
	private static final int FUEL_CAP = 32000;
	private static final int RANGE = 3;
	private static final int START_BURN_EVENT = 0;

	private int burnTime = 0;

	public SubTileEndoflame() {
		super(ModSubtiles.ENDOFLAME);
	}

	@Override
	public void tickFlower() {
		super.tickFlower();

		if (burnTime > 0) {
			burnTime--;
		}

		if (getLevel().isClientSide) {
			if (burnTime > 0 && getLevel().random.nextInt(10) == 0) {
				Vec3 offset = getLevel().getBlockState(getEffectivePos()).getOffset(getLevel(), getEffectivePos()).add(0.4, 0.7, 0.4);
				getLevel().addParticle(ParticleTypes.FLAME, getEffectivePos().getX() + offset.x + Math.random() * 0.2, getEffectivePos().getY() + offset.y, getEffectivePos().getZ() + offset.z + Math.random() * 0.2, 0.0D, 0.0D, 0.0D);
			}
			return;
		}

		if (linkedCollector != null) {
			if (burnTime == 0) {
				if (getMana() < getMaxMana()) {
					int slowdown = getSlowdownFactor();

					for (ItemEntity item : getLevel().getEntitiesOfClass(ItemEntity.class, new AABB(getEffectivePos().offset(-RANGE, -RANGE, -RANGE), getEffectivePos().offset(RANGE + 1, RANGE + 1, RANGE + 1)))) {
						int age = ((AccessorItemEntity) item).getAge();
						if (age >= 59 + slowdown && item.isAlive()) {
							ItemStack stack = item.getItem();
							if (stack.isEmpty() || stack.getItem().hasCraftingRemainingItem()) {
								continue;
							}

							int burnTime = getBurnTime(stack);
							if (burnTime > 0 && stack.getCount() > 0) {
								this.burnTime = Math.min(FUEL_CAP, burnTime) / 2;

								stack.shrink(1);
								getLevel().playSound(null, getEffectivePos(), ModSounds.endoflame, SoundSource.BLOCKS, 0.2F, 1F);
								getLevel().blockEvent(getBlockPos(), getBlockState().getBlock(), START_BURN_EVENT, item.getId());
								sync();

								return;
							}
						}
					}
				}
			}
		}
	}

	@Override
	public boolean triggerEvent(int event, int param) {
		if (event == START_BURN_EVENT) {
			Entity e = getLevel().getEntity(param);
			if (e != null) {
				e.level.addParticle(ParticleTypes.LARGE_SMOKE, e.getX(), e.getY() + 0.1, e.getZ(), 0.0D, 0.0D, 0.0D);
				e.level.addParticle(ParticleTypes.FLAME, e.getX(), e.getY(), e.getZ(), 0.0D, 0.0D, 0.0D);
			}
			return true;
		} else {
			return super.triggerEvent(event, param);
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
		return new RadiusDescriptor.Square(getEffectivePos(), RANGE);
	}

	@Override
	public void writeToPacketNBT(CompoundTag cmp) {
		super.writeToPacketNBT(cmp);

		cmp.putInt(TAG_BURN_TIME, burnTime);
	}

	@Override
	public void readFromPacketNBT(CompoundTag cmp) {
		super.readFromPacketNBT(cmp);

		burnTime = cmp.getInt(TAG_BURN_TIME);
	}

	@Override
	public boolean canGeneratePassively() {
		return burnTime > 0;
	}

	@Override
	public int getDelayBetweenPassiveGeneration() {
		return 2;
	}

	private int getBurnTime(ItemStack stack) {
		if (stack.isEmpty() || Block.byItem(stack.getItem()) instanceof BlockSpreader) {
			return 0;
		} else {
			return AbstractFurnaceBlockEntity.getFuel().getOrDefault(stack.getItem(), 0);
		}
	}

}
