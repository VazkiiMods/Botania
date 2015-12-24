/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [Mar 29, 2015, 7:54:40 PM (GMT)]
 */
package vazkii.botania.common.item.relic;

import java.util.List;
import java.util.UUID;

import com.mojang.authlib.GameProfile;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.Achievement;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.item.IRelic;
import vazkii.botania.common.core.helper.ItemNBTHelper;
import vazkii.botania.common.item.ItemMod;
import vazkii.botania.common.item.ModItems;

public class ItemRelic extends ItemMod implements IRelic {

	private static final String TAG_SOLBIND_NAME = "soulbind";
	private static final String TAG_SOULBIND_UUID = "soulbindUuid";

	Achievement achievement;

	public ItemRelic(String name) {
		setUnlocalizedName(name);
		setMaxStackSize(1);
	}

	@Override
	public void onUpdate(ItemStack p_77663_1_, World p_77663_2_, Entity p_77663_3_, int p_77663_4_, boolean p_77663_5_) {
		if(p_77663_3_ instanceof EntityPlayer)
			updateRelic(p_77663_1_, (EntityPlayer) p_77663_3_);
	}

	@Override
	public void addInformation(ItemStack p_77624_1_, EntityPlayer p_77624_2_, List p_77624_3_, boolean p_77624_4_) {
		addBindInfo(p_77624_3_, p_77624_1_, p_77624_2_);
	}

	public static void addBindInfo(List<String> list, ItemStack stack, EntityPlayer player) {
		if(GuiScreen.isShiftKeyDown()) {
			UUID bind = getSoulbindUuidS(stack);
			if(bind == null)
				addStringToTooltip(StatCollector.translateToLocal("botaniamisc.relicUnbound"), list);
			else {
				String disp = getSoulbindNameS(stack);
				if (disp.isEmpty()) {
					disp = bind.toString();
				}
				addStringToTooltip(String.format(StatCollector.translateToLocal("botaniamisc.relicSoulbound"), disp), list);
				if(!isRightPlayer(player, stack))
					addStringToTooltip(String.format(StatCollector.translateToLocal("botaniamisc.notYourSagittarius"), disp), list);
			}

			if(stack.getItem() == ModItems.aesirRing)
				addStringToTooltip(StatCollector.translateToLocal("botaniamisc.dropIkea"), list);

			if(stack.getItem() == ModItems.dice) {
				addStringToTooltip("", list);
				String name = stack.getUnlocalizedName() + ".poem";
				for(int i = 0; i < 4; i++)
					addStringToTooltip(EnumChatFormatting.ITALIC + StatCollector.translateToLocal(name + i), list);
			}
		} else addStringToTooltip(StatCollector.translateToLocal("botaniamisc.shiftinfo"), list);
	}

	public boolean shouldDamageWrongPlayer() {
		return true;
	}

	@Override
	public int getEntityLifespan(ItemStack itemStack, World world) {
		return Integer.MAX_VALUE;
	}

	static void addStringToTooltip(String s, List<String> tooltip) {
		tooltip.add(s.replaceAll("&", "\u00a7"));
	}

	public static UUID getSoulbindUuidS(ItemStack stack) {
		try {
			return UUID.fromString(ItemNBTHelper.getString(stack, TAG_SOULBIND_UUID, ""));
		} catch (IllegalArgumentException ex) {
			// Bad text in tag
			return null;
		}
	}

	public static String getSoulbindNameS(ItemStack stack) {
		return ItemNBTHelper.getString(stack, TAG_SOLBIND_NAME, "");
	}

	public static void updateRelic(ItemStack stack, EntityPlayer player) {
		if(stack == null || !(stack.getItem() instanceof IRelic))
			return;

		UUID soulbind = getSoulbindUuidS(stack);
		if(soulbind == null) {
			player.addStat(((IRelic) stack.getItem()).getBindAchievement(), 1);
			bindToPlayer(player, stack);
			soulbind = getSoulbindUuidS(stack);
		}

		if(!isRightPlayer(player, stack) && player.ticksExisted % 10 == 0 && (!(stack.getItem() instanceof ItemRelic) || ((ItemRelic) stack.getItem()).shouldDamageWrongPlayer()))
			player.attackEntityFrom(damageSource(), 2);
	}

	public static void bindToPlayer(EntityPlayer player, ItemStack stack) {
		bindToUuidS(player.getUniqueID(), stack);
		bindToNameS(player.getName(), stack);
	}

	public static void bindToNameS(String playerName, ItemStack stack) {
		ItemNBTHelper.setString(stack, TAG_SOLBIND_NAME, playerName);
	}

	public static void bindToUuidS(UUID uuid, ItemStack stack) {
		ItemNBTHelper.setString(stack, TAG_SOULBIND_UUID, uuid.toString());
	}

	public static boolean isRightPlayer(EntityPlayer player, ItemStack stack) {
		return isRightPlayer(player.getUniqueID(), stack);
	}

	public static boolean isRightPlayer(UUID uuid, ItemStack stack) {
		return uuid.equals(getSoulbindUuidS(stack));
	}

	public static DamageSource damageSource() {
		return new DamageSource("botania-relic");
	}

	@Override
	public void bindToUuid(UUID playerName, ItemStack stack) {
		bindToUuidS(playerName, stack);
	}

	@Override
	public UUID getSoulbindUuid(ItemStack stack) {
		return getSoulbindUuidS(stack);
	}

	@Override
	public Achievement getBindAchievement() {
		return achievement;
	}

	@Override
	public void setBindAchievement(Achievement achievement) {
		this.achievement = achievement;
	}

	@Override
	public EnumRarity getRarity(ItemStack p_77613_1_) {
		return BotaniaAPI.rarityRelic;
	}

}
