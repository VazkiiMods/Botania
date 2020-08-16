/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.client.render.tile;

import net.fabricmc.fabric.api.client.rendering.v1.BuiltinItemRendererRegistry;
import net.minecraft.block.Block;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.item.BuiltinModelItemRenderer;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.Lazy;
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

public class RenderTilePylon extends BlockEntityRenderer<TilePylon> {

	public static final Identifier MANA_TEXTURE = new Identifier(LibResources.MODEL_PYLON_MANA);
	public static final Identifier NATURA_TEXTURE = new Identifier(LibResources.MODEL_PYLON_NATURA);
	public static final Identifier GAIA_TEXTURE = new Identifier(LibResources.MODEL_PYLON_GAIA);

	private final ModelPylonMana manaModel = new ModelPylonMana();
	private final ModelPylonNatura naturaModel = new ModelPylonNatura();
	private final ModelPylonGaia gaiaModel = new ModelPylonGaia();

	// Overrides for when we call this TESR without an actual pylon
	private static BlockPylon.Variant forceVariant = BlockPylon.Variant.MANA;
	private static ModelTransformation.Mode forceTransform = ModelTransformation.Mode.NONE;

	public RenderTilePylon(BlockEntityRenderDispatcher manager) {
		super(manager);
	}

	@Override
	public void render(@Nonnull TilePylon pylon, float pticks, MatrixStack ms, VertexConsumerProvider buffers, int light, int overlay) {
		renderPylon(pylon, pticks, ms, buffers, light, overlay);
	}

	private void renderPylon(@Nullable TilePylon pylon, float pticks, MatrixStack ms, VertexConsumerProvider buffers, int light, int overlay) {
		boolean renderingItem = pylon == null;
		boolean direct = renderingItem && (forceTransform == ModelTransformation.Mode.GUI || forceTransform.isFirstPerson()); // loosely based off ItemRenderer logic
		BlockPylon.Variant type = renderingItem ? forceVariant : ((BlockPylon) pylon.getCachedState().getBlock()).variant;
		IPylonModel model;
		Identifier texture;
		RenderLayer shaderLayer;
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
			ms.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion(worldTime * 1.5F));
		}

		RenderLayer layer = RenderLayer.getEntityTranslucent(texture);

		VertexConsumer buffer = buffers.getBuffer(layer);
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
			ms.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion(-worldTime));
		}

		buffer = buffers.getBuffer(shaderLayer);
		model.renderCrystal(ms, buffer, light, overlay);

		ms.pop();

		ms.pop();
	}

	public static class TEISR implements BuiltinItemRendererRegistry.DynamicItemRenderer {
		private static final Lazy<TilePylon> DUMMY = new Lazy<>(TilePylon::new);

		@Override
		public void render(ItemStack stack, ModelTransformation.Mode type, MatrixStack ms, VertexConsumerProvider buffers, int light, int overlay) {
			if (Block.getBlockFromItem(stack.getItem()) instanceof BlockPylon) {
				RenderTilePylon.forceVariant = ((BlockPylon) Block.getBlockFromItem(stack.getItem())).variant;
				RenderTilePylon.forceTransform = type;
				BlockEntityRenderer<TilePylon> r = BlockEntityRenderDispatcher.INSTANCE.get(DUMMY.get());
				if (r instanceof RenderTilePylon) {
					((RenderTilePylon) r).renderPylon(null, 0, ms, buffers, light, overlay);
				}
			}
		}
	}
}
