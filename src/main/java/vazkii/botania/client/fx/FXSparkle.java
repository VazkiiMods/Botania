/**
 * This class was created by <Azanor>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [? (GMT)]
 */
package vazkii.botania.client.fx;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.IParticleRenderType;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.SpriteTexturedParticle;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.renderer.BufferBuilder;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.lwjgl.opengl.GL11;
import vazkii.botania.client.core.helper.ShaderHelper;
import vazkii.botania.client.lib.LibResources;
import vazkii.botania.common.core.handler.ConfigHandler;

import javax.annotation.Nonnull;
import java.util.ArrayDeque;
import java.util.Queue;

public class FXSparkle extends Particle {

	private static final ResourceLocation vanillaParticles = new ResourceLocation("textures/particle/particles.png");
	public static final ResourceLocation particles = new ResourceLocation(LibResources.MISC_PARTICLES);

	private static final Queue<FXSparkle> queuedRenders = new ArrayDeque<>();
	private static final Queue<FXSparkle> queuedCorruptRenders = new ArrayDeque<>();

	protected float particleScale = (this.rand.nextFloat() * 0.5F + 0.5F) * 2.0F;
	// Queue values
	private float f;
	private float f1;
	private float f2;
	private float f3;
	private float f4;
	private float f5;

	public FXSparkle(World world, double x, double y, double z, float size, float red, float green, float blue, int m) {
		super(world, x, y, z, 0.0D, 0.0D, 0.0D);
		particleRed = red;
		particleGreen = green;
		particleBlue = blue;
		particleGravity = 0;
		motionX = motionY = motionZ = 0;
		particleScale *= size;
		maxAge = 3 * m;
		multiplier = m;
		setSize(0.01F, 0.01F);
		prevPosX = posX;
		prevPosY = posY;
		prevPosZ = posZ;
	}

	public static void dispatchQueuedRenders(Tessellator tessellator) {
		ParticleRenderDispatcher.sparkleFxCount = 0;
		ParticleRenderDispatcher.fakeSparkleFxCount = 0;

		GlStateManager.color4f(1.0F, 1.0F, 1.0F, 0.75F);
		Minecraft.getInstance().textureManager.bindTexture(ConfigHandler.CLIENT.matrixMode.get() ? vanillaParticles : particles);

		tessellator.getBuffer().begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_COLOR);
		for(FXSparkle sparkle : queuedRenders)
			sparkle.renderQueued(tessellator);
		tessellator.draw();

