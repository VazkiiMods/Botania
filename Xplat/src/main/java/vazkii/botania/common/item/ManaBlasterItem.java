/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.item;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import org.jetbrains.annotations.Nullable;

import vazkii.botania.api.mana.BasicLensItem;
import vazkii.botania.api.mana.BurstProperties;
import vazkii.botania.api.mana.ControlLensItem;
import vazkii.botania.api.mana.ManaItemHandler;
import vazkii.botania.client.gui.ItemsRemainingRenderHandler;
import vazkii.botania.client.gui.TooltipHandler;
import vazkii.botania.common.advancements.ManaBlasterTrigger;
import vazkii.botania.common.component.BotaniaDataComponents;
import vazkii.botania.common.entity.ManaBurstEntity;
import vazkii.botania.common.handler.BotaniaSounds;
import vazkii.botania.common.handler.EquipmentHandler;
import vazkii.botania.common.helper.DataComponentHelper;
import vazkii.botania.common.proxy.Proxy;

import java.util.List;

public class ManaBlasterItem extends Item {

	public static final int CLIP_SLOTS = 6;
	private static final int COOLDOWN = 30;

	public ManaBlasterItem(Properties props) {
		super(props);
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
		ItemStack stack = player.getItemInHand(hand);
		int effCd = COOLDOWN;
		MobEffectInstance effect = player.getEffect(MobEffects.DIG_SPEED);
		if (effect != null) {
			effCd = Math.max(2, COOLDOWN - (effect.getAmplifier() + 1) * 8);
		}

		if (player.isSecondaryUseActive() && hasClip(stack)) {
			rotatePos(stack);
			world.playSound(null, player.getX(), player.getY(), player.getZ(), BotaniaSounds.manaBlasterCycle, SoundSource.PLAYERS, 0.6F, (1.0F + (world.random.nextFloat() - world.random.nextFloat()) * 0.2F) * 0.7F);
			if (!world.isClientSide) {
				ItemStack lens = getLens(stack);
				ItemsRemainingRenderHandler.send(player, lens, -2);
				setCooldown(stack, effCd);
			}
			return InteractionResultHolder.sidedSuccess(stack, world.isClientSide);
		} else if (getCooldown(stack) <= 0) {
			ManaBurstEntity burst = getBurst(player, stack, true, hand);
			if (burst != null && ManaItemHandler.instance().requestManaExact(stack, player, burst.getMana(), true)) {
				if (!world.isClientSide) {
					world.playSound(null, player.getX(), player.getY(), player.getZ(), BotaniaSounds.manaBlaster, SoundSource.PLAYERS, 1F, 1);
					world.addFreshEntity(burst);
					ManaBlasterTrigger.INSTANCE.trigger((ServerPlayer) player, stack);
					player.awardStat(Stats.ITEM_USED.get(this));
					setCooldown(stack, effCd);
				} else if (!EquipmentHandler.getAllWorn(player).hasAnyMatching(k -> k.is(BotaniaItems.knockbackBelt))) {
					player.setDeltaMovement(player.getDeltaMovement().subtract(burst.getDeltaMovement().multiply(0.1, 0.3, 0.1)));
				}
			} else {
				player.playSound(BotaniaSounds.manaBlasterMisfire, 0.6F, (1.0F + (world.random.nextFloat() - world.random.nextFloat()) * 0.2F) * 0.7F);
			}
			return InteractionResultHolder.sidedSuccess(stack, world.isClientSide);
		}

		return InteractionResultHolder.pass(stack);
	}

	public BurstProperties getBurstProps(Player player, ItemStack stack, boolean request, InteractionHand hand) {
		int maxMana = 120;
		int color = 0x20FF20;
		int ticksBeforeManaLoss = 60;
		float manaLossPerTick = 4F;
		float motionModifier = 5F;
		float gravity = 0F;
		BurstProperties props = new BurstProperties(maxMana, ticksBeforeManaLoss, manaLossPerTick, gravity, motionModifier, color);

		ItemStack lens = getLens(stack);
		if (!lens.isEmpty()) {
			((BasicLensItem) lens.getItem()).apply(lens, props, player.level());
		}
		return props;
	}

