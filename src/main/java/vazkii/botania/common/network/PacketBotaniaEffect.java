package vazkii.botania.common.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import vazkii.botania.common.Botania;
import vazkii.botania.common.block.tile.TileTerraPlate;
import vazkii.botania.common.core.handler.ConfigHandler;
import vazkii.botania.common.core.helper.Vector3;
import vazkii.botania.common.entity.EntityDoppleganger;
import vazkii.botania.common.item.ItemTwigWand;

import java.awt.Color;

// Prefer using World.addBlockEvent/Block.eventReceived/TileEntity.receiveClientEvent where possible
// as those use less network bandwidth (~14 bytes), vs 26+ bytes here
public class PacketBotaniaEffect implements IMessage {

	private EffectType type;
	private double x;
	private double y;
	private double z;
	private int[] args;

	public PacketBotaniaEffect() {}

	public PacketBotaniaEffect(EffectType type, double x, double y, double z, int... args) {
		this.type = type;
		this.x = x;
		this.y = y;
		this.z = z;
		this.args = args;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		type = EffectType.values()[buf.readByte()];
		x = buf.readDouble();
		y = buf.readDouble();
		z = buf.readDouble();
		args = new int[type.argCount];

		for (int i = 0; i < args.length; i++) {
			args[i] = ByteBufUtils.readVarInt(buf, 5);
		}
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeByte(type.ordinal());
		buf.writeDouble(x);
		buf.writeDouble(y);
		buf.writeDouble(z);

		for (int i = 0; i < type.argCount; i++) {
			ByteBufUtils.writeVarInt(buf, args[i], 5);
		}
	}

	public static class Handler implements IMessageHandler<PacketBotaniaEffect, IMessage> {

