/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.api.block;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;

/**
 * Any block (entity) that has this capability renders a HUD when being hovered
 * with a Wand of the Forest.
 */
public interface WandHUD {

	void renderHUD(GuiGraphics gui, Minecraft mc);

}
