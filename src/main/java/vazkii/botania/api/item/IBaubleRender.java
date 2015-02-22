/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [Dec 4, 2014, 3:28:43 PM (GMT)]
 */
package vazkii.botania.api.item;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.RenderPlayerEvent;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * A Bauble Item that implements this will be have hooks to render something on
 * the player while its equipped.
 * This class doesn't extend IBauble to make the API not depend on the Baubles
 * API, but the item in question still needs to implement IBauble.
 */
public interface IBaubleRender {

	/**
	 * Called for the rendering of the bauble on the player. The player instance can be
	 * acquired through the event parameter. Transformations are already applied for
	 * the RenderType passed in. Make sure to check against the type parameter for
	 * rendering. Will not be called if the item is a ICosmeticAttachable and
	 * has a cosmetic bauble attached to it.
	 */
	@SideOnly(Side.CLIENT)
	public void onPlayerBaubleRender(ItemStack stack, RenderPlayerEvent event, RenderType type);

	/**
	 * A few helper methods for the render.
	 */
	public static class Helper {

		public static void rotateIfSneaking(EntityPlayer player) {
			if(player.isSneaking())
				applySneakingRotation();
		}

		public static void applySneakingRotation() {
			GL11.glRotatef(28.64789F, 1.0F, 0.0F, 0.0F);
		}

		public static void translateToHeadLevel(EntityPlayer player) {
			GL11.glTranslated(0, (player != Minecraft.getMinecraft().thePlayer ? 1.62F : 0F) - player.getDefaultEyeHeight() + (player.isSneaking() ? 0.0625 : 0), 0);
		}

	}

	public static enum RenderType {
		/**
		 * Render Type for the player's body, translations apply on the player's rotation.
		 * Sneaking is not handled and should be done during the render.
		 * @see IBaubleRender.Helper
		 */
		BODY,

		/**
		 * Render Type for the player's body, translations apply on the player's head rotations.
		 * Sneaking is not handled and should be done during the render.~
		 * @see IBaubleRender.Helper
		 */
		HEAD;
	}

}
