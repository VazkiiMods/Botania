/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [Aug 21, 2014, 5:44:13 PM (GMT)]
 */
package vazkii.botania.api.mana.spark;

import net.minecraft.item.ItemStack;
import vazkii.botania.api.mana.IManaReceiver;

/**
 * A TileEntity that implements this can have a Spark attached to it.
 * For the Spark to be allowed to have upgrades, it needs to be an IManaPool.
 */
public interface ISparkAttachable extends IManaReceiver {

	/**
	 * Can this block have a Spark attached to it. Note that this will not
	 * unattach the Spark if it's changed later.
	 */
	public boolean canAttachSpark(ItemStack stack);

	/**
	 * Called when the Spark is attached.
	 */
	public void attachSpark(ISparkEntity entity);

	/**
	 * Returns how much space for mana is available in this block, normally the total - the current.
	 * Should NEVER return negative values. Make sure to check against that.
	 */
	public int getAvailableSpaceForMana();

	/**
	 * Gets the Spark that is attached to this block. A common implementation is
	 * to check for Spark entities above:
	 * 
	  	List<ISparkEntity> sparks = worldObj.getEntitiesWithinAABB(ISparkEntity.class, AxisAlignedBB.getBoundingBox(xCoord, yCoord + 1, zCoord, xCoord + 1, yCoord + 2, zCoord + 1));
	 	if(sparks.size() == 1) {
			Entity e = (Entity) sparks.get(0);
			return (ISparkEntity) e;
		}

		return null;
	 */
	public ISparkEntity getAttachedSpark();

	/**
	 * Return true if this Tile no longer requires mana and all Sparks
	 * transferring mana to it should cancel their transfer.
	 */
	public boolean areIncomingTranfersDone();

}
