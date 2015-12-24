package vazkii.botania.api.state;

import net.minecraftforge.common.property.IUnlistedProperty;
import vazkii.botania.api.subtile.SubTileEntity;

public class PropertyClass implements IUnlistedProperty<Class<? extends SubTileEntity>> {

    private final String name;

    public PropertyClass(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean isValid(Class<? extends SubTileEntity> value) {
        return true;
    }

    @Override
    public Class<Class<? extends SubTileEntity>> getType() {
        // todo is there a safe way?
		Class clazz = Class.class;
        return clazz;
    }

    @Override
    public String valueToString(Class<? extends SubTileEntity> value) {
        return value.getName();
    }
}
