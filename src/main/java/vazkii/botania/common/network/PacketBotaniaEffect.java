/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.network;

import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.Vec3;

import vazkii.botania.client.fx.SparkleParticleData;
import vazkii.botania.client.fx.WispParticleData;
import vazkii.botania.common.Botania;
import vazkii.botania.common.block.tile.TileTerraPlate;
import vazkii.botania.common.core.helper.ColorHelper;
import vazkii.botania.common.core.helper.Vector3;
import vazkii.botania.common.entity.EntityDoppleganger;
import vazkii.botania.common.item.ItemTwigWand;

import static vazkii.botania.common.lib.ResourceLocationHelper.prefix;

import io.netty.buffer.Unpooled;

// Prefer using World.addBlockEvent/Block.eventReceived/TileEntity.receiveClientEvent where possible
// as those use less network bandwidth (~14 bytes), vs 26+ bytes here
public class PacketBotaniaEffect {
	public static final ResourceLocation ID = prefix("eff");

	public static void send(Player player, EffectType type, double x, double y, double z, int... args) {
		if (player instanceof ServerPlayer) {
			((ServerPlayer) player).connection.send(make(type, x, y, z, args));
		}
	}

	public static void sendNearby(Entity e, EffectType type, double x, double y, double z, int... args) {
		if (!e.level.isClientSide) {
			Packet<?> pkt = make(type, x, y, z, args);
			PlayerLookup.tracking(e).stream()
					.filter(p -> p.distanceToSqr(e.position()) < 64 * 64)
					.forEach(p -> p.connection.send(pkt));
			if (e instanceof ServerPlayer) {
				((ServerPlayer) e).connection.send(pkt);
			}
		}
	}

	public static void sendNearby(Level world, BlockPos pos, EffectType type, double x, double y, double z, int... args) {
		if (world instanceof ServerLevel) {
			Packet<?> pkt = make(type, x, y, z, args);
			PlayerLookup.tracking((ServerLevel) world, pos).stream()
					.filter(p -> p.distanceToSqr(pos.getX(), pos.getY(), pos.getZ()) < 64 * 64)
					.forEach(p -> p.connection.send(pkt));
		}
	}

	public static Packet<?> make(EffectType type, double x, double y, double z, int... args) {
		FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
		buf.writeByte(type.ordinal());
		buf.writeDouble(x);
		buf.writeDouble(y);
		buf.writeDouble(z);

		for (int i = 0; i < type.argCount; i++) {
			buf.writeVarInt(args[i]);
		}

		return ServerPlayNetworking.createS2CPacket(ID, buf);
	}