		@Override
		public IMessage onMessage(final PacketBotaniaEffect message, final MessageContext ctx) {
			Minecraft.getMinecraft().addScheduledTask(new Runnable() {
				// Use anon - lambda causes classloading issues
				@Override
				public void run() {
					Minecraft mc = Minecraft.getMinecraft();
					World world = mc.world;
					switch (message.type) {
					case PAINT_LENS: {
						EnumDyeColor placeColor = EnumDyeColor.byMetadata(message.args[0]);
						int hex = placeColor.getColorValue();
						int r = (hex & 0xFF0000) >> 16;
						int g = (hex & 0xFF00) >> 8;
		int b = hex & 0xFF;
		for(int i = 0; i < 10; i++) {
			BlockPos pos = new BlockPos(message.x, message.y, message.z).offset(EnumFacing.VALUES[world.rand.nextInt(6)]);
			Botania.proxy.sparkleFX(
					pos.getX() + (float) Math.random(), pos.getY() + (float) Math.random(), pos.getZ() + (float) Math.random(),
					r / 255F, g / 255F, b / 255F, 0.6F + (float) Math.random() * 0.5F, 5);
		}
		break;
					}
					case ARENA_INDICATOR: {
						for(int i = 0; i < 360; i += 8) {
							float r = 1F;
							float g = 0F;
							float b = 1F;
							float rad = i * (float) Math.PI / 180F;
							double x = message.x + 0.5 - Math.cos(rad) * EntityDoppleganger.ARENA_RANGE;
							double y = message.y + 0.5;
							double z = message.z + 0.5 - Math.sin(rad) * EntityDoppleganger.ARENA_RANGE;

							Botania.proxy.sparkleFX(x, y, z, r, g, b, 5F, 120);
						}
						break;
					}
					case ITEM_SMOKE: {
						Entity item = world.getEntityByID(message.args[0]);
						if (item == null) return;
						
						int p = message.args[1];

						for(int i = 0; i < p; i++) {
							double m = 0.01;
							double d0 = item.world.rand.nextGaussian() * m;
							double d1 = item.world.rand.nextGaussian() * m;
							double d2 = item.world.rand.nextGaussian() * m;
							double d3 = 10.0D;
							item.world.spawnParticle(EnumParticleTypes.EXPLOSION_NORMAL,
									message.x + item.world.rand.nextFloat() * item.width * 2.0F - item.width - d0 * d3, message.y + item.world.rand.nextFloat() * item.height - d1 * d3,
									message.z + item.world.rand.nextFloat() * item.width * 2.0F - item.width - d2 * d3, d0, d1, d2);
						}
						break;
					}
					case SPARK_NET_INDICATOR: {
						Entity e1 = world.getEntityByID(message.args[0]);
						Entity e2 = world.getEntityByID(message.args[1]);

						if(e1 == null || e2 == null)
							return;

						Vector3 orig = new Vector3(e1.posX , e1.posY + 0.25, e1.posZ);
						Vector3 end = new Vector3(e2.posX, e2.posY + 0.25, e2.posZ);
						Vector3 diff = end.subtract(orig);
						Vector3 movement = diff.normalize().multiply(0.1);
						int iters = (int) (diff.mag() / movement.mag());
						float huePer = 1F / iters;
						float hueSum = (float) Math.random();

						Vector3 currentPos = orig;
						for(int i = 0; i < iters; i++) {
							float hue = i * huePer + hueSum;
							Color color = Color.getHSBColor(hue, 1F, 1F);
							float r = Math.min(1F, color.getRed() / 255F + 0.4F);
							float g = Math.min(1F, color.getGreen() / 255F + 0.4F);
							float b = Math.min(1F, color.getBlue() / 255F + 0.4F);

							Botania.proxy.setSparkleFXNoClip(true);
							Botania.proxy.sparkleFX(currentPos.x, currentPos.y, currentPos.z, r, g, b, 1F, 12);
							Botania.proxy.setSparkleFXNoClip(false);
							currentPos = currentPos.add(movement);
						}

						break;
					}
					case SPARK_MANA_FLOW: {
						Entity e1 = world.getEntityByID(message.args[0]);
						Entity e2 = world.getEntityByID(message.args[1]);

						if(e1 == null || e2 == null)
							return;

						double rc = 0.45;
						Vector3 thisVec = Vector3.fromEntityCenter(e1).add((Math.random() - 0.5) * rc, (Math.random() - 0.5) * rc, (Math.random() - 0.5) * rc);
						Vector3 receiverVec = Vector3.fromEntityCenter(e2).add((Math.random() - 0.5) * rc, (Math.random() - 0.5) * rc, (Math.random() - 0.5) * rc);

						Vector3 motion = receiverVec.subtract(thisVec).multiply(0.04F);
						float r = 0.4F + 0.3F * (float) Math.random();
						float g = 0.4F + 0.3F * (float) Math.random();
						float b = 0.4F + 0.3F * (float) Math.random();
						float size = 0.125F + 0.125F * (float) Math.random();

						Botania.proxy.wispFX(thisVec.x, thisVec.y, thisVec.z, r, g, b,
								size, (float) motion.x, (float) motion.y, (float) motion.z);
						break;
					}
					case ENCHANTER_DESTROY: {
						for(int i = 0; i < 50; i++) {
							float red = (float) Math.random();
							float green = (float) Math.random();
							float blue = (float) Math.random();
							Botania.proxy.wispFX(message.x, message.y, message.z,
									red, green, blue, (float) Math.random() * 0.15F + 0.15F, (float) (Math.random() - 0.5F) * 0.25F, (float) (Math.random() - 0.5F) * 0.25F, (float) (Math.random() - 0.5F) * 0.25F);
						}
						break;
					}
					case BLACK_LOTUS_DISSOLVE: {
						for(int i = 0; i < 50; i++) {
							float r = (float) Math.random() * 0.35F;
							float g = 0F;
							float b = (float) Math.random() * 0.35F;
							float s = 0.45F * (float) Math.random() * 0.25F;

							float m = 0.045F;
							float mx = ((float) Math.random() - 0.5F) * m;
							float my = (float) Math.random() * m;
							float mz = ((float) Math.random() - 0.5F) * m;

							Botania.proxy.wispFX(message.x, message.y, message.z, r, g, b, s, mx, my, mz);
						}

						break;
					}
					case TERRA_PLATE: {
						TileEntity te = world.getTileEntity(new BlockPos(message.x, message.y, message.z));
						if(te instanceof TileTerraPlate) {
							int ticks = (int) (100.0 * ((double) ((TileTerraPlate) te).getCurrentMana() / (double) TileTerraPlate.MAX_MANA));

							int totalSpiritCount = 3;
							double tickIncrement = 360D / totalSpiritCount;

							int speed = 5;
							double wticks = ticks * speed - tickIncrement;

							double r = Math.sin((ticks - 100) / 10D) * 2;
							double g = Math.sin(wticks * Math.PI / 180 * 0.55);

							for(int i = 0; i < totalSpiritCount; i++) {
								double x = message.x + Math.sin(wticks * Math.PI / 180) * r + 0.5;
								double y = message.y + 0.25 + Math.abs(r) * 0.7;
								double z = message.z + Math.cos(wticks * Math.PI / 180) * r + 0.5;

								wticks += tickIncrement;
								float[] colorsfx = new float[] {
										0F, (float) ticks / (float) 100, 1F - (float) ticks / (float) 100
								};
								Botania.proxy.wispFX(x, y, z, colorsfx[0], colorsfx[1], colorsfx[2], 0.85F, (float)g * 0.05F, 0.25F);
								Botania.proxy.wispFX(x, y, z, colorsfx[0], colorsfx[1], colorsfx[2], (float) Math.random() * 0.1F + 0.1F, (float) (Math.random() - 0.5) * 0.05F, (float) (Math.random() - 0.5) * 0.05F, (float) (Math.random() - 0.5) * 0.05F, 0.9F);

								if(ticks == 100)
									for(int j = 0; j < 15; j++)
										Botania.proxy.wispFX(message.x + 0.5, message.y + 0.5, message.z + 0.5, colorsfx[0], colorsfx[1], colorsfx[2], (float) Math.random() * 0.15F + 0.15F, (float) (Math.random() - 0.5F) * 0.125F, (float) (Math.random() - 0.5F) * 0.125F, (float) (Math.random() - 0.5F) * 0.125F);
							}
						}
						break;
					}
					case FLUGEL_EFFECT: {
						Entity entity = world.getEntityByID(message.args[0]);
						if(entity != null) {
							for(int i = 0; i < 15; i++) {
								float x = (float) (entity.posX + Math.random());
								float y = (float) (entity.posY + Math.random());
								float z = (float) (entity.posZ + Math.random());
								Botania.proxy.wispFX(x, y, z, (float) Math.random(), (float) Math.random(), (float) Math.random(), (float) Math.random(), -0.3F + (float) Math.random() * 0.2F);
							}
						}
						break;
					}
					case PARTICLE_BEAM: {
						ItemTwigWand.doParticleBeam(Minecraft.getMinecraft().world,
								new Vector3(message.x, message.y, message.z),
								new Vector3(message.args[0] + 0.5, message.args[1] + 0.5, message.args[2] + 0.5));
						break;
					}
					case DIVA_EFFECT: {
						Entity target = Minecraft.getMinecraft().world.getEntityByID(message.args[0]);
						if(target == null)
							break;

						double x = target.posX;
						double y = target.posY;
						double z = target.posZ;

						for(int i = 0; i < 50; i++)
							Botania.proxy.sparkleFX(x + Math.random() * target.width, y + Math.random() * target.height, z + Math.random() * target.width, 1F, 1F, 0.25F, 1F, 3);
						break;
					}
					}
				}
			});

			return null;
		}
	}

	public enum EffectType {
		PAINT_LENS(1),  // Arg: EnumDyeColor
		ARENA_INDICATOR(0),
		ITEM_SMOKE(2), // Arg: Entity ID, number of particles
		SPARK_NET_INDICATOR(2), // Arg: Entity ID from, Entity ID towards
		SPARK_MANA_FLOW(2), // Arg: Entity ID from, Entity ID towards
		ENCHANTER_DESTROY(0),
		BLACK_LOTUS_DISSOLVE(0),
		TERRA_PLATE(0),
		FLUGEL_EFFECT(1), // Arg: Entity ID
		PARTICLE_BEAM(3), // Args: dest xyz
		DIVA_EFFECT(1), // Arg: EntityID
		;

		private final int argCount;

		EffectType(int argCount) {
			this.argCount = argCount;
		}
	}

}
