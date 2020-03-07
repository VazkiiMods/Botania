/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.client.fx;

import vazkii.botania.common.core.helper.Vector3;

public class FXLightningBoltPoint {

	public final Vector3 point;
	public final Vector3 basepoint;
	public final Vector3 offsetvec;

	public FXLightningBoltPoint(Vector3 basepoint, Vector3 offsetvec) {
		point = basepoint.add(offsetvec);
		this.basepoint = basepoint;
		this.offsetvec = offsetvec;
	}

}
