/**
 * This class was created by <Flaxbeard>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [? (GMT)]
 */
package vazkii.botania.common.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import vazkii.botania.common.core.helper.Vector3;

import javax.annotation.Nonnull;
import java.util.List;

public class EntityThrownItem extends EntityItem {

	public EntityThrownItem(World world) {
		super(world);
	}

	public EntityThrownItem(World world, double x,
			double y, double z, EntityItem item) {
		super(world, x, y, z, item.getItem());
		setPickupDelay(item.pickupDelay);
		motionX = item.motionX;
		motionY = item.motionY;
		motionZ = item.motionZ;
	}

	@Override
	public boolean isEntityInvulnerable(@Nonnull DamageSource source) {
		return true;
	}

	@Override
	public void onUpdate() {
		super.onUpdate();
		Vec3d vec3 = new Vec3d(posX, posY, posZ);
		Vec3d vec31 = new Vec3d(posX + motionX, posY + motionY, posZ + motionZ);

		RayTraceResult RayTraceResult = world.rayTraceBlocks(vec3, vec31);


		if (!world.isRemote)
		{
			Entity entity = null;
			List<Entity> list = world.getEntitiesWithinAABBExcludingEntity(this, getEntityBoundingBox().offset(motionX*2, motionY*2, motionZ*2).grow(2));
			double d0 = 0.0D;

			for (Entity entity1 : list) {
				if (entity1.canBeCollidedWith() && (!(entity1 instanceof EntityPlayer) || pickupDelay == 0)) {
					float f = 1.0F;
					AxisAlignedBB axisalignedbb = entity1.getEntityBoundingBox().grow(f);
					RayTraceResult RayTraceResult1 = axisalignedbb.calculateIntercept(vec3, vec31);

					if (RayTraceResult1 != null) {
						double d1 = vec3.distanceTo(RayTraceResult1.hitVec);

						if (d1 < d0 || d0 == 0.0D) {
							entity = entity1;
							d0 = d1;
						}
					}
				}
			}

			if (entity != null)
			{
				RayTraceResult = new RayTraceResult(entity);
			}
		}

		if (RayTraceResult != null)
		{
			if (RayTraceResult.typeOfHit == net.minecraft.util.math.RayTraceResult.Type.BLOCK && world.getBlockState(RayTraceResult.getBlockPos()).getBlock() == Blocks.PORTAL)
			{
				setPortal(RayTraceResult.getBlockPos());
			}
			else
			{
				if (RayTraceResult.entityHit != null) {
					RayTraceResult.entityHit.attackEntityFrom(DamageSource.MAGIC, 2.0F);
					if (!world.isRemote) {
						Entity item = getItem().getItem().createEntity(world, this, getItem());
						if (item == null) {
							item = new EntityItem(world, posX, posY, posZ, getItem());
							world.spawnEntity(item);
							item.motionX = motionX*0.25F;
							item.motionY = motionY*0.25F;
							item.motionZ = motionZ*0.25F;

						}
						else
						{
							item.motionX = motionX*0.25F;
							item.motionY = motionY*0.25F;
							item.motionZ = motionZ*0.25F;
						}
					}
					setDead();

				}
			}
		}

		Vector3 vec3m = new Vector3(motionX, motionY, motionZ);
		if (vec3m.mag() < 1.0F) {
			if (!world.isRemote) {
				Entity item = getItem().getItem().createEntity(world, this, getItem());
				if (item == null) {
					item = new EntityItem(world, posX, posY, posZ, getItem());
					world.spawnEntity(item);
					item.motionX = motionX;
					item.motionY = motionY;
					item.motionZ = motionZ;
				}
				else
				{
					item.motionX = motionX;
					item.motionY = motionY;
					item.motionZ = motionZ;
				}
			}
			setDead();
		}
	}
}
