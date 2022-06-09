package vazkii.botania.fabric.integration.emi;

import com.mojang.blaze3d.vertex.PoseStack;

import dev.emi.emi.api.widget.Bounds;
import dev.emi.emi.api.widget.Widget;

import vazkii.botania.client.gui.HUDHandler;

public class ManaWidget extends Widget {
	private final int x, y;
	private final int mana, maxMana;

	public ManaWidget(int x, int y, int mana, int maxMana) {
		this.x = x;
		this.y = y;
		this.mana = mana;
		this.maxMana = maxMana;
	}

	@Override
	public Bounds getBounds() {
		return new Bounds(x, y, 102, 5);
	}

	@Override
	public void render(PoseStack matrices, int mouseX, int mouseY, float delta) {
		HUDHandler.renderManaBar(matrices, x, y, 0x0000FF, 0.75F, mana, maxMana);
	}
}
