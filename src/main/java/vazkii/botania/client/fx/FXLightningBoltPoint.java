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
