/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.network.clientbound;

import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.util.FastColor;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.Vec3;

import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.client.fx.SparkleParticleData;
import vazkii.botania.client.fx.WispParticleData;
import vazkii.botania.common.block.block_entity.TerrestrialAgglomerationPlateBlockEntity;
import vazkii.botania.common.entity.GaiaGuardianEntity;
import vazkii.botania.common.helper.VecHelper;
import vazkii.botania.common.item.GrassSeedsItem;
import vazkii.botania.common.item.WandOfTheForestItem;
import vazkii.botania.common.proxy.Proxy;
import vazkii.botania.network.EffectType;

import java.util.Objects;

import static vazkii.botania.api.BotaniaAPI.botaniaRL;

import io.netty.buffer.ByteBuf;

// Prefer using World.addBlockEvent/Block.eventReceived/TileEntity.receiveClientEvent where possible
// as those use less network bandwidth (~14 bytes), vs 26+ bytes here
public record BotaniaEffectPacket(EffectType effectType, double x, double y, double z, IntList args) implements CustomPacketPayload {

	public static final Type<BotaniaEffectPacket> ID = new Type<>(botaniaRL("eff"));
	// highest count in use currently is Thundercaller effect with up to 10 arguments
	private static final int MAX_VARIABLE_ARGS = 16;
	public static final StreamCodec<ByteBuf, BotaniaEffectPacket> STREAM_CODEC = StreamCodec.composite(
			EffectType.STREAM_CODEC, BotaniaEffectPacket::effectType,
			ByteBufCodecs.DOUBLE, BotaniaEffectPacket::x,
			ByteBufCodecs.DOUBLE, BotaniaEffectPacket::y,
			ByteBufCodecs.DOUBLE, BotaniaEffectPacket::z,
			ByteBufCodecs.VAR_INT.apply(ByteBufCodecs.list(MAX_VARIABLE_ARGS)), BotaniaEffectPacket::args,
			(type, x, y, z, args) -> new BotaniaEffectPacket(type, x, y, z, new IntArrayList(args))
	);

	public BotaniaEffectPacket(EffectType effectType, double x, double y, double z) {
		this(effectType, x, y, z, IntList.of());
	}

	public BotaniaEffectPacket(EffectType effectType, double x, double y, double z, int argument) {
		this(effectType, x, y, z, IntList.of(argument));
	}

	public BotaniaEffectPacket(EffectType effectType, double x, double y, double z, int arg1, int arg2) {
		this(effectType, x, y, z, IntList.of(arg1, arg2));
	}

	@Override
	public Type<BotaniaEffectPacket> type() {
		return ID;
	}

	public static class Handler {
		public static void handle(BotaniaEffectPacket packet) {
			var type = packet.effectType();
			var x = packet.x();
			var y = packet.y();
			var z = packet.z();
			var args = packet.args();
			if (type.argCount != -1 && args.size() != type.argCount) {
				BotaniaAPI.LOGGER.debug("Invalid argument count for effect packet type {}: {} (expected {})",
						type, args.size(), type.argCount);
				return;
			}
			// Using Lambda would somehow attempt to load ClientLevel on Neoforge dedicated server:
			//noinspection Convert2Lambda
			Minecraft.getInstance().execute(new Runnable() {
				@Override
				public void run() {
					ClientLevel world = Objects.requireNonNull(Minecraft.getInstance().level);
					switch (type) {
						case PAINT_LENS -> handlePaintLens(world, x, y, z, args);
						case ARENA_INDICATOR -> handleArenaIndicator(world, x, y, z);
						case ITEM_SMOKE -> handleItemSmoke(world, x, y, z, args);
						case SPARK_NET_INDICATOR -> handleSparkNetIndicator(world, args);
						case SPARK_MANA_FLOW -> handleSparkManaFlow(world, args);
						case ENCHANTER_DESTROY -> handleEnchanterDestroy(world, x, y, z);
						case BLACK_LOTUS_DISSOLVE -> handleBlackLotusDissolve(world, x, y, z);
						case TERRA_PLATE -> handleTerraPlateEffect(world, x, y, z, args);
						case FLUGEL_EFFECT -> handleFlugelEffect(world, args);
						case PARTICLE_BEAM -> handleParticleBeam(world, x, y, z, args);
						case DIVA_EFFECT -> handleDivaEffect(world, args);
						case HALO_CRAFT -> handleHaloCraft(world, args);
						case AVATAR_TORNADO_JUMP -> handleAvatarTornadoJump(world, args);
						case AVATAR_TORNADO_BOOST -> handleAvatarTornadoBoost(world, args);
						case THUNDERCALLER_EFFECT -> handleThundercallerEffect(world, x, y, z, args);
						case GRASS_SEED_PARTICLES -> handleGrassSeedParticles(world, x, y, z, args);
					}
				}
			});
		}

