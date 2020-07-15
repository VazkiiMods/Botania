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

import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementManager;
import net.minecraft.advancements.PlayerAdvancements;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;

import java.util.function.Predicate;

public final class PlayerHelper {

	// Checks if either of the player's hands has an item.
	public static boolean hasAnyHeldItem(PlayerEntity player) {
		return !player.getHeldItemMainhand().isEmpty() || !player.getHeldItemOffhand().isEmpty();
	}

	// Checks main hand, then off hand for this item.
	public static boolean hasHeldItem(PlayerEntity player, Item item) {
		return !player.getHeldItemMainhand().isEmpty() && player.getHeldItemMainhand().getItem() == item
				|| !player.getHeldItemOffhand().isEmpty() && player.getHeldItemOffhand().getItem() == item;
	}

	// Checks main hand, then off hand for this item class.
	public static boolean hasHeldItemClass(PlayerEntity player, Item template) {
		return hasHeldItemClass(player, template.getClass());
	}

	// Checks main hand, then off hand for this item class.
	public static boolean hasHeldItemClass(PlayerEntity player, Class<?> template) {
		return !player.getHeldItemMainhand().isEmpty() && template.isAssignableFrom(player.getHeldItemMainhand().getItem().getClass())
				|| !player.getHeldItemOffhand().isEmpty() && template.isAssignableFrom(player.getHeldItemOffhand().getItem().getClass());
	}

	public static ItemStack getFirstHeldItem(LivingEntity living, Item item) {
		return getFirstHeldItem(living, s -> s.getItem() == item);
	}

	public static ItemStack getFirstHeldItem(LivingEntity living, Predicate<ItemStack> pred) {
		ItemStack main = living.getHeldItemMainhand();
		ItemStack offhand = living.getHeldItemOffhand();
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
		if (ammoFunc.test(player.getHeldItem(Hand.OFF_HAND))) {
			return player.getHeldItem(Hand.OFF_HAND);
		} else if (ammoFunc.test(player.getHeldItem(Hand.MAIN_HAND))) {
			return player.getHeldItem(Hand.MAIN_HAND);
		} else {
			for (int i = 0; i < player.inventory.getSizeInventory(); ++i) {
				ItemStack itemstack = player.inventory.getStackInSlot(i);

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
			ammo.shrink(1);
		}
	}

	public static boolean hasItem(PlayerEntity player, Predicate<ItemStack> itemFunc) {
		for (int i = 0; i < player.inventory.getSizeInventory(); i++) {
			if (itemFunc.test(player.inventory.getStackInSlot(i))) {
				return true;
			}
		}
		return false;
	}

	public static void grantCriterion(ServerPlayerEntity player, ResourceLocation advancementId, String criterion) {
		PlayerAdvancements advancements = player.getAdvancements();
		AdvancementManager manager = player.getServerWorld().getServer().getAdvancementManager();
		Advancement advancement = manager.getAdvancement(advancementId);
		if (advancement != null) {
			advancements.grantCriterion(advancement, criterion);
		}
	}

	public static ActionResultType substituteUse(ItemUseContext ctx, ItemStack toUse) {
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
	public static Pair<ActionResultType, BlockPos> substituteUseTrackPos(ItemUseContext ctx, ItemStack toUse) {
		ItemStack save = ItemStack.EMPTY;
		BlockRayTraceResult hit = new BlockRayTraceResult(ctx.getHitVec(), ctx.getFace(), ctx.getPos(), ctx.isInside());
		ItemUseContext newCtx;

		if (ctx.getPlayer() != null) {
			save = ctx.getPlayer().getHeldItem(ctx.getHand());
			ctx.getPlayer().setHeldItem(ctx.getHand(), toUse);
			// Need to construct a new one still to refresh the itemstack
			newCtx = new ItemUseContext(ctx.getPlayer(), ctx.getHand(), hit);
		} else {
			newCtx = new ItemUseContext(ctx.getWorld(), null, ctx.getHand(), toUse, hit);
		}

		BlockPos finalPos = new BlockItemUseContext(newCtx).getPos();

		ActionResultType result = toUse.onItemUse(newCtx);

		if (ctx.getPlayer() != null) {
			ctx.getPlayer().setHeldItem(ctx.getHand(), save);
		}

		return Pair.of(result, finalPos);
	}

	private PlayerHelper() {}
}
