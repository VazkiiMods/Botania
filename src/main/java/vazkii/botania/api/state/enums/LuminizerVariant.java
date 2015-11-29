package vazkii.botania.api.state.enums;

import net.minecraft.util.IStringSerializable;

import java.util.Locale;

public enum LuminizerVariant implements IStringSerializable {
    DEFAULT,
    DETECTOR;

    @Override
    public String getName() {
        return name().toLowerCase(Locale.ROOT);
    }
}
