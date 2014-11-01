/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under a
 * Creative Commons Attribution-NonCommercial-ShareAlike 3.0 License
 * (http://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB)
 * 
 * File Created @ [Nov 1, 2014, 5:45:58 PM (GMT)]
 */
package vazkii.botania.common.item.brew;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IIcon;
import net.minecraft.util.StatCollector;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.brew.Brew;
import vazkii.botania.api.brew.IBrewItem;
import vazkii.botania.client.core.helper.IconHelper;
import vazkii.botania.common.core.helper.ItemNBTHelper;
import vazkii.botania.common.item.ItemMod;

public abstract class ItemBrewBase extends ItemMod implements IBrewItem {

	private static final String TAG_BREW_KEY = "brewKey";
	private static final String TAG_SWIGS_LEFT = "swigsLeft";

	String name;
	String texName;
	int swigs;
	int drinkSpeed;
	ItemStack baseItem;

	IIcon[] icons;

	public ItemBrewBase(String name, String texName, int swigs, int drinkSpeed, ItemStack baseItem) {
		this.name = name;
		this.texName = texName;
		this.swigs = swigs;
		this.drinkSpeed = drinkSpeed;
		this.baseItem = baseItem;
		setMaxDamage(swigs);
		setUnlocalizedName(name);
	}

	@Override
	public void getSubItems(Item item, CreativeTabs tab, List list) {
		for(String s : BotaniaAPI.brewMap.keySet()) {
			ItemStack stack = new ItemStack(item);
			setBrew(stack, s);
			list.add(stack);
		}
	}

	@Override
	public void registerIcons(IIconRegister par1IconRegister) {
		itemIcon = IconHelper.forName(par1IconRegister, texName + "0");

		icons = new IIcon[swigs];
		for(int i = 0; i < swigs; i++)
			icons[i] = IconHelper.forName(par1IconRegister, texName + "1_" + i);
	}

	@Override
	public boolean requiresMultipleRenderPasses() {
		return true;
	}

	@Override
	public IIcon getIcon(ItemStack stack, int pass) {
		return pass == 0 ? itemIcon : icons[Math.max(0, swigs - getSwigsLeft(stack))];
	}

	@Override
	public int getColorFromItemStack(ItemStack stack, int pass) {
		return pass == 0 ? 0xFFFFFF : getBrew(stack).getColor(stack);
	}

	@Override
	public String getItemStackDisplayName(ItemStack stack) {
		return String.format(StatCollector.translateToLocal(getUnlocalizedNameInefficiently(stack) + ".name"), StatCollector.translateToLocal(getBrew(stack).getUnlocalizedName(stack)), EnumChatFormatting.BOLD + "" + getSwigsLeft(stack) + EnumChatFormatting.RESET);
	}

	@Override
	public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean adv) {
		Brew brew = getBrew(stack);
		for(PotionEffect effect : brew.getPotionEffects(stack)) {
			Potion potion = Potion.potionTypes[effect.getPotionID()];
			EnumChatFormatting format = potion.isBadEffect() ? EnumChatFormatting.RED : EnumChatFormatting.GRAY;
			list.add(format + StatCollector.translateToLocal(effect.getEffectName()) + (effect.getAmplifier() == 0 ? "" : (" " + StatCollector.translateToLocal("botania.roman" + (effect.getAmplifier() + 1)))) + EnumChatFormatting.GRAY + (potion.isInstant() ? "" : (" (" + Potion.getDurationString(effect) + ")")));
		}
	}

	public Brew getBrew(ItemStack stack) {
		String key = ItemNBTHelper.getString(stack, TAG_BREW_KEY, "");
		return BotaniaAPI.getBrewFromKey(key);
	}

	public static void setBrew(ItemStack stack, Brew brew) {
		setBrew(stack, brew.getKey());
	}

	public static void setBrew(ItemStack stack, String brew) {
		ItemNBTHelper.setString(stack, TAG_BREW_KEY, brew);
	}

	public int getSwigsLeft(ItemStack stack) {
		return ItemNBTHelper.getInt(stack, TAG_SWIGS_LEFT, swigs);
	}

	public void setSwigsLeft(ItemStack stack, int swigs) {
		ItemNBTHelper.setInt(stack, TAG_SWIGS_LEFT, drinkSpeed);
	}

}
