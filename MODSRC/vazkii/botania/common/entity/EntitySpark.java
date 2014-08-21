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

import java.util.Collection;

import vazkii.botania.api.mana.spark.ISparkAttachable;
import vazkii.botania.api.mana.spark.ISparkEntity;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class EntitySpark extends Entity implements ISparkEntity {

	public EntitySpark(World world) {
		super(world);
	}

	@Override
	protected void entityInit() {
		//etSize(0F, 0F);
	}

	@Override
	protected void readEntityFromNBT(NBTTagCompound cmp) {
	}

	@Override
	protected void writeEntityToNBT(NBTTagCompound cmp) {
	}

	@Override
	public ISparkAttachable getAttachedTile() {
		return null;
	}

	@Override
	public Collection<ISparkEntity> getSparkTransferingTo() {
		return null;
	}

	@Override
	public void registerSparkTranfer(ISparkEntity spark) {
	}

}
