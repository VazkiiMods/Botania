/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.item;

import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.PlayerEnderChestContainer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;

import org.jetbrains.annotations.Nullable;

import vazkii.botania.api.item.BlockProvider;
import vazkii.botania.api.mana.ManaItemHandler;
import vazkii.botania.client.gui.enderhand.HandOfEnderMenu;
import vazkii.botania.common.item.rod.ShiftingCrustRodItem;
import vazkii.botania.xplat.BotaniaConfig;
import vazkii.botania.xplat.XplatAbstractions;

public class EnderHandItem extends Item {

	private static final int COST_PROVIDE = 5;
	private static final int COST_SELF = 250;
	private static final int COST_OTHER = 5000;

	public EnderHandItem(Properties props) {
		super(props);
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
		ItemStack enderHandStack = player.getItemInHand(hand);
		if (ManaItemHandler.instance().requestManaExact(enderHandStack, player, COST_SELF, false)) {
			openEnderChestMenu(player, hand, enderHandStack, player.getEnderChestInventory(), COST_SELF,
					Component.translatable("container.enderchest"));
			return InteractionResultHolder.sidedSuccess(enderHandStack, world.isClientSide());
		}
		return InteractionResultHolder.pass(enderHandStack);
	}

	@Override
	public InteractionResult interactLivingEntity(ItemStack enderHandStack, Player player, LivingEntity entity, InteractionHand hand) {
		if (entity.isAlive() && BotaniaConfig.common().enderPickpocketEnabled() && entity instanceof Player other
				&& ManaItemHandler.instance().requestManaExact(enderHandStack, player, COST_OTHER, false)) {
			openEnderChestMenu(player, hand, enderHandStack, other.getEnderChestInventory(), COST_OTHER,
					Component.translatable("botaniamisc.enderPickpocketing", other.getDisplayName()));
			return InteractionResult.sidedSuccess(player.level().isClientSide());
		}

		return InteractionResult.PASS;
	}

	private void openEnderChestMenu(Player player, InteractionHand hand, ItemStack enderHandStack,
			PlayerEnderChestContainer enderChestContainer, int manaCost, Component displayName) {
		if (!player.level().isClientSide()) {
			XplatAbstractions.INSTANCE.openMenu(
					(ServerPlayer) player,
					new EnderHandMenuProvider(enderChestContainer, hand == InteractionHand.MAIN_HAND, displayName),
					hand == InteractionHand.MAIN_HAND,
					ByteBufCodecs.BOOL);
			ManaItemHandler.instance().requestManaExact(enderHandStack, player, manaCost, true);
		}
		player.playSound(SoundEvents.ENDER_CHEST_OPEN, 1F, 1F);
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

		@Nullable
		@Override
		public Block getProvidedBlock(Player player, ItemStack requestor) {
			return null;
		}
	}

	private record EnderHandMenuProvider(PlayerEnderChestContainer enderChestContainer, boolean isMainHand,
			Component displayName) implements MenuProvider {

		@Override
		public Component getDisplayName() {
			return displayName;
		}

		@Override
		public AbstractContainerMenu createMenu(int containerId, Inventory playerInventory, Player player) {
			return new HandOfEnderMenu(containerId, playerInventory, enderChestContainer, isMainHand);
		}
	}
}
