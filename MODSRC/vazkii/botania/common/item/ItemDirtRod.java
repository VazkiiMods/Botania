/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under a
 * Creative Commons Attribution-NonCommercial-ShareAlike 3.0 License
 * (http://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB)
 * 
 * File Created @ [Apr 11, 2014, 2:53:41 PM (GMT)]
 */
package vazkii.botania.common.item;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import vazkii.botania.api.mana.IManaUsingItem;
import vazkii.botania.api.mana.ManaItemHandler;
import vazkii.botania.common.Botania;
import vazkii.botania.common.lib.LibItemNames;

public class ItemDirtRod extends ItemMod implements IManaUsingItem {

	static final int COST = 75;

	public ItemDirtRod() {
		this(LibItemNames.DIRT_ROD);
	}

	public ItemDirtRod(String name) {
		super();
		setMaxStackSize(1);
		setUnlocalizedName(name);
	}

	@Override
	public boolean onItemUse(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, World par3World, int par4, int par5, int par6, int par7, float par8, float par9, float par10) {
		if(ManaItemHandler.requestManaExact(par1ItemStack, par2EntityPlayer, COST, false)) {
			ForgeDirection dir = ForgeDirection.getOrientation(par7);
			int entities = par3World.getEntitiesWithinAABB(EntityLivingBase.class, AxisAlignedBB.getBoundingBox(par4 + dir.offsetX, par5 + dir.offsetY, par6 + dir.offsetZ, par4 + dir.offsetX + 1, par5 + dir.offsetY + 1, par6 + dir.offsetZ + 1)).size();

			if(entities == 0) {
				ItemStack stackToPlace = new ItemStack(Blocks.dirt);
				stackToPlace.tryPlaceItemIntoWorld(par2EntityPlayer, par3World, par4, par5, par6, par7, par8, par9, par10);

				if(stackToPlace.stackSize == 0) {
					ManaItemHandler.requestManaExact(par1ItemStack, par2EntityPlayer, COST, true);
					for(int i = 0; i < 6; i++)
						Botania.proxy.sparkleFX(par3World, par4 + dir.offsetX + Math.random(), par5 + dir.offsetY + Math.random(), par6 + dir.offsetZ + Math.random(), 0.35F, 0.2F, 0.05F, 1F, 5);
				}
			}
		}
		return true;
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