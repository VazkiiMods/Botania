/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under a
 * Creative Commons Attribution-NonCommercial-ShareAlike 3.0 License
 * (http://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB)
 * 
 * File Created @ [Jul 22, 2014, 3:33:25 PM (GMT)]
 */
package vazkii.botania.client.core.handler;

import vazkii.botania.api.item.IExtendedPlayerController;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.client.network.NetHandlerPlayClient;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class BotaniaPlayerController extends PlayerControllerMP implements IExtendedPlayerController {

	private float distance = 0F;

	public BotaniaPlayerController(Minecraft p_i45062_1_, NetHandlerPlayClient p_i45062_2_) {
		super(p_i45062_1_, p_i45062_2_);
	}

	@Override
	public float getBlockReachDistance() {
		return super.getBlockReachDistance() + distance;
	}

	@Override
	public void setReachDistanceExtension(float f) {
		distance = f;
	}
	
}
