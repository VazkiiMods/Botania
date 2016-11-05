/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Sep 28, 2015, 11:25:48 AM (GMT)]
 */
package vazkii.botania.api.corporea;

/**
 * A TileEntity that implements this be called by other TileEntities
 * to allow for it to do any request at any time. This is used by the
 * Corporea Retainer for example.
 */
public interface ICorporeaRequestor {

	/*
	 * Executes the passed in request.
	 */
	public void doCorporeaRequest(Object request, int count, ICorporeaSpark spark);

}
