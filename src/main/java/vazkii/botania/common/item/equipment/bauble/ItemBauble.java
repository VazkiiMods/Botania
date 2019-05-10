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

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import vazkii.botania.api.item.ICosmeticAttachable;
import vazkii.botania.api.item.IPhantomInkable;
import vazkii.botania.common.core.handler.ModSounds;
import vazkii.botania.common.core.helper.ItemNBTHelper;
import vazkii.botania.common.core.helper.PlayerHelper;
import vazkii.botania.common.entity.EntityDoppleganger;
import vazkii.botania.common.item.ItemMod;
import vazkii.botania.common.lib.LibMisc;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.UUID;

@Mod.EventBusSubscriber(modid = LibMisc.MOD_ID)
public abstract class ItemBauble extends ItemMod implements ICosmeticAttachable, IPhantomInkable {

	private static final String TAG_HASHCODE = "playerHashcode";
	private static final String TAG_BAUBLE_UUID_MOST = "baubleUUIDMost";
	private static final String TAG_BAUBLE_UUID_LEAST = "baubleUUIDLeast";
	private static final String TAG_COSMETIC_ITEM = "cosmeticItem";
	private static final String TAG_PHANTOM_INK = "phantomInk";

	public ItemBauble(Properties props) {
		super(props);
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void addInformation(ItemStack par1ItemStack, World world, List<ITextComponent> stacks, ITooltipFlag flags) {
		if(GuiScreen.isShiftKeyDown())
			addHiddenTooltip(par1ItemStack, world, stacks, flags);
		else stacks.add(new TextComponentTranslation("botaniamisc.shiftinfo"));
	}

	@OnlyIn(Dist.CLIENT)
	public void addHiddenTooltip(ItemStack par1ItemStack, World world, List<ITextComponent> stacks, ITooltipFlag flags) {
		String key = vazkii.botania.client.core.helper.RenderHelper.getKeyDisplayString("key.curios.open.desc");

		if(key != null)
			stacks.add(new TextComponentTranslation("botania.baubletooltip", key));

		ItemStack cosmetic = getCosmeticItem(par1ItemStack);
		if(!cosmetic.isEmpty())
			stacks.add(new TextComponentTranslation("botaniamisc.hasCosmetic", cosmetic.getDisplayName()));

		if(hasPhantomInk(par1ItemStack))
			stacks.add(new TextComponentTranslation("botaniamisc.hasPhantomInk"));
	}

	public void onWornTick(ItemStack stack, EntityLivingBase player) {
		if(getLastPlayerHashcode(stack) != player.hashCode()) {
			setLastPlayerHashcode(stack, player.hashCode());
		}
	}

	public void onEquipped(ItemStack stack, EntityLivingBase player) {
		if(player != null) {
			setLastPlayerHashcode(stack, player.hashCode());
		}
	}

	public void onUnequipped(ItemStack stack, EntityLivingBase player) { }

	@Override
	public ItemStack getCosmeticItem(ItemStack stack) {
		NBTTagCompound cmp = ItemNBTHelper.getCompound(stack, TAG_COSMETIC_ITEM, true);
		if(cmp == null)
			return ItemStack.EMPTY;
		return ItemStack.read(cmp);
	}

	@Override
	public void setCosmeticItem(ItemStack stack, ItemStack cosmetic) {
		NBTTagCompound cmp = new NBTTagCompound();
		if(!cosmetic.isEmpty())
			cmp = cosmetic.write(cmp);
		ItemNBTHelper.setCompound(stack, TAG_COSMETIC_ITEM, cmp);
	}

	@Override
	public boolean hasContainerItem(ItemStack stack) {
		return !getContainerItem(stack).isEmpty();
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
}
