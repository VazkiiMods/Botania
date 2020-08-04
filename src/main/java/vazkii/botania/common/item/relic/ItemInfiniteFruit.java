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
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.world.World;

import vazkii.botania.api.mana.IManaUsingItem;
import vazkii.botania.api.mana.ManaItemHandler;
import vazkii.botania.mixin.AccessorLivingEntity;

import javax.annotation.Nonnull;

import java.util.Locale;

import static vazkii.botania.common.lib.ResourceLocationHelper.prefix;

public class ItemInfiniteFruit extends ItemRelic implements IManaUsingItem {

	public ItemInfiniteFruit(Settings props) {
		super(props);
	}

	@Override
	public int getMaxUseTime(ItemStack stack) {
		return 32;
	}

	@Nonnull
	@Override
	public UseAction getUseAction(ItemStack stack) {
		return isBoot(stack) ? UseAction.DRINK : UseAction.EAT;
	}

	@Nonnull
	@Override
	public TypedActionResult<ItemStack> use(World world, PlayerEntity player, @Nonnull Hand hand) {
		ItemStack stack = player.getStackInHand(hand);
		if (player.canConsume(false) && isRightPlayer(player, stack)) {
			player.setCurrentHand(hand);
			return TypedActionResult.consume(stack);
		}
		return TypedActionResult.pass(stack);
	}

	@Override
	public void usageTick(@Nonnull World world, @Nonnull LivingEntity living, @Nonnull ItemStack stack, int count) {
		if (!(living instanceof PlayerEntity)) {
			return;
		}
		PlayerEntity player = (PlayerEntity) living;
		if (ManaItemHandler.instance().requestManaExact(stack, player, 500, true)) {
			if (count % 5 == 0) {
				player.getHungerManager().add(1, 1F);
			}

			if (count == 5) {
				if (player.canConsume(false)) {
					((AccessorLivingEntity) player).setActiveItemStackUseCount(20);
				}
			}
		}
	}

	public static boolean isBoot(ItemStack stack) {
		String name = stack.getName().getString().toLowerCase(Locale.ROOT).trim();
		return name.equals("das boot");
	}

	@Override
	public boolean usesMana(ItemStack stack) {
		return true;
	}

	@Override
	public Identifier getAdvancement() {
		return prefix("challenge/infinite_fruit");
	}

}
