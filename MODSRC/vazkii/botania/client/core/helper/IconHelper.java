/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under a
 * Creative Commons Attribution-NonCommercial-ShareAlike 3.0 License
 * (http://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB)
 * 
 * File Created @ [Jan 14, 2014, 5:28:21 PM (GMT)]
 */
package vazkii.botania.client.core.helper;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.item.Item;
import net.minecraft.util.Icon;
import vazkii.botania.client.lib.LibResources;

public final class IconHelper {

	private static Icon emptyTexture;

	public static Icon forName(IconRegister ir, String name) {
		return ir.registerIcon(LibResources.PREFIX_MOD + name);
	}

	public static Icon forNameRaw(IconRegister ir, String name) {
		return ir.registerIcon(name);
	}

	public static Icon emptyTexture(IconRegister ir) {
		return emptyTexture == null ? (emptyTexture = forName(ir, LibResources.EMTPY_TEXTURE)) : emptyTexture;
	}

	public static Icon forBlock(IconRegister ir, Block block) {
		return forNameRaw(ir, block.getUnlocalizedName().replaceAll("tile.", ""));
	}

	public static Icon forBlock(IconRegister ir, Block block, int i) {
		return forBlock(ir, block, Integer.toString(i));
	}

	public static Icon forBlock(IconRegister ir, Block block, String s) {
		return forNameRaw(ir, block.getUnlocalizedName().replaceAll("tile.", "") + s);
	}

	public static Icon forItem(IconRegister ir, Item item) {
		return forNameRaw(ir, item.getUnlocalizedName().replaceAll("item.", ""));
	}

	public static Icon forItem(IconRegister ir, Item item, int i) {
		return forItem(ir, item, Integer.toString(i));
	}

	public static Icon forItem(IconRegister ir, Item item, String s) {
		return forNameRaw(ir, item.getUnlocalizedName().replaceAll("item.", "") + s);
	}

}