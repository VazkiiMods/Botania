package vazkii.botania.forge.xplat;

import net.minecraftforge.fml.ModList;
import vazkii.botania.xplat.IXplatAbstractions;

public class ForgeXplatImpl implements IXplatAbstractions {
    @Override
    public boolean isModLoaded(String modId) {
        return ModList.get().isLoaded(modId);
    }
}
