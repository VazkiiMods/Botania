/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Mar 25, 2015, 5:49:28 PM (GMT)]
 */
package vazkii.botania.common.entity;

import net.minecraft.entity.ai.EntityAIAttackRanged;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.boss.EntityWither;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

public class EntityPinkWither extends EntityWither {

	public EntityPinkWither(World world) {
		super(world);

		tasks.taskEntries.removeIf(entry -> entry.action instanceof EntityAIAttackRanged); // Remove firing wither skulls

		targetTasks.taskEntries.removeIf(entry -> entry.action instanceof EntityAIHurtByTarget
				|| entry.action instanceof EntityAINearestAttackableTarget); // Remove revenge and aggro
	}

	@Override
	public void onLivingUpdate() {
		super.onLivingUpdate();

		if(Math.random() < 0.1)
			for(int j = 0; j < 3; ++j) {
				double d10 = func_82214_u(j);
				double d2 = func_82208_v(j);
				double d4 = func_82213_w(j);
				world.spawnParticle(EnumParticleTypes.HEART, d10 + rand.nextGaussian() * 0.30000001192092896D, d2 + rand.nextGaussian() * 0.30000001192092896D, d4 + rand.nextGaussian() * 0.30000001192092896D, 0.0D, 0.0D, 0.0D);
			}
	}

	@Override
	public void updateAITasks() {
		if(ticksExisted % 20 == 0)
			heal(1.0F);
	}

	@Override
	protected boolean processInteract(EntityPlayer player, EnumHand hand) {
		if(!player.isSneaking()) {
			player.startRiding(this);
			return true;
		}
		return false;
	}

	@Override
	protected void dropFewItems(boolean wasRecentlyHit, int lootingModifier) {}

	@Override
	public void addTrackingPlayer(@Nonnull EntityPlayerMP player) {}

	// COPYPASTA

	private double func_82214_u(int p_82214_1_)
	{
		if (p_82214_1_ <= 0)
		{
			return posX;
		}
		else
		{
			float f = (renderYawOffset + 180 * (p_82214_1_ - 1)) / 180.0F * (float)Math.PI;
			float f1 = MathHelper.cos(f);
			return posX + f1 * 1.3D;
		}
	}

	private double func_82208_v(int p_82208_1_)
	{
		return p_82208_1_ <= 0 ? posY + 3.0D : posY + 2.2D;
	}

	private double func_82213_w(int p_82213_1_)
	{
		if (p_82213_1_ <= 0)
		{
			return posZ;
		}
		else
		{
			float f = (renderYawOffset + 180 * (p_82213_1_ - 1)) / 180.0F * (float)Math.PI;
			float f1 = MathHelper.sin(f);
			return posZ + f1 * 1.3D;
		}
	}
}
