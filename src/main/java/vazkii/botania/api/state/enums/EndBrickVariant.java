package vazkii.botania.api.state.enums;

import net.minecraft.util.IStringSerializable;

import java.util.Locale;

public enum EndBrickVariant implements IStringSerializable {
    END_STONE_BRICKS,
    CHISELED_END_STONE_BRICKS,
    ENDER_BRICKS,
    PILLAR_ENDER_BRICKS;

    @Override
    public String getName() {
        return name().toLowerCase(Locale.ROOT);
    }
}
