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
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import org.lwjgl.glfw.GLFW;

import vazkii.botania.client.core.handler.*;
import vazkii.botania.client.fx.BoltParticleOptions;
import vazkii.botania.client.fx.BoltRenderer;
import vazkii.botania.common.entity.EntityDoppleganger;
import vazkii.botania.common.item.*;
import vazkii.botania.common.lib.LibMisc;
import vazkii.botania.common.proxy.IProxy;
import vazkii.botania.xplat.BotaniaConfig;
import vazkii.patchouli.api.IMultiblock;
import vazkii.patchouli.api.PatchouliAPI;

import javax.annotation.Nullable;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class ClientProxy implements IProxy {

	public static boolean jingleTheBells = false;
	public static boolean dootDoot = false;

	public static KeyMapping CORPOREA_REQUEST;

	public static void initKeybindings(Consumer<KeyMapping> consumer) {
		CORPOREA_REQUEST = new KeyMapping("key.botania_corporea_request", GLFW.GLFW_KEY_C, LibMisc.MOD_NAME);
		consumer.accept(CORPOREA_REQUEST);
	}

	public static void initSeasonal() {
		if (BotaniaConfig.client().enableSeasonalFeatures()) {
			LocalDateTime now = LocalDateTime.now();
			if (now.getMonth() == Month.DECEMBER && now.getDayOfMonth() >= 16 || now.getMonth() == Month.JANUARY && now.getDayOfMonth() <= 2) {
				ClientProxy.jingleTheBells = true;
			}
			if (now.getMonth() == Month.OCTOBER) {
				ClientProxy.dootDoot = true;
			}
		}
	}

	@Override
	public void runOnClient(Supplier<Runnable> s) {
		s.get().run();
	}

	@Override
	public Player getClientPlayer() {
		return Minecraft.getInstance().player;
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
	public void addParticleForceNear(Level world, ParticleOptions particleData, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
		Camera info = Minecraft.getInstance().gameRenderer.getMainCamera();
		if (info.isInitialized() && info.getPosition().distanceToSqr(x, y, z) <= 1024.0D) {
			world.addParticle(particleData, true, x, y, z, xSpeed, ySpeed, zSpeed);
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

	@Nullable
	@Override
	public HitResult getClientHit() {
		return Minecraft.getInstance().hitResult;
	}
}
