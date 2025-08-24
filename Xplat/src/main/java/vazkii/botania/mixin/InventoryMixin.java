package vazkii.botania.mixin;

import net.minecraft.core.NonNullList;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import vazkii.botania.common.lib.BotaniaTags;
import vazkii.botania.xplat.XplatAbstractions;

@Mixin(Inventory.class)
public class InventoryMixin {
	@Shadow
	@Final
	public Player player;

	@Shadow
	@Final
	public NonNullList<ItemStack> items;

	/**
	 * Also look for block providers when picking a block, if the actual block item could not be found.
	 */
	@Inject(method = "findSlotMatchingItem", at = @At("RETURN"), cancellable = true)
	private void findBlockProviderItem(ItemStack searchStack, CallbackInfoReturnable<Integer> cir) {
		if (this.player.getAbilities().instabuild || cir.getReturnValue() != -1
				|| !(searchStack.getItem() instanceof BlockItem blockItem)) {
			// skip when in creative mode, when an exact match was already found, or when not looking for a block item
			return;
		}

		Block block = blockItem.getBlock();
		for (int i = 0; i < this.items.size(); ++i) {
			ItemStack invStack = this.items.get(i);
			if (!invStack.is(BotaniaTags.Items.PICKABLE_BLOCK_PROVIDER)) {
				continue;
			}
			var provider = XplatAbstractions.INSTANCE.findBlockProvider(invStack);
			if (provider != null && provider.provideBlock(this.player, searchStack, block, false)) {
				cir.setReturnValue(i);
				return;
			}
		}
	}
}
