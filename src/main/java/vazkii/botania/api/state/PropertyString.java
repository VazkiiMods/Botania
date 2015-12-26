package vazkii.botania.api.state;

import net.minecraftforge.common.property.IUnlistedProperty;
import vazkii.botania.api.subtile.SubTileEntity;

public class PropertyString implements IUnlistedProperty<String> {

    private final String name;

    public PropertyString(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean isValid(String value) {
        return true;
    }

    @Override
    public Class<String> getType() {
        return String.class;
    }

    @Override
    public String valueToString(String value) {
        return value;
    }
}
