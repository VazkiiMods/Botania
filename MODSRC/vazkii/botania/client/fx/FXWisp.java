/**
 * This class was created by <Azanor>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under a
 * Creative Commons Attribution-NonCommercial-ShareAlike 3.0 License
 * (http://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB)
 * 
 * File Created @ [? (GMT)]
 */
package vazkii.botania.client.fx;

import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import org.lwjgl.opengl.GL11;

import vazkii.botania.client.lib.LibResources;
import vazkii.botania.common.core.helper.ObfuscationHelper;
import cpw.mods.fml.client.FMLClientHandler;

public class FXWisp extends EntityFX
{

	public static final ResourceLocation particles = new ResourceLocation(LibResources.MISC_WISP_LARGE);


	public FXWisp(World world, double d, double d1, double d2,  float f, float f1, float f2)
	{
		this(world, d, d1, d2, 1.0F, f, f1, f2);

	}

	public FXWisp(World world, double d, double d1, double d2,  float size, float red, float green, float blue)
	{
		super(world, d, d1, d2, 0.0D, 0.0D, 0.0D);
		particleRed = red;
		particleGreen = green;
		particleBlue = blue;
		particleGravity=0;
		motionX=motionY=motionZ=0;
		particleScale *= size;
		moteParticleScale = particleScale;
		particleMaxAge = (int)(36D / (Math.random() * 0.3D + 0.7D));

		moteHalfLife = particleMaxAge / 2;
		noClip = false;
		setSize(0.01F, 0.01F);
		EntityLivingBase renderentity = FMLClientHandler.instance().getClient().renderViewEntity;
		int visibleDistance = 50;
		if (!FMLClientHandler.instance().getClient().gameSettings.fancyGraphics) visibleDistance=25;
		if (renderentity.getDistance(posX, posY, posZ)>visibleDistance) particleMaxAge=0;
		prevPosX = posX;
		prevPosY = posY;
		prevPosZ = posZ;
	}


	public FXWisp(World world, double d, double d1, double d2, float f, int type)
	{
		this(world, d, d1, d2, f, 0f, 0f, 0f);

		switch (type) {
		case 0:
			particleRed=.75f + world.rand.nextFloat()*.25f;
			particleGreen=.25f + world.rand.nextFloat()*.25f;
			particleBlue=.75f + world.rand.nextFloat()*.25f;
			break;
		case 1:
			particleRed=.5f + world.rand.nextFloat()*.3f;
			particleGreen=.5f + world.rand.nextFloat()*.3f;
			particleBlue=.2f;
			break;
		case 2:
			particleRed=.2f;
			particleGreen=.2f;
			particleBlue=.7f + world.rand.nextFloat()*.3f;
			break;
		case 3:
			particleRed=.2f;
			particleGreen=.7f + world.rand.nextFloat()*.3f;
			particleBlue=.2f;
			break;
		case 4:
			particleRed=.7f + world.rand.nextFloat()*.3f;
			particleGreen=.2f;
			particleBlue=.2f;
			break;
		case 5:
			blendmode=771;
			particleRed= world.rand.nextFloat()*.1f;
			particleGreen= world.rand.nextFloat()*.1f;
			particleBlue= world.rand.nextFloat()*.1f;
			break;
		case 6:
			particleRed= .8f+world.rand.nextFloat()*.2f;
			particleGreen= .8f+world.rand.nextFloat()*.2f;
			particleBlue= .8f+world.rand.nextFloat()*.2f;
			break;
		case 7:
			particleRed=.7f + world.rand.nextFloat()*.3f;
			particleGreen=.5f + world.rand.nextFloat()*.2f;
			particleBlue=.3f + world.rand.nextFloat()*.1f;
			break;
		}

	}

	public FXWisp(World world, double d, double d1, double d2, double x, double y, double z, float f, int type)
	{
		this(world, d, d1, d2, f, type);
		if (particleMaxAge>0) {
			double dx = x - posX;
			double dy = y - posY;
			double dz = z - posZ;

			motionX = dx / particleMaxAge;
			motionY = dy / particleMaxAge;
			motionZ = dz / particleMaxAge;
		}

	}

