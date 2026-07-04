package vazkii.botania.fabric.integration.speedrunigt;

import com.redlimerl.speedrunigt.api.SpeedRunIGTApi;
import com.redlimerl.speedrunigt.timer.InGameTimer;
import com.redlimerl.speedrunigt.timer.category.RunCategory;

import vazkii.botania.integration.speedrunigt.BotaniaSpeedrunCategories;
import vazkii.botania.integration.speedrunigt.BotaniaSpeedrunCategories.BotaniaSpeedrunCategory;

import java.util.Collection;
import java.util.stream.Collectors;

public class BotaniaSpeedrunIGTPlugin implements SpeedRunIGTApi {
	@Override
	public Collection<RunCategory> registerCategories() {
		return BotaniaSpeedrunCategories.ALL.stream().map(cat -> new RunCategory(cat.idWithNamespace(), "",
				"botaniamisc.speedrun_category." + cat.id())
		).collect(Collectors.toList());
	}

	public static boolean isRunningBotaniaCategory(BotaniaSpeedrunCategory category) {
		InGameTimer timer = InGameTimer.getInstance();
		return timer.getCategory().getID().equals(category.idWithNamespace()) && timer.isPlaying() && !timer.isCompleted();
	}
}
