/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [Jan 18, 2015, 12:17:10 AM (GMT)]
 */
package vazkii.botania.common.item;

import java.util.List;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.StatCollector;
import vazkii.botania.api.internal.VanillaPacketDispatcher;
import vazkii.botania.api.item.IManaDissolvable;
import vazkii.botania.api.mana.IManaPool;
import vazkii.botania.common.Botania;
import vazkii.botania.common.lib.LibItemNames;

public class ItemBlackLotus extends ItemMod implements IManaDissolvable {

	private static final int MANA_PER = 8000;
	private static final int MANA_PER_T2 = 100000;

	public ItemBlackLotus() {
		setUnlocalizedName(LibItemNames.BLACK_LOTUS);
		setHasSubtypes(true);
	}

	@Override
	public void getSubItems(Item item, CreativeTabs tab, List list) {
		for(int i = 0; i < 2; i++)
			list.add(new ItemStack(item, 1, i));
	}

	@Override
	public boolean hasEffect(ItemStack par1ItemStack, int pass) {
		return par1ItemStack.getItemDamage() > 0;
	}

	@Override
	public String getUnlocalizedName(ItemStack par1ItemStack) {
		return super.getUnlocalizedName(par1ItemStack) + par1ItemStack.getItemDamage();
	}

	@Override
	public void onDissolveTick(IManaPool pool, ItemStack stack, EntityItem item) {
		if(pool.isFull() || pool.getCurrentMana() == 0)
			return;

		TileEntity tile = (TileEntity) pool;
		boolean t2 = stack.getItemDamage() > 0;

		if(!item.worldObj.isRemote) {
			pool.recieveMana(t2 ? MANA_PER_T2 : MANA_PER);
			stack.stackSize--;
			VanillaPacketDispatcher.dispatchTEToNearbyPlayers(item.worldObj, tile.xCoord, tile.yCoord, tile.zCoord);
		}

		for(int i = 0; i < 50; i++) {
			float r = (float) Math.random() * 0.25F;
			float g = 0F;
			float b = (float) Math.random() * 0.25F;
			float s = 0.45F * (float) Math.random() * 0.25F;

			float m = 0.045F;
			float mx = ((float) Math.random() - 0.5F) * m;
			float my = (float) Math.random() * m;
			float mz = ((float) Math.random() - 0.5F) * m;

			Botania.proxy.wispFX(item.worldObj, item.posX, tile.yCoord + 0.5F, item.posZ, r, g, b, s, mx, my, mz);
		}
		item.worldObj.playSoundAtEntity(item, "botania:blackLotus", 0.5F, t2 ? 0.1F : 1F);
	}

	@Override
	public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean adv) {
		list.add(StatCollector.translateToLocal("botaniamisc.lotusDesc"));
	}

}
