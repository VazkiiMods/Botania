/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.client.render.tile;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.item.ItemStack;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;
import vazkii.botania.api.item.IAvatarWieldable;
import vazkii.botania.client.core.handler.ClientTickHandler;
import vazkii.botania.client.lib.LibResources;
import vazkii.botania.client.model.ModelAvatar;
import vazkii.botania.common.block.tile.TileAvatar;

import javax.annotation.Nullable;

public class RenderTileAvatar extends BlockEntityRenderer<TileAvatar> {

	private static final float[] ROTATIONS = new float[] {
			180F, 0F, 90F, 270F
	};

	private static final Identifier texture = new Identifier(LibResources.MODEL_AVATAR);
	private static final ModelAvatar model = new ModelAvatar();

	public RenderTileAvatar(BlockEntityRenderDispatcher manager) {
		super(manager);
	}

	@Override
	public void render(@Nullable TileAvatar avatar, float pticks, MatrixStack ms, VertexConsumerProvider buffers, int light, int overlay) {
		ms.push();
		Direction facing = avatar != null && avatar.getWorld() != null ? avatar.getCachedState().get(Properties.HORIZONTAL_FACING) : Direction.SOUTH;

		ms.translate(0.5F, 1.6F, 0.5F);
		ms.scale(1F, -1F, -1F);
		ms.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion(ROTATIONS[Math.max(Math.min(ROTATIONS.length - 1, facing.getId() - 2), 0)]));
		VertexConsumer buffer = buffers.getBuffer(model.getLayer(texture));
		model.render(ms, buffer, light, overlay, 1, 1, 1, 1);

		if (avatar != null) {
			ItemStack stack = avatar.getItemHandler().getStack(0);
			if (!stack.isEmpty()) {
				ms.push();
				float s = 0.6F;
				ms.scale(s, s, s);
				ms.translate(-0.5F, 2F, -0.25F);
				ms.multiply(Vector3f.POSITIVE_X.getDegreesQuaternion(-70));
				MinecraftClient.getInstance().getItemRenderer().renderItem(stack, ModelTransformation.Mode.THIRD_PERSON_RIGHT_HAND, light, overlay, ms, buffers);
				ms.pop();

				IAvatarWieldable wieldable = (IAvatarWieldable) stack.getItem();
				buffer = buffers.getBuffer(RenderLayer.getEntityTranslucent(wieldable.getOverlayResource(avatar, stack)));
				s = 1.01F;

				ms.push();
				ms.scale(s, s, s);
				ms.translate(0F, -0.01F, 0F);
				float alpha = (float) Math.sin(ClientTickHandler.ticksInGame / 20D) / 2F + 0.5F;
				model.render(ms, buffer, 0xF000F0, overlay, 1, 1, 1, alpha + 0.183F);
				ms.pop();
			}
		}
		ms.pop();
	}

}
