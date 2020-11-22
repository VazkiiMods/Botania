/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.core.helper;

import com.mojang.datafixers.util.Pair;
import net.minecraft.advancement.Advancement;
import net.minecraft.advancement.PlayerAdvancementTracker;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.server.ServerAdvancementLoader;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.function.Predicate;

public final class PlayerHelper {

	// Checks if either of the player's hands has an item.
	public static boolean hasAnyHeldItem(PlayerEntity player) {
		return !player.getMainHandStack().isEmpty() || !player.getOffHandStack().isEmpty();
	}

	// Checks main hand, then off hand for this item.
	public static boolean hasHeldItem(PlayerEntity player, Item item) {
		return !player.getMainHandStack().isEmpty() && player.getMainHandStack().getItem() == item
				|| !player.getOffHandStack().isEmpty() && player.getOffHandStack().getItem() == item;
	}

	// Checks main hand, then off hand for this item class.
	public static boolean hasHeldItemClass(PlayerEntity player, Item template) {
		return hasHeldItemClass(player, template.getClass());
	}

	// Checks main hand, then off hand for this item class.
	public static boolean hasHeldItemClass(PlayerEntity player, Class<?> template) {
		return !player.getMainHandStack().isEmpty() && template.isAssignableFrom(player.getMainHandStack().getItem().getClass())
				|| !player.getOffHandStack().isEmpty() && template.isAssignableFrom(player.getOffHandStack().getItem().getClass());
	}

	public static ItemStack getFirstHeldItem(LivingEntity living, Item item) {
		return getFirstHeldItem(living, s -> s.getItem() == item);
	}

	public static ItemStack getFirstHeldItem(LivingEntity living, Predicate<ItemStack> pred) {
		ItemStack main = living.getMainHandStack();
		ItemStack offhand = living.getOffHandStack();
		if (!main.isEmpty() && pred.test(main)) {
			return main;
		} else if (!offhand.isEmpty() && pred.test(offhand)) {
			return offhand;
		} else {
			return ItemStack.EMPTY;
		}
	}

	public static ItemStack getFirstHeldItemClass(PlayerEntity player, Class<?> template) {
		return getFirstHeldItem(player, s -> template.isAssignableFrom(s.getItem().getClass()));
	}

	public static ItemStack getAmmo(PlayerEntity player, Predicate<ItemStack> ammoFunc) {
		// Mainly from ItemBow.findAmmo
		if (ammoFunc.test(player.getStackInHand(Hand.OFF_HAND))) {
			return player.getStackInHand(Hand.OFF_HAND);
		} else if (ammoFunc.test(player.getStackInHand(Hand.MAIN_HAND))) {
			return player.getStackInHand(Hand.MAIN_HAND);
		} else {
			for (int i = 0; i < player.inventory.size(); ++i) {
				ItemStack itemstack = player.inventory.getStack(i);

				if (ammoFunc.test(itemstack)) {
					return itemstack;
				}
			}

			return ItemStack.EMPTY;
		}
	}

	public static boolean hasAmmo(PlayerEntity player, Predicate<ItemStack> ammoFunc) {
		return !getAmmo(player, ammoFunc).isEmpty();
	}

	public static void consumeAmmo(PlayerEntity player, Predicate<ItemStack> ammoFunc) {
		ItemStack ammo = getAmmo(player, ammoFunc);
		if (!ammo.isEmpty()) {
			ammo.decrement(1);
		}
	}

	public static boolean hasItem(PlayerEntity player, Predicate<ItemStack> itemFunc) {
		for (int i = 0; i < player.inventory.size(); i++) {
			if (itemFunc.test(player.inventory.getStack(i))) {
				return true;
			}
		}
		return false;
	}

	public static void grantCriterion(ServerPlayerEntity player, Identifier advancementId, String criterion) {
		PlayerAdvancementTracker advancements = player.getAdvancementTracker();
		ServerAdvancementLoader manager = player.getServerWorld().getServer().getAdvancementLoader();
		Advancement advancement = manager.get(advancementId);
		if (advancement != null) {
			advancements.grantCriterion(advancement, criterion);
		}
	}

	public static ActionResult substituteUse(ItemUsageContext ctx, ItemStack toUse) {
		return substituteUseTrackPos(ctx, toUse).getFirst();
	}

	/**
	 * Temporarily swap <code>toUse</code> into the hand of the player, use it, then swap it back out.
	 * This is to ensure that any code in mods' onItemUse that relies on player.getHeldItem(ctx.hand) == ctx.stack
	 * will work as intended.
	 * Properly handles null players, as long as the Item's onItemUse also handles them.
	 * 
	 * @return The usage result, as well as a properly offset block position pointing at where the block was placed
	 *         (if the item was a BlockItem)
	 */
	public static Pair<ActionResult, BlockPos> substituteUseTrackPos(ItemUsageContext ctx, ItemStack toUse) {
		ItemStack save = ItemStack.EMPTY;
		BlockHitResult hit = new BlockHitResult(ctx.getHitPos(), ctx.getSide(), ctx.getBlockPos(), ctx.hitsInsideBlock());
		ItemUsageContext newCtx;

		if (ctx.getPlayer() != null) {
			save = ctx.getPlayer().getStackInHand(ctx.getHand());
			ctx.getPlayer().setStackInHand(ctx.getHand(), toUse);
			// Need to construct a new one still to refresh the itemstack
			newCtx = new ItemUsageContext(ctx.getPlayer(), ctx.getHand(), hit);
		} else {
			newCtx = new ItemUseContextWithNullPlayer(ctx.getWorld(), ctx.getHand(), toUse, hit);
		}

		BlockPos finalPos = new ItemPlacementContext(newCtx).getBlockPos();

		ActionResult result = toUse.useOnBlock(newCtx);

		if (ctx.getPlayer() != null) {
			ctx.getPlayer().setStackInHand(ctx.getHand(), save);
		}

		return Pair.of(result, finalPos);
	}

	// To expose protected ctor
	private static class ItemUseContextWithNullPlayer extends ItemUsageContext {
		public ItemUseContextWithNullPlayer(World world, Hand hand, ItemStack stack, BlockHitResult rayTraceResult) {
			super(world, null, hand, stack, rayTraceResult);
		}
	}

	private PlayerHelper() {}
}
