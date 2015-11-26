package vazkii.botania.api.state.enums;

import net.minecraft.util.IStringSerializable;

import java.util.Locale;

public enum SpreaderVariant implements IStringSerializable {
    MANA,
    REDSTONE,
    ELVEN,
    GAIA;

    @Override
    public String getName() {
        return name().toLowerCase(Locale.ROOT);
    }
}
