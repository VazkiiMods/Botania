/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [Nov 1, 2014, 5:45:58 PM (GMT)]
 */
package vazkii.botania.common.item.brew;

import java.awt.Color;
import java.util.List;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.stats.Achievement;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IIcon;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.brew.Brew;
import vazkii.botania.api.brew.IBrewItem;
import vazkii.botania.client.core.handler.ClientTickHandler;
import vazkii.botania.client.core.helper.IconHelper;
import vazkii.botania.common.achievement.IPickupAchievement;
import vazkii.botania.common.achievement.ModAchievements;
import vazkii.botania.common.core.helper.ItemNBTHelper;
import vazkii.botania.common.item.ItemMod;

public abstract class ItemBrewBase extends ItemMod implements IBrewItem, IPickupAchievement {

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
		setMaxStackSize(1);
		setMaxDamage(swigs);
		setUnlocalizedName(name);
		setNoRepair();
	}

	@Override
	public int getMaxItemUseDuration(ItemStack p_77626_1_) {
		return drinkSpeed;
	}

	@Override
	public EnumAction getItemUseAction(ItemStack p_77661_1_) {
		return EnumAction.drink;
	}

	@Override
	public ItemStack onItemRightClick(ItemStack p_77659_1_, World p_77659_2_, EntityPlayer p_77659_3_) {
		p_77659_3_.setItemInUse(p_77659_1_, getMaxItemUseDuration(p_77659_1_));
		return p_77659_1_;
	}

	@Override
	public ItemStack onEaten(ItemStack stack, World world, EntityPlayer player) {
		if(!world.isRemote) {
			for(PotionEffect effect : getBrew(stack).getPotionEffects(stack)) {
				PotionEffect newEffect = new PotionEffect(effect.getPotionID(), effect.getDuration(), effect.getAmplifier(), true);
				Potion potion = Potion.potionTypes[newEffect.getPotionID()];
				if(potion.isInstant())
					potion.affectEntity(player, player, newEffect.getAmplifier(), 1F);
				else player.addPotionEffect(newEffect);
			}

			if(world.rand.nextBoolean())
				world.playSoundAtEntity(player, "random.burp", 1F, 1F);

			int swigs = getSwigsLeft(stack);
			if(!player.capabilities.isCreativeMode) {
				if(swigs == 1) {
					ItemStack copy = baseItem.copy();
					if(!player.inventory.addItemStackToInventory(copy))
						return baseItem.copy();
					return null;
				}


				setSwigsLeft(stack, swigs - 1);
			}
		}

		return stack;
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
		return pass == 0 ? itemIcon : icons[Math.max(0, Math.min(icons.length - 1, swigs - getSwigsLeft(stack)))];
	}

	@Override
	public int getColorFromItemStack(ItemStack stack, int pass) {
		if(pass == 0)
			return 0xFFFFFF;

		Color color = new Color(getBrew(stack).getColor(stack));
		int add = (int) (Math.sin(ClientTickHandler.ticksInGame * 0.1) * 16);

		int r = Math.max(0, Math.min(255, color.getRed() + add));
		int g = Math.max(0, Math.min(255, color.getGreen() + add));
		int b = Math.max(0, Math.min(255, color.getBlue() + add));

		return r << 16 | g << 8 | b;
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
			list.add(format + StatCollector.translateToLocal(effect.getEffectName()) + (effect.getAmplifier() == 0 ? "" : " " + StatCollector.translateToLocal("botania.roman" + (effect.getAmplifier() + 1))) + EnumChatFormatting.GRAY + (potion.isInstant() ? "" : " (" + Potion.getDurationString(effect) + ")"));
		}
	}

	@Override
	public Brew getBrew(ItemStack stack) {
		String key = ItemNBTHelper.getString(stack, TAG_BREW_KEY, "");
		return BotaniaAPI.getBrewFromKey(key);
	}

	public static void setBrew(ItemStack stack, Brew brew) {
		setBrew(stack, (brew == null ? BotaniaAPI.fallbackBrew : brew).getKey());
	}

	public static void setBrew(ItemStack stack, String brew) {
		ItemNBTHelper.setString(stack, TAG_BREW_KEY, brew);
	}

	public int getSwigsLeft(ItemStack stack) {
		return ItemNBTHelper.getInt(stack, TAG_SWIGS_LEFT, swigs);
	}

	public void setSwigsLeft(ItemStack stack, int swigs) {
		ItemNBTHelper.setInt(stack, TAG_SWIGS_LEFT, swigs);
	}

	@Override
	public Achievement getAchievementOnPickup(ItemStack stack, EntityPlayer player, EntityItem item) {
		return ModAchievements.brewPickup;
	}

}
