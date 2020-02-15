/**
 * This class was created by <Hubry>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Sep 08 2019, 10:31 PM (GMT)]
 */
package vazkii.botania.client.patchouli.component;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.resources.I18n;
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
		if(mana.length() > 0) {
			this.manaValues = Arrays.stream(mana.split(";")).mapToInt(Integer::valueOf).toArray();
		} else {
			this.manaValues = new int[]{0};
		}
	}

	@Override
	public void render(IComponentRenderContext context, float pticks, int mouseX, int mouseY) {
		FontRenderer font = context.getFont();

		String manaUsage = I18n.format("botaniamisc.manaUsage");
		font.drawString(manaUsage, x + 102 / 2 - font.getStringWidth(manaUsage) / 2, y, 0x66000000);

		int ratio = 10;
		if(context.isAreaHovered(mouseX, mouseY, x, y - 2, 102, 5 + 20)) {
			ratio = 1;
		}
		HUDHandler.renderManaBar(x, y + 10, 0x0000FF, 0.75F, 
				manaValues[(context.getTicksInBook() / 20) % manaValues.length], TilePool.MAX_MANA / ratio);

		String ratioString = I18n.format("botaniamisc.ratio", ratio);
		font.drawString(ratioString, x + 102 / 2 - font.getStringWidth(ratioString) / 2, y + 15, 0x99000000);
	}

	@Override
	public void onVariablesAvailable(Function<String, String> lookup) {
		mana = lookup.apply(mana);
	}
}
