/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under a
 * Creative Commons Attribution-NonCommercial-ShareAlike 3.0 License
 * (http://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB)
 * 
 * File Created @ [Aug 21, 2014, 5:43:44 PM (GMT)]
 */
package vazkii.botania.common.entity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import vazkii.botania.api.mana.spark.ISparkAttachable;
import vazkii.botania.api.mana.spark.ISparkEntity;
import vazkii.botania.common.Botania;
import vazkii.botania.common.core.helper.Vector3;
import vazkii.botania.common.item.ModItems;

public class EntitySpark extends Entity implements ISparkEntity {

	private static final int TRANSFER_RATE = 1000;
	
	public EntitySpark(World world) {
		super(world);
	}

	@Override
	protected void entityInit() {
		dataWatcher.addObject(28, 0);
		dataWatcher.addObject(29, "");

		dataWatcher.setObjectWatched(28);
		dataWatcher.setObjectWatched(29);
	}

	@Override
	public void onUpdate() {
		super.onUpdate();
		
		ISparkAttachable tile = getAttachedTile();
		if(tile == null) {
			setDead();
			return;
		}

		if(getUpgrade() != 0) {

		}

		Collection<ISparkEntity> sparks = getTransfers();
		if(!sparks.isEmpty()) {
			int manaTotal = Math.min(TRANSFER_RATE * sparks.size(), tile.getCurrentMana());
			int manaForEach = manaTotal / sparks.size();
			for(ISparkEntity spark : sparks) {
				ISparkAttachable attached = spark.getAttachedTile();
				tile.recieveMana(-manaForEach);
				attached.recieveMana(manaForEach);
				
				Vector3 thisVec = Vector3.fromEntityCenter(this).add(0, 1, 0);
				Vector3 receiverVec = Vector3.fromEntityCenter((Entity) spark).add(0, 1, 0);
				Vector3 motion = receiverVec.copy().sub(thisVec);
				motion.multiply(0.04125F);
				Botania.proxy.wispFX(worldObj, posX, posY, posZ, 0F, 0F, 1F, 0.25F, (float) motion.x, (float) motion.y, (float) motion.z);
			}
		}
	}

	@Override
	protected void readEntityFromNBT(NBTTagCompound cmp) {
	}

	@Override
	protected void writeEntityToNBT(NBTTagCompound cmp) {
	}

	@Override
	public void setDead() {
		super.setDead();
		if(!worldObj.isRemote) {
			entityDropItem(new ItemStack(ModItems.spark), 0F);
			// Upgrade Drop here
		}
	}

	@Override
	public ISparkAttachable getAttachedTile() {
		int x = (int) MathHelper.floor_double(posX);
		int y = (int) MathHelper.floor_double(posY) - 1;
		int z = (int) MathHelper.floor_double(posZ);
		TileEntity tile = worldObj.getTileEntity(x, y, z);
		if(tile != null && tile instanceof ISparkAttachable)
			return (ISparkAttachable) tile;

		return null;
	}


	@Override
	public Collection<ISparkEntity> getTransfers() {
		Collection<ISparkEntity> entities = new ArrayList();
		String transfers = dataWatcher.getWatchableObjectString(29);
		String[] tokens = transfers.split(";");
		List<String> removals = new ArrayList();

		for(String s : tokens) {
			if(s.isEmpty())
				continue;
			
			int id = Integer.parseInt(s);
			boolean added = false;
			Entity e = worldObj.getEntityByID(id);
			if(e != null && e instanceof ISparkEntity) {
				ISparkEntity spark = (ISparkEntity) e;
				if(!spark.areIncomingTransfersDone() && spark.getAttachedTile() != null && !spark.getAttachedTile().isFull()) {
					entities.add((ISparkEntity) e);
					added = true;
				}
			}

			if(!added)
				removals.add(s);
		}

		if(!removals.isEmpty()) {
			String newTranfers = "";
			for(String s : tokens)
				if(!removals.contains(s))
					newTranfers = newTranfers + (newTranfers.isEmpty() ? "" : ";") + s;
			dataWatcher.updateObject(29, newTranfers);

			removals.clear();
		}

		return entities;
	}

	@Override
	public void registerTransfer(ISparkEntity entity) {
		if(worldObj.isRemote || getTransfers().contains(entity))
			return;
		
		String transfers = dataWatcher.getWatchableObjectString(29);
		dataWatcher.updateObject(29, transfers + (transfers.isEmpty() ? "" : ";") + ((Entity) entity).getEntityId()); 
	}

	public int getUpgrade() {
		return dataWatcher.getWatchableObjectInt(28);
	}

	public void setUpgrade(int upgrade) {
		dataWatcher.updateObject(28, upgrade);
	}

	@Override
	public boolean areIncomingTransfersDone() {
		ISparkAttachable tile = getAttachedTile();
		return tile != null && tile.areIncomingTranfersDone();
	}

}
