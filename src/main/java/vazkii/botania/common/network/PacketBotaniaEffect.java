/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.network;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.item.DyeColor;
import net.minecraft.network.PacketBuffer;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkEvent;

import vazkii.botania.client.fx.SparkleParticleData;
import vazkii.botania.client.fx.WispParticleData;
import vazkii.botania.common.block.tile.TileTerraPlate;
import vazkii.botania.common.core.helper.Vector3;
import vazkii.botania.common.entity.EntityDoppleganger;
import vazkii.botania.common.item.ItemTwigWand;

import java.util.function.Supplier;

// Prefer using World.addBlockEvent/Block.eventReceived/TileEntity.receiveClientEvent where possible
// as those use less network bandwidth (~14 bytes), vs 26+ bytes here
public class PacketBotaniaEffect {

	private final EffectType type;
	private final double x;
	private final double y;
	private final double z;
	private final int[] args;

	public PacketBotaniaEffect(EffectType type, double x, double y, double z, int... args) {
		this.type = type;
		this.x = x;
		this.y = y;
		this.z = z;
		this.args = args;
	}

	public static PacketBotaniaEffect decode(PacketBuffer buf) {
		EffectType type = EffectType.values()[buf.readByte()];
		double x = buf.readDouble();
		double y = buf.readDouble();
		double z = buf.readDouble();
		int[] args = new int[type.argCount];

		for (int i = 0; i < args.length; i++) {
			args[i] = buf.readVarInt();
		}
		return new PacketBotaniaEffect(type, x, y, z, args);
	}

	public static void encode(PacketBotaniaEffect msg, PacketBuffer buf) {
		buf.writeByte(msg.type.ordinal());
		buf.writeDouble(msg.x);
		buf.writeDouble(msg.y);
		buf.writeDouble(msg.z);

		for (int i = 0; i < msg.type.argCount; i++) {
			buf.writeVarInt(msg.args[i]);
		}
	}

