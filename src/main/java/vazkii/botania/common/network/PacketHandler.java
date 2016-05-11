package vazkii.botania.common.network;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.management.PlayerManager;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;
import vazkii.botania.common.lib.LibMisc;

public final class PacketHandler {

    private static final SimpleNetworkWrapper HANDLER = new SimpleNetworkWrapper(LibMisc.MOD_ID);

    public static void init() {
        HANDLER.registerMessage(PacketBotaniaEffect.Handler.class, PacketBotaniaEffect.class, 0, Side.CLIENT);
    }

    /**
     * Send message to all within 64 blocks that have this chunk loaded
     */
    public static void sendToNearby(World world, BlockPos pos, IMessage toSend) {
        if(world instanceof WorldServer) {
            WorldServer ws = ((WorldServer) world);
            PlayerManager.PlayerInstance chunk = ws.getPlayerChunkMap().getEntry(pos.getX() >> 4, pos.getZ() >> 4);

            if(chunk == null)
                return;

            for (EntityPlayer player : ws.playerEntities) {
                EntityPlayerMP playerMP = ((EntityPlayerMP) player);

                if (playerMP.getDistanceSq(pos) < 64 * 64
                        && chunk.containsPlayer(playerMP)) {
                    HANDLER.sendTo(toSend, playerMP);
                }
            }

        }
    }

    public static void sendTo(EntityPlayerMP playerMP, IMessage toSend) {
        HANDLER.sendTo(toSend, playerMP);
    }

    private PacketHandler() {}

}
