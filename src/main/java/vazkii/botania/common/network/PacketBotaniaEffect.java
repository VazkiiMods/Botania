package vazkii.botania.common.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import vazkii.botania.common.Botania;
import vazkii.botania.common.core.handler.ConfigHandler;
import vazkii.botania.common.core.helper.MathHelper;
import vazkii.botania.common.core.helper.Vector3;
import vazkii.botania.common.entity.EntityDoppleganger;

import java.awt.*;

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

    public PacketBotaniaEffect(EffectType type, double x, double y, double z, double... args) {
        this(type, x, y, z, MathHelper.doubleArrayToIntArray(args));
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        type = EffectType.values()[buf.readShort()];
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
        buf.writeShort(type.ordinal());
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
                    World world = mc.theWorld;
                    switch (message.type) {
                        case POOL_CRAFT: {
                            for(int i = 0; i < 25; i++) {
                                float red = (float) Math.random();
                                float green = (float) Math.random();
                                float blue = (float) Math.random();
                                Botania.proxy.sparkleFX(world, message.x + 0.5 + Math.random() * 0.4 - 0.2, message.y + 0.75, message.z + 0.5 + Math.random() * 0.4 - 0.2,
                                        red, green, blue, (float) Math.random(), 10);
                            }
                            break;
                        }
                        case POOL_CHARGE: {
                            if(ConfigHandler.chargingAnimationEnabled) {
                                boolean outputting = message.args[0] == 1;
                                BlockPos pos = new BlockPos(message.x, message.y, message.z);
                                Vector3 itemVec = Vector3.fromBlockPos(pos).add(0.5, 0.5 + Math.random() * 0.3, 0.5);
                                Vector3 tileVec = Vector3.fromBlockPos(pos).add(0.2 + Math.random() * 0.6, 0, 0.2 + Math.random() * 0.6);
                                Botania.proxy.lightningFX(world, outputting ? tileVec : itemVec,
                                        outputting ? itemVec : tileVec, 80, world.rand.nextLong(), 0x4400799c, 0x4400C6FF);
                            }
                            break;
                        }
                        case PAINT_LENS: {
                            EnumDyeColor placeColor = EnumDyeColor.byMetadata(message.args[0]);
                            int hex = placeColor.getMapColor().colorValue;
                            int r = (hex & 0xFF0000) >> 16;
                            int g = (hex & 0xFF00) >> 8;
                            int b = (hex & 0xFF);
                            for(int i = 0; i < 10; i++) {
                                BlockPos pos = new BlockPos(message.x, message.y, message.z).offset(EnumFacing.VALUES[world.rand.nextInt(6)]);
                                Botania.proxy.sparkleFX(world,
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

                                Botania.proxy.sparkleFX(world, x, y, z, r, g, b, 5F, 120);
                            }
                            break;
                        }
                        case ITEM_SMOKE: {
                            Entity item = world.getEntityByID(message.args[0]);
                            int p = message.args[1];

                            for(int i = 0; i < p; i++) {
                                double m = 0.01;
                                double d0 = item.worldObj.rand.nextGaussian() * m;
                                double d1 = item.worldObj.rand.nextGaussian() * m;
                                double d2 = item.worldObj.rand.nextGaussian() * m;
                                double d3 = 10.0D;
                                item.worldObj.spawnParticle(EnumParticleTypes.EXPLOSION_NORMAL,
                                        message.x + item.worldObj.rand.nextFloat() * item.width * 2.0F - item.width - d0 * d3, message.y + item.worldObj.rand.nextFloat() * item.height - d1 * d3,
                                        message.z + item.worldObj.rand.nextFloat() * item.width * 2.0F - item.width - d2 * d3, d0, d1, d2);
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
                                Botania.proxy.sparkleFX(e1.worldObj, currentPos.x, currentPos.y, currentPos.z, r, g, b, 1F, 12);
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

                            Botania.proxy.wispFX(world, thisVec.x, thisVec.y, thisVec.z, r, g, b,
                                    size, (float) motion.x, (float) motion.y, (float) motion.z);
                            break;
                        }
                        case ENCHANTER_CRAFT: {
                            for(int i = 0; i < 25; i++) {
                                float red = (float) Math.random();
                                float green = (float) Math.random();
                                float blue = (float) Math.random();
                                Botania.proxy.sparkleFX(world,
                                        message.x + Math.random() * 0.4 - 0.2, message.y, message.z + Math.random() * 0.4 - 0.2,
                                        red, green, blue, (float) Math.random(), 10);
                            }
                            break;
                        }
                        case ENCHANTER_DESTROY: {
                            for(int i = 0; i < 50; i++) {
                                float red = (float) Math.random();
                                float green = (float) Math.random();
                                float blue = (float) Math.random();
                                Botania.proxy.wispFX(world, message.x, message.y, message.z,
                                        red, green, blue, (float) Math.random() * 0.15F + 0.15F, (float) (Math.random() - 0.5F) * 0.25F, (float) (Math.random() - 0.5F) * 0.25F, (float) (Math.random() - 0.5F) * 0.25F);
                            }
                            break;
                        }
                        case ENTROPINNYUM: {
                            for(int i = 0; i < 50; i++)
                                Botania.proxy.sparkleFX(world, message.x + Math.random() * 4 - 2, message.y + Math.random() * 4 - 2, message.z + Math.random() * 4 - 2, 1F, (float) Math.random() * 0.25F, (float) Math.random() * 0.25F, (float) (Math.random() * 0.65F + 1.25F), 12);

                            world.spawnParticle(EnumParticleTypes.EXPLOSION_HUGE, message.x, message.y, message.z, 1D, 0D, 0D);
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

                                Botania.proxy.wispFX(world, message.x, message.y, message.z, r, g, b, s, mx, my, mz);
                            }

                            break;
                        }
                    }
                }
            });

            return null;
        }
    }

    public enum EffectType {
        POOL_CRAFT(0),
        POOL_CHARGE(1), // Arg: 1 if outputting, 0 if inputting
        PAINT_LENS(1),  // Arg: colour
        ARENA_INDICATOR(0),
        ITEM_SMOKE(2), // Arg: Entity ID, number of particles
        SPARK_NET_INDICATOR(2), // Arg: Entity ID from, Entity ID towards
        SPARK_MANA_FLOW(2), // Arg: Entity ID from, Entity ID towards
        ENCHANTER_CRAFT(0),
        ENCHANTER_DESTROY(0),
        ENTROPINNYUM(0),
        BLACK_LOTUS_DISSOLVE(0);

        private final int argCount;

        EffectType(int argCount) {
            this.argCount = argCount;
        }
    }

}
