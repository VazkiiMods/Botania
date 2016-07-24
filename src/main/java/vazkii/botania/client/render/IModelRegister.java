package vazkii.botania.client.render;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public interface IModelRegister {
    /**
     * Do whatever needs to be done to register models
     */
    @SideOnly(Side.CLIENT)
    void registerModels();
}
