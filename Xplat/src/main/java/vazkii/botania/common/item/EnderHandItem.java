/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.item;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;

import org.jetbrains.annotations.NotNull;

import vazkii.botania.api.item.BlockProvider;
import vazkii.botania.api.mana.ManaItemHandler;
import vazkii.botania.common.item.rod.ShiftingCrustRodItem;
import vazkii.botania.xplat.BotaniaConfig;

public class EnderHandItem extends Item {

	private static final int COST_PROVIDE = 5;
	private static final int COST_SELF = 250;
	private static final int COST_OTHER = 5000;

	public EnderHandItem(Properties props) {
		super(props);
	}

	@NotNull
	@Override
	public InteractionResultHolder<ItemStack> use(Level world, Player player, @NotNull InteractionHand hand) {
		ItemStack stack = player.getItemInHand(hand);
		if (ManaItemHandler.instance().requestManaExact(stack, player, COST_SELF, false)) {
			if (!player.getLevel().isClientSide) {
				player.openMenu(new SimpleMenuProvider((windowId, playerInv, p) -> {
					return ChestMenu.threeRows(windowId, playerInv, p.getEnderChestInventory());
				}, stack.getHoverName()));
				ManaItemHandler.instance().requestManaExact(stack, player, COST_SELF, true);
			}
			player.playSound(SoundEvents.ENDER_CHEST_OPEN, 1F, 1F);
			return InteractionResultHolder.sidedSuccess(stack, world.isClientSide());
		}
		return InteractionResultHolder.pass(stack);
	}

	@Override
	public InteractionResult interactLivingEntity(ItemStack stack, Player player, LivingEntity entity, InteractionHand hand) {
		if (entity.isAlive() && BotaniaConfig.common().enderPickpocketEnabled() && entity instanceof Player other && ManaItemHandler.instance().requestManaExact(stack, player, COST_OTHER, false)) {
			if (!player.getLevel().isClientSide) {
				player.openMenu(new SimpleMenuProvider((windowId, playerInv, p) -> ChestMenu.threeRows(windowId, playerInv, other.getEnderChestInventory()), stack.getHoverName()));
				ManaItemHandler.instance().requestManaExact(stack, player, COST_OTHER, true);
			}
			player.playSound(SoundEvents.ENDER_CHEST_OPEN, 1F, 1F);
			return InteractionResult.sidedSuccess(player.getLevel().isClientSide());
		}

		return InteractionResult.PASS;
	}

	public static class BlockProviderImpl implements BlockProvider {
		private final ItemStack stack;

		public BlockProviderImpl(ItemStack stack) {
			this.stack = stack;
		}

		@Override
		public boolean provideBlock(Player player, ItemStack requestor, Block block, boolean doit) {
			if (!requestor.isEmpty() && requestor.is(stack.getItem())) {
				return false;
			}

			ItemStack istack = ShiftingCrustRodItem.removeFromInventory(player, player.getEnderChestInventory(), stack, block.asItem(), false);
			if (!istack.isEmpty()) {
				boolean mana = ManaItemHandler.instance().requestManaExact(stack, player, COST_PROVIDE, false);
				if (mana) {
					if (doit) {
						ManaItemHandler.instance().requestManaExact(stack, player, COST_PROVIDE, true);
						ShiftingCrustRodItem.removeFromInventory(player, player.getEnderChestInventory(), stack, block.asItem(), true);
					}

					return true;
				}
			}

			return false;
		}

		@Override
		public int getBlockCount(Player player, ItemStack requestor, Block block) {
			if (!requestor.isEmpty() && requestor.is(stack.getItem())) {
				return 0;
			}

			return ShiftingCrustRodItem.getInventoryItemCount(player, player.getEnderChestInventory(), stack, block.asItem());
		}
	}

}
