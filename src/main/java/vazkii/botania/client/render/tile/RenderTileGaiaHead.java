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
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.SkullModelBase;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.SkullBlockRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.boss.wither.WitherBoss;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.monster.Skeleton;
import net.minecraft.world.entity.monster.WitherSkeleton;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.SkullBlock;

import vazkii.botania.client.core.helper.ShaderHelper;
import vazkii.botania.client.core.helper.ShaderWrappedRenderLayer;
import vazkii.botania.client.render.entity.RenderDoppleganger;

import javax.annotation.Nullable;

import java.util.Map;

public class RenderTileGaiaHead extends SkullBlockRenderer {
	public RenderTileGaiaHead(BlockEntityRendererProvider.Context ctx) {
		super(ctx);
	}

	// [VanillaCopy] super, but finding the skull type and profile ourselves and calling our own method to get RenderType
	public static void gaiaRender(@Nullable Direction facing, float rotation, float animationProgress, PoseStack ms,
			MultiBufferSource buffers, int light, Map<SkullBlock.Type, SkullModelBase> models) {
		Entity view = Minecraft.getInstance().getCameraEntity();
		SkullBlock.Type type = SkullBlock.Types.PLAYER;
		GameProfile profile = null;

		if (view instanceof Player) {
			profile = ((Player) view).getGameProfile();
		} else if (view instanceof Skeleton) {
			type = SkullBlock.Types.SKELETON;
		} else if (view instanceof WitherSkeleton) {
			type = SkullBlock.Types.WITHER_SKELETON;
		} else if (view instanceof WitherBoss) {
			type = SkullBlock.Types.WITHER_SKELETON;
		} else if (view instanceof Zombie) {
			type = SkullBlock.Types.ZOMBIE;
		} else if (view instanceof Creeper) {
			type = SkullBlock.Types.CREEPER;
		} else if (view instanceof EnderDragon) {
			type = SkullBlock.Types.DRAGON;
		}

		SkullModelBase model = models.get(type);
		ms.pushPose();
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
		RenderType layer = SkullBlockRenderer.getRenderType(type, profile);
		if (ShaderHelper.useShaders()) {
			layer = new ShaderWrappedRenderLayer(ShaderHelper.BotaniaShader.DOPPLEGANGER, RenderDoppleganger.defaultCallback, layer);
		}
		VertexConsumer ivertexbuilder = buffers.getBuffer(layer);
		model.setupAnim(animationProgress, rotation, 0.0F);
		model.renderToBuffer(ms, ivertexbuilder, light, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
		ms.popPose();
	}
}
