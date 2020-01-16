/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Jan 25, 2014, 9:42:31 PM (GMT)]
 */
package vazkii.botania.client.render.tile;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.client.renderer.Vector3f;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import vazkii.botania.api.ColorHelper;
import vazkii.botania.client.core.handler.ClientTickHandler;
import vazkii.botania.client.core.proxy.ClientProxy;
import vazkii.botania.client.lib.LibResources;
import vazkii.botania.client.model.ModelSpreader;
import vazkii.botania.common.block.tile.mana.TileSpreader;

import javax.annotation.Nonnull;
import java.awt.*;
import java.util.Random;

public class RenderTileSpreader extends TileEntityRenderer<TileSpreader> {

	private static final ResourceLocation texture = new ResourceLocation(LibResources.MODEL_SPREADER);
	private static final ResourceLocation textureRs = new ResourceLocation(LibResources.MODEL_SPREADER_REDSTONE);
	private static final ResourceLocation textureDw = new ResourceLocation(LibResources.MODEL_SPREADER_DREAMWOOD);
	private static final ResourceLocation textureG = new ResourceLocation(LibResources.MODEL_SPREADER_GAIA);
	
	private static final ResourceLocation textureHalloween = new ResourceLocation(LibResources.MODEL_SPREADER_HALLOWEEN);
	private static final ResourceLocation textureRsHalloween = new ResourceLocation(LibResources.MODEL_SPREADER_REDSTONE_HALLOWEEN);
	private static final ResourceLocation textureDwHalloween = new ResourceLocation(LibResources.MODEL_SPREADER_DREAMWOOD_HALLOWEEN);
	private static final ResourceLocation textureGHalloween = new ResourceLocation(LibResources.MODEL_SPREADER_GAIA_HALLOWEEN);

	private static final ModelSpreader model = new ModelSpreader();

	public RenderTileSpreader(TileEntityRendererDispatcher manager) {
		super(manager);
	}

	@Override
	public void render(@Nonnull TileSpreader spreader, float ticks, MatrixStack ms, IRenderTypeBuffer buffers, int light, int overlay) {
		ms.push();

		ms.translate(0.5F, 1.5F, 0.5F);
		ms.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion(spreader.rotationX + 90F));
		ms.translate(0F, -1F, 0F);
		ms.multiply(Vector3f.POSITIVE_X.getDegreesQuaternion(spreader.rotationY));
		ms.translate(0F, 1F, 0F);

		ResourceLocation texture = spreader.isRedstone() ? textureRs : spreader.isDreamwood() ? textureDw : spreader.isULTRA_SPREADER() ? textureG : RenderTileSpreader.texture;
		if(ClientProxy.dootDoot)
			texture = spreader.isRedstone() ? textureRsHalloween : spreader.isDreamwood() ? textureDwHalloween : spreader.isULTRA_SPREADER() ? textureGHalloween : textureHalloween;

		ms.scale(1F, -1F, -1F);

		double time = ClientTickHandler.ticksInGame + ticks;

		float r = 1, g = 1, b = 1;
		if(spreader.isULTRA_SPREADER()) {
			Color color = Color.getHSBColor((float) ((time * 5 + new Random(spreader.getPos().hashCode()).nextInt(10000)) % 360) / 360F, 0.4F, 0.9F);
			r = color.getRed() / 255F;
			g = color.getGreen() / 255F;
			b = color.getBlue() / 255F;
		}
		IVertexBuilder buffer = buffers.getBuffer(model.getLayer(texture));
		model.render(ms, buffer, light, overlay, r, g, b, 1);

		ms.push();
		double worldTicks = spreader.getWorld() == null ? 0 : time;
		ms.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion((float) worldTicks % 360));
		ms.translate(0F, (float) Math.sin(worldTicks / 20.0) * 0.05F, 0F);
		model.renderCube(ms, buffer, light, overlay);
		ms.pop();
		ms.scale(1F, -1F, -1F);
		ItemStack stack = spreader.getItemHandler().getStackInSlot(0);

		if(!stack.isEmpty()) {
			ms.push();
			ms.translate(0.0F, -1F, -0.4675F);
			ms.multiply(Vector3f.POSITIVE_Z.getDegreesQuaternion(180));
			ms.multiply(Vector3f.POSITIVE_X.getDegreesQuaternion(180));
			ms.scale(1.0F, 1.0F, 1.0F);
			Minecraft.getInstance().getItemRenderer().renderItem(stack, ItemCameraTransforms.TransformType.NONE, light, overlay, ms, buffers);
			ms.pop();
		}

		if(spreader.paddingColor != null) {
			BlockState carpet = ColorHelper.CARPET_MAP.get(spreader.paddingColor).getDefaultState();
			IBakedModel model = Minecraft.getInstance().getBlockRendererDispatcher().getModelForState(carpet);
			buffer = buffers.getBuffer(RenderTypeLookup.getEntityBlockLayer(carpet));

			float f = 1 / 16F;

			// back
			ms.push();
			ms.multiply(Vector3f.POSITIVE_X.getDegreesQuaternion(90));
			ms.translate(-0.5F, 0.5F - f, 0.5F);
			Minecraft.getInstance().getBlockRendererDispatcher().getBlockModelRenderer().render(ms.peek(), buffer, carpet, model, 1, 1, 1, light, overlay);
			ms.pop();

			// left
			ms.push();
			ms.multiply(Vector3f.POSITIVE_Z.getDegreesQuaternion(-90));
			ms.translate(0.5F, 0.5F, -0.5F - f);
			Minecraft.getInstance().getBlockRendererDispatcher().getBlockModelRenderer().render(ms.peek(), buffer, carpet, model, 1, 1, 1, light, overlay);
			ms.pop();

			// right
			ms.push();
			ms.multiply(Vector3f.POSITIVE_Z.getDegreesQuaternion(90));
			ms.translate(-1.5F, 0.5F , -0.5F - f);
			Minecraft.getInstance().getBlockRendererDispatcher().getBlockModelRenderer().render(ms.peek(), buffer, carpet, model, 1, 1, 1, light, overlay);
			ms.pop();

			// top
			ms.push();
			ms.translate(-0.5F, -0.5F, -0.5F - f);
			Minecraft.getInstance().getBlockRendererDispatcher().getBlockModelRenderer().render(ms.peek(), buffer, carpet, model, 1, 1, 1, light, overlay);
			ms.pop();

			// bottom
			ms.push();
			ms.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion(180));
			ms.translate(-0.5F, -1.5F - f, -0.5F + f);
			Minecraft.getInstance().getBlockRendererDispatcher().getBlockModelRenderer().render(ms.peek(), buffer, carpet, model, 1, 1, 1, light, overlay);
			ms.pop();
		}

		ms.pop();
	}
}
