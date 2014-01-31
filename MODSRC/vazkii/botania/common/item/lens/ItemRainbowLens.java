/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under a
 * Creative Commons Attribution-NonCommercial-ShareAlike 3.0 License
 * (http://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB)
 * 
 * File Created @ [Jan 31, 2014, 4:53:05 PM (GMT)]
 */
package vazkii.botania.common.item.lens;

import java.awt.Color;

import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import vazkii.botania.api.internal.IManaBurst;
import vazkii.botania.common.lib.LibItemIDs;
import vazkii.botania.common.lib.LibItemNames;

public class ItemRainbowLens extends ItemLens {

	public ItemRainbowLens() {
		super(LibItemIDs.idRainbowLens, LibItemNames.RAINBOW_LENS);
	}

	@Override
	public int getLensColor(ItemStack stack) {
		World world = Minecraft.getMinecraft().theWorld;
		return world == null ? 0xFFFFFF : Color.HSBtoRGB((float) ((world.getTotalWorldTime() * 2) % 360) / 360F, 1F, 1F);
	}
	
	@Override
	public void updateBurst(IManaBurst burst, ItemStack stack) {
		burst.setColor(getLensColor(stack));
	}

}
