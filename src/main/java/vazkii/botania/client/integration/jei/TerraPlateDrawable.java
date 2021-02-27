/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.client.integration.jei;

import com.mojang.blaze3d.matrix.MatrixStack;

import mezz.jei.api.gui.drawable.IDrawable;

public class TerraPlateDrawable implements IDrawable {
	private final IDrawable cornerBlock;
	private final IDrawable centerBlock;
	private final IDrawable middleBlock;

	public TerraPlateDrawable(IDrawable cornerBlock, IDrawable centerBlock, IDrawable middleBlock) {
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
	public void draw(MatrixStack ms, int xOffset, int yOffset) {
		ms.push();
		ms.translate(0, 0, -50);
		cornerBlock.draw(ms, xOffset + 13, yOffset + 1);

		ms.translate(0, 0, 5);
		middleBlock.draw(ms, xOffset + 20, yOffset + 4);
		middleBlock.draw(ms, xOffset + 7, yOffset + 4);

		ms.translate(0, 0, 5);
		cornerBlock.draw(ms, xOffset + 13, yOffset + 8);
		centerBlock.draw(ms, xOffset + 27, yOffset + 8);
		cornerBlock.draw(ms, xOffset, yOffset + 8);

		ms.translate(0, 0, 5);
		middleBlock.draw(ms, xOffset + 7, yOffset + 12);
		middleBlock.draw(ms, xOffset + 20, yOffset + 12);

		ms.translate(0, 0, 5);
		cornerBlock.draw(ms, xOffset + 14, yOffset + 15);
		ms.pop();
	}
}
