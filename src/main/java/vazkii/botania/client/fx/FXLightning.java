package vazkii.botania.client.fx;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.IParticleRenderType;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import vazkii.botania.client.core.handler.LightningHandler;
import vazkii.botania.common.core.helper.Vector3;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

// Originally taken with permission from WRCBE - heavily modified
public class FXLightning extends Particle {

	private static final int fadetime = 20;
	private final Map<Integer, Integer> splitParents = new HashMap<>();
	private final double length;
	private final Random rand;
	private final int colorOuter;
	private final int colorInner;

	private List<FXLightningSegment> segments = new ArrayList<>();
	private int segmentCount = 1;
	private int splitCount;
	private float speed = 1.5F;

	public FXLightning(World world, Vector3 sourcevec, Vector3 targetvec, float ticksPerMeter, long seed, int colorOuter, int colorInner) {
		super(world, sourcevec.x, sourcevec.y, sourcevec.z);
		rand = new Random(seed);
		speed = ticksPerMeter;
		this.colorOuter = colorOuter;
		this.colorInner = colorInner;
		length = targetvec.subtract(sourcevec).mag();
		maxAge = fadetime + rand.nextInt(fadetime) - fadetime / 2;
		age = -(int) (length * speed);

		segments.add(new FXLightningSegment(sourcevec, targetvec));

		fractal(2, length / 1.5, 0.7F, 0.7F, 45);
		fractal(2, length / 4, 0.5F, 0.8F, 50);
		fractal(2, length / 15, 0.5F, 0.9F, 55);
		fractal(2, length / 30, 0.5F, 1.0F, 60);
		fractal(2, length / 60, 0, 0, 0);
		fractal(2, length / 100, 0, 0, 0);
		fractal(2, length / 400, 0, 0, 0);

		calculateCollisionAndDiffs();

		segments.sort((o1, o2) -> Float.compare(o2.light, o1.light));
	}

	@Override
	public void renderParticle(BufferBuilder buffer, ActiveRenderInfo info, float partialTicks, float rotationX, float rotationZ, float rotationYZ, float rotationXY, float rotationXZ) {
		LightningHandler.queuedLightningBolts.offer(this);
	}

	@Nonnull
	@Override
	public IParticleRenderType getRenderType() {
		return IParticleRenderType.NO_RENDER;
	}

