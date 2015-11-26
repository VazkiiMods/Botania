package vazkii.botania.api.state.enums;

import net.minecraft.util.IStringSerializable;

import java.util.Locale;

public enum StorageVariant implements IStringSerializable {
    MANASTEEL,
    TERRASTEEL,
    ELEMENTIUM,
    MANA_DIAMOND,
    DRAGONSTONE;

    @Override
    public String getName() {
        return name().toLowerCase(Locale.ROOT);
    }
}
