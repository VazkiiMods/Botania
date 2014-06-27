/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under a
 * Creative Commons Attribution-NonCommercial-ShareAlike 3.0 License
 * (http://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB)
 * 
 * File Created @ [Jun 20, 2014, 7:09:51 PM (GMT)]
 */
package vazkii.botania.common.item;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import vazkii.botania.api.mana.IManaUsingItem;
import vazkii.botania.api.mana.ManaItemHandler;
import vazkii.botania.common.Botania;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.block.tile.TileBifrost;
import vazkii.botania.common.core.helper.Vector3;
import vazkii.botania.common.lib.LibItemNames;

public class ItemRainbowRod extends ItemMod implements IManaUsingItem {

	private static final int MANA_COST = 750;
	private static final int TIME = 300;

	public ItemRainbowRod() {
		setMaxDamage(TIME);
		setUnlocalizedName(LibItemNames.RAINBOW_ROD);
		setMaxStackSize(1);
	}

	@Override
	public ItemStack onItemRightClick(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer) {
		if(!par2World.isRemote && par1ItemStack.getItemDamage() == 0 && ManaItemHandler.requestManaExact(par1ItemStack, par3EntityPlayer, MANA_COST, false)) {
			Block place = ModBlocks.bifrost;
			Vector3 vector = new Vector3(par3EntityPlayer.getLookVec()).normalize();

			double x = par3EntityPlayer.posX;
			double y = par3EntityPlayer.posY;
			double z = par3EntityPlayer.posZ;

			double lx = 0;
			double ly = -1;
			double lz = 0;

			int count = 0;

			while(count < 100 && (int) lx == (int) x && (int) ly == (int) y && (int) lz == (int) z || count < 4 || par2World.getBlock((int) x, (int) y, (int) z).isAir(par2World, (int) x, (int) y, (int) z) || par2World.getBlock((int) x, (int) y, (int) z) == place) {
				if(y >= 256 || y <= 0)
					break;

				for(int i = -2; i < 1; i++)
					for(int j = -2; j < 1; j++)
						if(par2World.getBlock((int) x + i, (int) y, (int) z + j).isAir(par2World, (int) x + i, (int) y, (int) z + j) || par2World.getBlock((int) x + i, (int) y, (int) z + j) == place) {
							par2World.setBlock((int) x + i, (int) y, (int) z + j, place);
							TileBifrost tile = (TileBifrost) par2World.getTileEntity((int) x + i, (int) y, (int) z + j);
							if(tile != null) {
								for(int k = 0; k < 4; k++)
									Botania.proxy.sparkleFX(par2World, tile.xCoord + Math.random(), tile.yCoord + Math.random(), tile.zCoord + Math.random(), (float) Math.random(), (float) Math.random(), (float) Math.random(), 0.45F + 0.2F * (float) Math.random(), 6);
								tile.ticks = TIME;
							}
						}

				lx = x;
				ly = y;
				lz = z;

				x += vector.x;
				y += vector.y;
				z += vector.z;
				count++;
			}

			if(count > 0) {
				par2World.playSoundAtEntity(par3EntityPlayer, "random.levelup", 0.5F, 0.25F);
				ManaItemHandler.requestManaExact(par1ItemStack, par3EntityPlayer, MANA_COST, false);
				par1ItemStack.setItemDamage(TIME);
			}
		}

		return par1ItemStack;
	}

	@Override
	public void onUpdate(ItemStack par1ItemStack, World par2World, Entity par3Entity, int par4, boolean par5) {
		if(par1ItemStack.isItemDamaged())
			par1ItemStack.setItemDamage(par1ItemStack.getItemDamage() - 1);
	}

	@Override
	public boolean isFull3D() {
		return true;
	}

	@Override
	public boolean usesMana(ItemStack stack) {
		return true;
	}

}