	public void renderBolt(int pass, boolean inner) {
		BufferBuilder wr = Tessellator.getInstance().getBuffer();

		float boltAge = age < 0 ? 0 : (float) age / (float) maxAge;
		float mainAlpha;
		if(pass == 0)
			mainAlpha = (1 - boltAge) * 0.4F;
		else mainAlpha = 1 - boltAge * 0.5F;

		int expandTime = (int) (length * speed);

		int renderstart = (int) ((expandTime / 2 - maxAge + age) / (float) (expandTime / 2) * segmentCount);
		int renderend = (int) ((age + expandTime) / (float) expandTime * segmentCount);

		for(FXLightningSegment rendersegment : segments) {
			if(rendersegment.segmentNo < renderstart || rendersegment.segmentNo > renderend)
				continue;

			Vector3 playerVec = getRelativeViewVector(rendersegment.startPoint.point).multiply(-1);

			double width = 0.025F * (playerVec.mag() / 5 + 1) * (1 + rendersegment.light) * 0.5F;

			Vector3 diff1 = playerVec.crossProduct(rendersegment.prevDiff).normalize().multiply(width / rendersegment.sinPrev);
			Vector3 diff2 = playerVec.crossProduct(rendersegment.nextDiff).normalize().multiply(width / rendersegment.sinNext);

			Vector3 startvec = rendersegment.startPoint.point;
			Vector3 endvec = rendersegment.endPoint.point;

			int color = inner ? colorInner : colorOuter;
			int r = (color & 0xFF0000) >> 16;
		int g = (color & 0xFF00) >> 8;
		int b = color & 0xFF;
		int a = (int) (mainAlpha * rendersegment.light * 0xFF);

		wr.pos(endvec.x - diff2.x, endvec.y - diff2.y, endvec.z - diff2.z).tex(0.5, 0).lightmap(0xF0, 0xF0).color(r, g, b, a).endVertex();
		wr.pos(startvec.x - diff1.x, startvec.y - diff1.y, startvec.z - diff1.z).tex(0.5, 0).lightmap(0xF0, 0xF0).color(r, g, b, a).endVertex();
		wr.pos(startvec.x + diff1.x, startvec.y + diff1.y, startvec.z + diff1.z).tex(0.5, 1).lightmap(0xF0, 0xF0).color(r, g, b, a).endVertex();
		wr.pos(endvec.x + diff2.x, endvec.y + diff2.y, endvec.z + diff2.z).tex(0.5, 1).lightmap(0xF0, 0xF0).color(r, g, b, a).endVertex();

		if(rendersegment.next == null) {
			Vector3 roundend = rendersegment.endPoint.point.add(rendersegment.diff.normalize().multiply(width));

			wr.pos(roundend.x - diff2.x, roundend.y - diff2.y, roundend.z - diff2.z).tex(0, 0).lightmap(0xF0, 0xF0).color(r, g, b, a).endVertex();
			wr.pos(endvec.x - diff2.x, endvec.y - diff2.y, endvec.z - diff2.z).tex(0.5, 0).lightmap(0xF0, 0xF0).color(r, g, b, a).endVertex();
			wr.pos(endvec.x + diff2.x, endvec.y + diff2.y, endvec.z + diff2.z).tex(0.5, 1).lightmap(0xF0, 0xF0).color(r, g, b, a).endVertex();
			wr.pos(roundend.x + diff2.x, roundend.y + diff2.y, roundend.z + diff2.z).tex(0, 1).lightmap(0xF0, 0xF0).color(r, g, b, a).endVertex();
		}

		if(rendersegment.prev == null) {
			Vector3 roundend = rendersegment.startPoint.point.subtract(rendersegment.diff.normalize().multiply(width));

			wr.pos(startvec.x - diff1.x, startvec.y - diff1.y, startvec.z - diff1.z).tex(0.5, 0).lightmap(0xF0, 0xF0).color(r, g, b, a).endVertex();
			wr.pos(roundend.x - diff1.x, roundend.y - diff1.y, roundend.z - diff1.z).tex(0, 0).lightmap(0xF0, 0xF0).color(r, g, b, a).endVertex();
			wr.pos(roundend.x + diff1.x, roundend.y + diff1.y, roundend.z + diff1.z).tex(0, 1).lightmap(0xF0, 0xF0).color(r, g, b, a).endVertex();
			wr.pos(startvec.x + diff1.x, startvec.y + diff1.y, startvec.z + diff1.z).tex(0.5, 1).lightmap(0xF0, 0xF0).color(r, g, b, a).endVertex();
		}
		}
	}

	private void fractal(int splits, double amount, double splitChance, double splitLength, double splitAngle) {
		List<FXLightningSegment> oldSegments = segments;
		segments = new ArrayList<>();

		FXLightningSegment prev;

		for(FXLightningSegment segment : oldSegments) {
			prev = segment.prev;

			Vector3 subsegment = segment.diff.multiply(1F / splits);

			FXLightningBoltPoint[] newpoints = new FXLightningBoltPoint[splits + 1];

			Vector3 startpoint = segment.startPoint.point;
			newpoints[0] = segment.startPoint;
			newpoints[splits] = segment.endPoint;

			for(int i = 1; i < splits; i++) {
				Vector3 randoff = segment.diff.perpendicular().normalize().rotate(rand.nextFloat() * 360, segment.diff);
				randoff = randoff.multiply((rand.nextFloat() - 0.5F) * amount * 2);

				Vector3 basepoint = startpoint.add(subsegment.multiply(i));

				newpoints[i] = new FXLightningBoltPoint(basepoint, randoff);
			}

			for(int i = 0; i < splits; i++) {
				FXLightningSegment next = new FXLightningSegment(newpoints[i], newpoints[i + 1], segment.light, segment.segmentNo * splits + i, segment.splitNo);
				next.prev = prev;
				if (prev != null)
					prev.next = next;

				if(i != 0 && rand.nextFloat() < splitChance) {
					Vector3 splitrot = next.diff.xCrossProduct().rotate(rand.nextFloat() * 360, next.diff);
					Vector3 diff = next.diff.rotate((rand.nextFloat() * 0.66F + 0.33F) * splitAngle, splitrot).multiply(splitLength);

					splitCount++;
					splitParents.put(splitCount, next.splitNo);

					FXLightningSegment split = new FXLightningSegment(newpoints[i], new FXLightningBoltPoint(newpoints[i + 1].basepoint, newpoints[i + 1].offsetvec.add(diff)), segment.light / 2F, next.segmentNo, splitCount);
					split.prev = prev;

					segments.add(split);
				}

				prev = next;
				segments.add(next);
			}

			if(segment.next != null)
				segment.next.prev = prev;
		}

		segmentCount *= splits;
	}

