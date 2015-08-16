/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [Oct 29, 2014, 6:09:48 PM (GMT)]
 */
package vazkii.botania.api.boss;

import java.awt.Rectangle;

import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.entity.boss.IBossDisplayData;
import net.minecraft.util.ResourceLocation;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * An extension of IBossDisplayData. This counts as a botania boss and as a normal
 * minecraft boss since it has IBossDisplayData. Instead of using the typical
 * BossStatus.setBossStatus(entity, true) to set the health bar in the render use
 * BotaniaAPI.internalMethodHandler.setBossStatus(entity);<br><br>
 * Alternatively, you can check if botania is loaded and use the vanilla one
 * if that's not the case.
 */
public interface IBotaniaBoss extends IBossDisplayData {

	/**
	 * The ResourceLocation to bind for this boss's boss bar.
	 * You can use BotaniaAPI.internalMethodHandler.getDefaultBossBarTexture() to get
	 * the one used by botania bosses.
	 */
	@SideOnly(Side.CLIENT)
	public ResourceLocation getBossBarTexture();

	/**
	 * A Rectangle instance delimiting the uv, width and height of this boss's
	 * boss bar texture. This is for the background, not the bar that shows
	 * the HP.
	 */
	@SideOnly(Side.CLIENT)
	public Rectangle getBossBarTextureRect();

	/**
	 * A Rectangle instance delimiting the uv, width and height of this boss's
	 * boss bar HP texture. This is for the foreground that shows how much
	 * HP the boss has. The width of the rectangle will be multiplied by the
	 * faction of the boss's current HP by max HP.
	 */
	@SideOnly(Side.CLIENT)
	public Rectangle getBossBarHPTextureRect();

	/**
	 * A callback for when this boss's boss bar renders, you can do aditional rendering
	 * here if needed.
	 */
	@SideOnly(Side.CLIENT)
	public void bossBarRenderCallback(ScaledResolution res, int x, int y);
}
