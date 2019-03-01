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

import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionUtil;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.brew.Brew;
import vazkii.botania.api.brew.IBrewItem;
import vazkii.botania.common.core.helper.ItemNBTHelper;
import vazkii.botania.common.item.ItemMod;
import vazkii.botania.common.lib.LibMisc;

import javax.annotation.Nonnull;
import java.util.List;

public class ItemBrewBase extends ItemMod implements IBrewItem {

	private static final String TAG_BREW_KEY = "brewKey";
	private static final String TAG_SWIGS_LEFT = "swigsLeft";

	private final int swigs;
	private final int drinkSpeed;
	private final ItemStack baseItem;

	public ItemBrewBase(Properties builder, int swigs, int drinkSpeed, ItemStack baseItem) {
		super(builder);
		this.swigs = swigs;
		this.drinkSpeed = drinkSpeed;
		this.baseItem = baseItem;
		addPropertyOverride(new ResourceLocation(LibMisc.MOD_ID, "swigs_taken"), (stack, world, entity) -> swigs - getSwigsLeft(stack));
	}

	@Override
	public int getUseDuration(ItemStack stack) {
		return drinkSpeed;
	}

	@Nonnull
	@Override
	public EnumAction getUseAction(ItemStack stack) {
		return EnumAction.DRINK;
	}

	@Nonnull
	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, @Nonnull EnumHand hand) {
		player.setActiveHand(hand);
		return ActionResult.newResult(EnumActionResult.SUCCESS, player.getHeldItem(hand));
	}

	@Nonnull
	@Override
	public ItemStack onItemUseFinish(@Nonnull ItemStack stack, World world, EntityLivingBase living) {
		if(!world.isRemote) {
			for(PotionEffect effect : getBrew(stack).getPotionEffects(stack)) {
				PotionEffect newEffect = new PotionEffect(effect.getPotion(), effect.getDuration(), effect.getAmplifier(), true, true);
				if(effect.getPotion().isInstant())
					effect.getPotion().affectEntity(living, living, living, newEffect.getAmplifier(), 1F);
				else living.addPotionEffect(newEffect);
			}

			if(world.rand.nextBoolean())
				world.playSound(null, living.posX, living.posY, living.posZ, SoundEvents.ENTITY_PLAYER_BURP, SoundCategory.PLAYERS, 1F, 1F);

			int swigs = getSwigsLeft(stack);
			if(living instanceof EntityPlayer && !((EntityPlayer) living).abilities.isCreativeMode) {
				if(swigs == 1) {
					if(!((EntityPlayer) living).inventory.addItemStackToInventory(baseItem.copy()))
						return baseItem.copy();
					else {
						return ItemStack.EMPTY;
					}
				}


				setSwigsLeft(stack, swigs - 1);
			}
		}

		return stack;
	}

	@Override
	public void fillItemGroup(ItemGroup tab, NonNullList<ItemStack> list) {
		if(isInGroup(tab)) {
			for(String s : BotaniaAPI.brewMap.keySet()) {
				ItemStack stack = new ItemStack(this);
				setBrew(stack, s);
				list.add(stack);
			}
		}
	}

	@Nonnull
	@Override
	public ITextComponent getDisplayName(@Nonnull ItemStack stack) {
		ITextComponent cmp = new TextComponentTranslation(getTranslationKey(), new TextComponentTranslation(getBrew(stack).getUnlocalizedName(stack)));
		cmp.appendSibling(new TextComponentString(Integer.toString(getSwigsLeft(stack))).applyTextStyle(TextFormatting.BOLD));
		return cmp;
	}

	@OnlyIn(Dist.CLIENT)
	@Override
	public void addInformation(ItemStack stack, World world, List<ITextComponent> list, ITooltipFlag flags) {
		Brew brew = getBrew(stack);
		for(PotionEffect effect : brew.getPotionEffects(stack)) {
			TextFormatting format = effect.getPotion().isBadEffect() ? TextFormatting.RED : TextFormatting.GRAY;
			ITextComponent cmp = new TextComponentTranslation(effect.getEffectName());
			if(effect.getAmplifier() > 0) {
				cmp.appendText(" ");
				cmp.appendSibling(new TextComponentTranslation("botania.roman" + (effect.getAmplifier() + 1)));
			}
			if(!effect.getPotion().isInstant()) {
				cmp.appendText(" (" + PotionUtil.getPotionDurationString(effect, 1) + ")").applyTextStyle(TextFormatting.GRAY);
			}
			list.add(cmp.applyTextStyle(format));
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

	@Nonnull
	public static String getSubtype(ItemStack stack) {
		return stack.hasTag() ? ItemNBTHelper.getString(stack, TAG_BREW_KEY, "none") : "none";
	}
	
	public int getSwigsLeft(ItemStack stack) {
		return ItemNBTHelper.getInt(stack, TAG_SWIGS_LEFT, swigs);
	}

	public void setSwigsLeft(ItemStack stack, int swigs) {
		ItemNBTHelper.setInt(stack, TAG_SWIGS_LEFT, swigs);
	}
}
