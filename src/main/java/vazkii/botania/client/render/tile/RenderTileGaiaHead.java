/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.client.render.tile;

import com.mojang.authlib.GameProfile;

import net.minecraft.block.SkullBlock;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher;
import net.minecraft.client.render.block.entity.SkullBlockEntityRenderer;
import net.minecraft.client.render.entity.model.SkullEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.boss.WitherEntity;
import net.minecraft.entity.boss.dragon.EnderDragonEntity;
import net.minecraft.entity.mob.CreeperEntity;
import net.minecraft.entity.mob.SkeletonEntity;
import net.minecraft.entity.mob.WitherSkeletonEntity;
import net.minecraft.entity.mob.ZombieEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Direction;

import vazkii.botania.client.core.helper.ShaderHelper;
import vazkii.botania.client.core.helper.ShaderWrappedRenderLayer;
import vazkii.botania.client.render.entity.RenderDoppleganger;
import vazkii.botania.mixin.AccessorSkullTileEntityRenderer;

import javax.annotation.Nullable;

public class RenderTileGaiaHead extends SkullBlockEntityRenderer {
	public RenderTileGaiaHead(BlockEntityRenderDispatcher manager) {
		super(manager);
	}

	// [VanillaCopy] super, but finding the skull type and profile ourselves and calling our own method to get RenderType
	public static void gaiaRender(@Nullable Direction facing, float rotation, float animationProgress, MatrixStack ms, VertexConsumerProvider buffers, int light) {
		Entity view = MinecraftClient.getInstance().getCameraEntity();
		SkullBlock.SkullType type = SkullBlock.Type.PLAYER;
		GameProfile profile = null;

		if (view instanceof PlayerEntity) {
			profile = ((PlayerEntity) view).getGameProfile();
		} else if (view instanceof SkeletonEntity) {
			type = SkullBlock.Type.SKELETON;
		} else if (view instanceof WitherSkeletonEntity) {
			type = SkullBlock.Type.WITHER_SKELETON;
		} else if (view instanceof WitherEntity) {
			type = SkullBlock.Type.WITHER_SKELETON;
		} else if (view instanceof ZombieEntity) {
			type = SkullBlock.Type.ZOMBIE;
		} else if (view instanceof CreeperEntity) {
			type = SkullBlock.Type.CREEPER;
		} else if (view instanceof EnderDragonEntity) {
			type = SkullBlock.Type.DRAGON;
		}

		SkullEntityModel genericheadmodel = AccessorSkullTileEntityRenderer.getModels().get(type);
		ms.push();
		if (facing == null) {
			ms.translate(0.5D, 0.0D, 0.5D);
		} else {
			switch (facing) {
			case NORTH:
				ms.translate(0.5D, 0.25D, 0.74D);
				break;
			case SOUTH:
				ms.translate(0.5D, 0.25D, 0.26D);
				break;
			case WEST:
				ms.translate(0.74D, 0.25D, 0.5D);
				break;
			case EAST:
			default:
				ms.translate(0.26D, 0.25D, 0.5D);
			}
		}

		ms.scale(-1.0F, -1.0F, 1.0F);
		RenderLayer layer = AccessorSkullTileEntityRenderer.botania_getRenderType(type, profile);
		if (ShaderHelper.useShaders()) {
			layer = new ShaderWrappedRenderLayer(ShaderHelper.BotaniaShader.DOPPLEGANGER, RenderDoppleganger.defaultCallback, layer);
		}
		VertexConsumer ivertexbuilder = buffers.getBuffer(layer);
		genericheadmodel.method_2821(animationProgress, rotation, 0.0F);
		genericheadmodel.render(ms, ivertexbuilder, light, OverlayTexture.DEFAULT_UV, 1.0F, 1.0F, 1.0F, 1.0F);
		ms.pop();
	}
}
