/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.item;

import net.minecraft.block.Block;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.container.ChestContainer;
import net.minecraft.inventory.container.SimpleNamedContainerProvider;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundEvents;
import net.minecraft.world.World;

import vazkii.botania.api.item.IBlockProvider;
import vazkii.botania.api.mana.IManaUsingItem;
import vazkii.botania.api.mana.ManaItemHandler;
import vazkii.botania.common.core.handler.ConfigHandler;
import vazkii.botania.common.item.rod.ItemExchangeRod;

import javax.annotation.Nonnull;

public class ItemEnderHand extends Item implements IManaUsingItem, IBlockProvider {

	private static final int COST_PROVIDE = 5;
	private static final int COST_SELF = 250;
	private static final int COST_OTHER = 5000;

	public ItemEnderHand(Properties props) {
		super(props);
	}

	@Nonnull
	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, @Nonnull Hand hand) {
		ItemStack stack = player.getHeldItem(hand);
		if (ManaItemHandler.instance().requestManaExact(stack, player, COST_SELF, false)) {
			if (!player.world.isRemote) {
				player.openContainer(new SimpleNamedContainerProvider((windowId, playerInv, p) -> {
					return ChestContainer.createGeneric9X3(windowId, playerInv, p.getInventoryEnderChest());
				}, stack.getDisplayName()));
				ManaItemHandler.instance().requestManaExact(stack, player, COST_SELF, true);
			}
			player.playSound(SoundEvents.BLOCK_ENDER_CHEST_OPEN, 1F, 1F);
			return ActionResult.resultSuccess(stack);
		}
		return ActionResult.resultPass(stack);
	}

	@Override
	public ActionResultType itemInteractionForEntity(ItemStack stack, PlayerEntity player, LivingEntity entity, Hand hand) {
		if (ConfigHandler.COMMON.enderPickpocketEnabled.get() && entity instanceof PlayerEntity && ManaItemHandler.instance().requestManaExact(stack, player, COST_OTHER, false)) {
			if (!player.world.isRemote) {
				PlayerEntity other = (PlayerEntity) entity;
				player.openContainer(new SimpleNamedContainerProvider((windowId, playerInv, p) -> {
					return ChestContainer.createGeneric9X3(windowId, playerInv, other.getInventoryEnderChest());
				}, stack.getDisplayName()));
			}
			ManaItemHandler.instance().requestManaExact(stack, player, COST_OTHER, true);
			player.playSound(SoundEvents.BLOCK_ENDER_CHEST_OPEN, 1F, 1F);
			return ActionResultType.SUCCESS;
		}

		return ActionResultType.PASS;
	}

	@Override
	public boolean usesMana(ItemStack stack) {
		return true;
	}

	@Override
	public boolean provideBlock(PlayerEntity player, ItemStack requestor, ItemStack stack, Block block, boolean doit) {
		if (!requestor.isEmpty() && requestor.getItem() == this) {
			return false;
		}

		ItemStack istack = ItemExchangeRod.removeFromInventory(player, player.getInventoryEnderChest(), stack, block, false);
		if (!istack.isEmpty()) {
			boolean mana = ManaItemHandler.instance().requestManaExact(stack, player, COST_PROVIDE, false);
			if (mana) {
				if (doit) {
					ManaItemHandler.instance().requestManaExact(stack, player, COST_PROVIDE, true);
					ItemExchangeRod.removeFromInventory(player, player.getInventoryEnderChest(), stack, block, true);
				}

				return true;
			}
		}

		return false;
	}

	@Override
	public int getBlockCount(PlayerEntity player, ItemStack requestor, ItemStack stack, Block block) {
		if (!requestor.isEmpty() && requestor.getItem() == this) {
			return 0;
		}

		return ItemExchangeRod.getInventoryItemCount(player, player.getInventoryEnderChest(), stack, block);
	}

}
