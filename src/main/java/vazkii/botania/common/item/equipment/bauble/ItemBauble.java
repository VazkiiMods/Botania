/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.item.equipment.bauble;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.mojang.blaze3d.vertex.PoseStack;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.ChatFormatting;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import vazkii.botania.api.item.ICosmeticAttachable;
import vazkii.botania.api.item.IPhantomInkable;
import vazkii.botania.common.core.handler.ConfigHandler;
import vazkii.botania.common.core.handler.EquipmentHandler;
import vazkii.botania.common.core.helper.ItemNBTHelper;
import vazkii.botania.common.core.helper.PlayerHelper;

import java.util.List;
import java.util.UUID;

import static vazkii.botania.common.lib.ResourceLocationHelper.prefix;

public abstract class ItemBauble extends Item implements ICosmeticAttachable, IPhantomInkable {

	private static final String TAG_BAUBLE_UUID_MOST = "baubleUUIDMost";
	private static final String TAG_BAUBLE_UUID_LEAST = "baubleUUIDLeast";
	private static final String TAG_COSMETIC_ITEM = "cosmeticItem";
	private static final String TAG_PHANTOM_INK = "phantomInk";

	public ItemBauble(Properties props) {
		super(props);
		EquipmentHandler.initBaubleCap(this);
	}

	@Override
	@Environment(EnvType.CLIENT)
	public void appendHoverText(ItemStack stack, Level world, List<Component> tooltip, TooltipFlag flags) {
		ItemStack cosmetic = getCosmeticItem(stack);
		if (!cosmetic.isEmpty()) {
			tooltip.add(new TranslatableComponent("botaniamisc.hasCosmetic", cosmetic.getHoverName()).withStyle(ChatFormatting.GRAY, ChatFormatting.ITALIC));
		}

		if (hasPhantomInk(stack)) {
			tooltip.add(new TranslatableComponent("botaniamisc.hasPhantomInk").withStyle(ChatFormatting.AQUA));
		}
	}

	@Override
	public ItemStack getCosmeticItem(ItemStack stack) {
		CompoundTag cmp = ItemNBTHelper.getCompound(stack, TAG_COSMETIC_ITEM, true);
		if (cmp == null) {
			return ItemStack.EMPTY;
		}
		return ItemStack.of(cmp);
	}

	@Override
	public void setCosmeticItem(ItemStack stack, ItemStack cosmetic) {
		CompoundTag cmp = new CompoundTag();
		if (!cosmetic.isEmpty()) {
			cmp = cosmetic.save(cmp);
		}
		ItemNBTHelper.setCompound(stack, TAG_COSMETIC_ITEM, cmp);
	}

	public static UUID getBaubleUUID(ItemStack stack) {
		long most = ItemNBTHelper.getLong(stack, TAG_BAUBLE_UUID_MOST, 0);
		if (most == 0) {
			UUID uuid = UUID.randomUUID();
			ItemNBTHelper.setLong(stack, TAG_BAUBLE_UUID_MOST, uuid.getMostSignificantBits());
			ItemNBTHelper.setLong(stack, TAG_BAUBLE_UUID_LEAST, uuid.getLeastSignificantBits());
			return getBaubleUUID(stack);
		}

		long least = ItemNBTHelper.getLong(stack, TAG_BAUBLE_UUID_LEAST, 0);
		return new UUID(most, least);
	}

	@Override
	public boolean hasPhantomInk(ItemStack stack) {
		return ItemNBTHelper.getBoolean(stack, TAG_PHANTOM_INK, false);
	}

	@Override
	public void setPhantomInk(ItemStack stack, boolean ink) {
		ItemNBTHelper.setBoolean(stack, TAG_PHANTOM_INK, ink);
	}

	public void onWornTick(ItemStack stack, LivingEntity entity) {}

	public void onEquipped(ItemStack stack, LivingEntity entity) {
		if (!entity.level.isClientSide && entity instanceof ServerPlayer) {
			PlayerHelper.grantCriterion((ServerPlayer) entity, prefix("main/bauble_wear"), "code_triggered");
		}
	}

	public void onUnequipped(ItemStack stack, LivingEntity entity) {}

	public boolean canEquip(ItemStack stack, LivingEntity entity) {
		return true;
	}

	public Multimap<Attribute, AttributeModifier> getEquippedAttributeModifiers(ItemStack stack) {
		return HashMultimap.create();
	}

	public boolean hasRender(ItemStack stack, LivingEntity living) {
		return !hasPhantomInk(stack)
				&& ConfigHandler.CLIENT.renderAccessories.getValue()
				&& !living.isInvisible();
	}

	@Environment(EnvType.CLIENT)
	public void doRender(HumanoidModel<?> bipedModel, ItemStack stack, LivingEntity player, PoseStack ms, MultiBufferSource buffers, int light, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {}
}
