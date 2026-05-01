package vazkii.botania.api.block;

import com.mojang.blaze3d.platform.Window;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;

import static vazkii.botania.api.BotaniaAPI.botaniaRL;

/**
 * Any block or entity with this client capability renders a HUD when looked at while wearing a Manaseer Monocle.
 */
public interface MonocleHud {

	ResourceLocation ID = botaniaRL("monocle_hud");

	void renderHUD(GuiGraphics gui, Window window, Font font, float partialTick);

}
