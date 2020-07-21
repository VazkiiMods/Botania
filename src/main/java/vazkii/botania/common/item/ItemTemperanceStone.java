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
import net.minecraft.inventory.Inventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import vazkii.botania.common.core.helper.ItemNBTHelper;

import javax.annotation.Nonnull;

import java.util.List;

public class ItemTemperanceStone extends Item {
	public static final String TAG_ACTIVE = "active";

	public ItemTemperanceStone(Settings builder) {
		super(builder);
	}

	@Nonnull
	@Override
	public TypedActionResult<ItemStack> use(World world, PlayerEntity player, @Nonnull Hand hand) {
		ItemStack stack = player.getStackInHand(hand);
		ItemNBTHelper.setBoolean(stack, TAG_ACTIVE, !ItemNBTHelper.getBoolean(stack, TAG_ACTIVE, false));
		world.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.NEUTRAL, 0.3F, 0.1F);
		return TypedActionResult.success(stack);
	}

	@Override
	@Environment(EnvType.CLIENT)
	public void appendTooltip(ItemStack stack, World world, List<Text> stacks, TooltipContext flags) {
		if (ItemNBTHelper.getBoolean(stack, TAG_ACTIVE, false)) {
			stacks.add(new TranslatableText("botaniamisc.active"));
		} else {
			stacks.add(new TranslatableText("botaniamisc.inactive"));
		}
	}

	public static boolean hasTemperanceActive(PlayerEntity player) {
		Inventory inv = player.inventory;
		for (int i = 0; i < inv.size(); i++) {
			ItemStack stack = inv.getStack(i);
			if (!stack.isEmpty() && stack.getItem() == ModItems.temperanceStone && ItemNBTHelper.getBoolean(stack, TAG_ACTIVE, false)) {
				return true;
			}
		}

		return false;
	}

}
