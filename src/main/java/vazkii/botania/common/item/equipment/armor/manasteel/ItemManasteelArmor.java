/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Apr 13, 2014, 6:38:21 PM (GMT)]
 */
package vazkii.botania.common.item.equipment.armor.manasteel;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.IArmorMaterial;
import net.minecraft.item.ItemStack;
import net.minecraft.util.LazyValue;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.DistExecutor;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.item.IPhantomInkable;
import vazkii.botania.api.mana.IManaUsingItem;
import vazkii.botania.api.mana.ManaItemHandler;
import vazkii.botania.client.lib.LibResources;
import vazkii.botania.client.model.armor.ModelArmorManasteel;
import vazkii.botania.common.core.handler.ConfigHandler;
import vazkii.botania.common.core.helper.ItemNBTHelper;
import vazkii.botania.common.item.ModItems;
import vazkii.botania.common.item.equipment.tool.ToolCommons;

import javax.annotation.Nonnull;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class ItemManasteelArmor extends ArmorItem implements IManaUsingItem, IPhantomInkable {

	private static final int MANA_PER_DAMAGE = 70;

	private static final String TAG_PHANTOM_INK = "phantomInk";

	private final LazyValue<BipedModel> model;
	public final EquipmentSlotType type;

	public ItemManasteelArmor(EquipmentSlotType type, Properties props) {
		this(type, BotaniaAPI.MANASTEEL_ARMOR_MAT, props);
	}

	public ItemManasteelArmor(EquipmentSlotType type, IArmorMaterial mat, Properties props) {
		super(mat, type, props);
		this.type = type;
		this.model = DistExecutor.runForDist(() -> () -> new LazyValue<>(() -> this.provideArmorModelForSlot(type)),
				() -> () -> null);
	}

	@Override
	public void inventoryTick(ItemStack stack, World world, Entity player, int slot, boolean selected) {
		if(player instanceof PlayerEntity)
			onArmorTick(stack, world, (PlayerEntity) player);
	}

	@Override
	public void onArmorTick(ItemStack stack, World world, PlayerEntity player) {
		if(!world.isRemote && stack.getDamage() > 0 && ManaItemHandler.requestManaExact(stack, player, MANA_PER_DAMAGE * 2, true))
			stack.setDamage(stack.getDamage() - 1);
	}

	@Override
	public <T extends LivingEntity> int damageItem(ItemStack stack, int amount, T entity, Consumer<T> onBroken) {
		return ToolCommons.damageItemIfPossible(stack, amount, entity, MANA_PER_DAMAGE);
	}

	@Nonnull
	@Override
	public final String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlotType slot, String type) {
		return hasPhantomInk(stack) ? LibResources.MODEL_INVISIBLE_ARMOR : getArmorTextureAfterInk(stack, slot);
	}

	public String getArmorTextureAfterInk(ItemStack stack, EquipmentSlotType slot) {
		return ConfigHandler.CLIENT.enableArmorModels.get() ? LibResources.MODEL_MANASTEEL_NEW : slot == EquipmentSlotType.LEGS ? LibResources.MODEL_MANASTEEL_1 : LibResources.MODEL_MANASTEEL_0;
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public BipedModel getArmorModel(LivingEntity entityLiving, ItemStack itemStack, EquipmentSlotType armorSlot, BipedModel original) {
		if(ConfigHandler.CLIENT.enableArmorModels.get()) {
			return model.getValue();
		}

		return super.getArmorModel(entityLiving, itemStack, armorSlot, original);
	}

	@OnlyIn(Dist.CLIENT)
	public BipedModel provideArmorModelForSlot(EquipmentSlotType slot) {
		return new ModelArmorManasteel(slot);
	}

	@Override
	public boolean usesMana(ItemStack stack) {
		return true;
	}

	@OnlyIn(Dist.CLIENT)
	@Override
	public void addInformation(ItemStack stack, World world, List<ITextComponent> list, ITooltipFlag flags) {
		if(Screen.hasShiftDown())
			addInformationAfterShift(stack, world, list, flags);
		else list.add(new TranslationTextComponent("botaniamisc.shiftinfo"));
	}

	@OnlyIn(Dist.CLIENT)
	public void addInformationAfterShift(ItemStack stack, World world, List<ITextComponent> list, ITooltipFlag flags) {
		PlayerEntity player = Minecraft.getInstance().player;
		list.add(getArmorSetTitle(player));
		addArmorSetDescription(stack, list);
		ItemStack[] stacks = getArmorSetStacks();
		for (ItemStack armor : stacks) {
			ITextComponent cmp = new StringTextComponent(" - ").appendSibling(armor.getDisplayName());
			EquipmentSlotType slot = ((ArmorItem) armor.getItem()).getEquipmentSlot();
			cmp.applyTextStyle(hasArmorSetItem(player, slot) ? TextFormatting.GREEN : TextFormatting.GRAY);
			list.add(cmp);
		}
		if(hasPhantomInk(stack))
			list.add(new TranslationTextComponent("botaniamisc.hasPhantomInk").applyTextStyle(TextFormatting.GRAY));
	}

	private static final LazyValue<ItemStack[]> armorSet = new LazyValue<>(() -> new ItemStack[] {
			new ItemStack(ModItems.manasteelHelm),
			new ItemStack(ModItems.manasteelChest),
			new ItemStack(ModItems.manasteelLegs),
			new ItemStack(ModItems.manasteelBoots)
	});

	public ItemStack[] getArmorSetStacks() {
		return armorSet.getValue();
	}

	public boolean hasArmorSet(PlayerEntity player) {
		return hasArmorSetItem(player, EquipmentSlotType.HEAD) && hasArmorSetItem(player, EquipmentSlotType.CHEST) && hasArmorSetItem(player, EquipmentSlotType.LEGS) && hasArmorSetItem(player, EquipmentSlotType.FEET);
	}

	public boolean hasArmorSetItem(PlayerEntity player, EquipmentSlotType slot) {
		if(player == null || player.inventory == null || player.inventory.armorInventory == null)
			return false;
		
		ItemStack stack = player.getItemStackFromSlot(slot);
		if(stack.isEmpty())
			return false;

		switch(slot) {
		case HEAD: return stack.getItem() == ModItems.manasteelHelm || stack.getItem() == ModItems.manasteelHelmRevealing;
		case CHEST: return stack.getItem() == ModItems.manasteelChest;
		case LEGS: return stack.getItem() == ModItems.manasteelLegs;
		case FEET: return stack.getItem() == ModItems.manasteelBoots;
		}

		return false;
	}

	private int getSetPiecesEquipped(PlayerEntity player) {
		int pieces = 0;
		for(EquipmentSlotType slot : EquipmentSlotType.values())
			if (slot.getSlotType() == EquipmentSlotType.Group.ARMOR && hasArmorSetItem(player, slot))
				pieces++;

		return pieces;
	}

	public ITextComponent getArmorSetName() {
		return new TranslationTextComponent("botania.armorset.manasteel.name");
	}

	private ITextComponent getArmorSetTitle(PlayerEntity player) {
		ITextComponent end = getArmorSetName()
				.appendText(" (" + getSetPiecesEquipped(player) + "/" + getArmorSetStacks().length + ")")
				.applyTextStyle(TextFormatting.GRAY);
		return new TranslationTextComponent("botaniamisc.armorset")
				.appendText(" ")
				.appendSibling(end);
	}

	@OnlyIn(Dist.CLIENT)
	public void addArmorSetDescription(ItemStack stack, List<ITextComponent> list) {
		list.add(new TranslationTextComponent("botania.armorset.manasteel.desc").applyTextStyle(TextFormatting.GRAY));
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
