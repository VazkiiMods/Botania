package vazkii.botania.api.state.enums;

import net.minecraft.util.IStringSerializable;

import java.util.Locale;

public enum AltGrassType implements IStringSerializable {
    DRY,
    GOLDEN,
    VIVID,
    SCORCHED,
    INFUSED,
    MUTATED;

    @Override
    public String getName() {
        return this.name().toLowerCase(Locale.ROOT);
    }
}
