/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under a
 * Creative Commons Attribution-NonCommercial-ShareAlike 3.0 License
 * (http://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB)
 * 
 * File Created @ [May 17, 2014, 3:16:36 PM (GMT)]
 */
package vazkii.botania.common.item.equipment.bauble;

import java.util.List;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.item.ItemTossEvent;
import vazkii.botania.client.core.helper.IconHelper;
import vazkii.botania.common.Botania;
import vazkii.botania.common.core.helper.ItemNBTHelper;
import vazkii.botania.common.core.helper.MathHelper;
import vazkii.botania.common.core.helper.Vector3;
import vazkii.botania.common.lib.LibItemNames;
import baubles.api.BaubleType;
import baubles.common.container.InventoryBaubles;
import baubles.common.lib.PlayerHandler;
import baubles.common.network.PacketHandler;
import baubles.common.network.PacketSyncBauble;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemMagnetRing extends ItemBauble {

	IIcon iconOff;

	private static final String TAG_COOLDOWN = "cooldown";

	public ItemMagnetRing() {
		super(LibItemNames.MAGNET_RING);
		MinecraftForge.EVENT_BUS.register(this);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IIconRegister par1IconRegister) {
		itemIcon = IconHelper.forItem(par1IconRegister, this, 0);
		iconOff = IconHelper.forItem(par1IconRegister, this, 1);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIconIndex(ItemStack stack) {
		return getCooldown(stack) <= 0 ? itemIcon : iconOff;
	}

	@SubscribeEvent
	public void onTossItem(ItemTossEvent event) {
		InventoryBaubles inv = PlayerHandler.getPlayerBaubles(event.player);
		for(int i = 0; i < inv.getSizeInventory(); i++) {
			ItemStack stack = inv.getStackInSlot(i);
			if(stack != null && stack.getItem() == this) {
				setCooldown(stack, 100);
				if(event.player instanceof EntityPlayerMP)
					PacketHandler.INSTANCE.sendTo(new PacketSyncBauble(event.player, i), (EntityPlayerMP) event.player);
			}
		}
	}

	@Override
	public void onWornTick(ItemStack stack, EntityLivingBase player) {
		super.onWornTick(stack, player);

		int cooldown = getCooldown(stack);

		if(cooldown <= 0) {
			if(!player.isSneaking()) {
				int range = 6;
				double x = player.posX;
				double y = player.posY -(player.worldObj.isRemote ? 1.62 : 0) + 0.75;
				double z = player.posZ;

				List<EntityItem> items = player.worldObj.getEntitiesWithinAABB(EntityItem.class, AxisAlignedBB.getBoundingBox(x - range, y - range, z - range, x + range, y + range, z + range));
				for(EntityItem item : items) {
					MathHelper.setEntityMotionFromVector(item, new Vector3(x, y, z), 0.45F);
					if(player.worldObj.isRemote) {
						boolean red = player.worldObj.rand.nextBoolean();
						Botania.proxy.sparkleFX(player.worldObj, item.posX, item.posY, item.posZ, red ? 1F : 0F, 0F, red ? 0F : 1F, 1F, 3);
					}
				}
			}
		} else setCooldown(stack, cooldown - 1);
	}

	public static int getCooldown(ItemStack stack) {
		return ItemNBTHelper.getInt(stack, TAG_COOLDOWN, 0);
	}

	public static void setCooldown(ItemStack stack, int cooldown) {
		ItemNBTHelper.setInt(stack, TAG_COOLDOWN, cooldown);
	}

	@Override
	public BaubleType getBaubleType(ItemStack arg0) {
		return BaubleType.RING;
	}


}
