/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under a
 * Creative Commons Attribution-NonCommercial-ShareAlike 3.0 License
 * (http://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB)
 * 
 * File Created @ [Jul 31, 2014, 12:59:16 AM (GMT)]
 */
package vazkii.botania.common.item.equipment.bauble;

import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.AnimalChest;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import vazkii.botania.common.Botania;
import vazkii.botania.common.core.helper.ItemNBTHelper;
import vazkii.botania.common.lib.LibItemNames;
import vazkii.botania.common.lib.LibObfuscation;
import baubles.api.BaubleType;
import baubles.common.container.InventoryBaubles;
import baubles.common.lib.PlayerHandler;
import baubles.common.network.PacketHandler;
import baubles.common.network.PacketSyncBauble;
import cpw.mods.fml.relauncher.ReflectionHelper;

public class ItemItemFinder extends ItemBauble {

	private static final String TAG_POSITIONS = "highlightPositions";

	public ItemItemFinder() {
		super(LibItemNames.ITEM_FINDER);
	}

	@Override
	public void onWornTick(ItemStack stack, EntityLivingBase player) {
		super.onWornTick(stack, player);

		if(!(player instanceof EntityPlayer))
			return;

		if(player.worldObj.isRemote)
			tickClient(stack, (EntityPlayer) player);
		else tickServer(stack, (EntityPlayer) player);
	}

	public void tickClient(ItemStack stack, EntityPlayer player) {
		String pos = ItemNBTHelper.getString(stack, TAG_POSITIONS, "");
		String[] tokens = pos.split(";");
		for(String token : tokens) {
			if(token.isEmpty())
				continue;

			if(token.contains(",")) {
				String[] tokens_ = token.split(",");
				int x = Integer.parseInt(tokens_[0]);
				int y = Integer.parseInt(tokens_[1]);
				int z = Integer.parseInt(tokens_[2]);
				float m = 0.08F;
				Botania.proxy.wispFX(player.worldObj, x + (float) Math.random(), y + 0.5, z + (float) Math.random(), 0.2F * (float) Math.random(), 1F, 0.2F * (float) Math.random(), 0.15F + 0.05F * (float) Math.random(), m * (float) (Math.random() - 0.5), m * (float) (Math.random() - 0.5), m * (float) (Math.random() - 0.5));
			} else {
				int id = Integer.parseInt(token);
				Entity e = player.worldObj.getEntityByID(id);

				if(e != null && Math.random() < 0.6)
					Botania.proxy.wispFX(player.worldObj, e.posX + (float) (Math.random() * 0.5 - 0.25) * 0.45F, e.posY + e.height, e.posZ + (float) (Math.random() * 0.5 - 0.25) * 0.45F, 0.2F * (float) Math.random(), 1F, 0.2F * (float) Math.random(), 0.15F + 0.05F * (float) Math.random(), -0.05F - 0.03F * (float) Math.random());
			}
		}
	}

	public void tickServer(ItemStack stack, EntityPlayer player) {
		ItemStack pstack = player.getCurrentEquippedItem();
		StringBuilder positionsBuilder = new StringBuilder();

		if(pstack != null || player.isSneaking()) {
			int range = 24;

			List<Entity> entities = player.worldObj.getEntitiesWithinAABB(Entity.class, AxisAlignedBB.getBoundingBox(player.posX - range, player.posY - range, player.posZ - range, player.posX + range, player.posY + range, player.posZ + range));
			for(Entity e : entities) {
				if(e == player)
					continue;

				if(e instanceof EntityItem) {
					EntityItem item = (EntityItem) e;
					ItemStack istack = item.getEntityItem();
					if(player.isSneaking() || (istack.isItemEqual(pstack) && ItemStack.areItemStackTagsEqual(istack, pstack)))
						positionsBuilder.append(item.getEntityId()).append(";");
				} else if(e instanceof IInventory) {
					IInventory inv = (IInventory) e;
					if(scanInventory(inv, pstack))
						positionsBuilder.append(e.getEntityId()).append(";");
				} else if(e instanceof EntityHorse) {
					EntityHorse horse = (EntityHorse) e;
					AnimalChest chest = ReflectionHelper.getPrivateValue(EntityHorse.class, horse, LibObfuscation.HORSE_CHEST);
					if(scanInventory(chest, pstack))
						positionsBuilder.append(horse.getEntityId()).append(";");
				} else if(e instanceof EntityPlayer) {
					EntityPlayer player_ = (EntityPlayer) e;
					InventoryPlayer inv = player_.inventory;
					InventoryBaubles binv = PlayerHandler.getPlayerBaubles(player_);
					if(scanInventory(inv, pstack) || scanInventory(binv, pstack))
						positionsBuilder.append(player_.getEntityId()).append(";");
				} else if(e instanceof EntityLivingBase) {
					EntityLivingBase living = (EntityLivingBase) e;
					ItemStack estack = living.getEquipmentInSlot(0);
					if(pstack != null && estack != null && estack.isItemEqual(pstack) && ItemStack.areItemStackTagsEqual(estack, pstack))
						positionsBuilder.append(living.getEntityId()).append(";");
				}
			}

			if(pstack != null) {
				range = 12;
				int x = (int) MathHelper.floor_double(player.posX);
				int y = (int) MathHelper.floor_double(player.posY);
				int z = (int) MathHelper.floor_double(player.posZ);
				for(int i = -range; i < range + 1; i++)
					for(int j = -range; j < range + 1; j++)
						for(int k = -range; k < range + 1; k++) {
							int xp = x + i;
							int yp = y + j;
							int zp = z + k;
							TileEntity tile = player.worldObj.getTileEntity(xp, yp, zp);
							if(tile != null && tile instanceof IInventory) {
								IInventory inv = (IInventory) tile;
								if(scanInventory(inv, pstack))
									positionsBuilder.append(xp).append(",").append(yp).append(",").append(zp).append(";");
							}
						}
			}
		}

		String current = ItemNBTHelper.getString(stack, TAG_POSITIONS, "");
		String positions = positionsBuilder.toString();
		if(!current.equals(positions)) {
			ItemNBTHelper.setString(stack, TAG_POSITIONS, positions);
			PacketHandler.INSTANCE.sendToAll(new PacketSyncBauble(player, 0));
		}
	}

	boolean scanInventory(IInventory inv, ItemStack pstack) {
		if(pstack == null)
			return false;

		for(int l = 0; l < inv.getSizeInventory(); l++) {
			ItemStack istack = inv.getStackInSlot(l);
			if(istack != null && istack.isItemEqual(pstack) && ItemStack.areItemStackTagsEqual(istack, pstack))
				return true;
		}

		return false;
	}

	@Override
	public BaubleType getBaubleType(ItemStack arg0) {
		return BaubleType.AMULET;
	}

}
