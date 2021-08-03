/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.api.wand;

import com.mojang.blaze3d.vertex.PoseStack;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

/**
 * Any block that implements this has a HUD rendered when being hovered
 * with a Wand of the Forest.
 */
public interface IWandHUD {

	@Environment(EnvType.CLIENT)
	void renderHUD(PoseStack ms, Minecraft mc, Level world, BlockPos pos);

}
