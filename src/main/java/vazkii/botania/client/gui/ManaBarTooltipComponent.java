/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.client.gui;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.world.inventory.tooltip.TooltipComponent;

import vazkii.botania.api.mana.ManaBarTooltip;

import javax.annotation.Nullable;

public class ManaBarTooltipComponent implements ClientTooltipComponent {
	private final float percentageFull;
	private final int pickLevel;

	public ManaBarTooltipComponent(ManaBarTooltip component) {
		this.percentageFull = component.getPercentageFull();
		this.pickLevel = component.getPickLevel();
	}

	@Nullable
	public static ClientTooltipComponent tryConvert(TooltipComponent component) {
		if (component instanceof ManaBarTooltip t) {
			return new ManaBarTooltipComponent(t);
		}
		return null;
	}

	public float getPercentageFull() {
		return percentageFull;
	}

	public int getPickLevel() {
		return pickLevel;
	}

	@Override
	public int getHeight() {
		return 0;
	}

	@Override
	public int getWidth(Font font) {
		return 0;
	}

	@Override
	public void renderImage(Font font, int i, int j, PoseStack ps, ItemRenderer renderer, int k, TextureManager manager) {
		// Rendering is handled from a mixin callback in TooltipAdditionDisplayHandler
		// TODO: If wrapped by another component, like Inspecio's compound components, it will stop working
		//       - investigate moving the rendering here.
	}
}