	public FXWisp(World world, double d, double d1, double d2, double x, double y, double z,
			float f, float red, float green, float blue)
	{
		this(world, d, d1, d2, f, red,green,blue);
		if (particleMaxAge>0) {
			double dx = x - posX;
			double dy = y - posY;
			double dz = z - posZ;

			motionX = dx / particleMaxAge;
			motionY = dy / particleMaxAge;
			motionZ = dz / particleMaxAge;
		}
	}

	@Override
	public void renderParticle(Tessellator tessellator, float f, float f1, float f2, float f3, float f4, float f5)
	{



		float agescale = 0;
		if (shrink) {
			agescale = ((float)particleMaxAge-(float)particleAge) / particleMaxAge;
		} else {
			agescale = (float)particleAge / (float)moteHalfLife;
			if (agescale>1f) agescale = 2-agescale;
		}


		particleScale = moteParticleScale * agescale;

		tessellator.draw();
		GL11.glPushMatrix();


		GL11.glDepthMask(false);
		GL11.glEnable(3042);
		GL11.glBlendFunc(770, blendmode);

		Minecraft.getMinecraft().renderEngine.bindTexture(particles);

		GL11.glColor4f(1.0F, 1.0F, 1.0F, 0.75F);


		float f10 = 0.5F * particleScale;
		float f11 = (float)(prevPosX + (posX - prevPosX) * f - interpPosX);
		float f12 = (float)(prevPosY + (posY - prevPosY) * f - interpPosY);
		float f13 = (float)(prevPosZ + (posZ - prevPosZ) * f - interpPosZ);

		tessellator.startDrawingQuads();

		tessellator.setBrightness(0x0000f0);

		tessellator.setColorRGBA_F(particleRed, particleGreen, particleBlue, 0.5F);
		tessellator.addVertexWithUV(f11 - f1 * f10 - f4 * f10, f12 - f2 * f10, f13 - f3 * f10 - f5 * f10, 0, 1);
		tessellator.addVertexWithUV(f11 - f1 * f10 + f4 * f10, f12 + f2 * f10, f13 - f3 * f10 + f5 * f10, 1, 1);
		tessellator.addVertexWithUV(f11 + f1 * f10 + f4 * f10, f12 + f2 * f10, f13 + f3 * f10 + f5 * f10, 1, 0);
		tessellator.addVertexWithUV(f11 + f1 * f10 - f4 * f10, f12 - f2 * f10, f13 + f3 * f10 - f5 * f10, 0, 0);

		tessellator.draw();

		GL11.glDisable(3042);
		GL11.glDepthMask(true);

		GL11.glPopMatrix();
		Minecraft.getMinecraft().renderEngine.bindTexture(ObfuscationHelper.getParticleTexture());
		tessellator.startDrawingQuads();
	}

	@Override
	public void onUpdate()
	{


		prevPosX = posX;
		prevPosY = posY;
		prevPosZ = posZ;

		if (particleAge==0 && tinkle && worldObj.rand.nextInt(3)==0)
			worldObj.playSoundAtEntity(this, "random.orb", 0.02F, 0.5F * ((worldObj.rand.nextFloat() - worldObj.rand.nextFloat()) * 0.6F + 2F));

		if (particleAge++ >= particleMaxAge)
		{

			setDead();
		}


		motionY -= 0.040000000000000001D * particleGravity;
		//     moveEntity(motionX, motionY, motionZ);
		posX+=motionX;
		posY+=motionY;
		posZ+=motionZ;
		motionX *= 0.98000001907348633D;
		motionY *= 0.98000001907348633D;
		motionZ *= 0.98000001907348633D;
		if (onGround)
		{
			motionX *= 0.69999998807907104D;
			motionZ *= 0.69999998807907104D;
		}



	}

	public void setGravity(float value) {
		particleGravity = value;
	}

	public boolean shrink=false;
	float moteParticleScale;
	int moteHalfLife;
	public boolean tinkle=false;
	public int blendmode=1;
}
