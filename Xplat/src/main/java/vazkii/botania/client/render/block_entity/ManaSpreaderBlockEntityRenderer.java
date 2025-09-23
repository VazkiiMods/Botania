/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.client.render.block_entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FastColor;
import net.minecraft.util.Mth;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

import org.joml.Quaternionf;

import vazkii.botania.api.state.BotaniaStateProperties;
import vazkii.botania.client.core.handler.ClientTickHandler;
import vazkii.botania.common.block.block_entity.mana.ManaSpreaderBlockEntity;
import vazkii.botania.common.block.mana.ManaSpreaderBlock;
import vazkii.botania.common.helper.ColorHelper;
import vazkii.botania.common.helper.VecHelper;
import vazkii.botania.xplat.ClientXplatAbstractions;

import java.util.EnumMap;
import java.util.Map;
import java.util.Objects;
import java.util.Random;
import java.util.function.Function;
import java.util.stream.Collectors;

import static vazkii.botania.api.BotaniaAPI.botaniaRL;

public class ManaSpreaderBlockEntityRenderer implements BlockEntityRenderer<ManaSpreaderBlockEntity> {
	public static final Map<DyeColor, ResourceLocation> WOOL_PADDING_IDS = new EnumMap<>(
			ColorHelper.supportedColors().collect(Collectors.toMap(Function.identity(),
					color -> botaniaRL("block/" + color.getSerializedName() + "_spreader_padding"))));

	private final BlockRenderDispatcher blockRenderDispatcher;

	public ManaSpreaderBlockEntityRenderer(BlockEntityRendererProvider.Context ctx) {
		this.blockRenderDispatcher = ctx.getBlockRenderDispatcher();
	}

	@Override
	public void render(ManaSpreaderBlockEntity spreader, float partialTick, PoseStack ms, MultiBufferSource buffers, int light, int overlay) {
		ms.pushPose();

		ms.translate(0.5F, 0.5, 0.5F);

		Quaternionf transform = VecHelper.rotateY(spreader.rotationX + 90F);
		transform.mul(VecHelper.rotateX(spreader.rotationY));
		ms.mulPose(transform);

		ms.translate(-0.5F, -0.5F, -0.5F);

		float time = ClientTickHandler.getEntityTicksInGame() + partialTick + new Random(spreader.getBlockPos().asLong()).nextFloat() * 360;

		float r = 1, g = 1, b = 1;
		ManaSpreaderBlock spreaderBlock = spreader.getSpreaderBlock();
		if (spreaderBlock.isRainbowRendered()) {
			int color = Mth.hsvToRgb((time % 180) / 180, 0.4F, 0.9F);
			r = FastColor.ARGB32.red(color) / 255F;
			g = FastColor.ARGB32.green(color) / 255F;
			b = FastColor.ARGB32.blue(color) / 255F;
		}

		VertexConsumer buffer = buffers.getBuffer(ItemBlockRenderTypes.getRenderType(spreader.getBlockState(), false));
		BakedModel spreaderModel = blockRenderDispatcher.getBlockModel(spreader.getBlockState());
		blockRenderDispatcher.getModelRenderer()
				.renderModel(ms.last(), buffer, spreader.getBlockState(),
						spreaderModel, r, g, b, light, overlay);

		ms.pushPose();
		ms.translate(0.5, 0.5, 0.5);
		ms.mulPose(VecHelper.rotateY(time % 360));
		ms.translate(-0.5, -0.5, -0.5);
		ms.translate(0F, Mth.sin(time / 20.0f) * 0.05F, 0F);
		BakedModel core = getCoreModel(spreader);
		blockRenderDispatcher.getModelRenderer()
				.renderModel(ms.last(), buffer, spreader.getBlockState(),
						core, 1, 1, 1, light, overlay);
		ms.popPose();

		ItemStack stack = spreader.getItemHandler().getItem(0);
		if (!stack.isEmpty()) {
			ms.pushPose();
			ms.translate(0.5F, 0.5F, 0.094F);
			ms.mulPose(VecHelper.rotateZ(180));
			ms.mulPose(VecHelper.rotateX(180));
			// Prevents z-fighting. Otherwise not noticeable.
			ms.scale(0.997F, 0.997F, 1F);
			Minecraft.getInstance().getItemRenderer().renderStatic(stack, ItemDisplayContext.NONE,
					light, overlay, ms, buffers, spreader.getLevel(), 0);
			ms.popPose();
		}

		if (spreaderBlock.isRainbowRendered() && spreaderBlock.getOptionalColor().isPresent()) {
			ms.pushPose();
			// The padding model is facing up so that the textures are rotated the correct way
			// It's simpler to do this than mess with rotation and UV in the json model
			ms.translate(0.5F, 0.5F, 0.5F);
			ms.mulPose(VecHelper.rotateX(-90));
			ms.mulPose(VecHelper.rotateY(180));
			ms.translate(-0.5F, -0.5F, -0.5F);
			BakedModel paddingModel = getPaddingModel(spreaderBlock.getOptionalColor().get());
			blockRenderDispatcher.getModelRenderer()
					.renderModel(ms.last(), buffer, spreader.getBlockState(),
							paddingModel, r, g, b, light, overlay);
			ms.popPose();
		}

		ms.popPose();

		if (spreader.getBlockState().getValue(BotaniaStateProperties.HAS_SCAFFOLDING)) {
			BakedModel scaffolding = getScaffoldingModel(spreader);
			blockRenderDispatcher.getModelRenderer()
					.renderModel(ms.last(), buffer, spreader.getBlockState(),
							scaffolding, r, g, b, light, overlay);
		}

	}

	private BakedModel getCoreModel(ManaSpreaderBlockEntity tile) {
		ResourceLocation coreModelId = tile.getSpreaderBlock().getCoreModelId();
		return Objects.requireNonNull(ClientXplatAbstractions.instance().getResourceModel(coreModelId));
	}

	private BakedModel getPaddingModel(DyeColor color) {
		ResourceLocation paddingId = WOOL_PADDING_IDS.get(color);
		return Objects.requireNonNull(ClientXplatAbstractions.instance().getResourceModel(paddingId));
	}

	private BakedModel getScaffoldingModel(ManaSpreaderBlockEntity tile) {
		ResourceLocation scaffoldingModelId = tile.getSpreaderBlock().getScaffoldingModelId();
		return Objects.requireNonNull(ClientXplatAbstractions.instance().getResourceModel(scaffoldingModelId));
	}
}
