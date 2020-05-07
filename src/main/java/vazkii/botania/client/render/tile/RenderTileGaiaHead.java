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
import com.mojang.authlib.minecraft.MinecraftProfileTexture;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;

import net.minecraft.block.BlockState;
import net.minecraft.block.SkullBlock;
import net.minecraft.block.WallSkullBlock;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.model.GenericHeadModel;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.tileentity.SkullTileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.entity.Entity;
import net.minecraft.entity.boss.WitherEntity;
import net.minecraft.entity.boss.dragon.EnderDragonEntity;
import net.minecraft.entity.monster.CreeperEntity;
import net.minecraft.entity.monster.SkeletonEntity;
import net.minecraft.entity.monster.WitherSkeletonEntity;
import net.minecraft.entity.monster.ZombieEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.tileentity.SkullTileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

import vazkii.botania.client.core.helper.ShaderHelper;
import vazkii.botania.client.core.helper.ShaderWrappedRenderLayer;
import vazkii.botania.client.render.entity.RenderDoppleganger;

import javax.annotation.Nullable;

import java.util.Map;

public class RenderTileGaiaHead extends SkullTileEntityRenderer {
	private static final Map<SkullBlock.ISkullType, GenericHeadModel> MODELS = ObfuscationReflectionHelper.getPrivateValue(SkullTileEntityRenderer.class, null, "field_199358_e");
	private static final Map<SkullBlock.ISkullType, ResourceLocation> SKINS = ObfuscationReflectionHelper.getPrivateValue(SkullTileEntityRenderer.class, null, "field_199357_d");

	public RenderTileGaiaHead(TileEntityRendererDispatcher manager) {
		super(manager);
	}

	@Override
	public void render(@Nullable SkullTileEntity skull, float partialTicks, MatrixStack ms, IRenderTypeBuffer buffers, int light, int overlay) {
		// [VanillaCopy] super, but null safe and call our own render method with appropriate overridden type and profile
		float animateProgress = 0;
		float angle = 180;
		Direction direction = null;

		if (skull != null) {
			animateProgress = skull.getAnimationProgress(partialTicks);
			BlockState blockstate = skull.getBlockState();
			boolean flag = blockstate.getBlock() instanceof WallSkullBlock;
			direction = flag ? blockstate.get(WallSkullBlock.FACING) : null;
			angle = 22.5F * (float)(flag ? (2 + direction.getHorizontalIndex()) * 4 : blockstate.get(SkullBlock.ROTATION));
		}

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

		gaiaRender(direction, angle, type, profile, animateProgress, ms, buffers, light);
	}

	// [VanillaCopy] super, but calling our own method to get RenderType
	private static void gaiaRender(@Nullable Direction facing, float rotation, SkullBlock.ISkullType type, @Nullable GameProfile profile, float partialTicks, MatrixStack ms, IRenderTypeBuffer buffers, int light) {
		GenericHeadModel genericheadmodel = MODELS.get(type);
		ms.push();
		if (facing == null) {
			ms.translate(0.5D, 0.0D, 0.5D);
		} else {
			switch (facing) {
			case NORTH:
				ms.translate(0.5D, 0.25D, (double) 0.74F);
				break;
			case SOUTH:
				ms.translate(0.5D, 0.25D, (double) 0.26F);
				break;
			case WEST:
				ms.translate((double) 0.74F, 0.25D, 0.5D);
				break;
			case EAST:
			default:
				ms.translate((double) 0.26F, 0.25D, 0.5D);
			}
		}

		ms.scale(-1.0F, -1.0F, 1.0F);
		IVertexBuilder ivertexbuilder = buffers.getBuffer(layerFor(type, profile));
		genericheadmodel.render(partialTicks, rotation, 0.0F);
		genericheadmodel.render(ms, ivertexbuilder, light, OverlayTexture.DEFAULT_UV, 1.0F, 1.0F, 1.0F, 1.0F);
		ms.pop();
	}

	// [VanillaCopy] super but wrapped in a shader
	private static RenderType layerFor(SkullBlock.ISkullType type, @Nullable GameProfile profile) {
		RenderType base;
		ResourceLocation resourcelocation = SKINS.get(type);
		if (type == SkullBlock.Types.PLAYER && profile != null) {
			Minecraft minecraft = Minecraft.getInstance();
			Map<MinecraftProfileTexture.Type, MinecraftProfileTexture> map = minecraft.getSkinManager().loadSkinFromCache(profile);
			base = map.containsKey(MinecraftProfileTexture.Type.SKIN) ? RenderType.getEntityTranslucent(minecraft.getSkinManager().loadSkin(map.get(MinecraftProfileTexture.Type.SKIN), MinecraftProfileTexture.Type.SKIN)) : RenderType.getEntityCutoutNoCull(DefaultPlayerSkin.getDefaultSkin(PlayerEntity.getUUID(profile)));
		} else {
			base = RenderType.getEntityCutoutNoCull(resourcelocation);
		}

		return ShaderHelper.useShaders()
						? new ShaderWrappedRenderLayer(ShaderHelper.BotaniaShader.DOPPLEGANGER, RenderDoppleganger.defaultCallback, base)
						: base;
	}
}
