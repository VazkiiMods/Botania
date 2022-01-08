/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.client.render.tile;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Vector3f;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

import vazkii.botania.api.item.IAvatarWieldable;
import vazkii.botania.client.core.handler.ClientTickHandler;
import vazkii.botania.client.lib.LibResources;
import vazkii.botania.client.model.ModModelLayers;
import vazkii.botania.client.model.ModelAvatar;
import vazkii.botania.common.block.tile.TileAvatar;
import vazkii.botania.xplat.IXplatAbstractions;

import javax.annotation.Nullable;

public class RenderTileAvatar implements BlockEntityRenderer<TileAvatar> {

	private static final float[] ROTATIONS = new float[] {
			180F, 0F, 90F, 270F
	};

	private static final ResourceLocation texture = new ResourceLocation(LibResources.MODEL_AVATAR);
	private final ModelAvatar model;

	public RenderTileAvatar(BlockEntityRendererProvider.Context context) {
		model = new ModelAvatar(context.bakeLayer(ModModelLayers.AVATAR));
	}

	@Override
	public void render(@Nullable TileAvatar avatar, float pticks, PoseStack ms, MultiBufferSource buffers, int light, int overlay) {
		ms.pushPose();
		Direction facing = avatar != null && avatar.getLevel() != null ? avatar.getBlockState().getValue(BlockStateProperties.HORIZONTAL_FACING) : Direction.SOUTH;

		ms.translate(0.5F, 1.6F, 0.5F);
		ms.scale(1F, -1F, -1F);
		ms.mulPose(Vector3f.YP.rotationDegrees(ROTATIONS[Math.max(Math.min(ROTATIONS.length - 1, facing.get3DDataValue() - 2), 0)]));
		VertexConsumer buffer = buffers.getBuffer(model.renderType(texture));
		model.renderToBuffer(ms, buffer, light, overlay, 1, 1, 1, 1);

		if (avatar != null) {
			ItemStack stack = avatar.getItemHandler().getItem(0);
			if (!stack.isEmpty()) {
				ms.pushPose();
				float s = 0.6F;
				ms.scale(s, s, s);
				ms.translate(-0.5F, 2F, -0.25F);
				ms.mulPose(Vector3f.XP.rotationDegrees(-70));
				Minecraft.getInstance().getItemRenderer().renderStatic(stack, ItemTransforms.TransformType.THIRD_PERSON_RIGHT_HAND,
						light, overlay, ms, buffers, 0);
				ms.popPose();

				IAvatarWieldable wieldable = IXplatAbstractions.INSTANCE.findAvatarWieldable(stack);
				buffer = buffers.getBuffer(RenderType.entityTranslucent(wieldable.getOverlayResource(avatar)));
				s = 1.01F;

				ms.pushPose();
				ms.scale(s, s, s);
				ms.translate(0F, -0.01F, 0F);
				float alpha = (float) Math.sin(ClientTickHandler.ticksInGame / 20D) / 2F + 0.5F;
				model.renderToBuffer(ms, buffer, 0xF000F0, overlay, 1, 1, 1, alpha);
				ms.popPose();
			}
		}
		ms.popPose();
	}

}
