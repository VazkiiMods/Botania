/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.client.render.tile;

import vazkii.botania.client.core.handler.ClientTickHandler;
import vazkii.botania.client.core.proxy.ClientProxy;
import vazkii.botania.client.lib.LibResources;
import vazkii.botania.client.model.ModelTeruTeruBozu;
import vazkii.botania.common.Botania;
import vazkii.botania.common.block.tile.TileTeruTeruBozu;

import javax.annotation.Nullable;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.util.Identifier;
import java.util.Random;

public class RenderTileTeruTeruBozu extends BlockEntityRenderer<TileTeruTeruBozu> {

	private static final Identifier texture = new Identifier(LibResources.MODEL_TERU_TERU_BOZU);
	private static final Identifier textureHalloween = new Identifier(LibResources.MODEL_TERU_TERU_BOZU_HALLOWEEN);
	private final ModelTeruTeruBozu model = new ModelTeruTeruBozu();

	public RenderTileTeruTeruBozu(BlockEntityRenderDispatcher manager) {
		super(manager);
	}

	@Override
	public void render(@Nullable TileTeruTeruBozu tileentity, float partialTicks, MatrixStack ms, VertexConsumerProvider buffers, int light, int overlay) {
		ms.push();
		ms.multiply(Vector3f.POSITIVE_X.getDegreesQuaternion(180));
		double time = ClientTickHandler.ticksInGame + partialTicks;
		boolean hasWorld = tileentity != null && tileentity.getWorld() != null;
		if (hasWorld) {
			time += new Random(tileentity.getPos().hashCode()).nextInt(1000);
		}

		ms.translate(0.5F, -1.25F + (hasWorld ? (float) Math.sin(time * 0.01F) * 0.05F : 0F), -0.5F);
		if (hasWorld) {
			ms.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion((float) (time * 0.3)));
			ms.multiply(Vector3f.POSITIVE_Z.getDegreesQuaternion(4F * (float) Math.sin(time * 0.05F)));
			float s = 0.75F;
			ms.scale(s, s, s);
		}

		VertexConsumer buffer = buffers.getBuffer(model.getLayer(ClientProxy.dootDoot ? textureHalloween : texture));
		model.render(ms, buffer, light, overlay, 1, 1, 1, 1);
		ms.pop();
	}

}
