/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block.subtile.generating;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.common.ForgeHooks;

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

		if (getWorld().isRemote) {
			if (burnTime > 0 && getWorld().rand.nextInt(10) == 0) {
				Vector3d offset = getWorld().getBlockState(getEffectivePos()).getOffset(getWorld(), getEffectivePos()).add(0.4, 0.7, 0.4);
				getWorld().addParticle(ParticleTypes.FLAME, getEffectivePos().getX() + offset.x + Math.random() * 0.2, getEffectivePos().getY() + offset.y, getEffectivePos().getZ() + offset.z + Math.random() * 0.2, 0.0D, 0.0D, 0.0D);
			}
			return;
		}

		if (linkedCollector != null) {
			if (burnTime == 0) {
				if (getMana() < getMaxMana()) {
					int slowdown = getSlowdownFactor();

					for (ItemEntity item : getWorld().getEntitiesWithinAABB(ItemEntity.class, new AxisAlignedBB(getEffectivePos().add(-RANGE, -RANGE, -RANGE), getEffectivePos().add(RANGE + 1, RANGE + 1, RANGE + 1)))) {
						int age = ((AccessorItemEntity) item).getAge();
						if (age >= 59 + slowdown && item.isAlive()) {
							ItemStack stack = item.getItem();
							if (stack.isEmpty() || stack.getItem().hasContainerItem(stack)) {
								continue;
							}

							int burnTime = getBurnTime(stack);
							if (burnTime > 0 && stack.getCount() > 0) {
								this.burnTime = Math.min(FUEL_CAP, burnTime) / 2;

								stack.shrink(1);
								getWorld().playSound(null, getEffectivePos(), ModSounds.endoflame, SoundCategory.BLOCKS, 0.2F, 1F);
								getWorld().addBlockEvent(getPos(), getBlockState().getBlock(), START_BURN_EVENT, item.getEntityId());
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
	public boolean receiveClientEvent(int event, int param) {
		if (event == START_BURN_EVENT) {
			Entity e = getWorld().getEntityByID(param);
			if (e != null) {
				e.world.addParticle(ParticleTypes.LARGE_SMOKE, e.getPosX(), e.getPosY() + 0.1, e.getPosZ(), 0.0D, 0.0D, 0.0D);
				e.world.addParticle(ParticleTypes.FLAME, e.getPosX(), e.getPosY(), e.getPosZ(), 0.0D, 0.0D, 0.0D);
			}
			return true;
		} else {
			return super.receiveClientEvent(event, param);
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
	public void writeToPacketNBT(CompoundNBT cmp) {
		super.writeToPacketNBT(cmp);

		cmp.putInt(TAG_BURN_TIME, burnTime);
	}

	@Override
	public void readFromPacketNBT(CompoundNBT cmp) {
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
		if (stack.isEmpty() || Block.getBlockFromItem(stack.getItem()) instanceof BlockSpreader) {
			return 0;
		} else {
			return ForgeHooks.getBurnTime(stack);
		}
	}

}
