/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Jul 26, 2014, 1:42:17 PM (GMT)]
 */
package vazkii.botania.common.block.subtile.generating;

import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.particles.ItemParticleData;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.ServerWorld;
import net.minecraft.world.ServerWorld;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.registries.ObjectHolder;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.api.subtile.RadiusDescriptor;
import vazkii.botania.api.subtile.TileEntityGeneratingFlower;
import vazkii.botania.common.block.ModSubtiles;
import vazkii.botania.common.lexicon.LexiconData;
import vazkii.botania.common.lib.LibMisc;

import java.util.List;

public class SubTileGourmaryllis extends TileEntityGeneratingFlower {
	@ObjectHolder(LibMisc.MOD_ID + ":gourmaryllis")
	public static TileEntityType<SubTileGourmaryllis> TYPE;

	private static final String TAG_COOLDOWN = "cooldown";
	private static final String TAG_DIGESTING_MANA = "digestingMana";
	private static final String TAG_LAST_FOOD = "lastFood";
	private static final String TAG_LAST_FOOD_COUNT = "lastFoodCount";
	private static final int RANGE = 1;

	private int cooldown = 0;
	private int digestingMana = 0;
	private ItemStack lastFood = ItemStack.EMPTY;
	private int lastFoodCount = 0;

	public SubTileGourmaryllis() {
		super(TYPE);
	}

	@Override
	public void tickFlower() {
		super.tickFlower();

		if (getWorld().isRemote)
			return;
		
		if(cooldown > -1)
			cooldown--;
		if(digestingMana != 0) {
			int munchInterval = 2 + (2 * lastFoodCount);
			
			if(cooldown == 0) {
				mana = Math.min(getMaxMana(), mana + digestingMana);
				digestingMana = 0;
				
				float burpPitch = 1 - (lastFoodCount - 1) * 0.05F;
				getWorld().playSound(null, getPos(), SoundEvents.ENTITY_PLAYER_BURP, SoundCategory.BLOCKS, 1, burpPitch);
				sync();
			} else if(cooldown % munchInterval == 0) {
				getWorld().playSound(null, getPos(), SoundEvents.ENTITY_GENERIC_EAT, SoundCategory.BLOCKS, 0.5f, 1);
				
				Vec3d offset = getWorld().getBlockState(getPos()).getOffset(getWorld(), getPos()).add(0.4, 0.6, 0.4);
				
				((ServerWorld) getWorld()).spawnParticle(new ItemParticleData(ParticleTypes.ITEM, lastFood), getPos().getX()+offset.x, getPos().getY()+offset.y, getPos().getZ()+offset.z, 10, 0.1D, 0.1D, 0.1D, 0.03D);
			}
		}

		int slowdown = getSlowdownFactor();

		List<ItemEntity> items = getWorld().getEntitiesWithinAABB(ItemEntity.class, new AxisAlignedBB(getPos().add(-RANGE, -RANGE, -RANGE), getPos().add(RANGE + 1, RANGE + 1, RANGE + 1)));

		for(ItemEntity item : items) {
			ItemStack stack = item.getItem();

			if(!stack.isEmpty() && stack.getItem() instanceof ItemFood && !item.removed && item.age >= slowdown) {
				if(cooldown <= 0) {
					if(ItemHandlerHelper.canItemStacksStack(lastFood, stack)) {
						lastFoodCount++;
					} else {
						lastFood = stack.copy();
						lastFood.setCount(1);
						lastFoodCount = 1;
					}

					int val = Math.min(12, ((ItemFood) stack.getItem()).getHealAmount(stack));
					digestingMana = val * val * 70;
					digestingMana *= 1F / lastFoodCount;
					cooldown = val * 10;
					item.playSound(SoundEvents.ENTITY_GENERIC_EAT, 0.2F, 0.6F);
					sync();
					((ServerWorld) getWorld()).spawnParticle(new ItemParticleData(ParticleTypes.ITEM, stack), item.posX, item.posY, item.posZ, 20, 0.1D, 0.1D, 0.1D, 0.05D);
				}

				item.remove();
			}
		}
	}

	@Override
	public void writeToPacketNBT(CompoundNBT cmp) {
		super.writeToPacketNBT(cmp);
		cmp.putInt(TAG_COOLDOWN, cooldown);
		cmp.putInt(TAG_DIGESTING_MANA, digestingMana);
		cmp.put(TAG_LAST_FOOD, lastFood.write(new CompoundNBT()));
		cmp.putInt(TAG_LAST_FOOD_COUNT, lastFoodCount);
	}

	@Override
	public void readFromPacketNBT(CompoundNBT cmp) {
		super.readFromPacketNBT(cmp);
		cooldown = cmp.getInt(TAG_COOLDOWN);
		digestingMana = cmp.getInt(TAG_DIGESTING_MANA);
		lastFood = ItemStack.read(cmp.getCompound(TAG_LAST_FOOD));
		lastFoodCount = cmp.getInt(TAG_LAST_FOOD_COUNT);
	}

	@Override
	public RadiusDescriptor getRadius() {
		return new RadiusDescriptor.Square(toBlockPos(), RANGE);
	}

	@Override
	public int getMaxMana() {
		return 9000;
	}

	@Override
	public int getColor() {
		return 0xD3D604;
	}

	@Override
	public LexiconEntry getEntry() {
		return LexiconData.gourmaryllis;
	}

}
