/**
 * This class was created by <williewillus>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * <p>
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.api;

import com.google.common.collect.Maps;
import net.minecraft.client.renderer.model.ModelResourceLocation;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import vazkii.botania.api.item.IFloatingFlower;

import java.util.Collections;
import java.util.Map;

/**
 * Class for API calls that must be made clientside
 */
@OnlyIn(Dist.CLIENT)
public final class BotaniaAPIClient {

	private static final Map<IFloatingFlower.IslandType, ResourceLocation> islandTypeModels = Maps.newHashMap();

	private BotaniaAPIClient() {
	}

	/**
	 * Registers your model for island type islandType here.
	 * Call this during {@link net.minecraftforge.client.event.ModelRegistryEvent}.
	 *
	 * @param islandType The islandtype to register
	 * @param model      The model, only {@link ResourceLocation} allowed, no {@link ModelResourceLocation} allowed.
	 */
	public static void registerIslandTypeModel(IFloatingFlower.IslandType islandType, ResourceLocation model) {
		islandTypeModels.put(islandType, model);
	}

	/**
	 * @return An immutable and live view of the registered island type model map
	 */
	public static Map<IFloatingFlower.IslandType, ResourceLocation> getRegisteredIslandTypeModels() {
		return Collections.unmodifiableMap(islandTypeModels);
	}

}
