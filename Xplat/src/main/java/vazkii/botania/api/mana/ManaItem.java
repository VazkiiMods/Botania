/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.api.mana;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;

import vazkii.botania.api.capability.ItemApiNoContext;

import static vazkii.botania.api.BotaniaAPI.botaniaRL;

/**
 * An item that has this capability can contain mana or specify how it interacts with mana it might receive.
 */
public interface ManaItem {

	ResourceLocation ID = botaniaRL("mana_item");
	ItemApiNoContext<ManaItem> LOOKUP = new ItemApiNoContext<>(ID, ManaItem.class);

	/**
	 * Gets the amount of mana this item contains
	 */
	int getMana();

	/**
	 * Gets the max amount of mana this item can hold.
	 */
	int getMaxMana();

	/**
	 * Adds mana to this item.
	 */
	void addMana(int mana);

	/**
	 * Can this item receive mana from a mana Pool?
	 * 
	 * @param pool The pool it's receiving mana from, can be casted to ManaPool.
	 * @see ManaPool#isOutputtingPower()
	 */
	boolean canReceiveManaFromPool(BlockEntity pool);

	/**
	 * Does this item accept mana dispatched from another item?
	 * Mana sent by dispersive sparks specify a "virtual" item stack of the spark's item type.
	 *
	 * @param otherStack The item sending the mana, likely not a ManaItem itself.
	 */
	boolean acceptDispatchedManaFromItem(ItemStack otherStack);

	/**
	 * Does this item refuse mana it requested if it comes from the specified item?
	 *
	 * @param otherStack The item that offers the mana, usually a ManaItem.
	 */
	boolean refuseRequestedManaFromItem(ItemStack otherStack);

	/**
	 * Can this item's mana be drained into a mana pool?
	 * 
	 * @param pool The pool it's exporting mana to, can be casted to ManaPool.
	 * @see ManaPool#isOutputtingPower()
	 */
	boolean canDrainManaToPool(BlockEntity pool);

	/**
	 * Can this item send mana to another item that requested it?
	 *
	 * @param otherStack The item that requested the mana, likely not a ManaItem itself.
	 */
	boolean canSendRequestedManaToItem(ItemStack otherStack);

	/**
	 * If this item simply does not export mana at all, set this to true. This is
	 * used to skip items that contain mana but can't export it when drawing the
	 * mana bar above the XP bar.
	 */
	boolean isNoExport();
}