		private static void handlePaintLens(Level world, double x, double y, double z, IntList args) {
			DyeColor placeColor = DyeColor.byId(args.getInt(0));
			int color = placeColor.getTextureDiffuseColor();
			float r = FastColor.ARGB32.red(color) / 255f;
			float g = FastColor.ARGB32.green(color) / 255f;
			float b = FastColor.ARGB32.blue(color) / 255f;
			for (int i = 0; i < 10; i++) {
				Vec3 pos = Vec3.atLowerCornerOf(BlockPos.containing(x, y, z).relative(Direction.getRandom(world.random)));
				SparkleParticleData data = SparkleParticleData.sparkle(0.6f + (float) Math.random() * 0.5f, r, g, b, 5);
				world.addParticle(data, pos.x() + Math.random(), pos.y() + Math.random(), pos.z() + Math.random(), 0, 0, 0);
			}
		}

		private static void handleArenaIndicator(Level world, double x, double y, double z) {
			SparkleParticleData data = SparkleParticleData.sparkle(5F, 1, 0, 1, 120);
			for (int i = 0; i < 360; i += 8) {
				double rad = i * (Math.PI / 180);
				double wx = x + 0.5 - Math.cos(rad) * GaiaGuardianEntity.ARENA_RANGE;
				double wy = y + 0.5;
				double wz = z + 0.5 - Math.sin(rad) * GaiaGuardianEntity.ARENA_RANGE;
				Proxy.INSTANCE.addParticleForceNear(world, data, wx, wy, wz, 0, 0, 0);
			}
		}

		private static void handleItemSmoke(Level world, double x, double y, double z, IntList args) {
			RandomSource rng = world.getRandom();
			Entity item = world.getEntity(args.getInt(0));
			if (item == null) {
				return;
			}

			int p = args.getInt(1);

			for (int i = 0; i < p; i++) {
				double d0 = rng.nextGaussian() * 0.01;
				double d1 = rng.nextGaussian() * 0.01;
				double d2 = rng.nextGaussian() * 0.01;
				double d3 = 10.0;
				item.level().addParticle(ParticleTypes.POOF,
						x + Math.random() * item.getBbWidth() * 2 - item.getBbWidth() - d0 * d3,
						y + Math.random() * item.getBbHeight() - d1 * d3,
						z + Math.random() * item.getBbWidth() * 2 - item.getBbWidth() - d2 * d3,
						d0, d1, d2);
			}
		}

		private static void handleSparkNetIndicator(Level world, IntList args) {
			Entity e1 = world.getEntity(args.getInt(0));
			Entity e2 = world.getEntity(args.getInt(1));

			if (e1 == null || e2 == null) {
				return;
			}

			Vec3 orig = new Vec3(e1.getX(), e1.getY() + 0.25, e1.getZ());
			Vec3 end = new Vec3(e2.getX(), e2.getY() + 0.25, e2.getZ());
			Vec3 diff = end.subtract(orig);
			Vec3 movement = diff.normalize().scale(0.1);
			int iters = (int) (diff.length() / movement.length());
			float huePer = 1f / iters;
			float hueSum = (float) Math.random();

			Vec3 currentPos = orig;
			for (int i = 0; i < iters; i++) {
				float hue = i * huePer + hueSum;
				int color = Mth.hsvToRgb(Mth.frac(hue), 1, 1);
				float r = Math.min(1, FastColor.ARGB32.red(color) / 255f + 0.4f);
				float g = Math.min(1, FastColor.ARGB32.green(color) / 255f + 0.4f);
				float b = Math.min(1, FastColor.ARGB32.blue(color) / 255f + 0.4f);

				SparkleParticleData data = SparkleParticleData.noClip(1, r, g, b, 12);
				world.addAlwaysVisibleParticle(data, true, currentPos.x, currentPos.y, currentPos.z, 0, 0, 0);
				currentPos = currentPos.add(movement);
			}
		}

