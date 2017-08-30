package vazkii.botania.api.lexicon;

import net.minecraft.util.text.TextFormatting;

public class KnowledgeType {

	public final String id;
	public final TextFormatting color;
	public final boolean autoUnlock;

	public KnowledgeType(String id, TextFormatting color, boolean autoUnlock) {
		this.id = id;
		this.color = color;
		this.autoUnlock = autoUnlock;
	}

	public String getUnlocalizedName() {
		return "botania.knowledge." + id;
	}
}
