/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under a
 * Creative Commons Attribution-NonCommercial-ShareAlike 3.0 License
 * (http://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB)
 * 
 * File Created @ [Nov 2, 2014, 10:12:45 PM (GMT)]
 */
package vazkii.botania.common.brew.potion;

import net.minecraft.client.Minecraft;
import net.minecraft.potion.Potion;
import net.minecraft.util.ResourceLocation;
import vazkii.botania.client.lib.LibResources;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class PotionMod extends Potion {

	private static final ResourceLocation resource = new ResourceLocation(LibResources.GUI_POTIONS);

	public PotionMod(String name, boolean badEffect, int color, int iconIndex) {
		super(findFreeID(), badEffect, color);
		setPotionName("botania.potion." + name);
		setIconIndex(iconIndex % 8, iconIndex / 8);
	}

	static int findFreeID() {
		for(int i = 0; i < potionTypes.length; i++)
			if(potionTypes[i] == null)
				return i;

		return -1;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public int getStatusIconIndex() {
		Minecraft.getMinecraft().renderEngine.bindTexture(resource);

		return super.getStatusIconIndex();
	}

}
