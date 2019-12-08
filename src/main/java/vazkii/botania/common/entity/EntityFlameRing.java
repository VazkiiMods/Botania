/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Jun 26, 2014, 12:31:10 AM (GMT)]
 */
package vazkii.botania.common.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.SoundEvents;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Optional;
import vazkii.botania.common.Botania;
import vazkii.botania.common.core.helper.MathHelper;

import javax.annotation.Nonnull;

import elucent.albedo.lighting.ILightProvider;
import elucent.albedo.lighting.Light;

import java.util.List;

public class EntityFlameRing extends Entity {

	public EntityFlameRing(World world) {
		super(world);
	}

	@Override
	protected void entityInit() {
		setSize(0F, 0F);
	}

	@Override
	public void onEntityUpdate() {
		super.onEntityUpdate();

		float radius = 5F;
		float renderRadius = (float) (radius - Math.random());

		for(int i = 0; i < Math.min(90, ticksExisted); i++) {
			float a = i;
			if(a % 2 == 0)
				a = 45 + a;

			if(world.rand.nextInt(ticksExisted < 90 ? 8 : 20) == 0) {
				float rad = (float) (a * 4 * Math.PI / 180F);
				double x = Math.cos(rad) * renderRadius;
				double z = Math.sin(rad) * renderRadius;

				Botania.proxy.wispFX(posX + x, posY - 0.2, posZ + z, 1F, (float) Math.random() * 0.25F, (float) Math.random() * 0.25F, 0.65F + (float) Math.random() * 0.45F, (float) (Math.random() - 0.5F) * 0.15F, 0.055F + (float) Math.random() * 0.025F, (float) (Math.random() - 0.5F) * 0.15F);

				float gs = (float) Math.random() * 0.15F;
				float smokeRadius = (float) (renderRadius - Math.random() * renderRadius * 0.9);
				x = Math.cos(rad) * smokeRadius;
				z = Math.sin(rad) * smokeRadius;
				Botania.proxy.wispFX(posX + x, posY - 0.2, posZ + z, gs, gs, gs, 0.65F + (float) Math.random() * 0.45F, -0.155F - (float) Math.random() * 0.025F);
			}
		}

		if(world.rand.nextInt(20) == 0)
			world.playSound(posX, posY, posZ, SoundEvents.BLOCK_FIRE_AMBIENT, SoundCategory.BLOCKS, 1F, 1F, false);

		if(world.isRemote)
			return;

		if(ticksExisted >= 300) {
			setDead();
			return;
		}

		if(ticksExisted > 45) {
			AxisAlignedBB boundingBox = new AxisAlignedBB(posX, posY, posZ, posX, posY, posZ).grow(radius, radius, radius);
			List<EntityLivingBase> entities = world.getEntitiesWithinAABB(EntityLivingBase.class, boundingBox);

			if(entities.isEmpty())
				return;

			for(EntityLivingBase entity : entities) {
				if(entity == null || MathHelper.pointDistancePlane(posX, posY, entity.posX, entity.posY) > radius)
					continue;

				entity.setFire(4);
			}
		}
	}

	@Override
	public boolean attackEntityFrom(@Nonnull DamageSource par1DamageSource, float par2) {
		return false;
	}

	@Override
	protected void readEntityFromNBT(@Nonnull NBTTagCompound var1) {}

	@Override
	protected void writeEntityToNBT(@Nonnull NBTTagCompound var1) {}
}