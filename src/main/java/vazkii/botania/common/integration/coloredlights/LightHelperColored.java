package vazkii.botania.common.integration.coloredlights;

/*
 * This is an implementation of ILightHelper for when the colored lights mod is installed
 */
public class LightHelperColored implements ILightHelper{

	@Override
	public int makeRGBLightValue(float r, float g, float b,
			int currentLightValue) {
		return ColoredLightHelper.makeRGBLightValue(r, g, b, currentLightValue);
	}

	@Override
	public int getPackedColor(int meta, int light) {
		return ColoredLightHelper.getPackedColor(meta, light);
	}

}
