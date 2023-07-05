/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.client.integration.jei;

import com.mojang.blaze3d.vertex.PoseStack;

import mezz.jei.api.gui.drawable.IDrawable;

import net.minecraft.client.gui.GuiGraphics;

public class TerrestrialAgglomerationDrawable implements IDrawable {
	private final IDrawable cornerBlock;
	private final IDrawable centerBlock;
	private final IDrawable middleBlock;

	public TerrestrialAgglomerationDrawable(IDrawable cornerBlock, IDrawable centerBlock, IDrawable middleBlock) {
		this.cornerBlock = cornerBlock;
		this.centerBlock = centerBlock;
		this.middleBlock = middleBlock;
	}

	@Override
	public int getWidth() {
		return 43;
	}

	@Override
	public int getHeight() {
		return 31;
	}

	/**
	 * Offsets copied from {@link vazkii.botania.client.patchouli.component.TerraPlateComponent}
	 */
	@Override
	public void draw(GuiGraphics gui, int xOffset, int yOffset) {
		PoseStack ms = gui.pose();
		ms.pushPose();
		ms.translate(0, 0, -50);
		cornerBlock.draw(gui, xOffset + 13, yOffset + 1);

		ms.translate(0, 0, 5);
		middleBlock.draw(gui, xOffset + 20, yOffset + 4);
		middleBlock.draw(gui, xOffset + 7, yOffset + 4);

		ms.translate(0, 0, 5);
		cornerBlock.draw(gui, xOffset + 13, yOffset + 8);
		centerBlock.draw(gui, xOffset + 27, yOffset + 8);
		cornerBlock.draw(gui, xOffset, yOffset + 8);

		ms.translate(0, 0, 5);
		middleBlock.draw(gui, xOffset + 7, yOffset + 12);
		middleBlock.draw(gui, xOffset + 20, yOffset + 12);

		ms.translate(0, 0, 5);
		cornerBlock.draw(gui, xOffset + 14, yOffset + 15);
		ms.popPose();
	}
}
