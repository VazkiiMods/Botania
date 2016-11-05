package vazkii.botania.client.render;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public interface IModelRegister {
	/**
	 * Do whatever needs to be done to register models
	 * For Blocks, statemapper registration needs to be done BEFORE itemblock registration
	 * Exceptions to that rule are noted
	 */
	@SideOnly(Side.CLIENT)
	void registerModels();
}
