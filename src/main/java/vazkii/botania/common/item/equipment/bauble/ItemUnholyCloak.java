/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under a
 * Creative Commons Attribution-NonCommercial-ShareAlike 3.0 License
 * (http://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB)
 * 
 * File Created @ [Dec 4, 2014, 11:12:51 PM (GMT)]
 */
package vazkii.botania.common.item.equipment.bauble;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.util.ResourceLocation;
import vazkii.botania.client.lib.LibResources;
import vazkii.botania.common.lib.LibItemNames;

public class ItemUnholyCloak extends ItemHolyCloak {

	private static final ResourceLocation texture = new ResourceLocation(LibResources.MODEL_UNHOLY_CLOAK);

	public ItemUnholyCloak() {
		super(LibItemNames.UNHOLY_CLOAK);
	}
	
	@Override
	ResourceLocation getRenderTexture() {
		return texture;
	}
	
}