	@Nullable
	private ManaBurstEntity getBurst(Player player, ItemStack stack, boolean request, InteractionHand hand) {
		ManaBurstEntity burst = new ManaBurstEntity(player);
		BurstProperties props = getBurstProps(player, stack, request, hand);

		burst.setSourceLens(getLens(stack));
		if (!request || ManaItemHandler.instance().requestManaExact(stack, player, props.maxMana, false)) {
			burst.setColor(props.color);
			burst.setMana(props.maxMana);
			burst.setStartingMana(props.maxMana);
			burst.setMinManaLoss(props.ticksBeforeManaLoss);
			burst.setManaLossPerTick(props.manaLossPerTick);
			burst.setGravity(props.gravity);
			burst.setDeltaMovement(burst.getDeltaMovement().scale(props.motionModifier));

			return burst;
		}
		return null;
	}

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flags) {
		boolean clip = hasClip(stack);
		if (clip) {
			TooltipHandler.addOnShift(tooltip, () -> appendHoverTextImpl(stack, tooltip));
		} else {
			appendHoverTextImpl(stack, tooltip);
		}
	}

	private void appendHoverTextImpl(ItemStack stack, List<Component> tooltip) {
		boolean clip = hasClip(stack);
		if (clip && !Screen.hasShiftDown()) {
			tooltip.add(TooltipHandler.getShiftInfoTooltip());
			return;
		}

		ItemStack lens = getLens(stack);
		if (!lens.isEmpty()) {
			//todo idk
			List<Component> lensTip = lens.getTooltipLines(Item.TooltipContext.of(Proxy.INSTANCE.getClientPlayer().level()), Proxy.INSTANCE.getClientPlayer(), TooltipFlag.Default.NORMAL);
			if (lensTip.size() > 1) {
				tooltip.addAll(lensTip.subList(1, lensTip.size()));
			}
		}

		if (clip) {
			int pos = getClipPos(stack);
			tooltip.add(Component.translatable("botaniamisc.hasClip"));
			List<ItemStack> lenses = getAllLens(stack);
			for (int i = 0; i < CLIP_SLOTS; i++) {
				ItemStack lensAt = lenses.get(i);

				Component name;
				if (lensAt.isEmpty()) {
					name = Component.translatable("botaniamisc.clipEmpty");
				} else {
					name = lensAt.getHoverName();
				}

				MutableComponent tip = Component.literal(" - ").append(name);
				tip.withStyle(i == pos ? ChatFormatting.GREEN : ChatFormatting.GRAY);
				tooltip.add(tip);
			}
		}
	}

	@Override
	public Component getName(ItemStack stack) {
		ItemStack lens = getLens(stack);
		MutableComponent cmp = super.getName(stack).copy();
		if (!lens.isEmpty()) {
			cmp.append(" (");
			cmp.append(lens.getHoverName().copy().withStyle(ChatFormatting.GREEN));
			cmp.append(")");
		}
		return cmp;
	}

	public static boolean hasClip(ItemStack stack) {
		return stack.has(BotaniaDataComponents.CLIP);
	}

	public static void setClip(ItemStack stack, boolean clip) {
		DataComponentHelper.setFlag(stack, BotaniaDataComponents.CLIP, clip);
	}

	public static int getClipPos(ItemStack stack) {
		return stack.getOrDefault(BotaniaDataComponents.CLIP_POS, 0);
	}

	public static void setClipPos(ItemStack stack, int pos) {
		DataComponentHelper.setIntNonZero(stack, BotaniaDataComponents.CLIP_POS, pos);
	}

	public static void rotatePos(ItemStack stack) {
		int currPos = getClipPos(stack);
		boolean acceptEmpty = !getLensAtPos(stack, currPos).isEmpty();
		int[] slots = new int[CLIP_SLOTS - 1];

		int index = 0;
		for (int i = currPos + 1; i < CLIP_SLOTS; i++, index++) {
			slots[index] = i;
		}
		for (int i = 0; i < currPos; i++, index++) {
			slots[index] = i;
		}

		for (int i : slots) {
			ItemStack lensAt = getLensAtPos(stack, i);
			if (acceptEmpty || !lensAt.isEmpty()) {
				setClipPos(stack, i);
				return;
			}
		}
	}

	public static ItemStack getLensAtPos(ItemStack stack, int pos) {
		List<ItemStack> lenses = stack.get(BotaniaDataComponents.ATTACHED_LENSES);
		if (lenses != null && lenses.size() > pos) {
			return lenses.get(pos);
		}
		return ItemStack.EMPTY;
	}

	public static void setLensAtPos(ItemStack stack, ItemStack lens, int pos) {
		List<ItemStack> lenses = stack.get(BotaniaDataComponents.ATTACHED_LENSES);
		List<ItemStack> newLenses = NonNullList.withSize(CLIP_SLOTS, ItemStack.EMPTY);
		for (int i = 0; i < CLIP_SLOTS; i++) {
			if (i == pos) {
				newLenses.set(i, lens);
			} else if (lenses != null && i < lenses.size()) {
				ItemStack newLens = lenses.get(i);
				if (!newLens.isEmpty()) {
					newLenses.set(i, newLens);
				}
			}

		}
		stack.set(BotaniaDataComponents.ATTACHED_LENSES, newLenses);
	}

	public static void setLens(ItemStack stack, ItemStack lens) {
		if (hasClip(stack)) {
			setLensAtPos(stack, lens, getClipPos(stack));
		}

		DataComponentHelper.setNonEmpty(stack, BotaniaDataComponents.ATTACHED_LENS, lens);
	}

	public static ItemStack getLens(ItemStack stack) {
		if (hasClip(stack)) {
			return getLensAtPos(stack, getClipPos(stack));
		}

		return DataComponentHelper.getSingleItem(stack, BotaniaDataComponents.ATTACHED_LENS);
	}

	public static boolean isValidLens(ItemStack lens) {
		var item = lens.getItem();
		if (!(item instanceof BasicLensItem)) {
			return false;
		}
		if (item instanceof ControlLensItem control && control.isControlLens(lens)) {
			return false;
		}
		return true;
	}

	public static List<ItemStack> getAllLens(ItemStack stack) {
		List<ItemStack> lenses = stack.get(BotaniaDataComponents.ATTACHED_LENSES);
		return lenses != null ? lenses : NonNullList.withSize(CLIP_SLOTS, ItemStack.EMPTY);
	}

	@Override
	public void inventoryTick(ItemStack stack, Level world, Entity entity, int slot, boolean selected) {
		if (getCooldown(stack) > 0) {
			setCooldown(stack, getCooldown(stack) - 1);
		}
	}

	@Override
	public boolean isBarVisible(ItemStack stack) {
		return getCooldown(stack) > 0;
	}

	@Override
	public int getBarWidth(ItemStack stack) {
		return Math.round(13.0F * (1 - getCooldown(stack) / (float) COOLDOWN));
	}

	@Override
	public int getBarColor(ItemStack stack) {
		return Mth.hsvToRgb((1 - getCooldown(stack) / (float) COOLDOWN) / 3.0F, 1.0F, 1.0F);
	}

	private int getCooldown(ItemStack stack) {
		return stack.getOrDefault(BotaniaDataComponents.COOLDOWN, 0);
	}

	private void setCooldown(ItemStack stack, int cooldown) {
		DataComponentHelper.setIntNonZero(stack, BotaniaDataComponents.COOLDOWN, cooldown);
	}
}
