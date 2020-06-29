/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.client.patchouli.component;

import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import vazkii.botania.client.core.handler.HUDHandler;
import vazkii.botania.common.block.tile.mana.TilePool;
import vazkii.patchouli.api.IComponentRenderContext;
import vazkii.patchouli.api.ICustomComponent;

import java.util.Arrays;
import java.util.function.Function;

/**
 * A custom component that renders a mana bar.
 * It only has one custom parameter, {@code mana}, which is a semicolon-separated list of mana values.
 * Setting x to {@code -1} will center the component.
 */
public class ManaComponent implements ICustomComponent {
	private transient int x, y;
	private transient int[] manaValues;

	public String mana;

	@Override
	public void build(int componentX, int componentY, int pageNum) {
		this.x = componentX != -1 ? componentX : 7;
		this.y = componentY;
		if (mana.length() > 0) {
			this.manaValues = Arrays.stream(mana.split(";")).mapToInt(Integer::valueOf).toArray();
		} else {
			this.manaValues = new int[] { 0 };
		}
	}

	@Override
	public void render(MatrixStack ms, IComponentRenderContext context, float pticks, int mouseX, int mouseY) {
		FontRenderer font = Minecraft.getInstance().fontRenderer;
		ITextComponent manaUsage = new TranslationTextComponent("botaniamisc.manaUsage").func_230530_a_(context.getFont());
		font.func_238422_b_(ms, manaUsage, x + 102 / 2 - font.func_238414_a_(manaUsage) / 2, y, 0x66000000);

		int ratio = 10;
		if (context.isAreaHovered(mouseX, mouseY, x, y - 2, 102, 5 + 20)) {
			ratio = 1;
		}
		HUDHandler.renderManaBar(ms, x, y + 10, 0x0000FF, 0.75F,
				manaValues[(context.getTicksInBook() / 20) % manaValues.length], TilePool.MAX_MANA / ratio);

		ITextComponent ratioString = new TranslationTextComponent("botaniamisc.ratio", ratio).func_230530_a_(context.getFont());
		font.func_238422_b_(ms, ratioString, x + 102 / 2 - font.func_238414_a_(ratioString) / 2, y + 15, 0x99000000);
	}

	@Override
	public void onVariablesAvailable(Function<String, String> lookup) {
		mana = lookup.apply(mana);
	}
}
