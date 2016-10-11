/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Apr 20, 2014, 3:30:06 PM (GMT)]
 */
package vazkii.botania.common.item.equipment.bauble;

import java.util.List;
import java.util.UUID;

import javax.annotation.Nonnull;

import baubles.api.BaubleType;
import baubles.api.BaublesApi;
import baubles.api.IBauble;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Optional;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import thaumcraft.api.items.IRunicArmor;
import vazkii.botania.api.item.ICosmeticAttachable;
import vazkii.botania.api.item.IPhantomInkable;
import vazkii.botania.api.sound.BotaniaSoundEvents;
import vazkii.botania.common.achievement.ModAchievements;
import vazkii.botania.common.core.helper.ItemNBTHelper;
import vazkii.botania.common.entity.EntityDoppleganger;
import vazkii.botania.common.item.ItemMod;

@Optional.Interface(modid = "Thaumcraft", iface = "thaumcraft.api.items.IRunicArmor")
public abstract class ItemBauble extends ItemMod implements IBauble, ICosmeticAttachable, IPhantomInkable, IRunicArmor {

	private static final String TAG_HASHCODE = "playerHashcode";
	private static final String TAG_BAUBLE_UUID_MOST = "baubleUUIDMost";
	private static final String TAG_BAUBLE_UUID_LEAST = "baubleUUIDLeast";
	private static final String TAG_COSMETIC_ITEM = "cosmeticItem";
	private static final String TAG_PHANTOM_INK = "phantomInk";

	public ItemBauble(String name) {
		super(name);
		setMaxStackSize(1);
	}

	@Nonnull
	@Override
	public ActionResult<ItemStack> onItemRightClick(@Nonnull ItemStack stack, World world, EntityPlayer player, EnumHand hand) {
		if(!EntityDoppleganger.isTruePlayer(player))
			return ActionResult.newResult(EnumActionResult.FAIL, stack);

		ItemStack toEquip = stack.copy();
		toEquip.stackSize = 1;

		if(canEquip(toEquip, player)) {
			IInventory baubles = BaublesApi.getBaubles(player);
			for(int i = 0; i < baubles.getSizeInventory(); i++) {
				if(baubles.isItemValidForSlot(i, toEquip)) {
					ItemStack stackInSlot = baubles.getStackInSlot(i);
					if(stackInSlot == null || ((IBauble) stackInSlot.getItem()).canUnequip(stackInSlot, player)) {
						if(!world.isRemote) {
							baubles.setInventorySlotContents(i, toEquip);
							stack.stackSize--;
						}

						if(stackInSlot != null) {
							((IBauble) stackInSlot.getItem()).onUnequipped(stackInSlot, player);
							return ActionResult.newResult(EnumActionResult.SUCCESS, stackInSlot.copy());
						}
						break;
					}
				}
			}
		}

		return ActionResult.newResult(EnumActionResult.PASS, stack);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack par1ItemStack, EntityPlayer player, List<String> stacks, boolean par4) {
		if(GuiScreen.isShiftKeyDown())
			addHiddenTooltip(par1ItemStack, player, stacks, par4);
		else addStringToTooltip(I18n.format("botaniamisc.shiftinfo"), stacks);
	}

	@SideOnly(Side.CLIENT)
	public void addHiddenTooltip(ItemStack par1ItemStack, EntityPlayer player, List<String> stacks, boolean par4) {
		BaubleType type = getBaubleType(par1ItemStack);
		addStringToTooltip(I18n.format("botania.baubletype." + type.name().toLowerCase()), stacks);

		String key = vazkii.botania.client.core.helper.RenderHelper.getKeyDisplayString("Baubles Inventory");

		if(key != null)
			addStringToTooltip(I18n.format("botania.baubletooltip", key), stacks);

		ItemStack cosmetic = getCosmeticItem(par1ItemStack);
		if(cosmetic != null)
			addStringToTooltip(I18n.format("botaniamisc.hasCosmetic", cosmetic.getDisplayName()), stacks);

		if(hasPhantomInk(par1ItemStack))
			addStringToTooltip(I18n.format("botaniamisc.hasPhantomInk"), stacks);
	}

