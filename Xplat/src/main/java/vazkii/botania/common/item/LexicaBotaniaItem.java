/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.item;

import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;

import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Unit;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;

import vazkii.botania.common.advancements.UseItemSuccessTrigger;
import vazkii.botania.common.component.BotaniaDataComponents;
import vazkii.botania.common.handler.BotaniaSounds;
import vazkii.botania.common.helper.DataComponentHelper;
import vazkii.patchouli.api.PatchouliAPI;

import java.util.List;

public class LexicaBotaniaItem extends Item implements CustomCreativeTabContents {
	@SuppressWarnings("unchecked")
	public static final Supplier<DataComponentType<Component>> AKASHIC_DISPLAY_NAME_TYPE_SUPPLIER = Suppliers.memoize(() -> {
		ResourceKey<DataComponentType<?>> resourceKey = ResourceKey.create(Registries.DATA_COMPONENT_TYPE,
				ResourceLocation.fromNamespaceAndPath("akashictome", "og_display_name"));
		return (DataComponentType<Component>) BuiltInRegistries.DATA_COMPONENT_TYPE.get(resourceKey);
	});

	public LexicaBotaniaItem(Properties settings) {
		super(settings);
	}

	public static boolean isOpen() {
		return BuiltInRegistries.ITEM.getKey(BotaniaItems.lexicon).equals(PatchouliAPI.get().getOpenBookGui());
	}

	@Override
	public void addToCreativeTab(Item me, CreativeModeTab.Output output) {
		output.accept(me);

		ItemStack creative = new ItemStack(me);
		creative.set(BotaniaDataComponents.ELVEN_UNLOCK, Unit.INSTANCE);
		DataComponentHelper.bumpRarity(creative);
		output.accept(creative);
	}

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flagIn) {
		tooltip.add(getEdition().copy().withStyle(ChatFormatting.GRAY));
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level worldIn, Player playerIn, InteractionHand handIn) {
		ItemStack stack = playerIn.getItemInHand(handIn);

		if (playerIn instanceof ServerPlayer player) {
			UseItemSuccessTrigger.INSTANCE.trigger(player, stack, player.serverLevel(), player.getX(), player.getY(), player.getZ());
			PatchouliAPI.get().openBookGUI(player, BuiltInRegistries.ITEM.getKey(this));
			playerIn.playSound(BotaniaSounds.lexiconOpen, 1F, (float) (0.7 + Math.random() * 0.4));
		}

		return InteractionResultHolder.sidedSuccess(stack, worldIn.isClientSide());
	}

	public static Component getEdition() {
		try {
			return PatchouliAPI.get().getSubtitle(BuiltInRegistries.ITEM.getKey(BotaniaItems.lexicon));
		} catch (IllegalArgumentException e) {
			return Component.literal(""); // TODO Adjust Patchouli because first search tree creation is too early to get the edition
		}
	}

	public static Component getTitle(ItemStack stack) {
		Component title = stack.getHoverName();

		// TODO: verify
		DataComponentType<Component> ogDisplayNameType = AKASHIC_DISPLAY_NAME_TYPE_SUPPLIER.get();
		if (ogDisplayNameType != null) {
			Component ogDisplayName = stack.get(ogDisplayNameType);
			if (ogDisplayName != null) {
				return ogDisplayName;
			}
		}

		return title;
	}

	public static boolean isElven(ItemStack stack) {
		return stack.has(BotaniaDataComponents.ELVEN_UNLOCK);
	}

	// Random item to expose this as public
	public static BlockHitResult doRayTrace(Level world, Player player, ClipContext.Fluid fluidMode) {
		return Item.getPlayerPOVHitResult(world, player, fluidMode);
	}

}
