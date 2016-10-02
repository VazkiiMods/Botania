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

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;

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
	public void onPlayerBaubleRender(ItemStack stack, EntityPlayer player, RenderType type, float partialTicks);

	/**
	 * A few helper methods for the render.
	 */
	final class Helper {

		/**
		 * Rotates the render for a bauble correctly if the player is sneaking.
		 * Use for renders under {@link RenderType#BODY}.
		 */
		public static void rotateIfSneaking(EntityPlayer player) {
			if(player.isSneaking())
				applySneakingRotation();
		}

		/**
		 * Rotates the render for a bauble correctly for a sneaking player.
		 * Use for renders under {@link RenderType#BODY}.
		 */
		public static void applySneakingRotation() {
			GlStateManager.translate(0F, 0.2F, 0F);
			GlStateManager.rotate(90F / (float) Math.PI, 1.0F, 0.0F, 0.0F);
		}

		/**
		 * Shifts the render for a bauble correctly to the head, including sneaking rotation.
		 * Use for renders under {@link RenderType#HEAD}.
		 */
		public static void translateToHeadLevel(EntityPlayer player) {
			GlStateManager.translate(0, -player.getDefaultEyeHeight(), 0);
			if (player.isSneaking())
				GlStateManager.translate(0.25F * MathHelper.sin(player.rotationPitch * (float) Math.PI / 180), 0.25F * MathHelper.cos(player.rotationPitch * (float) Math.PI / 180), 0F);
		}

		/**
		 * Shifts the render for a bauble correctly to the face.
		 * Use for renders under {@link RenderType#HEAD}, and usually after calling {@link Helper#translateToHeadLevel(EntityPlayer)}.
		 */
		public static void translateToFace() {
			GlStateManager.rotate(90F, 0F, 1F, 0F);
			GlStateManager.rotate(180F, 1F, 0F, 0F);
			GlStateManager.translate(0f, -4.35f, -1.27f);
		}

		/**
		 * Scales down the render to a correct size.
		 * Use for any render.
		 */
		public static void defaultTransforms() {
			GlStateManager.translate(0.0, 3.0, 1.0);
			GlStateManager.scale(0.55, 0.55, 0.55);
		}

		/**
		 * Shifts the render for a bauble correctly to the chest.
		 * Use for renders under {@link RenderType#BODY}, and usually after calling {@link Helper#rotateIfSneaking(EntityPlayer)}.
		 */
		public static void translateToChest() {
			GlStateManager.rotate(180F, 1F, 0F, 0F);
			GlStateManager.translate(0F, -3.2F, -0.85F);
		}

	}

	enum RenderType {
		/**
		 * Render Type for the player's body, translations apply on the player's rotation.
		 * Sneaking is not handled and should be done during the render.
		 * @see IBaubleRender.Helper
		 */
		BODY,

		/**
		 * Render Type for the player's body, translations apply on the player's head rotations.
		 * Sneaking is not handled and should be done during the render.
		 * @see IBaubleRender.Helper
		 */
		HEAD;
	}

}
