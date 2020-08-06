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
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;

import net.minecraft.block.SkullBlock;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.model.GenericHeadModel;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.tileentity.SkullTileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.entity.Entity;
import net.minecraft.entity.boss.WitherEntity;
import net.minecraft.entity.boss.dragon.EnderDragonEntity;
import net.minecraft.entity.monster.CreeperEntity;
import net.minecraft.entity.monster.SkeletonEntity;
import net.minecraft.entity.monster.WitherSkeletonEntity;
import net.minecraft.entity.monster.ZombieEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Direction;

import vazkii.botania.client.core.helper.ShaderHelper;
import vazkii.botania.client.core.helper.ShaderWrappedRenderLayer;
import vazkii.botania.client.render.entity.RenderDoppleganger;
import vazkii.botania.mixin.AccessorSkullTileEntityRenderer;

import javax.annotation.Nullable;

public class RenderTileGaiaHead extends SkullTileEntityRenderer {
	public RenderTileGaiaHead(TileEntityRendererDispatcher manager) {
		super(manager);
	}

	// [VanillaCopy] super, but finding the skull type and profile ourselves and calling our own method to get RenderType
	public static void gaiaRender(@Nullable Direction facing, float rotation, float animationProgress, MatrixStack ms, IRenderTypeBuffer buffers, int light) {
		Entity view = Minecraft.getInstance().getRenderViewEntity();
		SkullBlock.ISkullType type = SkullBlock.Types.PLAYER;
		GameProfile profile = null;

		if (view instanceof PlayerEntity) {
			profile = ((PlayerEntity) view).getGameProfile();
		} else if (view instanceof SkeletonEntity) {
			type = SkullBlock.Types.SKELETON;
		} else if (view instanceof WitherSkeletonEntity) {
			type = SkullBlock.Types.WITHER_SKELETON;
		} else if (view instanceof WitherEntity) {
			type = SkullBlock.Types.WITHER_SKELETON;
		} else if (view instanceof ZombieEntity) {
			type = SkullBlock.Types.ZOMBIE;
		} else if (view instanceof CreeperEntity) {
			type = SkullBlock.Types.CREEPER;
		} else if (view instanceof EnderDragonEntity) {
			type = SkullBlock.Types.DRAGON;
		}

		GenericHeadModel genericheadmodel = AccessorSkullTileEntityRenderer.getModels().get(type);
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
		RenderType layer = AccessorSkullTileEntityRenderer.callGetRenderType(type, profile);
		if (ShaderHelper.useShaders()) {
			layer = new ShaderWrappedRenderLayer(ShaderHelper.BotaniaShader.DOPPLEGANGER, RenderDoppleganger.defaultCallback, layer);
		}
		IVertexBuilder ivertexbuilder = buffers.getBuffer(layer);
		genericheadmodel.func_225603_a_(animationProgress, rotation, 0.0F);
		genericheadmodel.render(ms, ivertexbuilder, light, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
		ms.pop();
	}
}
