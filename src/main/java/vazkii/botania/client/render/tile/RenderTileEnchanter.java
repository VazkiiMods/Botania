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
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.entity.item.ItemEntity;

import vazkii.botania.client.core.handler.ClientTickHandler;
import vazkii.botania.client.core.handler.MiscellaneousIcons;
import vazkii.botania.client.core.helper.IconHelper;
import vazkii.botania.client.core.helper.RenderHelper;
import vazkii.botania.common.block.tile.TileEnchanter;
import vazkii.botania.mixin.AccessorItemEntity;

import javax.annotation.Nonnull;

public class RenderTileEnchanter implements BlockEntityRenderer<TileEnchanter> {

	private ItemEntity item;

	public RenderTileEnchanter(BlockEntityRendererProvider.Context ctx) {}

	@Override
	public void render(@Nonnull TileEnchanter enchanter, float f, PoseStack ms, MultiBufferSource buffers, int light, int overlay) {
		float alphaMod = 0F;

		if (enchanter.stage == TileEnchanter.State.GATHER_MANA) {
			alphaMod = Math.min(20, enchanter.stageTicks) / 20F;
		} else if (enchanter.stage == TileEnchanter.State.RESET) {
			alphaMod = (20 - enchanter.stageTicks) / 20F;
		} else if (enchanter.stage == TileEnchanter.State.DO_ENCHANT) {
			alphaMod = 1F;
		}

		ms.pushPose();
		if (!enchanter.itemToEnchant.isEmpty()) {
			if (item == null) {
				item = new ItemEntity(enchanter.getLevel(), enchanter.getBlockPos().getX(), enchanter.getBlockPos().getY() + 1, enchanter.getBlockPos().getZ(), enchanter.itemToEnchant);
			}

			((AccessorItemEntity) item).setAge(ClientTickHandler.ticksInGame);
			item.setItem(enchanter.itemToEnchant);

			ms.translate(0.5F, 1.25F, 0.5F);
			Minecraft.getInstance().getEntityRenderDispatcher().render(item, 0, 0, 0, 0, f, ms, buffers, light);
			ms.translate(-0.5F, -1.25F, -0.5F);
		}

		ms.mulPose(Vector3f.XP.rotationDegrees(90F));
		ms.translate(-2F, -2F, -0.001F);

		float alpha = (float) ((Math.sin((ClientTickHandler.ticksInGame + f) / 8D) + 1D) / 5D + 0.4D) * alphaMod;

		if (alpha > 0) {
			if (enchanter.stage == TileEnchanter.State.DO_ENCHANT || enchanter.stage == TileEnchanter.State.RESET) {
				int ticks = enchanter.stageTicks + enchanter.stage3EndTicks;
				int angle = ticks * 2;
				float yTranslation = Math.min(20, ticks) / 20F * 1.15F;
				float scale = ticks < 10 ? 1F : 1F - Math.min(20, ticks - 10) / 20F * 0.75F;

				ms.translate(2.5F, 2.5F, -yTranslation);
				ms.scale(scale, scale, 1F);
				ms.mulPose(Vector3f.ZP.rotationDegrees(angle));
				ms.translate(-2.5F, -2.5F, 0F);
			}

			VertexConsumer buffer = buffers.getBuffer(RenderHelper.ENCHANTER);
			IconHelper.renderIcon(ms, buffer, 0, 0, MiscellaneousIcons.INSTANCE.enchanterOverlay.sprite(), 5, 5, alpha);
		}

		ms.popPose();
	}

}
