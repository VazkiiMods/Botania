/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.client.model;

import net.minecraft.client.model.Model;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.util.math.MatrixStack;

public class ModelPylonGaia extends Model implements IPylonModel {

	private final ModelPart platef;
	private final ModelPart plateb;
	private final ModelPart platel;
	private final ModelPart plater;

	private final ModelPart shardlbt;
	private final ModelPart shardrbt;
	private final ModelPart shardlft;
	private final ModelPart shardrft;

	private final ModelPart shardlbb;
	private final ModelPart shardrbb;
	private final ModelPart shardlfb;
	private final ModelPart shardrfb;

	public ModelPylonGaia() {
		super(RenderLayer::getEntityTranslucent);

		textureWidth = 64;
		textureHeight = 64;

		//plates
		platel = new ModelPart(this, 36, 0);
		platel.setPivot(0.0F, 16.0F, 0.0F);
		platel.addCuboid(-3.0F, -3.0F, -8.0F, 6, 6, 2, 0.0F);
		setRotation(platel, 0.0F, 1.5707963267948966F, 0.0F);
		plater = new ModelPart(this, 36, 0);
		plater.setPivot(0.0F, 16.0F, 0.0F);
		plater.addCuboid(-3.0F, -3.0F, -8.0F, 6, 6, 2, 0.0F);
		setRotation(plater, 0.0F, -1.5707963267948966F, 0.0F);
		platef = new ModelPart(this, 36, 0);
		platef.setPivot(0.0F, 16.0F, 0.0F);
		platef.addCuboid(-3.0F, -3.0F, -8.0F, 6, 6, 2, 0.0F);
		plateb = new ModelPart(this, 36, 0);
		plateb.setPivot(0.0F, 16.0F, 0.0F);
		plateb.addCuboid(-3.0F, -3.0F, -8.0F, 6, 6, 2, 0.0F);
		setRotation(plateb, 0.0F, 3.141592653589793F, 0.0F);

		//shards
		shardrft = new ModelPart(this, 16, 32);
		shardrft.setPivot(0.0F, 16.0F, 0.0F);
		shardrft.addCuboid(2.0F, -6.0F, -5.0F, 3, 2, 3, 0.0F);
		shardlbt = new ModelPart(this, 0, 0);
		shardlbt.setPivot(0.0F, 16.0F, 0.0F);
		shardlbt.addCuboid(-5.0F, -6.0F, 0.0F, 6, 5, 5, 0.0F);
		shardrbt = new ModelPart(this, 22, 9);
		shardrbt.setPivot(0.0F, 16.0F, 0.0F);
		shardrbt.addCuboid(3.0F, -5.0F, 0.0F, 2, 4, 5, 0.0F);
		shardlft = new ModelPart(this, 0, 32);
		shardlft.setPivot(0.0F, 16.0F, 0.0F);
		shardlft.addCuboid(-5.0F, -7.0F, -5.0F, 5, 6, 3, 0.0F);

		shardrfb = new ModelPart(this, 16, 37);
		shardrfb.setPivot(0.0F, 16.0F, 0.0F);
		shardrfb.addCuboid(2.0F, -2.0F, -5.0F, 3, 6, 3, 0.0F);
		shardlbb = new ModelPart(this, 0, 10);
		shardlbb.setPivot(0.0F, 16.0F, 0.0F);
		shardlbb.addCuboid(-5.0F, 1.0F, 0.0F, 6, 3, 5, 0.0F);
		shardrbb = new ModelPart(this, 22, 0);
		shardrbb.setPivot(0.0F, 16.0F, 0.0F);
		shardrbb.addCuboid(3.0F, 1.0F, 0.0F, 2, 4, 5, 0.0F);
		shardlfb = new ModelPart(this, 0, 41);
		shardlfb.setPivot(0.0F, 16.0F, 0.0F);
		shardlfb.addCuboid(-5.0F, 1.0F, -5.0F, 5, 2, 3, 0.0F);
	}

	@Override
	public void renderCrystal(MatrixStack ms, VertexConsumer buffer, int light, int overlay) {
		shardrft.render(ms, buffer, light, overlay);
		shardlbt.render(ms, buffer, light, overlay);
		shardrbt.render(ms, buffer, light, overlay);
		shardlft.render(ms, buffer, light, overlay);

		shardrfb.render(ms, buffer, light, overlay);
		shardlbb.render(ms, buffer, light, overlay);
		shardrbb.render(ms, buffer, light, overlay);
		shardlfb.render(ms, buffer, light, overlay);
	}

	@Override
	public void renderRing(MatrixStack ms, VertexConsumer buffer, int light, int overlay) {
		platef.render(ms, buffer, light, overlay);
		plateb.render(ms, buffer, light, overlay);
		platel.render(ms, buffer, light, overlay);
		plater.render(ms, buffer, light, overlay);
	}

	private void setRotation(ModelPart model, float x, float y, float z) {

		model.pitch = x;
		model.yaw = y;
		model.roll = z;
	}

	@Override
	public void render(MatrixStack ms, VertexConsumer buffer, int light, int overlay, float r, float g, float b, float a) {
		throw new UnsupportedOperationException("unimplemented");
	}
}
