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

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.Achievement;
import net.minecraft.util.DamageSource;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import vazkii.botania.api.item.IRelic;
import vazkii.botania.common.core.helper.ItemNBTHelper;
import vazkii.botania.common.item.ItemMod;

public class ItemRelic extends ItemMod implements IRelic {

	private static final String TAG_SOULBIND = "soulbind";
	
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
	
	public static void addBindInfo(List list, ItemStack stack, EntityPlayer player) {
		String bind = getSoulbindUsernameS(stack);
		if(bind.isEmpty())
			addStringToTooltip(StatCollector.translateToLocal("botaniamisc.relicUnbound"), list);
		else {
			addStringToTooltip(String.format(StatCollector.translateToLocal("botaniamisc.relicSoulbound"), bind), list);
			if(!isRightPlayer(player, stack))
				addStringToTooltip(String.format(StatCollector.translateToLocal("botaniamisc.notYourSagittarius"), bind), list);
		}
	}
	
	static void addStringToTooltip(String s, List<String> tooltip) {
		tooltip.add(s.replaceAll("&", "\u00a7"));
	}
	
	public static String getSoulbindUsernameS(ItemStack stack) {
		return ItemNBTHelper.getString(stack, TAG_SOULBIND, "");
	}
	
	public static void updateRelic(ItemStack stack, EntityPlayer player) {
		String soulbind = getSoulbindUsernameS(stack);
		if(soulbind.isEmpty()) {
			bindToPlayer(player, stack);
			soulbind = getSoulbindUsernameS(stack);
		}
		
		if(!isRightPlayer(player, stack) && player.ticksExisted % 10 == 0)
			player.attackEntityFrom(damageSource(), 6);
	}
	
	public static void bindToPlayer(EntityPlayer player, ItemStack stack) {
		ItemNBTHelper.setString(stack, TAG_SOULBIND, player.getCommandSenderName());
	}
	
	public static boolean isRightPlayer(EntityPlayer player, ItemStack stack) {
		return isRightPlayer(player.getCommandSenderName(), stack);
	}
	
	public static boolean isRightPlayer(String player, ItemStack stack) {
		return getSoulbindUsernameS(stack).equals(player);
	}
	
	public static DamageSource damageSource() {
		return new DamageSource("botania-relic");
	}
	
	@Override
	public String getSoulbindUsername(ItemStack stack) {
		return getSoulbindUsernameS(stack);
	}

	@Override
	public Achievement getBindAchievement() {
		return achievement;
	}

	@Override
	public void setBindAchievement(Achievement achievement) {
		this.achievement = achievement;
	}

}
