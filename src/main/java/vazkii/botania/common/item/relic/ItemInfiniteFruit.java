/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.item.relic;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;

import vazkii.botania.api.mana.ManaItemHandler;
import vazkii.botania.mixin.AccessorLivingEntity;

import javax.annotation.Nonnull;

import java.util.Locale;

import static vazkii.botania.common.lib.ResourceLocationHelper.prefix;

public class ItemInfiniteFruit extends ItemRelic {

	public ItemInfiniteFruit(Properties props) {
		super(props);
	}

	@Override
	public int getUseDuration(ItemStack stack) {
		return 32;
	}

	@Nonnull
	@Override
	public UseAnim getUseAnimation(ItemStack stack) {
		return isBoot(stack) ? UseAnim.DRINK : UseAnim.EAT;
	}

	@Nonnull
	@Override
	public InteractionResultHolder<ItemStack> use(Level world, Player player, @Nonnull InteractionHand hand) {
		ItemStack stack = player.getItemInHand(hand);
		if (player.canEat(false) && isRightPlayer(player, stack)) {
			return ItemUtils.startUsingInstantly(world, player, hand);
		}
		return InteractionResultHolder.pass(stack);
	}

	@Override
	public void onUseTick(@Nonnull Level world, @Nonnull LivingEntity living, @Nonnull ItemStack stack, int count) {
		if (!(living instanceof Player player)) {
			return;
		}
		if (ManaItemHandler.instance().requestManaExact(stack, player, 500, true)) {
			if (count % 5 == 0) {
				player.getFoodData().eat(2, 2.4F);
			}

			if (count == 5) {
				if (player.canEat(false)) {
					((AccessorLivingEntity) player).setUseItemRemaining(20);
				}
			}
		}
	}

	public static boolean isBoot(ItemStack stack) {
		String name = stack.getHoverName().getString().toLowerCase(Locale.ROOT).trim();
		return name.equals("das boot");
	}

	@Override
	public ResourceLocation getAdvancement() {
		return prefix("challenge/infinite_fruit");
	}

}
