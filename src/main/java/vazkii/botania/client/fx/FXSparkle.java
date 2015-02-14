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

import java.util.ArrayDeque;
import java.util.Queue;

import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import org.lwjgl.opengl.GL11;

import vazkii.botania.client.core.helper.ShaderHelper;
import vazkii.botania.client.lib.LibResources;
import vazkii.botania.common.core.handler.ConfigHandler;
import vazkii.botania.common.core.helper.ObfuscationHelper;

public class FXSparkle extends EntityFX {

	public static final ResourceLocation particles = new ResourceLocation(LibResources.MISC_PARTICLES);

	public static Queue<FXSparkle> queuedRenders = new ArrayDeque();
	public static Queue<FXSparkle> queuedCorruptRenders = new ArrayDeque();

	// Queue values
	float f;
	float f1;
	float f2;
	float f3;
	float f4;
	float f5;

	public FXSparkle(World world, double x, double y, double z, float size, float red, float green, float blue, int m) {
		super(world, x, y, z, 0.0D, 0.0D, 0.0D);

		particleRed = red;
		particleGreen = green;
		particleBlue = blue;
		particleGravity = 0;
		motionX = motionY = motionZ = 0;
		particleScale *= size;
		particleMaxAge = 3 * m;
		multiplier = m;
		noClip = false;
		setSize(0.01F, 0.01F);
		prevPosX = posX;
		prevPosY = posY;
		prevPosZ = posZ;
	}

	public static void dispatchQueuedRenders(Tessellator tessellator) {
		ParticleRenderDispatcher.sparkleFxCount = 0;
		ParticleRenderDispatcher.fakeSparkleFxCount = 0;

		GL11.glColor4f(1.0F, 1.0F, 1.0F, 0.75F);
		Minecraft.getMinecraft().renderEngine.bindTexture(ConfigHandler.matrixMode ? ObfuscationHelper.getParticleTexture() : particles);

		tessellator.startDrawingQuads();
		for(FXSparkle sparkle : queuedRenders)
			sparkle.renderQueued(tessellator);
		tessellator.draw();

		ShaderHelper.useShader(ShaderHelper.filmGrain);
		tessellator.startDrawingQuads();
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

		tessellator.setBrightness(0x0000f0);

		tessellator.setColorRGBA_F(particleRed * var16, particleGreen * var16, particleBlue * var16, 1);
		tessellator.addVertexWithUV(var13 - f1 * var12 - f4 * var12, var14 - f2 * var12, var15 - f3 * var12 - f5 * var12, var9, var11);
		tessellator.addVertexWithUV(var13 - f1 * var12 + f4 * var12, var14 + f2 * var12, var15 - f3 * var12 + f5 * var12, var9, var10);
		tessellator.addVertexWithUV(var13 + f1 * var12 + f4 * var12, var14 + f2 * var12, var15 + f3 * var12 + f5 * var12, var8, var10);
		tessellator.addVertexWithUV(var13 + f1 * var12 - f4 * var12, var14 - f2 * var12, var15 + f3 * var12 - f5 * var12, var8, var11);

	}

	@Override
	public void renderParticle(Tessellator tessellator, float f, float f1, float f2, float f3, float f4, float f5) {
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
			setDead();

		motionY -= 0.04D * particleGravity;

		if (!noClip && !fake)
			pushOutOfBlocks(posX, (boundingBox.minY + boundingBox.maxY) / 2.0D, posZ);

		posX += motionX;
		posY += motionY;
		posZ += motionZ;

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
			setDead();
	}

	public void setGravity(float value) {
		particleGravity = value;
	}

	protected boolean pushOutOfBlocks(double par1, double par3, double par5) {
		int var7 = MathHelper.floor_double(par1);
		int var8 = MathHelper.floor_double(par3);
		int var9 = MathHelper.floor_double(par5);
		double var10 = par1 - var7;
		double var12 = par3 - var8;
		double var14 = par5 - var9;

		if (!worldObj.isAirBlock(var7, var8, var9)) {
			boolean var16 = !worldObj.isBlockNormalCubeDefault(var7 - 1, var8, var9, false);
			boolean var17 = !worldObj.isBlockNormalCubeDefault(var7 + 1, var8, var9, false);
			boolean var18 = !worldObj.isBlockNormalCubeDefault(var7, var8 - 1, var9, false);
			boolean var19 = !worldObj.isBlockNormalCubeDefault(var7, var8 + 1, var9, false);
			boolean var20 = !worldObj.isBlockNormalCubeDefault(var7, var8, var9 - 1, false);
			boolean var21 = !worldObj.isBlockNormalCubeDefault(var7, var8, var9 + 1, false);
			byte var22 = -1;
			double var23 = 9999.0D;

			if (var16 && var10 < var23) {
				var23 = var10;
				var22 = 0;
			}

			if (var17 && 1.0D - var10 < var23) {
				var23 = 1.0D - var10;
				var22 = 1;
			}

			if (var18 && var12 < var23) {
				var23 = var12;
				var22 = 2;
			}

			if (var19 && 1.0D - var12 < var23) {
				var23 = 1.0D - var12;
				var22 = 3;
			}

			if (var20 && var14 < var23) {
				var23 = var14;
				var22 = 4;
			}

			if (var21 && 1.0D - var14 < var23) {
				var23 = 1.0D - var14;
				var22 = 5;
			}

			float var25 = rand.nextFloat() * 0.05F + 0.025F;
			float var26 = (rand.nextFloat() - rand.nextFloat()) * 0.1F;

			if (var22 == 0) {
				motionX = -var25;
				motionY=motionZ=var26;
			}

			if (var22 == 1) {
				motionX = var25;
				motionY=motionZ=var26;
			}

			if (var22 == 2) {
				motionY = -var25;
				motionX=motionZ=var26;
			}

			if (var22 == 3) {
				motionY = var25;
				motionX=motionZ=var26;
			}

			if (var22 == 4) {
				motionZ = -var25;
				motionY=motionX=var26;
			}

			if (var22 == 5) {
				motionZ = var25;
				motionY=motionX=var26;
			}

			return true;
		} else return false;
	}

	public boolean corrupt = false;
	public boolean fake = false;
	public int multiplier = 2;
	public boolean shrink = true;
	public int particle = 16;
	public boolean tinkle = false;
	public boolean slowdown = true;
	public int currentColor = 0;
}
