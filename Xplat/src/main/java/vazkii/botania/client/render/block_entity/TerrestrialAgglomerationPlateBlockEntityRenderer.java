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
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.world.inventory.InventoryMenu;

import org.joml.Matrix4f;
import org.joml.Vector3f;

import vazkii.botania.client.core.handler.ClientTickHandler;
import vazkii.botania.common.block.block_entity.TerrestrialAgglomerationPlateBlockEntity;

import java.util.Objects;

import static vazkii.botania.api.BotaniaAPI.botaniaRL;

public class TerrestrialAgglomerationPlateBlockEntityRenderer implements BlockEntityRenderer<TerrestrialAgglomerationPlateBlockEntity> {
	private final TextureAtlasSprite overlaySprite;

	public TerrestrialAgglomerationPlateBlockEntityRenderer(BlockEntityRendererProvider.Context manager) {
		this.overlaySprite = Objects.requireNonNull(
				Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS)
						.apply(botaniaRL("block/terra_plate_overlay"))
		);
	}

	@Override
	public void render(TerrestrialAgglomerationPlateBlockEntity plate, float f, PoseStack ms, MultiBufferSource buffers, int light, int overlay) {
		float alphaMod = Math.min(1.0F, plate.getCompletion() / 0.1F);
		float alpha = (float) ((Math.sin((ClientTickHandler.ticksInGame + f) / 8D) + 1D) / 5D + 0.6D) * alphaMod;

		ms.pushPose();
		ms.translate(0.5F, 3F / 16F + 0.001F, 0.5F);
		ms.scale(0.999F, 0.999F, 0.999F);

		Matrix4f pose = ms.last().pose();
		VertexConsumer buffer = buffers.getBuffer(RenderType.entityTranslucent(overlaySprite.atlasLocation()));

		float size = 1.0F;
		float halfSize = size / 2.0F;

		float x0 = -halfSize;
		float x1 = halfSize;
		float z0 = -halfSize;
		float z1 = halfSize;

		float u0 = overlaySprite.getU0();
		float u1 = overlaySprite.getU1();
		float v0 = overlaySprite.getV0();
		float v1 = overlaySprite.getV1();

		Vector3f normalVec = new Vector3f(0, 1, 0);

		int fullbright = LightTexture.FULL_BRIGHT;

		buffer.addVertex(pose, x0, 0, z0).setColor(1, 1, 1, alpha).setUv(u0, v0).setOverlay(overlay).setLight(fullbright).setNormal(normalVec.x(), normalVec.y(), normalVec.z());
		buffer.addVertex(pose, x0, 0, z1).setColor(1, 1, 1, alpha).setUv(u0, v1).setOverlay(overlay).setLight(fullbright).setNormal(normalVec.x(), normalVec.y(), normalVec.z());
		buffer.addVertex(pose, x1, 0, z1).setColor(1, 1, 1, alpha).setUv(u1, v1).setOverlay(overlay).setLight(fullbright).setNormal(normalVec.x(), normalVec.y(), normalVec.z());
		buffer.addVertex(pose, x1, 0, z0).setColor(1, 1, 1, alpha).setUv(u1, v0).setOverlay(overlay).setLight(fullbright).setNormal(normalVec.x(), normalVec.y(), normalVec.z());

		ms.popPose();
	}

}