	public static class Handler {
		public static void handle(final PacketBotaniaEffect message, final Supplier<NetworkEvent.Context> ctx) {
			if (ctx.get().getDirection().getReceptionSide().isServer()) {
				ctx.get().setPacketHandled(true);
				return;
			}
			ctx.get().enqueueWork(new Runnable() {
				// Use anon - lambda causes classloading issues
				@Override
				public void run() {
					Minecraft mc = Minecraft.getInstance();
					World world = mc.world;
					switch (message.type) {
					case PAINT_LENS: {
						DyeColor placeColor = DyeColor.byId(message.args[0]);
						int hex = placeColor.getColorValue();
						int r = (hex & 0xFF0000) >> 16;
						int g = (hex & 0xFF00) >> 8;
						int b = hex & 0xFF;
						for (int i = 0; i < 10; i++) {
							BlockPos pos = new BlockPos(message.x, message.y, message.z).offset(Direction.func_239631_a_(world.rand));
							SparkleParticleData data = SparkleParticleData.sparkle(0.6F + (float) Math.random() * 0.5F, r / 255F, g / 255F, b / 255F, 5);
							world.addParticle(data, pos.getX() + (float) Math.random(), pos.getY() + (float) Math.random(), pos.getZ() + (float) Math.random(), 0, 0, 0);
						}
						break;
					}
					case ARENA_INDICATOR: {
						SparkleParticleData data = SparkleParticleData.sparkle(5F, 1, 0, 1, 120);
						for (int i = 0; i < 360; i += 8) {
							float rad = i * (float) Math.PI / 180F;
							double x = message.x + 0.5 - Math.cos(rad) * EntityDoppleganger.ARENA_RANGE;
							double y = message.y + 0.5;
							double z = message.z + 0.5 - Math.sin(rad) * EntityDoppleganger.ARENA_RANGE;
							world.addParticle(data, x, y, z, 0, 0, 0);
						}
						break;
					}
					case ITEM_SMOKE: {
						Entity item = world.getEntityByID(message.args[0]);
						if (item == null) {
							return;
						}

						int p = message.args[1];

						for (int i = 0; i < p; i++) {
							double m = 0.01;
							double d0 = item.world.rand.nextGaussian() * m;
							double d1 = item.world.rand.nextGaussian() * m;
							double d2 = item.world.rand.nextGaussian() * m;
							double d3 = 10.0D;
							item.world.addParticle(ParticleTypes.POOF,
									message.x + item.world.rand.nextFloat() * item.getWidth() * 2.0F - item.getWidth() - d0 * d3, message.y + item.world.rand.nextFloat() * item.getHeight() - d1 * d3,
									message.z + item.world.rand.nextFloat() * item.getWidth() * 2.0F - item.getWidth() - d2 * d3, d0, d1, d2);
						}
						break;
					}
					case SPARK_NET_INDICATOR: {
						Entity e1 = world.getEntityByID(message.args[0]);
						Entity e2 = world.getEntityByID(message.args[1]);

						if (e1 == null || e2 == null) {
							return;
						}

						Vector3 orig = new Vector3(e1.getPosX(), e1.getPosY() + 0.25, e1.getPosZ());
						Vector3 end = new Vector3(e2.getPosX(), e2.getPosY() + 0.25, e2.getPosZ());
						Vector3 diff = end.subtract(orig);
						Vector3 movement = diff.normalize().multiply(0.1);
						int iters = (int) (diff.mag() / movement.mag());
						float huePer = 1F / iters;
						float hueSum = (float) Math.random();

						Vector3 currentPos = orig;
						for (int i = 0; i < iters; i++) {
							float hue = i * huePer + hueSum;
							int color = MathHelper.hsvToRGB(hue, 1F, 1F);
							float r = Math.min(1F, (color >> 16 & 0xFF) / 255F + 0.4F);
							float g = Math.min(1F, (color >> 8 & 0xFF) / 255F + 0.4F);
							float b = Math.min(1F, (color & 0xFF) / 255F + 0.4F);

							SparkleParticleData data = SparkleParticleData.noClip(1, r, g, b, 12);
							world.addParticle(data, currentPos.x, currentPos.y, currentPos.z, 0, 0, 0);
							currentPos = currentPos.add(movement);
						}

						break;
					}
					case SPARK_MANA_FLOW: {
						Entity e1 = world.getEntityByID(message.args[0]);
						Entity e2 = world.getEntityByID(message.args[1]);

						if (e1 == null || e2 == null) {
							return;
						}

						double rc = 0.45;
						Vector3 thisVec = Vector3.fromEntityCenter(e1).add((Math.random() - 0.5) * rc, (Math.random() - 0.5) * rc, (Math.random() - 0.5) * rc);
						Vector3 receiverVec = Vector3.fromEntityCenter(e2).add((Math.random() - 0.5) * rc, (Math.random() - 0.5) * rc, (Math.random() - 0.5) * rc);

						Vector3 motion = receiverVec.subtract(thisVec).multiply(0.04F);
						float r = 0.4F + 0.3F * (float) Math.random();
						float g = 0.4F + 0.3F * (float) Math.random();
						float b = 0.4F + 0.3F * (float) Math.random();
						float size = 0.125F + 0.125F * (float) Math.random();

						WispParticleData data = WispParticleData.wisp(size, r, g, b).withNoClip(true);
						world.addParticle(data, thisVec.x, thisVec.y, thisVec.z, (float) motion.x, (float) motion.y, (float) motion.z);
						break;
					}
					case ENCHANTER_DESTROY: {
						for (int i = 0; i < 50; i++) {
							float red = (float) Math.random();
							float green = (float) Math.random();
							float blue = (float) Math.random();
							WispParticleData data = WispParticleData.wisp((float) Math.random() * 0.15F + 0.15F, red, green, blue);
							world.addParticle(data, message.x, message.y, message.z, (float) (Math.random() - 0.5F) * 0.25F, (float) (Math.random() - 0.5F) * 0.25F, (float) (Math.random() - 0.5F) * 0.25F);
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
							world.addParticle(data, message.x, message.y, message.z, mx, my, mz);
						}

						break;
					}
					case TERRA_PLATE: {
						TileEntity te = world.getTileEntity(new BlockPos(message.x, message.y, message.z));
						if (te instanceof TileTerraPlate) {
							int ticks = (int) (100.0 * ((double) ((TileTerraPlate) te).getCurrentMana() / (double) TileTerraPlate.MAX_MANA));

							int totalSpiritCount = 3;
							double tickIncrement = 360D / totalSpiritCount;

							int speed = 5;
							double wticks = ticks * speed - tickIncrement;

							double r = Math.sin((ticks - 100) / 10D) * 2;
							double g = Math.sin(wticks * Math.PI / 180 * 0.55);

							for (int i = 0; i < totalSpiritCount; i++) {
								double x = message.x + Math.sin(wticks * Math.PI / 180) * r + 0.5;
								double y = message.y + 0.25 + Math.abs(r) * 0.7;
								double z = message.z + Math.cos(wticks * Math.PI / 180) * r + 0.5;

								wticks += tickIncrement;
								float[] colorsfx = new float[] {
										0F, (float) ticks / (float) 100, 1F - (float) ticks / (float) 100
								};
								WispParticleData data = WispParticleData.wisp(0.85F, colorsfx[0], colorsfx[1], colorsfx[2], 0.25F);
								world.addParticle(data, x, y, z, 0, (float) (-g * 0.05), 0);
								data = WispParticleData.wisp((float) Math.random() * 0.1F + 0.1F, colorsfx[0], colorsfx[1], colorsfx[2], 0.9F);
								world.addParticle(data, x, y, z, (float) (Math.random() - 0.5) * 0.05F, (float) (Math.random() - 0.5) * 0.05F, (float) (Math.random() - 0.5) * 0.05F);

								if (ticks == 100) {
									for (int j = 0; j < 15; j++) {
										data = WispParticleData.wisp((float) Math.random() * 0.15F + 0.15F, colorsfx[0], colorsfx[1], colorsfx[2]);
										world.addParticle(data, message.x + 0.5, message.y + 0.5, message.z + 0.5, (float) (Math.random() - 0.5F) * 0.125F, (float) (Math.random() - 0.5F) * 0.125F, (float) (Math.random() - 0.5F) * 0.125F);
									}
								}
							}
						}
						break;
					}
					case FLUGEL_EFFECT: {
						Entity entity = world.getEntityByID(message.args[0]);
						if (entity != null) {
							for (int i = 0; i < 15; i++) {
								float x = (float) (entity.getPosX() + Math.random());
								float y = (float) (entity.getPosY() + Math.random());
								float z = (float) (entity.getPosZ() + Math.random());
								WispParticleData data = WispParticleData.wisp((float) Math.random(), (float) Math.random(), (float) Math.random(), (float) Math.random(), 1);
								world.addParticle(data, x, y, z, 0, -(-0.3F + (float) Math.random() * 0.2F), 0);
							}
						}
						break;
					}
					case PARTICLE_BEAM: {
						ItemTwigWand.doParticleBeam(Minecraft.getInstance().world,
								new Vector3(message.x, message.y, message.z),
								new Vector3(message.args[0] + 0.5, message.args[1] + 0.5, message.args[2] + 0.5));
						break;
					}
					case DIVA_EFFECT: {
						Entity target = Minecraft.getInstance().world.getEntityByID(message.args[0]);
						if (target == null) {
							break;
						}

						double x = target.getPosX();
						double y = target.getPosY();
						double z = target.getPosZ();

						SparkleParticleData data = SparkleParticleData.sparkle(1F, 1F, 1F, 0.25F, 3);
						for (int i = 0; i < 50; i++) {
							world.addParticle(data, x + Math.random() * target.getWidth(), y + Math.random() * target.getHeight(), z + Math.random() * target.getWidth(), 0, 0, 0);
						}
						break;
					}
					case HALO_CRAFT: {
						Entity target = Minecraft.getInstance().world.getEntityByID(message.args[0]);
						if (target != null) {
							Vector3d lookVec3 = target.getLookVec();
							Vector3 centerVector = Vector3.fromEntityCenter(target).add(lookVec3.x * 3, 1.3, lookVec3.z * 3);
							float m = 0.1F;
							for (int i = 0; i < 4; i++) {
								WispParticleData data = WispParticleData.wisp(0.2F + 0.2F * (float) Math.random(), 1F, 0F, 1F);
								target.world.addParticle(data, centerVector.x, centerVector.y, centerVector.z, ((float) Math.random() - 0.5F) * m, ((float) Math.random() - 0.5F) * m, ((float) Math.random() - 0.5F) * m);
							}
						}

						break;
					}
					}
				}
			});

			ctx.get().setPacketHandled(true);
		}
	}

	public enum EffectType {
		PAINT_LENS(1), // Arg: EnumDyeColor
		ARENA_INDICATOR(0),
		ITEM_SMOKE(2), // Arg: Entity ID, number of particles
		SPARK_NET_INDICATOR(2), // Arg: Entity ID from, Entity ID towards
		SPARK_MANA_FLOW(2), // Arg: Entity ID from, Entity ID towards
		ENCHANTER_DESTROY(0),
		BLACK_LOTUS_DISSOLVE(0),
		TERRA_PLATE(0),
		FLUGEL_EFFECT(1), // Arg: Entity ID
		PARTICLE_BEAM(3), // Args: dest xyz
		DIVA_EFFECT(1), // Arg: Entity ID
		HALO_CRAFT(1), // Arg: Entity ID
		;

		private final int argCount;

		EffectType(int argCount) {
			this.argCount = argCount;
		}
	}

}
