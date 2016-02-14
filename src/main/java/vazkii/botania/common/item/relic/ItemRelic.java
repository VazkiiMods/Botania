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

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.stats.Achievement;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.common.UsernameCache;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.item.IRelic;
import vazkii.botania.common.core.helper.ItemNBTHelper;
import vazkii.botania.common.item.ItemMod;
import vazkii.botania.common.item.ModItems;

public class ItemRelic extends ItemMod implements IRelic {

	@Deprecated
	private static final String TAG_SOULBIND = "soulbind";
	private static final String TAG_SOULBIND_UUID = "soulbindUUID";

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
			String bind = getSoulbindUsernameS(stack);
			if(bind.isEmpty())
				addStringToTooltip(StatCollector.translateToLocal("botaniamisc.relicUnbound"), list);
			else {
				addStringToTooltip(String.format(StatCollector.translateToLocal("botaniamisc.relicSoulbound"), bind), list);
				if(!isRightPlayer(player, stack))
					addStringToTooltip(String.format(StatCollector.translateToLocal("botaniamisc.notYourSagittarius"), bind), list);
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

	public static String getSoulbindUsernameS(ItemStack stack) {
		if(hasUUIDS(stack))
			return UsernameCache.getLastKnownUsername(getSoulbindUUIDS(stack));
		return ItemNBTHelper.getString(stack, TAG_SOULBIND, "");
	}

	//TODO: Better name?
	public static UUID getSoulbindUUIDS(ItemStack stack) {
		if(hasUUIDS(stack))
			return UUID.fromString(ItemNBTHelper.getString(stack, TAG_SOULBIND_UUID, ""));
		return null;
	}

	public static void updateRelic(ItemStack stack, EntityPlayer player) {
		if(stack == null || !(stack.getItem() instanceof IRelic))
			return;

		if(!hasUUIDS(stack) && isRightPlayer(player.getName(), stack))
			bindToPlayer(player, stack);

		String soulbind = getSoulbindUsernameS(stack);
		if(soulbind.isEmpty()) {
			player.addStat(((IRelic) stack.getItem()).getBindAchievement(), 1);
			bindToPlayer(player, stack);
			soulbind = getSoulbindUsernameS(stack);
		}

		if(!isRightPlayer(player, stack) && player.ticksExisted % 10 == 0 && (!(stack.getItem() instanceof ItemRelic) || ((ItemRelic) stack.getItem()).shouldDamageWrongPlayer()))
			player.attackEntityFrom(damageSource(), 2);
	}

	public static void bindToPlayer(EntityPlayer player, ItemStack stack) {
		bindToUUIDS(player.getUniqueID(), stack);
	}

	@Deprecated
	public static void bindToUsernameS(String username, ItemStack stack) {
		ItemNBTHelper.setString(stack, TAG_SOULBIND, username);
	}

	//TODO: Better name?
	public static void bindToUUIDS(UUID uuid, ItemStack stack) {
		ItemNBTHelper.setString(stack, TAG_SOULBIND_UUID, uuid.toString());
	}

	public static boolean isRightPlayer(EntityPlayer player, ItemStack stack) {
		if(hasUUIDS(stack))
			return isRightPlayer(player.getUniqueID(), stack);
		return isRightPlayer(player.getName(), stack);
	}

	public static boolean isRightPlayer(String player, ItemStack stack) {
		return getSoulbindUsernameS(stack).equals(player);
	}

	public static boolean isRightPlayer(UUID uuid, ItemStack stack) {
		return getSoulbindUUIDS(stack) != null && getSoulbindUUIDS(stack).equals(uuid);
	}

	public static DamageSource damageSource() {
		return new DamageSource("botania-relic");
	}

	//TODO: Better name?
	public static boolean hasUUIDS(ItemStack stack) {
		return ItemNBTHelper.verifyExistance(stack, TAG_SOULBIND_UUID);
	}

	public static UUID usernameToUUID(String username) {
		return getPlayerByName(username) == null ? null : getPlayerByName(username).getUniqueID();
	}

	public static EntityPlayer getPlayerByName(String username) {
		if(username.isEmpty())
			return null;

		List<EntityPlayerMP> playerList = MinecraftServer.getServer().getConfigurationManager().playerEntityList;
		for (EntityPlayerMP player : playerList) {
			if (player.getName().equals(username))
				return player;
		}

		return null;
	}

	@Override
	@Deprecated
	public void bindToUsername(String playerName, ItemStack stack) {
		bindToUsernameS(playerName, stack);
	}

	@Override
	public String getSoulbindUsername(ItemStack stack) {
		return getSoulbindUsernameS(stack);
	}

	@Override
	public void bindToUUID(UUID uuid, ItemStack stack) {
		bindToUUIDS(uuid, stack);
	}

	@Override
	public UUID getSoulbindUUID(ItemStack stack) {
		return getSoulbindUUIDS(stack);
	}

	@Override
	public boolean hasUUID(ItemStack stack) {
		return hasUUIDS(stack);
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
