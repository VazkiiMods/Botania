package vazkii.botania.common.integration.coloredlights;

/*
 * This interface is use for wrappers for compatibility with the colored lights mod.
 * It has one version for colored lights, and one version which leaves the light values unmodified for vanilla
 */
public interface ILightHelper {

	public int makeRGBLightValue(float r, float g, float b, int currentLightValue);
	public int getPackedColor(int meta, int light);
}
