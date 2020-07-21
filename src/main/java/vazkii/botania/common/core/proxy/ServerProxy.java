/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.core.proxy;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import net.minecraftforge.fml.server.ServerLifecycleHooks;

import vazkii.botania.common.core.helper.Vector3;
import vazkii.botania.common.entity.EntityDoppleganger;

public class ServerProxy implements IProxy {

	@Override
	public boolean isTheClientPlayer(LivingEntity entity) {
		return false;
	}

	@Override
	public PlayerEntity getClientPlayer() {
		return null;
	}

	@Override
	public boolean isClientPlayerWearingMonocle() {
		return false;
	}

	@Override
	public long getWorldElapsedTicks() {
		return ServerLifecycleHooks.getCurrentServer().getWorld(World.OVERWORLD).getTime();
	}

	@Override
	public void lightningFX(Vector3 vectorStart, Vector3 vectorEnd, float ticksPerMeter, long seed, int colorOuter, int colorInner) {}

	@Override
	public void addBoss(EntityDoppleganger boss) {}

	@Override
	public void removeBoss(EntityDoppleganger boss) {}

	@Override
	public int getClientRenderDistance() {
		return 0;
	}

}
