/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [May 17, 2014, 3:16:36 PM (GMT)]
 */
package vazkii.botania.common.item.equipment.bauble;

import java.util.Arrays;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.item.ItemTossEvent;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.item.IRelic;
import vazkii.botania.api.mana.IManaItem;
import vazkii.botania.client.core.helper.IconHelper;
import vazkii.botania.common.Botania;
import vazkii.botania.common.block.subtile.functional.SubTileSolegnolia;
import vazkii.botania.common.core.handler.ConfigHandler;
import vazkii.botania.common.core.helper.ItemNBTHelper;
import vazkii.botania.common.core.helper.MathHelper;
import vazkii.botania.common.core.helper.Vector3;
import vazkii.botania.common.lib.LibItemNames;
import baubles.api.BaubleType;
import baubles.common.container.InventoryBaubles;
import baubles.common.lib.PlayerHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemMagnetRing extends ItemBauble {

	IIcon iconOff;

	private static final String TAG_COOLDOWN = "cooldown";

	private static final List<String> BLACKLIST = Arrays.asList(new String[] {
			"appliedenergistics2:item.ItemCrystalSeed",
	});

	int range;

	public ItemMagnetRing() {
		this(LibItemNames.MAGNET_RING, 6);
		MinecraftForge.EVENT_BUS.register(this);
	}

	public ItemMagnetRing(String name, int range) {
		super(name);
		this.range = range;
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
			if(stack != null && stack.getItem() instanceof ItemMagnetRing) {
				setCooldown(stack, 100);
				BotaniaAPI.internalHandler.sendBaubleUpdatePacket(event.player, i);
			}
		}
	}

	@Override
	public void onWornTick(ItemStack stack, EntityLivingBase player) {
		super.onWornTick(stack, player);

		int cooldown = getCooldown(stack);

		if(SubTileSolegnolia.hasSolegnoliaAround(player)) {
			if(cooldown < 0)
				setCooldown(stack, 2);
			return;
		}

		if(cooldown <= 0) {
			if(player.isSneaking() == ConfigHandler.invertMagnetRing) {
				double x = player.posX;
				double y = player.posY -(player.worldObj.isRemote ? 1.62 : 0) + 0.75;
				double z = player.posZ;

				List<EntityItem> items = player.worldObj.getEntitiesWithinAABB(EntityItem.class, AxisAlignedBB.getBoundingBox(x - range, y - range, z - range, x + range, y + range, z + range));
				int pulled = 0;
				for(EntityItem item : items)
					if(canPullItem(item)) {
						if(pulled > 200)
							break;

						MathHelper.setEntityMotionFromVector(item, new Vector3(x, y, z), 0.45F);
						if(player.worldObj.isRemote) {
							boolean red = player.worldObj.rand.nextBoolean();
							Botania.proxy.sparkleFX(player.worldObj, item.posX, item.posY, item.posZ, red ? 1F : 0F, 0F, red ? 0F : 1F, 1F, 3);
						}
						pulled++;
					}
			}
		} else setCooldown(stack, cooldown - 1);
	}

	private boolean canPullItem(EntityItem item) {
		if(item.isDead || SubTileSolegnolia.hasSolegnoliaAround(item))
			return false;

		ItemStack stack = item.getEntityItem();
		if(stack == null || stack.getItem() instanceof IManaItem || stack.getItem() instanceof IRelic || BLACKLIST.contains(itemRegistry.getNameForObject(stack.getItem())) || BotaniaAPI.isItemBlacklistedFromMagnet(stack))
			return false;

		int x = net.minecraft.util.MathHelper.floor_double(item.posX);
		int y = (int) Math.floor(item.posY);
		int z = net.minecraft.util.MathHelper.floor_double(item.posZ);
		Block block = item.worldObj.getBlock(x, y, z);
		int meta = item.worldObj.getBlockMetadata(x, y, z);

		if(BotaniaAPI.isBlockBlacklistedFromMagnet(block, meta))
			return false;

		block = item.worldObj.getBlock(x, y - 1, z);
		meta = item.worldObj.getBlockMetadata(x, y - 1, z);

		if(BotaniaAPI.isBlockBlacklistedFromMagnet(block, meta))
			return false;

		return true;
	}

	public static int getCooldown(ItemStack stack) {
		return ItemNBTHelper.getInt(stack, TAG_COOLDOWN, 0);
	}

	public static void setCooldown(ItemStack stack, int cooldown) {
		ItemNBTHelper.setInt(stack, TAG_COOLDOWN, cooldown);
	}

	public static void addItemToBlackList(String item) {
		BLACKLIST.add(item);
	}

	@Override
	public BaubleType getBaubleType(ItemStack arg0) {
		return BaubleType.RING;
	}


}
