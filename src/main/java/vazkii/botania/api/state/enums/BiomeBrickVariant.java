package vazkii.botania.api.state.enums;

import net.minecraft.util.IStringSerializable;

import java.util.Locale;

public enum BiomeBrickVariant implements IStringSerializable {
    FOREST,
    PLAINS,
    MOUNTAIN,
    FUNGAL,
    SWAMP,
    DESERT,
    TAIGA,
    MESA,
    CHISELED_FOREST,
    CHISELED_PLAINS,
    CHISELED_MOUNTAIN,
    CHISELED_FUNGAL,
    CHISELED_SWAMP,
    CHISELED_DESERT,
    CHISELED_TAIGA,
    CHISELED_MESA;

    @Override
    public String getName() {
        return name().toLowerCase(Locale.ROOT);
    }
}
