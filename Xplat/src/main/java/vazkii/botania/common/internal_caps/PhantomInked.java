/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */

package vazkii.botania.common.internal_caps;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.decoration.ItemFrame;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.gameevent.GameEvent;

import vazkii.botania.api.attachment.DataMarkerId;
import vazkii.botania.common.item.BotaniaItems;

import static vazkii.botania.api.BotaniaAPI.botaniaRL;

/**
 * A marker for an entity that has phantom ink applied but does not support it natively.
 */
public final class PhantomInked {

	public static final ResourceLocation ID = botaniaRL("phantom_inked");
	public static final DataMarkerId MARKER = new DataMarkerId(ID);

	/**
	 * Called when the item in an item frame changes.
	 * If the frame has the phantom-inked marker, update its invisibility status so it's still visible while empty.
	 */
	public static void updateItemFrame(ItemFrame frame, ItemStack stack) {
		if (!MARKER.existsFor(frame)) {
			return;
		}

		if (frame.isInvisible()) {
			if (stack.isEmpty()) {
				frame.setInvisible(false);
			}
		} else if (!stack.isEmpty()) {
			frame.setInvisible(true);
		}
	}

	/**
	 * Called when a player interacts with an item frame. If the player sneak-interacts while holding phantom ink, it is
	 * applied to the item frame, rather than putting it into the frame or rotating the framed item.
	 * 
	 * @return {@code true} if the interaction was successful, {@code false} otherwise.
	 */
	public static boolean applyToItemFrame(ItemFrame frame, Player player, InteractionHand hand) {
		if (!player.isSecondaryUseActive()) {
			return false;
		}
		ItemStack stack = player.getItemInHand(hand);
		if (stack.isEmpty() || !stack.is(BotaniaItems.PHANTOM_INK)) {
			return false;
		}

		if (!MARKER.existsFor(frame) && !frame.level().isClientSide()) {
			// TODO should this (and other in-world uses of phantom ink) play a sound?
			MARKER.addFor(frame);
			frame.gameEvent(GameEvent.BLOCK_CHANGE, player);
			stack.consume(1, player);
			updateItemFrame(frame, frame.getItem());
		}
		return true;
	}

	private PhantomInked() {}
}
