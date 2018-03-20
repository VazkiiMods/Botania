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

import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.lwjgl.opengl.GL11;
import vazkii.botania.client.core.helper.ShaderHelper;
import vazkii.botania.client.lib.LibResources;
import vazkii.botania.common.core.handler.ConfigHandler;

import java.util.ArrayDeque;
import java.util.Queue;

public class FXSparkle extends Particle {

	private static final ResourceLocation vanillaParticles = new ResourceLocation("textures/particle/particles.png");
	public static final ResourceLocation particles = new ResourceLocation(LibResources.MISC_PARTICLES);

	private static final Queue<FXSparkle> queuedRenders = new ArrayDeque<>();
	private static final Queue<FXSparkle> queuedCorruptRenders = new ArrayDeque<>();

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
		particleAlpha = 0.5F; // So MC renders us on the alpha layer, value not actually used
		particleGravity = 0;
		motionX = motionY = motionZ = 0;
		particleScale *= size;
		particleMaxAge = 3 * m;
		multiplier = m;
		setSize(0.01F, 0.01F);
		prevPosX = posX;
		prevPosY = posY;
		prevPosZ = posZ;
	}

	public static void dispatchQueuedRenders(Tessellator tessellator) {
		ParticleRenderDispatcher.sparkleFxCount = 0;
		ParticleRenderDispatcher.fakeSparkleFxCount = 0;

		GlStateManager.color(1.0F, 1.0F, 1.0F, 0.75F);
		Minecraft.getMinecraft().renderEngine.bindTexture(ConfigHandler.matrixMode ? vanillaParticles : particles);

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

		int part = particle + particleAge/multiplier;

		float var8 = part % 8 / 8.0F;
		float var9 = var8 + 0.0624375F*2;
		float var10 = part / 8 / 8.0F;
		float var11 = var10 + 0.0624375F*2;
		float var12 = 0.1F * particleScale;
		if (shrink) var12 *= (particleMaxAge-particleAge+1)/(float)particleMaxAge;
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
	public void renderParticle(BufferBuilder worldRendererIn, Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
		this.f = f;
		this.f1 = f1;
		this.f2 = f2;
		this.f3 = f3;
		this.f4 = f4;
		this.f5 = f5;

		if(corrupt)
			queuedCorruptRenders.add(this);
		else queuedRenders.add(this);
	}

	@Override
	public void onUpdate() {
		prevPosX = posX;
		prevPosY = posY;
		prevPosZ = posZ;

		if (particleAge++ >= particleMaxAge)
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

		if(fake && particleAge > 1)
			setExpired();
	}

	@Override
	protected void setSize(float width, float height) {
		super.setSize(width, height);
		// fix MC-12269: the bb is expanded only in +X +Z directions by super, call setPosition again to recalculate a correct BB
		setPosition(posX, posY, posZ);
	}

	public void setGravity(float value) {
		particleGravity = value;
	}

	// Copy of Entity.pushOutOfBlocks with several important changes
	private boolean wiggleAround(double x, double y, double z)
	{
		BlockPos blockpos = new BlockPos(x, y, z);
		double d0 = x - blockpos.getX();
		double d1 = y - blockpos.getY();
		double d2 = z - blockpos.getZ();

		// Botania - change collision box list check to !airblock check
		if (!world.isAirBlock(blockpos))
		{
			EnumFacing enumfacing = EnumFacing.UP;
			double d3 = Double.MAX_VALUE;

			if (!world.isBlockFullCube(blockpos.west()) && d0 < d3)
			{
				d3 = d0;
				enumfacing = EnumFacing.WEST;
			}

			if (!world.isBlockFullCube(blockpos.east()) && 1.0D - d0 < d3)
			{
				d3 = 1.0D - d0;
				enumfacing = EnumFacing.EAST;
			}

			if (!world.isBlockFullCube(blockpos.north()) && d2 < d3)
			{
				d3 = d2;
				enumfacing = EnumFacing.NORTH;
			}

			if (!world.isBlockFullCube(blockpos.south()) && 1.0D - d2 < d3)
			{
				d3 = 1.0D - d2;
				enumfacing = EnumFacing.SOUTH;
			}

			if (!world.isBlockFullCube(blockpos.up()) && 1.0D - d1 < d3)
			{
				d3 = 1.0D - d1;
				enumfacing = EnumFacing.UP;
			}

			float f = rand.nextFloat() * 0.05F + 0.025F; // Botania - made multiplier and add both smaller
			float f1 = enumfacing.getAxisDirection().getOffset();
			float secondary = (rand.nextFloat() - rand.nextFloat()) * 0.1F; // Botania - Make and use secondary movement variables below
			float secondary2 = (rand.nextFloat() - rand.nextFloat()) * 0.1F;

			if (enumfacing.getAxis() == EnumFacing.Axis.X)
			{
				motionX = f1 * f;
				motionY = secondary;
				motionZ = secondary2;
			}
			else if (enumfacing.getAxis() == EnumFacing.Axis.Y)
			{
				motionY = f1 * f;
				motionX = secondary;
				motionZ = secondary2;
			}
			else if (enumfacing.getAxis() == EnumFacing.Axis.Z)
			{
				motionZ = f1 * f;
				motionX = secondary;
				motionY = secondary2;
			}

			return true;
		}

		return false;
	}

	public boolean corrupt = false;
	public boolean fake = false;
	private int multiplier = 2;
	private final boolean shrink = true;
	public final int particle = 16;
	public boolean tinkle = false;
	public final boolean slowdown = true;
	public int currentColor = 0;
}
