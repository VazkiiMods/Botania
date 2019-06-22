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
import net.minecraft.entity.EntityType;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.block.Blocks;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.registries.ObjectHolder;
import vazkii.botania.common.core.helper.Vector3;
import vazkii.botania.common.lib.LibMisc;

import javax.annotation.Nonnull;
import java.util.List;

public class EntityThrownItem extends ItemEntity {
	@ObjectHolder(LibMisc.MOD_ID + ":thrown_item")
	public static EntityType<?> TYPE;

	public EntityThrownItem(World world) {
		super(world);
		setInvulnerable(true);
	}

	public EntityThrownItem(World world, double x,
			double y, double z, ItemEntity item) {
		super(world, x, y, z, item.getItem());
		setPickupDelay(item.pickupDelay);
		motionX = item.motionX;
		motionY = item.motionY;
		motionZ = item.motionZ;
		setInvulnerable(true);
	}

	@Nonnull
	@Override
	public EntityType<?> getType() {
		return TYPE;
	}

	@Override
	public void tick() {
		super.tick();
		Vec3d vec3 = new Vec3d(posX, posY, posZ);
		Vec3d vec31 = new Vec3d(posX + motionX, posY + motionY, posZ + motionZ);

		RayTraceResult RayTraceResult = world.rayTraceBlocks(vec3, vec31);


		if (!world.isRemote)
		{
			Entity entity = null;
			List<Entity> list = world.getEntitiesWithinAABBExcludingEntity(this, getBoundingBox().offset(motionX*2, motionY*2, motionZ*2).grow(2));
			double d0 = 0.0D;

			for (Entity entity1 : list) {
				if (entity1.canBeCollidedWith() && (!(entity1 instanceof PlayerEntity) || pickupDelay == 0)) {
					float f = 1.0F;
					AxisAlignedBB axisalignedbb = entity1.getBoundingBox().grow(f);
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
			if (RayTraceResult.type == net.minecraft.util.math.RayTraceResult.Type.BLOCK && world.getBlockState(RayTraceResult.getBlockPos()).getBlock() == Blocks.NETHER_PORTAL)
			{
				setPortal(RayTraceResult.getBlockPos());
			}
			else
			{
				if (RayTraceResult.type != null) {
					RayTraceResult.entity.attackEntityFrom(DamageSource.MAGIC, 2.0F);
					if (!world.isRemote) {
						Entity item = getItem().getItem().createEntity(world, this, getItem());
						if (item == null) {
							item = new ItemEntity(world, posX, posY, posZ, getItem());
							world.addEntity(item);
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
					remove();

				}
			}
		}

		Vector3 vec3m = new Vector3(motionX, motionY, motionZ);
		if (vec3m.mag() < 1.0F) {
			if (!world.isRemote) {
				Entity item = getItem().getItem().createEntity(world, this, getItem());
				if (item == null) {
					item = new ItemEntity(world, posX, posY, posZ, getItem());
					world.addEntity(item);
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
			remove();
		}
	}
}
