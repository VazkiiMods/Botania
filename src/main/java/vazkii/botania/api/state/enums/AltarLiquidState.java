package vazkii.botania.api.state.enums;

import net.minecraft.util.IStringSerializable;

import java.util.Locale;

public enum AltarLiquidState implements IStringSerializable {
    NONE,
    WATER,
    LAVA;

    @Override
    public String getName() {
        return name().toLowerCase(Locale.ROOT);
    }
}