		private static void handleSparkManaFlow(Level world, IntList args) {
			RandomSource rng = world.getRandom();
			Entity e1 = world.getEntity(args.getInt(0));
			Entity e2 = world.getEntity(args.getInt(1));

			if (e1 == null || e2 == null) {
				return;
			}

			double rc = 0.45;
			Vec3 thisVec = VecHelper.fromEntityCenter(e1).add((Math.random() - 0.5) * rc, (Math.random() - 0.5) * rc, (Math.random() - 0.5) * rc);
			Vec3 receiverVec = VecHelper.fromEntityCenter(e2).add((Math.random() - 0.5) * rc, (Math.random() - 0.5) * rc, (Math.random() - 0.5) * rc);

			Vec3 motion = receiverVec.subtract(thisVec).scale(0.04);
			int color = args.getInt(2);
			float r = FastColor.ARGB32.red(color) / 255f;
			float g = FastColor.ARGB32.green(color) / 255f;
			float b = FastColor.ARGB32.blue(color) / 255f;
			if (Math.random() < 0.25) {
				r += 0.2f * (float) rng.nextGaussian();
				g += 0.2f * (float) rng.nextGaussian();
				b += 0.2f * (float) rng.nextGaussian();
			}
			float size = 0.125f + 0.125f * (float) Math.random();

			WispParticleData data = WispParticleData.wisp(size, r, g, b).withNoClip(true);
			world.addAlwaysVisibleParticle(data, thisVec.x, thisVec.y, thisVec.z, motion.x, motion.y, motion.z);
		}

		private static void handleEnchanterDestroy(Level world, double x, double y, double z) {
			for (int i = 0; i < 50; i++) {
				float red = (float) Math.random();
				float green = (float) Math.random();
				float blue = (float) Math.random();
				WispParticleData data = WispParticleData.wisp((float) Math.random() * 0.15f + 0.15f, red, green, blue);
				world.addParticle(data, x, y, z,
						(Math.random() - 0.5) * 0.25,
						(Math.random() - 0.5) * 0.25,
						(Math.random() - 0.5) * 0.25);
			}
		}

		private static void handleBlackLotusDissolve(Level world, double x, double y, double z) {
			for (int i = 0; i < 50; i++) {
				float r = (float) Math.random() * 0.35f;
				float g = 0;
				float b = (float) Math.random() * 0.35f;
				float s = (float) Math.random() * 0.1125f;

				double mx = (Math.random() - 0.5) * 0.045;
				double my = Math.random() * 0.045;
				double mz = (Math.random() - 0.5) * 0.045;

				WispParticleData data = WispParticleData.wisp(s, r, g, b);
				world.addParticle(data, x, y, z, mx, my, mz);
			}
		}

		private static void handleTerraPlateEffect(Level world, double x, double y, double z, IntList args) {
			BlockEntity te = world.getBlockEntity(BlockPos.containing(x, y, z));
			if (te instanceof TerrestrialAgglomerationPlateBlockEntity) {
				float percentage = Float.intBitsToFloat(args.getInt(0));
				int ticks = (int) (100.0f * percentage);

				int totalSpiritCount = 3;
				double tickIncrement = 360.0 / totalSpiritCount;

				int speed = 5;
				double wticks = ticks * speed - tickIncrement;

				double radius = Math.sin((ticks - 100) / 10.0) * 2;
				double vY = Math.sin(wticks * Math.PI / 180 * 0.55) * -0.05;

				float r = 0F;
				float g = ticks / 100f;
				float b = 1f - ticks / 100f;

				for (int i = 0; i < totalSpiritCount; i++) {
					double angle = wticks * (Math.PI / 180);
					double wx = x + Math.sin(angle) * radius + 0.5;
					double wy = y + 0.25 + Math.abs(radius) * 0.7;
					double wz = z + Math.cos(angle) * radius + 0.5;

					wticks += tickIncrement;
					WispParticleData data = WispParticleData.wisp(0.85f, r, g, b, 0.25f);
					Proxy.INSTANCE.addParticleForceNear(world, data, wx, wy, wz, 0, vY, 0);
					data = WispParticleData.wisp((float) Math.random() * 0.1f + 0.1f, r, g, b, 0.9f);
					world.addParticle(data, wx, wy, wz,
							(Math.random() - 0.5) * 0.05,
							(Math.random() - 0.5) * 0.05,
							(Math.random() - 0.5) * 0.05);

					if (ticks == 100) {
						for (int j = 0; j < 15; j++) {
							data = WispParticleData.wisp((float) Math.random() * 0.15f + 0.15f, r, g, b);
							world.addParticle(data, x + 0.5, y + 0.5, z + 0.5,
									(Math.random() - 0.5) * 0.125,
									(Math.random() - 0.5) * 0.125,
									(Math.random() - 0.5) * 0.125);
						}
					}
				}
			}
		}

