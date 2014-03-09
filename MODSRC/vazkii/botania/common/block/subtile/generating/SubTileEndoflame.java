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
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.api.subtile.SubTileGenerating;
import vazkii.botania.common.Botania;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.lexicon.LexiconData;
import cpw.mods.fml.common.network.PacketDispatcher;

public class SubTileEndoflame extends SubTileGenerating {

	private static final String TAG_BURN_TIME = "burnTime";
	int burnTime = 0;

	@Override
	public void onUpdate() {
		super.onUpdate();

		if(linkedCollector != null) {
			if(burnTime == 0) {
				if(mana < getMaxMana() && !supertile.worldObj.isRemote) {
					final int range = 3;
					boolean didSomething = false;

					List<EntityItem> items = supertile.worldObj.getEntitiesWithinAABB(EntityItem.class, AxisAlignedBB.getBoundingBox(supertile.xCoord - range, supertile.yCoord - range, supertile.zCoord - range, supertile.xCoord + range, supertile.yCoord + range, supertile.zCoord + range));
					for(EntityItem item : items) {
						if(item.age >= 60 && !item.isDead) {
							ItemStack stack = item.getEntityItem();
							if(stack.getItem().hasContainerItem())
								continue;
							
							int burnTime = stack == null || stack.itemID == ModBlocks.spreader.blockID ? 0 : TileEntityFurnace.getItemBurnTime(stack);
							if(burnTime > 0 && stack.stackSize > 0) {
								this.burnTime = burnTime / 2;

								stack.stackSize--;

								if(stack.stackSize == 0)
									item.setDead();

								didSomething = true;

								break;
							}
						}
					}

					if(didSomething)
						PacketDispatcher.sendPacketToAllInDimension(supertile.getDescriptionPacket(), supertile.worldObj.provider.dimensionId);
				}
			} else {
				if(supertile.worldObj.rand.nextInt(8) == 0)
					Botania.proxy.wispFX(supertile.worldObj, supertile.xCoord + 0.55 + Math.random() * 0.2 - 0.1, supertile.yCoord + 0.55 + Math.random() * 0.2 - 0.1, supertile.zCoord + 0.5, 0.7F, 0.05F, 0.05F, (float) Math.random() / 6, (float) -Math.random() / 60);

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
		return 2;
	}

	@Override
	public int getColor() {
		return 0x785000;
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
		return 1;
	}

}
