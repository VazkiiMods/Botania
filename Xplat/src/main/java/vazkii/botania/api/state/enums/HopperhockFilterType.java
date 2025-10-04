package vazkii.botania.api.state.enums;

import net.minecraft.util.StringRepresentable;

public enum HopperhockFilterType implements StringRepresentable {
	ACCEPT_IN_FRAME("items_in_frames"),
	ACCEPT_NOT_IN_FRAME("items_not_in_frames"),
	ACCEPT_ALL("all_items");

	private final String name;

	HopperhockFilterType(String name) {
		this.name = name;
	}

	@Override
	public String getSerializedName() {
		return name;
	}
}
