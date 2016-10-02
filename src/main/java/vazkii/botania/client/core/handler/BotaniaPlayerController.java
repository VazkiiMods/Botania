/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Jul 22, 2014, 3:33:25 PM (GMT)]
 */
package vazkii.botania.client.core.handler;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import vazkii.botania.api.item.IExtendedPlayerController;

@SideOnly(Side.CLIENT)
public class BotaniaPlayerController extends PlayerControllerMP implements IExtendedPlayerController {

	private float distance = 0F;

	public BotaniaPlayerController(Minecraft mc, NetHandlerPlayClient netHandler) {
		super(mc, netHandler);
	}

	@Override
	public float getBlockReachDistance() {
		return super.getBlockReachDistance() + distance;
	}

	@Override
	public void setReachDistanceExtension(float f) {
		distance = f;
	}

	@Override
	public float getReachDistanceExtension() {
		return distance;
	}

}
