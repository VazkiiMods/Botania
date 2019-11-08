/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Aug 28, 2015, 8:30:19 PM (GMT)]
 */
package vazkii.botania.common.item.equipment.armor.manaweave;

import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.util.LazyLoadBase;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.client.core.proxy.ClientProxy;
import vazkii.botania.client.lib.LibResources;
import vazkii.botania.client.model.armor.ModelArmorManaweave;
import vazkii.botania.common.core.handler.ConfigHandler;
import vazkii.botania.common.item.ModItems;
import vazkii.botania.common.item.equipment.armor.manasteel.ItemManasteelArmor;

import javax.annotation.Nonnull;
import java.util.List;

public class ItemManaweaveArmor extends ItemManasteelArmor {

	public ItemManaweaveArmor(EquipmentSlotType type, Properties props) {
		super(type, BotaniaAPI.MANAWEAVE_ARMOR_MAT, props);
		addPropertyOverride(new ResourceLocation("botania", "holiday"), (stack, worldIn, entityIn) -> ClientProxy.jingleTheBells ? 1 : 0);
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public BipedModel provideArmorModelForSlot(EquipmentSlotType slot) {
		return new ModelArmorManaweave(slot);
	}

	@Override
	public String getArmorTextureAfterInk(ItemStack stack, EquipmentSlotType slot) {
		return ConfigHandler.CLIENT.enableArmorModels.get() ? ClientProxy.jingleTheBells ? LibResources.MODEL_MANAWEAVE_NEW_HOLIDAY : LibResources.MODEL_MANAWEAVE_NEW : slot == EquipmentSlotType.LEGS ? LibResources.MODEL_MANAWEAVE_1 : LibResources.MODEL_MANAWEAVE_0;
	}

	@Nonnull
	@Override
	@OnlyIn(Dist.CLIENT)
	public String getTranslationKey(ItemStack stack) {
		String name = super.getTranslationKey(stack);
		if(ClientProxy.jingleTheBells)
			name = name.replaceAll("manaweave", "santaweave");
		return name;
	}

	private static final LazyLoadBase<ItemStack[]> armorSet = new LazyLoadBase<>(() -> new ItemStack[] {
			new ItemStack(ModItems.manaweaveHelm),
			new ItemStack(ModItems.manaweaveChest),
			new ItemStack(ModItems.manaweaveLegs),
			new ItemStack(ModItems.manaweaveBoots)
	});

	@Override
	public ItemStack[] getArmorSetStacks() {
		return armorSet.getValue();
	}

	@Override
	public boolean hasArmorSetItem(PlayerEntity player, EquipmentSlotType slot) {
		if(player == null)
			return false;
		
		ItemStack stack = player.getItemStackFromSlot(slot);
		if(stack.isEmpty())
			return false;

		switch(slot) {
		case HEAD: return stack.getItem() == ModItems.manaweaveHelm;
		case CHEST: return stack.getItem() == ModItems.manaweaveChest;
		case LEGS: return stack.getItem() == ModItems.manaweaveLegs;
		case FEET: return stack.getItem() == ModItems.manaweaveBoots;
		}

		return false;
	}

	@Override
	public ITextComponent getArmorSetName() {
		return new TranslationTextComponent("botania.armorset.manaweave.name");
	}

	@OnlyIn(Dist.CLIENT)
	@Override
	public void addInformationAfterShift(ItemStack stack, World world, List<ITextComponent> list, ITooltipFlag flags) {
		if(ClientProxy.jingleTheBells) {
			list.add(new TranslationTextComponent("botaniamisc.santaweaveInfo"));
			list.add(new StringTextComponent(""));
		}

		super.addInformationAfterShift(stack, world, list, flags);
	}

	@OnlyIn(Dist.CLIENT)
	@Override
	public void addArmorSetDescription(ItemStack stack, List<ITextComponent> list) {
		list.add(new TranslationTextComponent("botania.armorset.manaweave.desc0"));
		list.add(new TranslationTextComponent("botania.armorset.manaweave.desc1"));
	}
}
