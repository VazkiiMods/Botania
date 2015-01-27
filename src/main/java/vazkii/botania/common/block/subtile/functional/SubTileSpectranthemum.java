/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under a
 * Creative Commons Attribution-NonCommercial-ShareAlike 3.0 License
 * (http://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB)
 * 
 * File Created @ [Jan 27, 2015, 4:06:58 PM (GMT)]
 */
package vazkii.botania.common.block.subtile.functional;

import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ChunkCoordinates;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.api.subtile.SubTileFunctional;
import vazkii.botania.common.lexicon.LexiconData;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class SubTileSpectranthemum extends SubTileFunctional {

	private static final String TAG_BIND_X = "bindX";
	private static final String TAG_BIND_Y = "bindY";
	private static final String TAG_BIND_Z = "bindZ";

	private static final int COST = 24;
	int bindX, bindY = -1, bindZ;

	@Override
	public void onUpdate() {
		super.onUpdate();

		if(redstoneSignal == 0 && supertile.getWorldObj().blockExists(bindX, bindY, bindZ)) {
			int range = 2;

			int x = supertile.xCoord;
			int y = supertile.yCoord;
			int z = supertile.zCoord;
			
			boolean did = false;
			List<EntityItem> items = supertile.getWorldObj().getEntitiesWithinAABB(EntityItem.class, AxisAlignedBB.getBoundingBox(x - range, y - range, z - range, x + range + 1, y + range, z + range + 1));
			for(EntityItem item : items) {
				if(item.age < 60 || item.isDead)
					continue;

				ItemStack stack = item.getEntityItem();
				if(stack != null) {
					int cost = stack.stackSize * COST;
					if(mana >= cost) {
						spawnExplosionParticles(item);
						item.setPosition(bindX + 0.5, bindY + 1.5, bindZ + 0.5);
						item.motionX = item.motionY = item.motionZ = 0;
						spawnExplosionParticles(item);
						if(!supertile.getWorldObj().isRemote) {
							mana -= cost;
							did = true;
						}
					}
				}
			}
			
			if(did)
				sync();
		}
	}
	
	void spawnExplosionParticles(EntityItem item) {
        for(int i = 0; i < 10; i++) {
        	double m = 0.01;
            double d0 = item.worldObj.rand.nextGaussian() * m;
            double d1 = item.worldObj.rand.nextGaussian() * m;
            double d2 = item.worldObj.rand.nextGaussian() * m;
            double d3 = 10.0D;
            item.worldObj.spawnParticle("explode", item.posX + (double)(item.worldObj.rand.nextFloat() * item.width * 2.0F) - (double)item.width - d0 * d3, item.posY + (double)(item.worldObj.rand.nextFloat() * item.height) - d1 * d3, item.posZ + (double)(item.worldObj.rand.nextFloat() * item.width * 2.0F) - (double)item.width - d2 * d3, d0, d1, d2);
        }
	}
	
	@Override
	public void writeToPacketNBT(NBTTagCompound cmp) {
		super.writeToPacketNBT(cmp);
		cmp.setInteger(TAG_BIND_X, bindX);
		cmp.setInteger(TAG_BIND_Y, bindY);
		cmp.setInteger(TAG_BIND_Z, bindZ);
	}
	
	@Override
	public void readFromPacketNBT(NBTTagCompound cmp) {
		super.readFromPacketNBT(cmp);
		bindX = cmp.getInteger(TAG_BIND_X);
		bindY = cmp.getInteger(TAG_BIND_Y);
		bindZ = cmp.getInteger(TAG_BIND_Z);
	}
	
	@Override
	public boolean acceptsRedstone() {
		return true;
	}

	@Override
	public int getColor() {
		return 0x98BCFF;
	}

	@Override
	public int getMaxMana() {
		return 16000;
	}

	@Override
	public boolean bindTo(EntityPlayer player, ItemStack wand, int x, int y, int z, int side) {
		boolean bound = super.bindTo(player, wand, x, y, z, side);
		if(!bound && (x != bindX || y != bindY || z != bindZ) && (x != supertile.xCoord || y != supertile.yCoord || z != supertile.zCoord)) {
			bindX = x;
			bindY = y;
			bindZ = z;
			sync();

			return true;
		}

		return bound;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public ChunkCoordinates getBinding() {
		return (Minecraft.getMinecraft().thePlayer.isSneaking() && bindY != -1) ? new ChunkCoordinates(bindX, bindY, bindZ) : super.getBinding();
	}
	
	@Override
	public LexiconEntry getEntry() {
		return LexiconData.spectranthemum;
	}

}
