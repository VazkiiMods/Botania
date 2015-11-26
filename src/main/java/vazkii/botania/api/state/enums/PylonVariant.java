package vazkii.botania.api.state.enums;

import net.minecraft.util.IStringSerializable;

import java.util.Locale;

public enum PylonVariant implements IStringSerializable {
    MANA,
    NATURA,
    GAIA;

    @Override
    public String getName() {
        return name().toLowerCase(Locale.ROOT);
    }
}
