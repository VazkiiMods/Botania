package vazkii.botania.api.state.enums;

import net.minecraft.util.IStringSerializable;

import java.util.Locale;

public enum PrismarineVariant implements IStringSerializable {
    PRISMARINE,
    PRISMARINE_BRICKS,
    DARK_PRISMARINE;

    @Override
    public String getName() {
        return name().toLowerCase(Locale.ROOT);
    }
}
