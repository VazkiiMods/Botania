package vazkii.botania.api.state.enums;

import net.minecraft.util.StringRepresentable;

public enum ManastarState implements StringRepresentable {
	NEUTRAL("neutral"),
	DECREASING("decreasing"),
	INCREASING("increasing");

	private final String state;

	ManastarState(String state) {
		this.state = state;
	}

	@Override
	public String getSerializedName() {
		return state;
	}
}
