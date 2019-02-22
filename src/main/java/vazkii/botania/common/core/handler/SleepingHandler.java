package vazkii.botania.common.core.handler;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerSleepInBedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import vazkii.botania.common.entity.EntityDoppleganger;
import vazkii.botania.common.lib.LibMisc;

@Mod.EventBusSubscriber(modid = LibMisc.MOD_ID)
public final class SleepingHandler {
	
	private SleepingHandler() {}
	
	@SubscribeEvent
	public static void trySleep(PlayerSleepInBedEvent event) {
		World world = event.getEntityPlayer().world;
		for(EntityDoppleganger guardian : world.getEntities(EntityDoppleganger.class, e -> true)) {
			if(guardian.getPlayersAround().contains(event.getEntityPlayer())) {
				event.setResult(EntityPlayer.SleepResult.NOT_SAFE);
			}
		}
	}
}
