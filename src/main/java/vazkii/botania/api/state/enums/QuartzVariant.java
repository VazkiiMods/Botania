package vazkii.botania.api.state.enums;

import net.minecraft.util.IStringSerializable;

import java.util.Locale;

public enum QuartzVariant implements IStringSerializable {
    NORMAL,
    CHISELED,
    PILLAR_Y,
    PILLAR_X,
    PILLAR_Z;

    @Override
    public String getName() {
        return name().toLowerCase(Locale.ROOT);
    }
}