	public static class Handler {
		public static void handle(Minecraft client, ClientPacketListener handler, FriendlyByteBuf buf, PacketSender responseSender) {
			EffectType type = EffectType.values()[buf.readByte()];
			double x = buf.readDouble();
			double y = buf.readDouble();
			double z = buf.readDouble();
			int[] args = new int[type.argCount];

			for (int i = 0; i < args.length; i++) {
				args[i] = buf.readVarInt();
			}

			client.execute(new Runnable() {
				// Use anon - lambda causes classloading issues
				@Override
				public void run() {
					Minecraft mc = Minecraft.getInstance();
					Level world = mc.level;
					switch (type) {
					case PAINT_LENS: {
						DyeColor placeColor = DyeColor.byId(args[0]);
						int hex = ColorHelper.getColorValue(placeColor);
						int r = (hex & 0xFF0000) >> 16;
						int g = (hex & 0xFF00) >> 8;
						int b = hex & 0xFF;
						for (int i = 0; i < 10; i++) {
							BlockPos pos = new BlockPos(x, y, z).relative(Direction.getRandom(world.random));
							SparkleParticleData data = SparkleParticleData.sparkle(0.6F + (float) Math.random() * 0.5F, r / 255F, g / 255F, b / 255F, 5);
							world.addParticle(data, pos.getX() + (float) Math.random(), pos.getY() + (float) Math.random(), pos.getZ() + (float) Math.random(), 0, 0, 0);
						}
						break;
					}
					case ARENA_INDICATOR: {
						SparkleParticleData data = SparkleParticleData.sparkle(5F, 1, 0, 1, 120);
						for (int i = 0; i < 360; i += 8) {
							float rad = i * (float) Math.PI / 180F;
							double wx = x + 0.5 - Math.cos(rad) * EntityDoppleganger.ARENA_RANGE;
							double wy = y + 0.5;
							double wz = z + 0.5 - Math.sin(rad) * EntityDoppleganger.ARENA_RANGE;
							Botania.proxy.addParticleForceNear(world, data, wx, wy, wz, 0, 0, 0);
						}
						break;
					}
					case ITEM_SMOKE: {
						Entity item = world.getEntity(args[0]);
						if (item == null) {
							return;
						}

						int p = args[1];

						for (int i = 0; i < p; i++) {
							double m = 0.01;
							double d0 = item.level.random.nextGaussian() * m;
							double d1 = item.level.random.nextGaussian() * m;
							double d2 = item.level.random.nextGaussian() * m;
							double d3 = 10.0D;
							item.level.addParticle(ParticleTypes.POOF,
									x + item.level.random.nextFloat() * item.getBbWidth() * 2.0F - item.getBbWidth() - d0 * d3, y + item.level.random.nextFloat() * item.getBbHeight() - d1 * d3,
									z + item.level.random.nextFloat() * item.getBbWidth() * 2.0F - item.getBbWidth() - d2 * d3, d0, d1, d2);
						}
						break;
					}
					case SPARK_NET_INDICATOR: {
						Entity e1 = world.getEntity(args[0]);
						Entity e2 = world.getEntity(args[1]);

						if (e1 == null || e2 == null) {
							return;
						}

						Vec3 orig = new Vec3(e1.getX(), e1.getY() + 0.25, e1.getZ());
						Vec3 end = new Vec3(e2.getX(), e2.getY() + 0.25, e2.getZ());
						Vec3 diff = end.subtract(orig);
						Vec3 movement = diff.normalize().scale(0.1);
						int iters = (int) (diff.length() / movement.length());
						float huePer = 1F / iters;
						float hueSum = (float) Math.random();

						Vec3 currentPos = orig;
						for (int i = 0; i < iters; i++) {
							float hue = i * huePer + hueSum;
							int color = Mth.hsvToRgb(Mth.frac(hue), 1F, 1F);
							float r = Math.min(1F, (color >> 16 & 0xFF) / 255F + 0.4F);
							float g = Math.min(1F, (color >> 8 & 0xFF) / 255F + 0.4F);
							float b = Math.min(1F, (color & 0xFF) / 255F + 0.4F);

							SparkleParticleData data = SparkleParticleData.noClip(1, r, g, b, 12);
							world.addAlwaysVisibleParticle(data, true, currentPos.x, currentPos.y, currentPos.z, 0, 0, 0);
							currentPos = currentPos.add(movement);
						}

						break;
					}
					case SPARK_MANA_FLOW: {
						Entity e1 = world.getEntity(args[0]);
						Entity e2 = world.getEntity(args[1]);

						if (e1 == null || e2 == null) {
							return;
						}

						double rc = 0.45;
						Vec3 thisVec = Vector3.fromEntityCenter(e1).add((Math.random() - 0.5) * rc, (Math.random() - 0.5) * rc, (Math.random() - 0.5) * rc);
						Vec3 receiverVec = Vector3.fromEntityCenter(e2).add((Math.random() - 0.5) * rc, (Math.random() - 0.5) * rc, (Math.random() - 0.5) * rc);

						Vec3 motion = receiverVec.subtract(thisVec).scale(0.04F);
						int color = args[2];
						float r = ((color >> 16) & 0xFF) / 255.0F;
						float g = ((color >> 8) & 0xFF) / 255.0F;
						float b = (color & 0xFF) / 255.0F;
						if (world.random.nextFloat() < 0.25) {
							r += 0.2F * (float) world.random.nextGaussian();
							g += 0.2F * (float) world.random.nextGaussian();
							b += 0.2F * (float) world.random.nextGaussian();
						}
						float size = 0.125F + 0.125F * (float) Math.random();

						WispParticleData data = WispParticleData.wisp(size, r, g, b).withNoClip(true);
						world.addAlwaysVisibleParticle(data, thisVec.x, thisVec.y, thisVec.z, motion.x, motion.y, motion.z);
						break;
					}
					case ENCHANTER_DESTROY: {
						for (int i = 0; i < 50; i++) {
							float red = (float) Math.random();
							float green = (float) Math.random();
							float blue = (float) Math.random();
							WispParticleData data = WispParticleData.wisp((float) Math.random() * 0.15F + 0.15F, red, green, blue);
							world.addParticle(data, x, y, z, (float) (Math.random() - 0.5F) * 0.25F, (float) (Math.random() - 0.5F) * 0.25F, (float) (Math.random() - 0.5F) * 0.25F);
						}
						break;
					}
					case BLACK_LOTUS_DISSOLVE: {
						for (int i = 0; i < 50; i++) {
							float r = (float) Math.random() * 0.35F;
							float g = 0F;
							float b = (float) Math.random() * 0.35F;
							float s = 0.45F * (float) Math.random() * 0.25F;

							float m = 0.045F;
							float mx = ((float) Math.random() - 0.5F) * m;
							float my = (float) Math.random() * m;
							float mz = ((float) Math.random() - 0.5F) * m;

							WispParticleData data = WispParticleData.wisp(s, r, g, b);
							world.addParticle(data, x, y, z, mx, my, mz);
						}

						break;
					}
					case TERRA_PLATE: {
						BlockEntity te = world.getBlockEntity(new BlockPos(x, y, z));
						if (te instanceof TileTerraPlate) {
							float percentage = Float.intBitsToFloat(args[0]);
							int ticks = (int) (100.0 * percentage);

							int totalSpiritCount = 3;
							double tickIncrement = 360D / totalSpiritCount;

							int speed = 5;
							double wticks = ticks * speed - tickIncrement;

							double r = Math.sin((ticks - 100) / 10D) * 2;
							double g = Math.sin(wticks * Math.PI / 180 * 0.55);

							for (int i = 0; i < totalSpiritCount; i++) {
								double wx = x + Math.sin(wticks * Math.PI / 180) * r + 0.5;
								double wy = y + 0.25 + Math.abs(r) * 0.7;
								double wz = z + Math.cos(wticks * Math.PI / 180) * r + 0.5;

								wticks += tickIncrement;
								float[] colorsfx = new float[] {
										0F, (float) ticks / (float) 100, 1F - (float) ticks / (float) 100
								};
								WispParticleData data = WispParticleData.wisp(0.85F, colorsfx[0], colorsfx[1], colorsfx[2], 0.25F);
								Botania.proxy.addParticleForceNear(world, data, wx, wy, wz, 0, (float) (-g * 0.05), 0);
								data = WispParticleData.wisp((float) Math.random() * 0.1F + 0.1F, colorsfx[0], colorsfx[1], colorsfx[2], 0.9F);
								world.addParticle(data, wx, wy, wz, (float) (Math.random() - 0.5) * 0.05F, (float) (Math.random() - 0.5) * 0.05F, (float) (Math.random() - 0.5) * 0.05F);

								if (ticks == 100) {
									for (int j = 0; j < 15; j++) {
										data = WispParticleData.wisp((float) Math.random() * 0.15F + 0.15F, colorsfx[0], colorsfx[1], colorsfx[2]);
										world.addParticle(data, x + 0.5, y + 0.5, z + 0.5, (float) (Math.random() - 0.5F) * 0.125F, (float) (Math.random() - 0.5F) * 0.125F, (float) (Math.random() - 0.5F) * 0.125F);
									}
								}
							}
						}
						break;
					}
					case FLUGEL_EFFECT: {
						Entity entity = world.getEntity(args[0]);
						if (entity != null) {
							for (int i = 0; i < 15; i++) {
								float x = (float) (entity.getX() + Math.random());
								float y = (float) (entity.getY() + Math.random());
								float z = (float) (entity.getZ() + Math.random());
								WispParticleData data = WispParticleData.wisp((float) Math.random(), (float) Math.random(), (float) Math.random(), (float) Math.random(), 1);
								world.addParticle(data, x, y, z, 0, -(-0.3F + (float) Math.random() * 0.2F), 0);
							}
						}
						break;
					}
					case PARTICLE_BEAM: {
						ItemTwigWand.doParticleBeam(Minecraft.getInstance().level,
								new Vec3(x, y, z),
								new Vec3(args[0] + 0.5, args[1] + 0.5, args[2] + 0.5));
						break;
					}
					case DIVA_EFFECT: {
						Entity target = Minecraft.getInstance().level.getEntity(args[0]);
						if (target == null) {
							break;
						}

						double x = target.getX();
						double y = target.getY();
						double z = target.getZ();

						SparkleParticleData data = SparkleParticleData.sparkle(1F, 1F, 1F, 0.25F, 3);
						for (int i = 0; i < 50; i++) {
							world.addParticle(data, x + Math.random() * target.getBbWidth(), y + Math.random() * target.getBbHeight(), z + Math.random() * target.getBbWidth(), 0, 0, 0);
						}
						break;
					}
					case HALO_CRAFT: {
						Entity target = Minecraft.getInstance().level.getEntity(args[0]);
						if (target != null) {
							Vec3 lookVec3 = target.getLookAngle();
							Vec3 centerVector = Vector3.fromEntityCenter(target).add(lookVec3.x * 3, 1.3, lookVec3.z * 3);
							float m = 0.1F;
							for (int i = 0; i < 4; i++) {
								WispParticleData data = WispParticleData.wisp(0.2F + 0.2F * (float) Math.random(), 1F, 0F, 1F);
								target.level.addParticle(data, centerVector.x, centerVector.y, centerVector.z, ((float) Math.random() - 0.5F) * m, ((float) Math.random() - 0.5F) * m, ((float) Math.random() - 0.5F) * m);
							}
						}

						break;
					}
					case AVATAR_TORNADO_JUMP: {
						Entity p = world.getEntity(args[0]);
						if (p != null) {
							for (int i = 0; i < 20; i++) {
								for (int j = 0; j < 5; j++) {
									WispParticleData data = WispParticleData.wisp(0.35F + (float) Math.random() * 0.1F, 0.25F, 0.25F, 0.25F);
									world.addParticle(data, p.getX(),
											p.getY() + i, p.getZ(),
											0.2F * (float) (Math.random() - 0.5),
											-0.01F * (float) Math.random(),
											0.2F * (float) (Math.random() - 0.5));
								}
							}
						}
						break;
					}
					case AVATAR_TORNADO_BOOST: {
						Entity p = world.getEntity(args[0]);
						if (p != null) {
							Vec3 lookDir = p.getLookAngle();
							for (int i = 0; i < 20; i++) {
								for (int j = 0; j < 5; j++) {
									WispParticleData data = WispParticleData.wisp(0.35F + (float) Math.random() * 0.1F, 0.25F, 0.25F, 0.25F);
									world.addParticle(data, p.getX() + lookDir.x() * i,
											p.getY() + lookDir.y() * i,
											p.getZ() + lookDir.z() * i,
											0.2F * (float) (Math.random() - 0.5) * (Math.abs(lookDir.y()) + Math.abs(lookDir.z())) + -0.01F * (float) Math.random() * lookDir.x(),
											0.2F * (float) (Math.random() - 0.5) * (Math.abs(lookDir.x()) + Math.abs(lookDir.z())) + -0.01F * (float) Math.random() * lookDir.y(),
											0.2F * (float) (Math.random() - 0.5) * (Math.abs(lookDir.y()) + Math.abs(lookDir.x())) + -0.01F * (float) Math.random() * lookDir.z());
								}
							}
						}
					}
					}
				}
			});
		}
	}

	public enum EffectType {
		PAINT_LENS(1), // Arg: EnumDyeColor
		ARENA_INDICATOR(0),
		ITEM_SMOKE(2), // Arg: Entity ID, number of particles
		SPARK_NET_INDICATOR(2), // Arg: Entity ID from, Entity ID towards
		SPARK_MANA_FLOW(3), // Arg: Entity ID from, Entity ID towards, color
		ENCHANTER_DESTROY(0),
		BLACK_LOTUS_DISSOLVE(0),
		TERRA_PLATE(1), // Arg: Completion proportion (transmuted from float)
		FLUGEL_EFFECT(1), // Arg: Entity ID
		PARTICLE_BEAM(3), // Args: dest xyz
		DIVA_EFFECT(1), // Arg: Entity ID
		HALO_CRAFT(1), // Arg: Entity ID
		AVATAR_TORNADO_JUMP(1), // Arg: Entity ID
		AVATAR_TORNADO_BOOST(1), // Arg: Entity ID
		;

		private final int argCount;

		EffectType(int argCount) {
			this.argCount = argCount;
		}
	}

}
