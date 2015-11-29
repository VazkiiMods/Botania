package vazkii.botania.api.state.enums;

import net.minecraft.util.IStringSerializable;

import java.util.Locale;

public enum AlfPortalState implements IStringSerializable {
    OFF,
    ON_Z,
    ON_X;

    @Override
    public String getName() {
        return name().toLowerCase(Locale.ROOT);
    }
}
