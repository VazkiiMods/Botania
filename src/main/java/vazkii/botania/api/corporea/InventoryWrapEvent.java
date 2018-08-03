/**
 * This class was created by <quaternary>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 */
package vazkii.botania.api.corporea;

import net.minecraftforge.fml.common.eventhandler.Event;

/** 
 * InventoryWrapEvent is fired when the corporea system attempts to wrap an inventory for use.
 * */
public class InventoryWrapEvent extends Event {
	public IWrappedInventory wrapped = null;
	final ICorporeaSpark spark;
	
	public InventoryWrapEvent(ICorporeaSpark spark) {
		this.spark = spark;
	}
	
	public ICorporeaSpark getSpark() {
		return spark;
	}
	
	public InvWithLocation getInvWithLocation() {
		return spark.getSparkInventory();
	}
}
