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

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.stats.Achievement;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.brew.Brew;
import vazkii.botania.api.brew.IBrewItem;
import vazkii.botania.client.core.handler.ClientTickHandler;
import vazkii.botania.common.achievement.IPickupAchievement;
import vazkii.botania.common.achievement.ModAchievements;
import vazkii.botania.common.core.helper.ItemNBTHelper;
import vazkii.botania.common.item.IColorable;
import vazkii.botania.common.item.ItemMod;

import java.awt.*;
import java.util.List;

public abstract class ItemBrewBase extends ItemMod implements IBrewItem, IPickupAchievement, IColorable {

	private static final String TAG_BREW_KEY = "brewKey";
	private static final String TAG_SWIGS_LEFT = "swigsLeft";

	private String name;
	private String texName;
	private int swigs;
	private int drinkSpeed;
	private ItemStack baseItem;

	public ItemBrewBase(String name, String texName, int swigs, int drinkSpeed, ItemStack baseItem) {
		super(name);
		this.name = name;
		this.texName = texName;
		this.swigs = swigs;
		this.drinkSpeed = drinkSpeed;
		this.baseItem = baseItem;
		setMaxStackSize(1);
		setMaxDamage(swigs);
		setNoRepair();
	}

	@Override
	public int getMaxItemUseDuration(ItemStack p_77626_1_) {
		return drinkSpeed;
	}

	@Override
	public EnumAction getItemUseAction(ItemStack p_77661_1_) {
		return EnumAction.DRINK;
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(ItemStack p_77659_1_, World p_77659_2_, EntityPlayer p_77659_3_, EnumHand hand) {
		p_77659_3_.setActiveHand(hand);
		return ActionResult.newResult(EnumActionResult.SUCCESS, p_77659_1_);
	}

	@Override
	public ItemStack onItemUseFinish(ItemStack stack, World world, EntityLivingBase living) {
		if(!world.isRemote) {
			for(PotionEffect effect : getBrew(stack).getPotionEffects(stack)) {
				PotionEffect newEffect = new PotionEffect(effect.getPotion(), effect.getDuration(), effect.getAmplifier(), true, true);
				if(effect.getPotion().isInstant())
					effect.getPotion().affectEntity(living, living, living, newEffect.getAmplifier(), 1F);
				else living.addPotionEffect(newEffect);
			}

			if(world.rand.nextBoolean())
				world.playSound(null, living.posX, living.posY, living.posZ, SoundEvents.entity_player_burp, SoundCategory.PLAYERS, 1F, 1F);

			int swigs = getSwigsLeft(stack);
			if(living instanceof EntityPlayer && !((EntityPlayer) living).capabilities.isCreativeMode) {
				if(swigs == 1) {
					if(!((EntityPlayer) living).inventory.addItemStackToInventory(baseItem.copy()))
						return baseItem.copy();
					else {
						ItemStack copy = stack.copy();
						copy.stackSize = 0;
						return copy;
					}
				}


				setSwigsLeft(stack, swigs - 1);
			}
		}

		return stack;
	}

	@Override
	public void getSubItems(Item item, CreativeTabs tab, List<ItemStack> list) {
		for(String s : BotaniaAPI.brewMap.keySet()) {
			ItemStack stack = new ItemStack(item);
			setBrew(stack, s);
			list.add(stack);
		}
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
		return String.format(I18n.translateToLocal(getUnlocalizedNameInefficiently(stack) + ".name"), I18n.translateToLocal(getBrew(stack).getUnlocalizedName(stack)), TextFormatting.BOLD + "" + getSwigsLeft(stack) + TextFormatting.RESET);
	}

	@Override
	public void addInformation(ItemStack stack, EntityPlayer player, List<String> list, boolean adv) {
		Brew brew = getBrew(stack);
		for(PotionEffect effect : brew.getPotionEffects(stack)) {
			TextFormatting format = effect.getPotion().isBadEffect() ? TextFormatting.RED : TextFormatting.GRAY;
			list.add(format + I18n.translateToLocal(effect.getEffectName()) + (effect.getAmplifier() == 0 ? "" : " " + I18n.translateToLocal("botania.roman" + (effect.getAmplifier() + 1))) + TextFormatting.GRAY + (effect.getPotion().isInstant() ? "" : " (" + Potion.getPotionDurationString(effect, 1F) + ")"));
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
