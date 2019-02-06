package vazkii.botania.client.render;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public interface IModelRegister {
	/**
	 * Do whatever needs to be done to register models
	 * For Blocks, statemapper registration needs to be done BEFORE itemblock registration
	 * Exceptions to that rule are noted
	 */
	@OnlyIn(Dist.CLIENT)
	void registerModels();
}
