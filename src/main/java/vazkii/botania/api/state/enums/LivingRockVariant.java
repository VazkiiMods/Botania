package vazkii.botania.api.state.enums;

import net.minecraft.util.IStringSerializable;

import java.util.Locale;

public enum LivingRockVariant implements IStringSerializable {
    DEFAULT,
    BRICK,
    MOSSY_BRICK,
    CRACKED_BRICK,
    CHISELED_BRICK;

    @Override
    public String getName() {
        return name().toLowerCase(Locale.ROOT);
    }
}
