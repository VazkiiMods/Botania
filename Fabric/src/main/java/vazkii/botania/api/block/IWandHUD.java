/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.api.block;

import com.mojang.blaze3d.vertex.PoseStack;

import net.fabricmc.fabric.api.lookup.v1.block.BlockApiLookup;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Unit;

import vazkii.botania.api.BotaniaAPI;

/**
 * Any block (entity) that has this capability renders a HUD when being hovered
 * with a Wand of the Forest.
 */
public interface IWandHUD {
	BlockApiLookup<IWandHUD, Unit> API = BlockApiLookup.get(new ResourceLocation(BotaniaAPI.MODID, "wand_hud"), IWandHUD.class, Unit.class);

	void renderHUD(PoseStack ms, Minecraft mc);

}
