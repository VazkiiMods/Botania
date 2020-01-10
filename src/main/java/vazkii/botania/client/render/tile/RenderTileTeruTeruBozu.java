/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Oct 1, 2015, 9:02:30 PM (GMT)]
 */
package vazkii.botania.client.render.tile;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.Vector3f;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import vazkii.botania.client.core.proxy.ClientProxy;
import vazkii.botania.client.lib.LibResources;
import vazkii.botania.client.model.ModelTeruTeruBozu;
import vazkii.botania.common.Botania;
import vazkii.botania.common.block.tile.TileAvatar;
import vazkii.botania.common.block.tile.TileTeruTeruBozu;

import javax.annotation.Nullable;
import java.util.Random;

public class RenderTileTeruTeruBozu extends TileEntityRenderer<TileTeruTeruBozu> {

	private static final ResourceLocation texture = new ResourceLocation(LibResources.MODEL_TERU_TERU_BOZU);
	private static final ResourceLocation textureHalloween = new ResourceLocation(LibResources.MODEL_TERU_TERU_BOZU_HALLOWEEN);
	private final ModelTeruTeruBozu model = new ModelTeruTeruBozu();

	public RenderTileTeruTeruBozu(TileEntityRendererDispatcher manager) {
		super(manager);
	}

	@Override
	public void render(@Nullable TileTeruTeruBozu tileentity, float partialTicks, MatrixStack ms, IRenderTypeBuffer buffers, int light, int overlay) {
		ms.push();
		ms.multiply(Vector3f.POSITIVE_X.getDegreesQuaternion(180));
		double time = Botania.proxy.getWorldElapsedTicks() + partialTicks;
		boolean hasWorld = tileentity != null && tileentity.getWorld() != null;
		if(hasWorld)
			time += new Random(tileentity.getPos().hashCode()).nextInt(1000);

		ms.translate(0.5F, -1.25F + (hasWorld ? (float) Math.sin(time * 0.01F) * 0.05F : 0F), -0.5F);
		if(hasWorld) {
			ms.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion((float) (time * 0.3)));
			ms.multiply(Vector3f.POSITIVE_Z.getDegreesQuaternion(4F * (float) Math.sin(time * 0.05F)));
			float s = 0.75F;
			ms.scale(s, s, s);
		}

		IVertexBuilder buffer = buffers.getBuffer(model.getLayer(ClientProxy.dootDoot ? textureHalloween : texture));
		model.render(ms, buffer, light, overlay, 1, 1, 1, 1);
		ms.pop();
	}

}
