/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.item.relic;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;

import vazkii.botania.api.item.Relic;
import vazkii.botania.api.mana.ManaItemHandler;
import vazkii.botania.mixin.LivingEntityAccessor;

import java.util.Locale;

import static vazkii.botania.api.BotaniaAPI.botaniaRL;

public class FruitOfGrisaiaItem extends RelicItem {

	public static final int MANA_PER_USE_TICK = 500;

	public FruitOfGrisaiaItem(Properties properties) {
		super(properties);
	}

	@Override
	public int getUseDuration(ItemStack stack, LivingEntity entity) {
		return 32;
	}

	@Override
	public UseAnim getUseAnimation(ItemStack stack) {
		return isBoot(stack) ? UseAnim.DRINK : UseAnim.EAT;
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
		ItemStack stack = player.getItemInHand(usedHand);
		var relic = Relic.LOOKUP.find(stack);
		if (player.canEat(false) && relic != null && relic.isRightPlayer(player)
		// no point starting to eat if not enough mana to reach the first effect tick:
				&& ManaItemHandler.instance().requestManaExact(stack, player, 4 * MANA_PER_USE_TICK, false)) {
			return ItemUtils.startUsingInstantly(level, player, usedHand);
		}
		return InteractionResultHolder.pass(stack);
	}

	@Override
	public void onUseTick(Level level, LivingEntity livingEntity, ItemStack stack, int remainingUseDuration) {
		if (!(livingEntity instanceof Player player)) {
			return;
		}
		if (ManaItemHandler.instance().requestManaExact(stack, player, MANA_PER_USE_TICK, true)) {
			if (remainingUseDuration % 5 == 0) {
				player.gameEvent(GameEvent.EAT);
				player.getFoodData().eat(1, 1.2F);
			}

			if (remainingUseDuration == 5) {
				if (player.canEat(false)) {
					((LivingEntityAccessor) player).botania_setUseItemRemaining(20);
				}
			}
		}
	}

	@Override
	public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity livingEntity) {
		// don't allow actually eating the fruit
		return stack;
	}

	public static boolean isBoot(ItemStack stack) {
		String name = stack.getHoverName().getString().toLowerCase(Locale.ROOT).trim();
		return name.equals("das boot");
	}

	public static Relic makeRelic(ItemStack stack) {
		return new RelicImpl(stack, botaniaRL("challenge/the_fruit_of_grisaia"));
	}

}
