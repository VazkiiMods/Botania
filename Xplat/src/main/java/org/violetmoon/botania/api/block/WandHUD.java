/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.api.block;

import com.mojang.blaze3d.platform.Window;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;

import vazkii.botania.api.capability.BlockApiNoContext;
import vazkii.botania.api.capability.EntityApiNoContext;

import static vazkii.botania.api.BotaniaAPI.botaniaRL;

/**
 * Any block (entity) that has this capability renders a HUD when being hovered
 * with a Wand of the Forest.
 */
public interface WandHUD {

	ResourceLocation ID = botaniaRL("wand_hud");
	BlockApiNoContext<WandHUD> BLOCK_LOOKUP = new BlockApiNoContext<>(ID, WandHUD.class);
	EntityApiNoContext<WandHUD> ENTITY_LOOKUP = new EntityApiNoContext<>(ID, WandHUD.class);

	void renderHUD(GuiGraphics gui, Window window, Font font, float partialTick);

}
