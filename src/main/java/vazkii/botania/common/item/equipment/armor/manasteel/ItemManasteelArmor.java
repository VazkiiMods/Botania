/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.item.equipment.armor.manasteel;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.util.LazyLoadedValue;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.item.IPhantomInkable;
import vazkii.botania.api.mana.IManaUsingItem;
import vazkii.botania.api.mana.ManaItemHandler;
import vazkii.botania.client.core.handler.TooltipHandler;
import vazkii.botania.client.lib.LibResources;
import vazkii.botania.client.model.ModModelLayers;
import vazkii.botania.client.model.armor.ModelArmor;
import vazkii.botania.common.core.helper.ItemNBTHelper;
import vazkii.botania.common.item.ModItems;
import vazkii.botania.common.item.equipment.tool.ToolCommons;

import javax.annotation.Nonnull;

import java.util.List;
import java.util.function.Consumer;

public class ItemManasteelArmor extends ArmorItem implements IManaUsingItem, IPhantomInkable {

	private static final int MANA_PER_DAMAGE = 70;

	private static final String TAG_PHANTOM_INK = "phantomInk";

	@Environment(EnvType.CLIENT)
	private HumanoidModel<LivingEntity> model;
	public final EquipmentSlot type;

	public ItemManasteelArmor(EquipmentSlot type, Properties props) {
		this(type, BotaniaAPI.instance().getManasteelArmorMaterial(), props);
	}

	public ItemManasteelArmor(EquipmentSlot type, ArmorMaterial mat, Properties props) {
		super(mat, type, props);
		this.type = type;
	}

	@Override
	public void inventoryTick(ItemStack stack, Level world, Entity player, int slot, boolean selected) {
		if (player instanceof Player) {
			onArmorTick(stack, world, (Player) player);
		}
	}

	public void onArmorTick(ItemStack stack, Level world, Player player) {
		if (!world.isClientSide && stack.getDamageValue() > 0 && ManaItemHandler.instance().requestManaExact(stack, player, MANA_PER_DAMAGE * 2, true)) {
			stack.setDamageValue(stack.getDamageValue() - 1);
		}
	}

	public static <T extends LivingEntity> int damageItem(ItemStack stack, int amount, T entity, Consumer<T> onBroken) {
		return ToolCommons.damageItemIfPossible(stack, amount, entity, MANA_PER_DAMAGE);
	}

	@Nonnull
	public final String getArmorTexture(ItemStack stack, EquipmentSlot slot) {
		return hasPhantomInk(stack) ? LibResources.MODEL_INVISIBLE_ARMOR : getArmorTextureAfterInk(stack, slot);
	}

	public String getArmorTextureAfterInk(ItemStack stack, EquipmentSlot slot) {
		return LibResources.MODEL_MANASTEEL_NEW;
	}

	@Environment(EnvType.CLIENT)
	public HumanoidModel<LivingEntity> getArmorModel(LivingEntity entityLiving, ItemStack itemStack, EquipmentSlot armorSlot, HumanoidModel<LivingEntity> original) {
		if (model == null) {
			model = provideArmorModelForSlot(slot);
		}
		return model;
	}

	@Environment(EnvType.CLIENT)
	protected HumanoidModel<LivingEntity> provideArmorModelForSlot(EquipmentSlot slot) {
		var entityModels = Minecraft.getInstance().getEntityModels();
		var root = entityModels.bakeLayer(slot == EquipmentSlot.LEGS
				? ModModelLayers.MANASTEEL_INNER_ARMOR
				: ModModelLayers.MANASTEEL_OUTER_ARMOR);
		return new ModelArmor(root, slot);
	}

	@Override
	public boolean usesMana(ItemStack stack) {
		return true;
	}

	@Environment(EnvType.CLIENT)
	@Override
	public void appendHoverText(ItemStack stack, Level world, List<Component> list, TooltipFlag flags) {
		TooltipHandler.addOnShift(list, () -> addInformationAfterShift(stack, world, list, flags));
	}

	@Environment(EnvType.CLIENT)
	public void addInformationAfterShift(ItemStack stack, Level world, List<Component> list, TooltipFlag flags) {
		Player player = Minecraft.getInstance().player;
		list.add(getArmorSetTitle(player));
		addArmorSetDescription(stack, list);
		ItemStack[] stacks = getArmorSetStacks();
		for (ItemStack armor : stacks) {
			MutableComponent cmp = new TextComponent(" - ").append(armor.getHoverName());
			EquipmentSlot slot = ((ArmorItem) armor.getItem()).getSlot();
			cmp.withStyle(hasArmorSetItem(player, slot) ? ChatFormatting.GREEN : ChatFormatting.GRAY);
			list.add(cmp);
		}
		if (hasPhantomInk(stack)) {
			list.add(new TranslatableComponent("botaniamisc.hasPhantomInk").withStyle(ChatFormatting.GRAY));
		}
	}

	private static final LazyLoadedValue<ItemStack[]> armorSet = new LazyLoadedValue<>(() -> new ItemStack[] {
			new ItemStack(ModItems.manasteelHelm),
			new ItemStack(ModItems.manasteelChest),
			new ItemStack(ModItems.manasteelLegs),
			new ItemStack(ModItems.manasteelBoots)
	});

	public ItemStack[] getArmorSetStacks() {
		return armorSet.get();
	}

	public boolean hasArmorSet(Player player) {
		return hasArmorSetItem(player, EquipmentSlot.HEAD) && hasArmorSetItem(player, EquipmentSlot.CHEST) && hasArmorSetItem(player, EquipmentSlot.LEGS) && hasArmorSetItem(player, EquipmentSlot.FEET);
	}

	public boolean hasArmorSetItem(Player player, EquipmentSlot slot) {
		if (player == null || player.getInventory() == null || player.getInventory().armor == null) {
			return false;
		}

		ItemStack stack = player.getItemBySlot(slot);
		if (stack.isEmpty()) {
			return false;
		}

		switch (slot) {
		case HEAD:
			return stack.is(ModItems.manasteelHelm);
		case CHEST:
			return stack.is(ModItems.manasteelChest);
		case LEGS:
			return stack.is(ModItems.manasteelLegs);
		case FEET:
			return stack.is(ModItems.manasteelBoots);
		}

		return false;
	}

	private int getSetPiecesEquipped(Player player) {
		int pieces = 0;
		for (EquipmentSlot slot : EquipmentSlot.values()) {
			if (slot.getType() == EquipmentSlot.Type.ARMOR && hasArmorSetItem(player, slot)) {
				pieces++;
			}
		}

		return pieces;
	}

	public MutableComponent getArmorSetName() {
		return new TranslatableComponent("botania.armorset.manasteel.name");
	}

	private Component getArmorSetTitle(Player player) {
		Component end = getArmorSetName()
				.append(" (" + getSetPiecesEquipped(player) + "/" + getArmorSetStacks().length + ")")
				.withStyle(ChatFormatting.GRAY);
		return new TranslatableComponent("botaniamisc.armorset")
				.append(" ")
				.append(end);
	}

	@Environment(EnvType.CLIENT)
	public void addArmorSetDescription(ItemStack stack, List<Component> list) {
		list.add(new TranslatableComponent("botania.armorset.manasteel.desc").withStyle(ChatFormatting.GRAY));
	}

	@Override
	public boolean hasPhantomInk(ItemStack stack) {
		return ItemNBTHelper.getBoolean(stack, TAG_PHANTOM_INK, false);
	}

	@Override
	public void setPhantomInk(ItemStack stack, boolean ink) {
		ItemNBTHelper.setBoolean(stack, TAG_PHANTOM_INK, ink);
	}
}
