package vazkii.botania.api.block;

import com.mojang.blaze3d.platform.Window;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;

import vazkii.botania.api.capability.BlockApiNoContext;
import vazkii.botania.api.capability.EntityApiNoContext;

import static vazkii.botania.api.BotaniaAPI.botaniaRL;

/**
 * Any block or entity with this client capability renders a HUD when looked at while wearing a Manaseer Monocle.
 */
public interface MonocleHud {

	ResourceLocation ID = botaniaRL("monocle_hud");
	BlockApiNoContext<MonocleHud> BLOCK_LOOKUP = new BlockApiNoContext<>(ID, MonocleHud.class);
	EntityApiNoContext<MonocleHud> ENTITY_LOOKUP = new EntityApiNoContext<>(ID, MonocleHud.class);

	void renderHUD(GuiGraphics gui, Window window, Font font, float partialTick);

}
