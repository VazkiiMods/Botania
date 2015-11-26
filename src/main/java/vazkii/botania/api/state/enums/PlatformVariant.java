package vazkii.botania.api.state.enums;

import net.minecraft.util.IStringSerializable;

import java.util.Locale;

public enum PlatformVariant implements IStringSerializable {
    ABSTRUSE,
    SPECTRAL,
    INFRANGIBLE;

    @Override
    public String getName() {
        return name().toLowerCase(Locale.ROOT);
    }
}
