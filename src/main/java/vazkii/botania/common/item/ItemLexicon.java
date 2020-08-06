/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.item;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import vazkii.botania.api.recipe.IElvenItem;
import vazkii.botania.client.core.handler.TooltipHandler;
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
		return Registry.ITEM.getKey(ModItems.lexicon).equals(PatchouliAPI.instance.getOpenBookGui());
	}

	@Override
	public void fillItemGroup(@Nonnull ItemGroup tab, @Nonnull NonNullList<ItemStack> list) {
		if (isInGroup(tab)) {
			list.add(new ItemStack(this));
			ItemStack creative = new ItemStack(this);
			creative.getOrCreateTag().putBoolean(TAG_ELVEN_UNLOCK, true);
			list.add(creative);
		}
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void addInformation(ItemStack stack, World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
		super.addInformation(stack, worldIn, tooltip, flagIn);

		TooltipHandler.addOnShift(tooltip, () -> {
			tooltip.add(getEdition().deepCopy().func_240699_a_(TextFormatting.GRAY));
		});
	}

	@Nonnull
	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
		ItemStack stack = playerIn.getHeldItem(handIn);

		if (playerIn instanceof ServerPlayerEntity) {
			ServerPlayerEntity player = (ServerPlayerEntity) playerIn;
			UseItemSuccessTrigger.INSTANCE.trigger(player, stack, player.getServerWorld(), player.getPosX(), player.getPosY(), player.getPosZ());
			PatchouliAPI.instance.openBookGUI((ServerPlayerEntity) playerIn, Registry.ITEM.getKey(this));
			playerIn.playSound(ModSounds.lexiconOpen, 1F, (float) (0.7 + Math.random() * 0.4));
		}

		return new ActionResult<>(ActionResultType.SUCCESS, stack);
	}

	public static ITextComponent getEdition() {
		return PatchouliAPI.instance.getSubtitle(Registry.ITEM.getKey(ModItems.lexicon));
	}

	public static ITextComponent getTitle(ItemStack stack) {
		ITextComponent title = stack.getDisplayName();

		String akashicTomeNBT = "akashictome:displayName";
		if (stack.hasTag() && stack.getTag().contains(akashicTomeNBT)) {
			title = new StringTextComponent(stack.getTag().getString(akashicTomeNBT));
		}

		return title;
	}

	@Override
	public boolean isElvenItem(ItemStack stack) {
		return stack.hasTag() && stack.getTag().getBoolean(TAG_ELVEN_UNLOCK);
	}

	// Random item to expose this as public
	public static BlockRayTraceResult doRayTrace(World world, PlayerEntity player, RayTraceContext.FluidMode fluidMode) {
		return Item.rayTrace(world, player, fluidMode);
	}
}
