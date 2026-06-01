/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */

package vazkii.botania.client.fx;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderBuffers;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.phys.Vec3;

// [VanillaCopy] ItemPickupParticle, but with BlockPos as target instead of an entity
public class FlowerItemPickupParticle extends Particle {
	private static final int LIFE_TIME = 3;
	private final RenderBuffers renderBuffers;
	private final Entity itemEntity;
	private final BlockPos target;
	private int life;
	private final EntityRenderDispatcher entityRenderDispatcher;
	// Botania: target doesn't move, so no need for position updates (I can already hear Sable noises, though)

	public FlowerItemPickupParticle(EntityRenderDispatcher entityRenderDispatcher, RenderBuffers buffers,
			ClientLevel level, Entity itemEntity, BlockPos target) {
		this(entityRenderDispatcher, buffers, level, itemEntity, target, itemEntity.getDeltaMovement());
	}

	private FlowerItemPickupParticle(EntityRenderDispatcher entityRenderDispatcher, RenderBuffers buffers,
			ClientLevel level, Entity itemEntity, BlockPos target, Vec3 speedVector) {
		super(level, itemEntity.getX(), itemEntity.getY(), itemEntity.getZ(), speedVector.x, speedVector.y, speedVector.z);
		this.renderBuffers = buffers;
		this.itemEntity = this.getSafeCopy(itemEntity);
		this.target = target;
		this.entityRenderDispatcher = entityRenderDispatcher;
	}

	private Entity getSafeCopy(Entity entity) {
		return !(entity instanceof ItemEntity) ? entity : ((ItemEntity) entity).copy();
	}

	@Override
	public ParticleRenderType getRenderType() {
		return ParticleRenderType.CUSTOM;
	}

	@Override
	public void render(VertexConsumer buffer, Camera renderInfo, float partialTicks) {
		float time = ((float) this.life + partialTicks) / LIFE_TIME;
		time *= time;
		Vec3 targetPos = target.getCenter().add(level.getBlockState(target).getOffset(level, target));
		double xx = Mth.lerp(time, this.itemEntity.getX(), targetPos.x);
		double yy = Mth.lerp(time, this.itemEntity.getY(), targetPos.y);
		double zz = Mth.lerp(time, this.itemEntity.getZ(), targetPos.z);
		MultiBufferSource.BufferSource source = this.renderBuffers.bufferSource();
		Vec3 pos = renderInfo.getPosition();
		this.entityRenderDispatcher
				.render(
						this.itemEntity,
						xx - pos.x(),
						yy - pos.y(),
						zz - pos.z(),
						this.itemEntity.getYRot(),
						partialTicks,
						new PoseStack(),
						source,
						this.entityRenderDispatcher.getPackedLightCoords(this.itemEntity, partialTicks)
				);
		source.endBatch();
	}

	@Override
	public void tick() {
		this.life++;
		if (this.life == LIFE_TIME) {
			this.remove();
		}
	}
}
