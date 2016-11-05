/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Mar 29, 2015, 7:56:27 PM (GMT)]
 */
package vazkii.botania.common.item.relic;

import java.util.List;
import java.util.UUID;

import javax.annotation.Nonnull;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.Achievement;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.item.IRelic;
import vazkii.botania.common.item.equipment.bauble.ItemBauble;

public abstract class ItemRelicBauble extends ItemBauble implements IRelic {

	Achievement achievement;
	private final ItemRelic dummy = new ItemRelic("dummy") { // Delegate for relic stuff
		@Override
		protected boolean shouldRegister() {
			return false;
		}
	};

	public ItemRelicBauble(String name) {
		super(name);
	}

	@Override
	public void onUpdate(ItemStack stack, World world, Entity entity, int slot, boolean held) {
		if(entity instanceof EntityPlayer)
			dummy.updateRelic(stack, (EntityPlayer) entity);
	}

	@Override
	public void onWornTick(ItemStack stack, EntityLivingBase player) {
		super.onWornTick(stack, player);
		if(player instanceof EntityPlayer) {
			EntityPlayer ePlayer = (EntityPlayer) player;
			dummy.updateRelic(stack, ePlayer);
			if(dummy.isRightPlayer(ePlayer, stack))
				onValidPlayerWornTick(stack, ePlayer);
		}
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void addHiddenTooltip(ItemStack par1ItemStack, EntityPlayer player, List<String> stacks, boolean par4) {
		super.addHiddenTooltip(par1ItemStack, player, stacks, par4);
		dummy.addBindInfo(stacks, par1ItemStack, player);
	}

	public void onValidPlayerWornTick(ItemStack stack, EntityPlayer player) {}

	@Override
	public boolean canEquip(ItemStack stack, EntityLivingBase player) {
		return player instanceof EntityPlayer && dummy.isRightPlayer((EntityPlayer) player, stack);
	}

	@Override
	public void bindToUsername(String playerName, ItemStack stack) {
		dummy.bindToUsername(playerName, stack);
	}

	@Override
	public String getSoulbindUsername(ItemStack stack) {
		return dummy.getSoulbindUsername(stack);
	}

	@Override
	public void bindToUUID(UUID uuid, ItemStack stack) {
		dummy.bindToUUID(uuid, stack);
	}

	@Override
	public UUID getSoulbindUUID(ItemStack stack) {
		return dummy.getSoulbindUUID(stack);
	}

	@Override
	public boolean hasUUID(ItemStack stack) {
		return dummy.hasUUID(stack);
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

	@Override
	public int getEntityLifespan(ItemStack itemStack, World world) {
		return Integer.MAX_VALUE;
	}

}
