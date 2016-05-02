package vazkii.botania.common.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

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
            args[i] = ByteBufUtils.readVarInt(buf, 3);
        }
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeShort(type.ordinal());
        buf.writeDouble(x);
        buf.writeDouble(y);
        buf.writeDouble(z);

        for (int i = 0; i < type.argCount; i++) {
            ByteBufUtils.writeVarInt(buf, args[i], 3);
        }
    }

    public static class Handler implements IMessageHandler<PacketBotaniaEffect, IMessage> {

        @Override
        public IMessage onMessage(PacketBotaniaEffect message, MessageContext ctx) {
            Minecraft.getMinecraft().addScheduledTask(() -> {

            });

            return null;
        }

    }

    public enum EffectType {
        ;

        private final int argCount;

        EffectType(int argCount) {
            this.argCount = argCount;
        }

    }

}
