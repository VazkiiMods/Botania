/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under a
 * Creative Commons Attribution-NonCommercial-ShareAlike 3.0 License
 * (http://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB)
 * 
 * File Created @ [Jan 13, 2014, 7:46:05 PM (GMT)]
 */
package vazkii.botania.client.core.proxy;

import net.minecraft.client.Minecraft;
import net.minecraft.world.World;
import vazkii.botania.client.fx.FXSparkle;
import vazkii.botania.common.core.proxy.CommonProxy;

public class ClientProxy extends CommonProxy {

	@Override
	public void sparkleFX(World world, double x, double y, double z, float r, float g, float b, float size, int m) {
		FXSparkle sparkle = new FXSparkle(world, x, y, z, size, r, g, b, m);
		Minecraft.getMinecraft().effectRenderer.addEffect(sparkle);
	}
}
