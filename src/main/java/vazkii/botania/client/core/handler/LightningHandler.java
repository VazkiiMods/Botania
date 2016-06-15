/**
 * This class was created by <Vazkii/ChickenBones>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [Feb 3, 2014, 9:05:38 PM (GMT)]
 */
package vazkii.botania.client.core.handler;

import com.google.common.collect.Lists;
import gnu.trove.map.hash.TIntIntHashMap;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.profiler.Profiler;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.opengl.GL11;
import vazkii.botania.client.core.handler.LightningHandler.LightningBolt.Segment;
import vazkii.botania.client.fx.ParticleRenderDispatcher;
import vazkii.botania.client.lib.LibResources;
import vazkii.botania.common.core.helper.Vector3;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class LightningHandler {

	private static final int BATCH_THRESHOLD = 200;
	private static final ResourceLocation outsideResource = new ResourceLocation(LibResources.MISC_WISP_LARGE);
	private static final ResourceLocation insideResource = new ResourceLocation(LibResources.MISC_WISP_SMALL);

	private static Vector3 getRelativeViewVector(Vector3 pos) {
		Entity renderEntity = Minecraft.getMinecraft().getRenderViewEntity();
		return new Vector3((float) renderEntity.posX - pos.x, (float) renderEntity.posY + renderEntity.getEyeHeight() - pos.y, (float) renderEntity.posZ - pos.z);
	}

	@SubscribeEvent
	public void onRenderWorldLast(RenderWorldLastEvent event) {
		Profiler profiler = Minecraft.getMinecraft().mcProfiler;

		profiler.startSection("botania-particles");
		ParticleRenderDispatcher.dispatch();
		profiler.startSection("redString");
		RedStringRenderer.renderAll();
		profiler.endStartSection("lightning");

		float frame = event.getPartialTicks();
		Entity entity = Minecraft.getMinecraft().thePlayer;
		TextureManager render = Minecraft.getMinecraft().renderEngine;

		double interpPosX = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * frame;
		double interpPosY = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * frame;
		double interpPosZ = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * frame;

		GlStateManager.pushMatrix();
		GlStateManager.translate(-interpPosX, -interpPosY, -interpPosZ);

		Tessellator tessellator = Tessellator.getInstance();

		GlStateManager.depthMask(false);
		GlStateManager.enableBlend();
		GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

		ParticleRenderDispatcher.lightningCount = 0;

		render.bindTexture(outsideResource);
		int counter = 0;

		tessellator.getBuffer().begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_LMAP_COLOR);
		for (LightningBolt bolt : LightningBolt.boltlist) {
			renderBolt(bolt, tessellator, 0, false);
			if(counter % BATCH_THRESHOLD == BATCH_THRESHOLD - 1) {
				tessellator.draw();
				tessellator.getBuffer().begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_LMAP_COLOR);
			}
			counter++;
		}
		tessellator.draw();

		render.bindTexture(insideResource);
		counter = 0;

		tessellator.getBuffer().begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_LMAP_COLOR);
		for (LightningBolt bolt : LightningBolt.boltlist) {
			renderBolt(bolt, tessellator, 0, false);
			if(counter % BATCH_THRESHOLD == BATCH_THRESHOLD - 1) {
				tessellator.draw();
				tessellator.getBuffer().begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_LMAP_COLOR);
			}
			counter++;
		}
		tessellator.draw();

		GlStateManager.disableBlend();
		GlStateManager.depthMask(true);

		GlStateManager.translate(interpPosX, interpPosY, interpPosZ);
		GlStateManager.popMatrix();

		profiler.endSection();
		profiler.endSection();

	}

	public static void spawnLightningBolt(World world, Vector3 vectorStart, Vector3 vectorEnd, float ticksPerMeter, long seed, int colorOuter, int colorInner) {
		LightningBolt bolt = new LightningBolt(world, vectorStart, vectorEnd, ticksPerMeter, seed, colorOuter, colorInner);
		bolt.defaultFractal();
		bolt.finalizeBolt();
		LightningBolt.boltlist.add(bolt);
	}

	private void renderBolt(LightningBolt bolt, Tessellator tessellator, int pass, boolean inner) {
		ParticleRenderDispatcher.lightningCount++;
		float boltage = bolt.particleAge < 0 ? 0 : (float) bolt.particleAge / (float) bolt.particleMaxAge;
		float mainalpha;
		if(pass == 0)
			mainalpha = (1 - boltage) * 0.4F;
		else mainalpha = 1 - boltage * 0.5F;

		int expandTime = (int) (bolt.length * bolt.speed);

		int renderstart = (int) ((expandTime / 2 - bolt.particleMaxAge + bolt.particleAge) / (float) (expandTime / 2) * bolt.numsegments0);
		int renderend = (int) ((bolt.particleAge + expandTime) / (float) expandTime * bolt.numsegments0);

		for(Segment rendersegment : bolt.segments) {
			if(rendersegment.segmentno < renderstart || rendersegment.segmentno > renderend)
				continue;

			Vector3 playervec = getRelativeViewVector(rendersegment.startpoint.point).multiply(-1);

			double width = 0.025F * (playervec.mag() / 5 + 1) * (1 + rendersegment.light) * 0.5F;

			Vector3 diff1 = playervec.copy().crossProduct(rendersegment.prevdiff).normalize().multiply(width / rendersegment.sinprev);
			Vector3 diff2 = playervec.copy().crossProduct(rendersegment.nextdiff).normalize().multiply(width / rendersegment.sinnext);

			Vector3 startvec = rendersegment.startpoint.point;
			Vector3 endvec = rendersegment.endpoint.point;

			int color = inner ? bolt.colorInner : bolt.colorOuter;
			int r = (color & 0xFF0000) >> 16;
			int g = (color & 0xFF00) >> 8;
			int b = (color & 0xFF);
			int a = (int) (mainalpha * rendersegment.light * new Color(color).getAlpha());

			tessellator.getBuffer().pos(endvec.x - diff2.x, endvec.y - diff2.y, endvec.z - diff2.z).tex(0.5, 0).lightmap(0xF0, 0xF0).color(r, g, b, a).endVertex();
			tessellator.getBuffer().pos(startvec.x - diff1.x, startvec.y - diff1.y, startvec.z - diff1.z).tex(0.5, 0).lightmap(0xF0, 0xF0).color(r, g, b, a).endVertex();
			tessellator.getBuffer().pos(startvec.x + diff1.x, startvec.y + diff1.y, startvec.z + diff1.z).tex(0.5, 1).lightmap(0xF0, 0xF0).color(r, g, b, a).endVertex();
			tessellator.getBuffer().pos(endvec.x + diff2.x, endvec.y + diff2.y, endvec.z + diff2.z).tex(0.5, 1).lightmap(0xF0, 0xF0).color(r, g, b, a).endVertex();

			if(rendersegment.next == null) {
				Vector3 roundend = rendersegment.endpoint.point.copy().add(rendersegment.diff.copy().normalize().multiply(width));

				tessellator.getBuffer().pos(roundend.x - diff2.x, roundend.y - diff2.y, roundend.z - diff2.z).tex(0, 0).lightmap(0xF0, 0xF0).color(r, g, b, a).endVertex();
				tessellator.getBuffer().pos(endvec.x - diff2.x, endvec.y - diff2.y, endvec.z - diff2.z).tex(0.5, 0).lightmap(0xF0, 0xF0).color(r, g, b, a).endVertex();
				tessellator.getBuffer().pos(endvec.x + diff2.x, endvec.y + diff2.y, endvec.z + diff2.z).tex(0.5, 1).lightmap(0xF0, 0xF0).color(r, g, b, a).endVertex();
				tessellator.getBuffer().pos(roundend.x + diff2.x, roundend.y + diff2.y, roundend.z + diff2.z).tex(0, 1).lightmap(0xF0, 0xF0).color(r, g, b, a).endVertex();
			}

			if(rendersegment.prev == null) {
				Vector3 roundend = rendersegment.startpoint.point.copy().subtract(rendersegment.diff.copy().normalize().multiply(width));

				tessellator.getBuffer().pos(startvec.x - diff1.x, startvec.y - diff1.y, startvec.z - diff1.z).tex(0.5, 0).lightmap(0xF0, 0xF0).color(r, g, b, a).endVertex();
				tessellator.getBuffer().pos(roundend.x - diff1.x, roundend.y - diff1.y, roundend.z - diff1.z).tex(0, 0).lightmap(0xF0, 0xF0).color(r, g, b, a).endVertex();
				tessellator.getBuffer().pos(roundend.x + diff1.x, roundend.y + diff1.y, roundend.z + diff1.z).tex(0, 1).lightmap(0xF0, 0xF0).color(r, g, b, a).endVertex();
				tessellator.getBuffer().pos(startvec.x + diff1.x, startvec.y + diff1.y, startvec.z + diff1.z).tex(0.5, 1).lightmap(0xF0, 0xF0).color(r, g, b, a).endVertex();
			}
		}
	}

	public static class LightningBolt {

		public ArrayList<Segment> segments = new ArrayList<>();
		public final Vector3 start;
		public final Vector3 end;
		final TIntIntHashMap splitparents = new TIntIntHashMap();

		public final double length;
		public int numsegments0;
		private int numsplits;
		private boolean finalized;
		private final Random rand;
		public final long seed;

		public int particleAge;
		public final int particleMaxAge;
		public boolean isDead;

		private final World world;
		private Entity source;

		public static final List<LightningBolt> boltlist = new ArrayList<>();

		public float speed = 1.5F;
		public static final int fadetime = 20;

		public final int colorOuter;
		public final int colorInner;

		public static class BoltPoint {

			public BoltPoint(Vector3 basepoint, Vector3 offsetvec) {
				point = basepoint.copy().add(offsetvec);
				this.basepoint = basepoint;
				this.offsetvec = offsetvec;
			}

			public final Vector3 point;
			final Vector3 basepoint;
			final Vector3 offsetvec;
		}

		private static final Comparator<Segment> SEGMENT_SORTER = (o1, o2) -> {
			int comp = Integer.valueOf(o1.splitno).compareTo(o2.splitno);
			if(comp == 0)
				return Integer.valueOf(o1.segmentno).compareTo(o2.segmentno);
			else return comp;
		};

		private static final Comparator<Segment> SEGMENT_LIGHT_SORTER = (o1, o2) -> Float.compare(o2.light, o1.light);

		public class Segment {
			public Segment(BoltPoint start, BoltPoint end, float light, int segmentnumber, int splitnumber) {
				startpoint = start;
				endpoint = end;
				this.light = light;
				segmentno = segmentnumber;
				splitno = splitnumber;

				calcDiff();
			}

			public Segment(Vector3 start, Vector3 end) {
				this(new BoltPoint(start, new Vector3(0, 0, 0)), new BoltPoint(end, new Vector3(0, 0, 0)), 1, 0, 0);
			}

			public void calcDiff() {
				diff = endpoint.point.copy().subtract(startpoint.point);
			}

			public void calcEndDiffs() {
				if(prev != null) {
					Vector3 prevdiffnorm = prev.diff.copy().normalize();
					Vector3 thisdiffnorm = diff.copy().normalize();

					prevdiff = thisdiffnorm.copy().add(prevdiffnorm).normalize();
					sinprev = (float) Math.sin(thisdiffnorm.angle(prevdiffnorm.multiply(-1)) / 2);
				} else {
					prevdiff = diff.copy().normalize();
					sinprev = 1;
				}

				if(next != null) {
					Vector3 nextdiffnorm = next.diff.copy().normalize();
					Vector3 thisdiffnorm = diff.copy().normalize();

					nextdiff = thisdiffnorm.add(nextdiffnorm).normalize();
					sinnext = (float) Math.sin(thisdiffnorm.angle(nextdiffnorm.multiply(-1)) / 2);
				} else {
					nextdiff = diff.copy().normalize();
					sinnext = 1;
				}
			}

			@Override
			public String toString() {
				return startpoint.point.toString() + " " + endpoint.point.toString();
			}

			public final BoltPoint startpoint;
			public final BoltPoint endpoint;

			public Vector3 diff;

			public Segment prev;
			public Segment next;

			public Vector3 nextdiff;
			public Vector3 prevdiff;

			public float sinprev;
			public float sinnext;
			public final float light;

			public final int segmentno;
			public final int splitno;
		}

		public LightningBolt(World world, Vector3 sourcevec, Vector3 targetvec, float ticksPerMeter, long seed, int colorOuter, int colorInner) {
			this.world = world;
			this.seed = seed;
			rand = new Random(seed);

			start = sourcevec;
			end = targetvec;

			speed = ticksPerMeter;

			this.colorOuter = colorOuter;
			this.colorInner = colorInner;

			numsegments0 = 1;

			length = end.copy().subtract(start).mag();
			particleMaxAge = fadetime + rand.nextInt(fadetime) - fadetime / 2;
			particleAge = -(int) (length * speed);

			segments.add(new Segment(start, end));
		}

		public void setWrapper(Entity entity) {
			source = entity;
		}

		public void fractal(int splits, double amount, double splitchance, double splitlength, double splitangle) {
			if(finalized)
				return;

			ArrayList<Segment> oldsegments = segments;
			segments = new ArrayList<>();

			Segment prev;

			for(Segment segment : oldsegments) {
				prev = segment.prev;

				Vector3 subsegment = segment.diff.copy().multiply(1F / splits);

				BoltPoint[] newpoints = new BoltPoint[splits + 1];

				Vector3 startpoint = segment.startpoint.point;
				newpoints[0] = segment.startpoint;
				newpoints[splits] = segment.endpoint;

				for(int i = 1; i < splits; i++) {
					Vector3 randoff = segment.diff.copy().perpendicular().normalize().rotate(rand.nextFloat() * 360, segment.diff);
					randoff.multiply((rand.nextFloat() - 0.5F) * amount * 2);

					Vector3 basepoint = startpoint.copy().add(subsegment.copy().multiply(i));

					newpoints[i] = new BoltPoint(basepoint, randoff);
				}

				for(int i = 0; i < splits; i++) {
					Segment next = new Segment(newpoints[i], newpoints[i + 1], segment.light, segment.segmentno * splits + i, segment.splitno);
					next.prev = prev;
					if (prev != null)
						prev.next = next;

					if(i != 0 && rand.nextFloat() < splitchance) {
						Vector3 splitrot = next.diff.copy().xCrossProduct().rotate(rand.nextFloat() * 360, next.diff);
						Vector3 diff = next.diff.copy().rotate((rand.nextFloat() * 0.66F + 0.33F) * splitangle, splitrot).multiply(splitlength);

						numsplits++;
						splitparents.put(numsplits, next.splitno);

						Segment split = new Segment(newpoints[i], new BoltPoint(newpoints[i + 1].basepoint, newpoints[i + 1].offsetvec.copy().add(diff)), segment.light / 2F, next.segmentno, numsplits);
						split.prev = prev;

						segments.add(split);
					}

					prev = next;
					segments.add(next);
				}

				if(segment.next != null)
					segment.next.prev = prev;
			}

			numsegments0 *= splits;
		}

		public void defaultFractal() {
			fractal(2, length / 1.5, 0.7F, 0.7F, 45);
			fractal(2, length / 4, 0.5F, 0.8F, 50);
			fractal(2, length / 15, 0.5F, 0.9F, 55);
			fractal(2, length / 30, 0.5F, 1.0F, 60);
			fractal(2, length / 60, 0, 0, 0);
			fractal(2, length / 100, 0, 0, 0);
			fractal(2, length / 400, 0, 0, 0);
		}

		private float rayTraceResistance(Vector3 start, Vector3 end, float prevresistance) {
			RayTraceResult mop = world.rayTraceBlocks(start.toVec3D(), end.toVec3D());

			if(mop == null)
				return prevresistance;

			if(mop.typeOfHit == RayTraceResult.Type.BLOCK) {
				Block block = world.getBlockState(mop.getBlockPos()).getBlock();

				if(world.isAirBlock(mop.getBlockPos()))
					return prevresistance;

				return prevresistance + block.getExplosionResistance(source) + 0.3F;
			} else return prevresistance;
		}

		private void calculateCollisionAndDiffs() {
			TIntIntHashMap lastactivesegment = new TIntIntHashMap();

			Collections.sort(segments, SEGMENT_SORTER);

			int lastsplitcalc = 0;
			int lastactiveseg = 0;// unterminated
			float splitresistance = 0;

			for(Segment segment : segments) {
				if(segment.splitno > lastsplitcalc) {
					lastactivesegment.put(lastsplitcalc, lastactiveseg);
					lastsplitcalc = segment.splitno;
					lastactiveseg = lastactivesegment.get(splitparents.get(segment.splitno));
					splitresistance = lastactiveseg < segment.segmentno ? 50 : 0;
				}

				if(splitresistance >= 40 * segment.light)
					continue;

				splitresistance = rayTraceResistance(segment.startpoint.point, segment.endpoint.point, splitresistance);
				lastactiveseg = segment.segmentno;
			}
			lastactivesegment.put(lastsplitcalc, lastactiveseg);

			lastsplitcalc = 0;
			lastactiveseg = lastactivesegment.get(0);
			for(Iterator<Segment> iterator = segments.iterator(); iterator.hasNext();) {
				Segment segment = iterator.next();
				if(lastsplitcalc != segment.splitno) {
					lastsplitcalc = segment.splitno;
					lastactiveseg = lastactivesegment.get(segment.splitno);
				}

				if(segment.segmentno > lastactiveseg)
					iterator.remove();
				segment.calcEndDiffs();
			}
		}

		public void finalizeBolt() {
			if(finalized)
				return;
			finalized = true;

			calculateCollisionAndDiffs();

			Collections.sort(segments, SEGMENT_LIGHT_SORTER);

			boltlist.add(this);
		}

		public void onUpdate() {
			particleAge++;

			if (particleAge >= particleMaxAge)
				isDead = true;
		}

		// Called in ClientTickHandler
		public static void update() {
			for(Iterator<LightningBolt> iterator = boltlist.iterator(); iterator.hasNext();) {
				LightningBolt bolt = iterator.next();

				bolt.onUpdate();
				if(bolt.isDead)
					iterator.remove();
			}
		}
	}
}