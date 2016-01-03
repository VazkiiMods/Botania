/**
 * This class was created by <williewillus>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.api.state;

import net.minecraftforge.common.property.IUnlistedProperty;

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