	private float rayTraceResistance(Vector3 start, Vector3 end, float prevresistance) {
		// don't have an entity to pass, just use the render view
		Entity viewer = Minecraft.getInstance().renderViewEntity;
		RayTraceContext ctx = new RayTraceContext(start.toVec3D(), end.toVec3D(), RayTraceContext.BlockMode.OUTLINE, RayTraceContext.FluidMode.NONE, viewer);
		RayTraceResult mop = world.rayTraceBlocks(ctx);

		if(mop.getType() == RayTraceResult.Type.BLOCK) {
			BlockPos pos = ((BlockRayTraceResult) mop).getPos();
			Block block = world.getBlockState(pos).getBlock();

			if(world.isAirBlock(pos))
				return prevresistance;

			return prevresistance + block.getExplosionResistance() + 0.3F;
		} else return prevresistance;
	}

	private void calculateCollisionAndDiffs() {
		Map<Integer, Integer> lastactivesegment = new HashMap<>();

		segments.sort((o1, o2) -> {
			int comp = Integer.compare(o1.splitNo, o2.splitNo);
			if (comp == 0)
				return Integer.compare(o1.segmentNo, o2.segmentNo);
			else return comp;
		});

		int lastSplitCalc = 0;
		int lastActiveSegment = 0;// unterminated
		float splitResistance = 0;

		for(FXLightningSegment segment : segments) {
			if(segment.splitNo > lastSplitCalc) {
				lastactivesegment.put(lastSplitCalc, lastActiveSegment);
				lastSplitCalc = segment.splitNo;
				lastActiveSegment = lastactivesegment.get(splitParents.get(segment.splitNo));
				splitResistance = lastActiveSegment < segment.segmentNo ? 50 : 0;
			}

			if(splitResistance >= 40 * segment.light)
				continue;

			splitResistance = rayTraceResistance(segment.startPoint.point, segment.endPoint.point, splitResistance);
			lastActiveSegment = segment.segmentNo;
		}
		lastactivesegment.put(lastSplitCalc, lastActiveSegment);

		lastSplitCalc = 0;
		lastActiveSegment = lastactivesegment.get(0);
		for(Iterator<FXLightningSegment> iterator = segments.iterator(); iterator.hasNext();) {
			FXLightningSegment segment = iterator.next();
			if(lastSplitCalc != segment.splitNo) {
				lastSplitCalc = segment.splitNo;
				lastActiveSegment = lastactivesegment.get(segment.splitNo);
			}

			if(segment.segmentNo > lastActiveSegment)
				iterator.remove();
			segment.calcEndDiffs();
		}
	}

	private static Vector3 getRelativeViewVector(Vector3 pos) {
		Entity renderEntity = Minecraft.getInstance().getRenderViewEntity();
		return new Vector3((float) renderEntity.getX() - pos.x, (float) renderEntity.getY() + renderEntity.getEyeHeight() - pos.y, (float) renderEntity.getZ() - pos.z);
	}

}
