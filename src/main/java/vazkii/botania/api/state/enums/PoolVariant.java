package vazkii.botania.api.state.enums;

import net.minecraft.util.IStringSerializable;

import java.util.Locale;

public enum PoolVariant implements IStringSerializable {
    DEFAULT,
    CREATIVE,
    DILUTED,
    FABULOUS;

    @Override
    public String getName() {
        return name().toLowerCase(Locale.ROOT);
    }
}
