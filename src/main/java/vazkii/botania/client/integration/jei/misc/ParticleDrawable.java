/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.client.integration.jei.misc;

import com.google.common.collect.Maps;
import com.google.common.collect.Queues;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import mezz.jei.api.MethodsReturnNonnullByDefault;
import mezz.jei.api.gui.drawable.IDrawableAnimated;
import vazkii.botania.client.core.handler.ClientTickHandler;
import vazkii.botania.mixin.AccessorParticleManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.IParticleRenderType;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.particles.IParticleData;
import net.minecraft.util.math.vector.Quaternion;
import net.minecraft.util.math.vector.Vector3d;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Queue;
import java.util.function.Consumer;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class ParticleDrawable implements IDrawableAnimated {
	private final ParticleManager particles;

	private final Queue<Particle> queue = Queues.newArrayDeque();
	private final Map<IParticleRenderType, Queue<Particle>> byType = Maps.newIdentityHashMap();
	private Consumer<ParticleDrawable> onTick = null;
	private final ActiveRenderInfo activeRenderInfo;

	public ParticleDrawable() {
		Minecraft mc = Minecraft.getInstance();
		particles = mc.particles;
		activeRenderInfo = new BlankRenderInfo();
	}

	public ParticleDrawable onTick(Consumer<ParticleDrawable> action) {
		onTick = action;
		return this;
	}

	@Nullable
	public Particle addParticle(IParticleData particleData, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
		Particle particle = ((AccessorParticleManager) particles).callMakeParticle(particleData, x, y, z, xSpeed, ySpeed, zSpeed);
		if (particle != null) {
			queue.add(particle);
			return particle;
		} else {
			return null;
		}
	}

	@Override
	public int getWidth() {
		return 16;
	}

	@Override
	public int getHeight() {
		return 16;
	}

	private int ticks = ClientTickHandler.ticksInGame;

	@Override
	public void draw(@Nonnull MatrixStack matrixStack, int xOffset, int yOffset) {
		if (ticks < ClientTickHandler.ticksInGame) {
			tick();
		}
		matrixStack.push();
		matrixStack.translate(xOffset + 16, yOffset + 16, 200);
		matrixStack.scale(-16, -16, -16);
		renderParticles(matrixStack);
		matrixStack.pop();
	}

	private void renderParticles(MatrixStack matrixStackIn) {
		LightTexture lightTextureIn = Minecraft.getInstance().gameRenderer.getLightTexture();

		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder bufferbuilder = tessellator.getBuffer();

		lightTextureIn.enableLightmap();
		RenderSystem.enableDepthTest();
		RenderSystem.activeTexture(org.lwjgl.opengl.GL13.GL_TEXTURE2);
		RenderSystem.disableTexture();
		RenderSystem.activeTexture(org.lwjgl.opengl.GL13.GL_TEXTURE0);

		RenderSystem.pushMatrix();
		RenderSystem.multMatrix(matrixStackIn.getLast().getMatrix());

		for (IParticleRenderType type : this.byType.keySet()) {
			if (type == IParticleRenderType.NO_RENDER)
				continue;

			Iterable<Particle> particles = this.byType.get(type);
			if (particles != null) {

				type.beginRender(bufferbuilder, Minecraft.getInstance().textureManager);

				for (Particle particle : particles) {
					particle.renderParticle(bufferbuilder, activeRenderInfo, ClientTickHandler.partialTicks);
				}

				type.finishRender(tessellator);
			}
		}

		RenderSystem.disableDepthTest();
		RenderSystem.popMatrix();

		lightTextureIn.disableLightmap();
	}

	private void tick() {
		ticks = ClientTickHandler.ticksInGame;
		if(onTick != null) {
			onTick.accept(this);
		}

		this.byType.forEach((type, particles) -> this.tickParticleList(particles));

		Particle particle;
		if (!this.queue.isEmpty()) {
			while ((particle = this.queue.poll()) != null) {
				this.byType.computeIfAbsent(particle.getRenderType(), type -> Queues.newArrayDeque())
						.add(particle);
			}
		}

	}

	private void tickParticleList(Collection<Particle> particlesIn) {
		if (!particlesIn.isEmpty()) {
			Iterator<Particle> particles = particlesIn.iterator();

			while (particles.hasNext()) {
				Particle particle = particles.next();
				particle.tick();
				if (!particle.isAlive()) {
					particles.remove();
				}
			}
		}
	}

	private static class BlankRenderInfo extends ActiveRenderInfo {

		@Override
		public Vector3d getProjectedView() {
			return Vector3d.ZERO;
		}

		@Override
		public Quaternion getRotation() {
			return Quaternion.ONE;
		}

		@Override
		public boolean isValid() {
			return true;
		}
	}
}
