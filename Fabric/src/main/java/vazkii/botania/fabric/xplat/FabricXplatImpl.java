package vazkii.botania.fabric.xplat;

import net.fabricmc.loader.api.FabricLoader;

import vazkii.botania.xplat.IXplatAbstractions;

public class FabricXplatImpl implements IXplatAbstractions {
	@Override
	public boolean isModLoaded(String modId) {
		return FabricLoader.getInstance().isModLoaded(modId);
	}
}
