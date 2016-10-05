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

import javax.annotation.Nonnull;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.Achievement;
import net.minecraft.util.DamageSource;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.UsernameCache;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.item.IRelic;
import vazkii.botania.common.core.helper.ItemNBTHelper;
import vazkii.botania.common.item.ItemMod;
import vazkii.botania.common.item.ModItems;

public class ItemRelic extends ItemMod implements IRelic {

	private static final String TAG_SOULBIND_NAME = "soulbind";
	private static final String TAG_SOULBIND_UUID = "soulbindUUID";

	private Achievement achievement;

	public ItemRelic(String name) {
		super(name);
		setMaxStackSize(1);
	}

	@Override
	public void onUpdate(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
		if(!world.isRemote && entity instanceof EntityPlayer)
			updateRelic(stack, (EntityPlayer) entity);
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void addInformation(ItemStack stack, EntityPlayer player, List<String> tooltip, boolean advanced) {
		addBindInfo(tooltip, stack, player);
	}

	@SideOnly(Side.CLIENT)
	public void addBindInfo(List<String> list, ItemStack stack, EntityPlayer player) {
		if(GuiScreen.isShiftKeyDown()) {
			if(!hasUUID(stack)) {
				addStringToTooltip(I18n.format("botaniamisc.relicUnbound"), list);
			} else {
				addStringToTooltip(I18n.format("botaniamisc.relicSoulbound", getSoulbindUsername(stack)), list);
				if(!getSoulbindUUID(stack).equals(player.getUniqueID()))
					addStringToTooltip(I18n.format("botaniamisc.notYourSagittarius", getSoulbindUsername(stack)), list);
			}

			if(stack.getItem() == ModItems.aesirRing)
				addStringToTooltip(I18n.format("botaniamisc.dropIkea"), list);

			if(stack.getItem() == ModItems.dice) {
				addStringToTooltip("", list);
				String name = stack.getUnlocalizedName() + ".poem";
				for(int i = 0; i < 4; i++)
					addStringToTooltip(TextFormatting.ITALIC + I18n.format(name + i), list);
			}
		} else addStringToTooltip(I18n.format("botaniamisc.shiftinfo"), list);
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

	public void updateRelic(ItemStack stack, EntityPlayer player) {
		if(stack == null || !(stack.getItem() instanceof IRelic))
			return;

		boolean rightPlayer = true;
		if(hasUUID(stack)) {
			// Sync to username todo is this worth 'optimizing'?
			if (UsernameCache.containsUUID(getSoulbindUUID(stack))) {
				bindToUsername(UsernameCache.getLastKnownUsername(getSoulbindUUID(stack)), stack);
			} else {
				bindToUsername("", stack);
			}

			// UUID trumps username
			rightPlayer = getSoulbindUUID(stack).equals(player.getUniqueID());
		} else {
			if ("".equals(getSoulbindUsername(stack))) {
				// New user
				bindToUUID(player.getUniqueID(), stack);
				player.addStat(((IRelic) stack.getItem()).getBindAchievement(), 1);
			} else {
				if (player.getName().equals(getSoulbindUsername(stack))) {
					// Old relic, correct owner, convert to UUID
					bindToUUID(player.getUniqueID(), stack);
				} else {
					// Old relic, wrong owner, damage
					rightPlayer = false;
				}
			}
		}

		if(!rightPlayer && player.ticksExisted % 10 == 0 && (!(stack.getItem() instanceof ItemRelic) || ((ItemRelic) stack.getItem()).shouldDamageWrongPlayer()))
			player.attackEntityFrom(damageSource(), 2);
	}

	public boolean isRightPlayer(EntityPlayer player, ItemStack stack) {
		if (hasUUID(stack)) {
			return getSoulbindUUID(stack).equals(player.getUniqueID());
		} else {
			return getSoulbindUsername(stack).equals(player.getName());
		}
	}

	public static DamageSource damageSource() {
		return new DamageSource("botania-relic");
	}

	@Override
	public void bindToUsername(String playerName, ItemStack stack) {
		ItemNBTHelper.setString(stack, TAG_SOULBIND_NAME, playerName);
	}

	@Override
	public String getSoulbindUsername(ItemStack stack) {
		return ItemNBTHelper.getString(stack, TAG_SOULBIND_NAME, "");
	}

	@Override
	public void bindToUUID(UUID uuid, ItemStack stack) {
		ItemNBTHelper.setString(stack, TAG_SOULBIND_UUID, uuid.toString());
	}

	@Override
	public UUID getSoulbindUUID(ItemStack stack) {
		if(ItemNBTHelper.verifyExistance(stack, TAG_SOULBIND_UUID)) {
			try {
				return UUID.fromString(ItemNBTHelper.getString(stack, TAG_SOULBIND_UUID, ""));
			} catch (IllegalArgumentException ex) { // Bad UUID in tag
				ItemNBTHelper.removeEntry(stack, TAG_SOULBIND_UUID);
			}
		}

		return null;
	}

	@Override
	public boolean hasUUID(ItemStack stack) {
		return getSoulbindUUID(stack) != null;
	}

	@Override
	public Achievement getBindAchievement() {
		return achievement;
	}

	@Override
	public void setBindAchievement(Achievement achievement) {
		this.achievement = achievement;
	}

	@Nonnull
	@Override
	public EnumRarity getRarity(ItemStack stack) {
		return BotaniaAPI.rarityRelic;
	}

}
