package vazkii.botania.common.core.handler;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.ServerWorld;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerSleepInBedEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import vazkii.botania.common.entity.EntityDoppleganger;
import vazkii.botania.common.lib.LibMisc;

@Mod.EventBusSubscriber(modid = LibMisc.MOD_ID)
public final class SleepingHandler {
	
	private SleepingHandler() {}

	@SubscribeEvent
	public static void trySleep(PlayerSleepInBedEvent event) {
		World world = event.getEntityPlayer().world;
		boolean nearGuardian = ((ServerWorld) world).getEntities()
				.filter(e -> e instanceof EntityDoppleganger)
				.anyMatch(e -> ((EntityDoppleganger) e).getPlayersAround().contains(event.getEntityPlayer()));

		if(nearGuardian) {
			event.setResult(PlayerEntity.SleepResult.NOT_SAFE);
		}
	}
}
