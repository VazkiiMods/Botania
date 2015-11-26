package vazkii.botania.api.state.enums;

import net.minecraft.util.IStringSerializable;

import java.util.Locale;

public enum LivingWoodVariant implements IStringSerializable {
    DEFAULT,
    PLANKS,
    MOSSY_PLANKS,
    FRAMED,
    PATTERN_FRAMED,
    GLIMMERING;

    @Override
    public String getName() {
        return name().toLowerCase(Locale.ROOT);
    }
}
