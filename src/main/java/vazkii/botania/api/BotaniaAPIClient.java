/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.api;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.Lazy;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import org.apache.logging.log4j.LogManager;

import vazkii.botania.api.item.IFloatingFlower;

import java.util.Collections;
import java.util.Map;

/**
 * Class for API calls that must be made clientside
 */
@Environment(EnvType.CLIENT)
public interface BotaniaAPIClient {
	Lazy<BotaniaAPIClient> INSTANCE = new Lazy<>(() -> {
		try {
			return (BotaniaAPIClient) Class.forName("vazkii.botania.client.impl.BotaniaAPIClientImpl").newInstance();
		} catch (ReflectiveOperationException e) {
			LogManager.getLogger().warn("Unable to find BotaniaAPIClientImpl, using a dummy");
			return new BotaniaAPIClient() {};
		}
	});

	static BotaniaAPIClient instance() {
		return INSTANCE.get();
	}

	/**
	 * Registers your model for island type islandType here.
	 * Call this during {@link net.minecraftforge.client.event.ModelRegistryEvent}.
	 *
	 * @param islandType The islandtype to register
	 * @param model      The model, only {@link ResourceLocation} allowed, no {@link ModelResourceLocation} allowed.
	 */
	default void registerIslandTypeModel(IFloatingFlower.IslandType islandType, Identifier model) {}

	/**
	 * @return An immutable and live view of the registered island type model map
	 */
	default Map<IFloatingFlower.IslandType, Identifier> getRegisteredIslandTypeModels() {
		return Collections.emptyMap();
	}

	/**
	 * Draw a mana bar on the screen
	 */
	default void drawSimpleManaHUD(MatrixStack ms, int color, int mana, int maxMana, String name) {}

	/**
	 * Performs the effects of {@link #drawSimpleManaHUD}, then renders {@code bindDisplay}, and a checkmark or x-mark
	 * dependong on the value of {@code properlyBound}.
	 */
	default void drawComplexManaHUD(MatrixStack ms, int color, int mana, int maxMana, String name, ItemStack bindDisplay, boolean properlyBound) {}
}
