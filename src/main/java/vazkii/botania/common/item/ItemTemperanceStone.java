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
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import vazkii.botania.common.core.helper.ItemNBTHelper;
import vazkii.botania.common.lib.LibMisc;

import javax.annotation.Nonnull;

import java.util.List;

public class ItemTemperanceStone extends Item {
	public static final String TAG_ACTIVE = "active";

	public ItemTemperanceStone(Properties builder) {
		super(builder);
		addPropertyOverride(new ResourceLocation(LibMisc.MOD_ID, "active"),
				(stack, worldIn, entityIn) -> ItemNBTHelper.getBoolean(stack, TAG_ACTIVE, false) ? 1 : 0);
	}

	@Nonnull
	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, @Nonnull Hand hand) {
		ItemStack stack = player.getHeldItem(hand);
		ItemNBTHelper.setBoolean(stack, TAG_ACTIVE, !ItemNBTHelper.getBoolean(stack, TAG_ACTIVE, false));
		world.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.NEUTRAL, 0.3F, 0.1F);
		return ActionResult.success(stack);
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void addInformation(ItemStack stack, World world, List<ITextComponent> stacks, ITooltipFlag flags) {
		if (ItemNBTHelper.getBoolean(stack, TAG_ACTIVE, false)) {
			stacks.add(new TranslationTextComponent("botaniamisc.active"));
		} else {
			stacks.add(new TranslationTextComponent("botaniamisc.inactive"));
		}
	}

	public static boolean hasTemperanceActive(PlayerEntity player) {
		IInventory inv = player.inventory;
		for (int i = 0; i < inv.getSizeInventory(); i++) {
			ItemStack stack = inv.getStackInSlot(i);
			if (!stack.isEmpty() && stack.getItem() == ModItems.temperanceStone && ItemNBTHelper.getBoolean(stack, TAG_ACTIVE, false)) {
				return true;
			}
		}

		return false;
	}

}
