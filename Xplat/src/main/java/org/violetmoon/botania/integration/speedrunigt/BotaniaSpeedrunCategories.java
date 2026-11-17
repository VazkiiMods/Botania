package vazkii.botania.integration.speedrunigt;

import java.util.ArrayList;

public class BotaniaSpeedrunCategories {
	public record BotaniaSpeedrunCategory(String id) {
		public String idWithNamespace() {
			return "botania:" + id;
		}
	}

	public static final ArrayList<BotaniaSpeedrunCategory> ALL = new ArrayList<>();

	public static final BotaniaSpeedrunCategory GAIA_I = make("gaia_i");
	public static final BotaniaSpeedrunCategory GAIA_II = make("gaia_ii");
	public static final BotaniaSpeedrunCategory OBTAIN_DREAMWOOD = make("obtain_dreamwood");
	public static final BotaniaSpeedrunCategory BLESSING = make("blessing");

	private static BotaniaSpeedrunCategory make(String id) {
		BotaniaSpeedrunCategory cat = new BotaniaSpeedrunCategory(id);
		ALL.add(cat);
		return cat;
	}
}
