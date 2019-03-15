package vazkii.botania.common.world;

import net.minecraft.world.gen.feature.IFeatureConfig;
import vazkii.botania.common.core.handler.ConfigHandler;

public class MysticalFlowerConfig implements IFeatureConfig {
    public int getPatchSize() {
        return ConfigHandler.COMMON.flowerPatchSize.get();
    }

    public int getPatchCount() {
        return ConfigHandler.COMMON.flowerQuantity.get();
    }

    public int getPatchDensity() {
        return ConfigHandler.COMMON.flowerDensity.get();
    }

    public int getPatchChance() {
        return ConfigHandler.COMMON.flowerPatchChance.get();
    }

    public double getTallChance() {
        return ConfigHandler.COMMON.flowerTallChance.get();
    }

}
