/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under a
 * Creative Commons Attribution-NonCommercial-ShareAlike 3.0 License
 * (http://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB)
 * 
 * File Created @ [Jul 26, 2014, 1:42:17 PM (GMT)]
 */
package vazkii.botania.common.block.subtile.generating;

import java.util.List;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.api.subtile.SubTileGenerating;
import vazkii.botania.common.lexicon.LexiconData;

public class SubTileGourmaryllis extends SubTileGenerating {

	private static final String TAG_COOLDOWN = "cooldown";
	int cooldown = 0;
	int storedMana = 0;

	@Override
	public void onUpdate() {
		super.onUpdate();

		if(cooldown > 0)
			cooldown--;
		if(cooldown == 0 && !supertile.getWorldObj().isRemote) {
			mana = Math.min(getMaxMana(), mana + storedMana);
			storedMana = 0;
			sync();
		}

		if(!supertile.getWorldObj().isRemote) {
			int range = 1;
			List<EntityItem> items = supertile.getWorldObj().getEntitiesWithinAABB(EntityItem.class, AxisAlignedBB.getBoundingBox(supertile.xCoord - range, supertile.yCoord - range, supertile.zCoord - range, supertile.xCoord + range + 1, supertile.yCoord + range + 1, supertile.zCoord + range + 1));
			for(EntityItem item : items) {
				ItemStack stack = item.getEntityItem();
				if(stack != null && stack.getItem() instanceof ItemFood) {
					if(cooldown == 0) {
						int val = ((ItemFood) stack.getItem()).func_150905_g(stack);
						storedMana = val * val * 18;
						cooldown = val * 10;
						sync();
					}

					item.setDead();
				}
			}
		}
	}

	@Override
	public void writeToPacketNBT(NBTTagCompound cmp) {
		super.writeToPacketNBT(cmp);
		cmp.setInteger(TAG_COOLDOWN, cooldown);
		cmp.setInteger(TAG_COOLDOWN, cooldown);
	}

	@Override
	public void readFromPacketNBT(NBTTagCompound cmp) {
		super.readFromPacketNBT(cmp);
		cooldown = cmp.getInteger(TAG_COOLDOWN);
	}

	@Override
	public int getMaxMana() {
		return 2400;
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
