package vazkii.botania.api.state.enums;

import net.minecraft.util.IStringSerializable;

import java.util.Locale;

public enum DrumVariant implements IStringSerializable {
    WILD,
    GATHERING,
    CANOPY;

    @Override
    public String getName() {
        return name().toLowerCase(Locale.ROOT);
    }
}
