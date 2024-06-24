package vazkii.botania.client.integration.emi;

import com.mojang.blaze3d.systems.RenderSystem;

import dev.emi.emi.api.widget.TextureWidget;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;

public class BlendTextureWidget extends TextureWidget {

	public BlendTextureWidget(ResourceLocation texture, int x, int y, int width, int height, int u, int v) {
		super(texture, x, y, width, height, u, v);
	}

	@Override
	public void render(GuiGraphics gui, int mouseX, int mouseY, float delta) {
		RenderSystem.enableBlend();
		super.render(gui, mouseX, mouseY, delta);
		RenderSystem.disableBlend();
	}
}
