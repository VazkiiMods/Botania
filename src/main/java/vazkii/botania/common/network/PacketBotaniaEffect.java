package vazkii.botania.common.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import vazkii.botania.client.core.handler.LightningHandler;
import vazkii.botania.common.Botania;
import vazkii.botania.common.core.handler.ConfigHandler;
import vazkii.botania.common.core.helper.Vector3;
import vazkii.botania.common.entity.EntityDoppleganger;

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
                    switch (message.type) {
                        case POOL_CRAFT: {
                            for(int i = 0; i < 25; i++) {
                                float red = (float) Math.random();
                                float green = (float) Math.random();
                                float blue = (float) Math.random();
                                Botania.proxy.sparkleFX(Minecraft.getMinecraft().theWorld, message.x + 0.5 + Math.random() * 0.4 - 0.2, message.y + 1, message.z + 0.5 + Math.random() * 0.4 - 0.2,
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
                                LightningHandler.spawnLightningBolt(Minecraft.getMinecraft().theWorld, outputting ? tileVec : itemVec,
                                        outputting ? itemVec : tileVec, 80, Minecraft.getMinecraft().theWorld.rand.nextLong(), 0x4400799c, 0x4400C6FF);
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
                                BlockPos pos = new BlockPos(message.x, message.y, message.z).offset(EnumFacing.VALUES[Minecraft.getMinecraft().theWorld.rand.nextInt(6)]);
                                Botania.proxy.sparkleFX(Minecraft.getMinecraft().theWorld,
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

                                Botania.proxy.sparkleFX(Minecraft.getMinecraft().theWorld, x, y, z, r, g, b, 5F, 120);
                            }
                            break;
                        }
                        case ITEM_SMOKE: {
                            Entity item = Minecraft.getMinecraft().theWorld.getEntityByID(message.args[0]);
                            int p = message.args[1];

                            for(int i = 0; i < p; i++) {
                                double m = 0.01;
                                double d0 = item.worldObj.rand.nextGaussian() * m;
                                double d1 = item.worldObj.rand.nextGaussian() * m;
                                double d2 = item.worldObj.rand.nextGaussian() * m;
                                double d3 = 10.0D;
                                item.worldObj.spawnParticle(EnumParticleTypes.EXPLOSION_NORMAL, message.x + item.worldObj.rand.nextFloat() * item.width * 2.0F - item.width - d0 * d3, message.y + item.worldObj.rand.nextFloat() * item.height - d1 * d3, message.z + item.worldObj.rand.nextFloat() * item.width * 2.0F - item.width - d2 * d3, d0, d1, d2);
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
        ITEM_SMOKE(2); // Arg: Entity ID, number of particles

        private final int argCount;

        EffectType(int argCount) {
            this.argCount = argCount;
        }
    }

}
