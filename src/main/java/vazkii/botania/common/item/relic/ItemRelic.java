/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.item.relic;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.Rarity;
import net.minecraft.world.World;

import vazkii.botania.api.item.IRelic;
import vazkii.botania.client.core.handler.TooltipHandler;
import vazkii.botania.common.advancements.RelicBindTrigger;
import vazkii.botania.common.core.helper.ItemNBTHelper;
import vazkii.botania.common.item.ModItems;

import javax.annotation.Nullable;

import java.util.List;
import java.util.UUID;

public class ItemRelic extends Item implements IRelic {

	private static final String TAG_SOULBIND_UUID = "soulbindUUID";

	public ItemRelic(Settings props) {
		super(props);
	}

	@Override
	public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
		if (!world.isClient && entity instanceof PlayerEntity) {
			updateRelic(stack, (PlayerEntity) entity);
		}
	}

	@Environment(EnvType.CLIENT)
	@Override
	public void appendTooltip(ItemStack stack, World world, List<Text> tooltip, TooltipContext flags) {
		TooltipHandler.addOnShift(tooltip, () -> addInformationAfterShift(stack, world, tooltip, flags));
	}

	@Environment(EnvType.CLIENT)
	public void addInformationAfterShift(ItemStack stack, World world, List<Text> tooltip, TooltipContext flags) {
		if (!hasUUID(stack)) {
			tooltip.add(new TranslatableText("botaniamisc.relicUnbound"));
		} else {
			if (!getSoulbindUUID(stack).equals(MinecraftClient.getInstance().player.getUuid())) {
				tooltip.add(new TranslatableText("botaniamisc.notYourSagittarius"));
			} else {
				tooltip.add(new TranslatableText("botaniamisc.relicSoulbound", MinecraftClient.getInstance().player.getName()));
			}
		}

		if (stack.getItem() == ModItems.dice) {
			tooltip.add(new LiteralText(""));
			String name = stack.getTranslationKey() + ".poem";
			for (int i = 0; i < 4; i++) {
				tooltip.add(new TranslatableText(name + i).formatted(Formatting.GRAY, Formatting.ITALIC));
			}
		}
	}

	public boolean shouldDamageWrongPlayer() {
		return true;
	}

	/* todo 1.16-fabric
	@Override
	public int getEntityLifespan(ItemStack itemStack, World world) {
		return Integer.MAX_VALUE;
	}
	*/

	public void updateRelic(ItemStack stack, PlayerEntity player) {
		if (stack.isEmpty() || !(stack.getItem() instanceof IRelic)) {
			return;
		}

		boolean rightPlayer = true;

		if (!hasUUID(stack)) {
			bindToUUID(player.getUuid(), stack);
			if (player instanceof ServerPlayerEntity) {
				RelicBindTrigger.INSTANCE.trigger((ServerPlayerEntity) player, stack);
			}
		} else if (!getSoulbindUUID(stack).equals(player.getUuid())) {
			rightPlayer = false;
		}

		if (!rightPlayer && player.age % 10 == 0 && (!(stack.getItem() instanceof ItemRelic) || ((ItemRelic) stack.getItem()).shouldDamageWrongPlayer())) {
			player.damage(damageSource(), 2);
		}
	}

	public boolean isRightPlayer(PlayerEntity player, ItemStack stack) {
		return hasUUID(stack) && getSoulbindUUID(stack).equals(player.getUuid());
	}

	public static DamageSource damageSource() {
		return new DamageSource("botania-relic") {};
	}

	@Override
	public void bindToUUID(UUID uuid, ItemStack stack) {
		ItemNBTHelper.setString(stack, TAG_SOULBIND_UUID, uuid.toString());
	}

	@Override
	public UUID getSoulbindUUID(ItemStack stack) {
		if (ItemNBTHelper.verifyExistance(stack, TAG_SOULBIND_UUID)) {
			try {
				return UUID.fromString(ItemNBTHelper.getString(stack, TAG_SOULBIND_UUID, ""));
			} catch (IllegalArgumentException ex) { // Bad UUID in tag
				ItemNBTHelper.removeEntry(stack, TAG_SOULBIND_UUID);
			}
		}

		return null;
	}

	@Override
	public boolean hasUUID(ItemStack stack) {
		return getSoulbindUUID(stack) != null;
	}
}
