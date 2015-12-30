/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [Jan 14, 2014, 5:28:21 PM (GMT)]
 */
package vazkii.botania.client.core.helper;

import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.ResourceLocation;
import vazkii.botania.client.lib.LibResources;

public final class IconHelper {

	public static TextureAtlasSprite forName(TextureMap ir, String name) {
		return ir.registerSprite(new ResourceLocation(LibResources.PREFIX_MOD + name));
	}

	public static TextureAtlasSprite forName(TextureMap ir, String name, String dir) {
		return ir.registerSprite(new ResourceLocation(LibResources.PREFIX_MOD + dir + "/" + name));
	}

}