/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.client.core.proxy;

import net.minecraft.client.Camera;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.phys.Vec3;

import vazkii.botania.client.core.handler.*;
import vazkii.botania.client.fx.BoltParticleOptions;
import vazkii.botania.client.fx.BoltRenderer;
import vazkii.botania.common.core.proxy.IProxy;
import vazkii.botania.common.entity.EntityDoppleganger;
import vazkii.botania.common.item.*;
import vazkii.botania.common.item.equipment.bauble.ItemMonocle;
import vazkii.patchouli.api.IMultiblock;
import vazkii.patchouli.api.PatchouliAPI;

public class ClientProxy implements IProxy {

	public static boolean jingleTheBells = false;
	public static boolean dootDoot = false;

	public static KeyMapping CORPOREA_REQUEST;

	@Override
	public Player getClientPlayer() {
		return Minecraft.getInstance().player;
	}

	@Override
	public boolean isClientPlayerWearingMonocle() {
		return ItemMonocle.hasMonocle(Minecraft.getInstance().player);
	}

	@Override
	public void lightningFX(Vec3 vectorStart, Vec3 vectorEnd, float ticksPerMeter, long seed, int colorOuter, int colorInner) {
		// todo wip, params are ignored
		BoltRenderer.INSTANCE.add(new BoltParticleOptions(vectorStart, vectorEnd).size(0.08F), ClientTickHandler.partialTicks);
	}

	@Override
	public void addBoss(EntityDoppleganger boss) {
		BossBarHandler.bosses.add(boss);
	}

	@Override
	public void removeBoss(EntityDoppleganger boss) {
		BossBarHandler.bosses.remove(boss);
	}

	@Override
	public int getClientRenderDistance() {
		return Minecraft.getInstance().options.renderDistance;
	}

	@Override
	public void addParticleForce(Level world, ParticleOptions particleData, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
		world.addParticle(particleData, true, x, y, z, xSpeed, ySpeed, zSpeed);
	}

	@Override
	public void addParticleForceNear(Level world, ParticleOptions particleData, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
		Camera info = Minecraft.getInstance().gameRenderer.getMainCamera();
		if (info.isInitialized() && info.getPosition().distanceToSqr(x, y, z) <= 1024.0D) {
			addParticleForce(world, particleData, x, y, z, xSpeed, ySpeed, zSpeed);
		}
	}

	@Override
	public void showMultiblock(IMultiblock mb, Component name, BlockPos anchor, Rotation rot) {
		PatchouliAPI.get().showMultiblock(mb, name, anchor, rot);
	}

	@Override
	public void clearSextantMultiblock() {
		IMultiblock mb = PatchouliAPI.get().getCurrentMultiblock();
		if (mb != null && mb.getID().equals(ItemSextant.MULTIBLOCK_ID)) {
			PatchouliAPI.get().clearMultiblock();
		}
	}
}
