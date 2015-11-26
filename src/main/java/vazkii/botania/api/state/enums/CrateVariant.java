package vazkii.botania.api.state.enums;

import net.minecraft.util.IStringSerializable;

import java.util.Locale;

public enum CrateVariant implements IStringSerializable {
    OPEN,
    CRAFTY;

    @Override
    public String getName() {
        return name().toLowerCase(Locale.ROOT);
    }
}
