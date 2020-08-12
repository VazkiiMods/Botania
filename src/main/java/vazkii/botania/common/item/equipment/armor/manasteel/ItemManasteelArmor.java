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
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.ItemStack;
import net.minecraft.text.LiteralText;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.Lazy;
import net.minecraft.world.World;

import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.item.IPhantomInkable;
import vazkii.botania.api.mana.IManaUsingItem;
import vazkii.botania.api.mana.ManaItemHandler;
import vazkii.botania.client.core.handler.TooltipHandler;
import vazkii.botania.client.lib.LibResources;
import vazkii.botania.client.model.armor.ModelArmorManasteel;
import vazkii.botania.common.core.handler.ConfigHandler;
import vazkii.botania.common.core.helper.ItemNBTHelper;
import vazkii.botania.common.item.ModItems;
import vazkii.botania.common.item.equipment.tool.ToolCommons;

import javax.annotation.Nonnull;

import java.util.List;
import java.util.function.Consumer;

public class ItemManasteelArmor extends ArmorItem implements IManaUsingItem, IPhantomInkable {

	private static final int MANA_PER_DAMAGE = 70;

	private static final String TAG_PHANTOM_INK = "phantomInk";

	private final Lazy<BipedEntityModel<?>> model;
	public final EquipmentSlot type;

	public ItemManasteelArmor(EquipmentSlot type, Settings props) {
		this(type, BotaniaAPI.instance().getManasteelArmorMaterial(), props);
	}

	public ItemManasteelArmor(EquipmentSlot type, ArmorMaterial mat, Settings props) {
		super(mat, type, props);
		this.type = type;
		this.model = DistExecutor.runForDist(() -> () -> new Lazy<>(() -> this.provideArmorModelForSlot(type)),
				() -> () -> null);
	}

	@Override
	public void inventoryTick(ItemStack stack, World world, Entity player, int slot, boolean selected) {
		if (player instanceof PlayerEntity) {
			onArmorTick(stack, world, (PlayerEntity) player);
		}
	}

	@Override
	public void onArmorTick(ItemStack stack, World world, PlayerEntity player) {
		if (!world.isClient && stack.getDamage() > 0 && ManaItemHandler.instance().requestManaExact(stack, player, MANA_PER_DAMAGE * 2, true)) {
			stack.setDamage(stack.getDamage() - 1);
		}
	}

	@Override
	public <T extends LivingEntity> int damageItem(ItemStack stack, int amount, T entity, Consumer<T> onBroken) {
		return ToolCommons.damageItemIfPossible(stack, amount, entity, MANA_PER_DAMAGE);
	}

	@Nonnull
	@Override
	public final String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, String type) {
		return hasPhantomInk(stack) ? LibResources.MODEL_INVISIBLE_ARMOR : getArmorTextureAfterInk(stack, slot);
	}

	public String getArmorTextureAfterInk(ItemStack stack, EquipmentSlot slot) {
		return ConfigHandler.CLIENT.enableArmorModels.getValue() ? LibResources.MODEL_MANASTEEL_NEW : slot == EquipmentSlot.LEGS ? LibResources.MODEL_MANASTEEL_1 : LibResources.MODEL_MANASTEEL_0;
	}

	@Override
	@Environment(EnvType.CLIENT)
	@SuppressWarnings("unchecked")
	public <A extends BipedEntityModel<?>> A getArmorModel(LivingEntity entityLiving, ItemStack itemStack, EquipmentSlot armorSlot, A original) {
		if (ConfigHandler.CLIENT.enableArmorModels.getValue()) {
			return (A) model.get();
		}

		return super.getArmorModel(entityLiving, itemStack, armorSlot, original);
	}

	@Environment(EnvType.CLIENT)
	public BipedEntityModel<?> provideArmorModelForSlot(EquipmentSlot slot) {
		return new ModelArmorManasteel(slot);
	}

	@Override
	public boolean usesMana(ItemStack stack) {
		return true;
	}

	@Environment(EnvType.CLIENT)
	@Override
	public void appendTooltip(ItemStack stack, World world, List<Text> list, TooltipContext flags) {
		TooltipHandler.addOnShift(list, () -> addInformationAfterShift(stack, world, list, flags));
	}

	@Environment(EnvType.CLIENT)
	public void addInformationAfterShift(ItemStack stack, World world, List<Text> list, TooltipContext flags) {
		PlayerEntity player = MinecraftClient.getInstance().player;
		list.add(getArmorSetTitle(player));
		addArmorSetDescription(stack, list);
		ItemStack[] stacks = getArmorSetStacks();
		for (ItemStack armor : stacks) {
			MutableText cmp = new LiteralText(" - ").append(armor.getName());
			EquipmentSlot slot = ((ArmorItem) armor.getItem()).getSlotType();
			cmp.formatted(hasArmorSetItem(player, slot) ? Formatting.GREEN : Formatting.GRAY);
			list.add(cmp);
		}
		if (hasPhantomInk(stack)) {
			list.add(new TranslatableText("botaniamisc.hasPhantomInk").formatted(Formatting.GRAY));
		}
	}

	private static final Lazy<ItemStack[]> armorSet = new Lazy<>(() -> new ItemStack[] {
			new ItemStack(ModItems.manasteelHelm),
			new ItemStack(ModItems.manasteelChest),
			new ItemStack(ModItems.manasteelLegs),
			new ItemStack(ModItems.manasteelBoots)
	});

	public ItemStack[] getArmorSetStacks() {
		return armorSet.get();
	}

	public boolean hasArmorSet(PlayerEntity player) {
		return hasArmorSetItem(player, EquipmentSlot.HEAD) && hasArmorSetItem(player, EquipmentSlot.CHEST) && hasArmorSetItem(player, EquipmentSlot.LEGS) && hasArmorSetItem(player, EquipmentSlot.FEET);
	}

	public boolean hasArmorSetItem(PlayerEntity player, EquipmentSlot slot) {
		if (player == null || player.inventory == null || player.inventory.armor == null) {
			return false;
		}

		ItemStack stack = player.getEquippedStack(slot);
		if (stack.isEmpty()) {
			return false;
		}

		switch (slot) {
		case HEAD:
			return stack.getItem() == ModItems.manasteelHelm;
		case CHEST:
			return stack.getItem() == ModItems.manasteelChest;
		case LEGS:
			return stack.getItem() == ModItems.manasteelLegs;
		case FEET:
			return stack.getItem() == ModItems.manasteelBoots;
		}

		return false;
	}

	private int getSetPiecesEquipped(PlayerEntity player) {
		int pieces = 0;
		for (EquipmentSlot slot : EquipmentSlot.values()) {
			if (slot.getType() == EquipmentSlot.Type.ARMOR && hasArmorSetItem(player, slot)) {
				pieces++;
			}
		}

		return pieces;
	}

	public MutableText getArmorSetName() {
		return new TranslatableText("botania.armorset.manasteel.name");
	}

	private Text getArmorSetTitle(PlayerEntity player) {
		Text end = getArmorSetName()
				.append(" (" + getSetPiecesEquipped(player) + "/" + getArmorSetStacks().length + ")")
				.formatted(Formatting.GRAY);
		return new TranslatableText("botaniamisc.armorset")
				.append(" ")
				.append(end);
	}

	@Environment(EnvType.CLIENT)
	public void addArmorSetDescription(ItemStack stack, List<Text> list) {
		list.add(new TranslatableText("botania.armorset.manasteel.desc").formatted(Formatting.GRAY));
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
