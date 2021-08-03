/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.RenderType;

public class ModelPylonMana extends Model implements IPylonModel {

	private final ModelPart platef;
	private final ModelPart plateb;
	private final ModelPart platel;
	private final ModelPart plater;

	private final ModelPart shardlf;
	private final ModelPart shardrf;
	private final ModelPart shardlb;
	private final ModelPart shardrb;

	public ModelPylonMana() {
		super(RenderType::entityTranslucent);

		texWidth = 64;
		texHeight = 64;

		//plates
		platef = new ModelPart(this, 36, 0);
		platef.setPos(0.0F, 16.0F, 0.0F);
		platef.addBox(-3.0F, -4.0F, -8.0F, 6, 8, 2, 0.0F);
		plateb = new ModelPart(this, 36, 0);
		plateb.setPos(0.0F, 16.0F, 0.0F);
		plateb.addBox(-3.0F, -4.0F, -8.0F, 6, 8, 2, 0.0F);
		setRotation(plateb, 0.0F, 3.141592653589793F, 0.0F);
		platel = new ModelPart(this, 36, 0);
		platel.setPos(0.0F, 16.0F, 0.0F);
		platel.addBox(-3.0F, -4.0F, -8.0F, 6, 8, 2, 0.0F);
		setRotation(platel, 0.0F, 1.5707963267948966F, 0.0F);
		plater = new ModelPart(this, 36, 0);
		plater.setPos(0.0F, 16.0F, 0.0F);
		plater.addBox(-3.0F, -4.0F, -8.0F, 6, 8, 2, 0.0F);
		setRotation(plater, 0.0F, -1.5707963267948966F, 0.0F);

		//shards
		shardlf = new ModelPart(this, 0, 21);
		shardlf.setPos(0.0F, 16.0F, 0.0F);
		shardlf.addBox(-5.0F, -9.0F, -5.0F, 5, 16, 3, 0.0F);
		shardrf = new ModelPart(this, 16, 21);
		shardrf.setPos(0.0F, 16.0F, 0.0F);
		shardrf.addBox(2.0F, -12.0F, -5.0F, 3, 16, 3, 0.0F);
		shardlb = new ModelPart(this, 0, 0);
		shardlb.setPos(0.0F, 16.0F, 0.0F);
		shardlb.addBox(-5.0F, -10.0F, 0.0F, 6, 16, 5, 0.0F);
		shardrb = new ModelPart(this, 22, 0);
		shardrb.setPos(0.0F, 16.0F, 0.0F);
		shardrb.addBox(3.0F, -11.0F, 0.0F, 2, 16, 5, 0.0F);
	}

	@Override
	public void renderCrystal(PoseStack ms, VertexConsumer buffer, int light, int overlay) {
		shardlf.render(ms, buffer, light, overlay);
		shardrf.render(ms, buffer, light, overlay);
		shardlb.render(ms, buffer, light, overlay);
		shardrb.render(ms, buffer, light, overlay);
	}

	@Override
	public void renderRing(PoseStack ms, VertexConsumer buffer, int light, int overlay) {
		platef.render(ms, buffer, light, overlay);
		plateb.render(ms, buffer, light, overlay);
		platel.render(ms, buffer, light, overlay);
		plater.render(ms, buffer, light, overlay);
	}

	private void setRotation(ModelPart model, float x, float y, float z) {
		model.xRot = x;
		model.yRot = y;
		model.zRot = z;
	}

	@Override
	public void renderToBuffer(PoseStack ms, VertexConsumer buffer, int light, int overlay, float r, float g, float b, float a) {
		throw new UnsupportedOperationException("unimplemented");
	}
}
