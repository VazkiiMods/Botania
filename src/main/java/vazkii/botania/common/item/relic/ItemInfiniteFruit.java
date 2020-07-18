/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.item.relic;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.UseAction;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import vazkii.botania.api.mana.IManaUsingItem;
import vazkii.botania.api.mana.ManaItemHandler;
import vazkii.botania.common.lib.LibMisc;
import vazkii.botania.mixin.AccessorLivingEntity;

import javax.annotation.Nonnull;

import java.util.Locale;

public class ItemInfiniteFruit extends ItemRelic implements IManaUsingItem {

	public ItemInfiniteFruit(Properties props) {
		super(props);
	}

	@Override
	public int getUseDuration(ItemStack stack) {
		return 32;
	}

	@Nonnull
	@Override
	public UseAction getUseAction(ItemStack stack) {
		return isBoot(stack) ? UseAction.DRINK : UseAction.EAT;
	}

	@Nonnull
	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, @Nonnull Hand hand) {
		ItemStack stack = player.getHeldItem(hand);
		if (player.canEat(false) && isRightPlayer(player, stack)) {
			player.setActiveHand(hand);
			return ActionResult.resultSuccess(stack);
		}
		return ActionResult.resultPass(stack);
	}

	@Override
	public void onUse(@Nonnull World world, @Nonnull LivingEntity living, @Nonnull ItemStack stack, int count) {
		if (!(living instanceof PlayerEntity)) {
			return;
		}
		PlayerEntity player = (PlayerEntity) living;
		if (ManaItemHandler.instance().requestManaExact(stack, player, 500, true)) {
			if (count % 5 == 0) {
				player.getFoodStats().addStats(1, 1F);
			}

			if (count == 5) {
				if (player.canEat(false)) {
					((AccessorLivingEntity) player).setActiveItemStackUseCount(20);
				}
			}
		}
	}

	public static boolean isBoot(ItemStack stack) {
		String name = stack.getDisplayName().getString().toLowerCase(Locale.ROOT).trim();
		return name.equals("das boot");
	}

	@Override
	public boolean usesMana(ItemStack stack) {
		return true;
	}

	@Override
	public ResourceLocation getAdvancement() {
		return new ResourceLocation(LibMisc.MOD_ID, "challenge/infinite_fruit");
	}

}
