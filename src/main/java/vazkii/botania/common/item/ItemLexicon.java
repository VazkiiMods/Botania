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
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.RayTraceContext;
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

	public ItemLexicon(Settings props) {
		super(props);
	}

	public static boolean isOpen() {
		return Registry.ITEM.getId(ModItems.lexicon).equals(PatchouliAPI.instance.getOpenBookGui());
	}

	@Override
	public void appendStacks(@Nonnull ItemGroup tab, @Nonnull DefaultedList<ItemStack> list) {
		if (isIn(tab)) {
			list.add(new ItemStack(this));
			ItemStack creative = new ItemStack(this);
			creative.getOrCreateTag().putBoolean(TAG_ELVEN_UNLOCK, true);
			list.add(creative);
		}
	}

	@Override
	@Environment(EnvType.CLIENT)
	public void appendTooltip(ItemStack stack, World worldIn, List<Text> tooltip, TooltipContext flagIn) {
		super.appendTooltip(stack, worldIn, tooltip, flagIn);

		TooltipHandler.addOnShift(tooltip, () -> {
			tooltip.add(getEdition().shallowCopy().formatted(Formatting.GRAY));
		});
	}

	@Nonnull
	@Override
	public TypedActionResult<ItemStack> use(World worldIn, PlayerEntity playerIn, Hand handIn) {
		ItemStack stack = playerIn.getStackInHand(handIn);

		if (playerIn instanceof ServerPlayerEntity) {
			ServerPlayerEntity player = (ServerPlayerEntity) playerIn;
			UseItemSuccessTrigger.INSTANCE.trigger(player, stack, player.getServerWorld(), player.getX(), player.getY(), player.getZ());
			PatchouliAPI.instance.openBookGUI((ServerPlayerEntity) playerIn, Registry.ITEM.getId(this));
			playerIn.playSound(ModSounds.lexiconOpen, 1F, (float) (0.7 + Math.random() * 0.4));
		}

		return new TypedActionResult<>(ActionResult.SUCCESS, stack);
	}

	public static Text getEdition() {
		return PatchouliAPI.instance.getSubtitle(Registry.ITEM.getId(ModItems.lexicon));
	}

	public static Text getTitle(ItemStack stack) {
		Text title = stack.getName();

		String akashicTomeNBT = "akashictome:displayName";
		if (stack.hasTag() && stack.getTag().contains(akashicTomeNBT)) {
			title = new LiteralText(stack.getTag().getString(akashicTomeNBT));
		}

		return title;
	}

	@Override
	public boolean isElvenItem(ItemStack stack) {
		return stack.hasTag() && stack.getTag().getBoolean(TAG_ELVEN_UNLOCK);
	}

	// Random item to expose this as public
	public static BlockHitResult doRayTrace(World world, PlayerEntity player, RayTraceContext.FluidHandling fluidMode) {
		return Item.rayTrace(world, player, fluidMode);
	}
}
