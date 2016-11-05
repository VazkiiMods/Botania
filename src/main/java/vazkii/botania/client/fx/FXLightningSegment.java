package vazkii.botania.client.fx;

import vazkii.botania.common.core.helper.Vector3;

public class FXLightningSegment {

	public final FXLightningBoltPoint startPoint;
	public final FXLightningBoltPoint endPoint;

	public Vector3 diff;

	public FXLightningSegment prev;
	public FXLightningSegment next;

	public Vector3 nextDiff;
	public Vector3 prevDiff;

	public float sinPrev;
	public float sinNext;
	public final float light;

	public final int segmentNo;
	public final int splitNo;

	public FXLightningSegment(FXLightningBoltPoint start, FXLightningBoltPoint end, float light, int segmentnumber, int splitnumber) {
		startPoint = start;
		endPoint = end;
		this.light = light;
		segmentNo = segmentnumber;
		splitNo = splitnumber;

		calcDiff();
	}

	public FXLightningSegment(Vector3 start, Vector3 end) {
		this(new FXLightningBoltPoint(start, new Vector3(0, 0, 0)), new FXLightningBoltPoint(end, new Vector3(0, 0, 0)), 1, 0, 0);
	}

	public void calcDiff() {
		diff = endPoint.point.subtract(startPoint.point);
	}

	public void calcEndDiffs() {
		if(prev != null) {
			Vector3 prevdiffnorm = prev.diff.normalize();
			Vector3 thisdiffnorm = diff.normalize();

			prevDiff = thisdiffnorm.add(prevdiffnorm).normalize();
			sinPrev = (float) Math.sin(thisdiffnorm.angle(prevdiffnorm.multiply(-1)) / 2);
		} else {
			prevDiff = diff.normalize();
			sinPrev = 1;
		}

		if(next != null) {
			Vector3 nextdiffnorm = next.diff.normalize();
			Vector3 thisdiffnorm = diff.normalize();

			nextDiff = thisdiffnorm.add(nextdiffnorm).normalize();
			sinNext = (float) Math.sin(thisdiffnorm.angle(nextdiffnorm.multiply(-1)) / 2);
		} else {
			nextDiff = diff.normalize();
			sinNext = 1;
		}
	}

	@Override
	public String toString() {
		return startPoint.point.toString() + " " + endPoint.point.toString();
	}
}
