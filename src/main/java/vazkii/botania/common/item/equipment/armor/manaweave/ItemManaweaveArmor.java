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

import java.util.List;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.Achievement;
import net.minecraft.util.IIcon;
import net.minecraft.util.StatCollector;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.client.core.helper.IconHelper;
import vazkii.botania.client.core.proxy.ClientProxy;
import vazkii.botania.client.lib.LibResources;
import vazkii.botania.client.model.armor.ModelArmorManaweave;
import vazkii.botania.common.achievement.ICraftAchievement;
import vazkii.botania.common.achievement.ModAchievements;
import vazkii.botania.common.core.handler.ConfigHandler;
import vazkii.botania.common.item.ModItems;
import vazkii.botania.common.item.equipment.armor.manasteel.ItemManasteelArmor;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemManaweaveArmor extends ItemManasteelArmor implements ICraftAchievement {

	IIcon iconChristmas;
	
	public ItemManaweaveArmor(int type, String name) {
		super(type, name, BotaniaAPI.manaweaveArmorMaterial);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public ModelBiped provideArmorModelForSlot(ItemStack stack, int slot) {
		models[slot] = new ModelArmorManaweave(slot);
		return models[slot];
	}
	
	@Override
	public void registerIcons(IIconRegister par1IconRegister) {
		super.registerIcons(par1IconRegister);
		iconChristmas = IconHelper.forItem(par1IconRegister, this, "Holiday");
	}
	
	@Override
	public String getArmorTextureAfterInk(ItemStack stack, int slot) {
		return ConfigHandler.enableArmorModels ? (ClientProxy.jingleTheBells ? LibResources.MODEL_MANAWEAVE_NEW_HOLIDAY : LibResources.MODEL_MANAWEAVE_NEW) : slot == 2 ? LibResources.MODEL_MANAWEAVE_1 : LibResources.MODEL_MANAWEAVE_0;
	}

	@Override
	public IIcon getIconFromDamage(int dmg) {
		return ClientProxy.jingleTheBells ? iconChristmas : super.getIconFromDamage(dmg);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public String getUnlocalizedName(ItemStack p_77667_1_) {
		String name = super.getUnlocalizedName(p_77667_1_);
		if(ClientProxy.jingleTheBells)
			name = name.replaceAll("manaweave", "santaweave");
		return name;
	}
	
	@Override
	public boolean getIsRepairable(ItemStack par1ItemStack, ItemStack par2ItemStack) {
		return par2ItemStack.getItem() == ModItems.manaResource && par2ItemStack.getItemDamage() == 22 ? true : super.getIsRepairable(par1ItemStack, par2ItemStack);
	}

	static ItemStack[] armorset;

	@Override
	public ItemStack[] getArmorSetStacks() {
		if(armorset == null)
			armorset = new ItemStack[] {
				new ItemStack(ModItems.manaweaveHelm),
				new ItemStack(ModItems.manaweaveChest),
				new ItemStack(ModItems.manaweaveLegs),
				new ItemStack(ModItems.manaweaveBoots)
		};

		return armorset;
	}

	@Override
	public boolean hasArmorSetItem(EntityPlayer player, int i) {
		ItemStack stack = player.inventory.armorInventory[3 - i];
		if(stack == null)
			return false;

		switch(i) {
		case 0: return stack.getItem() == ModItems.manaweaveHelm;
		case 1: return stack.getItem() == ModItems.manaweaveChest;
		case 2: return stack.getItem() == ModItems.manaweaveLegs;
		case 3: return stack.getItem() == ModItems.manaweaveBoots;
		}

		return false;
	}

	@Override
	public String getArmorSetName() {
		return StatCollector.translateToLocal("botania.armorset.manaweave.name");
	}
	
	@Override
	public void addInformationAfterShift(ItemStack stack, EntityPlayer player, List list, boolean adv) {
		if(ClientProxy.jingleTheBells) {
			addStringToTooltip(StatCollector.translateToLocal("botaniamisc.santaweaveInfo"), list);
			addStringToTooltip("", list);
		}
		
		super.addInformationAfterShift(stack, player, list, adv);
	}
	
	@Override
	public void addArmorSetDescription(ItemStack stack, List<String> list) {
		addStringToTooltip(StatCollector.translateToLocal("botania.armorset.manaweave.desc0"), list);
		addStringToTooltip(StatCollector.translateToLocal("botania.armorset.manaweave.desc1"), list);	
	}

	@Override
	public Achievement getAchievementOnCraft(ItemStack stack, EntityPlayer player, IInventory matrix) {
		return ModAchievements.manaweaveArmorCraft;
	}

}