		private static void handleFlugelEffect(Level world, IntList args) {
			Entity entity = world.getEntity(args.getInt(0));
			if (entity != null) {
				for (int i = 0; i < 15; i++) {
					double x1 = entity.getX() + Math.random();
					double y1 = entity.getY() + Math.random();
					double z1 = entity.getZ() + Math.random();
					WispParticleData data = WispParticleData.wisp((float) Math.random(), (float) Math.random(), (float) Math.random(), (float) Math.random(), 1);
					world.addParticle(data, x1, y1, z1, 0, 0.3 - Math.random() * 0.2, 0);
				}
			}
		}

		private static void handleParticleBeam(Level world, double x, double y, double z, IntList args) {
			WandOfTheForestItem.doParticleBeam(world,
					new Vec3(x, y, z),
					new Vec3(args.getInt(0) + 0.5, args.getInt(1) + 0.5, args.getInt(2) + 0.5));
		}

		private static void handleDivaEffect(Level world, IntList args) {
			Entity target = world.getEntity(args.getInt(0));
			if (target == null) {
				return;
			}

			double x1 = target.getX();
			double y1 = target.getY();
			double z1 = target.getZ();

			SparkleParticleData data = SparkleParticleData.sparkle(1, 1, 1, 0.25f, 3);
			for (int i = 0; i < 50; i++) {
				world.addParticle(data,
						x1 + Math.random() * target.getBbWidth(),
						y1 + Math.random() * target.getBbHeight(),
						z1 + Math.random() * target.getBbWidth(),
						0, 0, 0);
			}
		}

		private static void handleHaloCraft(Level world, IntList args) {
			Entity target = world.getEntity(args.getInt(0));
			if (target != null) {
				Vec3 lookVec3 = target.getLookAngle();
				Vec3 centerVector = VecHelper.fromEntityCenter(target).add(lookVec3.x * 3, 1.3, lookVec3.z * 3);
				for (int i = 0; i < 4; i++) {
					WispParticleData data = WispParticleData.wisp(0.2f + 0.2f * (float) Math.random(), 1, 0, 1);
					target.level().addParticle(data, centerVector.x, centerVector.y, centerVector.z,
							(Math.random() - 0.5) * 0.1,
							(Math.random() - 0.5) * 0.1,
							(Math.random() - 0.5) * 0.1);
				}
			}
		}

		private static void handleAvatarTornadoJump(Level world, IntList args) {
			Entity p = world.getEntity(args.getInt(0));
			if (p != null) {
				for (int i = 0; i < 20; i++) {
					for (int j = 0; j < 5; j++) {
						WispParticleData data = WispParticleData.wisp(0.35f + (float) Math.random() * 0.1f, 0.25f, 0.25f, 0.25f);
						world.addParticle(data, p.getX(),
								p.getY() + i, p.getZ(),
								0.2 * (Math.random() - 0.5),
								-0.01 * Math.random(),
								0.2 * (Math.random() - 0.5));
					}
				}
			}
		}

		private static void handleAvatarTornadoBoost(Level world, IntList args) {
			Entity p = world.getEntity(args.getInt(0));
			if (p != null) {
				Vec3 lookDir = p.getLookAngle();
				for (int i = 0; i < 20; i++) {
					for (int j = 0; j < 5; j++) {
						WispParticleData data = WispParticleData.wisp(0.35f + (float) Math.random() * 0.1f, 0.25f, 0.25f, 0.25f);
						world.addParticle(data, p.getX() + lookDir.x() * i,
								p.getY() + lookDir.y() * i,
								p.getZ() + lookDir.z() * i,
								0.2 * (Math.random() - 0.5) * (Math.abs(lookDir.y()) + Math.abs(lookDir.z())) + -0.01 * Math.random() * lookDir.x(),
								0.2 * (Math.random() - 0.5) * (Math.abs(lookDir.x()) + Math.abs(lookDir.z())) + -0.01 * Math.random() * lookDir.y(),
								0.2 * (Math.random() - 0.5) * (Math.abs(lookDir.y()) + Math.abs(lookDir.x())) + -0.01 * Math.random() * lookDir.z());
					}
				}
			}
		}

		private static void handleThundercallerEffect(Level world, double x, double y, double z, IntList args) {
			Vec3 source = new Vec3(x, y, z);
			for (int id : args) {
				var entity = world.getEntity(id);
				if (entity == null) {
					break;
				}
				var entityPos = VecHelper.fromEntityCenter(entity);
				Proxy.INSTANCE.lightningFX(world, source, entityPos, 1, 0x0179C4, 0xAADFFF);
				source = entityPos;
			}
		}

		private static void handleGrassSeedParticles(Level world, double x, double y, double z, IntList args) {
			int color = args.getInt(0);
			GrassSeedsItem.spawnParticles(world, BlockPos.containing(x, y, z), color);
		}
	}

}
