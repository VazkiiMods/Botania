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
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import vazkii.botania.api.item.IFloatingFlower;
import vazkii.botania.api.subtile.SubTileEntity;

import java.util.Collections;
import java.util.Map;

/**
 * Class for API calls that must be made clientside
 */
@SideOnly(Side.CLIENT)
public final class BotaniaAPIClient {

	private static final Map<String, ModelResourceLocation> subtileBlockModels = Maps.newHashMap();
	private static final Map<String, ModelResourceLocation> subtileItemModels = Maps.newHashMap();
	private static final Map<IFloatingFlower.IslandType, ModelResourceLocation> islandTypeModels = Maps.newHashMap();

	private BotaniaAPIClient() {
	}

	/**
	 * Register your model for the given subtile class here.
	 * Call this during {@code ModelRegistryEvent}. Calling it anytime after blockModels have already baked does not guarantee that your model will work.
	 * Your model json must specify key "tintindex" in all the faces it wants tint applied.
	 * Tint is applied whenever a player recolors the flower using floral dye
	 *
	 * @param subTileName The String ID of the subtile
	 * @param model       A path to a blockstate json and variant to be used for this subtile
	 * @param itemModel   A path to a blockstate json and variant to be used for this subtile's item form
	 */
	public static void registerSubtileModel(String subTileName, ModelResourceLocation model, ModelResourceLocation itemModel) {
		subtileBlockModels.put(subTileName, model);
		subtileItemModels.put(subTileName, itemModel);
	}

	/**
	 * Register your model for the given subtile class here.
	 * Call this DURING PREINIT.
	 * Your model json must specify key "tintindex" in all the faces it wants tint applied.
	 * Tint is applied whenever a player recolors the flower using floral dye
	 *
	 * @param subTileName The String ID of the subtile
	 * @param model       A path to a blockstate json and variant to be used the block. The item model will be drawn from the same blockstate json, from variant "inventory"
	 */
	public static void registerSubtileModel(String subTileName, ModelResourceLocation model) {
		registerSubtileModel(subTileName, model, new ModelResourceLocation(model.getNamespace() + ":" + model.getPath(), "inventory"));
	}

	// Convenience overloads for the above two calls
	public static void registerSubtileModel(Class<? extends SubTileEntity> clazz, ModelResourceLocation model) {
		registerSubtileModel(BotaniaAPI.getSubTileStringMapping(clazz), model);
	}

	public static void registerSubtileModel(Class<? extends SubTileEntity> clazz, ModelResourceLocation model, ModelResourceLocation itemModel) {
		registerSubtileModel(BotaniaAPI.getSubTileStringMapping(clazz), model, itemModel);
	}

	/**
	 * @return An immutable and live view of the registered subtile block model map
	 */
	public static Map<String, ModelResourceLocation> getRegisteredSubtileBlockModels() {
		return Collections.unmodifiableMap(subtileBlockModels);
	}

	/**
	 * @return An immutable and live view of the registered subtile item model map
	 */
	public static Map<String, ModelResourceLocation> getRegisteredSubtileItemModels() {
		return Collections.unmodifiableMap(subtileItemModels);
	}

	/**
	 * Registers your model for island type islandType here.
	 * Call this during {@code ModelRegistryEvent}.
	 *
	 * @param islandType The islandtype to register
	 * @param model      The variant within a blockstate json to use as the islandtype's model
	 */
	public static void registerIslandTypeModel(IFloatingFlower.IslandType islandType, ModelResourceLocation model) {
		islandTypeModels.put(islandType, model);
	}

	/**
	 * @return An immutable and live view of the registered island type model map
	 */
	public static Map<IFloatingFlower.IslandType, ModelResourceLocation> getRegisteredIslandTypeModels() {
		return Collections.unmodifiableMap(islandTypeModels);
	}

}
