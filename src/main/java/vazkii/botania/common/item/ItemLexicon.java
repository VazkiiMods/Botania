/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.item;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.ChatFormatting;
import net.minecraft.core.NonNullList;
import net.minecraft.core.Registry;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;

import vazkii.botania.api.recipe.IElvenItem;
import vazkii.botania.common.advancements.UseItemSuccessTrigger;
import vazkii.botania.common.core.handler.ModSounds;
import vazkii.patchouli.api.PatchouliAPI;

import javax.annotation.Nonnull;

import java.util.List;

public class ItemLexicon extends Item implements IElvenItem {

	public static final String TAG_ELVEN_UNLOCK = "botania:elven_unlock";

	public ItemLexicon(Properties props) {
		super(props);
	}

	public static boolean isOpen() {
		return Registry.ITEM.getKey(ModItems.lexicon).equals(PatchouliAPI.get().getOpenBookGui());
	}

	@Override
	public void fillItemCategory(@Nonnull CreativeModeTab tab, @Nonnull NonNullList<ItemStack> list) {
		if (allowdedIn(tab)) {
			list.add(new ItemStack(this));
			ItemStack creative = new ItemStack(this);
			creative.getOrCreateTag().putBoolean(TAG_ELVEN_UNLOCK, true);
			list.add(creative);
		}
	}

	@Override
	@Environment(EnvType.CLIENT)
	public void appendHoverText(ItemStack stack, Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
		tooltip.add(getEdition().copy().withStyle(ChatFormatting.GRAY));
	}

	@Nonnull
	@Override
	public InteractionResultHolder<ItemStack> use(Level worldIn, Player playerIn, InteractionHand handIn) {
		ItemStack stack = playerIn.getItemInHand(handIn);

		if (playerIn instanceof ServerPlayer) {
			ServerPlayer player = (ServerPlayer) playerIn;
			UseItemSuccessTrigger.INSTANCE.trigger(player, stack, player.getLevel(), player.getX(), player.getY(), player.getZ());
			PatchouliAPI.get().openBookGUI((ServerPlayer) playerIn, Registry.ITEM.getKey(this));
			playerIn.playSound(ModSounds.lexiconOpen, 1F, (float) (0.7 + Math.random() * 0.4));
		}

		return new InteractionResultHolder<>(InteractionResult.SUCCESS, stack);
	}

	public static Component getEdition() {
		return PatchouliAPI.get().getSubtitle(Registry.ITEM.getKey(ModItems.lexicon));
	}

	public static Component getTitle(ItemStack stack) {
		Component title = stack.getHoverName();

		String akashicTomeNBT = "akashictome:displayName";
		if (stack.hasTag() && stack.getTag().contains(akashicTomeNBT)) {
			title = Component.Serializer.fromJson(stack.getTag().getString(akashicTomeNBT));
		}

		return title;
	}

	@Override
	public boolean isElvenItem(ItemStack stack) {
		return stack.hasTag() && stack.getTag().getBoolean(TAG_ELVEN_UNLOCK);
	}

	// Random item to expose this as public
	public static BlockHitResult doRayTrace(Level world, Player player, ClipContext.Fluid fluidMode) {
		return Item.getPlayerPOVHitResult(world, player, fluidMode);
	}
}
