/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under a
 * Creative Commons Attribution-NonCommercial-ShareAlike 3.0 License
 * (http://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB)
 * 
 * File Created @ [Feb 15, 2014, 9:47:56 PM (GMT)]
 */
package vazkii.botania.common.block.subtile.generating;

import java.util.List;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.AxisAlignedBB;
import vazkii.botania.common.Botania;
import vazkii.botania.common.block.subtile.SubTileGenerating;

public class SubTileEndoflame extends SubTileGenerating {

	private static final String TAG_BURN_TIME = "burnTime";
	int burnTime = 0;

	@Override
	public void onUpdate() {
		super.onUpdate();

		if(burnTime == 0) {
			if(mana < getMaxMana()) {
				final int range = 3;
				List<EntityItem> items = supertile.worldObj.getEntitiesWithinAABB(EntityItem.class, AxisAlignedBB.getBoundingBox(supertile.xCoord - range, supertile.yCoord - range, supertile.zCoord - range, supertile.xCoord + range, supertile.yCoord + range, supertile.zCoord + range));
				for(EntityItem item : items) {
					if(item.delayBeforeCanPickup == 0) {
						ItemStack stack = item.getEntityItem();
						int burnTime = TileEntityFurnace.getItemBurnTime(stack); 
						if(burnTime > 0 && stack.stackSize > 0) {
							this.burnTime = burnTime / 2;

							if(!supertile.worldObj.isRemote) {
								stack.stackSize--;
								if(stack.stackSize == 0)
									item.setDead();
							}

							break;
						}
					}
				}
			}
		} else {
			Botania.proxy.wispFX(supertile.worldObj, (float) supertile.xCoord + 0.8, (float) supertile.yCoord + 0.5, (float) supertile.zCoord + 0.8, 0.7F, 0.05F, 0.05F, (float) Math.random() / 6, (float) -Math.random() / 60);
			Botania.proxy.wispFX(supertile.worldObj, (float) supertile.xCoord + 0.8, (float) supertile.yCoord + 0.5, (float) supertile.zCoord + 0.2, 0.7F, 0.05F, 0.05F, (float) Math.random() / 6, (float) -Math.random() / 60);
			Botania.proxy.wispFX(supertile.worldObj, (float) supertile.xCoord + 0.2, (float) supertile.yCoord + 0.5, (float) supertile.zCoord + 0.2, 0.7F, 0.05F, 0.05F, (float) Math.random() / 6, (float) -Math.random() / 60);
			Botania.proxy.wispFX(supertile.worldObj, (float) supertile.xCoord + 0.2, (float) supertile.yCoord + 0.5, (float) supertile.zCoord + 0.8, 0.7F, 0.05F, 0.05F, (float) Math.random() / 6, (float) -Math.random() / 60);

			burnTime--;
		}
	}

	@Override
	public int getMaxMana() {
		return 300;
	}

	@Override
	public int getColor() {
		return 0x785000;
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
		return 1;
	}

}
