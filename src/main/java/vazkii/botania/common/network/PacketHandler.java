package vazkii.botania.common.network;

import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.relauncher.Side;
import vazkii.botania.common.lib.LibMisc;

public class PacketHandler {

    private PacketHandler() {}

    public static final SimpleNetworkWrapper INSTANCE =
            NetworkRegistry.INSTANCE.newSimpleChannel(LibMisc.MOD_ID.toLowerCase());

    public static void initPackets() {
        INSTANCE.registerMessage(PacketLokiToggle.class, PacketLokiToggle.class, 0, Side.SERVER);
        INSTANCE.registerMessage(PacketLokiToggleAck.class, PacketLokiToggleAck.class, 1, Side.CLIENT);
    }
}
