/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.client.render.block_entity;

import com.mojang.authlib.GameProfile;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.SkullModelBase;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.SkullBlockRenderer;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.boss.wither.WitherBoss;
import net.minecraft.world.entity.monster.AbstractSkeleton;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.monster.WitherSkeleton;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.monster.piglin.AbstractPiglin;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.component.ResolvableProfile;
import net.minecraft.world.level.block.AbstractSkullBlock;
import net.minecraft.world.level.block.SkullBlock;
import net.minecraft.world.level.block.WallSkullBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.RotationSegment;

import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import vazkii.botania.client.core.helper.CoreShaders;
import vazkii.botania.client.render.entity.GaiaGuardianRenderer;
import vazkii.botania.common.block.BotaniaBlocks;
import vazkii.botania.common.block.block_entity.GaiaHeadBlockEntity;

import java.util.HashMap;
import java.util.Map;
import java.util.WeakHashMap;

public class GaiaHeadBlockEntityRenderer implements BlockEntityRenderer<GaiaHeadBlockEntity> {
	public static final Map<SkullBlock.Type, SkullModelBase> models = new HashMap<>();
	private static final WeakHashMap<GameProfile, ResolvableProfile> resolvedProfiles = new WeakHashMap<>();

	public GaiaHeadBlockEntityRenderer(BlockEntityRendererProvider.Context ctx) {
		models.putAll(SkullBlockRenderer.createSkullRenderers(ctx.getModelSet()));
	}

	public static SkullBlock.Type getViewType() {
		Entity view = Minecraft.getInstance().getCameraEntity();

		if (view instanceof WitherSkeleton || view instanceof WitherBoss) {
			return SkullBlock.Types.WITHER_SKELETON;
		}
		if (view instanceof AbstractSkeleton) {
			return SkullBlock.Types.SKELETON;
		}
		if (view instanceof Zombie) {
			return SkullBlock.Types.ZOMBIE;
		}
		if (view instanceof Creeper) {
			return SkullBlock.Types.CREEPER;
		}
		if (view instanceof AbstractPiglin) {
			return SkullBlock.Types.PIGLIN;
		}
		if (view instanceof EnderDragon) {
			return SkullBlock.Types.DRAGON;
		}
		return SkullBlock.Types.PLAYER;
	}

	// [VanillaCopy] SkullBlockRenderer::render
	@Override
	public void render(@Nullable GaiaHeadBlockEntity blockEntity, float partialTick, PoseStack poseStack,
			MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
		// Botania: block entity can be null when rendering the item form
		float animationProgress = blockEntity != null ? blockEntity.getAnimation(partialTick) : 0;
		BlockState blockstate = blockEntity != null ? blockEntity.getBlockState() : BotaniaBlocks.gaiaHead.defaultBlockState();
		boolean isWallSkull = blockstate.getBlock() instanceof WallSkullBlock;
		Direction facing = isWallSkull ? blockstate.getValue(WallSkullBlock.FACING) : null;
		int segment = blockEntity == null ? 8 : isWallSkull
				? RotationSegment.convertToSegment(facing.getOpposite())
				: blockstate.getValue(SkullBlock.ROTATION);
		float yRot = RotationSegment.convertToDegrees(segment);
		SkullBlock.Type skullblock$Type = ((AbstractSkullBlock) blockstate.getBlock()).getType();
		SkullModelBase skullmodelbase = models.get(skullblock$Type);
		// Botania: using spectated player or local player as reference skin
		AbstractClientPlayer viewingPlayer = GaiaGuardianRenderer.getViewingPlayer();
		RenderType rendertype = SkullBlockRenderer.getRenderType(skullblock$Type, viewingPlayer != null
				? resolvedProfiles.computeIfAbsent(viewingPlayer.getGameProfile(), ResolvableProfile::new)
				: null);
		SkullBlockRenderer.renderSkull(facing, yRot, animationProgress, poseStack, bufferSource, packedLight, skullmodelbase, rendertype);
	}

	public static void hookGetRenderType(CallbackInfoReturnable<RenderType> cir) {
		SkullBlock.Type type = getViewType();
		ResolvableProfile profile = null;
		if (type == SkullBlock.Types.PLAYER && Minecraft.getInstance().getCameraEntity() instanceof Player player) {
			profile = new ResolvableProfile(player.getGameProfile());
		}

		RenderType layer = SkullBlockRenderer.getRenderType(type, profile);
		cir.setReturnValue(new WrapperLayer(layer));
	}

	private static class WrapperLayer extends RenderType {
		public WrapperLayer(RenderType compose) {
			super(compose.toString(), compose.format(), compose.mode(), compose.bufferSize(), compose.affectsCrumbling(), false,
					() -> {
						compose.setupRenderState();
						// Override the shader to ours
						ShaderInstance shader = CoreShaders.gaiaNoiseConstant();
						if (shader != null) {
							RenderSystem.setShader(CoreShaders::gaiaNoiseConstant);
						}
					}, compose::clearRenderState);
		}
	}
}
