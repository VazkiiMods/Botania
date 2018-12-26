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

import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.api.subtile.RadiusDescriptor;
import vazkii.botania.api.subtile.SubTileGenerating;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.core.handler.ModSounds;
import vazkii.botania.common.lexicon.LexiconData;

public class SubTileEndoflame extends SubTileGenerating {
	private static final String TAG_BURN_TIME = "burnTime";
	private static final int FUEL_CAP = 32000;
	private static final int RANGE = 3;
	private static final int START_BURN_EVENT = 0;

	private int burnTime = 0;

	@Override
	public void onUpdate() {
		super.onUpdate();

		if(burnTime > 0)
			burnTime--;

		if(getWorld().isRemote) {
			if(burnTime > 0 && supertile.getWorld().rand.nextInt(10) == 0) {
				Vec3d offset = getWorld().getBlockState(getPos()).getOffset(getWorld(), getPos()).add(0.4, 0.7, 0.4);
				supertile.getWorld().spawnParticle(EnumParticleTypes.FLAME, supertile.getPos().getX() + offset.x + Math.random() * 0.2, supertile.getPos().getY() + offset.y, supertile.getPos().getZ() + offset.z + Math.random() * 0.2, 0.0D, 0.0D, 0.0D);
			}
			return;
		}

		if(linkedCollector != null) {
			if(burnTime == 0) {
				if(mana < getMaxMana()) {
					int slowdown = getSlowdownFactor();

					for(EntityItem item : supertile.getWorld().getEntitiesWithinAABB(EntityItem.class, new AxisAlignedBB(supertile.getPos().add(-RANGE, -RANGE, -RANGE), supertile.getPos().add(RANGE + 1, RANGE + 1, RANGE + 1)))) {
						if(item.age >= 59 + slowdown && !item.isDead) {
							ItemStack stack = item.getItem();
							if(stack.isEmpty() || stack.getItem().hasContainerItem(stack))
								continue;

							int burnTime = stack.getItem() == Item.getItemFromBlock(ModBlocks.spreader) ? 0 : TileEntityFurnace.getItemBurnTime(stack);
							if(burnTime > 0 && stack.getCount() > 0) {
								this.burnTime = Math.min(FUEL_CAP, burnTime) / 2;

								stack.shrink(1);
								supertile.getWorld().playSound(null, supertile.getPos(), ModSounds.endoflame, SoundCategory.BLOCKS, 0.2F, 1F);
								getWorld().addBlockEvent(getPos(), getWorld().getBlockState(getPos()).getBlock(), START_BURN_EVENT, item.getEntityId());
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
		if(event == START_BURN_EVENT) {
			Entity e = getWorld().getEntityByID(param);
			if(e != null) {
				e.world.spawnParticle(EnumParticleTypes.SMOKE_LARGE, e.posX, e.posY + 0.1, e.posZ, 0.0D, 0.0D, 0.0D);
				e.world.spawnParticle(EnumParticleTypes.FLAME, e.posX, e.posY, e.posZ, 0.0D, 0.0D, 0.0D);
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
