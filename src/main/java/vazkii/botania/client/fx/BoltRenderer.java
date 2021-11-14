/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.client.fx;
/*
 * This class is derived from the Mekanism project.
 * See ALTERNATE_LICENSES.txt.
 * Imported October 2021 and heavily modified.
 */

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Matrix4f;

import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.phys.Vec3;

import org.apache.commons.lang3.tuple.Pair;

import vazkii.botania.client.core.helper.RenderHelper;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Set;

// todo(williewillus) integrate this properly into the particle system
public class BoltRenderer {

	public static final BoltRenderer INSTANCE = new BoltRenderer();
	/** Amount of times per tick we refresh. 3 implies 60 Hz. */
	private static final float REFRESH_TIME = 3F;
	/** We will keep track of an owner's render data for 100 ticks after there are no bolts remaining. */
	private static final double MAX_OWNER_TRACK_TIME = 100;

	private Timestamp refreshTimestamp = Timestamp.ZERO;

	private final Random random = new Random();
	private final Minecraft minecraft = Minecraft.getInstance();

	private final List<BoltOwnerData> boltOwners = new LinkedList<>();

	public static void onWorldRenderLast(float partialTicks, PoseStack ps) {
		ps.pushPose();
		// here we translate based on the inverse position of the client viewing camera to get back to 0, 0, 0
		Vec3 camVec = BoltRenderer.INSTANCE.minecraft.gameRenderer.getMainCamera().getPosition();
		ps.translate(-camVec.x, -camVec.y, -camVec.z);
		MultiBufferSource.BufferSource buffers = BoltRenderer.INSTANCE.minecraft.renderBuffers().bufferSource();
		BoltRenderer.INSTANCE.render(partialTicks, ps, buffers);
		buffers.endBatch(RenderHelper.LIGHTNING);
		ps.popPose();
	}

	public void render(float partialTicks, PoseStack matrixStack, MultiBufferSource buffers) {
		VertexConsumer buffer = buffers.getBuffer(RenderHelper.LIGHTNING);
		Matrix4f matrix = matrixStack.last().pose();
		Timestamp timestamp = new Timestamp(minecraft.level.getGameTime(), partialTicks);
		boolean refresh = timestamp.isPassed(refreshTimestamp, (1 / REFRESH_TIME));
		if (refresh) {
			refreshTimestamp = timestamp;
		}

		// todo XXX see other synchronize block
		synchronized (boltOwners) {
			for (Iterator<BoltOwnerData> iter = boltOwners.iterator(); iter.hasNext();) {
				BoltOwnerData data = iter.next();
				data.renderTick(timestamp, refresh, matrix, buffer);
				if (data.shouldRemove(timestamp)) {
					iter.remove();
				}
			}
		}
	}

	public void add(BoltEffect newBoltData, float partialTicks) {
		if (minecraft.level == null) {
			return;
		}
		var data = new BoltOwnerData();
		data.lastBolt = newBoltData;
		Timestamp timestamp = new Timestamp(minecraft.level.getGameTime(), partialTicks);
		if ((!data.lastBolt.getSpawnFunction().isConsecutive() || data.bolts.isEmpty()) && timestamp.isPassed(data.lastBoltTimestamp, data.lastBoltDelay)) {
			data.addBolt(new BoltInstance(newBoltData, timestamp), timestamp);
		}
		data.lastUpdateTimestamp = timestamp;
		// todo XXX ItemThunderSword calls this method from logical server in SP, don't do that.
		synchronized (boltOwners) {
			boltOwners.add(data);
		}
	}

	public class BoltOwnerData {

		private final Set<BoltInstance> bolts = new ObjectOpenHashSet<>();
		private BoltEffect lastBolt;
		private Timestamp lastBoltTimestamp = Timestamp.ZERO;
		private Timestamp lastUpdateTimestamp = Timestamp.ZERO;
		private double lastBoltDelay;

		private void addBolt(BoltInstance instance, Timestamp timestamp) {
			bolts.add(instance);
			lastBoltDelay = instance.bolt.getSpawnFunction().getSpawnDelay(random);
			lastBoltTimestamp = timestamp;
		}

		public void renderTick(Timestamp timestamp, boolean refresh, Matrix4f matrix, VertexConsumer buffer) {
			// tick our bolts based on the refresh rate, removing if they're now finished
			if (refresh) {
				bolts.removeIf(bolt -> bolt.tick(timestamp));
			}
			if (bolts.isEmpty() && lastBolt != null && lastBolt.getSpawnFunction().isConsecutive()) {
				addBolt(new BoltInstance(lastBolt, timestamp), timestamp);
			}
			bolts.forEach(bolt -> bolt.render(matrix, buffer, timestamp));
		}

		public boolean shouldRemove(Timestamp timestamp) {
			return bolts.isEmpty() && timestamp.isPassed(lastUpdateTimestamp, MAX_OWNER_TRACK_TIME);
		}
	}

	private static class BoltInstance {

		private final BoltEffect bolt;
		private final List<BoltEffect.BoltQuads> renderQuads;
		private final Timestamp createdTimestamp;

		public BoltInstance(BoltEffect bolt, Timestamp timestamp) {
			this.bolt = bolt;
			this.renderQuads = bolt.generate();
			this.createdTimestamp = timestamp;
		}

		public void render(Matrix4f matrix, VertexConsumer buffer, Timestamp timestamp) {
			float lifeScale = timestamp.subtract(createdTimestamp).value() / bolt.getLifespan();
			Pair<Integer, Integer> bounds = bolt.getFadeFunction().getRenderBounds(renderQuads.size(), lifeScale);
			for (int i = bounds.getLeft(); i < bounds.getRight(); i++) {
				renderQuads.get(i).getVecs().forEach(v -> buffer.vertex(matrix, (float) v.x, (float) v.y, (float) v.z)
						.color(bolt.getColor().x(), bolt.getColor().y(), bolt.getColor().z(), bolt.getColor().w())
						.endVertex());
			}
		}

		public boolean tick(Timestamp timestamp) {
			return timestamp.isPassed(createdTimestamp, bolt.getLifespan());
		}
	}

	private static class Timestamp {

		public static final Timestamp ZERO = new Timestamp(0, 0);
		private final long ticks;
		private final float partial;

		public Timestamp(long ticks, float partial) {
			this.ticks = ticks;
			this.partial = partial;
		}

		public Timestamp subtract(Timestamp other) {
			long newTicks = ticks - other.ticks;
			float newPartial = partial - other.partial;
			if (newPartial < 0) {
				newPartial += 1;
				newTicks -= 1;
			}
			return new Timestamp(newTicks, newPartial);
		}

		public float value() {
			return ticks + partial;
		}

		public boolean isPassed(Timestamp prev, double duration) {
			long ticksPassed = ticks - prev.ticks;
			if (ticksPassed > duration) {
				return true;
			}
			duration -= ticksPassed;
			if (duration >= 1) {
				return false;
			}
			return (partial - prev.partial) >= duration;
		}
	}
}
