/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.client.render.tile;

import com.google.common.base.Suppliers;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Vector3f;

import net.fabricmc.fabric.api.client.rendering.v1.BuiltinItemRendererRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;

import vazkii.botania.client.core.handler.ClientTickHandler;
import vazkii.botania.client.core.helper.RenderHelper;
import vazkii.botania.client.lib.LibResources;
import vazkii.botania.client.model.*;
import vazkii.botania.common.block.BlockPylon;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.block.tile.TilePylon;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import java.util.Random;
import java.util.function.Supplier;

public class RenderTilePylon implements BlockEntityRenderer<TilePylon> {

	public static final ResourceLocation MANA_TEXTURE = new ResourceLocation(LibResources.MODEL_PYLON_MANA);
	public static final ResourceLocation NATURA_TEXTURE = new ResourceLocation(LibResources.MODEL_PYLON_NATURA);
	public static final ResourceLocation GAIA_TEXTURE = new ResourceLocation(LibResources.MODEL_PYLON_GAIA);

	private final ModelPylonMana manaModel;
	private final ModelPylonNatura naturaModel;
	private final ModelPylonGaia gaiaModel;

	// Overrides for when we call this without an actual pylon
	private static BlockPylon.Variant forceVariant = BlockPylon.Variant.MANA;
	private static ItemTransforms.TransformType forceTransform = ItemTransforms.TransformType.NONE;

	public RenderTilePylon(BlockEntityRendererProvider.Context ctx) {
		manaModel = new ModelPylonMana(ctx.bakeLayer(ModModelLayers.PYLON_MANA));
		naturaModel = new ModelPylonNatura(ctx.bakeLayer(ModModelLayers.PYLON_NATURA));
		gaiaModel = new ModelPylonGaia(ctx.bakeLayer(ModModelLayers.PYLON_GAIA));
	}

	@Override
	public void render(@Nonnull TilePylon pylon, float pticks, PoseStack ms, MultiBufferSource buffers, int light, int overlay) {
		renderPylon(pylon, pticks, ms, buffers, light, overlay);
	}

	private void renderPylon(@Nullable TilePylon pylon, float pticks, PoseStack ms, MultiBufferSource buffers, int light, int overlay) {
		boolean renderingItem = pylon == null;
		boolean direct = renderingItem && (forceTransform == ItemTransforms.TransformType.GUI || forceTransform.firstPerson()); // loosely based off ItemRenderer logic
		BlockPylon.Variant type = renderingItem ? forceVariant : ((BlockPylon) pylon.getBlockState().getBlock()).variant;
		IPylonModel model;
		ResourceLocation texture;
		RenderType shaderLayer;
		switch (type) {
		default:
		case MANA: {
			model = manaModel;
			texture = MANA_TEXTURE;
			shaderLayer = direct ? RenderHelper.MANA_PYLON_GLOW_DIRECT : RenderHelper.MANA_PYLON_GLOW;
			break;
		}
		case NATURA: {
			model = naturaModel;
			texture = NATURA_TEXTURE;
			shaderLayer = direct ? RenderHelper.NATURA_PYLON_GLOW_DIRECT : RenderHelper.NATURA_PYLON_GLOW;
			break;
		}
		case GAIA: {
			model = gaiaModel;
			texture = GAIA_TEXTURE;
			shaderLayer = direct ? RenderHelper.GAIA_PYLON_GLOW_DIRECT : RenderHelper.GAIA_PYLON_GLOW;
			break;
		}
		}

		ms.pushPose();

		float worldTime = ClientTickHandler.ticksInGame + pticks;

		worldTime += pylon == null ? 0 : new Random(pylon.getBlockPos().hashCode()).nextInt(360);

		ms.translate(0, pylon == null ? 1.35 : 1.5, 0);
		ms.scale(1.0F, -1.0F, -1.0F);

		ms.pushPose();
		ms.translate(0.5F, 0F, -0.5F);
		if (pylon != null) {
			ms.mulPose(Vector3f.YP.rotationDegrees(worldTime * 1.5F));
		}

		RenderType layer = RenderType.entityTranslucent(texture);

		VertexConsumer buffer = buffers.getBuffer(layer);
		model.renderRing(ms, buffer, light, overlay);
		if (pylon != null) {
			ms.translate(0D, Math.sin(worldTime / 20D) / 20 - 0.025, 0D);
		}
		ms.popPose();

		ms.pushPose();
		if (pylon != null) {
			ms.translate(0D, Math.sin(worldTime / 20D) / 17.5, 0D);
		}

		ms.translate(0.5F, 0F, -0.5F);
		if (pylon != null) {
			ms.mulPose(Vector3f.YP.rotationDegrees(-worldTime));
		}

		buffer = buffers.getBuffer(shaderLayer);
		model.renderCrystal(ms, buffer, light, overlay);

		ms.popPose();

		ms.popPose();
	}

	public static class TEISR implements BuiltinItemRendererRegistry.DynamicItemRenderer {
		private static final Supplier<TilePylon> DUMMY = Suppliers.memoize(() -> new TilePylon(new BlockPos(0, Integer.MIN_VALUE, 0), ModBlocks.manaPylon.defaultBlockState()));

		@Override
		public void render(ItemStack stack, ItemTransforms.TransformType type, PoseStack ms, MultiBufferSource buffers, int light, int overlay) {
			if (Block.byItem(stack.getItem()) instanceof BlockPylon) {
				RenderTilePylon.forceVariant = ((BlockPylon) Block.byItem(stack.getItem())).variant;
				RenderTilePylon.forceTransform = type;
				BlockEntityRenderer<TilePylon> r = Minecraft.getInstance().getBlockEntityRenderDispatcher().getRenderer(DUMMY.get());
				if (r instanceof RenderTilePylon) {
					((RenderTilePylon) r).renderPylon(null, 0, ms, buffers, light, overlay);
				}
			}
		}
	}
}
