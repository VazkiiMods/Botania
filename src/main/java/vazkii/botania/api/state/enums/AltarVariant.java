package vazkii.botania.api.state.enums;

import net.minecraft.util.IStringSerializable;

import java.util.Locale;

public enum AltarVariant implements IStringSerializable {
    DEFAULT,
    FOREST,
    PLAINS,
    MOUNTAIN,
    FUNGAL,
    SWAMP,
    DESERT,
    TAIGA,
    MESA;

    @Override
    public String getName() {
        return name().toLowerCase(Locale.ROOT);
    }
}
