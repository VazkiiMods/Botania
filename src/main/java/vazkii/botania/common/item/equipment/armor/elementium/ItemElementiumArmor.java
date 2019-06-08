package vazkii.botania.common.item.equipment.armor.elementium;

import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.item.IPixieSpawner;
import vazkii.botania.client.lib.LibResources;
import vazkii.botania.client.model.armor.ModelArmorElementium;
import vazkii.botania.common.core.handler.ConfigHandler;
import vazkii.botania.common.item.ModItems;
import vazkii.botania.common.item.equipment.armor.manasteel.ItemManasteelArmor;

import javax.annotation.Nonnull;
import java.util.List;

public abstract class ItemElementiumArmor extends ItemManasteelArmor implements IPixieSpawner {

	public ItemElementiumArmor(EquipmentSlotType type, Properties props) {
		super(type, BotaniaAPI.ELEMENTIUM_ARMOR_MAT, props);
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public BipedModel provideArmorModelForSlot(ItemStack stack, EquipmentSlotType slot) {
		models.put(slot, new ModelArmorElementium(slot));
		return models.get(slot);
	}

	@Override
	public String getArmorTextureAfterInk(ItemStack stack, EquipmentSlotType slot) {
		return ConfigHandler.CLIENT.enableArmorModels.get() ? LibResources.MODEL_ELEMENTIUM_NEW : slot == EquipmentSlotType.LEGS ? LibResources.MODEL_ELEMENTIUM_1 : LibResources.MODEL_ELEMENTIUM_0;
	}

	static ItemStack[] armorset;

	@Override
	public ItemStack[] getArmorSetStacks() {
		if(armorset == null)
			armorset = new ItemStack[] {
					new ItemStack(ModItems.elementiumHelm),
					new ItemStack(ModItems.elementiumChest),
					new ItemStack(ModItems.elementiumLegs),
					new ItemStack(ModItems.elementiumBoots)
		};

		return armorset;
	}

	@Override
	public boolean hasArmorSetItem(PlayerEntity player, int i) {
		if(player == null || player.inventory == null || player.inventory.armorInventory == null)
			return false;
		
		ItemStack stack = player.inventory.armorInventory.get(3 - i);
		if(stack.isEmpty())
			return false;

		switch(i) {
		case 0: return stack.getItem() == ModItems.elementiumHelm || stack.getItem() == ModItems.elementiumHelmRevealing;
		case 1: return stack.getItem() == ModItems.elementiumChest;
		case 2: return stack.getItem() == ModItems.elementiumLegs;
		case 3: return stack.getItem() == ModItems.elementiumBoots;
		}

		return false;
	}

	@OnlyIn(Dist.CLIENT)
	@Override
	public ITextComponent getArmorSetName() {
		return new TranslationTextComponent("botania.armorset.elementium.name");
	}

	@OnlyIn(Dist.CLIENT)
	@Override
	public void addArmorSetDescription(ItemStack stack, List<ITextComponent> list) {
		super.addArmorSetDescription(stack, list);
		list.add(new TranslationTextComponent("botania.armorset.elementium.desc"));
	}

}
