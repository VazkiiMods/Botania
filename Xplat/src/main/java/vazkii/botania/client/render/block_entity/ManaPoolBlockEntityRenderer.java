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
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Direction;
import net.minecraft.util.FastColor;
import net.minecraft.util.Mth;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.VoxelShape;

import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.UnknownNullability;

import vazkii.botania.api.mana.PoolOverlayProvider;
import vazkii.botania.client.core.handler.ClientTickHandler;
import vazkii.botania.client.core.helper.RenderHelper;
import vazkii.botania.common.block.block_entity.mana.ManaPoolBlockEntity;
import vazkii.botania.common.block.mana.ManaPoolBlock;
import vazkii.botania.common.helper.VecHelper;

import java.util.Objects;

import static vazkii.botania.api.BotaniaAPI.botaniaRL;

public class ManaPoolBlockEntityRenderer implements BlockEntityRenderer<ManaPoolBlockEntity> {

	// Overrides for when we call this renderer from a cart
	public static int cartMana = -1;
	public static int cartMaxMana = -1;
	@UnknownNullability
	public static ManaPoolBlock cartBlock;

	private final TextureAtlasSprite waterSprite;
	private final BlockRenderDispatcher blockRenderDispatcher;

	public ManaPoolBlockEntityRenderer(BlockEntityRendererProvider.Context ctx) {
		this.blockRenderDispatcher = ctx.getBlockRenderDispatcher();
		this.waterSprite = Objects.requireNonNull(
				Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS)
						.apply(botaniaRL("block/mana_water"))
		);
	}

	@Override
	public void render(@Nullable ManaPoolBlockEntity pool, float partialTick, PoseStack ms, MultiBufferSource buffers, int light, int overlay) {
		ms.pushPose();
		Minecraft minecraft = Minecraft.getInstance();

		boolean fabulous;
		VoxelShape innerShape;
		VoxelShape outerShape;
		int mana;
		int maxMana;
		BlockState state;
		Level level;

		if (pool != null) {
			level = pool.getLevel() != null ? pool.getLevel() : minecraft.level;
			state = pool.getBlockState();
			ManaPoolBlock block = (ManaPoolBlock) state.getBlock();
			fabulous = block.isFabulous();
			innerShape = block.getInnerShape(pool.getBlockState());
			outerShape = block.getInteractionShape(pool.getBlockState(), pool.getLevel(), pool.getBlockPos());
			mana = pool.getCurrentMana();
			maxMana = pool.getMaxMana();
		} else {
			level = minecraft.level;
			ManaPoolBlock block = cartBlock;
			state = block.defaultBlockState();
			fabulous = block.isFabulous();
			innerShape = block.getInnerShape(block.defaultBlockState());
			outerShape = block.getInteractionShape(block.defaultBlockState(), null, null);
			mana = cartMana;
			maxMana = cartMaxMana == -1 ? ManaPoolBlock.MAX_MANA : cartMaxMana;
		}

		float poolBottom = (float) innerShape.min(Direction.Axis.Y) + 0.001F;
		float poolTop = (float) outerShape.max(Direction.Axis.Y) - 1 / 16f;
		int uvStartX = (int) (16 * innerShape.min(Direction.Axis.X));
		int uvStartY = (int) (16 * innerShape.min(Direction.Axis.Z));
		int uvEndX = (int) (16 * innerShape.max(Direction.Axis.X));
		int uvEndY = (int) (16 * innerShape.max(Direction.Axis.Z));

		if (fabulous) {
			int color = minecraft.getBlockColors().getColor(state, level, pool != null ? pool.getBlockPos() : null, 0);

			float red = FastColor.ARGB32.red(color) / 255f;
			float green = FastColor.ARGB32.green(color) / 255f;
			float blue = FastColor.ARGB32.blue(color) / 255f;
			BakedModel model = blockRenderDispatcher.getBlockModel(state);
			VertexConsumer buffer = buffers.getBuffer(ItemBlockRenderTypes.getRenderType(state, false));
			blockRenderDispatcher.getModelRenderer()
					.renderModel(ms.last(), buffer, state, model, red, green, blue, light, overlay);
		}

		if (pool != null) {
			Block below = pool.getLevel().getBlockState(pool.getBlockPos().below()).getBlock();
			if (below instanceof PoolOverlayProvider overlayProvider) {
				var overlaySpriteId = overlayProvider.getIcon(pool.getLevel(), pool.getBlockPos());
				var overlayIcon = minecraft.getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(overlaySpriteId);
				ms.pushPose();

				float alpha = (float) ((Mth.sin((ClientTickHandler.getEntityTicksInGame() + partialTick) / 20.0f) + 1) * 0.3 + 0.2);

				ms.translate(0, poolBottom, 0);
				ms.mulPose(VecHelper.rotateX(90F));

				VertexConsumer buffer = buffers.getBuffer(RenderHelper.ICON_OVERLAY);
				RenderHelper.renderIconCropped(
						ms, buffer,
						uvStartX, uvStartY, uvEndX, uvEndY,
						overlayIcon, 0xFFFFFF, alpha, light
				);

				ms.popPose();
			}
		}

		float manaLevel = (float) mana / (float) maxMana;
		if (manaLevel > 0) {
			ms.pushPose();
			ms.translate(0, Mth.clampedMap(manaLevel, 0, 1, poolBottom, poolTop), 0);
			ms.mulPose(VecHelper.rotateX(90F));

			VertexConsumer buffer = buffers.getBuffer(RenderHelper.MANA_POOL_WATER);
			RenderHelper.renderIconCropped(
					ms, buffer,
					uvStartX, uvStartY, uvEndX, uvEndY,
					this.waterSprite, 0xFFFFFF, 1, light);

			ms.popPose();
		}
		ms.popPose();

		cartMana = -1;
		cartMaxMana = -1;
		cartBlock = null;
	}

}
