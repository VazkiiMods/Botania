package vazkii.botania.fabric.integration.emi;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;

import dev.emi.emi.api.widget.TextureWidget;

import net.minecraft.resources.ResourceLocation;

public class BlendTextureWidget extends TextureWidget {

	public BlendTextureWidget(ResourceLocation texture, int x, int y, int width, int height, int u, int v) {
		super(texture, x, y, width, height, u, v);
	}

	@Override
	public void render(PoseStack matrices, int mouseX, int mouseY, float delta) {
		RenderSystem.enableBlend();
		super.render(matrices, mouseX, mouseY, delta);
		RenderSystem.disableBlend();
	}
}