	void addStringToTooltip(String s, List<String> tooltip) {
		tooltip.add(s.replaceAll("&", "\u00a7"));
	}

	@Override
	public boolean canEquip(ItemStack stack, EntityLivingBase player) {
		return true;
	}

	@Override
	public boolean canUnequip(ItemStack stack, EntityLivingBase player) {
		return true;
	}

	@Override
	public void onWornTick(ItemStack stack, EntityLivingBase player) {
		if(getLastPlayerHashcode(stack) != player.hashCode()) {
			onEquippedOrLoadedIntoWorld(stack, player);
			setLastPlayerHashcode(stack, player.hashCode());
		}
	}

	@Override
	public void onEquipped(ItemStack stack, EntityLivingBase player) {
		if(player != null) {
			if(!player.worldObj.isRemote)
				player.worldObj.playSound(null, player.posX, player.posY, player.posZ, BotaniaSoundEvents.equipBauble, SoundCategory.PLAYERS, 0.1F, 1.3F);

			if(player instanceof EntityPlayer)
				((EntityPlayer) player).addStat(ModAchievements.baubleWear, 1);

			onEquippedOrLoadedIntoWorld(stack, player);
			setLastPlayerHashcode(stack, player.hashCode());
		}
	}

	public void onEquippedOrLoadedIntoWorld(ItemStack stack, EntityLivingBase player) {}

	@Override
	public void onUnequipped(ItemStack stack, EntityLivingBase player) {}

	@Override
	public ItemStack getCosmeticItem(ItemStack stack) {
		NBTTagCompound cmp = ItemNBTHelper.getCompound(stack, TAG_COSMETIC_ITEM, true);
		if(cmp == null)
			return null;
		return ItemStack.loadItemStackFromNBT(cmp);
	}

	@Override
	public void setCosmeticItem(ItemStack stack, ItemStack cosmetic) {
		NBTTagCompound cmp = new NBTTagCompound();
		if(cosmetic != null)
			cosmetic.writeToNBT(cmp);
		ItemNBTHelper.setCompound(stack, TAG_COSMETIC_ITEM, cmp);
	}

	@Override
	public boolean hasContainerItem(ItemStack stack) {
		return getContainerItem(stack) != null;
	}

	@Nonnull
	@Override
	public ItemStack getContainerItem(@Nonnull ItemStack itemStack) {
		return getCosmeticItem(itemStack);
	}

	public static UUID getBaubleUUID(ItemStack stack) {
		long most = ItemNBTHelper.getLong(stack, TAG_BAUBLE_UUID_MOST, 0);
		if(most == 0) {
			UUID uuid = UUID.randomUUID();
			ItemNBTHelper.setLong(stack, TAG_BAUBLE_UUID_MOST, uuid.getMostSignificantBits());
			ItemNBTHelper.setLong(stack, TAG_BAUBLE_UUID_LEAST, uuid.getLeastSignificantBits());
			return getBaubleUUID(stack);
		}

		long least = ItemNBTHelper.getLong(stack, TAG_BAUBLE_UUID_LEAST, 0);
		return new UUID(most, least);
	}

	public static int getLastPlayerHashcode(ItemStack stack) {
		return ItemNBTHelper.getInt(stack, TAG_HASHCODE, 0);
	}

	public static void setLastPlayerHashcode(ItemStack stack, int hash) {
		ItemNBTHelper.setInt(stack, TAG_HASHCODE, hash);
	}

	@Override
	public boolean hasPhantomInk(ItemStack stack) {
		return ItemNBTHelper.getBoolean(stack, TAG_PHANTOM_INK, false);
	}

	@Override
	public void setPhantomInk(ItemStack stack, boolean ink) {
		ItemNBTHelper.setBoolean(stack, TAG_PHANTOM_INK, ink);
	}

	@Override
	@Optional.Method(modid = "Thaumcraft")
	public int getRunicCharge(ItemStack itemstack) {
		return 0;
	}
}
