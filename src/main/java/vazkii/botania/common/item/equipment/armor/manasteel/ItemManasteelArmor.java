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
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.IArmorMaterial;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.item.IPhantomInkable;
import vazkii.botania.api.mana.IManaUsingItem;
import vazkii.botania.api.mana.ManaItemHandler;
import vazkii.botania.client.lib.LibResources;
import vazkii.botania.client.model.armor.ModelArmorManasteel;
import vazkii.botania.common.core.BotaniaCreativeTab;
import vazkii.botania.common.core.handler.ConfigHandler;
import vazkii.botania.common.core.helper.ItemNBTHelper;
import vazkii.botania.common.item.ModItems;
import vazkii.botania.common.item.equipment.tool.ToolCommons;
import vazkii.botania.common.lib.LibMisc;

import javax.annotation.Nonnull;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class ItemManasteelArmor extends ArmorItem implements IManaUsingItem, IPhantomInkable {

	private static final int MANA_PER_DAMAGE = 70;

	private static final String TAG_PHANTOM_INK = "phantomInk";

	protected Map<EquipmentSlotType, BipedModel> models = null;
	public final EquipmentSlotType type;

	public ItemManasteelArmor(EquipmentSlotType type, Properties props) {
		this(type, BotaniaAPI.MANASTEEL_ARMOR_MAT, props);
	}

	public ItemManasteelArmor(EquipmentSlotType type, IArmorMaterial mat, Properties props) {
		super(mat, type, props);
		this.type = type;
	}

	@Override
	public void inventoryTick(ItemStack stack, World world, Entity player, int par4, boolean par5) {
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
			BipedModel model = getArmorModelForSlot(entityLiving, itemStack, armorSlot);
			if(model == null)
				model = provideArmorModelForSlot(itemStack, armorSlot);

			if(model != null) {
				model.setModelAttributes(original);
				return model;
			}
		}

		return super.getArmorModel(entityLiving, itemStack, armorSlot, original);
	}

	@OnlyIn(Dist.CLIENT)
	public BipedModel getArmorModelForSlot(LivingEntity entity, ItemStack stack, EquipmentSlotType slot) {
		if(models == null)
			models = new EnumMap<>(EquipmentSlotType.class);

		return models.get(slot);
	}

	@OnlyIn(Dist.CLIENT)
	public BipedModel provideArmorModelForSlot(ItemStack stack, EquipmentSlotType slot) {
		models.put(slot, new ModelArmorManasteel(slot));
		return models.get(slot);
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
		for(int i = 0; i < stacks.length; i++) {
			ITextComponent cmp = new StringTextComponent(" - ").appendSibling(stack.getDisplayName());
			if(hasArmorSetItem(player, i))
				cmp.getStyle().setColor(TextFormatting.GREEN);
			list.add(cmp);
		}
		if(hasPhantomInk(stack))
			list.add(new TranslationTextComponent("botaniamisc.hasPhantomInk"));
	}

	static ItemStack[] armorset;

	public ItemStack[] getArmorSetStacks() {
		if(armorset == null)
			armorset = new ItemStack[] {
					new ItemStack(ModItems.manasteelHelm),
					new ItemStack(ModItems.manasteelChest),
					new ItemStack(ModItems.manasteelLegs),
					new ItemStack(ModItems.manasteelBoots)
		};

		return armorset;
	}

	public boolean hasArmorSet(PlayerEntity player) {
		return hasArmorSetItem(player, 0) && hasArmorSetItem(player, 1) && hasArmorSetItem(player, 2) && hasArmorSetItem(player, 3);
	}

	public boolean hasArmorSetItem(PlayerEntity player, int i) {
		if(player == null || player.inventory == null || player.inventory.armorInventory == null)
			return false;
		
		ItemStack stack = player.inventory.armorInventory.get(3 - i);
		if(stack.isEmpty())
			return false;

		switch(i) {
		case 0: return stack.getItem() == ModItems.manasteelHelm || stack.getItem() == ModItems.manasteelHelmRevealing;
		case 1: return stack.getItem() == ModItems.manasteelChest;
		case 2: return stack.getItem() == ModItems.manasteelLegs;
		case 3: return stack.getItem() == ModItems.manasteelBoots;
		}

		return false;
	}

	private int getSetPiecesEquipped(PlayerEntity player) {
		int pieces = 0;
		for(int i = 0; i < 4; i++)
			if(hasArmorSetItem(player, i))
				pieces++;

		return pieces;
	}

	@OnlyIn(Dist.CLIENT)
	public ITextComponent getArmorSetName() {
		return new TranslationTextComponent("botania.armorset.manasteel.name");
	}

	@OnlyIn(Dist.CLIENT)
	private ITextComponent getArmorSetTitle(PlayerEntity player) {
		return new TranslationTextComponent("botaniamisc.armorset")
				.appendText(" ")
				.appendSibling(getArmorSetName())
				.appendText(" (" + getSetPiecesEquipped(player) + "/" + getArmorSetStacks().length + ")");
	}

	@OnlyIn(Dist.CLIENT)
	public void addArmorSetDescription(ItemStack stack, List<ITextComponent> list) {
		list.add(new TranslationTextComponent("botania.armorset.manasteel.desc"));
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
