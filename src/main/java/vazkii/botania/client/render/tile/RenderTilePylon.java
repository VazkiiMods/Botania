/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.client.render.tile;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.ItemStackTileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.ItemStack;
import net.minecraft.util.LazyValue;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3f;

import vazkii.botania.client.core.handler.ClientTickHandler;
import vazkii.botania.client.core.helper.RenderHelper;
import vazkii.botania.client.lib.LibResources;
import vazkii.botania.client.model.IPylonModel;
import vazkii.botania.client.model.ModelPylonGaia;
import vazkii.botania.client.model.ModelPylonMana;
import vazkii.botania.client.model.ModelPylonNatura;
import vazkii.botania.common.block.BlockPylon;
import vazkii.botania.common.block.tile.TilePylon;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import java.util.Random;

public class RenderTilePylon extends TileEntityRenderer<TilePylon> {

	public static final ResourceLocation MANA_TEXTURE = new ResourceLocation(LibResources.MODEL_PYLON_MANA);
	public static final ResourceLocation NATURA_TEXTURE = new ResourceLocation(LibResources.MODEL_PYLON_NATURA);
	public static final ResourceLocation GAIA_TEXTURE = new ResourceLocation(LibResources.MODEL_PYLON_GAIA);

	private final ModelPylonMana manaModel = new ModelPylonMana();
	private final ModelPylonNatura naturaModel = new ModelPylonNatura();
	private final ModelPylonGaia gaiaModel = new ModelPylonGaia();

	// Overrides for when we call this TESR without an actual pylon
	private static BlockPylon.Variant forceVariant = BlockPylon.Variant.MANA;
	private static ItemCameraTransforms.TransformType forceTransform = ItemCameraTransforms.TransformType.NONE;

	public RenderTilePylon(TileEntityRendererDispatcher manager) {
		super(manager);
	}

	@Override
	public void render(@Nonnull TilePylon pylon, float pticks, MatrixStack ms, IRenderTypeBuffer buffers, int light, int overlay) {
		renderPylon(pylon, pticks, ms, buffers, light, overlay);
	}

	private void renderPylon(@Nullable TilePylon pylon, float pticks, MatrixStack ms, IRenderTypeBuffer buffers, int light, int overlay) {
		boolean renderingItem = pylon == null;
		boolean direct = renderingItem && (forceTransform == ItemCameraTransforms.TransformType.GUI || forceTransform.func_241716_a_()); // loosely based off ItemRenderer logic
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

		ms.push();

		float worldTime = ClientTickHandler.ticksInGame + pticks;

		worldTime += pylon == null ? 0 : new Random(pylon.getPos().hashCode()).nextInt(360);

		ms.translate(0, pylon == null ? 1.35 : 1.5, 0);
		ms.scale(1.0F, -1.0F, -1.0F);

		ms.push();
		ms.translate(0.5F, 0F, -0.5F);
		if (pylon != null) {
			ms.rotate(Vector3f.YP.rotationDegrees(worldTime * 1.5F));
		}

		RenderType layer = RenderType.getEntityTranslucent(texture);

		IVertexBuilder buffer = buffers.getBuffer(layer);
		model.renderRing(ms, buffer, light, overlay);
		if (pylon != null) {
			ms.translate(0D, Math.sin(worldTime / 20D) / 20 - 0.025, 0D);
		}
		ms.pop();

		ms.push();
		if (pylon != null) {
			ms.translate(0D, Math.sin(worldTime / 20D) / 17.5, 0D);
		}

		ms.translate(0.5F, 0F, -0.5F);
		if (pylon != null) {
			ms.rotate(Vector3f.YP.rotationDegrees(-worldTime));
		}

		buffer = buffers.getBuffer(shaderLayer);
		model.renderCrystal(ms, buffer, light, overlay);

		ms.pop();

		ms.pop();
	}

	public static class TEISR extends ItemStackTileEntityRenderer {
		private static final LazyValue<TilePylon> DUMMY = new LazyValue<>(TilePylon::new);

		@Override
		public void func_239207_a_(ItemStack stack, ItemCameraTransforms.TransformType type, MatrixStack ms, IRenderTypeBuffer buffers, int light, int overlay) {
			if (Block.getBlockFromItem(stack.getItem()) instanceof BlockPylon) {
				RenderTilePylon.forceVariant = ((BlockPylon) Block.getBlockFromItem(stack.getItem())).variant;
				RenderTilePylon.forceTransform = type;
				TileEntityRenderer<TilePylon> r = TileEntityRendererDispatcher.instance.getRenderer(DUMMY.getValue());
				((RenderTilePylon) r).renderPylon(null, 0, ms, buffers, light, overlay);
			}
		}
	}
}