		ShaderHelper.useShader(ShaderHelper.filmGrain);
		tessellator.getBuffer().begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_COLOR);
		for(FXSparkle sparkle : queuedCorruptRenders)
			sparkle.renderQueued(tessellator);
		tessellator.draw();
		ShaderHelper.releaseShader();

		queuedRenders.clear();
		queuedCorruptRenders.clear();
	}

	private void renderQueued(Tessellator tessellator) {
		if(fake)
			ParticleRenderDispatcher.fakeSparkleFxCount++;
		else ParticleRenderDispatcher.sparkleFxCount++;

		int part = particle + age/multiplier;

		float var8 = part % 8 / 8.0F;
		float var9 = var8 + 0.0624375F*2;
		float var10 = part / 8 / 8.0F;
		float var11 = var10 + 0.0624375F*2;
		float var12 = 0.1F * particleScale;
		boolean shrink = true;
		if (shrink) var12 *= (maxAge-age+1)/(float)maxAge;
		float var13 = (float)(prevPosX + (posX - prevPosX) * f - interpPosX);
		float var14 = (float)(prevPosY + (posY - prevPosY) * f - interpPosY);
		float var15 = (float)(prevPosZ + (posZ - prevPosZ) * f - interpPosZ);
		float var16 = 1.0F;

		tessellator.getBuffer().pos(var13 - f1 * var12 - f4 * var12, var14 - f2 * var12, var15 - f3 * var12 - f5 * var12).tex(var9, var11).color(particleRed * var16, particleGreen * var16, particleBlue * var16, 1).endVertex();
		tessellator.getBuffer().pos(var13 - f1 * var12 + f4 * var12, var14 + f2 * var12, var15 - f3 * var12 + f5 * var12).tex(var9, var10).color(particleRed * var16, particleGreen * var16, particleBlue * var16, 1).endVertex();
		tessellator.getBuffer().pos(var13 + f1 * var12 + f4 * var12, var14 + f2 * var12, var15 + f3 * var12 + f5 * var12).tex(var8, var10).color(particleRed * var16, particleGreen * var16, particleBlue * var16, 1).endVertex();
		tessellator.getBuffer().pos(var13 + f1 * var12 - f4 * var12, var14 - f2 * var12, var15 + f3 * var12 - f5 * var12).tex(var8, var11).color(particleRed * var16, particleGreen * var16, particleBlue * var16, 1).endVertex();

	}

	public void setCanCollide(boolean canCollide) {
		this.canCollide = canCollide;
	}

	@Override
	public void renderParticle(BufferBuilder buffer, ActiveRenderInfo entityIn, float partialTicks, float rotationX, float rotationZ, float rotationYZ, float rotationXY, float rotationXZ) {
		this.f = partialTicks;
		this.f1 = rotationX;
		this.f2 = rotationZ;
		this.f3 = rotationYZ;
		this.f4 = rotationXY;
		this.f5 = rotationXZ;

		if(corrupt)
			queuedCorruptRenders.add(this);
		else queuedRenders.add(this);
	}

	@Override
	public void tick() {
		prevPosX = posX;
		prevPosY = posY;
		prevPosZ = posZ;

		if (age++ >= maxAge)
			setExpired();

		motionY -= 0.04D * particleGravity;

		if (canCollide && !fake)
			wiggleAround(posX, (getBoundingBox().minY + getBoundingBox().maxY) / 2.0D, posZ);

		this.move(motionX, motionY, motionZ);

		if (slowdown) {
			motionX *= 0.908000001907348633D;
			motionY *= 0.908000001907348633D;
			motionZ *= 0.908000001907348633D;

			if (onGround) {
				motionX *= 0.69999998807907104D;
				motionZ *= 0.69999998807907104D;
			}
		}

		if(fake && age > 1)
			setExpired();
	}

	@Nonnull
	@Override
	public IParticleRenderType getRenderType() {
		return IParticleRenderType.CUSTOM;
	}

	public void setGravity(float value) {
		particleGravity = value;
	}



	// [VanillaCopy] Entity.pushOutOfBlocks with tweaks
	private void wiggleAround(double x, double y, double z)
	{
		BlockPos blockpos = new BlockPos(x, y, z);
		Vec3d vec3d = new Vec3d(x - (double)blockpos.getX(), y - (double)blockpos.getY(), z - (double)blockpos.getZ());
		BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();
		Direction direction = Direction.UP;
		double d0 = Double.MAX_VALUE;

		for(Direction direction1 : new Direction[]{Direction.NORTH, Direction.SOUTH, Direction.WEST, Direction.EAST, Direction.UP}) {
			blockpos$mutableblockpos.setPos(blockpos).move(direction1);
			if (!Block.isOpaque(this.world.getBlockState(blockpos$mutableblockpos).getCollisionShape(this.world, blockpos$mutableblockpos))) {
				double d1 = vec3d.getCoordinate(direction1.getAxis());
				double d2 = direction1.getAxisDirection() == Direction.AxisDirection.POSITIVE ? 1.0D - d1 : d1;
				if (d2 < d0) {
					d0 = d2;
					direction = direction1;
				}
			}
		}

		// Botania - made multiplier and add both smaller
		float f = this.rand.nextFloat() * 0.05F + 0.025F;
		float f1 = (float)direction.getAxisDirection().getOffset();
		// Botania - Randomness in other axes as well
		float secondary = (rand.nextFloat() - rand.nextFloat()) * 0.1F;
		float secondary2 = (rand.nextFloat() - rand.nextFloat()) * 0.1F;
		if (direction.getAxis() == Direction.Axis.X) {
			motionX = (double)(f1 * f);
			motionY = secondary;
			motionZ = secondary2;
		} else if (direction.getAxis() == Direction.Axis.Y) {
			motionX = secondary;
			motionY = (double)(f1 * f);
			motionZ = secondary2;
		} else if (direction.getAxis() == Direction.Axis.Z) {
			motionX = secondary;
			motionY = secondary2;
			motionZ = (double)(f1 * f);
		}
	}

	public boolean corrupt = false;
	public boolean fake = false;
	private int multiplier = 2;
	public final int particle = 16;
	public final boolean slowdown = true;
}
