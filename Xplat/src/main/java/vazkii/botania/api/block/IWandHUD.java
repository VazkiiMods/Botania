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

import net.minecraft.client.Minecraft;

/**
 * Any block (entity) that has this capability renders a HUD when being hovered
 * with a Wand of the Forest.
 */
public interface IWandHUD {

	void renderHUD(PoseStack ms, Minecraft mc);

}
