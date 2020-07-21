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
import net.minecraft.block.EnderChestBlock;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.GenericContainerScreenHandler;
import net.minecraft.screen.SimpleNamedScreenHandlerFactory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import net.minecraftforge.items.wrapper.InvWrapper;

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

	public ItemEnderHand(Settings props) {
		super(props);
	}

	@Nonnull
	@Override
	public TypedActionResult<ItemStack> use(World world, PlayerEntity player, @Nonnull Hand hand) {
		ItemStack stack = player.getStackInHand(hand);
		if (ManaItemHandler.instance().requestManaExact(stack, player, COST_SELF, false)) {
			if (!player.world.isClient) {
				player.openHandledScreen(new SimpleNamedScreenHandlerFactory((windowId, playerInv, p) -> {
					return GenericContainerScreenHandler.createGeneric9x3(windowId, playerInv, p.getEnderChestInventory());
				}, EnderChestBlock.CONTAINER_NAME));
				ManaItemHandler.instance().requestManaExact(stack, player, COST_SELF, true);
			}
			player.playSound(SoundEvents.BLOCK_ENDER_CHEST_OPEN, 1F, 1F);
			return TypedActionResult.success(stack);
		}
		return TypedActionResult.pass(stack);
	}

	@Override
	public ActionResult useOnEntity(ItemStack stack, PlayerEntity player, LivingEntity entity, Hand hand) {
		if (ConfigHandler.COMMON.enderPickpocketEnabled.get() && entity instanceof PlayerEntity && ManaItemHandler.instance().requestManaExact(stack, player, COST_OTHER, false)) {
			if (!player.world.isClient) {
				PlayerEntity other = (PlayerEntity) entity;
				player.openHandledScreen(new SimpleNamedScreenHandlerFactory((windowId, playerInv, p) -> {
					return GenericContainerScreenHandler.createGeneric9x3(windowId, playerInv, other.getEnderChestInventory());
				}, EnderChestBlock.CONTAINER_NAME));
			}
			ManaItemHandler.instance().requestManaExact(stack, player, COST_OTHER, true);
			player.playSound(SoundEvents.BLOCK_ENDER_CHEST_OPEN, 1F, 1F);
			return ActionResult.SUCCESS;
		}

		return ActionResult.PASS;
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

		ItemStack istack = ItemExchangeRod.removeFromInventory(player, player.getEnderChestInventory(), stack, block, false);
		if (!istack.isEmpty()) {
			boolean mana = ManaItemHandler.instance().requestManaExact(stack, player, COST_PROVIDE, false);
			if (mana) {
				if (doit) {
					ManaItemHandler.instance().requestManaExact(stack, player, COST_PROVIDE, true);
					ItemExchangeRod.removeFromInventory(player, player.getEnderChestInventory(), stack, block, true);
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

		return ItemExchangeRod.getInventoryItemCount(player, player.getEnderChestInventory(), stack, block);
	}

}
