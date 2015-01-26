/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [Mar 29, 2014, 10:41:52 PM (GMT)]
 */
package vazkii.botania.common.item;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.World;
import vazkii.botania.common.Botania;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.lib.LibItemNames;

public class ItemFertilizer extends ItemMod {

	public ItemFertilizer() {
		super();
		setUnlocalizedName(LibItemNames.FERTILIZER);
	}

	@Override
	public boolean onItemUse(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, World par3World, int par4, int par5, int par6, int par7, float par8, float par9, float par10) {
		final int range = 3;
		if(!par3World.isRemote) {
			List<ChunkCoordinates> validCoords = new ArrayList();

			for(int i = -range - 1; i < range; i++)
				for(int j = -range - 1; j < range; j++) {
					for(int k = 2; k >= -2; k--) {
						int x = par4 + i + 1;
						int y = par5 + k + 1;
						int z = par6 + j + 1;
						if(par3World.isAirBlock(x, y, z) && (!par3World.provider.hasNoSky || y < 255) && ModBlocks.flower.canBlockStay(par3World, x, y, z))
							validCoords.add(new ChunkCoordinates(x, y, z));
					}
				}

			int flowerCount = Math.min(validCoords.size(), par3World.rand.nextBoolean() ? 3 : 4);
			for(int i = 0; i < flowerCount; i++) {
				ChunkCoordinates coords = validCoords.get(par3World.rand.nextInt(validCoords.size()));
				validCoords.remove(coords);
				par3World.setBlock(coords.posX, coords.posY, coords.posZ, ModBlocks.flower, par3World.rand.nextInt(16), 1 | 2);
			}
			par1ItemStack.stackSize--;
		} else {
			for(int i = 0; i < 15; i++) {
				double x = par4 - range + par3World.rand.nextInt(range * 2 + 1) + Math.random();
				double y = par5 + 1;
				double z = par6 - range + par3World.rand.nextInt(range * 2 + 1) + Math.random();
				float red = (float) Math.random();
				float green = (float) Math.random();
				float blue = (float) Math.random();
				Botania.proxy.wispFX(par3World, x, y, z, red, green, blue, 0.15F + (float) Math.random() * 0.25F, -(float) Math.random() * 0.1F - 0.05F);
			}
		}

		return true;
	}
}
