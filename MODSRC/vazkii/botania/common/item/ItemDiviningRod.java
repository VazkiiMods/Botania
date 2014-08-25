/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under a
 * Creative Commons Attribution-NonCommercial-ShareAlike 3.0 License
 * (http://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB)
 * 
 * File Created @ [Aug 25, 2014, 2:57:16 PM (GMT)]
 */
package vazkii.botania.common.item;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;
import vazkii.botania.api.mana.IManaUsingItem;
import vazkii.botania.api.mana.ManaItemHandler;
import vazkii.botania.common.Botania;
import vazkii.botania.common.lib.LibItemNames;

public class ItemDiviningRod extends ItemMod implements IManaUsingItem {

	static final int COST = 3000;

	public ItemDiviningRod() {
		setMaxStackSize(1);
		setUnlocalizedName(LibItemNames.DIVINING_ROD);
	}

	@Override
	public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer p) {
		if(ManaItemHandler.requestManaExact(stack, p, COST, true) && world.isRemote) {
			int x = (int) MathHelper.floor_double(p.posX);
			int y = (int) MathHelper.floor_double(p.posY);
			int z = (int) MathHelper.floor_double(p.posZ);
			int range = 15;
			long seedxor = world.rand.nextLong();
			
			Botania.proxy.setWispFXDepthTest(false);
			for(int i = -range; i < range + 1; i++)
				for(int j = -range; j < range + 1; j++)
					for(int k = -range; k < range + 1; k++) {
						int xp = x + i;
						int yp = y + j;
						int zp = z + k;
						Block block = world.getBlock(xp, yp, zp);
						int meta = world.getBlockMetadata(xp, yp, zp);
						ItemStack orestack = new ItemStack(block, 1, meta);
						for(int id : OreDictionary.getOreIDs(orestack)) {
							String s = OreDictionary.getOreName(id);
							if(s.matches("^ore.+")) {
								Random rand = new Random(s.hashCode() ^ seedxor);
								Botania.proxy.wispFX(world, xp + world.rand.nextFloat(), yp + world.rand.nextFloat(), zp + world.rand.nextFloat(), rand.nextFloat(), rand.nextFloat(), rand.nextFloat(), 0.25F, 0F, 8);
								break;
							}
						}
					}
			Botania.proxy.setWispFXDepthTest(true);
			p.swingItem();
		}

		return stack;
	}
	
	@Override
	public boolean usesMana(ItemStack stack) {
		return true;
	}
}