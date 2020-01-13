package vazkii.botania.client.model;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;

public interface IPylonModel {
	void renderRing(MatrixStack ms, IVertexBuilder buffer, int light, int overlay);

	void renderCrystal(MatrixStack ms, IVertexBuilder buffer, int light, int overlay);
}
