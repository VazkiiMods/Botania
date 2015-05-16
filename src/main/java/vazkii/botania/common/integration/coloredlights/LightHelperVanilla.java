package vazkii.botania.common.integration.coloredlights;

/*
 * This is an implementation of ILightHelper for when the colored lights mod isn't installed
 */
public class LightHelperVanilla implements ILightHelper{

	@Override
	public int makeRGBLightValue(float r, float g, float b,
			int currentLightValue) {
		return currentLightValue;
	}

	@Override
	public int getPackedColor(int meta, int light) {
		return light;
	}

}
